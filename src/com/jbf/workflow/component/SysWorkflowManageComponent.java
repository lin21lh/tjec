/************************************************************
 * 类名：SysWorkflowManageComponent
 * 
 * 类别：开发接口组件
 * 功能：工作流实例管理组件
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   hyf         初版
 *   
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbpm.api.Execution;
import com.jbf.common.web.ResultMsg;
import com.jbf.workflow.po.SysWorkflowOpinion;
import com.jbf.workflow.vo.HistoryOpinionVO;
import com.jbf.workflow.vo.HistoryTaskVO;
import com.jbf.workflow.vo.ProcessDefinitionVO;
import com.jbf.workflow.vo.UserTodoListVo;

public interface SysWorkflowManageComponent {

	/**
	 * 启动流程 <br/>
	 * 如果启动成功，可以由ResutlMsg取得流程实例ID,方法如下 ResultMsg.getBody().get("EXECID")<br/>
	 * 并可以由ResultMsg.getBody().get("ACTIID")取得启动后的当时节点<br/>
	 * 如果启动失败，可以由ResultMsg.getBody().get("ERRORCODE")取得错误代码<br/>
	 * 
	 * @param pdefkey
	 *            流程定义key
	 * @param variables
	 *            流程环境变量
	 * @throws Exception
	 */
	public ResultMsg startProcessByKey(String pdefkey, Map variables);

	/**
	 * 启动流程，并推送到第二个节点 <br/>
	 * 如果启动成功，可以由ResutlMsg取得流程实例ID,方法如下 ResultMsg.getBody().get("EXECID")<br/>
	 * 并可以由ResultMsg.getBody().get("ACTIID")取得启动后的当时节点<br/>
	 * 如果启动失败，可以由ResultMsg.getBody().get("ERRORCODE")取得错误代码<br/>
	 * 
	 * @param pdefkey
	 *            流程定义key
	 * @param variables
	 *            流程环境变量
	 * @throws Exception
	 */
	public ResultMsg startProcessByKeyAndPush(String pdefkey, Map variables);

	/**
	 * 完成任务节点处理 ,不需要指定流出路径<br/>
	 * 如果处理成功可以由ResultMsg.getBody().get("ACTIID")取得启动后的当时节点<br/>
	 * 如果启动失败，可以由ResultMsg.getBody().get("ERRORCODE")取得错误代码<br/>
	 * 
	 * @param execId
	 *            流程实例id
	 * @param actiId
	 *            流程节点，用于验证是否具有处理权限
	 * @param variables
	 *            要新增或修改的环境变量
	 * @param assignee
	 *            处理人
	 * @param opinion
	 *            意见
	 * 
	 * @throws Exception
	 */
	public ResultMsg completeTask(String execId, String actiId, Map variables,
			String assignee, String opinion) throws Exception;

	/**
	 * 完成任务节点处理<br/>
	 * 如果处理成功可以由ResultMsg.getBody().get("ACTIID")取得启动后的当时节点<br/>
	 * 如果启动失败，可以由ResultMsg.getBody().get("ERRORCODE")取得错误代码<br/>
	 * 
	 * @param execId
	 *            流程实例id
	 * @param actiId
	 *            流程节点，用于验证是否具有处理权限
	 * @param outcome
	 *            流出路径,可为null
	 * @param variables
	 *            要新增或修改的环境变量
	 * @param assignee
	 *            处理人
	 * @param opinion
	 *            意见
	 * 
	 * @throws Exception
	 */
	public ResultMsg completeTask(String execId, String actiId, String outcome,
			Map variables, String assignee, String opinion) throws Exception;

	/**
	 * 查询用户的待办任务（不推荐使用）
	 * 
	 * @return 用户的待办任务
	 */
	@Deprecated
	public List<HashMap> getUserTasks();

	/**
	 * 查询用户待办任务对应的流程实例（不推荐使用）
	 * 
	 * @param key
	 *            流程key
	 * @param activityId
	 *            活动节点ID
	 * @param businessPKVarName
	 *            流程中存放关联业务对象主键的环境变量名称
	 * @return 流程实例ID列表
	 */
	// @Deprecated
	// public List<UserTodoListVo> getUserTodoListByWfKey(String usercode,
	// String key, String activityId, String businessPKVarName);

	/**
	 * 查询用户待办任务对应的流程实例列表
	 * 
	 * @param usercode
	 *            用户名
	 * @param key
	 *            流程key
	 * @param activityId
	 *            活动节点ID
	 * @param backFlags
	 *            回退标志过滤条件, NORMAL 代表正常流转, RETURN 代表退回 ,WITHDRAW 代表撤回,可多选
	 *            如不需要过滤，可以填null
	 * 
	 * @return 流程实例列表
	 */
	public List<UserTodoListVo> getUserTodoListByWfKey(String usercode,
			String key, String activityId, Set<String> backFlags);

	/**
	 * 查询用户历史任务对应的流程实例列表，以","分割
	 * 
	 * @param usercode
	 *            用户名
	 * @param key
	 *            流程key
	 * @param activityId
	 *            活动节点ID
	 * 
	 * @return 流程实例ID列表
	 */
	public String getUserHistoryExecidsByWfKey(String usercode, String key,
			String activityId);

	/**
	 * 查询用户的历史任务
	 * 
	 * @return 历史任务列表
	 */

	public List<HistoryTaskVO> getUserHistoryTasks(String key);

	/**
	 * 流程是否可被撤回
	 * 
	 * @param execId
	 *            流程id
	 * @param actiId
	 *            节点id
	 * @param assignee
	 *            处理人
	 * @return 是否可被撤回以及信息
	 */
	public ResultMsg isWorkflowWithdrawable(String execId, String actiId,
			String assignee);

	/**
	 * 撤回任务
	 * 
	 * @param execid
	 *            流程实例ID
	 * @param actiId
	 *            工作流节点ID
	 * @param usercode
	 *            申请人用户名
	 * @param variables
	 *            流程变量
	 * @throws Exception
	 */
	public ResultMsg getBackWorkflow(String execid, String actiId,
			String usercode, Map variables) throws Exception;

	/**
	 * 查询用户的候选任务(二次开发很少使用)
	 * 
	 * @return 用户的候选任务
	 */
	public List getUserCandidateTask();

	/**
	 * 接受任务(二次开发很少使用)，即所说的“预占”
	 * 
	 * @param taskid
	 * @throws Exception
	 */
	public void takeTask(String taskid) throws Exception;

	/**
	 * 取得流程KEY的所有活动实例
	 * 
	 * @param key
	 * @return 活动实例列表
	 */
	public List<? extends Map> queryWorkflowInstances(String key);

	/**
	 * 取得流程实例所在的节点名称 ，使用getCurrentActivityName方法代替
	 * 
	 * @param e
	 * @return 节点名称
	 */
	@Deprecated
	public String getExcutionName(Execution e);

	/**
	 * 工作流任务节点表单是否可编辑
	 * 
	 * @param execId
	 *            流程的实例id
	 * @param actiId
	 *            流程节点ID.
	 * @return 表单是否可编辑
	 */
	public Boolean isWorkflowTaskFormEditable(String execId, String actiId)
			throws Exception;

	/**
	 * 工作流任务节点表单是否可编辑
	 * 
	 * @param key
	 *            流程定义key
	 * @param version
	 *            流程定义version
	 * @param actiId
	 *            流程节点ID.
	 * @return 表单是否可编辑
	 * @throws Exception
	 */
	public Boolean isWorkflowTaskFormEditable(String key, Integer version,
			String actiId) throws Exception;

	/**
	 * 取得流程任务节点的可选流出路径
	 * 
	 * @param execid
	 *            流程实例ID
	 * @param actiId
	 *            流程节点
	 * @param assignee
	 *            受托人
	 * @param outcomeType
	 *            流出路径类型 ALL代表所有的，RETURN 代表退回 ，NORMAL 代表正常流转，传入null时等同于NORMAL
	 * @return 可选流出路径
	 * @throws Exception
	 */
	public Set<String> getOutcomes(String execid, String actiId,
			String assignee, String outcomeType);

	/**
	 * 查询流程所有的意见列表
	 * 
	 * @param execid
	 *            流程ID
	 * 
	 * @param dateOrder
	 *            排序方式，可为 按时间正序asc 或按时间逆序desc ，为 null时默认逆序
	 * @return 意见列表
	 */
	public List<SysWorkflowOpinion> getOpinions(String execid, String dateOrder);

	/**
	 * 查询流程特定处理人、特定节点的意见列表
	 * 
	 * @param execId
	 *            流程ID
	 * @param srcacti
	 *            源活动节点ID 可为null
	 * @param transition
	 *            流出路径名称 可为null
	 * @param dateOrder
	 *            排序方式，可为 按时间正序asc 或按时间逆序desc ，为 null时默认逆序
	 * @return 意见列表
	 */
	public List<SysWorkflowOpinion> getOpinions(String execId, String srcacti,
			String transition, String dateOrder);

	/**
	 * 查询流程最后的一次处理意见
	 * 
	 * @param execId
	 *            流程ID
	 */
	public SysWorkflowOpinion getLatestOpinion(String execId);

	/**
	 * 查询 一个工作流实例的所有意见
	 * 
	 * @param execId
	 *            流程实例
	 * @param order
	 *            排序方式,按时间排序，可选值"asc"或"desc"
	 * @return 工作流实例的所有意见
	 */
	public List<HistoryOpinionVO> getWorkflowHistoryOpinions(String execId,
			String order);

	/**
	 * 流程是否可执行普通退回
	 * 
	 * @param execId
	 *            流程ID
	 * @param actiId
	 *            活动节点
	 * @param assignee
	 *            当前节点处理人
	 * @return 是否可退回
	 */
	public ResultMsg isWorkflowReturnable(String execId, String actiId,
			String assignee);

	/**
	 * 退回流程，退回到上一节点的处理人进行处理 <br/>
	 * 如果退回成功，可由resultmsg.getBody().get("ACTIID")取得返回目标节点
	 * 
	 * @param execId
	 *            流程ID
	 * @param actiId
	 *            活动节点
	 * @param variables
	 *            环境 变量
	 * @param assignee
	 *            当前节点处理人
	 * @param opinion
	 *            意见
	 * @return 操作结果
	 */
	public ResultMsg sendBackWorkflow(String execId, String actiId,
			Map variables, String assignee, String opinion);

	/**
	 * 首节点是否可退回
	 * 
	 * @param execId
	 *            流程ID
	 * @param actiId
	 *            活动节点
	 * @param assignee
	 *            当前节点处理人
	 * @return
	 * 
	 */
	public ResultMsg isFirstNodeBackable(String execId, String actiId,
			String assignee);

	/**
	 * 退回流程，退回到首节点 <br/>
	 * 如果退回成功，可由resultmsg.getBody().get("ACTIID")取得返回目标节点
	 * 
	 * @param execId
	 *            流程ID
	 * @param actiId
	 *            活动节点
	 * @param variables
	 *            环境 变量
	 * @param assignee
	 *            当前节点处理人
	 * @param opinion
	 *            意见
	 * @return 操作结果
	 */
	public ResultMsg sendBackWorkflowToFirstNode(String execId, String actiId,
			Map variables, String assignee, String opinion);

	/**
	 * 取得流程当前环节的name
	 * 
	 * @param execId
	 *            流程实例ID
	 * @return 当前环节的name
	 */
	public String getCurrentActivityName(String execId);

	/**
	 * 取得迁称线上配置的过后状态
	 * 
	 * @param key
	 * @param version
	 * @param actiId
	 *            源活动节点
	 * @param transition
	 *            迁移线名称
	 * @return 配置的状态，如果查询不到返回null
	 */
	public String getTransitionDoneStatus(String key, String version,
			String actiId, String transition);

	/**
	 * 将字符串格式的环境变量转成HashMap
	 * 
	 * @param varStr
	 *            格式例如 "varA:valueA;varB:valueB"
	 * @return HashMap格式的环境变量
	 */
	public HashMap parseVariables(String varStr);

	/**
	 * 由流程实例ID 取得流程定义
	 * 
	 * @param execId
	 *            流程实例ID
	 * @return 流程定义信息
	 */
	public ProcessDefinitionVO getProcessDefinitionByExecId(String execId);

	/**
	 * 流程实例挂起
	 * 
	 * @param execId
	 *            流程实例ID
	 * @return 操作结果是否成功及出错信息
	 */
	public ResultMsg suspend(String execId);

	/**
	 * 流程实例解挂
	 * 
	 * @param execId
	 *            流程实例ID
	 * @return 操作结果是否成功及出错信息
	 */
	public ResultMsg resume(String execId);

	/**
	 * 检查流程实例是否已被挂起
	 * 
	 * @param execId
	 *            流程实例ID
	 * @return ResultMsg
	 *         如果流程实例存在则resultMsg.isSucesss()为true，同时是被挂起的结果可由resultMsg.getBody
	 *         ().get("isSuspended")取得。如果流程实例不存在或其他原因，resultMsg.isSucesss()
	 *         为false，且原因由resultMsg.getTitle()取得。
	 * 
	 */
	public ResultMsg isSuspended(String execId);

	/**
	 * 由错误编码取得错误信息
	 * 
	 * @param code
	 *            错误编码
	 * @param args
	 *            参数列表
	 * @return 错误信息
	 */
	public String getExceptionMessage(String code, String[] args);

	/**
	 * 取得全局参数配置
	 * 
	 * @param configCode
	 * @return 全局配置值
	 */
	public String getGlobalConfig(String configCode);

	/**
	 * 取得工作流所定义的所有版本
	 * 
	 * @param key
	 *            工作流key
	 * @return 版本列表
	 */
	public List<Integer> getWorkflowVersions(String key);

	/**
	 * 取得工作流版本中的所有活动节点
	 * 
	 * @param key
	 *            工作流key
	 * @param version
	 *            版本号
	 * @return Map 其中包含 actiId,actiName,type三个属性
	 */
	public List<HashMap> getWorkflowDefinitionActivities(String key, int version);

	/**
	 * 取得工作流启用的版本
	 * 
	 * @param key
	 *            工作流key
	 * @param date
	 *            日期
	 * @return 找不到合法的版本时，返回-1
	 */
	public int getValidVersionByDate(String key, Date date);

}
