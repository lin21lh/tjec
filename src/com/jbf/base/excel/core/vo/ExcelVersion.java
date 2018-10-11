/************************************************************
 * 类名：ExcelVersion.java
 *
 * 类别：枚举类
 * 功能：excel版本枚举
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-01-21  CFIT-PM   mqs        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.vo;


public enum ExcelVersion {

	Excel2003(2003, "EXCEL2003", "xls"),
	Excel2007(2007, "EXCEL2007", "xlsx");
	
	private int index;
	private String name;
	private String suffix;
	
	ExcelVersion(int index, String name, String suffix) {
		this.index = index;
		this.name = name;
		this.suffix = suffix;
	}
	
	public String getNameByIndex(int index) {
		
		for (ExcelVersion ev : ExcelVersion.values()) {
			if (index == ev.getIndex())
				return ev.getName();
		}
		return null;
	}
	
	public static ExcelVersion getByIndex(int index) {
		for (ExcelVersion ev : ExcelVersion.values()) {
			if (index == ev.getIndex())
				return ev;
		}
		
		return ExcelVersion.Excel2003;
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
