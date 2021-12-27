package com.cym.task;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.noear.solon.annotation.Inject;
import org.noear.solon.extend.quartz.Quartz;

import com.cym.controller.adminPage.ConfController;
import com.cym.model.Http;
import com.cym.service.HttpService;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;

// 分隔日志,每天
@Quartz(cron7x = "0 55 23 * * ?")
public class DiviLog implements Runnable {
	@Inject
	HttpService httpService;
	@Inject
	ConfController confController;
	@Override
	public void run() {
		Http access = httpService.getName("access_log");
		if (access != null) {
			cutLog(access);
		}

		Http error = httpService.getName("error_log");
		if (access != null) {
			cutLog(error);
		}

	}

	private void cutLog(Http http) {
		String path = http.getValue();

		if (StrUtil.isNotEmpty(path)) {
			// 去掉格式化
			path = path.split(" ")[0];
			if (FileUtil.exist(path)) {
				String date = DateUtil.format(new Date(), "yyyy-MM-dd");
				// 分隔日志
				File dist = new File(path + "." + date);
				FileUtil.move(new File(path), dist, true);
				ZipUtil.zip(dist.getPath(), dist.getPath() + ".zip", false); // 打包
				FileUtil.del(dist); // 删除原文件
				// 重载Nginx产生新的文件
				confController.reload(null, null, null);

				// 删除多余文件
				long time = System.currentTimeMillis();

				File dir = new File(path).getParentFile();
				for (File file : dir.listFiles()) {
					if (file.getName().contains(new File(path).getName()) && file.getName().endsWith(".zip")) {
						String[] array = file.getName().split("[.]");
						String dateStr = array[array.length - 2];
						DateTime dateTime = DateUtil.parse(dateStr, "yyyy-MM-dd");
						if (time - dateTime.getTime() > TimeUnit.DAYS.toMillis(8)) {
							FileUtil.del(file);
						}
					}
				}
			}
		}

	}

}