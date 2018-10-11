package com.freework.queryData.compileSQL.simpleSQL;

import java.util.List;

import com.freework.base.formula.LogicFormula;

public class IFSQLPart implements ISQLPart{
	
	private String targetSql="";
	private List<String> paramNames;
	private List<String> paramNames2;
	private LogicFormula formula=null;
	
	public void setTargetSql(String targetSql) {
		this.targetSql = targetSql;
	}

	public void setParamNames(List<String> paramNames) {
		this.paramNames = paramNames;
	}

	public void setParamNames2(List<String> paramNames2) {
		this.paramNames2 = paramNames2;
	}

	public void setFormula(LogicFormula formula) {
		this.formula = formula;
	}

	public List<String> getParamNames() {
		return paramNames;
	}
	
	@Override
	public void addSqlAndParam(Object uvalue,List<Object> params,StringBuilder sql){
		if(formula.compareTo(uvalue)){
			for (String name : paramNames2) {
				Object value=SQLPart.getParamValue(name,uvalue);
				params.add(value);
			}
			sql.append(targetSql);

			
			
		}
		
		
	}


	public String  toString(){
		return targetSql;
	}
	
	
	

	

	
	
	
	

}
