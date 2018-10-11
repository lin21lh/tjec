/************************************************************
 * 类名：DatascopeService.java
 *
 * 类别：Service接口
 * 功能：数据权限服务接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-23  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.datascope.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import com.jbf.base.datascope.po.SysRoleUserResDscope;

public interface DatascopeService {

	/**
	 * 角色、用户、功能菜单和数据权限关联关系保存
	 * @param roleUserResDscope 角色、用户、功能菜单和数据权限关联关系PO
	 */
	public void save(SysRoleUserResDscope roleUserResDscope);
	
	/**
	 * 数据权限保存
	 * @param text 数据权限字符串
	 * @throws Exception
	 */
	public void saveDatascope(String text) throws Exception;
	
	/**
	 * 查询数据权限主数据
	 * @return 数据权限主数据集合
	 */
	public List findDataScopeMain();
	
	/**
	 * 通过数据权限主ID获取数据权限明细
	 * @param scopemainid 数据权限主ID
	 * @return 数据权限明细
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public HashMap<String, Object> getDataScopeDetailByID(Long scopemainid) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;
	
	/**
	 * 通过角色、用户、功能菜单获取对应分配的数据权限
	 * @param roleid 角色ID
	 * @param isallmenu 是否全部功能菜单 1=是 0=否
	 * @param resourceid 功能菜单ID
	 * @param isalluser 是否全部用户 1=是 0=否
	 * @param userid 用户ID
	 * @return 数据权限明细
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public HashMap<String, Object> getDataScopeDetail(Long roleid, Integer isallmenu, Long resourceid, Integer isalluser, Long userid) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;
	
	/**
	 * 通过角色获取角色、用户、功能菜单和数据权限的关联关系
	 * @param roleid 角色ID
	 * @return 角色、用户、功能菜单和数据权限的关联关系PO
	 */
	public SysRoleUserResDscope getByRole(Long roleid);
	
	/**
	 * 数据权限删除
	 * @param scopemainid 数据权限主ID
	 * @return 删除失败原因
	 */
	public String deleteDataScope(Long scopemainid);
	
	/**
	 * 删除角色、用户、功能菜单和数据权限关联关系
	 * @param roleid 角色ID
	 * @param isallmenu 是否全部功能菜单 1=是 0=否
	 * @param resourceid 功能菜单ID
	 * @param isalluser 是否全部用户
	 * @param userid 用户ID
	 * @return 删除失败原因
	 */
	public HashMap<String, Object> deleteDataScopeRelation(Long roleid, Integer isallmenu, Long resourceid, Integer isalluser, Long userid);
	
	/**
	 * 通过数据权限主ID获取对应的角色、用户、功能菜单和数据权限关联关系PO集合
	 * @param scopemainid 数据权限主ID
	 * @return 角色、用户、功能菜单和数据权限关联关系PO集合
	 */
	public List findRelationByScopemainID(Long scopemainid);
	
	/**
	 * 工作流动作执行条件保存
	 * @param text 工作流动作执行条件字符串
	 * @throws Exception
	 */
	public void saveWFScope(String text) throws Exception;
	
	/**
	 * 获取工作流动作执行条件
	 * @param wfkey 工作流key
	 * @param wfversion 工作流版本号
	 * @param decisionname decision名称
	 * @param taskname 任务节点名称
	 * @return 工作流动作执行条件
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public HashMap<String, Object> getWFDataScope(String wfkey, Integer wfversion, String decisionname, String taskname) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;
}
