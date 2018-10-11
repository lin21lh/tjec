package com.freework.queryData.compileSQL.simpleSQL;
import java.util.List;

import com.freework.base.formula.LogicFormula;
public class IFSQLListPart implements ISQLPart {
	private List<ISQLPart> sqlPartList=null;
	
	private List<String> paramNames=null;
	private LogicFormula formula=null;

	public void setFormula(LogicFormula formula) {
		this.formula = formula;
	}

	public void setSQLPartList(List<ISQLPart> sqlPartList) {
		this.sqlPartList = sqlPartList;

	}

	public void setParamNames(List<String> paramNames) {
		this.paramNames = paramNames;
	}

	@Override
	public List<String> getParamNames() {
		MyList<String> paramNames=new MyList<String>();
		
		paramNames.addAllList(this.paramNames);
		for (ISQLPart sqlPart : sqlPartList) {
			if(sqlPart.getParamNames()!=null)
				paramNames.addAllList(sqlPart.getParamNames());
		}
		return paramNames;
	}

	@Override
	public void addSqlAndParam(Object uvalue, List<Object> params,
			StringBuilder sql) {
		if(formula.compareTo(uvalue)){
			for (ISQLPart sqlPart : sqlPartList) {
				sqlPart.addSqlAndParam(uvalue, params, sql);
			}
		}
		
		
	}

	public String toString(){
		StringBuilder str=new StringBuilder();
		for (ISQLPart sqlPart : sqlPartList) {
			str.append(sqlPart);
		}
		return str.toString();
	}

}
