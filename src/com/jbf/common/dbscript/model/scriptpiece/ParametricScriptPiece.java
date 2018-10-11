/************************************************************
 * 类名：ParametricScriptPiece
 *
 * 类别：模型类
 * 功能：带有参数的数据库脚本片段
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   hyf         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.dbscript.model.scriptpiece;

import java.util.Collection;

import com.jbf.common.dbscript.model.ScriptPieceI;

public class ParametricScriptPiece implements ScriptPieceI {

	String script;

	public ParametricScriptPiece(String script) {
		this.script = script;
	}

	@Override
	public String getRawScript() {

		return script;
	}

	@Override
	public String getCompiledSqlOrHql(Collection<String> paramNames) {
		int startIndex = script.indexOf(':');
		int endIndex = script.indexOf(' ', startIndex);
		if (endIndex == -1) {
			endIndex = script.length();
		}
		String paramName = script.substring(startIndex + 1, endIndex);
		if (paramName == null || paramName.length() == 0) {
			return "";
		}
		paramName = paramName.trim();
		if (paramNames.contains(paramName)) {
			// 条件生效
			return script.replaceAll(":" + paramName, "?");
		}
		return "";
	}

	@Override
	public String getParametricHql(Collection<String> paramNames) {

		int startIndex = script.indexOf(':');
		int endIndex = script.indexOf(' ', startIndex);
		if (endIndex == -1) {
			endIndex = script.length();
		}
		String paramName = script.substring(startIndex + 1, endIndex);
		if (paramName == null || paramName.length() == 0) {
			return "";
		}
		paramName = paramName.trim();
		if (paramNames.contains(paramName)) {
			// 条件生效
			return script;
		}
		return "";
	}
}
