package com.freework.freedbm.util.listUtil.group.Aggregate;

import java.math.BigDecimal;
import java.util.Map;

import com.freework.base.formula.FormulaAbstract;
import com.freework.base.formula.FormulaAnalyse;
import com.freework.base.formula.LogicFormula;
import com.freework.base.formula.LoigcFormulaAnalyse;
import com.freework.base.util.NumberUtil;
import com.freework.freedbm.util.listUtil.group.AggregateCol;

public class DecodeSumData extends FormulaAbstract implements AggregateData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	LogicFormula formula=null;
	
	public  Object[]  getValValue(String sval){
		Object[] objs=new Object[2];
		 if(NumberUtil.isNumber(sval)){
				objs[0]=CONVENTION;
				objs[1]=NumberUtil.bigDecimal(sval);
			}else if(FormulaAnalyse.isFormula(sval)){
		    		FormulaAnalyse f=new FormulaAnalyse(sval);
		    		objs[0]=FORMULA;
		    		objs[1]=f.getFormula();
		    	
		    	}else{
		    		objs[0]=VARIABLE;
		    		objs[1]=sval;
		    		
		    	}
		    	
//		 if(sval.length()>=2&&sval.charAt(0)=='\''&&sval.charAt(sval.length()-1)=='\''){
//				objs[0]=CONVENTION;
//	    		objs[1]=sval.substring(1, sval.length()-1);
//				
//			}else
	 
		return objs;
	} 
	public AggregateData newAggregateData(String[] args) {
		DecodeSumData decode=new DecodeSumData();
		Object[] objs= getValValue(args[0]);
		decode.lvalType=(Integer) objs[0];
		decode.lval =objs[1];
		
		objs= getValValue(args[1]);
		decode.rvalType=(Integer) objs[0];
		decode.rval =objs[1];
		return decode;
	}

	@Override
	public void setCol(AggregateCol col) {
		formula=new LoigcFormulaAnalyse(col.sourceName).getFormula();
	}

	@Override
	public void setData(Map sourceMap, Map groupMap, String tagertName) {
		BigDecimal sum=null;
		if(formula.compareTo(sourceMap)){
			sum= (BigDecimal) this.getLvalValue(sourceMap);
		}else{
			sum= (BigDecimal) this.getRvalValue(sourceMap);
		}
		sum =NumberUtil.add(groupMap.get(tagertName), sum);
		groupMap.put(tagertName, sum);

	}

	@Override
	public String getName() {
		return "decodeSum";
	}

	


}
