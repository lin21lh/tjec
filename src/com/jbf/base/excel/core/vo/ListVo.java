/************************************************************
 * 类名：ListVo.java
 *
 * 类别：vo类
 * 功能：ListVo 列表vo
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-03-03  CFIT-PM   mqy        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.vo;

public class ListVo implements BaseVo {
	private String id = "";
	private String name = "";
	private String startrow = ""; // 起始行
	private String endrow = ""; // 结束行
	private String startcol = ""; // 起始列(startrow、startcol不能同时有值)
	private String endcol = ""; // 结束列
	private String tablename = ""; // 表名称
	private String uuidfield = ""; // uuid存放的字段名
	private String rule = null; // 验证规则：根据具体的业务需求确定，由具体的业务系统解析。
	private String ruleinfo = null; // 验证失败的提示信息。
	private String checkrule = null; // 是否启用验证规则。

	private boolean vertlist = false;// 是否是竖向列表
	private String typecode = "";// 1横向列表2竖向列表3表单

	private String poclassname=""; //hibernate po 类名

	/**
	 * 是否是竖向列表
	 * 
	 * @return
	 */
	public boolean isVertlist() {
		return vertlist;
	}

	public void setVertlist(boolean vertlist) {
		this.vertlist = vertlist;
	}

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

	public String getStartrow() {
		return startrow;
	}

	public void setStartrow(String startrow) {
		this.startrow = startrow;
	}

	public String getEndrow() {
		return endrow;
	}

	public void setEndrow(String endrow) {
		this.endrow = endrow;
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

	public String getStartcol() {
		return startcol;
	}

	public void setStartcol(String startcol) {
		this.startcol = startcol;
	}

	public String getEndcol() {
		return endcol;
	}

	public void setEndcol(String endcol) {
		this.endcol = endcol;
	}

	public String getTypecode() {
		return typecode;
	}

	public void setTypecode(String typecode) {
		this.typecode = typecode;
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
