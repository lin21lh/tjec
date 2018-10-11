package com.freework.freedbm.util.listUtil.group.Aggregate;

import java.math.BigDecimal;
import java.util.Map;

import com.freework.base.formula.Formula;
import com.freework.base.formula.FormulaAnalyse;
import com.freework.base.util.NumberUtil;
import com.freework.freedbm.util.listUtil.group.AggregateCol;

public class SumData implements AggregateData {
	AggregateCol col;
	boolean isFormula=false;
	Formula formula=null;
	public void setCol(AggregateCol col) {
					this.col = col;
					if(FormulaAnalyse.isFormula(col.sourceName)){
						isFormula=true;
						formula=new FormulaAnalyse(col.sourceName).getFormula();

					}
	}
	
	public void setData(Map sourceMap, Map groupMap,String tagertName) {
		BigDecimal sum=null;
		if(isFormula){
			 sum =formula.calculateNumber(sourceMap);
		}else{
			 sum= NumberUtil.bigDecimal(sourceMap.get(col.sourceName));
			
		}
		sum =NumberUtil.add(groupMap.get(tagertName), sum);
		groupMap.put(tagertName, sum);
	}



	public String getName() {
		return "sum";
	}

	public AggregateData newAggregateData(String[] args) {
		return new SumData();
		
	}


}
