/************************************************************
 * 类名：SysWorkflowManageService
 * 
 * 类别：Service
 * 功能：工作流实例管理服务
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   hyf         初版
 *   
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.api.ProcessInstance;

import com.jbf.common.web.ResultMsg;

public interface SysWorkflowManageService {

	/**
	 * 启动流程
	 * 
	 * @param pdefkey
	 *            流程定义key
	 * @param variables
	 *            流程环境变量
	 * @throws Exception
	 */
	public ResultMsg startProcessByKey(String pdefkey, Map variables)
			throws Exception;

	public ResultMsg startProcessByKeyAndPush(String pdefkey, Map var)
			throws Exception;

	/**
	 * 查询用户的待办任务
	 * 
	 * @return
	 */
	public List<HashMap> getUserTasks(String key, String actiid);

	/**
	 * 查询用户的历史任务
	 * 
	 * @return
	 */
	public List getUserHistoryTasks(String key);

	/**
	 * 撤回任务
	 * 
	 * @param execid
	 *            流程实例ID
	 * @param usercode
	 *            申请人用户名
	 * @param variables
	 *            流程变量
	 * @throws Exception
	 */
	public void getBackWorkflow(String execid, String actId, String usercode,
			String variables) throws Exception;

	/**
	 * 查询用户的可选任务
	 * 
	 * @return
	 */
	public List getUserCandidateTask();

	/**
	 * 授受任务
	 * 
	 * @param taskid
	 * @throws Exception
	 */
	public void takeTask(String taskid) throws Exception;

	/**
	 * 取得流程的活动 实例
	 * 
	 * @param key
	 * @return
	 */
	public List queryWorkflowInstances(String key);

	/**
	 * 二次开发不使用， 工作流组件测试用
	 * 
	 * @param execId
	 * @param key
	 * @param version
	 * @param assignee
	 * @param transition
	 */
	public void workflowComponentTest(String execId, String key,
			String version, String assignee, String transition)
			throws Exception;

	void workflowComponentTest2(String execId, String key, String version,
			String assignee, String transition) throws Exception;

	public void completeTask(String execId, String actiId, String outcome,
			Map variables, String assignee, String opinion) throws Exception;
}
