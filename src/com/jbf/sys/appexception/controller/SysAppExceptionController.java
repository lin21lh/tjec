/************************************************************
 * 类名：SysAppExceptionController.java.java
 *
 * 类别：Controller
 * 功能：提供自定义异常的页面入口和增删改查功能
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-12-6  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.appexception.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.appexception.service.SysAppExceptionService;

@Scope("prototype")
@Controller
@RequestMapping({"/sys/appexception/sysAppExceptionController"})
public class SysAppExceptionController {

	@Autowired
	SysAppExceptionService appExceptionService;
	
	/**
	 * 自定义异常页面
	 * @return
	 */
	@RequestMapping({"/entry.do"})
	public ModelAndView entry() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
	
		return new ModelAndView("/sys/appExceptionEntry", "modelMap", modelMap);
	}
	
	/**
	 * 自定义异常查询
	 * @param request
	 * @return 自定义异常列表数据
	 * @throws AppException 
	 */
	@RequestMapping("/query.do")
	@ResponseBody
	public EasyUITotalResult query(HttpServletRequest request) throws AppException, NullPointerException {
		
		String rowsStr = request.getParameter("rows");
		String pageStr = request.getParameter("page");
		Integer rows = StringUtil.isNotBlank(rowsStr) ? Integer.valueOf(rowsStr) : 30;
		Integer page = StringUtil.isNotBlank(pageStr) ? Integer.valueOf(pageStr) : 1;

		PaginationSupport ps = appExceptionService.query(rows, page);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 自定义异常保存
	 * @param request
	 * @return
	 */
	@RequestMapping({"/saveAppException.do"})
	@ResponseBody
	public ResultMsg saveLog(HttpServletRequest request) {
		ResultMsg resultMsg = null;	
		return resultMsg;
	}
	
	/**
	 * 异常删除
	 * @param request
	 * @return ResultMsg
	 * @throws AppException 
	 */
	@RequestMapping({"/deleteAppException.do"})
	@ResponseBody
	public ResultMsg deleteLog(HttpServletRequest request) throws AppException {
		
		String logids = request.getParameter("logids");
		ResultMsg resultMsg = null;
		
		return resultMsg;
	}
}
