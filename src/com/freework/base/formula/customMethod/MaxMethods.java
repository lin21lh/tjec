package com.freework.base.formula.customMethod;

import java.util.Map;

import com.freework.base.formula.LogicFormula;

public class MaxMethods extends MethodAbstract{

	public String getMethodName() {
		return "max";
	}

	public Object getValue(Object obj) {
		Object lvalue=getLvalValue(obj);
		Object rvalue=getRvalValue(obj);
		LogicFormula f=new LogicFormula(
				lvalue,CONVENTION,
				LogicFormula.GREATER,
				rvalue,CONVENTION);
		if(f.compareTo(null)){
			return lvalue;
		}else
			return rvalue;
	}

	public CustomMethod newCustomMethod(String[] args) {
		MaxMethods max=new MaxMethods();
		Object[] objs= getValValue(args[0]);
		max.lvalType=(Integer) objs[0];
		max.lval =objs[1];
		objs= getValValue(args[1]);
		max.rvalType =(Integer) objs[0];
		max.rval=objs[1];
		return max;
	}

	
}
