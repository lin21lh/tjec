package com.freework.base.formula.customMethod;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.freework.base.formula.Property;

public class RegexMethod implements CustomMethod {

	private String regex;
	private String 	variable;
	Pattern pattern;
	@Override
	public CustomMethod newCustomMethod(String[] args) {
		
		RegexMethod method=new RegexMethod();
		method.regex=raplaceInstead(args[0]);

		method.variable=args[1];
		method.pattern = Pattern.compile(method.regex); 
		return method;
	}

	static String characters[]={",","``",
							    "$","`&",
							    "}","`)",
							    "{","`("};
	public static String replacePositive(String oldStr){
		for (int i = 0; i < characters.length; i+=2) {
			oldStr=oldStr.replace(characters[i], characters[i+1]);
		}
		return oldStr;
		
	}
	public static String raplaceInstead(String oldStr){
		for (int i = 0; i < characters.length; i+=2) {
			oldStr=oldStr.replace(characters[i+1], characters[i]);
		}
		return oldStr;
		
	}
	@Override
	public String getMethodName() {
		return "regex";
	}

	@Override
	public Object getValue(Object obj) {
		String value=getValue(obj,variable);
		if(variable==null)
			return 0;
		Matcher m = pattern.matcher(value); 
		return m.find(); 

		
		
	}

	/**
	 * @param args
	 */
	public String getValue(Object obj,String name) {
		Object value=null;
		 if(obj instanceof Map){
			 value=((Map)obj).get(name);
		 }else{
			 value=Property.getPropertyValue(obj, name);
		 }
		 return value==null?null:value.toString();
	}

}
