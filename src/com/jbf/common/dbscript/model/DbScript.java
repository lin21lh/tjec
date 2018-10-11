/************************************************************
 * 类名：DbScript
 *
 * 类别：PO
 * 功能：数据类脚本po
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   hyf         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.dbscript.model;

public class DbScript {
	String name; // 名称
	String type; // 分类
	String remark; // 备注
	String script; // 脚本

	public DbScript(String name, String type, String remark, String script) {

		this.name = name;
		this.type = type;
		this.remark = remark;
		this.script = script;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

}
