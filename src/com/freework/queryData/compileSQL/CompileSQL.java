package com.freework.queryData.compileSQL;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CompileSQL {
	/**
	 * 返回sql中所以参数的名称
	 * @return
	 */
	Set<String> getParamNames();
	/**
	 * 返回原sql
	 * @return
	 */
	String getSourceSql();
	/**
	 * 返回执行sql
	 * @param param sql中的参数
	 * @return
	 */
	SqlAndParam getSQL(Object param);

	
}
