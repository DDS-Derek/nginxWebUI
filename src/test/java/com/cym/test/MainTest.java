package com.cym.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cym.NginxWebUI;
import com.cym.controller.adminPage.ConfController;
import com.cym.utils.MessageUtils;
import com.cym.utils.TimeExeUtils;

import cn.craccd.sqlHelper.utils.SqlHelper;
import cn.hutool.core.util.RuntimeUtil;

@SpringBootTest(classes = NginxWebUI.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainTest {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SqlHelper sqlHelper;
	@Autowired
	MessageUtils m;
	@Autowired
	ConfController confController;
	@Autowired
	TimeExeUtils exeUtils;

	@Test
	public void testStartUp() throws InterruptedException, IOException {

		List<String> list = RuntimeUtil.execForLines("wmic process get commandline,ProcessId /value");
		List<String> pids = new ArrayList<String>();

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).contains("java") && list.get(i).contains("nginxWebUI") && list.get(i).contains(".jar")) {
				String pid = list.get(i + 2).split("=")[1];
				pids.add(pid);
			}
		}

		for (String pid : pids) {
			logger.info("杀掉进程:" + pid);
			RuntimeUtil.exec("taskkill /im " + pid + " /f");
		}

	}

//	public static void main(String[] args) {
//		try {
//			// 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
//			// 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
//			Credential cred = new Credential("AKIDV2RipjPq47xb3HNEvyvhFi2GRZoyxjIe", "EI7k8G9oDWdfXOFxV1Tn4KUhopiZK0fM");
//			// 实例化一个http选项，可选的，没有特殊需求可以跳过
//			HttpProfile httpProfile = new HttpProfile();
//			httpProfile.setEndpoint("dnspod.tencentcloudapi.com");
//			// 实例化一个client选项，可选的，没有特殊需求可以跳过
//			ClientProfile clientProfile = new ClientProfile();
//			clientProfile.setHttpProfile(httpProfile);
//			// 实例化要请求产品的client对象,clientProfile是可选的
//			DnspodClient client = new DnspodClient(cred, "", clientProfile);
//			// 实例化一个请求对象,每个接口都会对应一个request对象
//			CreateRecordRequest req = new CreateRecordRequest();
//			req.setDomain("scaeme.cn");
//			req.setSubDomain("acme");
//			req.setRecordType("TXT");
//			req.setRecordLine("默认");
//			req.setValue("EI7k8G9oDWdfXOFxV1Tn4KUhopiZK0fM");
//			// 返回的resp是一个CreateRecordResponse的实例，与请求对象对应
//			CreateRecordResponse resp = client.CreateRecord(req);
//			// 输出json格式的字符串回包
//			System.out.println(CreateRecordResponse.toJsonString(resp));
//		} catch (TencentCloudSDKException e) {
//			System.out.println(e.toString());
//		}
//	}

}
