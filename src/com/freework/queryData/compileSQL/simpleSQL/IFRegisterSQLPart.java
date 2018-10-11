package com.freework.queryData.compileSQL.simpleSQL;

import java.util.List;

import com.freework.base.formula.LogicFormula;
import com.freework.base.formula.LoigcFormulaAnalyse;
import com.freework.base.util.SqlUtil;

public class IFRegisterSQLPart implements RegisterSQLPart {

	@Override
	public String getMethodName() {
		return "if";
	}


	public ISQLPart createIFSQLPart(String sql,String args){
		
		LoigcFormulaAnalyse   analyse=new LoigcFormulaAnalyse(args);
	 	String targetSql=sql;
	 	List<String>	paramNames=analyse.getVariableList();
	 	List<String> paramNames2=SqlUtil.getSqlParameter(targetSql,ISQLPart.PARAM_START_CHAR, ISQLPart.PARAM_END_CHAR);
		paramNames.addAll(paramNames2);
		LogicFormula	formula=analyse.getFormula();
		for (String paramName : paramNames2) {
			targetSql=targetSql.replace(ISQLPart.PARAM_START_CHAR+paramName+ISQLPart.PARAM_END_CHAR, "?");
		}
		IFSQLPart sqlPart=new IFSQLPart();
		sqlPart.setFormula(formula);
		sqlPart.setTargetSql(targetSql);
		sqlPart.setParamNames(paramNames);
		sqlPart.setParamNames2(paramNames2);
		return sqlPart;
	}
	

	public ISQLPart analyze(String args, String sql,SQLPartAnalyze analyze) {
			List<String> sqlPartStrs=analyze.getSQLPartStr(sql);
			if(sqlPartStrs.size()==0){

				return this.createIFSQLPart(sql, args);
			}else{
				LoigcFormulaAnalyse   analyse=new LoigcFormulaAnalyse(args);
				List<ISQLPart> sqlPartList=analyze.analyze(sql,sqlPartStrs);
				IFSQLListPart sqlPart=new IFSQLListPart();
				sqlPart.setSQLPartList(sqlPartList);
				sqlPart.setParamNames(analyse.getVariableList());
				sqlPart.setFormula(analyse.getFormula());
				return sqlPart;
			}
			
			
		
	}

}
