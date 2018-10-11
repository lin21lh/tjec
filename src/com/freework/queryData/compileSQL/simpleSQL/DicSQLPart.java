package com.freework.queryData.compileSQL.simpleSQL;

import java.util.List;
import java.util.Map;

import com.freework.base.util.SqlUtil;
import com.freework.queryData.servcie.QueryData;

public class DicSQLPart implements ISQLPart, RegisterSQLPart {
	String dicId,srcId, tgtId, tgtName,alias;
	List<String> paramNames;
	
	
	private String targetSql = "";

	@Override
	public String getMethodName() {
		return "dic";
	}

	@Override
	public ISQLPart analyze(String args, String sql, SQLPartAnalyze analyze) {
		DicSQLPart sqlPart=	new DicSQLPart();
		String argsArray[]=args.split(",");

		sqlPart.dicId=argsArray[0];
		sqlPart.srcId=argsArray[1];
		sqlPart.tgtId=argsArray[2];
		sqlPart.tgtName=argsArray[3];
		if(argsArray.length>5){
			sqlPart.alias=argsArray[4];
		}else{
			sqlPart.alias=sqlPart.srcId;
			
		}
		sqlPart.paramNames = SqlUtil.getSqlParameter(sqlPart.dicId, PARAM_START_CHAR,PARAM_END_CHAR);
	
		
		return sqlPart;
	}


	
	@Override
	public List<String> getParamNames() {
		return paramNames;
	}

	@Override
	public void addSqlAndParam(Object uvalue, List<Object> params,
			StringBuilder sql) {
		String dicId=this.dicId;
		for (String str : paramNames) {
			Object value;
		
				value = SQLPart.getParamValue(str,uvalue);
				if(value==null)
					value="";
				   dicId=dicId.replace(PARAM_START_CHAR+str+PARAM_END_CHAR, value.toString());

			
			
		}
		sql.append("(CASE ");
		 List<Map> dic=QueryData.queryById(dicId);
		 for (Map map : dic) {
				sql.append(" when ").append(srcId).append("='").append(map.get(tgtId)).append("' then '").append(map.get(tgtName)).append("' ");

		}
		sql.append("  ELSE NULL END ) ").append(alias);
	
	}
	

}
