/************************************************************
 * 类名：SysHelpDocController.java
 *
 * 类别：Controller
 * 功能：提供帮助文档的页面入口和增删改查功能
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-12-6  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.helpdoc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.exception.AppException;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.appexception.service.SysAppExceptionService;

@Scope("prototype")
@Controller
@RequestMapping({"/sys/helpdoc/sysHelpDocController"})
public class SysHelpDocController {

	@Autowired
	SysAppExceptionService appExceptionService;
	
	/**
	 * 自定义异常页面
	 * @return
	 */
	@RequestMapping({"/entry.do"})
	public ModelAndView entry() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
	
		return new ModelAndView("/sys/helpDocEntry", "modelMap", modelMap);
	}
	
	/**
	 * 帮助文档保存
	 * @param request
	 * @return
	 */
	@RequestMapping({"/saveHelpDoc.do"})
	@ResponseBody
	public ResultMsg saveLog(HttpServletRequest request) {
		ResultMsg resultMsg = null;	
		return resultMsg;
	}
	
	/**
	 * 帮助文档删除
	 * @param request
	 * @return ResultMsg
	 * @throws AppException 
	 */
	@RequestMapping({"/deleteHelpDoc.do"})
	@ResponseBody
	public ResultMsg deleteHelpDoc(HttpServletRequest request) throws AppException {
		
		ResultMsg resultMsg = null;
		
		return resultMsg;
	}
}
