/************************************************************
 * 类名：SysDicElementService.java
 *
 * 类别：Service接口
 * 功能：数据项服务接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-08  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.service;

import java.util.List;
import java.util.Map;

import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.dic.po.SysDicElementViewFilter;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;

public interface SysDicElementService {

	/**
	 * 查询数据项类型
	 * @return 数据项类型集合
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> queryElementClass();
	
	/**
	 * 数据项 分页查询
	 * @param elementclass 数据项分类
	 * @param elementcode 数据项编码
	 * @param elementname 数据项名称
	 * @param pageNumber 当前页
	 * @param pageSize 分页条数
	 * @return 数据项集合
	 */
	public PaginationSupport queryPage(String elementclass, String elementcode, String elementname, String pageNumber, String pageSize);
	
	/**
	 * 查询数据项
	 * @param params 查询参数Map
	 * @return 数据项集合
	 */
	public List<SysDicElement> searchDicEles(Map<String, Object> params);
	
	/**
	 * 数据项保存
	 * @param dicElement 数据项PO
	 */
	public void saveDicElement(SysDicElement dicElement);
	
	/**
	 * 获取数据项视图过滤条件
	 * @param elemntcode 视图数据项
	 * @return
	 */
	public List<SysDicElementViewFilter> getFilters(String elementcode) throws NumberFormatException, Exception;
	
	/**
	 * 数据项视图
	 * @param elementMap 数据项Map
	 * @param elementviewfilters 过滤条件
	 */
	@SuppressWarnings("rawtypes")
	public void saveDicElementView(Map elementMap, String elementviewfilters) throws Exception  ;
	
	/**
	 * 删除数据项
	 * @param elementids 数据项ID
	 * @return 删除结果
	 * @throws AppException
	 */
	public String delete(String elementids) throws AppException;
	
	/**
	 * 查找与数据权限关联的数据项
	 * @return 数据项集合
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> findDicElementToDataScope();
	
	/**
	 * 获取数据项PO
	 * @param elementcode 数据项编码
	 * @return 数据项PO
	 */
	public SysDicElement getDicElement(String elementcode);
	
	/**
	 * 获取源数据项集合
	 * @return
	 */
	public List<SysDicElement> getSourceDicElements();
	
	/**
	 * 获取列PO
	 * @param elementcode 数据项编码
	 * @return 列集合
	 */
	public List<SysDicColumn> getColumnsByElementcode(String elementcode);
}
