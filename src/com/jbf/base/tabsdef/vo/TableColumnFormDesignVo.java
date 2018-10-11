/************************************************************
 * 类名：TableColumnFormDesignVo.java
 *
 * 类别：VO
 * 功能：表单界面字段列VO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-09-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.vo;

public class TableColumnFormDesignVo {

	String name;
	String label;
	String type;
	Byte notNull;
	Byte isUnique;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Byte getNotNull() {
		return notNull;
	}

	public void setNotNull(Byte notNull) {
		this.notNull = notNull;
	}

	public Byte getIsUnique() {
		return isUnique;
	}

	public void setIsUnique(Byte isUnique) {
		this.isUnique = isUnique;
	}

}
