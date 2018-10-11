package com.jbf.sys.user.service;

import java.util.Map;

import com.jbf.sys.user.po.SysUser;

/**
 * 个人信息维护接口
 * @author songxiaojie
 *
 */
public interface SysUserInfoService {

	/**
	 * 查询个人信息
	 * @param param
	 * @return
	 */
	public Map getUserInfo(Map<String, Object> param);
	
	/**
	 * 根据编码查询用户扩展信息记录条数
	 * @param userid
	 * @return
	 */
	public int getCountUserExtByUserId(Long userid);
	
	/**
	 * 修改个人信息
	 * @param userid
	 * @return
	 */
	public void editPersonalInfo(SysUser sysUser,Map<String, Object> param);
}
