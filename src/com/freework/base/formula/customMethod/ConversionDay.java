package com.freework.base.formula.customMethod;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
/**
 * conversionDay{yyyymmdd,var,aaa}
 * @author Administrator
 *
 */
public class ConversionDay implements CustomMethod {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 7379151339989605047L;
	SimpleDateFormat dateFormatter =null;
	 boolean isVariable=true;
	 boolean isNowDate=false;
	 String value=null;
	public ConversionDay(){}
	public ConversionDay(String[] info) {
		if(info.length<2){
			isNowDate=true;
			return;
		}
		if(!info[0].equals(""))
			dateFormatter= new SimpleDateFormat(info[0]);
		value=info[1];
		if(value.length()>2){
			if(value.charAt(0)=='\''&&value.charAt(value.length()-1)=='\''){
				value=value.substring(1, value.length()-1);
				isVariable=false;
			}
		}else if(value.equals("")){
			isNowDate=true;
			
		}
	}

	
	


	public	String getMethodName() {
		return "toDay";
	}


	public BigDecimal getValue(Object obj1) {
		Date date=null;
		if(isNowDate){
			date=new Date();
		}else
		if(isVariable){
			Object obj=null;
			
			if(obj1 instanceof Map){
				obj= ((Map)obj1).get(value);
			 }else{
				try {
					obj= PropertyUtils.getProperty(obj1,(String) value);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
					throw new RuntimeException(e);

				}
			 }
			
			
			if(obj instanceof Date)
				date=(Date) obj;
			else
				try {
					date=dateFormatter.parse(obj.toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
		}else{
			
			try {
				date=dateFormatter.parse(value);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return new BigDecimal (date.getTime()/1000/ 60 / 60 / 24);
	}

	
	public CustomMethod newCustomMethod(String[] info) {
		return new ConversionDay(info);
	}

	

}
