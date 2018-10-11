package com.freework.queryData.compileSQL.simpleSQL;

import java.util.List;


public interface ISQLPart {
	public static final char PARAM_START_CHAR='{';
	public static final char PARAM_END_CHAR='}';


	public List<String> getParamNames();
	public void addSqlAndParam(Object uvalue,List<Object> params,StringBuilder sql);

}
