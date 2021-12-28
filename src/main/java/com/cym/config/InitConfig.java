package com.cym.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cym.model.Basic;
import com.cym.model.Http;
import com.cym.service.BasicService;
import com.cym.service.SettingService;
import com.cym.sqlhelper.utils.JdbcTemplate;
import com.cym.sqlhelper.utils.SqlHelper;
import com.cym.utils.FilePermissionUtil;
import com.cym.utils.JarUtil;
import com.cym.utils.MessageUtils;
import com.cym.utils.NginxUtils;
import com.cym.utils.SystemTool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;

@Component
public class InitConfig {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Inject
	protected MessageUtils m;

	@Inject
	HomeConfig homeConfig;
	
	
	public static String acmeSh;
	public static String acmeShDir;
	public static String home;

	@Inject
	SettingService settingService;
	@Inject
	BasicService basicService;
	@Inject
	SqlHelper sqlHelper;
	@Inject
	JdbcTemplate jdbcTemplate;

	@Init(index = 40)
	public void init() throws IOException {
		
		InitConfig.home = homeConfig.home;
		InitConfig.acmeShDir = homeConfig.home + ".acme.sh/";
		InitConfig.acmeSh = homeConfig.home + ".acme.sh/acme.sh";
		
		if (!FilePermissionUtil.canWrite(new File(home))) {
			logger.error(home + " " + "directory does not have writable permission. Please specify it again.");
			logger.error(home + " " + "目录没有可写权限,请重新指定.");
			System.exit(1);
		}

		// 初始化base值
		Long count = sqlHelper.findAllCount(Basic.class);
		if (count == 0) {
			List<Basic> basics = new ArrayList<Basic>();
			basics.add(new Basic("worker_processes", "auto", 1l));
			basics.add(new Basic("events", "{\r\n" + "    worker_connections  1024;\r\n    accept_mutex on;\r\n" + "}", 2l));
			sqlHelper.insertAll(basics);
		}

		// 初始化http值
		count = sqlHelper.findAllCount(Http.class);
		if (count == 0) {
			List<Http> https = new ArrayList<Http>();
			https.add(new Http("include", "mime.types", 0l));
			https.add(new Http("default_type", "application/octet-stream", 1l));
			sqlHelper.insertAll(https);
		}

		// 释放nginx.conf,mime.types
		if (!FileUtil.exist(home + "nginx.conf")) {
			ClassPathResource resource = new ClassPathResource("nginx.conf");
			FileUtil.writeFromStream(resource.getStream(), home + "nginx.conf");
		}
		if (!FileUtil.exist(home + "mime.types")) {
			ClassPathResource resource = new ClassPathResource("mime.types");
			FileUtil.writeFromStream(resource.getStream(), home + "mime.types");
		}

		// 设置nginx配置文件
		String nginxPath = settingService.get("nginxPath");
		if (StrUtil.isEmpty(nginxPath)) {
			nginxPath = home + "nginx.conf";
			// 设置nginx.conf路径
			settingService.set("nginxPath", nginxPath);
		}

		if (SystemTool.isLinux()) {
			// 释放acme全新包
			ClassPathResource resource = new ClassPathResource("acme.zip");
			InputStream inputStream = resource.getStream();
			FileUtil.writeFromStream(inputStream, InitConfig.home + "acme.zip");
			FileUtil.mkdir(acmeShDir);
			ZipUtil.unzip(home + "acme.zip", acmeShDir);
			FileUtil.del(home + "acme.zip");

			// 修改acme.sh文件
			List<String> res = FileUtil.readUtf8Lines(acmeSh);
			for (int i = 0; i < res.size(); i++) {
				if (res.get(i).contains("DEFAULT_INSTALL_HOME=\"$HOME/.$PROJECT_NAME\"")) {
					res.set(i, "DEFAULT_INSTALL_HOME=\"" + acmeShDir + "\"");
				}
			}

			FileUtil.writeUtf8Lines(res, acmeSh);
			RuntimeUtil.exec("chmod a+x " + acmeSh);

			// 查找ngx_stream_module模块
			if (!basicService.contain("ngx_stream_module.so")) {
				logger.info(m.get("commonStr.ngxStream"));
				List<String> list = RuntimeUtil.execForLines(CharsetUtil.systemCharset(), "find / -name ngx_stream_module.so");
				for (String path : list) {
					if (path.contains("ngx_stream_module.so") && path.length() < 80) {
						Basic basic = new Basic("load_module", path, -10l);
						sqlHelper.insert(basic);
						break;
					}
				}
			}

			// 判断是否是容器中
			if (inDocker()) {
				// 设置nginx执行文件
				settingService.set("nginxExe", "nginx");
			}

			// 尝试启动nginx
			String nginxExe = settingService.get("nginxExe");
			String nginxDir = settingService.get("nginxDir");

			logger.info("nginxIsRun:" + NginxUtils.isRun());
			if (!NginxUtils.isRun() && StrUtil.isNotEmpty(nginxExe) && StrUtil.isNotEmpty(nginxPath)) {
				String cmd = nginxExe + " -c " + nginxPath;

				if (StrUtil.isNotEmpty(nginxDir)) {
					cmd += " -p " + nginxDir;
				}
				logger.info("runCmd:" + cmd);
				RuntimeUtil.execForStr("/bin/sh", "-c", cmd);
			}
		}

	}

	/**
	 * 是否在docker中
	 * 
	 * @return
	 */
	private Boolean inDocker() {
		List<String> rs = RuntimeUtil.execForLines("cat /proc/1/cgroup");
		for (String str : rs) {
//			logger.info(str);
			if (str.contains("docker")) {
				logger.info("I am in docker");
				return true;
			}
			if (str.contains("kubepods")) {
				logger.info("I am in k8s");
				return true;
			}
		}
		logger.info("I am not in docker");
		return false;
	}

}
