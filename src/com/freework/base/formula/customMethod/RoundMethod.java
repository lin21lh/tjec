package com.freework.base.formula.customMethod;

import java.math.MathContext;
import java.util.Map;

import com.freework.base.util.NumberUtil;

public class RoundMethod  extends MethodAbstract{

	public String getMethodName() {
		// TODO Auto-generated method stub
		return "round";
	}
	public String getMethodNameFormat() {
		// TODO Auto-generated method stub
		return "$round(@,@)}";
	}

	public static String getMethodFormat(String format){
		 if(format.indexOf("$")>-1){
			 if(format.equals("$round{"))
				 return "round";
		 }
		 else if((format.indexOf(",")>-1)&&(format.indexOf("}")>-1)){
				 String str = format.substring(format.indexOf(",")+1, format.indexOf("}"));
				     if(NumberUtil.isNumber(str)){
				    	 return "round";
				     }else{
				    	 return format;
				     }
			      } 
		 else  if(format.indexOf("}")>-1){
			    	  if(format.equals("}")){
			    		  return "round";
			    	  }else{
			    		  return format;
			    	  }
			      }
		 else  return format+"roundº¯Êý¸ñÊ½´íÎó";
			 
		return null;
	}
	public Object getValue(Object obj) {
		Map map=(Map)obj;
		Object lvalue=getLvalValue(obj);
		Object rvalue=getRvalValue(obj);
		
		return NumberUtil.bigDecimal(lvalue).round(new MathContext(NumberUtil.bigDecimal(rvalue).intValue()));
	}

	public CustomMethod newCustomMethod(String[] args) {
		RoundMethod round=new RoundMethod();
		Object[] objs= round.getValValue(args[0]);
		round.lvalType=(Integer) objs[0];
		round.lval =objs[1];
		objs= round.getValValue(args[1]);
		round.rvalType =(Integer) objs[0];
		round.rval=objs[1];
		return round;
	}

}
