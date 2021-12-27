package com.cym.task;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.noear.solon.extend.quartz.Quartz;

import com.cym.config.InitConfig;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;

// 删除7天前的备份
@Quartz(cron7x = "0 0 0 * * ?")
public class DelBak implements Runnable {
	@Override
	public void run() {

		long time = System.currentTimeMillis();
		File dir = new File(InitConfig.home + "bak/");
		if (dir.exists()) {
			for (File file : dir.listFiles()) {
				if (file.getName().contains("nginx.conf.") && (file.getName().endsWith(".zip") || file.getName().endsWith(".bak"))) {
					String dateStr = file.getName().replace("nginx.conf.", "").replace(".zip", "").replace(".bak", "").split("_")[0];
					DateTime date = null;
					if (dateStr.length() != 10) {
						FileUtil.del(file);
					} else {
						date = DateUtil.parse(dateStr, "yyyy-MM-dd");
						if (time - date.getTime() > TimeUnit.DAYS.toMillis(8)) {
							FileUtil.del(file);
						}
					}
				}
			}
		}
	}
}