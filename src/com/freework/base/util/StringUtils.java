package com.freework.base.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

public class StringUtils {
	public final static String MD5(String s) {
		if (s == null)
			return null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();

			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	static final char hexDigits[] = { 'Z', 'H', 'F', '0', 'A', 'E', 'G', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	public static boolean checkVerifyCode(String loginname, String authorizeNo, String verifyCode) {
		if (verifyCode.length() != 46) {
			return false;
		}
		String time = verifyCode.substring(32);
		String oldMd5 = verifyCode.substring(0, 32);
		String newMd5 = StringUtils.MD5(loginname + time + authorizeNo);
		if (newMd5.equals(oldMd5)) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				Date otime = dateFormatter.parse(time);
				long difference = System.currentTimeMillis() - otime.getTime();
				long seconds = difference / 1000;
				return seconds < 1800;
			} catch (ParseException e) {
				e.printStackTrace();
				return false;
			}
		} else
			return false;
	}

	public static String verificationCode(String authorizeNo, String userCode) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String nowTime = dateFormatter.format(new Date());
		return MD5(userCode + nowTime + authorizeNo) + nowTime;
	}

	public static String password(String password, String salt) {
		if (password == null) {
			password = "";
		}

		if ((salt != null) && !"".equals(salt)) {
			password = password + "{" + salt.toString() + "}";
		}
		return md5hex(password);

	}

	public static String replaceBlank(String str) {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		// String str="I am a, I am Hello ok, \n new line ffdsa!";
		// System.out.println("before:"+str);
		Matcher m = p.matcher(str);
		String after = m.replaceAll("");
		return after;
	}

	public static String md5hex(String str) {
		return hex(md5(str));
	}

	public static String shahex(String str) {
		return hex(sha(str));
	}

	public static String hex(byte[] obj) {
		StringBuffer rtn = new StringBuffer();
		for (int i = 0; i < obj.length; i++) {
			String hex = Integer.toHexString(obj[i] & 0xFF);
			if (hex.length() == 1) {
				rtn.append("0");
			}
			rtn.append(hex);
		}
		return rtn.toString();
	}

	public static byte[] md5(String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes());
			return md5.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return (new byte[0]);
	}

	public static byte[] sha(String str) {
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA");
			sha.update(str.getBytes());
			return sha.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return (new byte[0]);
	}

	public static final String replace(String line, String oldString, String newString) {
		if (line == null) {
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
	// Jad home page:
	// http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
	// Decompiler options: packimports(3) fieldsfirst ansi
	// Source File Name: StringUtil.java

	public static final int LEFT_SPACE = 0;
	public static final int RIGHT_SPACE = 1;
	public static final int TRUNC_LEFT = 0;
	public static final int TRUNC_RIGHT = 1;

	private StringUtils() {
	}

	public static boolean startsWithIgnoreCase(String base, String start) {
		if (base.length() < start.length())
			return false;
		else
			return base.regionMatches(true, 0, start, 0, start.length());
	}

	public static boolean endsWithIgnoreCase(String base, String end) {
		if (base.length() < end.length())
			return false;
		else
			return base.regionMatches(true, base.length() - end.length(), end, 0, end.length());
	}

	public static int parseInt(String value, int defaultValue) {
		if (value == null)
			return defaultValue;
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static boolean parseBoolean(String attribute, boolean defaultValue) {
		if (attribute == null)
			return defaultValue;
		return attribute.equals("true");
	}

	public static String trim(String string) {
		return string != null ? string.trim() : "";
	}

	public static String allTrim(String str) {

		if (str == null)
			return "";
		String tmp = str.trim();
		if (tmp.equals(""))
			return "";
		int idx = 0;
		int len = 0;
		len = tmp.length();
		for (idx = tmp.indexOf(" "); idx > 0;) {
			tmp = tmp.substring(0, idx) + tmp.substring(idx + 1, len);
			idx = tmp.indexOf(" ");
			len = tmp.length();
		}

		return tmp;
	}

	public static String toUTF8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= '\377') {
				sb.append(c);
			} else {
				byte b[];
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}

			}
		}
		return sb.toString();
	}

	public static final ArrayList splitString(String targetString, String seperator) {
		if (targetString == null || targetString.trim().equals(""))
			return new ArrayList();
		ArrayList<String> resultStrs = new ArrayList<String>();
		String singleStatement = "";
		int fromIndex = 0;
		int endIndex = 0;
		boolean breakFor = false;
		while (fromIndex < targetString.length()) {
			endIndex = targetString.indexOf(seperator, fromIndex);
			if (endIndex == -1) {
				endIndex = targetString.length();
				breakFor = true;
			}
			singleStatement = targetString.substring(fromIndex, endIndex);
			if (singleStatement != null && !singleStatement.trim().equals(""))
				resultStrs.add(singleStatement);
			fromIndex = endIndex + 1;
			if (breakFor)
				break;
		}
		return resultStrs;
	}

	public static String fill(int value, int length) {
		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMinimumIntegerDigits(length);
		formatter.setGroupingUsed(false);
		return formatter.format(value);
	}

	// 去掉分行符号
	public static String replaceEnter(String str, String replacement) {
		Pattern p = Pattern.compile("\\t|\r|\n");
		// String str="I am a, I am Hello ok, \n new line ffdsa!";
		// System.out.println("before:"+str);
		Matcher m = p.matcher(str);
		String after = m.replaceAll(replacement);
		return after;
	}

	public static List<String> getParameter(String str, char startStr, char endStr) {
		List<String> list = new LinkedList<String>();
		int j = 0;
		int index = -1;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == startStr) {

				if (j == 0)
					index = i;
				j++;
			} else if (str.charAt(i) == endStr) {
				j--;
				if (j == 0 && index != -1)
					list.add(str.substring(index + 1, i));
			}
		}
		return list;
	}

	public static String str(JSONObject json, String name) {
		Object obj;
		obj = json.get(name);
		if (obj == null) {
			return "";
		}
		String str = obj.toString();
		return (str.equals("null") ? "" : str);
	}

	/**
	 * 根据模板输出格式化字符串
	 * @param json
	 * @param name	消息模板
	 * @return
	 */
	public static String strFormat(JSONObject json, String name) {
		List<String> parameter = getParameter(name, '{', '}');
		Object values[] = new Object[parameter.size()];
		int i = 0;
		for (String string : parameter) {
			int index = name.indexOf("%amt");
			if (index > 0) {
				String name2 = string.substring(0, index - 1);
				name = name.replace("{" + string + "}", "%,.2f");
				Double amt = json.getDouble(name2);
				if (amt == null)
					amt = 0.0;
				values[i] = amt;
			} else {
				values[i] = str(json, string);
				i++;
				name = name.replace("{" + string + "}", "%s");
			}

		}
		return String.format(name, values);
	}
	
	public static void main(String args[]) {
		System.out.println(password("123456", "EOCD520*_*"));
		
		JSONObject json = new JSONObject();
		json.put("name", "张三");
		json.put("svname", "患者咨询");
		
		System.out.println(strFormat(json,"{name}发送了一条{svname}"));
	}
}
