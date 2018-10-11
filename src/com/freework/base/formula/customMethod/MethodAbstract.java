package com.freework.base.formula.customMethod;

import java.util.ArrayList;
import java.util.List;

import com.freework.base.util.NumberUtil;
import com.freework.base.formula.FormulaAbstract;
import com.freework.base.formula.FormulaAnalyse;

public abstract class MethodAbstract extends FormulaAbstract implements
		CustomMethod {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8671215174644277706L;

	public MethodAbstract(){}
	private List<String> variableList=new ArrayList<String>();
	public  Object[]  getValValue(String sval){
		Object[] objs=new Object[2];
			if(NumberUtil.isNumber(sval)){
				objs[0]=CONVENTION;
				objs[1]=NumberUtil.bigDecimal(sval);
			}else if(FormulaAnalyse.isFormula(sval)){
		    		FormulaAnalyse f=new FormulaAnalyse(sval);
		    		objs[0]=FORMULA;
		    		objs[1]=f.getFormula();
		    	
		    		variableList.addAll(f.getVariableList());
		    	}else if(sval.charAt(0)=='$'){	
			    		objs[0]=CUSTOM_METHOD;
			    	
			    		objs[1]=CustomMethods.getCustomMethod(sval);
			    		if(objs[1] instanceof MethodAbstract){
			    			variableList.addAll(((MethodAbstract)objs[1]).getVariableList());
			    		}
			    		
		    	}else{
		    		objs[0]=VARIABLE;
		    		objs[1]=sval;
		    		if(variableList.contains(sval))
		    		variableList.add(sval);
		    	}
		    	
		
	 
		return objs;
	} 

	public List<String>  getVariableList(){
		return variableList;
		
	}


}
