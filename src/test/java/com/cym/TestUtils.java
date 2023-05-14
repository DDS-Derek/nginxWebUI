package com.cym;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;

public class TestUtils {

	public static void main(String[] args) {

		String pass = "abcde12345";
		for (int i = 0; i < 100000; i++) {
			pass += "abcde12345";
		}
		
		FileUtil.writeString(pass, "d:\\1.txt", "UTF-8");

		Map<String, Object> map = new HashMap<>();
		map.put("name", "admin");
		map.put("pass", pass);

		String rs = HttpUtil.post("http://127.0.0.1:8080/adminPage/login/login", map);

		System.err.println(rs);
	}

}
