/************************************************************
 * 类名：SysLogController.java
 *
 * 类别：Controller
 * 功能：提供日志管理的页面入口和删查功能
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.log.controller;

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
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.log.service.SysLogService;

@Scope("prototype")
@Controller
@RequestMapping({"/sys/log/sysLogController"})
public class SysLogController {

	@Autowired
	SysLogService logService;
	
	/**
	 * 日志页面
	 * @return
	 */
	@RequestMapping({"/entry.do"})
	public ModelAndView entry() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
	
		modelMap.put("starttimeDef", DateUtil.getCurrentDate() + " 00:00:00");
		modelMap.put("endtimeDef", DateUtil.getCurrentDate() + " 23:59:59");
		return new ModelAndView("/sys/logEntry", "modelMap", modelMap);
	}
	
	/**
	 * 日志查询
	 * @param request
	 * @return 日志列表数据
	 */
	@RequestMapping("/query.do")
	@ResponseBody
	public EasyUITotalResult query(HttpServletRequest request) {
		
		String rowsStr = request.getParameter("rows");
		String pageStr = request.getParameter("page");
		Integer rows = StringUtil.isNotBlank(rowsStr) ? Integer.valueOf(rowsStr) : 30;
		Integer page = StringUtil.isNotBlank(pageStr) ? Integer.valueOf(pageStr) : 1;
		
		String opertypeStr = request.getParameter("opertype");
		Integer opertype = StringUtil.isNotBlank(opertypeStr) ? Integer.valueOf(opertypeStr) : null;
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		PaginationSupport ps = logService.query(rows, page, opertype, starttime, endtime);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 日志删除
	 * @param request
	 * @return ResultMsg
	 */
	@RequestMapping({"/deleteLog.do"})
	@ResponseBody
	public ResultMsg deleteLog(HttpServletRequest request) {
		
		String logids = request.getParameter("logids");
		ResultMsg resultMsg = null;
		try {
			logService.deleteLog(logids);
			resultMsg = new ResultMsg(true, AppException.getMessage("crud.delok"));
		} catch (Exception e) {
			resultMsg = new ResultMsg(false, AppException.getMessage("crud.delerr"));
			e.printStackTrace();
		}
		
		return resultMsg;
	}
}
