package com.freework.base.formula.customMethod;

import java.util.List;
import java.util.Map;

import com.freework.base.formula.LogicFormula;
import com.freework.base.formula.LoigcFormulaAnalyse;

public class IFMethod extends MethodAbstract {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2814009585126823496L;
	private LogicFormula formula = null;

	public String getMethodName() {
		return "if";
	}
	
	
	public Object getValue(Object obj) {
		return formula.compareTo(obj)?
							 getValue(lval, obj, lvalType)
							:getValue(rval, obj, rvalType);
	}

	public CustomMethod newCustomMethod(String[] args) {
		IFMethod m=new IFMethod();
		LoigcFormulaAnalyse loigcAnalyse=new LoigcFormulaAnalyse(args[0]);
			m.formula=loigcAnalyse.getFormula();
			if(args[1].charAt(0)=='\''&&args[1].charAt(args[1].length()-1)=='\''){
				m.lvalType=CONVENTION;
				m.lval=args[1].substring(1, args[1].length()-1);
			}else{
				Object[] objs= getValValue(args[1]);
				m.lvalType=(Integer) objs[0];
				m.lval =objs[1];
			}
			if(args[2].charAt(0)=='\''&&args[2].charAt(args[2].length()-1)=='\''){
				m.rvalType=CONVENTION;
				m.rval=args[2].substring(1, args[2].length()-1);
				
			}else{
				Object[] objs= getValValue(args[2]);
				m.rvalType =(Integer) objs[0];
				m.rval=objs[1];
			}
			List<String> var=loigcAnalyse.getVariableList();
			if(var!=null&&var.size()>0){
			   m.getVariableList().addAll(var);
			}
		return m;
	}
	
}
