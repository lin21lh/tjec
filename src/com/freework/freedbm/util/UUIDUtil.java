package com.freework.freedbm.util;

public class UUIDUtil {
	public static final String SIGN = "-";

	public static String UUIDVsSign(String uuid){
		StringBuffer sb = new StringBuffer(uuid);
		sb.insert(8, SIGN).insert(17,SIGN).insert(22, SIGN).insert(31, SIGN);
		return sb.toString();
	}
	
	public static String UUIDNoSign(String uuid){
		return uuid.replace(SIGN, "");
	}
}
