package com.freework.queryData.compileSQL;

import java.util.List;

public class SqlAndParam{
	String sql;
	List<Object> param=null;
	
	public SqlAndParam(String sql, List<Object> param) {
		this.sql = sql;
		this.param = param;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public List<Object> getParam() {
		return param;
	}
	
	public Object[] getParamArray() {
		return param.toArray();
	}
	public void setParam(List<Object> param) {
		this.param = param;
	}
}