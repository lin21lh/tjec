/************************************************************
 * 类名：TableColumnVo.java
 *
 * 类别：VO
 * 功能：数据表字段列VO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-09-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.vo;

public class TableColumnVo {

	String colname;
	String datatype;
	Byte used;
	String remarks;

	public String getColname() {
		return colname;
	}

	public void setColname(String colname) {
		this.colname = colname;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public Byte getUsed() {
		return used;
	}

	public void setUsed(Byte used) {
		this.used = used;
	}
	
	public String getRemarks() {
		return remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
