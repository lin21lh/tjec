package com.wfzcx.aros.xzfy.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;

import com.jbf.common.util.StringUtil;

public class ConvertUtil {

	public static String getStringObject(Object object){
		
		if(object == null ) {
			return "";
		}
		if(object instanceof String) {
			return (String)object;
		}
		if(object instanceof Date) {
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
			if(object instanceof Timestamp) {
				dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}
			try{
				return dateformat.format(object);
			}catch(Exception e) {
				throw new ConversionException("日期转换字符串时出错 ");  
			}

		}
		return "";
	}
	
	public static Integer getIntObject(Object object){
		String str = getStringObject(object);

		if(str.equals("")) {
			return 0;
		}
		return Integer.valueOf(str);
	}
	public static Date getDateObject(Object object, String dateformat){
		if("".equals(StringUtil.stringConvert(dateformat))) {
			dateformat = "yyyy-MM-dd";
		}
		
		if(object == null ) {
			return null;
		}
		if(object instanceof Date) {
			return (Date)object;
		}
		if(object instanceof String){			
			SimpleDateFormat format = new SimpleDateFormat(dateformat);
			try {
				
				Date date = format.parse((String)object);
				return date;
			} catch (ParseException e) {
				throw new ConversionException("属性转换为日期格式时出错 ");  
			}
		}
		
		throw new ConversionException("属性不能转换为日期格式 "); 
	}
	/**
	 * 将object转换为double 
	 * @param object
	 * @param pres 精度： 值为-1时表示不设置精度
	 * @return
	 */
	public static double getDoubleObject(Object object,int pres){
		double num = 0D;
		if(pres < 0) {
			pres = 0;
		}
		String str = getStringObject(object);
		str = str.trim();
		if(str.equals("")) {
			if(pres < 0) {
				return num;
			}else {				
				return new BigDecimal(num).setScale(pres, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
		}
		try{			
			if(pres < 0) {
				num = new BigDecimal(str).doubleValue();
			}else {
				
				num = new BigDecimal(str).setScale(pres, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
		}catch(Exception e) {
			throw new ConversionException("属性不能转换为Double格式 "); 
		}
		
		
		return num;
	}

}
