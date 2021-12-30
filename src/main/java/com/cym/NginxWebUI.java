package com.cym;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.noear.solon.Solon;
import org.noear.solon.schedule.annotation.EnableScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cym.utils.JarUtil;
import com.cym.utils.SystemTool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.RuntimeUtil;

@EnableScheduling
public class NginxWebUI {
	static Logger logger = LoggerFactory.getLogger(NginxWebUI.class);

	public static void main(String[] args) {
		try {
			// 尝试杀掉旧版本
			killSelf();

			// 删掉多余的jar
			removeJar();

			// 展示logo
			showLogo();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		Solon.start(NginxWebUI.class, args, app -> {
			app.onError(e -> e.printStackTrace());
			app.before(c -> {
				c.pathNew(c.path().replace("//", "/"));
			});

			app.enableWebSocket(true);

			app.onEvent(freemarker.template.Configuration.class, cfg -> {
				cfg.setSetting("classic_compatible", "true");
				cfg.setSetting("number_format", "0.##");
			});

		});
	}

	private static void showLogo() throws IOException {
		ClassPathResource resource = new ClassPathResource("banner.txt");
		BufferedReader reader = resource.getReader(Charset.forName("utf-8"));
		String str = "";
		// 使用readLine() 比较方便的读取一行
		while (null != (str = reader.readLine())) {
			System.out.println(str);
		}
		reader.close();// 关闭流
	}

	public static void killSelf() {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		String myPid = runtimeMXBean.getName().split("@")[0];

		List<String> list = new ArrayList<String>();
		List<String> pids = new ArrayList<String>();

		if (SystemTool.isWindows()) {
			list = RuntimeUtil.execForLines("wmic process get commandline,ProcessId /value");
			pids = new ArrayList<String>();

			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).contains("java") && list.get(i).contains("nginxWebUI") && list.get(i).contains(".jar")) {
					String pid = list.get(i + 2).split("=")[1];
					if (!pid.equals(myPid)) {
						pids.add(pid);
					}
				}
			}
		} else if (SystemTool.isLinux()) {
			list = RuntimeUtil.execForLines("ps -ef");
			for (String line : list) {
				if (line.contains("java") && line.contains("nginxWebUI") && line.contains(".jar")) {
					String[] strs = line.split("\\s+");
					String pid = strs[1];

					if (!pid.equals(myPid)) {
						logger.info("找到进程:" + line);
						pids.add(pid);
					}
				}
			}
		}

		for (String pid : pids) {
			logger.info("杀掉进程:" + pid);
			if (SystemTool.isWindows()) {
				RuntimeUtil.exec("taskkill /im " + pid + " /f");
			} else if (SystemTool.isLinux()) {
				RuntimeUtil.exec("kill -9 " + pid);
			}
		}

	}

	private static void removeJar() {
		File[] list = new File(JarUtil.getCurrentFilePath()).getParentFile().listFiles();
		for (File file : list) {
			if (file.getName().startsWith("nginxWebUI") && file.getName().endsWith(".jar") && !file.getPath().equals(JarUtil.getCurrentFilePath())) {
				FileUtil.del(file);
				logger.info("删除文件:" + file);
			}
		}
	}

}
