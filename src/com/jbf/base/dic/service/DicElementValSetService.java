/************************************************************
 * 类名：DicElementValSetService.java
 *
 * 类别：Service接口
 * 功能：数据项值集服务接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-10  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.exception.AppException;

public interface DicElementValSetService {

	/**
	 * 查询数据项值集
	 * @param paramMap 参数Map
	 * @return 数据项值集集合
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 */
	public EasyUITotalResult queryPageDicElementVals(Map<String, Object> paramMap) throws SecurityException, ClassNotFoundException, NoSuchFieldException;
	
	/**
	 * 顺序码数据项值集保存
	 * @param value 数据项值集Map
	 * @throws Exception
	 */
	public void saveDicElementVal(Map value) throws Exception;
	
	/**
	 * 层码数据项值集保存
	 * @param value 数据项值集Map
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public void saveDicElementTreeVal(Map value) throws NumberFormatException, Exception;
	
	/**
	 * 查询数据项值集
	 * @param elementcode 数据项编码
	 * @return 数据项值集集合
	 */
	public List<Map> findDicElementVals(String elementcode);
	
	/**
	 * 获取数据项值集明细
	 * @param tablecode 表编码
	 * @param id 数据项值集ID
	 * @return 数据项值集Object
	 */
	public Object getDetailByID(String tablecode, Long id);
	
	/**
	 * 查询层码数据项值集
	 * @param elementcode 数据项编码
	 * @param valueModel 取值模式
	 * @param textModel 显示模式
	 * @param menuid 功能菜单ID
	 * @return 数据项值集集合
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws AppException
	 */
	public List queryDicTreeElementVals(String elementcode, String byElementcode, String valueModel, String textModel, String menuid, String customSql, boolean isRelation,  boolean isCFilter) throws SecurityException, ClassNotFoundException, NoSuchFieldException, AppException ;
	
	/**
	 * 查询数据权限关联的数据项值集
	 * @param elementcode 数据项编码
	 * @param scopeitemid 数据权限条件ID
	 * @param currenttdid 当前td的ID
	 * @return Map
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws AppException
	 */
	public Map<String, Object> queryDicValsToDataScope(String elementcode, Long scopeitemid, String currenttdid) throws SecurityException, ClassNotFoundException, NoSuchFieldException, AppException;
	
	/**
	 * 删除顺序码数据项值集
	 * @param ids 数据项值集ID
	 * @param tablecode 表编码
	 * @return 删除结果
	 */
	public String deleteDicElementVal(String ids, String tablecode);
	
	/**
	 * 删除层码数据项值集
	 * @param id 数据项值集ID
	 * @param elementcode 数据项编码
	 * @return 删除结果
	 * @throws Exception
	 */
	public String deleteDicTreeElementVal(HttpServletRequest request, Long id, String elementcode) throws Exception;
	
	/**
	 * 查找数据项值集ID
	 * @param elementcode 数据项编码
	 * @param code 数据项值集编码
	 * @return 数据项值集ID
	 * @throws AppException
	 */
	public Long findIDByCodeElement(String elementcode, String code) throws AppException;
	
	/**
	 * 查询数据项值集
	 * @param menuid 资源（菜单）ID
	 * @param elementcode 数据项编码
	 * @param byElementcode 关联数据项
	 * @param idColumn id列
	 * @param textColumn 显示列
	 * @param customSql 外带查询条件
	 * @param isCFilter 是否启用数据权限
	 * @param isRelation 是否启用关联查询
	 * @return
	 * @throws AppException
	 */
	public List queryByElementcode(String menuid, String elementcode, String byElementcode, String idColumn, String textColumn, String customSql, boolean isCFilter, boolean isRelation) throws AppException;
}
