/************************************************************
 * 类名：SysDicElementDao.java
 *
 * 类别：DAO接口
 * 功能：数据项DAO接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-08  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.dao;

import java.util.List;
import java.util.Map;

import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.dao.IGenericDao;
import com.jbf.common.dao.PaginationSupport;

public interface SysDicElementDao extends IGenericDao<SysDicElement, Long> {

	/**
	 * 获取数据项类型
	 * @return 数据项类型集合
	 */
	@SuppressWarnings("rawtypes")
	public List queryByElementClass();
	
	/**
	 * 获取基础数据项列表
	 * @param elementclass 数据项类型
	 * @param elementcode 数据项编码
	 * @param elementname 数据项名称
	 * @param pageNumber 当前页
	 * @param pageSize 每页条数
	 * @return PaginationSupport 基础数据项集合
	 */
	public PaginationSupport query(Long elementclass, String elementcode, String elementname, Integer pageNumber, Integer pageSize);
	
	/**
	 * 查询数据项
	 * @param params 参数
	 * @return 数据项集合
	 */
	public List<SysDicElement> searchDicEles(Map<String, Object> params);
	
	/**
	 * 获取数据项明细
	 * @param elementcode 数据项编码
	 * @return 数据项PO
	 */
	public SysDicElement getByElementcode(String elementcode);
	
	/**
	 * 通过elementcode查询数据项值集 下拉框
	 * @param dicTable 数据表PO
	 * @param elementcode 数据项编码
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> findDicElementVals(SysDicTable dicTable, String elementcode);
	
	/**
	 * 查询数据项
	 * @param elementid 数据项ID
	 * @return
	 */
	public List<SysDicElement> findDicElement(List<?> elementid);
	
	/**
	 * 获取数据项给数据权限
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> findDicElementToDataScope();
	
	/**
	 * 从缓从中取数据项
	 * @param elementcode
	 * @return
	 */
	public SysDicElement getDicelement(String elementcode);
}
