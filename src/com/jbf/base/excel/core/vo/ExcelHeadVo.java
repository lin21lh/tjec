/************************************************************
 * 类名：ExcelHeadVO.java
 *
 * 类别：vo类
 * 功能：excel文件vo
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-01-21  CFIT-PM   mqs        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.vo;

public class ExcelHeadVo {


	private String colname;
	private String title;
	private boolean isNumber = false;
	private boolean isHidden = false;
	private int width = 17;
	
	public ExcelHeadVo() {
		
	}
	
	public ExcelHeadVo(String colname, String title, boolean isNumber, boolean isHidden, int width) {
		this.colname = colname;
		this.title = title;
		this.isNumber = isNumber;
		this.isHidden = isHidden;
		this.width = width;
	}
	
	public String getColname() {
		return colname;
	}
	public void setColname(String colname) {
		this.colname = colname;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isNumber() {
		return isNumber;
	}
	public void setIsNumber(boolean isNumber) {
		this.isNumber = isNumber;
	}
	public boolean isHidden() {
		return isHidden;
	}
	public void setIsHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
}
