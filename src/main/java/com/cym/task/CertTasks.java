package com.cym.task;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.noear.solon.annotation.Inject;
import org.noear.solon.extend.quartz.Quartz;

import com.cym.controller.adminPage.CertController;
import com.cym.controller.adminPage.ConfController;
import com.cym.model.Cert;
import com.cym.sqlhelper.utils.SqlHelper;

// 续签证书
@Quartz(cron7x = "0 0 2 * * ?")
public class CertTasks implements Runnable {
	@Inject
	SqlHelper sqlHelper;
	@Inject
	CertController certController;
	@Inject
	ConfController confController;
	@Override
	public void run() {
		List<Cert> certList = sqlHelper.findAll(Cert.class);

		// 检查需要续签的证书
		long time = System.currentTimeMillis();
		for (Cert cert : certList) {
			// 大于50天的续签
			if (cert.getMakeTime() != null && cert.getAutoRenew() == 1 && time - cert.getMakeTime() > TimeUnit.DAYS.toMillis(50)) {
				certController.apply(cert.getId(), "renew");

				// 重载nginx使证书生效
				confController.reload(null, null, null);
			}
		}
	}
}