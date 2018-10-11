/************************************************************
 * 类名：XmlCfgI.java
 *
 * 类别：配置接口
 * 功能：读取xml配置文件得到配置信息
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-03-03  CFIT-PM   mqy        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.xml;

import java.util.List;
import java.util.Map;

import com.jbf.base.excel.core.vo.CellVo;
import com.jbf.base.excel.core.vo.ExcelVo;
import com.jbf.base.excel.core.vo.FormVo;
import com.jbf.base.excel.core.vo.ListVo;
import com.jbf.base.excel.core.vo.SheetVo;

public interface XmlCfgI {
	public ExcelVo getExcelVo();

	/**
	 * 得到列表的列信息
	 * 
	 * @param sheetId
	 * @return
	 */
	public Map<Integer, CellVo> getListCols(String sheetId) throws Exception;

	/**
	 * 得到列表的列信息(不包括虚拟列)
	 * 
	 * @param sheetId
	 * @param listId
	 *            列表Id
	 * @return Map<列号, CellVo>
	 */
	public Map<Integer, CellVo> getListCols(String sheetId, String listId)
			throws Exception;

	/**
	 * 得到列表的列信息(虚拟列)
	 * 
	 * @param sheetId
	 * @param listId
	 *            列表Id
	 * @return
	 */
	public List<CellVo> getListColsElse(String sheetId, String listId);

	/**
	 * 得到列表的列号与数据库字段名的Map
	 * 
	 * @param sheetId
	 * @return
	 */
	public Map<Integer, String> getListColFieldMap(String sheetId)
			throws Exception;

	/**
	 * 得到列表的列号与数据库字段名的Map
	 * 
	 * @param sheetId
	 * @param listId
	 *            列表Id
	 * @return
	 */
	public Map<Integer, String> getListColFieldMap(String sheetId, String listId)
			throws Exception;

	/**
	 * 得到列表的行号与数据库字段名的Map(竖形列表)
	 * 
	 * @param sheetId
	 * @return
	 */
	public Map<Integer, String> getListRowFieldMap(String sheetId, String listId);

	/**
	 * 得到列表的列号与数据库字段名的Map(竖形列表)
	 * 
	 * @param sheetId
	 * @return
	 */
	public Map<Integer, String> getListRowFieldMap(String sheetId);

	/**
	 * 得到列表Vo
	 * 
	 * @param sheetId
	 * @param listId
	 * @return
	 */
	public ListVo getListVo(String sheetId, String listId) throws Exception;

	/**
	 * 得到sheet的所有list
	 * 
	 * @param sheetId
	 * @return
	 */
	public List<ListVo> getListVos(String sheetId);

	/**
	 * 得到表单Vo
	 * 
	 * @param sheetId
	 * @param formId
	 * @return
	 */
	public FormVo getFormVo(String sheetId, String formId);

	/**
	 * 得到sheet的所有form
	 * 
	 * @param sheetId
	 * @return
	 */
	public List<FormVo> getFormVos(String sheetId);

	/**
	 * 得到Form的所有字段信息
	 * 
	 * @param sheetId
	 * @return
	 */
	public List<CellVo> getFormFields(String sheetId);

	/**
	 * 得到Form的所有字段信息
	 * 
	 * @param sheetId
	 * @param formId
	 * @return
	 */
	public List<CellVo> getFormFields(String sheetId, String formId);

	/**
	 * 得到excel的所有sheet
	 * 
	 * @return
	 */
	public List<SheetVo> getSheetVos();

	public Map<String, String> getSheetsMap();

	/**
	 * 得到sheetVo
	 * 
	 * @param sheetId
	 * @return
	 */
	public SheetVo getSheetVo(String sheetId);

	public CellVo getCellVo(String sheetId, String typeId, String fieldname);

	/**
	 * 得到sheet的子，包括列表和表单
	 * 
	 * @param sheetId
	 * @return
	 */
	public List<Map<String, Object>> getSheetChildren(String sheetId);

	/**
	 * 得到列表和表单的子
	 * 
	 * @param sheetId
	 * @param typeid
	 * @param typecode
	 *            1横向列表2竖向列表3表单
	 * @return
	 */
	public List<Map<String, Object>> getListFormChildren(String sheetId,
			String typeid, String typecode);

}
