/************************************************************
 * 类名：ReadExcelConfig.java
 *
 * 类别：Service接口类
 * 功能：导出Excel
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-01-21  CFIT-PM   mqs        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.service;

import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.jbf.base.excel.core.vo.ExcelCfg;
import com.jbf.base.excel.core.vo.ExcelVersion;
import com.jbf.common.exception.AppException;

public interface SysExcelOutService {

	/**
	 * 获取Excel配置
	 * @param filename
	 * @param excelVersion
	 * @return
	 */
	public ExcelCfg getExcelCfg(String filename, String excelVersion);
	
	/**
	 * 导出Excel
	 * @param filename
	 * @param excelCfg
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Workbook outExcel(String filename, ExcelCfg excelCfg,Map paramMap) throws Exception;
	
	/**
	 * 导出Excel
	 * @param title 导出Excel文件名
	 * @param excelVersion Excel版本
	 * @param headers 表头
	 * @param datas 数据
	 * @param includeHidden 是否包含隐藏列
	 * @return
	 * @throws AppException
	 */
	public Workbook outExcelCurrentPage(String title, ExcelVersion excelVersion, String headers, String datas,boolean includeHidden) throws AppException;
}
