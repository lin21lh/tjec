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
	 * ����������������֮���������������ʼ���ںͽ�ֹ����
	 * 
	 * @param qsrq
	 *            ��ʼ���ڣ���ʽ����Ϊ"yyyy-mm-dd"
	 * @param jzrq
	 *            ��ֹ���ڣ���ʽ����Ϊ"yyyy-mm-dd"
	 * @return int ����
	 */
	public static int getDateCountDifferentYears(String firstString, String secondString) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date firstDate = null;
		Date secondDate = null;
		try {
			firstDate = df.parse(firstString);
			secondDate = df.parse(secondString);
		} catch (Exception e) {
			// �������ַ�����ʽ����
		}
		int nDay = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
		return nDay + 1;
	}
	/**
	 * ����������������֮���������������ʼ���ںͽ�ֹ����
	 * 
	 * @param qsrq
	 *            ��ʼ���ڣ���ʽ����Ϊ"yyyy-mm-dd"
	 * @param jzrq
	 *            ��ֹ���ڣ���ʽ����Ϊ"yyyy-mm-dd"
	 * @return int ����
	 */
	public static int getDateCountDifferentDays(String firstString, String secondString) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date firstDate = null;
		Date secondDate = null;
		try {
			firstDate = df.parse(firstString);
			secondDate = df.parse(secondString);
		} catch (Exception e) {
			// �������ַ�����ʽ����
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
			// �������ַ�����ʽ����
		}
		int nDay = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
		return nDay;
	}

	/**
	 * ����ͬһ�����������֮���������������ʼ���ںͽ�ֹ����
	 * 
	 * @param qsrq
	 *            ��ʼ���ڣ���ʽ����Ϊ"yyyy-mm-dd"
	 * @param jzrq
	 *            ��ֹ���ڣ���ʽ����Ϊ"yyyy-mm-dd"
	 * @return int ����
	 */
	public static int getDateDiff(String qsrq, String jzrq) {

		// ��������֮�������
		int dateDiff = 0;
		// ȡ�����
		int year = Integer.parseInt(qsrq.substring(0, 4));
		// ȡ����ʼ��
		int startMonth = Integer.parseInt(qsrq.substring(5, 7));
		// ȡ����ʼ��
		int startDay = Integer.parseInt(qsrq.substring(8, 10));
		// ȡ�ý�ֹ��
		int endMonth = Integer.parseInt(jzrq.substring(5, 7));
		// ȡ�ý�ֹ��
		int endDay = Integer.parseInt(jzrq.substring(8, 10));

		// �������2��
		if (startMonth <= 2 && endMonth > 2) {
			// ���������
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
				dateDiff = dateDiff - 1;
			}
			// �����������
			else {
				dateDiff = dateDiff - 2;
			}
		}
		// �������
		int diffMonths = endMonth - startMonth;
		// �����Խ7��8��
		if (startMonth <= 7 && endMonth > 8) {
			// ����������ż��
			if (diffMonths % 2 == 0) {
				// �����ʼ����ż��
				if (startMonth % 2 == 0) {
					dateDiff = dateDiff + 61 * diffMonths / 2 + endDay
							- startDay + 1;
				}
				// �����ʼ��������
				else {
					dateDiff = dateDiff + 61 * diffMonths / 2 + endDay
							- startDay + 2;
				}
			}
			// ��������������
			else {
				dateDiff = dateDiff + 31 + 61 * (diffMonths - 1) / 2 + endDay
						- startDay + 1;
			}
		}
		// �������Խ7��8��
		else {
			// ����������ż��
			if (diffMonths % 2 == 0) {
				dateDiff = dateDiff + 61 * diffMonths / 2 + endDay - startDay
						+ 1;
			}
			// ��������������
			else {
				// �����ʼ���Ǵ���
				if ((startMonth % 2 == 1 && startMonth <= 7)
						|| (startMonth % 2 == 0 && startMonth >= 8)) {
					dateDiff = dateDiff + 31 + 61 * (diffMonths - 1) / 2
							+ endDay - startDay + 1;
				}
				// �����ʼ����С��
				else {
					dateDiff = dateDiff + 30 + 61 * (diffMonths - 1) / 2
							+ endDay - startDay + 1;
				}
			}
		}

		return dateDiff;

	}
	
	/**
	 * ��ʽ������
	 * @author zhang_gbo 
	 * @editor li_hxiang �޸ĺ�����,���޸�return���õ÷��� ����ʽ��ʱ�䵽 �� yyyy-mm-dd hh24:mi
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
	 * ��ʽ������ yyyy-MM-dd hh24:mi
	 * @author zhang_gbo
	 * @editor li_hxiang �޸ĺ�����
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
	 * ��ǰ�����ַ���
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

	// �������ַ���ת��Ϊ���ڱ���,���������,���ص�ǰ����
	public static java.util.Date stringToDateTime(String str) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return (java.util.Date) formatter.parse(str);
		} catch (ParseException e) {
			return new java.util.Date();
		}
	}

	// �������ַ���ת��Ϊ���ڱ���,���������,���ص�ǰ����
	public static java.util.Date stringToDate(String str) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return (java.util.Date) formatter.parse(str);
		} catch (ParseException e) {
			return new java.util.Date();
		}
	}

	/**
	 * ��ǰ�����ַ���
	 */

	public static String stringOfCnDateTime() {
		return stringOfCnDateTime(new java.util.Date());
	}

	public static String stringOfCnDateTime(java.util.Date date) {
		Format formatter = new SimpleDateFormat("yyyy��M��d�� Hʱm��s��");
		return formatter.format(date);
	}

	/**
	 * ��ǰ�����ַ���
	 */
	public static String stringOfCnDate() {
		return stringOfCnDate(new java.util.Date());
	}

	public static String stringOfCnDate(java.util.Date date) {
		Format formatter = new SimpleDateFormat("yyyy��M��d��");
		return formatter.format(date);
	}

	/**
	 * ��ǰ�����ַ���
	 */
	public static String stringOfDate() {
		return stringOfDate(new java.util.Date());
	}

	public static String stringOfDate(java.util.Date date) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

	/**
	 * ���㲢���ظ������µ����һ��
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
	 * ���㲢���������е����ڼ�
	 */

	public static int weekOfDate(java.util.Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.DAY_OF_WEEK);
	}
	

	/**
	 * ���㲢���������е���
	 */
	public static int dayOfDate(java.util.Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * ���㲢���ص�ǰ���ڵ��·�
	 * @return
	 */
	public static int nowMonthOfDate() {
		
		return monthOfDate(new Date());
	}
	
	/**
	 * ���㲢���������е���
	 */
	public static int monthOfDate(java.util.Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.MONTH) + 1;
	}

	/**
	 * ���ص�ǰ���
	 */
	public static int yearOfDate() {
		return yearOfDate(new java.util.Date());
	}

	/**
	 * ���㲢���������е���
	 */
	public static int yearOfDate(java.util.Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.YEAR);
	}

	/**
	 * ���㲢���������е�ʱ
	 */
	public static int hourOfDate(java.util.Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * ���㲢���������еķ�
	 */
	public static int minuteOfDate(java.util.Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.MINUTE);
	}

	/**
	 * ���㲢���������е���
	 */
	public static int secondOfDate(java.util.Date d1) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		return gc.get(Calendar.SECOND);
	}

	/**
	 * �������º������
	 */
	public static java.util.Date addDateByMonth(java.util.Date d, int mcount) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		gc.add(Calendar.MONTH, mcount);
		gc.add(Calendar.DATE, -1);
		return new java.util.Date(gc.getTime().getTime());
	}

	/**
	 * �������պ������
	 */
	public static java.util.Date addDateByDay(java.util.Date d, int dcount) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		gc.add(Calendar.DATE, dcount);
		return new java.util.Date(gc.getTime().getTime());
	}

	/**
	 * ��������������
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
