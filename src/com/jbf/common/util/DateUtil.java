package com.jbf.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author zhaocz
 */
public class DateUtil {

    public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public final static String DATE_FORMAT = "yyyy-MM-dd";

    private String dateString;

    public DateUtil() {
        dateString = new SimpleDateFormat(DATE_TIME_FORMAT).format(new Date());
    }

    /**
     * 根据指定格式，解释字符串，生成日期对象
     * 
     * @param src
     *            　日期字符串
     * @param format
     *            　格式化字符串
     * @return　不能转化时返回Null
     */
    public static Date parseDate(String src, String format) {
        Date date = null;
        if (src == null || src.equals(""))
            return null;
        try {
            date = new SimpleDateFormat(format).parse(src);
        }
        catch (Exception e) {
            return null;
        }
        return date;
    }

    /**
     * 把传入的日期，按指定格式返回其字符串形式
     * 
     * @param d
     *            　日期对象
     * @param format
     *            　格式字符串
     * @return　不能转换返回Null
     */
    public static String dateToString(Date d, String format) {
        String str = null;
        if (d == null) {
            return null;
        } else {
            try {
                str = new SimpleDateFormat(format).format(d);
            }
            catch (Exception e) {
                return null;
            }
        }
        return str;
    }
    
    /**
     * 获取当前时间yyyy-MM-dd字符串形式
     * 
     * @return
     */
    public static String getCurrentDate() {
        return getCurrentDate(DATE_FORMAT);
    }

    /**
     * 获取当前时间yyyy-MM-dd HH:mm:ss字符串形式
     * 
     * @return
     */
    public static String getCurrentDateTime() {
        return getCurrentDate(DATE_TIME_FORMAT);
    }

    /**
     * 获取当前时间指定格式的字符串形式 格式错误按yyyy-MM-dd HH:mm:ss
     * 
     * @param String
     *            sFormat　日期格式字符串
     * @return String　转换成指定格式的字符串形式
     */
    public static String getCurrentDate(String sFormat) {
        if (sFormat == null || sFormat.equals(""))
            sFormat = DATE_TIME_FORMAT;
        SimpleDateFormat formatter = new SimpleDateFormat(sFormat);
        return formatter.format(new java.util.Date());
    }
    /**
     * 获取当前时间
     * @Title: getDate 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param sFormat
     * @return 设定文件
     */
    public static Date getDate(String sFormat) {
        if (sFormat == null || sFormat.equals(""))
            sFormat = DATE_TIME_FORMAT;
        SimpleDateFormat formatter = new SimpleDateFormat(sFormat);
        Calendar calendar = Calendar.getInstance();
        String sj = formatter.format(calendar.getInstance().getTime());
        Date date = null;
        if (sFormat == null || sFormat.equals(""))
        	sFormat = DATE_TIME_FORMAT;
        try {
            date = new SimpleDateFormat(sFormat).parse(sj);
        }
        catch (Exception e) {
            return null;
        }
        return date;
    }
    
    public static String getDateByObject(Object o) {
    	if (o != null && o instanceof java.util.Date)
    		return getDateString((java.util.Date) o);
    	return "";
    }
    
    public static String getDateString(java.util.Date date) {
    	return getDTString(date, DATE_FORMAT);
    }
    
    public static String getDateTimeString(java.util.Date date) {
    	return getDTString(date, DATE_TIME_FORMAT);
    }
    
    public static String getDTString(java.util.Date date, String sFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(sFormat);
        return formatter.format(date);
    }

    /**
     * 计算时间差：天 /小时/分/秒
     * 
     * @param beginTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param type
     *            时间差类型：1天，2小时，3分，4秒
     * @return
     * @throws ParseException
     */
    public static String getTimeLength(String beginTime,
                                       String endTime,
                                       String type) throws ParseException {

        String timeLength = "";
        Date begin = parseDate(beginTime, DATE_TIME_FORMAT);
        Date end = parseDate(endTime, DATE_TIME_FORMAT);
        long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
        if ("1".equals(type)) {
            long day = between / (24 * 3600);
            timeLength = day + "";
        } else if ("2".equals(type)) {
            long hour = between % (24 * 3600) / 3600;
            timeLength = hour + "";
        } else if ("3".equals(type)) {
            long minute = between % 3600 / 60;
            ;
            timeLength = minute + "";
        } else if ("4".equals(type)) {
            long second = between % 60 / 60;
            timeLength = second + "";
        }
        return timeLength;
    }

    /**
     * 返回年份数值
     * 
     * @return
     */
    public int getYear() {
        return Integer.parseInt(this.dateString.substring(0, 4));
    }

    /**
     * 返回4位年份字符串
     * 
     * @return
     */
    public String getStrYear() {
        return this.dateString.substring(0, 4);
    }

    /**
     * 返回月份数值
     * 
     * @return int
     */
    public int getMonth() {
        return Integer.parseInt(this.dateString.substring(5, 7));
    }

    /**
     * 返回2位月份字符串
     * 
     * @return string
     */
    public String getStrMonth() {
        return this.dateString.substring(5, 7);
    }

    /**
     * 返回日数值
     * 
     * @return int
     */
    public int getDay() {
        return Integer.parseInt(this.dateString.substring(8, 10));
    }

    /**
     * 返回2位日字符串
     * 
     * @return String
     */
    public String getStrDay() {
        return this.dateString.substring(8, 10);
    }

    /**
     * 返回小时值
     * 
     * @return int
     */
    public int getHour() {
        return Integer.parseInt(this.dateString.substring(11, 13));
    }

    /**
     * 返回2位小时字符串
     * 
     * @return String
     */
    public String getStrHour() {
        return this.dateString.substring(11, 13);
    }

    /**
     * 返回分钟值
     * 
     * @return int
     */
    public int getMinute() {
        return Integer.parseInt(this.dateString.substring(10, 12));
    }

    /**
     * 返回2位分钟字符串
     * 
     * @return String
     */
    public String getStrMinute() {
        return this.dateString.substring(14, 16);
    }

    /**
     * 返回秒值
     * 
     * @return int
     */
    public int getSecond() {
        return Integer.parseInt(this.dateString.substring(14, 16));
    }

    /**
     * 返回2位秒字符串
     * 
     * @return String
     */
    public String getStrSecond() {
        return this.dateString.substring(17, 19);
    }

    /**
     * 返回年月日字符串格式如　1997-07-01
     * 
     * @return Sting
     */
    public String getStrDate() {
        return this.dateString.substring(0, 10);
    }

    /**
     * 返回时分秒字符串　01:01:01
     * 
     * @return Sting
     */
    public String getStrTime() {
        return this.dateString.substring(12, 19);
    }
    public static void main(String[] args) {
    	Date a = getDate("yyyy-MM-dd HH:mm:ss");
    	System.out.println(a);
	}
}
