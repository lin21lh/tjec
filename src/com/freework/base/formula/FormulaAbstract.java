package com.freework.base.formula;

import java.util.Map;

import com.freework.base.formula.customMethod.CustomMethod;
public abstract class FormulaAbstract implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5094271074549131747L;
	public final static int CONVENTION=0;//常量
	public final static int FORMULA=1;//计算公式
	public final static int VARIABLE=2;//变量
	public final static int CUSTOM_METHOD=3;//自定义方法
	
	
	

	protected  Object getVariableValue(String val,Object value){
		int index=val.indexOf(".");
		if(index!=-1){
			 if(value instanceof Map){
				 Object obj=((Map)value).get(val);
				 if(obj!=null)
					 return obj;
			 }
			String name=val.substring(0, index);
			String lastName=val.substring(index+1);
			Object rValue=getValue(name,value);
			return getVariableValue(lastName,rValue);
		}
		return getValue(val,value);
	}
	
	protected  Object getValue(String name,Object obj){
		
		
		 if(obj instanceof Map){
			 return((Map)obj).get(name);
		 }else{
			 return  Property.getPropertyValue(obj,name);
		 }
	}
	
	
	protected Object getValue(Object value,Object obj, int valType){
		switch (valType){
		case VARIABLE:{
			return this.getVariableValue((String)value, obj);
		}
		case FORMULA:
			return ((Formula)value).calculateNumber(obj);
		case CONVENTION:
			return value;
		case CUSTOM_METHOD:
			return((CustomMethod)value).getValue(obj);
		}
		return null;
		
	}
	
	public Object getRvalValue(Object obj){
		return getValue(rval,obj,rvalType);
	}
	public Object getLvalValue(Object obj){
		return getValue(lval,obj,lvalType);

	}
	
	protected Object lval;
	protected int lvalType=0;

	protected Object rval;
	protected int  rvalType=0;
	
	protected int operaction;
	
	public int getOperaction() {
		return operaction;
	}
	public int getLvalType() {
		return lvalType;
	}
	public Object getLval() {
		return lval;
	}
	public int getRvalType() {
		return rvalType;
	}

	public Object getRval() {
		return rval;
	}
}
