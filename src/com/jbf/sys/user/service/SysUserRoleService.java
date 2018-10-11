/************************************************************
 * 类名：SysUserRoleService.java
 *
 * 类别：Service
 * 功能：用户角色服务
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.user.service;

import java.util.List;

import com.jbf.sys.user.po.SysUserRole;

public interface SysUserRoleService {
	/**
	 * 取得用户被授权的角色
	 * 
	 * @param userid
	 *            用户id
	 * @return 用户被授权的角色
	 */
	public List<SysUserRole> getAuthRoles(Long userid);

	/**
	 * 将角色授权给用户
	 * 
	 * @param userid
	 *            用户id
	 * @param roleids
	 *            角色id列表
	 */
	public void addRolesToUser(Long userid, String roleids);

	/**
	 * 查询角色已授权的用户
	 * 
	 * @param roleId
	 *            角色id
	 * @param username
	 *            用户名
	 * @return 角色已授权的用户
	 */
	public List querySelectedUserByRole(Long roleId, String username);

	/**
	 * 查询角色未授权的用户
	 * 
	 * @param roleId
	 *            角色id
	 * @param username
	 *            用户名
	 * @return 角色未授权的用户
	 */
	public List queryUnselectedUserByRole(Long roleId, String username);

	/**
	 * 取消角色对用户的授权
	 * 
	 * @param roleid
	 *            角色id
	 * @param userids
	 *            用户id
	 * @throws Exception
	 */
	public void removeUserFromRole(Long roleid, String userids)
			throws Exception;

	/**
	 * 将角色授权给用户
	 * 
	 * @param roleid
	 *            角色id
	 * @param userids
	 *            用户id
	 * @throws Exception
	 */
	public void addUserToRole(Long roleid, String userids) throws Exception;

	/**
	 * 查询用户已有的角色授权
	 * 
	 * @param userid
	 *            用户id
	 * @return
	 */
	public List queryRoleByUser(Long userid);

	/**
	 * 修改用户的角色授权
	 * 
	 * @param userid
	 *            用户id
	 * @param roleids
	 *            角色id列表
	 * @throws Exception
	 */
	public void editUserRoles(Long userid, String roleids) throws Exception;
}
