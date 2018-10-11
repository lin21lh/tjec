/**
 * Copyright @copy; 2014-2015 All rights reserved.
 */
package com.jbf.common.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * @author zhaocz
 * 
 */
public class StringUtil {

    /**
     * 首字母变小写
     */
    public static String firstCharToLowerCase(String str) {
        Character firstChar = str.charAt(0);
        String tail = str.substring(1);
        str = Character.toLowerCase(firstChar) + tail;
        return str;
    }

    /**
     * 首字母变大写
     */
    public static String firstCharToUpperCase(String str) {
        Character firstChar = str.charAt(0);
        String tail = str.substring(1);
        str = Character.toUpperCase(firstChar) + tail;
        return str;
    }

    /**
     * 字符串为 null 或者为 "" 时返回 true
     */
    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim()) ? true : false;
    }


    /**
     * 为null时返回""否则返回字符串
     * add by XinPeng 2015年4月23日8:34:17
     */
    public static String stringConvert(Object obj) {
        return obj == null ?"":obj.toString();
    }
    /**
     * 字符串不为 null 而且不为 "" 时返回 true
     */
    public static boolean isNotBlank(String str) {
        return str == null || "".equals(str.trim()) ? false : true;
    }

    public static boolean isNotBlank(String... strings) {
        if (strings == null)
            return false;
        for (String str : strings)
            if (str == null || "".equals(str.trim()))
                return false;
        return true;
    }

    public static boolean isNotNull(Object... paras) {
        if (paras == null)
            return false;
        for (Object obj : paras)
            if (obj == null)
                return false;
        return true;
    }

    /**
     * 替换回车、换行、制表符
     */
    public static String replaceEnter(String str, String replacement) {
        Pattern p = Pattern.compile("\\t|\r|\n");
        Matcher m = p.matcher(str);
        String after = m.replaceAll(replacement);
        return after;
    }

    /**
     * 转换为Double类型
     */
    public static Double toDouble(Object val) {
        if (val == null) {
            return 0D;
        }
        try {
            return Double.valueOf(val.toString().trim());
        }
        catch (Exception e) {
            return 0D;
        }
    }

    /**
     * 转换为Float类型
     */
    public static Float toFloat(Object val) {
        return toDouble(val).floatValue();
    }

    /**
     * 转换为Long类型
     */
    public static Long toLong(Object val) {
        return toDouble(val).longValue();
    }

    /**
     * 转换为Integer类型
     */
    public static Integer toInteger(Object val) {
        return toLong(val).intValue();
    }
    
    /**
     * 剪切指定长度字符串
     * @Title: subStr 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param str
     * @param @param subSLength（字节长度）
     * @param @return
     * @param @throws UnsupportedEncodingException 设定文件 
     * @return String 返回类型 
     */
	public static String subStr(String str, int subSLength)
			throws UnsupportedEncodingException {
		if (str == null)
			return "";
		else {
			int tempSubLength = subSLength;// 截取字节数
			String subStr = str.substring(0,
					str.length() < subSLength ? str.length() : subSLength);// 截取的子串
			int subStrByetsL = subStr.getBytes("GBK").length;// 截取子串的字节长度
			// int subStrByetsL = subStr.getBytes().length;//截取子串的字节长度
			// 说明截取的字符串中包含有汉字
			while (subStrByetsL > tempSubLength) {
				int subSLengthTemp = --subSLength;
				subStr = str.substring(0,
						subSLengthTemp > str.length() ? str.length()
								: subSLengthTemp);
				subStrByetsL = subStr.getBytes("GBK").length;
				// subStrByetsL = subStr.getBytes().length;
			}
			return subStr;
		}
	}
}
