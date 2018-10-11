/************************************************************
 * 类名：SysUserDao.java
 *
 * 类别：Dao
 * 功能：用户DAO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.user.dao;

import java.util.List;
import java.util.Map;

import com.jbf.common.dao.IGenericDao;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;

public interface SysUserDao extends IGenericDao<SysUser, Long> {

	/**
	 * 根据用户id取得菜单
	 * 
	 * @param userid
	 *            用户id
	 * @return 用户拥有的菜单列表
	 */
	public List<SysResource> getMenusByUser(SysUser user);
	
	/**
	 * 根据用户id取得菜单
	 * 
	 * @param userid
	 *            用户id
	 * @return 用户拥有的菜单列表
	 */
	public List<Map> getMenusByUser(SysUser user, Long resourceid);

}
