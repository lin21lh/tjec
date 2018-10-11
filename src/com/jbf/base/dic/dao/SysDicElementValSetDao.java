/************************************************************
 * 类名：SysDicElementValSetDao.java
 *
 * 类别：DAO接口
 * 功能：数据项值集DAO接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-10  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.dao;

import java.util.List;
import java.util.Map;

import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.exception.AppException;

public interface SysDicElementValSetDao {

	/**
	 * 获取数据项值集
	 * @param tablePO 数据表PO
	 * @param elementcode 数据项编码
	 * @param viewFilterSql 过滤条件SQL
	 * @return 数据项值集集合
	 */
	public List<Map> queryDicElementVals(SysDicTable tablePO, String elementcode, String viewFilterSql);
	
	/**
	 * 获取数据项值集
	 * @param tablePO 数据表PO
	 * @param elementcode 数据项编码
	 * @param viewFilterSql 过滤条件SQL
	 * @return 数据项值集集合
	 */
	public List<Map> queryDicElementVals(SysDicTable tablePO, String elementcode, String viewFilterSql, String CustomSql, String conditionFilterSql, String relationSql);
	
	/**
	 * 获取数据项值集 分页
	 * @param dicTable 数据表PO
	 * @param elementcode 数据项编码
	 * @param page 当前页
	 * @param rows 分页条数
	 * @return 数据项值集集合
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 */
	public EasyUITotalResult queryPageDicElementVals(SysDicTable dicTable, String elementcode, Integer page, Integer rows)  throws SecurityException, ClassNotFoundException, NoSuchFieldException;
	
	/**
	 * 查询
	 * @param tablename 表名
	 * @param where 条件
	 * @return 值集合
	 */
	public List query(String tablename, String where);
	
	/**
	 * 值集添加
	 * @param values 值集Map
	 * @param tablename 表名
	 * @return
	 * @throws Exception
	 */
	public Map add(Map<String, Object> values, String tablename) throws Exception;
	
	/**
	 * 值集修改
	 * @param values 值集Map
	 * @param tablename 表名
	 * @return
	 * @throws Exception
	 */
	public Map update(Map<String, Object> values, String tablename) throws Exception;
	
	/**
	 * 值集删除
	 * @param tablename 表名
	 * @param where 条件
	 * @return
	 */
	public Boolean delete(String tablename, String where);
	
	/**
	 * 获取值集明细
	 * @param dicTable 数据表PO
	 * @param id 值集ID
	 * @return 值集Object
	 * @throws Exception
	 */
	public Object getByID(SysDicTable dicTable, Long id) throws Exception;
	
	/**
	 * 获取值集明细
	 * @param tablename 表名
	 * @param elementcode 数据项编码
	 * @param code 值集编码
	 * @return 值集Map
	 */
	public Map getByElementAndCode(String tablename, String elementcode, String code);
	
	/**
	 * 删除层码值集
	 * @param dicTable 数据表PO
	 * @param id 值集ID
	 * @return 是否删除成功 true=成功 false=失败
	 * @throws AppException
	 */
	public boolean deleteDicTreeElementVal(SysDicTable dicTable, Long id) throws AppException;
	
	/**
	 * 获取值集ID
	 * @param dicTable 数据表PO
	 * @param elementcode 数据项编码
	 * @param code 值集编码
	 * @return 值集ID
	 * @throws AppException
	 */
	public Long findIDByCodeElement(SysDicTable dicTable, String elementcode, String code) throws AppException;
	
	/**
	 * 查询值集
	 * @param dicElement 数据项PO
	 * @param dicTable 数据表PO
	 * @param idColumn ID列
	 * @param textColumn text 显示列
	 * @param viewFilterSql 过滤条件SQL
	 * @return 值集集合
	 */
	public List findByElemenetcode(SysDicElement dicElement, SysDicTable dicTable, String idColumn, String textColumn, String viewFilterSql,
			String customSql, String conditionFilterSql, String relationSql);
}
