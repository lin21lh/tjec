/************************************************************
 * 类名：PlainScriptPiece
 *
 * 类别：模型类
 * 功能：纯文本数据库脚本片段
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

public class PlainScriptPiece implements ScriptPieceI {

	String script;

	public PlainScriptPiece(String script) {
		this.script = script;
	}

	@Override
	public String getRawScript() {
		return script;
	}

	@Override
	public String getCompiledSqlOrHql(Collection<String> paramNames) {
		return script;
	}

	@Override
	public String getParametricHql(Collection<String> paramNames) {
		return script;
	}

}
