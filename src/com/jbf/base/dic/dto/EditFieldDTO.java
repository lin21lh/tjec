/************************************************************
 * 类名：EditFieldDTO.java
 *
 * 类别：DTO
 * 功能： form表单维护属性字段DTO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.dto;

public class EditFieldDTO {

	String name = ""; //属性字段名称
	Integer mergedcols = 1; //所占列数
	Integer row = 1; //所在行
	Integer col = 1; //所在列
	Integer notnull = 0; //是否必填 1=必填； 0=非必填
	Integer isunique = 0; //是否唯一 1=唯一；0=不唯一
	String controlname =""; //控件名称
	String defaultValue = ""; //默认值
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getMergedcols() {
		return mergedcols;
	}
	public void setMergedcols(Integer mergedcols) {
		this.mergedcols = mergedcols;
	}
	public Integer getRow() {
		return row;
	}
	public void setRow(Integer row) {
		this.row = row;
	}
	public Integer getCol() {
		return col;
	}
	public void setCol(Integer col) {
		this.col = col;
	}
	public Integer getNotnull() {
		return notnull;
	}
	public void setNotnull(Integer notnull) {
		this.notnull = notnull;
	}
	public Integer getIsunique() {
		return isunique;
	}
	public void setIsunique(Integer isunique) {
		this.isunique = isunique;
	}
	public String getControlname() {
		return controlname;
	}
	public void setControlname(String controlname) {
		this.controlname = controlname;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
