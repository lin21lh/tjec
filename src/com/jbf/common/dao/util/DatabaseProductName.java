/************************************************************
 * 类名：DatabaseProductName.java
 *
 * 类别：枚举类
 * 功能：数据库产品名称枚举类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.dao.util;

public enum DatabaseProductName {

	ORACLE("Oracle", 1), MYSQL("MySQL", 2), SQLSERVER("Microsoft SQL Server", 3);
	
	private String value;
	private int index;
	
	private DatabaseProductName(String value, int index) {
		this.value = value;
		this.index = index;
	}
	
	public static int getDataBaseIndex(String value) {
		
		for (DatabaseProductName d : DatabaseProductName.values()) {
			if (d.getValue().equals(value))
				return d.getIndex();
		}
		return 0;
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
