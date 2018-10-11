/************************************************************
 * 类名：SysRoleResourceController.java
 *
 * 类别：Controller
 * 功能：提供系统资源角色的增删改查功能
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.role.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbf.common.exception.AppException;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.role.service.SysRoleResourceService;
import com.jbf.sys.role.service.SysRoleService;

@Scope("prototype")
@Controller
@RequestMapping("/sys/SysRoleResourceController")
public class SysRoleResourceController {

	@Autowired
	SysRoleService sysRoleService;

	@Autowired
	SysRoleResourceService sysRoleResourceService;

	/**
	 * 查询角色的资源树
	 * 
	 * @param roleid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query.do")
	public List query(Long roleid) {
		return sysRoleResourceService.query(roleid);
	}

	/**
	 * 保存角色的资源树
	 * 
	 * @param roleid
	 *            角色id
	 * @param resourceIds
	 *            资源id列表
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/saveResourceToRole.do")
	public ResultMsg saveResourceToRole(Long roleid, String resourceIds) {
		try {
			sysRoleResourceService.saveResourceToRole(roleid, resourceIds);
			return new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AppException) {
				return new ResultMsg(false, e.getMessage());
			} else {
				return new ResultMsg(false,
						AppException.getMessage("crud.saveerr"));
			}
		}
	}

	/**
	 * 查询角色、资源对应的操作
	 * 
	 * @param roleId
	 *            角色id
	 * @param resourceId
	 *            资源id
	 * @return 角色、资源对应的操作
	 */

	@ResponseBody
	@RequestMapping("/queryRoleResourceOper.do")
	public List queryRoleResourceOper(Long roleId, Long resourceId) {
		return sysRoleResourceService.queryRoleResourceOper(roleId, resourceId);
	}

	/**
	 * 修改角色、资源对应的操作权限
	 * 
	 * @param roleId
	 *            角色id
	 * @param resourceId
	 *            资源id
	 * @param operIds
	 *            要保存的操作id列表
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/editRoleResourceOper.do")
	public ResultMsg editRoleResourceOper(Long roleId, Long resourceId,
			String operIds) {
		try {
			sysRoleResourceService.editRoleResourceOper(roleId, resourceId,
					operIds);
			return new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AppException) {
				return new ResultMsg(false, e.getMessage());
			} else {
				return new ResultMsg(false,
						AppException.getMessage("crud.saveerr"));
			}
		}

	}

	/**
	 * 查询角色对应的用用户
	 * 
	 * @param roleid
	 *            用户id
	 * @return 角色对应的用用户列表
	 */
	@ResponseBody
	@RequestMapping("/querySelectedUsers.do")
	public List querySelectedUsers(Long roleid) {
		return sysRoleService.querySelectedUsers(roleid);
	}

	/**
	 * 查询角色没分配给权限的用户
	 * 
	 * @param roleid
	 *            角色id
	 * @return 角色没分配给权限的用户列表
	 */
	@ResponseBody
	@RequestMapping("/queryUnselectUsers.do")
	public List queryUnselectUsers(Long roleid) {
		return sysRoleService.queryUnselectUsers(roleid);
	}
	
	/**
	 * 查询当前功能菜单被授予哪几个角色
	 * @Title: findRoleListByResource 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param resourceid
	 * @param @return 设定文件 
	 * @return List 返回类型 
	 * @throws
	 */
	@RequestMapping("/findRoleListByResource.do")
	@ResponseBody
	public List findRoleListByResource(HttpServletRequest request) {
		String resourceid = request.getParameter("resourceid");
		
		return sysRoleResourceService.findRoleListByResource(Long.valueOf(resourceid));
	}
}
