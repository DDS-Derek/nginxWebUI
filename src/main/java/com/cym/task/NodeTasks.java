package com.cym.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.noear.solon.annotation.Inject;
import org.noear.solon.extend.cron4j.Cron4j;

import com.cym.model.Upstream;
import com.cym.model.UpstreamServer;
import com.cym.service.SettingService;
import com.cym.service.UpstreamService;
import com.cym.sqlhelper.utils.SqlHelper;
import com.cym.utils.MessageUtils;
import com.cym.utils.SendMailUtils;
import com.cym.utils.TelnetUtils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

// 检查负载节点情况
@Cron4j(cron5x = "0/30 * * * * ?")
public class NodeTasks implements Runnable {	
	@Inject
	SettingService settingService;
	@Inject
	UpstreamService upstreamService;
	@Inject
	SqlHelper sqlHelper;
	@Inject
	SendMailUtils sendMailUtils;
	@Inject
	MessageUtils m;
	
	@Override
	public void run() {
		String lastUpstreamSend = settingService.get("lastUpstreamSend");
		String mail = settingService.get("mail");
		String upstreamMonitor = settingService.get("upstreamMonitor");
		String mailInterval = settingService.get("mail_interval");
		if (StrUtil.isEmpty(mailInterval)) {
			mailInterval = "30";
		}

		if ("true".equals(upstreamMonitor)) {

			List<UpstreamServer> upstreamServers = upstreamService.getAllServer();

			List<String> ips = new ArrayList<>();
			for (UpstreamServer upstreamServer : upstreamServers) {
				if (!TelnetUtils.isRunning(upstreamServer.getServer(), upstreamServer.getPort())) {
					Upstream upstream = sqlHelper.findById(upstreamServer.getUpstreamId(), Upstream.class);
					if (upstream.getMonitor() == 1 && StrUtil.isNotEmpty(mail)
							&& (StrUtil.isEmpty(lastUpstreamSend) || System.currentTimeMillis() - Long.parseLong(lastUpstreamSend) > TimeUnit.MINUTES.toMillis(Integer.parseInt(mailInterval)))) {
						ips.add(upstreamServer.getServer() + ":" + upstreamServer.getPort());
					}
					upstreamServer.setMonitorStatus(0);
				} else {
					upstreamServer.setMonitorStatus(1);
				}

				sqlHelper.updateById(upstreamServer);
			}

			if (ips.size() > 0) {
				String dateStr = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				if (settingService.get("lang") != null && settingService.get("lang").equals("en_US")) {

					SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.ENGLISH);
					dateStr = dateFormat.format(new Date());
				}

				sendMailUtils.sendMailSmtp(mail, m.get("mailStr.upstreamFail"), m.get("mailStr.upstreamTips") + StrUtil.join(" ", ips) + "\r\n" + dateStr);
				settingService.set("lastUpstreamSend", String.valueOf(System.currentTimeMillis()));
			}
		}
	}
}