/************************************************************
 * 类名：DataScopeScriptPiece
 *
 * 类别：模型类
 * 功能：带有数据权限的数据库脚本片段
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

public class DataScopeScriptPiece implements ScriptPieceI {

	String script;

	public DataScopeScriptPiece(String script) {
		this.script = script;
	}

	@Override
	public String getRawScript() {
		return script;
	}

	@Override
	public String getCompiledSqlOrHql(Collection<String> paramNames) {

		return "";
	}

	@Override
	public String getParametricHql(Collection<String> paramNames) {
		
		return "";
	}

}
