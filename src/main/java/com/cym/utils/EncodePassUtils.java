package com.cym.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

public class EncodePassUtils {

	public static String key = "nginxWebUIKey";

	public static String encode(String pass) {

		if (StrUtil.isNotEmpty(pass)) {
			pass = SecureUtil.des(key.getBytes()).encryptBase64(pass);
			pass = SecureUtil.des(key.getBytes()).encryptBase64(pass);
			pass += SecureUtil.md5(key);
		}

		return pass;
	}

	public static String decode(String pass) {

		if (StrUtil.isNotEmpty(pass)) {
			pass = pass.replace(SecureUtil.md5(key), "");
			pass = SecureUtil.des(key.getBytes()).decryptStr(pass);
			pass = SecureUtil.des(key.getBytes()).decryptStr(pass);
		}

		return pass;
	}

	public static void main(String[] args) {
		System.out.println(encode("12345678"));
		System.out.println(decode(encode("12345678")));
	}
}
