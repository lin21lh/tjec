/************************************************************
 * 类名：JbfConditionFilter
 *
 * 类别：组件类
 * 功能：数据库脚本管理，提供访问保存在resource/query下在数据库脚本的通用接口,供业务开发人员使用
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-4-01  CFIT-PM   maqs         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.datascope;

import com.jbf.common.exception.AppException;

public interface JbfConditionFilter {
	
	/**
	 * 获取数据权限 （以字典表ID字段为基准）
	 * @param resourceid 菜单ID
	 * @param tablecode 主表名 表别名默认为 “t”
	 * @return
	 */
	public String getConditionFilterById(Long resourceid, String tablecode) throws AppException;
	
	
	/**
	 * 获取数据权限 （以字典表ID字段为基准）
	 * @param resourceid 菜单ID
	 * @param tablecode 主表名 表别名默认为 “t”
	 * @return
	 */
	public String getConditionFilterByCode(Long resourceid, String tablecode) throws AppException;
	
	/**
	 * 获取数据权限 （以字典表CODE字段为基准）
	 * @param resourceid 菜单ID
	 * @param tablecode 主表名
	 * @param tableAlias 主表别名
	 * @return
	 */
	public String getConditionFilterById(Long resourceid, String tablecode, String tableAlias) throws AppException;
	
	/**
	 * 获取数据权限（以字典表CODE字段为基准）
	 * @param resourceid 菜单ID
	 * @param tablecode 主表名
	 * @param tableAlias 主表别名
	 * @return
	 */
	public String getConditionFilterByCode(Long resourceid, String tablecode, String tableAlias) throws AppException;
	
}
