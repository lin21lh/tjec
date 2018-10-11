/************************************************************
 * 类名：DatascopeComponent.java
 *
 * 类别：组件接口
 * 功能：组装多条件项数据权限组件接口
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-4-01  CFIT-PM   maqs         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.datascope.component;

import java.util.List;

import com.jbf.base.datascope.po.SysDatascopeitem;
import com.jbf.common.exception.AppException;

public interface DatascopeComponent {

	/**
	 * 
	 * @param scopemainid
	 * @param tableAlias
	 * @param tablecode
	 * @param iUserid
	 * @param scopeType
	 * @return
	 * @throws AppException
	 */
	public String getDynamicSQLExpression(Long scopemainid, String tableAlias, String tablecode, Long iUserid, int scopeType) throws AppException;
	
	/**
	 * 
	 * @param scopeitem
	 * @param tableAlias
	 * @param tableCode
	 * @param userid
	 * @param isSQL
	 * @return
	 * @throws AppException
	 */
	public String createSQLByScopeItem(SysDatascopeitem scopeitem, String tableAlias, String tableCode, Long userid, int scopeType) throws AppException;
	
	/**
	 * 查询数据项值集加数据权限
	 * @param scopemainid 主数据权限ID
	 * @param elementcode 
	 * @param tableAlias 表别名
	 * @param tableCode 表编码
	 * @param iUserid 当前登录用户ID
	 * @param scopeType 当前应用类型
	 * @return
	 * @throws AppException
	 */
	public String getConditionByElementcode(Long scopemainid, String elementcode, String tableAlias, String tableCode, Long iUserid, int scopeType) throws AppException;
	
	/**
	 * 获取数据权限名称
	 * @Title: getDatascopemainName 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param scopemainId 数据权限ID
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	public String getDatascopemainName(Long scopemainId);
	
	/**
	 * 查询数据权限配置项
	 * @Title: findDatascopeItemList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param scopemainID
	 * @param @return 设定文件 
	 * @return List<SysDatascopeitem> 返回类型 
	 * @throws
	 */
	public List<SysDatascopeitem> findDatascopeItemList(Long scopemainID);
}
