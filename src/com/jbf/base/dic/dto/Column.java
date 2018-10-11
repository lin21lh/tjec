/************************************************************
 * 类名：Column.java
 *
 * 类别：DTO
 * 功能：grid列类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-6-19  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.dto;
public class Column {
	String field;
	String title;
	String width;
	Boolean hidden;
	Boolean isKeyColumn = false;
	String formatter;
	
	public Column() {
		
	}
	
	public Column(String field, Boolean isKeyColumn, String title, String width, Boolean hidden) {
		this.field = field;
		this.isKeyColumn = isKeyColumn;
		this.title = title;
		this.width = width;
		this.hidden = hidden;
	}
	
	public String toString() {
		if (this.isKeyColumn) //复选框
			return "{field:\"" + field + "\", checkbox : true}";
		else
			return "{field:\"" + field + "\",title:\"" + title + "\",width:" + width + ", hidden:" + hidden+ "}";
	}
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public Boolean getHidden() {
		return hidden;
	}
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	public Boolean getIsKeyColumn() {
		return isKeyColumn;
	}
	public void setIsKeyColumn(Boolean isKeyColumn) {
		this.isKeyColumn = isKeyColumn;
	}
}