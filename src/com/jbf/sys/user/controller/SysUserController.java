/************************************************************
 * 类名：SysUserController.java
 *
 * 类别：Controller
 * 功能：用户控制器
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.user.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.user.po.SysUser;
import com.jbf.sys.user.po.SysUserRole;
import com.jbf.sys.user.service.SysUserRoleService;
import com.jbf.sys.user.service.SysUserService;

@Controller
@RequestMapping({ "/sys/SysUserController" })
public class SysUserController {

	@Autowired
	private SysUserService userService;

	@Autowired
	private SysUserRoleService userRoleService;

	/**
	 * 入口
	 * 
	 * @return
	 */
	@RequestMapping("/entry.do")
	public String entry() {
		return "/sys/userEntry";
	}

	/**
	 * 用户表单入口
	 * 
	 * @param request
	 * @return 页面
	 */
	@RequestMapping("/userForm.do")
	public ModelAndView entryForm(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/sys/userForm");
		mav.addObject("type", request.getParameter("type"));

		SysUser user = SecureUtil.getCurrentUser();
		mav.addObject("usertype", String.valueOf(user.getUsertype()));
		return mav;
	}

	/**
	 * 查询用户列表
	 * 
	 * @param request
	 * @return 用户列表
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/query.do")
	@ResponseBody
	public EasyUITotalResult query(HttpServletRequest request) {

		/*
		 * usercode 用户编码 username 用户名称
		 */
		Map<String, Object> map = ControllerUtil
				.getRequestParameterMap(request);
		PaginationSupport ps = userService.query(map);
		return EasyUITotalResult.from(ps);
	}

	/**
	 * 添加用户
	 * 
	 * @param user
	 *            用户详情
	 * @return 成功标志
	 */
	@RequestMapping("/add.do")
	@ResponseBody
	public ResultMsg add(SysUser user) {
		ResultMsg msg = null;
		try {
			userService.add(user);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
		return msg;
	}

	/**
	 * 修改用户
	 * 
	 * @param user
	 *            用户详情
	 * @return 成功标志
	 */
	@RequestMapping("/edit.do")
	@ResponseBody
	public ResultMsg editUser(SysUser user) {
		ResultMsg msg = null;
		try {
			userService.edit(user);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
		return msg;
	}

	/**
	 * 删除用户
	 * 
	 * @param userid
	 *            用户id
	 * @return 成功标志
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public ResultMsg delete(Long userid) {
		ResultMsg msg = null;
		try {
			userService.delete(userid);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
		return msg;
	}

	/**
	 * 取得用户授权的角色
	 * 
	 * @param userid
	 *            用户id
	 * @return 用户授权的角色
	 */
	@ResponseBody
	@RequestMapping("/getAuthRoles.do")
	public List<SysUserRole> getAuthRoles(Long userid) {
		return userRoleService.getAuthRoles(userid);
	}

	/**
	 * 对用户授权角色
	 * 
	 * @param userid
	 *            用户id
	 * @param roleids
	 *            角色id列表
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/addRolesToUser.do")
	public ResultMsg addRolesToUser(Long userid, String roleids) {
		ResultMsg msg = null;
		try {
			userRoleService.addRolesToUser(userid, roleids);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
		return msg;
	}

	/**
	 * 取用角色
	 * 
	 * @param roleid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getUserByRoleID.do")
	@ResponseBody
	public List getUserByRoleID(Long roleid) {
		return userService.getUserByRoleID(roleid);
	}

	/**
	 * 修改用户密码
	 * 
	 * @param username
	 * @param pawdold
	 * @param pawdnew
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editPassword.do")
	public ResultMsg editPassword(String username, String pswdold,
			String pswdnew) {
		ResultMsg msg = null;
		try {
			userService.editPassword(username, pswdold, pswdnew);
			msg = new ResultMsg(true, AppException.getMessage("crud.editok"));
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AppException) {
				msg = new ResultMsg(false, e.getMessage());
			} else {
				msg = new ResultMsg(false,
						AppException.getMessage("crud.editerr"));
			}
		}
		return msg;
	}

	/**
	 * 重置用户密码
	 * @Title: resetUserPasw 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param userid
	 * @param @param pasw
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("/resetUserPasw.do")
	public ResultMsg resetUserPasw(Long userid, String pasw) {
		ResultMsg msg = null;
		try {
			userService.resetUserPasw(userid, pasw);
			msg = new ResultMsg(true, AppException.getMessage("crud.editok"));
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AppException) {
				msg = new ResultMsg(false, e.getMessage());
			} else {
				msg = new ResultMsg(false,
						AppException.getMessage("crud.editerr"));
			}
		}
		return msg;
	}
	
	/**
	 * 
	 * @Title: resetUserPasw 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param pasw
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/resetAllUserPasw.do")
	@ResponseBody
	public ResultMsg resetUserPasw(String pasw) {
		ResultMsg msg = null;
		try {
			userService.resetAllUserPasw(pasw);
			msg = new ResultMsg(true, AppException.getMessage("crud.editok"));
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AppException) {
				msg = new ResultMsg(false, e.getMessage());
			} else {
				msg = new ResultMsg(false,
						AppException.getMessage("crud.editerr"));
			}
		}
		return msg;
	}
	
	/**
	 * 查找某角色下所有用户
	 * @Title: findUserListByRoleid 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return List<SysUser> 返回类型 
	 * @throws
	 */
	@RequestMapping("/findUserListByRoleid.do")
	@ResponseBody
	public List<SysUser> findUserListByRoleid(HttpServletRequest request) {
		String roleid = request.getParameter("roleid");
		
		return userService.findUserListByRoleid(Long.valueOf(roleid));
	} 
}
