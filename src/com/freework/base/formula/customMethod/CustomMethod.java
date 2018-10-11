package com.freework.base.formula.customMethod;

import java.util.Map;


public  interface CustomMethod extends java.io.Serializable{
	 public CustomMethod newCustomMethod(String[] args);
	 public String getMethodName();
	 public Object getValue(Object obj);
	
}
