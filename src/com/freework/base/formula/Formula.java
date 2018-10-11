package com.freework.base.formula;

import java.math.BigDecimal;
import java.util.List;

import com.freework.base.formula.customMethod.CustomMethod;
import com.freework.base.util.NumberUtil;

public class Formula extends FormulaAbstract{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3218381925332948518L;
	public Formula(Object lval,int operaction,Object rval,List<Formula> Formulas,List<CustomMethod> customMethods) {
		super();
		this.setLvalValue(lval,Formulas,customMethods);
		this.operaction=operaction;
		this.setRvalValue(rval,Formulas,customMethods);
	}
	
	private  Object[]  getValValue(Object val,List<Formula> Formulas,List<CustomMethod> customMethods){
		Object[] objs=new Object[2];
		if(val==null){
			objs[0]=CONVENTION;
			objs[1]=null;
		}else if (val instanceof Formula){
			objs[0]=FORMULA;
			objs[1]=val;
			
		}else if(NumberUtil.isNumber((String)val)){
				
				objs[0]=CONVENTION;
				objs[1]=NumberUtil.bigDecimal(val);
		 }else{
				String name=(String)val;
		    	if(name.indexOf("@#analyseList_")!=-1){
					objs[0]=FORMULA;
		    		objs[1]=Formulas.get(Integer.parseInt(name.substring(14)));
		    	}else	
		    	if(customMethods!=null&&name.indexOf("@#customMethod_")!=-1){
		    			
		    			objs[0]=CUSTOM_METHOD;
			    		objs[1]=customMethods.get(Integer.parseInt(name.substring(15)));
		    	}else{
			    		objs[0]=VARIABLE;
			    		objs[1]=name;
		    	}
		    	
		}
		return objs;
	} 
	
	public void setRvalValue(Object rval,List<Formula> Formulas,List<CustomMethod> customMethods){
		Object[] objs= getValValue(rval,Formulas,customMethods);
		rvalType=(Integer) objs[0];
		this.rval=objs[1];
	

	}
	public void setLvalValue(Object lval,List<Formula> Formulas,List<CustomMethod> customMethods){
		Object[] objs= getValValue(lval,Formulas,customMethods);
		lvalType=(Integer) objs[0];
		this.lval=objs[1];
		

	}
	
	
	public BigDecimal calculateNumber(Object obj){
		try{
			Object leftNumber=getLvalValue(obj);
			Object rightNumber=getRvalValue(obj);
			
			switch (operaction){
			case '+':
				return NumberUtil.add(leftNumber, rightNumber);
			case '`':
				return NumberUtil.subtract(leftNumber, rightNumber);
			case '*':
				return NumberUtil.multiply(leftNumber, rightNumber);
			case '/':
				return NumberUtil.divide(leftNumber, rightNumber);
				
		}
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
				return BigDecimal.ZERO;
	}
	public String toString(){
		return new StringBuilder(32)
		.append(lval)
		.append((char)operaction)
		.append(rval).toString();
	}
	
	
}
