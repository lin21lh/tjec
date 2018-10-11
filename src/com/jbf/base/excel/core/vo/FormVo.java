/************************************************************
 * 类名：FormVo.java
 *
 * 类别：vo类
 * 功能：Form表单Vo
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-03-03  CFIT-PM   mqy        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.vo;

public class FormVo implements BaseVo {
	private String id = "";
	private String name = "";
	private String tablename = ""; // 表名称
	private String uuidfield = ""; // uuid存放的字段名
	private String rule = null; // 验证规则：根据具体的业务需求确定，由具体的业务系统解析。
	private String ruleinfo = null; // 验证失败的提示信息。
	private String checkrule = null; // 是否启用验证规则。
	private String poclassname = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuidfield() {
		return uuidfield;
	}

	public void setUuidfield(String uuidfield) {
		this.uuidfield = uuidfield;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getRuleinfo() {
		return ruleinfo;
	}

	public void setRuleinfo(String ruleinfo) {
		this.ruleinfo = ruleinfo;
	}

	public String getCheckrule() {
		return checkrule;
	}

	public void setCheckrule(String checkrule) {
		this.checkrule = checkrule;
	}

	public String getPoclassname() {
		return poclassname;
	}

	public void setPoclassname(String poclassname) {
		this.poclassname = poclassname;
	}

}
