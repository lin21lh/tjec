/************************************************************
 * 类名：SysDicTableDao.java
 *
 * 类别：DAO接口
 * 功能：界面设计器DAO接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.dao;

import java.util.List;
import java.util.Map;

import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.dao.IGenericDao;
import com.jbf.common.dao.PaginationSupport;

public interface SysDicTableDao extends IGenericDao<SysDicTable, Long> {

	@SuppressWarnings("rawtypes")
	public List query(String tablename);

	/**
	 * 查询数据表
	 * 
	 * @param params
	 *            包括表编码、表名、表类型等条件
	 * @return 数据表列表
	 */
	public PaginationSupport query(Map<String, Object> params);

	/**
	 * 查询数据表
	 * 
	 * @param params
	 *            查询条件为表编码
	 * @return 数据表列表
	 */
	public List<SysDicTable> searchTables(Map<String, Object> params);

	/**
	 * 通过tablecode 查询表对象
	 * 
	 * @param tablecode
	 * @return 数据表详情
	 */
	public SysDicTable getByTablecode(String tablecode);

	/**
	 * 通过Elementcode 查询表对象
	 * 
	 * @param elementcode
	 *            数据项编码
	 * @return 数据表详情
	 */
	public SysDicTable getByElementcode(String elementcode);

	/**
	 * 从缓存中去数据
	 * 
	 * @param tablecode
	 *            表编码
	 * @return 数据表详情
	 */
	public SysDicTable getDicTable(String tablecode);
}
