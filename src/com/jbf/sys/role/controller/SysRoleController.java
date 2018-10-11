/************************************************************
 * 类名：SysRoleController.java
 *
 * 类别：Controller
 * 功能：提供系统角色的页面入口和增删改查功能
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.role.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.exception.AppException;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.role.po.SysRole;
import com.jbf.sys.role.service.SysRoleService;

@Scope("prototype")
@Controller
@RequestMapping("/sys/SysRoleController")
public class SysRoleController {
	@Autowired
	SysRoleService sysRoleService;

	/**
	 * 入口
	 * 
	 * @return
	 */
	@RequestMapping("/entry.do")
	public ModelAndView entry() {
		return new ModelAndView("/sys/roleEntry");
	}

	/**
	 * 查询所有的角色
	 * 
	 * @return 角色树
	 */
	@ResponseBody
	@RequestMapping("/query.do")
	public List query() {
		List list = null;
		try {
			list = sysRoleService.query();
		} catch (Exception e) {
			e.printStackTrace();
			list = new ArrayList();
			if (e instanceof AppException) {
				list.add(e.getMessage());
			}
		}
		return list;
	}

	/**
	 * 取得角色详情
	 * 
	 * @param id
	 *            角色id
	 * @return 角色详情
	 */
	@ResponseBody
	@RequestMapping("/get.do")
	public SysRole getRoleDetail(Long id) {
		return sysRoleService.get(id);
	}

	/**
	 * 保存角色
	 * 
	 * @param role
	 *            角色详情
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/save.do")
	public ResultMsg save(@ModelAttribute SysRole role) {
		try {
			sysRoleService.save(role);
			return new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AppException)
				return new ResultMsg(false, e.getMessage());
			else
				return new ResultMsg(false,
						AppException.getMessage("crud.saveerr"));
		}
	}

	/**
	 * 删除角色
	 * 
	 * @param id
	 *            角色
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/delete.do")
	public ResultMsg delete(Long id) {
		try {
			sysRoleService.delete(id);
			return new ResultMsg(true, AppException.getMessage("crud.delok"));
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AppException)
				return new ResultMsg(false, e.getMessage());
			else
				return new ResultMsg(false,
						AppException.getMessage("crud.delerr"));
		}
	}

}
