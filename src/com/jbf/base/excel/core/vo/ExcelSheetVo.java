/************************************************************
 * 类名：ExcelSheetVo.java
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

import java.util.List;

public class ExcelSheetVo {
	
	private String sheetName;
	private String scenecode;
	private String elementcode;
	private String table = "";
	private List<ExcelHeadVo> excelHeadList=null;
	private String sql = "";
	private String beanname = "";
	private String method = "";
	private int startrow = 0;
	
	public ExcelSheetVo() {
		
	}
	
	public ExcelSheetVo(String sheetName, String scenecode, String elementcode, String table, List<ExcelHeadVo> excelHeadList, String sql, String beanname, String method, int startrow) {
		this.sheetName = sheetName;
		this.scenecode = scenecode;
		this.elementcode = elementcode;
		this.table = table;
		this.excelHeadList = excelHeadList;
		this.sql = sql;
		this.beanname = beanname;
		this.method = method;
		this.startrow = startrow;
	}
	
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public String getScenecode() {
		return scenecode;
	}
	public void setScenecode(String scenecode) {
		this.scenecode = scenecode;
	}
	public String getElementcode() {
		return elementcode;
	}
	public void setElementcode(String elementcode) {
		this.elementcode = elementcode;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public List<ExcelHeadVo> getExcelHeadList() {
		return excelHeadList;
	}
	public void setExcelHeadList(List<ExcelHeadVo> excelHeadList) {
		this.excelHeadList = excelHeadList;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getBeanname() {
		return beanname;
	}
	public void setBeanname(String beanname) {
		this.beanname = beanname;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public int getStartrow() {
		return startrow;
	}
	public void setStartrow(int startrow) {
		this.startrow = startrow;
	}
	
}
