/************************************************************
 * 类名：SysDicUIDesignController.java
 *
 * 类别：Controller
 * 功能：界面设计器controller
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.dao;

import java.util.List;

import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.common.dao.IGenericDao;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;

public interface SysDicColumnDao extends IGenericDao<SysDicColumn, Long> {

	/**
	 * 分页查询数据表列
	 * 
	 * @param tablecode
	 *            表编码
	 * @param columncode
	 *            列编码
	 * @param columnname
	 *            列名
	 * @param page
	 *            页索引
	 * @param rows
	 *            页容量
	 * @return 表列列表
	 */
	public PaginationSupport query(String tablecode, String columncode,
			String columnname, Integer page, Integer rows);

	/**
	 * 按表查询表列
	 * 
	 * @param tablecode
	 *            表编码
	 * @return 列列表
	 */
	public List<SysDicColumn> findColumnsByTablecode(String tablecode);

	/**
	 * 按数据项查询表列
	 * 
	 * @param tableCode
	 *            表编码
	 * @param sourceElement
	 *            源数据项
	 * @return 列详情
	 * @throws AppException
	 */
	public SysDicColumn getColumnBySourceElement(String tableCode,
			String sourceElement) throws AppException;

	/**
	 * 取得列详情
	 * 
	 * @param tablecode
	 *            表编码
	 * @param columncode
	 *            列编码
	 * @return 列详情
	 */
	public SysDicColumn getDiccolumn(String tablecode, String columncode);
}
