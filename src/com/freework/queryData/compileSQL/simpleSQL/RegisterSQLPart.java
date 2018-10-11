package com.freework.queryData.compileSQL.simpleSQL;


public interface RegisterSQLPart  {

	public  abstract String getMethodName();
	public	abstract ISQLPart analyze(String args,String sql,SQLPartAnalyze analyze);
}
