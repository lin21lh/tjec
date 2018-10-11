/************************************************************
 * 类名：SysUserRoleController.java
 *
 * 类别：Controller
 * 功能：用户角色控制器
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbf.common.exception.AppException;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.user.service.SysUserRoleService;

@Controller
@RequestMapping({ "/sys/SysUserRoleController" })
public class SysUserRoleController {

	@Autowired
	SysUserRoleService sysUserRoleService;

	/**
	 * 按用户查询角色
	 * 
	 * @param userid
	 *            用户id
	 * @return 用户被授权的角色
	 */
	@ResponseBody
	@RequestMapping({ "/queryRoleByUser.do" })
	public List queryRoleByUser(Long userid) {
		return sysUserRoleService.queryRoleByUser(userid);
	}

	/**
	 * 查询角色已授权的用户
	 * 
	 * @param roleId
	 * @param username
	 * @return 角色已授权的用户
	 */
	@ResponseBody
	@RequestMapping({ "/querySelectedUserByRole.do" })
	public List querySelectedUserByRole(Long roleId, String username) {

		return sysUserRoleService.querySelectedUserByRole(roleId, username);
	}

	/**
	 * 查询角色未授权的用户
	 */
	@ResponseBody
	@RequestMapping({ "/queryUnselectedUserByRole.do" })
	public List queryUnselectedUserByRole(Long roleId, String username) {
		return sysUserRoleService.queryUnselectedUserByRole(roleId, username);
	}

	/**
	 * 角色授权给多个用户
	 * 
	 * @param roleid
	 * @param userids
	 * @return 成功标志
	 */
	@RequestMapping("/addUserToRole.do")
	@ResponseBody
	public ResultMsg addUserToRole(Long roleid, String userids) {
		ResultMsg msg = null;
		try {
			sysUserRoleService.addUserToRole(roleid, userids);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AppException) {
				msg = new ResultMsg(false, e.getMessage());
			}
		}
		return msg;

	}

	/**
	 * 取消角色授权从多个用户
	 * 
	 * @param roleid
	 * @param userids
	 * @return 成功标志
	 */
	@RequestMapping("/removeUserFromRole.do")
	@ResponseBody
	public ResultMsg removeUserFromRole(Long roleid, String userids) {
		ResultMsg msg = null;
		try {
			sysUserRoleService.removeUserFromRole(roleid, userids);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AppException) {
				msg = new ResultMsg(false, e.getMessage());
			}
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
		return msg;

	}

	/**
	 * 修改用户所拥有的角色
	 * 
	 * @param userid
	 * @param roleids
	 * @return 用户所拥有的角色
	 */
	@RequestMapping("/editUserRoles.do")
	@ResponseBody
	public ResultMsg editUserRoles(Long userid, String roleids) {
		ResultMsg msg = null;
		try {
			sysUserRoleService.editUserRoles(userid, roleids);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AppException) {
				msg = new ResultMsg(false, e.getMessage());
			}
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
		return msg;
	}
}
