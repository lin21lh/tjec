/************************************************************
 * 类名：TableType.java
 *
 * 类别：枚举类
 * 功能：表类型枚举项 
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-6-19  CFIT-PM   maqs        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.po;

public enum TableType {
	
	DicTable("字典表", Byte.parseByte("0")), 
//	DicView("业务表", Byte.parseByte("1"));
	BusinessTable("业务表", Byte.parseByte("1"));
	
	private String name; //名称
	private Byte index; //实际数据库中值
	
	private TableType(String name, Byte index) {
		this.name = name;
		this.index = index;
	}
	
	public String getNameByIndex(Byte index) {
		
		for (TableType t : TableType.values()) {
			if (index == t.getIndex())
				return t.getName();
		}
		return null;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Byte getIndex() {
		return index;
	}
	
	public void setIndex(Byte index) {
		this.index = index;
	}
}
