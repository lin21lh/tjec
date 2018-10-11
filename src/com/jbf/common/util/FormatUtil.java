package com.jbf.common.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {
	// private static NumberFormat numberFormat=NumberFormat.getInstance();
	// private static NumberFormat formatter2 = new DecimalFormat("0.00");
	//
	// private static SimpleDateFormat dateFormatter = new
	// SimpleDateFormat("yyyyMMdd");
	// private static SimpleDateFormat dateFormatter2 = new
	// SimpleDateFormat("yyyy-MM-dd");
	// private static SimpleDateFormat dateTimeFormatter2 = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// private static NumberFormat formatter = new DecimalFormat("0.000000");

	// static{
	// numberFormat.setMaximumFractionDigits(2);
	// numberFormat.setMinimumFractionDigits(2);
	//
	// }

	public static int getLevel(String code, int[] splitType) {
		int currlen = 0;
		int len = code.length();
		int i = 0;
		for (i = 0; i < splitType.length; i++) {
			currlen += splitType[i];
			if (currlen == len)
				return (i + 1);
		}

		return i;
	}

	public static String[] getCodes(String code, int[] splitType) {

		int len = code.length();
		if (len == 0) {
			return new String[0];

		}
		int level = getLevel(code, splitType);
		if (level == -1)
			return new String[0];
		String[] rtn = new String[level];
		int index = 0;
		for (int i = 0; i < level; i++) {
			StringBuffer str = new StringBuffer();
			for (int j = 0; j < splitType[i]; j++) {
				str.append(code.charAt(index++));
			}
			rtn[i] = str.toString();
		}
		return rtn;
	}

	/**
	 * 
	 * @param number
	 * @return 返回有且只有六位六位小数的字符(千位[不]带有逗号分隔)
	 */
	public static String fromDouble_6(double d) {

		NumberFormat formatter = new DecimalFormat("0.000000");
		return formatter.format(d);
	}

	/**
	 * 
	 * @param number
	 * @return 返回有且只有两位小数的字符(千位带有逗号分隔)
	 */
	public static String getNumber(Object number) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		numberFormat.setMinimumFractionDigits(2);
		return numberFormat.format(number);

	}

	/**
	 * 
	 * @param number
	 * @return 返回有且只有两位小数的字符(千位带有逗号分隔)
	 */
	public static String getNumber(double number) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		numberFormat.setMinimumFractionDigits(2);
		return numberFormat.format(number);

	}

	/**
	 * 返回有且只有两位小数的字符(千位[不]带有逗号分隔)
	 * 
	 * @param amt
	 * @return
	 */
	public static String getNumber2(Object amt) {
		NumberFormat formatter2 = new DecimalFormat("0.00");
		return formatter2.format(amt);

	}

	/**
	 * 返回有且只有两位小数的字符(千位[不]带有逗号分隔)
	 * 
	 * @param amt
	 * @return
	 */
	public static String getNumber2(double amt) {
		NumberFormat formatter2 = new DecimalFormat("0.00");
		return formatter2.format(amt);

	}

	/**
	 * 返回 yyyyMMdd日期格式的当前日期
	 * 
	 * @return yyyyMMdd日期格式的当前日期
	 */
	public static String stringDate() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
		return dateFormatter.format(new java.util.Date());
	}

	/**
	 * 返回 yyyyMMdd日期格式
	 * 
	 * @return yyyyMMdd日期格式的当前日期
	 */
	public static String stringDate(Date date) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
		return dateFormatter.format(date);
	}

	/**
	 * 返回yyyy-MM-dd HH:mm:ss日期格式的当前日期
	 * 
	 * @return yyyyMMdd日期格式的当前日期
	 */
	public static String stringDate2() {
		SimpleDateFormat dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormatter2.format(new java.util.Date());
	}

	/**
	 * 返回 yyyy-MM-dd日期格式
	 * 
	 * @return yyyyMMdd日期格式的当前日期
	 */
	public static String stringDate2(Date date) {
		SimpleDateFormat dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormatter2.format(date);
	}

	/**
	 * 返回 yyyy-MM-dd日期格式
	 * 
	 * @return yyyyMMdd日期格式的当前日期
	 */
	public static String stringDateTime2(Date date) {
		SimpleDateFormat dateTimeFormatter2 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateTimeFormatter2.format(date);
	}

	/**
	 * 返回 yyyy-MM-dd HH:mm:ss日期格式的当前日期
	 * 
	 * @return yyyy-MM-dd HH:mm:ss日期格式的当前日期
	 */
	public static String stringDateTime2() {
		SimpleDateFormat dateTimeFormatter2 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateTimeFormatter2.format(new java.util.Date());
	}

	public static java.util.Date dateTime2(String datestr) {
		java.util.Date rdatetime = null;
		try {

			SimpleDateFormat dateTimeFormatter2 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			ParsePosition pos = new ParsePosition(0);
			rdatetime = dateTimeFormatter2.parse(datestr, pos);
			return rdatetime;
		} catch (IllegalArgumentException myException) {
			return rdatetime;
		}

	}

	/**
	 * 根据长度补齐开始位数位数 如 filNumber(1,2,"0") 将返回 01
	 * 
	 * @param value
	 *            需要补齐的值
	 * @param length
	 *            总长度
	 * @param filSymbol
	 *            补齐符号
	 * @return 补齐后的string
	 */
	public static String filLeftNumber(int value, int length, char filSymbol) {

		return filLeftNumber(String.valueOf(value), length, filSymbol);

	}

	/**
	 * 根据长度补齐开始位数位数 如 filLeftNumber(1,2,"0") 将返回 01
	 * 
	 * @param value
	 *            需要补齐的值
	 * @param length
	 *            总长度
	 * @param filSymbol
	 *            补齐符号
	 * @return 补齐后的string
	 */
	public static String filLeftNumber(String value, int length, char filSymbol) {
		String str = value;
		int strlength = str.getBytes().length;
		if (strlength < length) {
			strlength = length - strlength;
			char cp[] = new char[length];
			int i = 0;
			for (; i < strlength; i++) {
				cp[i] = filSymbol;
			}
			value.getChars(0, value.length(), cp, i);
			return new String(cp);
		} else
			return str;

	}

	/**
	 * 根据长度补齐开始位数位数 如 filRightNumber(1,2,"0") 将返回 01
	 * 
	 * @param value
	 *            需要补齐的值
	 * @param length
	 *            总长度
	 * @param filSymbol
	 *            补齐符号
	 * @return 补齐后的string
	 */
	public static String filRightNumber(String value, int length, char filSymbol) {
		String str = value;
		int strlength = str.length();
		if (strlength < length) {
			char cp[] = new char[length];
			value.getChars(0, value.length(), cp, 0);
			for (int i = value.length(); i < length; i++) {
				cp[i] = filSymbol;
			}
			return new String(cp);
		} else
			return str;

	}

	/**
	 * 根据长度补齐开始位数位数 如 filRightNumber(1,2,"0") 将返回 01
	 * 
	 * @param value
	 *            需要补齐的值
	 * @param length
	 *            总长度
	 * @param filSymbol
	 *            补齐符号
	 * @return 补齐后的string
	 */
	public static String filRightNumber(int value, int length, char filSymbol) {
		return filRightNumber(String.valueOf(value), length, filSymbol);

	}

	public static void main(String args[]) {
		System.out.println(filRightNumber("1121", 8, '0'));
		System.out.println(filLeftNumber("你好", 8, '0'));
		System.out.println(filLeftNumber("2113", 8, ' '));

		long starttime = System.currentTimeMillis();
		// for (int i = 0; i < 8000000; i++) {
		// filRightNumber("112",8,'0');
		//
		// }
		System.out.println(System.currentTimeMillis() - starttime);
	}
}
