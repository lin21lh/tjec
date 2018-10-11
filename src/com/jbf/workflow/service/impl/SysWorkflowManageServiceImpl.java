/************************************************************
 * 类名：SysWorkflowBackattr
 *
 * 类别：ServiceImpl
 * 功能：工作流实例管理服务实现
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbpm.api.ProcessInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.common.security.SecureUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.workflow.component.SysWorkflowManageComponent;
import com.jbf.workflow.po.SysWorkflowOpinion;
import com.jbf.workflow.service.SysWorkflowManageService;
import com.jbf.workflow.vo.UserTodoListVo;

@Service
public class SysWorkflowManageServiceImpl implements SysWorkflowManageService {

	@Autowired
	SysWorkflowManageComponent sysWorkflowManageComponent;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public ResultMsg startProcessByKey(String pdefkey, Map variables)
			throws Exception {
		return sysWorkflowManageComponent.startProcessByKey(pdefkey, variables);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultMsg startProcessByKeyAndPush(String pdefkey, Map variables)
			throws Exception {
		return sysWorkflowManageComponent.startProcessByKeyAndPush(pdefkey,
				variables);
	}

	// @Override
	// @Transactional(propagation = Propagation.REQUIRED, rollbackFor =
	// Exception.class)
	// public void completeTask(String taskid, String outcome) throws Exception
	// {
	// sysWorkflowManageComponent.completeTask(taskid, outcome);
	// }
	//
	// @Override
	// @Transactional(propagation = Propagation.REQUIRED, rollbackFor =
	// Exception.class)
	// public void completeTask(String taskid, String outcome, String variables)
	// throws Exception {
	// sysWorkflowManageComponent.completeTask(taskid, outcome,
	// sysWorkflowManageComponent.parseVariables(variables));
	//
	// }
	//
	// @Override
	// @Transactional(propagation = Propagation.REQUIRED, rollbackFor =
	// Exception.class)
	// public void completeTaskByExecId(String execId, String outcome, String
	// vars)
	// throws Exception {
	// String usercode = SecureUtil.getCurrentUser().getUsercode();
	// sysWorkflowManageComponent.completeTaskByExecId(execId, outcome,
	// sysWorkflowManageComponent.parseVariables(vars), usercode);
	// }

	@Override
	public List getUserTasks(String key, String actiid) {
		// 待办任务
		List<UserTodoListVo> list = sysWorkflowManageComponent
				.getUserTodoListByWfKey(SecureUtil.getCurrentUser()
						.getUsercode(), key, actiid, null);
		List l = new ArrayList();
		for (UserTodoListVo vo : list) {
			HashMap map = new HashMap();
			map.put("assignee", SecureUtil.getCurrentUser().getUsercode());
			map.put("execid", vo.getWfid());
			map.put("activityName", actiid);
			map.put("backflag", vo.getBackflag());
			map.put("iscandidate", vo.getIscandidate());
			l.add(map);
		}
		//
		// list = sysWorkflowManageComponent.getUserTodoListByWfKey(SecureUtil
		// .getCurrentUser().getUsercode(), "LEAVE", "create", null);
		//
		// for (UserTodoListVo vo : list) {
		// HashMap map = new HashMap();
		// map.put("assignee", SecureUtil.getCurrentUser().getUsercode());
		// map.put("execid", vo.getWfid());
		// map.put("activityName", "create");
		// map.put("backflag", vo.getBackflag());
		// map.put("iscandidate", vo.getIscandidate());
		// l.add(map);
		// }
		//
		// list = sysWorkflowManageComponent.getUserTodoListByWfKey(SecureUtil
		// .getCurrentUser().getUsercode(), "LEAVE", "audit2", null);
		//
		// for (UserTodoListVo vo : list) {
		// HashMap map = new HashMap();
		// map.put("assignee", SecureUtil.getCurrentUser().getUsercode());
		// map.put("execid", vo.getWfid());
		// map.put("activityName", "audit2");
		// map.put("backflag", vo.getBackflag());
		// map.put("iscandidate", vo.getIscandidate());
		// l.add(map);
		// }
		return l;
	}

	@Override
	public List getUserHistoryTasks(String key) {
		return sysWorkflowManageComponent.getUserHistoryTasks(key);
	}

	/**
	 * 撤回流程
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void getBackWorkflow(String execid, String actiId, String usercode,
			String variables) throws Exception {
		ResultMsg msg = sysWorkflowManageComponent.getBackWorkflow(execid,
				actiId, usercode,
				sysWorkflowManageComponent.parseVariables(variables));
		if (!msg.isSuccess()) {
			throw new Exception(msg.getTitle());
		}
	}

	@Override
	public List getUserCandidateTask() {
		return sysWorkflowManageComponent.getUserCandidateTask();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void takeTask(String taskid) throws Exception {
		sysWorkflowManageComponent.takeTask(taskid);
	}

	@Override
	public List queryWorkflowInstances(String key) {
		return sysWorkflowManageComponent.queryWorkflowInstances(key);
	}

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void workflowComponentTest(String execId, String key,
			String version, String assignee, String transition)
			throws Exception {
		execId = "LEAVE.230001";
		key = "LEAVE";
		version = "13";
		assignee = "admin";
		transition = "发送至";
		String actiId = "task3";

		// 测试1 - 启动流程测试，可验证环境变量设定、受托分配器、状态写入器的可用情况
		// HashMap map = new HashMap();
		// map.put("TASK", "task333");
		// ResultMsg msg = sysWorkflowManageComponent.startProcessByKeyAndPush(
		// key, map);
		// if (msg.isSuccess()) {
		// String pid = (String) msg.getBody().get("EXECID");
		// System.out.println("流程实例ID:" + pid);
		//
		// System.out.println("当前环节名称："
		// + sysWorkflowManageComponent.getCurrentActivityName(pid));
		// } else {
		// System.out.println("流程启动失败:" + msg.getTitle());
		// }
		// //
		// System.out.println("调试结束");
		// if (true)
		// return;
		// 测试2 - 查询当前环节
		// System.out.println("当前环节名称："
		// + sysWorkflowManageComponent.getCurrentActivityName(execId));

		// 测试3 -测试待处理查询

		// List<UserTodoListVo> list1 = sysWorkflowManageComponent
		// .getUserTodoListByWfKey("admin", key, actiId, null);
		//
		// Set<String> set = new HashSet<String>();
		// set.add("RETURN");
		// List<UserTodoListVo> list2 = sysWorkflowManageComponent
		// .getUserTodoListByWfKey("admin", key, actiId, set);
		// set.clear();
		// set.add("NORMAL");
		// List<UserTodoListVo> list3 = sysWorkflowManageComponent
		// .getUserTodoListByWfKey("admin", key, actiId, set);
		//
		// set.clear();
		// set.add("WITHDRAW");
		// List<UserTodoListVo> list4 = sysWorkflowManageComponent
		// .getUserTodoListByWfKey("admin", key, actiId, set);
		//
		// set.clear();
		// set.add("WITHDRAW");
		// set.add("NORMAL");
		// List<UserTodoListVo> list5 = sysWorkflowManageComponent
		// .getUserTodoListByWfKey("admin", key, actiId, set);

		// 测试4 -测试已处理查询

		// String str = sysWorkflowManageComponent.getUserHistoryExecidsByWfKey(
		// "admin", key, "task1");

		// 测试5 -测试是否可撤回

		// ResultMsg msg = sysWorkflowManageComponent.isWorkflowWithdrawable(
		// execId, "create", assignee);

		// 测试6 - 取得当前可选流出路径

		// Set<String> outcome1 = sysWorkflowManageComponent.getOutcomes(execId,
		// actiId, assignee, "ALL");
		//
		// Set<String> outcome2 = sysWorkflowManageComponent.getOutcomes(execId,
		// actiId, assignee, "RETURN");
		//
		// Set<String> outcome3 = sysWorkflowManageComponent.getOutcomes(execId,
		// actiId, assignee, "NORMAL");
		//
		// Set<String> outcome4 = sysWorkflowManageComponent.getOutcomes(execId,
		// actiId, assignee, null);
		// System.out.println("调试结束");
		// 测试7 - 取得当前表单是否可修改
		// Boolean bl =
		// sysWorkflowManageComponent.isWorkflowTaskFormEditable(key,
		// Long.parseLong(version), actiId);

		// 测试8 - 测试流程推进
		// HashMap map = new HashMap();
		// map.put("new_var", "新变量值");
		// ResultMsg msg =
		// sysWorkflowManageComponent.completeTask("LEAVE.230001",
		// "task2", null, null, "admin", "发送");

		// ResultMsg msg2 =
		// sysWorkflowManageComponent.completeTask("LEAVE.20064",
		// "audit2", null, null, "admin", "发送no.4");

		// System.out.println("调试结束");

		// if (true)
		// throw new Exception("a");

		// ResultMsg msg2 =
		// sysWorkflowManageComponent.completeTask("LEAVE.20076",
		// "audit2", "发送至结束", null, "admin", "发送");

		// 测试9 - 意见查询
		// List<SysWorkflowOpinion> opinions = sysWorkflowManageComponent
		// .getOpinions(execId, "desc");
		//
		// SysWorkflowOpinion opn = sysWorkflowManageComponent
		// .getLatestOpinion(execId);

		// 测试10 - 测试流程退回
		//
		// Set<String> outSet = sysWorkflowManageComponent.getOutcomes(execId,
		// "task3", assignee, "ALL");
		//
		ResultMsg msg = sysWorkflowManageComponent.sendBackWorkflow(
				"LEAVE.220043", "task2", null, "admin", "不合格");
		// int io=0;
		// if(true){
		// throw new Exception("aa");
		// }
		System.out.println("调试结束");

		// Set<String> set = new HashSet<String>();
		// set.add("RETURN");
		// List<UserTodoListVo> list2 = sysWorkflowManageComponent
		// .getUserTodoListByWfKey("admin", key, "task2", set);

		// 测试11 - 过后状态查询

		// String str =
		// sysWorkflowManageComponent.getTransitionDoneStatus("LEAVE", "1",
		// "task1", "发送至初审");

		// 4.解挂测试
		// ResultMsg msg = sysWorkflowManageComponent.resume("LEAVE.30029");
		// 5.是否挂起API测试

		// ResultMsg msg =
		// sysWorkflowManageComponent.isSuspended("LEAVE.30029");

	}

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void workflowComponentTest2(String execId, String key,
			String version, String assignee, String transition)
			throws Exception {
		// 1.撤回测试
		ResultMsg msg = sysWorkflowManageComponent.getBackWorkflow(
				"LEAVE.240003", "task2", "admin", null);
		// 2.历史查询测试
		// List ops = sysWorkflowManageComponent.getWorkflowHistoryOpinions(
		// "LEAVE.130029", "asc");

		// 3.挂起测试
		// ResultMsg msg = sysWorkflowManageComponent.suspend("LEAVE.30029");
		// 4.解挂测试
		// ResultMsg msg = sysWorkflowManageComponent.resume("LEAVE.30029");

		// 5.测试退回首节点
		// ResultMsg msg =
		// sysWorkflowManageComponent.sendBackWorkflowToFirstNode(
		// "LEAVE.220043", "task3", null, "admin", "");
		//
		System.out.println("调试结束");

		// if (msg.isSuccess() == false) {
		// throw new Exception(msg.getTitle());
		// }

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void completeTask(String execId, String actiId, String outcome,
			Map variables, String assignee, String opinion) throws Exception {
		ResultMsg msg = sysWorkflowManageComponent.completeTask(execId, actiId,
				outcome, variables, assignee, opinion);
		if (!msg.isSuccess()) {
			throw new Exception(msg.getTitle());
		}

	}
}
