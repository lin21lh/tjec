/************************************************************
 * 类名：DemoWorkflowManageController
 *
 * 类别：Controller
 * 功能：工作流demo页面控制器
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.demo.workflow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbf.common.security.SecureUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.demo.workflow.service.LeaveNoteService;

@Controller
@RequestMapping("demo/DemoWorkflowManageController/")
public class DemoWorkflowManageController {
	/**
	 * 页面入口
	 * 
	 * @return
	 */

	@Autowired
	LeaveNoteService leaveNoteService;

	@RequestMapping("entry.do")
	public String entry() {
		return "demo/workflowManage";
	}

	/**
	 * 任务查询
	 * 
	 * @return
	 */
	@RequestMapping("workflowTaskQueryEntry.do")
	public String taskQueryEntry() {
		return "demo/workflowTaskQuery";
	}

	/**
	 * 任务查询
	 * 
	 * @return
	 */
	@RequestMapping("leaveNoteEntry.do")
	public String leaveNoteEntry() {
		return "demo/leaveNoteEntry";
	}

	/**
	 * 查询业务数据
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/leaveNoteQuery.do")
	@ResponseBody
	public List leaveNoteQuery() {
		return leaveNoteService.query();
	}

	/**
	 * 启动流程
	 * 
	 * @param leaveid
	 * @return
	 */

	@RequestMapping("/startLeaveWorkflow.do")
	@ResponseBody
	public ResultMsg startLeaveWorkflow(String leaveid) {
		try {
			leaveNoteService.startLeaveWorkflow(leaveid);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, "流程启动失败，请查看日志！");
		}
		return new ResultMsg(true, "流程启动成功！");
	}

	@RequestMapping("/completeTaskByExecId.do")
	@ResponseBody
	public ResultMsg completeTaskByExecId(String execId, String outcome,
			String vars, String opinion) {
		try {
			leaveNoteService.completeTaskByExecId(execId, outcome, vars,
					opinion);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, "任务处理失败，请查看日志！");
		}
		return new ResultMsg(true, "任务处理成功！");
	}

	@RequestMapping("/getUserTodoExecidsByWfKey.do")
	@ResponseBody
	public Map getUserTodoExecidsByWfKey(String wfkey, String activityId) {

		String usercode = SecureUtil.getCurrentUser().getUsercode();
		HashMap map = new HashMap();
		String s = leaveNoteService.getUserTodoExecidsByWfKey(usercode, wfkey,
				activityId);
		map.put("wfids", s);
		return map;

	}
	
	
	@RequestMapping("/getUserHistoryExecidsByWfKey.do")
	@ResponseBody
	public Map getUserHistoryExecidsByWfKey(String wfkey, String activityId) {

		String usercode = SecureUtil.getCurrentUser().getUsercode();
		HashMap map = new HashMap();
		String s = leaveNoteService.getUserHistoryExecidsByWfKey(usercode, wfkey,
				activityId);
		map.put("wfids", s);
		return map;

	}
}
