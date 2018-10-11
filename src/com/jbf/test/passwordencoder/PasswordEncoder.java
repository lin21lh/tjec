/************************************************************
 * 类名：PasswordEncoder.java
 *
 * 类别：测试
 * 功能：说明系统的加密方式
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.test.passwordencoder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class PasswordEncoder {
	public static void main(String args[]) {

		Map<String, String> usernames = new HashMap<String, String>();
		//usernames.put("lisan", "lisan"); // 用户名，密码
		usernames.put("test", "123");
		usernames.put("1112321312", "123");
		usernames.put("123", "123");
		usernames.put("12323", "123");
		usernames.put("zhang", "123");
		usernames.put("admin", "123456");
		usernames.put("lisan", "123");
		usernames.put("li", "456");
		usernames.put("ddd", "123");

		Md5PasswordEncoder e = new Md5PasswordEncoder();
		e.setEncodeHashAsBase64(false);
		for (String user : usernames.keySet()) {
			System.out.println(user + "\t\t"
					+ e.encodePassword(usernames.get(user), user)); // 使用username作为salt来加密password
		}

	}
}
