package com.freework.queryData.compileSQL;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CompileSQL {
	/**
	 * ����sql�����Բ���������
	 * @return
	 */
	Set<String> getParamNames();
	/**
	 * ����ԭsql
	 * @return
	 */
	String getSourceSql();
	/**
	 * ����ִ��sql
	 * @param param sql�еĲ���
	 * @return
	 */
	SqlAndParam getSQL(Object param);

	
}
