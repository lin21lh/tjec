/************************************************************
 * 类名：SysUserRoleDao.java
 *
 * 类别：DAO
 * 功能：用户角色DAO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.user.dao;

import java.util.List;

import com.jbf.common.dao.IGenericDao;
import com.jbf.sys.role.po.SysRole;
import com.jbf.sys.user.po.SysUserRole;

public interface SysUserRoleDao extends IGenericDao<SysUserRole, Long> {

	/**
	 * 添加角色授权给用户
	 * 
	 * @param userid
	 * @param roleids
	 */
	public void addRolesToUser(Long userid, String roleids);

	/**
	 * 通过角色ID查找对应用户
	 * 
	 * @param roleid
	 * @return 角色ID对应用户
	 */
	public List<?> getUserByRoleID(Long roleid);

	/**
	 * 取得角色授权的用户
	 * 
	 * @param roleid
	 * @return 角色授权的用户
	 */
	public List<?> getUsernameByRoleID(Long roleid);

	/**
	 * 取得用户授权过的角色列表
	 * 
	 * @param userid
	 * @return
	 */
	public List<SysRole> getRolesByUserid(Long userid);

}
