/************************************************************
 * 类名：SysRoleResourceService.java
 *
 * 类别：Service
 * 功能：角色资源服务
 * 
 *   Ver     変更日              部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.role.service;

import java.util.List;

public interface SysRoleResourceService {
	/**
	 * 查询角色对应的资源树
	 * 
	 * @param roleid
	 *            角色id
	 * @return
	 */
	List query(Long roleid);

	/**
	 * 保存角色对资源的访问关系
	 * 
	 * @param roleId
	 *            角色id
	 * @param resourceIds
	 *            资源列表
	 * @throws Exception
	 */
	public void saveResourceToRole(Long roleId, String resourceIds)
			throws Exception;

	/**
	 * 修改角色、资源所拥有的操作
	 * 
	 * @param roleId
	 *            角色Id
	 * @param resourceId
	 *            资源Id
	 * @param operIds
	 *            操作列表
	 * @throws Exception
	 */
	void editRoleResourceOper(Long roleId, Long resourceId, String operIds)
			throws Exception;

	/**
	 * 查询角色、资源对应的操作
	 * 
	 * @param roleId
	 * @param resourceId
	 * @return
	 */
	List queryRoleResourceOper(Long roleId, Long resourceId);
	
	/**
	 * 查询当前功能菜单被授予那几个角色
	 * @Title: findRoleListByResource 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param resourceid
	 * @param @return 设定文件 
	 * @return List 返回类型 
	 * @throws
	 */
	public List findRoleListByResource(Long resourceid);
}
