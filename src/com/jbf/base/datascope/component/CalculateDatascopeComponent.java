/************************************************************
 * 类名：CalculateDatascopeComponent
 *
 * 类别：组件接口
 * 功能：通过资源（菜单）、用户及用户角色计算数据权限
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

import com.jbf.base.datascope.po.SysRoleUserResDscope;
import com.jbf.common.exception.AppException;
import com.jbf.sys.role.po.SysRole;

public interface CalculateDatascopeComponent {

	/**
	 * 计算数据权限
	 * @param resourceid 资源（菜单）ID
	 * @param userid 用户ID
	 * @param tableAlias 业务主表别名
	 * @param tablecode 业务主表编码
	 * @param scopeType （以字典表ID或CODE字段为基准）
	 * @return 数据权限条件字符串
	 * @throws AppException
	 */
	public StringBuffer calculateDataRight(Long resourceid, Long userid, String tableAlias, String tablecode, int scopeType) throws AppException;
	
	/**
	 * 计算数据项的数据权限
	 * @param resourceid 资源（菜单）ID
	 * @param elementcode 数据项编码
	 * @param userid 用户ID
	 * @param tableAlias 数据项值集所在表别名
	 * @param tablecode 数据项值集所在表编码
	 * @return
	 * @throws AppException
	 */
	public String calDataRightByElementcode(Long resourceid, String elementcode, Long userid, String tableAlias, String tablecode, int scopeType) throws AppException;

	/**
	 * 判断是否配置数据权限
	 * @Title: isHasDataRight 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param resourceid 资源（菜单）ID
	 * @param @param userid 登录用户ID
	 * @param @param elementcode 数据项编码
	 * @param @return 设定文件 
	 * @return boolean 返回类型 
	 */
	public boolean isHasDataRight(Long resourceid, Long userid, String elementcode);
	
	/**
	 * 查找数据权限关联关系
	 * @Title: findRoleUserResDscopeList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param resourceid
	 * @param @param userid
	 * @param @return 设定文件 
	 * @return List<SysRoleUserResDscope> 返回类型 
	 * @throws
	 */
	public List<SysRoleUserResDscope> findRoleUserResDscopeList(Long resourceid, Long userid);
	
	/**
	 * 获取角色对象
	 * @Title: getRoleByRoleID 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param roleid
	 * @param @return 设定文件 
	 * @return SysRole 返回类型 
	 * @throws
	 */
	public SysRole getRoleByRoleID(Long roleid);
}
