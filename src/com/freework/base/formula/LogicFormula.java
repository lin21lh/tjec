package com.freework.base.formula;


import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.freework.base.util.NumberUtil;

public class LogicFormula extends FormulaAbstract{



	public final static int LESS = -1; // <小于
	public final static int LESS_EQUALS = -2; // <=小于等于

	public final static int EQUALS = 0; // =等于

	public final static int GREATER_EQUALS = 2; // >=大于等于
	public final static int GREATER = 1; // >大于

	public final static int NOTEQUALS = 3; // !=不等于
	
	
	public final static int OR = 7; // |或
	public final static int AND = 8; // &与

	
	
	
	public LogicFormula(Object lval,int lvalType, int operaction, Object rval,int rvalType) {
		this.lvalType=lvalType;
		this.lval=lval;
		this.operaction=operaction;
		this.rvalType=rvalType;
		this.rval=rval;
	}
//	public LogicFormula(Object lval, int operaction, Object rval,List<CustomMethod> customMethods) {
//		Object[] lobjs= getValValue(lval ,customMethods);
//		this.lvalType=(Integer) lobjs[0];
//		this.lval=lobjs[1];
//		
//		this.operaction=operaction;
//
//		Object[] robjs= getValValue(rval,customMethods);
//		this.rvalType=(Integer) robjs[0];
//		this.rval=robjs[1];
//		
//	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 8978641968322492905L;
	

	
	Object getValue(Object obj,Class clazz){
		Converter converter=ConvertUtils.lookup(clazz);
		Object value2=converter.convert(obj.getClass(), obj);
		return value2;
		
	}
	
	
	public int compare(Object obj){
		Object lvalValue =this.getLvalValue(obj);
		Object rvalValue =this.getRvalValue(obj);
		
		
		
		if(lvalValue==null||rvalValue==null){
			return  rvalValue ==null&&lvalValue!=null?1:
					lvalValue ==null&&rvalValue!=null?-1:0;
		}else{
			if(lvalValue instanceof Number&& rvalValue instanceof Number){
				return NumberUtil.bigDecimal(lvalValue).compareTo(NumberUtil.bigDecimal(rvalValue));
			}else{
					if(lvalValue.getClass()!=rvalValue.getClass()){
						Converter converter=ConvertUtils.lookup(lvalValue.getClass());
						if(converter!=null){
							Object value2=converter.convert(lvalValue.getClass(), rvalValue);
							int i=((Comparable)lvalValue).compareTo(value2); 
							return i;
						}else
							throw new RuntimeException(lvalValue.getClass()+"与"+rvalValue.getClass()+"类型无法转换");
						
					}else{
						
						   return ((Comparable)lvalValue).compareTo(rvalValue);
		
						
					}	
			}
			

		}
	}

	public boolean compareFormula(Object obj,boolean isOr){
		LogicFormula lvalValue =(LogicFormula) this.getLval();
		LogicFormula rvalValue =(LogicFormula) this.getRval();
		return lvalValue.compareTo(obj)==isOr?isOr:rvalValue.compareTo(obj);
	}
	public boolean compareTo(Object obj) {
		switch (getOperaction()) {
		case LESS:
			return compare(obj) <0;
		case GREATER:
			return compare(obj) >0;
		case EQUALS:
			return compare(obj) ==0;
		case LESS_EQUALS: 
			return compare(obj) <= 0;
		case GREATER_EQUALS: 
			return compare(obj) >= 0;
		case NOTEQUALS: 
			return compare(obj)!= 0;
		case OR: 
			return compareFormula(obj,true);
		case AND: 
			return compareFormula(obj,false);
		}
		
		return false;
	}
	public String toString(){
		String operaction="";
		switch (getOperaction()) {
		case LESS:
			operaction="<";
			break;
		case GREATER:
			operaction=">";
			break;
		case EQUALS:
			operaction="=";
			break;
		case LESS_EQUALS: 
			operaction="<=";
			break;
		case GREATER_EQUALS: 
			operaction=">=";
			break;
		case NOTEQUALS: 
			operaction="!=";
			break;
		case OR: 
			operaction="|";
			break;
		case AND: 
			operaction="&";
			break;
		}
		return String.valueOf(lval)+operaction+String.valueOf(rval);
	}
}