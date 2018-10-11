package com.freework.queryData.compileSQL.simpleSQL;

import java.util.Arrays;
import java.util.List;

public class StringSQL implements RegisterSQLPart, ISQLPart {

	String name;
	@Override
	public List<String> getParamNames() {
		return Arrays.asList(name);
	}

	@Override
	public void addSqlAndParam(Object uvalue, List<Object> params,
			StringBuilder sql) {

		
			Object value;
			if(uvalue==null){
				value="";
			}else{
				 value=SQLPart.getParamValue(name,uvalue);

			}
			if(value==null)
				value="";
			
			 sql.append(value);
	
	}

	@Override
	public String getMethodName() {
		// TODO Auto-generated method stub
		return "str";
	}

	@Override
	public ISQLPart analyze(String args, String sql, SQLPartAnalyze analyze) {
		StringSQL str=	new StringSQL();
		str.name=args;
		return str;
	}

}
