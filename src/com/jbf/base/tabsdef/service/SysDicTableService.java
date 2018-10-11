/************************************************************
 * 类名：SysDicTableService.java
 *
 * 类别：Service
 * 功能：数据表Service
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.service;

import java.util.List;
import java.util.Map;

import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;

public interface SysDicTableService {

	/**
	 * 修改数据表记录
	 * 
	 * @param dicTable
	 *            数据表对象
	 */
	Long edit(SysDicTable dicTable) throws Exception;

	/**
	 * 删除数据表记录
	 * 
	 * @param tableid
	 *            数据表ID
	 */
	void delete(Long tableid) throws Exception;

	/**
	 * 查询数据表记录
	 * 
	 * @param tablecode
	 *            数据表编码
	 * @param tablename
	 *            数据表名称
	 * @param pageNumber
	 *            当前页码
	 * @param pageSize
	 *            当页记录数
	 * @return 数据表分页数据
	 */
	public PaginationSupport query(Map<String, Object> params) throws AppException;

	
	public List<SysDicTable> searchTables(Map<String, Object> params);
	
	/**
	 * 查询所有的表，用于工作表选择数据表
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List queryAllTables(String tablecode, String tablename);
	
	/**
	 * 查询配置字段的表，用于复制表字段
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List queryTabsToCopyCol(String tablecode, String tablename);

	/**
	 * 按id取得对象
	 * 
	 * @param tableid
	 * @return
	 */
	public SysDicTable get(Long tableid);

}
