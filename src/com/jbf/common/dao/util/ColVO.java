/************************************************************
 * 类名：ColVO.java
 *
 * 类别：VO
 * 功能：列对象VO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.dao.util;

import com.jbf.common.util.StringUtil;

public class ColVO {
	/**
	 * 列名称
	 */
	private String name = "";
	/**
	 * 列是否为数字
	 */
	private boolean isnumber = false;
	
	/**
	 * 是否允许为空
	 */
	private int nullable = 1;
	/**
	 * 列是否为主键
	 */
	private boolean isKey = false;
	/**
	 * 列缺省值
	 */
	private String defVal = null;
	
	/**
	 * 列长度
	 */
	private int length = 0;
	
	/**
	 * 小数位数
	 */
	private int decimalDigits;
	
	/**
	 * 数据源依赖的类型名称，对于 UDT，该类型名称是完全限定的
	 */
	private String typename;
	
	/**
	 * 来自 java.sql.Types 的 SQL 类型 
	 */
	private int datatype;
	
	/**
	 * 列的注释
	 */
	private String remarks = "";
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isKey() {
		return isKey;
	}
	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}
	public boolean isIsnumber() {
		return isnumber;
	}
	public void setIsnumber(boolean isnumber) {
		this.isnumber = isnumber;
	}
	public int getNullable() {
		return nullable;
	}
	public void setNullable(int nullable) {
		this.nullable = nullable;
	}
	public String getDefVal() {
		return defVal;
	}
	public void setDefVal(String defVal) {
		this.defVal = (defVal == null ? null : StringUtil.replaceEnter(defVal, ""));
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getDecimalDigits() {
		return decimalDigits;
	}
	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public int getDatatype() {
		return datatype;
	}
	public void setDatatype(int datatype) {
		this.datatype = datatype;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


}
