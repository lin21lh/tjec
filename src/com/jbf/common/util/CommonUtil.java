package com.jbf.common.util;

import java.util.UUID;

public class CommonUtil {
	
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	// 32位
	public static String getUUID32() {
		String s = UUID.randomUUID().toString();
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18)
				+ s.substring(19, 23) + s.substring(24);
	}
}
