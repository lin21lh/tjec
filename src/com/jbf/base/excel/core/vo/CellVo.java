/************************************************************
 * 类名：CellVo.java
 *
 * 类别：vo类
 * 功能：列vo、单元格vo
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-03-03  CFIT-PM   mqy        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.vo;

public class CellVo implements BaseVo {
	private String sheetid = null;
	private String rownum = null; // 行号
	private String colnum = null; // 列号：行号和列号，要么都有值，要不都为空。
	private String fieldname = null; // 字段名：对应数据库，必填。
	private String text = null; // 中文名：可为空。
	private String key = null; // 是否是关键字
	private String datatype = null; // 数据类型：如为decimal，系统自动检查是否为数值。默认值 String。
	private String allownull = null; // 是否可为null：默认可为空。
	private String defaulttype = null; // 0[具体值]；1[函数值]
	private String defaultvalue = null; // 默认值：如为null，则取默认值。如没有行号和列号，认为是附加字段。

	private String typecode = "";// 1横向列表2竖向列表3表单

	public String getSheetid() {
		return sheetid;
	}

	public void setSheetid(String sheetid) {
		this.sheetid = sheetid;
	}

	public String getRownum() {
		return rownum;
	}

	public void setRownum(String rownum) {
		this.rownum = rownum;
	}

	public String getColnum() {
		return colnum;
	}

	public void setColnum(String colnum) {
		this.colnum = colnum;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getAllownull() {
		return allownull;
	}

	public void setAllownull(String allownull) {
		this.allownull = allownull;
	}

	public String getDefaulttype() {
		return defaulttype;
	}

	public void setDefaulttype(String defaulttype) {
		this.defaulttype = defaulttype;
	}

	public String getDefaultvalue() {
		return defaultvalue;
	}

	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}

	public String getTypecode() {
		return typecode;
	}

	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}

}
