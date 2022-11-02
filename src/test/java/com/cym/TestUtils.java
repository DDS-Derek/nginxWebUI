package com.cym;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;

public class TestUtils {

	@Test
	public void test() throws IOException {

	}

	public static void main(String[] args) {
		String username = "18030546255";
		String uid = "7974";
		String key = "wiqB2gaCMzP83Z8wh37L00uSWmgDcIvX";

		String t = System.currentTimeMillis() + "";
		String k = SecureUtil.md5(uid + key + username + t);
		String sign = SecureUtil.md5(k);
		String action = "login";

		Map<String, Object> map = new HashMap<>();
		map.put("username", username);
		map.put("uid", uid);
		map.put("t", t);
		map.put("k", k);
		map.put("sign", sign);
		map.put("action", action);
		
		System.out.println(map);
		
		String rs = HttpUtil.get("https://www.freecdn.pw:8092/cdn/api/login.php", map);
		
		System.out.println(rs);
	}
}
