/************************************************************
 * 类名：ReadExcelI.java
 *
 * 类别：工具类接口
 * 功能：通过xml配置文件读取excel文件数据
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-03-03  CFIT-PM   mqy         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.excel;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.jbf.base.excel.core.xml.XmlCfgI;

public interface ReadExcelI {
	/**
	 * 得到Excel列表数据
	 * 
	 * @param file
	 *            Excel输入流
	 * @param sheetId
	 *            sheet页号（从0始）
	 * @return
	 * @throws Exception
	 */
	public List<Map> getListData(File file, String sheetId) throws Exception;

	public List<Map> getListData(File file, String sheetId, String listId)
			throws Exception;

	/**
	 * 得到Excel表单数据
	 * 
	 * @param file
	 *            Excel输入流
	 * @param sheetId
	 *            sheet页号（从0始）
	 * @return
	 * @throws Exception
	 */
	public Map getFormData(File file, String sheetId) throws Exception;

	public Map getFormData(File file, String sheetId, String formId)
			throws Exception;

	/**
	 * 获取Excel的Xml配置
	 * 
	 * @return
	 */
	public XmlCfgI getCfg();

	/**
	 * 处理业务数据异常
	 * 
	 * @param msg
	 * @throws BizDataException
	 */
	public void handleDataException(String msg) throws Exception;

	/**
	 * 得到数据异常信息（dataexception=insert时才可能有值）
	 * 
	 * @return
	 */
	public String getDataExceptionMsg();

	/**
	 * 得到本次导入的UUID
	 * 
	 * @return
	 */
	public String getUUID();

}
