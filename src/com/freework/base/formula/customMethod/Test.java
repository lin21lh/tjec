package com.freework.base.formula.customMethod;

import java.util.Map;

public class Test extends MethodAbstract {

	String valname="";
	public String getMethodName() {
		return "test";
	}

	public Object getValue(Map map) {
		if(Integer.parseInt(map.get(valname).toString())>100)
			return this.getValue(lval, map, lvalType);
		else
			return this.getValue(rval, map, rvalType);
		
	}

	public CustomMethod newCustomMethod(String[] args) {
		Test test=new Test();
		test.valname=args[0];
		
		
		Object[] objs= getValValue(args[1]);
		test.lvalType=(Integer) objs[0];
		test.lval =objs[1];
		
		
		objs= getValValue(args[2]);
		test.rvalType =(Integer) objs[0];
		test.rval=objs[1];
		return test;
	}

	public Object getValue(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
