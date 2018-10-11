/************************************************************
 * 类名：SysRoleService.java
 *
 * 类别：Service
 * 功能：角色服务
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.role.service;

import java.util.List;

import com.jbf.sys.role.po.SysRole;

public interface SysRoleService {
	/**
	 * 查询用户拥有的角色
	 * 
	 * @param userID
	 *            用户id
	 * @return
	 */
	public List<Integer> queryRoleIdsByUserID(Integer userID);

	/**
	 * 取得角色详情
	 * 
	 * @param id
	 *            角色id
	 * @return
	 */
	public SysRole get(Long id);

	/**
	 * 保存角色
	 * 
	 * @param role
	 *            角色详情po
	 * @throws Exception
	 */
	public void save(SysRole role) throws Exception;

	/**
	 * 删除角色
	 * 
	 * @param id
	 *            角色
	 * @throws Exception
	 */
	public void delete(Long id) throws Exception;

	/**
	 * 查询所有的角色的树
	 * 
	 * @return 角色树
	 */
	public List query();

	/**
	 * 查询角色已授权的用户
	 * 
	 * @param roleid
	 *            角色id
	 * @return 角色已授权的用户
	 */
	List querySelectedUsers(Long roleid);

	/**
	 * 查询角色尚未授权的用户
	 * 
	 * @param roleid
	 *            角色id
	 * @return 角色尚未授权的用户
	 */
	List queryUnselectUsers(Long roleid);
}
