/************************************************************
 * 类名：ListVo.java
 *
 * 类别：vo类
 * 功能：sheet页vo
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-03-03  CFIT-PM   mqy        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.vo;

public class SheetVo implements BaseVo {

	private String id = "";
	private String name = "";

	private String cfgid = "";
	private String cfgname = "";

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

	public String getCfgname() {
		return cfgname;
	}

	public void setCfgname(String cfgname) {
		this.cfgname = cfgname;
	}

	public String getCfgid() {
		return cfgid;
	}

	public void setCfgid(String cfgid) {
		this.cfgid = cfgid;
	}

}
