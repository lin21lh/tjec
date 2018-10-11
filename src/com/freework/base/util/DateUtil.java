package com.freework.base.util;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil  {




	/**
	 * 计算跨年的两个日期之间的天数，包括起始日期和截止日期
	 * 
	 * @param qsrq
	 *            起始日期，格式必须为"yyyy-mm-dd"
	 * @param jzrq
	 *            截止日期，格式必须为"yyyy-mm-dd"
	 * @return int 天数
	 */
	public static int getDateCountDifferentYears(String firstString, String secondString) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date firstDate = null;
		Date secondDate = null;
		try {
			firstDate = df.parse(firstString);
			secondDate = df.parse(secondString);
		} catch (Exception e) {
			// 日期型字符串格式错误
		}
		int nDay = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
		return nDay + 1;
	}
	/**
	 * 计算跨年的两个日期之间的天数，包括起始日期和截止日期
	 * 
	 * @param qsrq
	 *            起始日期，格式必须为"yyyy-mm-dd"
	 * @param jzrq
	 *            截止日期，格式必须为"yyyy-mm-dd"
	 * @return int 天数
	 */
	public static int getDateCountDifferentDays(String firstString, String secondString) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date firstDate = null;
		Date secondDate = null;
		try {
			firstDate = df.parse(firstString);
			secondDate = df.parse(secondString);
		} catch (Exception e) {
			// 日期型字符串格式错误
		}
		int nDay = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
		return nDay;
	}
	
	public static int getDateTimeCountDifferentDays(String firstString, String secondString) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date firstDate = null;
		Date secondDate = null;
		try {
			firstDate = df.parse(firstString);
			secondDate = df.parse(secondString);
		} catch (Exception e) {
			// 日期型字符串格式错误
		}
		int nDay = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
		return nDay;
	}

	/**
	 * 计算同一年的两个日期之间的天数，包括起始日期和截止日期
	 * 
	 * @param qsrq
	 *            起始日期，格式必须为"yyyy-mm-dd"
	 * @param jzrq
	 *            截止日期，格式必须为"yyyy-mm-dd"
	 * @return int 天数
	 */
	public static int getDateDiff(String qsrq, String jzrq) {

		// 两个日期之间的天数
		int dateDiff = 0;
		// 取得年份
		int year = Integer.parseInt(qsrq.substring(0, 4));
		// 取得起始月
		int startMonth = Integer.parseInt(qsrq.substring(5, 7));
		// 取得起始日
		int startDay = Integer.parseInt(qsrq.substring(8, 10));
		// 取得截止月
		int endMonth = Integer.parseInt(jzrq.substring(5, 7));
		// 取得截止日
		int endDay = Integer.parseInt(jzrq.substring(8, 10));

		// 如果包含2月
		if (startMonth <= 2 && endMonth > 2) {
			// 如果是闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
				dateDiff = dateDiff - 1;
			}
			// 如果不是闰年
			else {
				dateDiff = dateDiff - 2;
			}
		}
		// 间隔月数
		int diffMonths = endMonth - startMonth;
		// 如果跨越7、8月
		if (startMonth <= 7 && endMonth > 8) {
			// 如果间隔月是偶数
			if (diffMonths % 2 == 0) {
				// 如果起始月是偶数
				if (startMonth % 2 == 0) {
					dateDiff = dateDiff + 61 * diffMonths / 2 + endDay
							- startDay + 1;
				}
				// 如果起始月是奇数
				else {
					dateDiff = dateDiff + 61 * diffMonths / 2 + endDay
							- startDay + 2;
				}
			}
			// 如果间隔月是奇数
			else {
				dateDiff = dateDiff + 31 + 61 * (diffMonths - 1) / 2 + endDay
						- startDay + 1;
			}
		}
		// 如果不跨越7、8月
		else {
			// 如果间隔月是偶数
			if (diffMonths % 2 == 0) {
				dateDiff = dateDiff + 61 * diffMonths / 2 + endDay - startDay
						+ 1;
			}
			// 如果间隔月是奇数
			else {
				// 如果起始月是大月
				if ((startMonth % 2 == 1 && startMonth <= 7)
						|| (startMonth % 2 == 0 && startMonth >= 8)) {
					dateDiff = dateDiff + 31 + 61 * (diffMonths - 1) / 2
							+ endDay - startDay + 1;
				}
				// 如果起始月是小月
				else {
					dateDiff = dateDiff + 30 + 61 * (diffMonths - 1) / 2
							+ endDay - startDay + 1;
				}
			}
		}

		return dateDiff;

	}
	
	/**
	 * 格式化日期
	 * @author zhang_gbo 
	 * @editor li_hxiang 修改函数名,并修改return调用得方法 ，格式话时间到 分 yyyy-mm-dd hh24:mi
	 * @param time
	 * @param delimeter
	 * @return
	 */
	public static String getFormatedDateStringtoMi(long time, String delimeter) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(time);
		return getFormatedDateStringtoMi(calendar, delimeter);
	}

	/**
	 * 格式化日期 yyyy-MM-dd hh24:mi
	 * @author zhang_gbo
	 * @editor li_hxiang 修改函数名
	 * @param calendar
	 * @param delimeter
	 * @return
	 */
	public static String getFormatedDateStringtoMi(Calendar calendar,
			String delimeter) {
		String year = String.valueOf(calendar.get(1));
		String month = String.valueOf(calendar.get(2) + 1);
		if (month.length() == 1)
			month = "0" + month;
		String day = String.valueOf(calendar.get(5));
		if (day.length() == 1)
			day = "0" + day;
		String hour = String.valueOf(calendar.get(11));
		if (hour.length() == 1)
			hour = "0" + hour;
		String minute = String.valueOf(calendar.get(12));
		if (minute.length() == 1)
			minute = "0" + minute;
		String str = "";
		str = year + delimeter + month + delimeter + day + " " + hour + ":"
				+ minute;
		return str;
	}
	
	public static Date dateTime() {
		return new java.util.Date();
	}
	
	/**
	 * 当前日期字符串
	 */

	public static String stringOfDateTime() {
		return stringOfDateTime(new java.util.Date());
	}

	public static String stringOfDateTime(java.util.Date date) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}

	public static String stringTime() {
		return stringTime(new java.util.Date());
	}

	public static String stringTime(java.util.Date date) {
		Format formatter = new SimpleDateFormat("HHmmss");
		return formatter.format(date);
	}

	// 将日期字符串转换为日期变量,如果有问题,返回当前日期
	public static java.util.Date stringToDateTime(String str) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return (java.util.Date) formatter.parse(str);
		} catch (ParseException e) {
			return new java.util.Date();
		}
	}

	// 将日期字符串转换为日期变量,如果有问题,返回当前日期
	public static java.util.Date stringToDate(String str) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return (java.util.Date) formatter.parse(str);
		} catch (ParseException e) {
			return new java.util.Date();
		}
	}

	/**
	 * 当前日期字符串
	 */

	public static String stringOfCnDateTime() {
		return stringOfCnDateTime(new java.util.Date());
	}

	public static String stringOfCnDateTime(java.util.Date date) {
		Format formatter = new SimpleDateFormat("yyyy年M月d日 H时m分s秒");
		return formatter.format(date);
	}

	/**
	 * 当前日期字符串
	 */
	public static String stringOfCnDate() {
		return stringOfCnDate(new java.util.Date());
	}

	public static String stringOfCnDate(java.util.Date date) {
		Format formatter = new SimpleDateFormat("yyyy年M月d日");
		return formatter.format(date);
	}

	/**
	 * 当前日期字符串
	 */
	public static String stringOfDate() {
		return stringOfDate(new java.util.Date());
	}

	public static String stringOfDate(java.util.Date date) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

	/**
	 * 计算并返回给定年月的最后一天
	 */
	public static String lastDateOfMonth(int year, int month) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.YEAR, year);
		gc.set(Calendar.MONTH, month - 1);
		int maxDate = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
		gc.set(Calendar.DATE, maxDate);
		return stringOfDate(gc.getTime());
	}

	/**
	 * 计算并返回日期中的星期几
	 */

	public static int weekOfDate(java.util.Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.DAY_OF_WEEK);
	}
	

	/**
	 * 计算并返回日期中的日
	 */
	public static int dayOfDate(java.util.Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 计算并返回当前日期的月份
	 * @return
	 */
	public static int nowMonthOfDate() {
		
		return monthOfDate(new Date());
	}
	
	/**
	 * 计算并返回日期中的月
	 */
	public static int monthOfDate(java.util.Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.MONTH) + 1;
	}

	/**
	 * 返回当前年度
	 */
	public static int yearOfDate() {
		return yearOfDate(new java.util.Date());
	}

	/**
	 * 计算并返回日期中的年
	 */
	public static int yearOfDate(java.util.Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.YEAR);
	}

	/**
	 * 计算并返回日期中的时
	 */
	public static int hourOfDate(java.util.Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 计算并返回日期中的分
	 */
	public static int minuteOfDate(java.util.Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.MINUTE);
	}

	/**
	 * 计算并返回日期中的秒
	 */
	public static int secondOfDate(java.util.Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.SECOND);
	}

	/**
	 * 计算数月后的日期
	 */
	public static java.util.Date addDateByMonth(java.util.Date d, int mcount) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		gc.add(Calendar.MONTH, mcount);
		gc.add(Calendar.DATE, -1);
		return new java.util.Date(gc.getTime().getTime());
	}

	/**
	 * 计算数日后的日期
	 */
	public static java.util.Date addDateByDay(java.util.Date d, int dcount) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		gc.add(Calendar.DATE, dcount);
		return new java.util.Date(gc.getTime().getTime());
	}

	/**
	 * 计算数秒后的日期
	 */
	public static java.util.Date addDateBySecond(java.util.Date d, int scount) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		gc.add(Calendar.SECOND, scount);
		return gc.getTime();
	}
	
	public static void main(String[] args) {
		System.out.println(stringOfDateTime());
		
		
	}

}
