package com.cym;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.KeyPair;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.solon.test.HttpTester;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.solon.test.SolonTest;
import org.shredzone.acme4j.Account;
import org.shredzone.acme4j.Authorization;
import org.shredzone.acme4j.Certificate;
import org.shredzone.acme4j.Order;
import org.shredzone.acme4j.Session;
import org.shredzone.acme4j.exception.AcmeException;
import org.shredzone.acme4j.util.CSRBuilder;

import ch.qos.logback.core.status.Status;
import cn.hutool.http.HttpUtil;

@RunWith(SolonJUnit4ClassRunner.class)
@SolonTest(NginxWebUI.class)
public class TestUtils extends HttpTester {
	@Test
	@Ignore
	public void test() throws Exception {

		StringBuilder pass = new StringBuilder(1000);
		for (int i = 0; i < 2000000; i++) {
			pass.append("abcde12345");
		}

		Map<String, Object> map = new HashMap<>();
		map.put("name", "admin");
		map.put("pass", pass.toString());

		System.out.println("pass.length: " + pass.length());

		try {
			String rs = path("/adminPage/login/login").data(map).post();
			System.err.println(rs);
			assert false;
		} catch (IOException e) {
			assert true;
		}
	}

	public static void main(String[] args) {
		StringBuilder pass = new StringBuilder(1000);
		for (int i = 0; i < 2000000; i++) {
			pass.append("abcde12345");
		}

		Map<String, Object> map = new HashMap<>();
		map.put("name", "admin");
		map.put("pass", pass.toString());

		String rs = HttpUtil.post("http://127.0.0.1:8080/adminPage/login/login", map);
		System.err.println(rs);
	}
	
}