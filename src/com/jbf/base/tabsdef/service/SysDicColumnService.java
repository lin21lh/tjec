/************************************************************
 * 类名：SysDicColumnService.java
 *
 * 类别：Service
 * 功能：数据表列 Service
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.service;

import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.web.ResultMsg;

public interface SysDicColumnService {

	/**
	 * 查询数据表列
	 * 
	 * @param tablecode
	 *            表名
	 * @param pageNumber
	 *            页索引
	 * @param pageSize
	 *            页容量
	 * @return 数据表列列表
	 */
	public PaginationSupport query(String tablecode, Integer pageNumber,
			Integer pageSize);

	/**
	 * 修改数据表表列
	 * 
	 * @param col
	 * @throws Exception
	 */
	public void edit(SysDicColumn col) throws Exception;

	/**
	 * 删除表表列
	 * 
	 * @param colid
	 *            列id
	 */
	public void delete(Long colid);

	/**
	 * 查询物理表的表列
	 * 
	 * @param tablecode
	 *            表名
	 * @param pageNumber
	 *            页索引
	 * @param pageSize
	 *            页容量
	 * @return 物理表的表列列表
	 */
	public PaginationSupport queryDbTableColumns(String tablecode,
			Integer pageNumber, Integer pageSize);

	/**
	 * 取得表列的详情
	 * 
	 * @param id
	 *            列id
	 * @return 表列的详情
	 */
	public SysDicColumn get(Long id);

	/**
	 * 取得编码字段详情
	 * 
	 * @param tablecode
	 * @return 编码字段详情
	 */
	public SysDicColumn getCodeColumnDetail(String tablecode);

	/**
	 * 复制表列
	 * 
	 * @param sourceTablecode
	 *            源表
	 * @param targetTablecode
	 *            目标表
	 */
	public void copyColToTargetTable(String sourceTablecode,
			String targetTablecode);

}
