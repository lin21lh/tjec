/************************************************************
 * 类名：SysResourceOperController.java
 *
 * 类别：Controller
 * 功能：提供系统资源操作的增删改查功能
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.resource.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jbf.common.exception.AppException;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.resource.po.SysResourceOper;
import com.jbf.sys.resource.service.SysResourceOperService;

@Scope("prototype")
@Controller
@RequestMapping("/sys/SysResourceOperController")
public class SysResourceOperController {

	@Autowired
	SysResourceOperService sysResourceOperService;

	/**
	 * 取得操作详情
	 * 
	 * @param id
	 * @return 操作详情
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public SysResourceOper get(Long id) {
		return sysResourceOperService.get(id);
	}

	/**
	 * 查询资源未添加预定义oper
	 * 
	 * @param resourceid
	 *            资源id
	 * @return 资源未添加预定义oper
	 */
	@RequestMapping("/queryPresetOper.do")
	@ResponseBody
	public List queryPresetOper(Long resourceid) {
		return sysResourceOperService.queryPresetOper(resourceid);
	}

	/**
	 * 查询资源已有的操作
	 * 
	 * @param resourceid
	 *            资源id
	 * @return 资源已有的操作
	 */
	@RequestMapping("/queryOper.do")
	@ResponseBody
	public List queryOper(Long resourceid) {
		return sysResourceOperService.queryOper(resourceid);
	}

	/**
	 * 保存操作
	 * 
	 * @param oper
	 *            操作内容
	 * @return 是否成功标志
	 */
	@RequestMapping("/saveResourceOper.do")
	@ResponseBody
	public ResultMsg saveResourceOper(@ModelAttribute SysResourceOper oper) {
		try {
			sysResourceOperService.saveResourceOper(oper);
			return new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			if (e instanceof AppException) {
				return new ResultMsg(false, e.getMessage());
			}
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
	}

	/**
	 * 删除操作
	 * 
	 * @param id
	 *            操作id
	 * @return 是否成功标志
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public ResultMsg delete(Long id) {
		try {
			sysResourceOperService.delete(id);
			return new ResultMsg(true, AppException.getMessage("crud.delok"));
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AppException) {
				return new ResultMsg(false, e.getMessage());
			}
			return new ResultMsg(false, AppException.getMessage("crud.delerr"));
		}
	}

}
