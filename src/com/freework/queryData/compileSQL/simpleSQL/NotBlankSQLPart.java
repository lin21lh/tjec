package com.freework.queryData.compileSQL.simpleSQL;

import java.util.Arrays;
import java.util.List;

import com.freework.base.util.SqlUtil;

public class NotBlankSQLPart implements ISQLPart {
	private List<String> paramNames;
	private String targetSql="";

	public NotBlankSQLPart(String sourceSql){
		paramNames = SqlUtil.getSqlParameter(sourceSql, PARAM_START_CHAR,PARAM_END_CHAR);

		targetSql=sourceSql;
		if(paramNames.size()==0){
			paramNames=null;
		}else{
			for (String paramName : paramNames) {
				targetSql = targetSql.replace(PARAM_START_CHAR + paramName+ PARAM_END_CHAR, "?");
			}
		}

		
	}
	
	@Override
	public List<String> getParamNames() {
		return paramNames;
	}


	@Override
	public void addSqlAndParam(Object uvalue, List<Object> params,
			StringBuilder sql) {
		if(paramNames!=null){
			Object[] objects=new Object[paramNames.size()];
			int i=0;
			for (String name : paramNames) {
				Object value=SQLPart.getParamValue(name,uvalue);
				if(value==null||value.equals("")){
					return;
				}
				objects[i]=value;
				i++;
			}
			params.addAll(Arrays.asList(objects));
			sql.append(targetSql);
		}else{
			sql.append(targetSql);
		}
	}



}
