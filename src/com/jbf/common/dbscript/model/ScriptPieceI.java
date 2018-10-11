/************************************************************
 * 类名：ScriptPieceI
 *
 * 类别：模型类接口
 * 功能：数据库脚本片段接口
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   hyf         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.dbscript.model;

import java.util.Collection;

public interface ScriptPieceI {

	/**
	 * 取得未原始脚本
	 * 
	 * @return
	 */
	public String getRawScript();

	/**
	 * 取得填充参数后的脚本
	 * 
	 * @param params
	 * @return
	 */
	public String getCompiledSqlOrHql(Collection<String> paramNames);

	public String getParametricHql(Collection<String> paramNames);

}
