package com.freework.queryData.compileSQL.simpleSQL;

import java.util.ArrayList;
import java.util.List;

public class SQLEscape implements RegisterSQLPart, ISQLPart {
	private  String sql;
	@Override
	public List<String> getParamNames() {
		return new ArrayList<String>(0);
	}

	@Override
	public void addSqlAndParam(Object uvalue, List<Object> params,
			StringBuilder sql) {
		sql.append(this.sql);
	}

	@Override
	public String getMethodName() {
		return "e";
	}
	@Override
	public ISQLPart analyze(String args, String sql, SQLPartAnalyze analyze) {
		SQLEscape e=	new SQLEscape();
		e.sql=args;
		return e;
	}

}
