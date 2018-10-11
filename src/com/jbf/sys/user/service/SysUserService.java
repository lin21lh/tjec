/************************************************************
 * 类名：SysUserService.java
 *
 * 类别：Service
 * 功能：用户服务
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.user.service;

import java.util.List;
import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.sys.user.po.SysUser;

public interface SysUserService {

	/**
	 * 查询用户列表
	 * 
	 * @param param
	 * @return
	 */
	public PaginationSupport query(Map<String, Object> param);

	/**
	 * 取用角色对应的用户
	 * 
	 * @param roleid
	 * @return
	 */
	public List getUserByRoleID(Long roleid);

	/**
	 * 添加用户
	 * 
	 * @param user
	 *            用户详情
	 * @throws Exception
	 */
	public Long add(SysUser user) throws Exception;

	/**
	 * 修改用户
	 * 
	 * @param user
	 *            用户详情
	 * @throws Exception
	 */
	public void edit(SysUser user) throws Exception;

	/**
	 * 删除用户
	 * 
	 * @param userid
	 *            用户id
	 * @throws Exception
	 */
	public void delete(Long userid) throws Exception;

	/**
	 * 重置用户密码
	 * 
	 * @param userid
	 *            用户id
	 * @param pasw
	 *            新密码
	 * @throws Exception
	 */
	void resetUserPasw(Long userid, String pasw) throws Exception;
	
	/**
	 * 重置全部用户密码
	 * @Title: resetAllUserPasw 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @throws Exception 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void resetAllUserPasw(String pasw) throws Exception;

	/**
	 * 根据用户编码获取用户信息
	 * 
	 * @param usercode
	 * @return user
	 */
	public SysUser getUserByUsercode(String usercode);

	/**
	 * 根据用肪编码取得菜单树
	 * 
	 * @param userid
	 *            用户ID
	 * @return 菜单树
	 * @throws Exception
	 */
	public List getResourceTree(SysUser user) throws Exception;
	/**
	 * 根据用肪编码取得菜单树
	 * 
	 * @param userid
	 *            用户ID
	 * @return 菜单树
	 * @throws Exception
	 */
	public List getResourceTree(SysUser user, Long resourceid) throws Exception;
	/**
	 * 获取用户的所有菜单
	 * @return
	 * @throws Exception
	 */
	public List getResourceTree() throws Exception;

	/**
	 * 给用户某个角色的授权
	 * 
	 * @param roleid
	 *            角色id
	 * @param userids
	 *            用户 id 列表
	 * @throws Exception
	 */
	public void addUserToRole(Long roleid, String userids) throws Exception;

	/**
	 * 将一个角色授权的多个用户取消授权
	 * 
	 * @param roleid
	 *            角色id
	 * @param userids
	 *            用户id列表
	 * @throws Exception
	 */
	public void removeUserFromRole(Long roleid, String userids)
			throws Exception;

	/**
	 * 修改用户密码
	 * 
	 * @param username
	 *            用户名
	 * @param pawdold
	 *            旧密码
	 * @param pawdnew
	 *            新密码
	 * @throws Exception
	 */
	public void editPassword(String username, String pswdold, String pswdnew)
			throws Exception;
	
	/**
	 * 查找当前角色下所有用户
	 * @Title: findUserListByRoleid 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param roleid
	 * @param @return 设定文件 
	 * @return List<SysUser> 返回类型 
	 * @throws
	 */
	public List<SysUser> findUserListByRoleid(Long roleid);
	
	/**
	 * 查询所有用户
	 * @Title: findAllUserList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return 设定文件 
	 * @return List<SysUser> 返回类型 
	 * @throws
	 */
	public List<SysUser>findAllUserList();
	
	/**
	 * 查询系统预设字典值
	 * @Title: getNameByCode 
	 * @param 字典名称编号，字典项编号
	 * @return Sting 返回类型 
	 * @author ztt
	 */
	public String getNameByCode(String elementcode, String code);
}
