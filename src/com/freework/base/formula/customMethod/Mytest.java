package com.freework.base.formula.customMethod;

import java.util.Map;

public class Mytest implements CustomMethod {

	String a[]=null;
	public String getMethodName() {
	
		return "mytest";
	}

	public Object getValue(Map map) {
		int i=Integer.parseInt(map.get(a[0]).toString());
		return i==1?a[1]:a[2];
	}

	public CustomMethod newCustomMethod(String[] args) {
		Mytest mytest=new Mytest();
		mytest.a=args;
		return  mytest;
	}

	public Object getValue(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
