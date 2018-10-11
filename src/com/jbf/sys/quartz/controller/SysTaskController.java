/************************************************************
 * 类名：SysTaskController.java
 *
 * 类别：Controller
 * 功能：提供定时任务管理的页面入口 查询、暂停、恢复、移除定时任务
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-01-07  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.quartz.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.exception.AppException;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.quartz.dto.TaskDetailDTO;
import com.jbf.sys.quartz.service.QuartzTaskService;

@Scope("prototype")
@Controller
@RequestMapping({"/sys/log/sysTaskController"})
public class SysTaskController {
	
	@Autowired
	QuartzTaskService quartzTaskService;

	/**
	 * 日志页面
	 * @return
	 */
	@RequestMapping({"/entry.do"})
	public ModelAndView entry() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
	
		return new ModelAndView("/sys/sysTaskEntry", "modelMap", modelMap);
	}
	
	/**
	 * 定时任务查询
	 * @param request
	 * @return 定时任务列表数据
	 * @throws SchedulerException 
	 */
	@RequestMapping("/query.do")
	@ResponseBody
	public List<TaskDetailDTO> query(HttpServletRequest request) throws SchedulerException {
		
		String group = request.getParameter("group");
		
		return quartzTaskService.query(group);
	}
	/**
	 * 暂停定时任务
	 * @param name 定时任务名称
	 * @param group 定时任务组
	 * @return
	 */
	@RequestMapping({"/pauseTask.do"})
	@ResponseBody
	public ResultMsg pauseTask(String name, String group) {
		ResultMsg resultMsg = null;
		try {
			quartzTaskService.pauseTask(name, group);
			resultMsg = new ResultMsg(true, "暂停成功！");
		} catch(RuntimeException e) {
			resultMsg = new ResultMsg(false, "暂停失败。失败原因：" + e.getMessage());
		}
		
		return resultMsg;
	}
	
	/**
	 * 恢复定时任务
	 * @param name 定时任务名称
	 * @param group 定时任务组
	 * @return
	 */
	@RequestMapping({"/resumeTask.do"})
	@ResponseBody
	public ResultMsg resumeTask(String name, String group) {
		ResultMsg resultMsg = null;
		try {
			quartzTaskService.resumeTask(name, group);
			resultMsg = new ResultMsg(true, "恢复成功！");
		} catch(RuntimeException e) {
			resultMsg = new ResultMsg(false, "恢复失败。失败原因：" + e.getMessage());
		}
		
	return resultMsg;
	}
	
	/**
	 * 移除定时任务
	 * @param name 定时任务名称
	 * @param group 定时任务组
	 * @return
	 */
	@RequestMapping({"/removeTask.do"})
	@ResponseBody
	public ResultMsg removeTask(String name, String group) {
		ResultMsg resultMsg = null;
			boolean success = quartzTaskService.removeTask(name, group);
			if (success)
				resultMsg = new ResultMsg(success, AppException.getMessage("crud.delok"));
			else
				resultMsg = new ResultMsg(success, AppException.getMessage("crud.delerr"));
		return resultMsg;
	}
}
