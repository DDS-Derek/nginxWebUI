package com.cym.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.noear.solon.annotation.Inject;
import org.noear.solon.extend.quartz.Quartz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cym.controller.adminPage.RemoteController;
import com.cym.model.Remote;
import com.cym.service.RemoteService;
import com.cym.service.SettingService;
import com.cym.utils.MessageUtils;
import com.cym.utils.SendMailUtils;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

// 检查远程服务器
@Quartz(cron7x = "0/30 * * * * ?")
public class NginxTasks implements Runnable {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Inject("server.port")
	String port;
	@Inject
	MessageUtils m;
	@Inject
	SendMailUtils sendMailUtils;
	@Inject
	RemoteController remoteController;
	@Inject
	RemoteService remoteService;
	@Inject
	SettingService settingService;

	@Override
	public void run() {

		String lastNginxSend = settingService.get("lastNginxSend");
		String mail = settingService.get("mail");
		String nginxMonitor = settingService.get("nginxMonitor");
		String mailInterval = settingService.get("mail_interval");
		if (StrUtil.isEmpty(mailInterval)) {
			mailInterval = "30";
		}

		if ("true".equals(nginxMonitor) && StrUtil.isNotEmpty(mail)
				&& (StrUtil.isEmpty(lastNginxSend) || System.currentTimeMillis() - Long.parseLong(lastNginxSend) > TimeUnit.MINUTES.toMillis(Integer.parseInt(mailInterval)))) {
			List<String> names = new ArrayList<>();

			// 测试远程
			List<Remote> remoteList = remoteService.getMonitorRemoteList();
			for (Remote remote : remoteList) {
				try {
					String json = HttpUtil.get(remote.getProtocol() + "://" + remote.getIp() + ":" + remote.getPort() + "/adminPage/remote/version?creditKey=" + remote.getCreditKey(), 1000);
					Map<String, Object> map = JSONUtil.toBean(json, new TypeReference<Map<String, Object>>() {
					}.getType(), false);

					if ((Integer) map.get("nginx") == 0 && remote.getMonitor() == 1) {
						names.add(remote.getDescr() + "[" + remote.getIp() + ":" + remote.getPort() + "]");
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);

					names.add(remote.getDescr() + "[" + remote.getIp() + ":" + remote.getPort() + "]");
				}
			}

			// 测试本地
			if ("1".equals(settingService.get("monitorLocal"))) {
				Map<String, Object> map = remoteController.version();
				if ((Integer) map.get("nginx") == 0) {
					names.add(0, m.get("remoteStr.local") + "[127.0.0.1:" + port + "]");
				}
			}

			if (names.size() > 0) {
				sendMailUtils.sendMailSmtp(mail, m.get("mailStr.nginxFail"), m.get("mailStr.nginxTips") + StrUtil.join(" ", names));
				settingService.set("lastNginxSend", String.valueOf(System.currentTimeMillis()));
			}
		}

	}
}