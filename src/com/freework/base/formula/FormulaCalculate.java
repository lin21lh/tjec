package com.freework.base.formula;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.freework.base.formula.customMethod.CustomMethods;





public class FormulaCalculate {
	
	public static void main(String aaa[]){
		Map map=new HashMap();
		map.put("a",new BigDecimal("1.253") );
		map.put("b",new BigDecimal(1) );
		map.put("c",new BigDecimal(1) );
		map.put("d",new BigDecimal(1) );
		System.out.println(calculate("$round{a+b+c+d,2}+1",map));;
		System.out.println(CustomMethods.isCustomMethod("round"));
		
	}
	Formula fa=null;
	static HashMap<String,Formula> formulaCache=new HashMap<String,Formula>();

	public static BigDecimal calculate(String formulaStr,Object values){
		Formula formula=formulaCache.get(formulaStr);
		if(formula==null){
			formula= new FormulaAnalyse(formulaStr).getFormula();
			formulaCache.put(formulaStr,formula );
		}
		return formula.calculateNumber(values);
	}

	
	
	
	public static void calculate(String[] nameFormula,Object values){
		 if(nameFormula.length%2==1)
		        throw new IllegalStateException("您的 与  不配对");
		for (int i = 0; i < nameFormula.length; i+=2) {
			BigDecimal value=calculate(nameFormula[i+1],values);
			 if(values instanceof Map){
					((Map)values).put(nameFormula[i],value );
			 }else{
					 Property.setPropertyValue(values, nameFormula[i], value);
				
				 
			 }
		
		}
		
	}
	
	
}
