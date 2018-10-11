/************************************************************
 * 类名：GridFieldDTO.java
 *
 * 类别：DTO
 * 功能：grid字段DTO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.dto;

public class GridFieldDTO {

	String name; //属性名称
	String width; //列宽度
	String algin;
	Integer istotal; //是否需要汇总 1=汇总；0=不汇总
	Integer ishidden; // 是否隐藏列 1=隐藏 ；0=显示
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getAlgin() {
		return algin;
	}
	public void setAlgin(String algin) {
		this.algin = algin;
	}
	public Integer getIstotal() {
		return istotal;
	}
	public void setIstotal(Integer istotal) {
		this.istotal = istotal;
	}
	public Integer getIshidden() {
		return ishidden;
	}
	public void setIshidden(Integer ishidden) {
		this.ishidden = ishidden;
	}
}
