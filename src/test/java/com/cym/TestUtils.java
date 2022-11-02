package com.cym;

import java.io.IOException;

import org.junit.Test;

import cn.hutool.crypto.SecureUtil;

public class TestUtils {

	@Test
	public void test() throws IOException {

	}

	public static void main(String[] args) {
		String str = SecureUtil.md5("7974" + "%OHr1IIf6Gh19SXe2zI#y17ag$$%dw9p" + "18030546255" + "1667393791844");
	
		System.out.println(str);
		
		System.out.println(SecureUtil.md5(str));
	}
}
