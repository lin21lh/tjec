/************************************************************
 * 类名：ExcelCfg.java
 *
 * 类别：工具类
 * 功能：提供Excel数据写出，针对2007及以后版本(.xlsx)
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-1-22  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.vo;

import java.util.List;

public class ExcelCfg {
	
	private String excelName;
	private ExcelVersion excelVersion;
	private String excelTemplateFile;
	private List<ExcelSheetVo> excelSheetVoList = null;
	
	public ExcelCfg() {
		
	}
	
	public ExcelCfg(String excelName, ExcelVersion excelVersion, String excelTemplateFile, List<ExcelSheetVo> excelSheetVoList) {
		this.excelName = excelName;
		this.excelVersion = excelVersion;
		this.excelTemplateFile = excelTemplateFile;
		this.excelSheetVoList = excelSheetVoList;
	}
	
	public String getExcelName() {
		return excelName;
	}
	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}
	public ExcelVersion getExcelVersion() {
		return excelVersion;
	}
	public void setExcelVersion(ExcelVersion excelVersion) {
		this.excelVersion = excelVersion;
	}
	public String getExcelTemplateFile() {
		return excelTemplateFile;
	}
	public void setExcelTemplateFile(String excelTemplateFile) {
		this.excelTemplateFile = excelTemplateFile;
	}
	public List<ExcelSheetVo> getExcelSheetVoList() {
		return excelSheetVoList;
	}
	public void setExcelSheetVoList(List<ExcelSheetVo> excelSheetVoList) {
		this.excelSheetVoList = excelSheetVoList;
	}
}
