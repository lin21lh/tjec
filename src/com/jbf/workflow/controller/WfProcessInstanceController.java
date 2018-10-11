/************************************************************
 * 类名：WfProcessInstanceController
 *
 * 类别：Controller
 * 功能：工作流实例管理控制器
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbf.common.security.SecureUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.service.SysWorkflowManageService;

@Controller
@RequestMapping({ "/workflow/WfProcessInstanceController" })
public class WfProcessInstanceController {

	@Autowired
	SysWorkflowManageService sysWorkflowManageService;

	/**
	 * 启动流程
	 * 
	 * @param key
	 *            流程key
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping({ "/startProcessByKey.do" })
	public ResultMsg startProcessByKey(String pdefkey, String variables) {

		ResultMsg msg = null;
		try {
			// 放入发起人
			Map var = new HashMap();
			SysUser user = SecureUtil.getCurrentUser();
			var.put("startUser", user);
			if (variables != null)
				variables = URLDecoder.decode(variables, "UTF-8");

			// 放入前台变量
			if (variables != null && variables.trim().length() > 0) {
				String[] pairs = variables.split(";");
				for (String p : pairs) {
					String[] kv = p.split(":");
					if (kv.length >= 2) {
						var.put(kv[0], kv[1]);
					}
				}
			}
			sysWorkflowManageService.startProcessByKey(pdefkey, var);
			msg = new ResultMsg(true, "启动成功!");
		} catch (Exception e) {
			msg = ResultMsg.build(e, "启动流程时发生异常");
		}
		return msg;
	}

	/**
	 * 启动新流程并推送到下一个流程
	 * 
	 * @param pdefkey
	 *            流程定义key
	 * @param variables
	 *            环境变量
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping({ "/startProcessByKeyAndPush.do" })
	public ResultMsg startProcessByKeyAndPush(String pdefkey, String variables) {

		ResultMsg msg = null;
		try {
			// 放入发起人
			Map var = new HashMap();
			SysUser user = SecureUtil.getCurrentUser();
			var.put("startUser", user);
			variables = URLDecoder.decode(variables, "UTF-8");

			// 放入前台变量
			if (variables != null && variables.trim().length() > 0) {
				String[] pairs = variables.split(";");
				for (String p : pairs) {
					String[] kv = p.split(":");
					if (kv.length >= 2) {
						var.put(kv[0], kv[1]);
					}
				}
			}
			sysWorkflowManageService.startProcessByKeyAndPush(pdefkey, var);
			msg = new ResultMsg(true, "启动成功!");
		} catch (Exception e) {
			msg = ResultMsg.build(e, "启动流程时发生异常");
		}
		return msg;
	}

	/**
	 * 完成任务节点处理
	 * 
	 * @param taskid
	 *            任务id
	 * @param outcome
	 *            流出路径
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping({ "/completeTask.do" })
	public ResultMsg completeTask(String execId, String outcome, String actiId) {
		ResultMsg msg = null;
		try {
			sysWorkflowManageService.completeTask(execId, actiId, outcome,
					null, null, "");
			msg = new ResultMsg(true, "流程处理成功!");
		} catch (Exception e) {
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}

	/**
	 * 完成当前任务并设置环境变量
	 * 
	 * @param taskid
	 * @param outcome
	 * @param variables
	 * @return 成功标志
	 */
	// @ResponseBody
	// @RequestMapping({ "/completeTaskWithVariables.do" })
	// public ResultMsg completeTaskWithVariables(String taskid, String outcome,
	// String variables) {
	// ResultMsg msg = null;
	// try {
	// sysWorkflowManageService.completeTask(taskid, outcome, variables);
	// msg = new ResultMsg(true, "流程处理成功!");
	// } catch (Exception e) {
	// msg = ResultMsg.build(e, "处理任务时发生异常!");
	// }
	// return msg;
	// }

	// @RequestMapping("/completeTaskByExecId.do")
	// @ResponseBody
	// public ResultMsg completeTaskByExecId(String execId, String outcome,
	// String vars) {
	// try {
	// sysWorkflowManageService
	// .completeTaskByExecId(execId, outcome, vars);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return new ResultMsg(false, "任务处理失败，请查看日志！");
	// }
	// return new ResultMsg(true, "任务处理成功！");
	// }

	/**
	 * 取得用户的待办任务
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping({ "/getUserTasks.do" })
	public List getUserTasks(String key, String actiid) {
		return sysWorkflowManageService.getUserTasks(key, actiid);
	}

	/**
	 * 取得用户的历史任务
	 * 
	 * @param key
	 * @return 历史任务列表
	 */
	@ResponseBody
	@RequestMapping({ "/getUserHistoryTasks.do" })
	public List getUserHistoryTasks(String key) {
		return sysWorkflowManageService.getUserHistoryTasks(key);
	}

	/**
	 * 取得用户的可选任务
	 * 
	 * @return 可选任务列表
	 */
	@ResponseBody
	@RequestMapping({ "/getUserCandidateTask.do" })
	public List getUserCandidateTask() {
		List l = sysWorkflowManageService.getUserCandidateTask();
		return l;
	}

	/**
	 * 接受可选任务变成待办任务
	 * 
	 * @param taskid
	 *            任务 id
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping({ "/takeTask.do" })
	public ResultMsg takeTask(String taskid) {
		ResultMsg msg = null;
		try {
			sysWorkflowManageService.takeTask(taskid);
			msg = new ResultMsg(true, "接受任务成功!");
		} catch (Exception e) {
			msg = ResultMsg.build(e, "接受任务时发生异常!");
		}
		return msg;
	}

	/**
	 * 撤回流程
	 * 
	 * @param execid
	 *            流程流向
	 * @param actiId
	 *            撤回目标任务节点id
	 * @param variables
	 *            变量
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping({ "/getBackWorkflow.do" })
	public ResultMsg getBackWorkflow(String execid, String actiId,
			String variables) {
		ResultMsg msg = null;
		try {
			sysWorkflowManageService.getBackWorkflow(execid, actiId, SecureUtil
					.getCurrentUser().getUsercode(), variables);
			msg = new ResultMsg(true, "流程撤回成功!");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}

	/**
	 * 按key查询工作流的实例
	 * 
	 * @param key
	 * @return 工作流的实例列表
	 */
	@ResponseBody
	@RequestMapping({ "/queryWorkflowInstances.do" })
	public List queryWorkflowInstances(String key) {
		return sysWorkflowManageService.queryWorkflowInstances(key);
	}

	/**
	 * (组件调试用)
	 * 
	 * @param execId
	 * @param key
	 * @param version
	 * @param assignee
	 * @param transition
	 * @return
	 */
	@ResponseBody
	@RequestMapping({ "/workflowComponentTest.do" })
	public ResultMsg workflowComponentTest(String execId, String key,
			String version, String assignee, String transition) {

		try {
			sysWorkflowManageService.workflowComponentTest(execId, key,
					version, assignee, transition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultMsg(true, "测试结果请使用调试进行或查看控制台结果打印!");

	}

	@ResponseBody
	@RequestMapping({ "/workflowComponentTest2.do" })
	public ResultMsg workflowComponentTest2(String execId, String key,
			String version, String assignee, String transition) {

		try {
			sysWorkflowManageService.workflowComponentTest2(execId, key,
					version, assignee, transition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResultMsg(true, "测试结果请使用调试进行或查看控制台结果打印!");

	}
}
