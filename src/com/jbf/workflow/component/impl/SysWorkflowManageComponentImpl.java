/************************************************************
 * 类名：SysWorkflowManageComponentImpl
 *
 * 类别：开发接口组件实现
 * 功能：工作流实例管理服务实现
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.component.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.jbpm.api.Execution;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessDefinitionQuery;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.ProcessInstanceQuery;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskQuery;
import org.jbpm.api.TaskService;
import org.jbpm.api.history.HistoryActivityInstance;
import org.jbpm.api.history.HistoryActivityInstanceQuery;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.history.HistoryProcessInstanceQuery;
import org.jbpm.api.history.HistoryTask;
import org.jbpm.api.history.HistoryTaskQuery;
import org.jbpm.api.model.Activity;
import org.jbpm.api.model.Transition;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.history.model.HistoryActivityInstanceImpl;
import org.jbpm.pvm.internal.history.model.HistoryTaskInstanceImpl;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.model.TransitionImpl;
import org.jbpm.pvm.internal.task.TaskImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.web.JbfContextLoaderListener;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.user.dao.SysUserDao;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.common.WfSpecialProcessCmd;
import com.jbf.workflow.common.WfSuspendAndResumeCmd;
import com.jbf.workflow.component.SysWorkflowManageComponent;
import com.jbf.workflow.dao.SysWorkflowBacklineRecDao;
import com.jbf.workflow.dao.SysWorkflowExtAttrDao;
import com.jbf.workflow.dao.SysWorkflowOpinionDao;
import com.jbf.workflow.dao.SysWorkflowProcversionDao;
import com.jbf.workflow.listener.WfEventListener;
import com.jbf.workflow.po.SysWorkflowBacklineRec;
import com.jbf.workflow.po.SysWorkflowExtAttr;
import com.jbf.workflow.po.SysWorkflowOpinion;
import com.jbf.workflow.po.SysWorkflowProcversion;
import com.jbf.workflow.vo.HistoryActivityVO;
import com.jbf.workflow.vo.HistoryOpinionVO;
import com.jbf.workflow.vo.HistoryTaskVO;
import com.jbf.workflow.vo.ProcessDefinitionVO;
import com.jbf.workflow.vo.UserTodoListVo;

@Component
public class SysWorkflowManageComponentImpl implements
		SysWorkflowManageComponent {

	@Autowired
	ProcessEngine processEngine;
	@Autowired
	SysWorkflowProcversionDao sysWorkflowProcversionDao;
	@Autowired
	SysWorkflowOpinionDao sysWorkflowOpinionDao;
	@Autowired
	SysWorkflowExtAttrDao sysWorkflowExtAttrDao;
	@Autowired
	SysWorkflowBacklineRecDao sysWorkflowBacklineRecDao;

	@Autowired
	SysUserDao sysUserDao;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ResultMsg startProcessByKey(String pdefkey, Map variables) {

		// 查询启用的version
		List<SysWorkflowProcversion> verlist = (List<SysWorkflowProcversion>) sysWorkflowProcversionDao
				.find(" from SysWorkflowProcversion where key= ?", pdefkey);

		if (verlist.size() == 0) {
			return new ResultMsg(false, "WFERR-0201", new String[] { pdefkey });
		}

		String curdate = DateUtil.getCurrentDate();
		SysWorkflowProcversion sel = null;
		for (SysWorkflowProcversion v : verlist) {
			if (v.getStartdate() == null) {
				continue;
			}
			if (curdate.compareTo(v.getStartdate()) >= 0) {
				// 满足起始条件
				if (v.getEnddate() == null) {
					sel = v;
					break;
				}
				if (curdate.compareTo(v.getEnddate()) <= 0) {
					sel = v;
					break;
				}
			}
		}

		if (sel == null) {
			// return new ResultMsg(false, "没有启用日期合法的工作流版本!");
			return new ResultMsg(false, "WFERR-0202", new String[] {});
		}

		// 查询流程定义
		RepositoryService re = processEngine.getRepositoryService();

		ProcessDefinitionQuery pdq = re.createProcessDefinitionQuery();
		pdq.deploymentId(sel.getDeploymentid());
		ProcessDefinition pd = pdq.uniqueResult();

		ExecutionService es = processEngine.getExecutionService();

		// 设置退回状态
		if (variables.get(WfEventListener.WF_BACK_FLAG) == null) {
			variables.put(WfEventListener.WF_BACK_FLAG, "NORMAL");
		}

		// 记录流程启动用户
		variables.put("startUser", SecureUtil.getCurrentUser());

		ProcessInstance processInstance = es.startProcessInstanceById(
				pd.getId(), variables);

		ResultMsg msg = new ResultMsg(true, "工作流启动成功！");
		HashMap map = new HashMap();
		map.put("EXECID", processInstance.getId());
		map.put("ACTIID", getCurrentActivityName(processInstance.getId()));
		msg.setBody(map);
		return msg;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResultMsg startProcessByKeyAndPush(String pdefkey, Map variables) {
		try {
			// 查询启用的version
			List<SysWorkflowProcversion> verlist = (List<SysWorkflowProcversion>) sysWorkflowProcversionDao
					.find(" from SysWorkflowProcversion where key= ?", pdefkey);

			if (verlist.size() == 0) {
				// return new ResultMsg(false, "工作流定义不存在，KEY值为" + pdefkey +
				// "!");
				return new ResultMsg(false, "WFERR-0201",
						new String[] { pdefkey });
			}

			String curdate = DateUtil.getCurrentDate();
			SysWorkflowProcversion sel = null;
			for (SysWorkflowProcversion v : verlist) {
				if (v.getStartdate() == null) {
					continue;
				}
				if (curdate.compareTo(v.getStartdate()) >= 0) {
					// 满足起始条件
					if (v.getEnddate() == null) {
						sel = v;
						break;
					}
					if (curdate.compareTo(v.getEnddate()) <= 0) {
						sel = v;
						break;
					}
				}
			}

			if (sel == null) {
				return new ResultMsg(false, "WFERR-0202", new String[] {});
				// return new ResultMsg(false, "没有启用日期合法的工作流版本!");
			}

			// 查询流程定义
			RepositoryService re = processEngine.getRepositoryService();
			ProcessDefinitionQuery pdq = re.createProcessDefinitionQuery();
			pdq.deploymentId(sel.getDeploymentid());
			ProcessDefinition pd = pdq.uniqueResult();
			ExecutionService es = processEngine.getExecutionService();
			// 设置退回状态
			if (variables.get(WfEventListener.WF_BACK_FLAG) == null) {
				variables.put(WfEventListener.WF_BACK_FLAG, "NORMAL");
			}
			// 记录流程启动用户
			variables.put("startUser", SecureUtil.getCurrentUser());
			ProcessInstance processInstance = es.startProcessInstanceById(
					pd.getId(), variables);
			System.out.println("process 启动：" + processInstance.getId() + " @"
					+ processInstance.getKey());
			String execid = processInstance.getId();

			TaskService taskService = processEngine.getTaskService();

			// 首先查询修行候选任务
			TaskQuery tq = taskService.createTaskQuery();
			tq.executionId(execid);
			tq.candidate(SecureUtil.getCurrentUser().getUsercode());
			List<Task> candidateTasks = tq.list();
			// 需要限定任务查询范围为用户可以处理的任务

			// 查询已分配任务
			tq = taskService.createTaskQuery();
			tq.executionId(execid);
			tq.assignee(SecureUtil.getCurrentUser().getUsercode());
			List<Task> tasks = tq.list();

			if (candidateTasks.size() == 0 && tasks.size() == 0) {
				// 没有取到有效的用户任务！
				return new ResultMsg(false, "WFERR-0401", new String[] {});
			}

			if (candidateTasks.size() + tasks.size() > 1) {
				// 发现任务重复异常
				return new ResultMsg(false, "WFERR-0402", new String[] {});

			}
			if (candidateTasks.size() > 0) {

				// 接受候选任务并推送
				taskService.takeTask(candidateTasks.get(0).getId(), SecureUtil
						.getCurrentUser().getUsercode());
				Set<String> outcomes = taskService.getOutcomes(candidateTasks
						.get(0).getId());
				String outcome = outcomes.iterator().next();
				taskService
						.completeTask(candidateTasks.get(0).getId(), outcome);
			} else {

				// 正常推送
				Set<String> outcomes = taskService.getOutcomes(tasks.get(0)
						.getId());
				String outcome = outcomes.iterator().next();
				taskService.completeTask(tasks.get(0).getId(), outcome);
			}
			ResultMsg msg = new ResultMsg(true, "工作流启动成功！");
			HashMap map = new HashMap();
			map.put("EXECID", processInstance.getId());
			// 放入工作流的当前节点
			map.put("ACTIID", getCurrentActivityName(processInstance.getId()));
			msg.setBody(map);

			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, "WFERR-0301", new String[] {});
		}
	}

	// @Override
	// public void completeTask(String taskid, String outcome) throws Exception
	// {
	// completeTask(taskid, outcome, null);
	// }
	//
	// @Override
	// public void completeTask(String taskid, String outcome, Map variables)
	// throws Exception {
	// TaskService ts = processEngine.getTaskService();
	// outcome = java.net.URLDecoder.decode(outcome, "UTF-8");
	// outcome = outcome.trim();
	//
	// if (outcome.indexOf("退回至") == 0) {
	// // 设置流程回退标志为“退回”,用于在EventListener业务处理参考
	// // EventListener业务处理时，应该清除这个标志
	// variables.put(WfEventListener.WF_BACK_FLAG, "RETURN");
	// }
	// ts.completeTask(taskid, outcome, variables);
	// }

	@Override
	public ResultMsg completeTask(String execId, String actiId, Map variables,
			String assignee, String opinion) {
		return completeTask(execId, actiId, null, variables, assignee, opinion);
	}

	@Override
	public ResultMsg completeTask(String execId, String actiId, String outcome,
			Map variables, String assignee, String opinion) {

		return completeTaskByType(execId, actiId, outcome, variables, assignee,
				opinion, "NORMAL");
	}

	private ResultMsg completeTaskByType(String execId, String actiId,
			String outcome, Map variables, String assignee, String opinion,
			String type) {

		if (null == assignee) {
			assignee = SecureUtil.getCurrentUser().getUsercode();
		}
		Set<String> outcomes = null;

		if ("NORMAL".equals(type)) {
			outcomes = getOutcomes(execId, actiId, assignee, "NORMAL");
		} else {
			outcomes = getOutcomes(execId, actiId, assignee, "RETURN");
		}

		// 当没有指定流出路径时
		if (null == outcome || 0 == outcome.trim().length()) {
			// 取得可选 的流出路径
			switch (outcomes.size()) {
			case 0:
				// 没有有效的流出路径
				return new ResultMsg(false, "WFERR-0404", new String[] {});

			case 1:
				outcome = outcomes.iterator().next();
				break;
			default:
				String cfg = this.getGlobalConfig("WFCFG-RANDOM_OUTCOME_PATH");
				if ("ON".equalsIgnoreCase(cfg)) {
					// 如果开启了随机路径
					outcome = outcomes.iterator().next();
				} else {
					// 如果关闭了随机路径，提示无效的路径，并返回有效路径
					ResultMsg rm = new ResultMsg(false, "WFERR-0405",
							new String[] {});
					StringBuilder sb = new StringBuilder();
					for (String s : outcomes) {
						sb.append(s).append(",");
					}
					// 放入有效的流出路径
					sb.deleteCharAt(sb.length() - 1);
					rm.getBody().put("OUTCOMES", sb.toString());
					return rm;
				}
				break;
			}

		} else if (!outcomes.contains(outcome)) {
			// 流出路径无效
			ResultMsg rm = new ResultMsg(false, "WFERR-0406", new String[] {});
			StringBuilder sb = new StringBuilder();
			for (String s : outcomes) {
				sb.append(s).append(",");
			}
			// 放入有效的流出路径
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			rm.getBody().put("OUTCOMES", sb.toString());
			return rm;
		}

		if (null == variables) {
			variables = new HashMap();
		}

		// 没有退回标志时，即重置流程标志为正常流转
		if (null == variables.get(WfEventListener.WF_BACK_FLAG)) {
			variables.put(WfEventListener.WF_BACK_FLAG, "NORMAL");
		}

		SysWorkflowOpinion opn = null;

		TaskService taskService = processEngine.getTaskService();
		// 首先查询候选任务
		TaskQuery tq = taskService.createTaskQuery();
		tq.executionId(execId);
		tq.activityName(actiId);
		tq.candidate(assignee);
		List<Task> candidateTasks = tq.list();
		// 需要限定任务查询范围为用户可以处理的任务

		// 查询已分配任务
		tq = taskService.createTaskQuery();
		tq.executionId(execId);
		tq.activityName(actiId);
		tq.assignee(assignee);
		List<Task> tasks = tq.list();

		if (candidateTasks.size() == 0 && tasks.size() == 0) {
			// 没有取到有效的用户任务！
			return new ResultMsg(false, "WFERR-0401", new String[] {});
		}

		if (candidateTasks.size() + tasks.size() > 1) {
			// 发现任务重复异常
			return new ResultMsg(false, "WFERR-0402", new String[] {});
		}

		if (candidateTasks.size() > 0) {
			TaskImpl ti = (TaskImpl) candidateTasks.get(0);
			if (ti.isSuspended()) {
				// 流程已被被挂起，操作失败
				return new ResultMsg(false, "WFERR-0701", new String[] {});
			}
			// 创建意见
			opn = createOpinion(Long.parseLong(candidateTasks.get(0).getId()),
					execId, candidateTasks.get(0).getActivityName(), outcome,
					assignee, opinion);

			// opn.setSrcacti(candidateTasks.get(0).getActivityName());
			// opn.setHtask(Long.parseLong(candidateTasks.get(0).getId()));

			// 接受候选任务并推送
			taskService.takeTask(candidateTasks.get(0).getId(), SecureUtil
					.getCurrentUser().getUsercode());
			taskService.completeTask(candidateTasks.get(0).getId(), outcome,
					variables);
		} else {

			TaskImpl ti = (TaskImpl) tasks.get(0);
			if (ti.isSuspended()) {
				// 流程已被被挂起，操作失败
				return new ResultMsg(false, "WFERR-0701", new String[] {});
			}

			// 创建意见
			opn = createOpinion(Long.parseLong(tasks.get(0).getId()), execId,
					tasks.get(0).getActivityName(), outcome, assignee, opinion);

			// opn.setSrcacti(tasks.get(0).getActivityName());
			// opn.setHtask(Long.parseLong(tasks.get(0).getId()));

			// 正常推送
			taskService.completeTask(tasks.get(0).getId(), outcome, variables);
		}

		// 保存审核意见
		sysWorkflowOpinionDao.saveOrUpdate(opn);

		ResultMsg msg = new ResultMsg(true, "流程处理成功！");

		ExecutionService es = processEngine.getExecutionService();

		List<ProcessInstance> pis = es.createProcessInstanceQuery()
				.processInstanceId(execId).list();

		// 查看流程是否已结束
		HistoryProcessInstanceQuery htq = processEngine.getHistoryService()
				.createHistoryProcessInstanceQuery();
		htq.processInstanceId(execId);
		HistoryProcessInstance hpi = htq.uniqueResult();

		if (hpi != null) {
			String state = hpi.getState();
			if ("active".equals(state)) {
				// 流程未结束，要取得当前的结点
				if (msg.getBody() == null) {
					msg.setBody(new HashMap());
				}
				msg.getBody()
						.put("ACTIID", this.getCurrentActivityName(execId));
				msg.getBody().put("FINISHED", false);
			} else if ("ended".equals(state)) {
				// 流程已结束
				if (msg.getBody() == null) {
					msg.setBody(new HashMap());
				}
				msg.getBody().put("FINISHED", true);
			} else {
				msg = new ResultMsg(false, "WFERR-0408", new String[] { state });
			}
		} else {
			// 历史没取到
			msg = new ResultMsg(false, "WFERR-0407", new String[] {});
		}
		return msg;
	}

	/**
	 * 创建意见对象
	 * 
	 * @param taskId
	 *            历史任务ID
	 * @param execId
	 *            流程id
	 * @param srcActi
	 * @param transition
	 * @param author
	 * @param opinion
	 * @return SysWorkflowOpinion对象
	 */
	private SysWorkflowOpinion createOpinion(Long taskId, String execId,
			String srcActi, String transition, String author, String opinion) {

		SysWorkflowOpinion op = null;
		List<SysWorkflowOpinion> ops = (List<SysWorkflowOpinion>) sysWorkflowOpinionDao
				.find(" from Sys"
						+ "WorkflowOpinion where execid = ? and htask = ?",
						execId, taskId);

		if (ops.size() > 0) {
			op = ops.get(0);
		} else {
			op = new SysWorkflowOpinion();
			op.setExecid(execId);
			op.setHtask(taskId);
		}

		op.setSrcacti(srcActi);
		op.setTransation(transition);
		op.setAuthor(author);
		op.setOpinion(opinion);
		op.setCrdate(DateUtil.getCurrentDate("yyyy-MM-dd hh:mm:ss"));

		return op;
	}

	/**
	 * 将字符串格式的环境变量转成HashMap
	 * 
	 * @param varStr
	 *            格式例如 "varA:valueA;varB:valueB"
	 * @return HashMap格式的环境变量
	 */
	public HashMap parseVariables(String varStr) {
		HashMap var = new HashMap();
		if (varStr != null && varStr.length() > 0) {
			if (varStr != null && varStr.trim().length() > 0) {
				String[] pairs = varStr.split(";");
				for (String s : pairs) {
					String[] pair = s.split(":");
					if (pair.length == 2) {
						var.put(pair[0], pair[1]);
					}
				}
			}
		}
		return var;
	}

	@Override
	@Deprecated
	public List getUserTasks() {

		RepositoryService rs = processEngine.getRepositoryService();
		ProcessDefinitionQuery pdq = rs.createProcessDefinitionQuery();
		List<ProcessDefinition> pdlist = pdq.list();
		HashMap<String, String> nameMap = new HashMap<String, String>();
		for (ProcessDefinition pd : pdlist) {
			nameMap.put(pd.getKey(), pd.getName());
		}
		TaskService taskService = processEngine.getTaskService();
		SysUser user = SecureUtil.getCurrentUser();
		List<Task> tasks = taskService.findPersonalTasks(user.getUsercode());
		List<HashMap> result = new ArrayList<HashMap>();
		for (Task task : tasks) {
			HashMap map = new HashMap();
			map.put("id", task.getId());
			map.put("name", task.getName());
			map.put("activityName", task.getActivityName());
			map.put("assignee", task.getAssignee());
			map.put("execid", task.getExecutionId());
			int index = task.getExecutionId().indexOf(".");
			map.put("processName",
					nameMap.get(task.getExecutionId().substring(0, index)));
			map.put("outcomes", taskService.getOutcomes(task.getId())
					.toString());
			map.put("formid", taskService.getVariable(task.getId(), "formid"));
			result.add(map);
		}
		return result;
	}

	// @Override
	// @Deprecated
	// public List<UserTodoListVo> getUserTodoListByWfKey(String usercode,
	// String wfkey, String activityId, String businessPKVarName) {
	// if (usercode == null || usercode.trim().length() == 0) {
	// usercode = SecureUtil.getCurrentUser().getUsercode();
	// }
	//
	// StringBuilder sql = new StringBuilder();
	// sql.append(" select e.id_ wfid, v.string_value_ objid");
	// sql.append(" from jbpm4_execution e");
	// sql.append(" left join jbpm4_variable v");
	// sql.append(" on e.dbid_ = v.execution_");
	// sql.append(" and v.key_ = '" + businessPKVarName + "'");
	// sql.append(" where e.id_ in (select execution_id_");
	// sql.append(" from jbpm4_task t");
	// sql.append(" where t.name_ = '" + activityId + "'");
	// sql.append(" and assignee_ = '" + usercode + "'");
	// sql.append(" and t.execution_id_ like '" + wfkey + ".%')");
	// sql.append(" order by e.dbid_ desc");
	//
	// List<UserTodoListVo> wfidList = (List<UserTodoListVo>)
	// sysWorkflowProcversionDao
	// .findVoBySql(sql.toString(), UserTodoListVo.class);
	// return wfidList;
	// }

	@Override
	public List<UserTodoListVo> getUserTodoListByWfKey(String usercode,
			String wfkey, String activityId, Set<String> backFlags) {

		StringBuilder sql = new StringBuilder();
		sql.append(" select *");
		sql.append(" from (select wfid,");
		sql.append(" (select string_value_");
		sql.append(" from jbpm4_variable v");
		sql.append(" where v.execution_ = tt.execution_");
		sql.append(" and v.key_ = 'WF_BACK_FLAG') backflag,");
		sql.append(" iscandidate,");
		sql.append(" dbid_");
		sql.append(" from (select execution_id_ wfid,");
		sql.append(" t.dbid_,");
		sql.append(" t.execution_,");
		sql.append(" 0          iscandidate");
		sql.append(" from jbpm4_task t");
		sql.append(" where t.name_ = '" + activityId + "'");
		sql.append(" and assignee_ = '" + usercode + "'");
		sql.append(" and t.execution_id_ like '" + wfkey + ".%'");
		sql.append(" union all");
		sql.append(" select t.execution_id_ wfid,");
		sql.append(" t.dbid_,");
		sql.append(" t.execution_,");
		sql.append(" 1              iscandidate");
		sql.append(" from jbpm4_task t, jbpm4_participation p");
		sql.append(" where t.dbid_ = p.task_");
		sql.append(" and p.type_ = 'candidate'");
		sql.append(" and p.userid_ = '" + usercode + "'");
		sql.append(" and t.name_ = '" + activityId + "'");
		sql.append(" and t.assignee_ is null");
		sql.append(" and t.execution_id_ like '" + wfkey + ".%') tt)");
		if (backFlags != null && backFlags.size() > 0) {
			// 加入退回标志查询条件
			sql.append(" where backflag in (");

			for (String str : backFlags) {
				sql.append("'").append(str).append("',");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
		}

		sql.append(" order by dbid_ desc");

		List<UserTodoListVo> wfidList = (List<UserTodoListVo>) sysWorkflowProcversionDao
				.findVoBySql(sql.toString(), UserTodoListVo.class);

		return wfidList;
	}

	@Override
	public String getUserHistoryExecidsByWfKey(String usercode, String key,
			String activityId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select execution_ wfid from(");
		sql.append(" select t.dbid_,t.execution_,");
		sql.append(" (select inst.activity_name_");
		sql.append(" from jbpm4_hist_actinst inst");
		sql.append(" where inst.htask_ = t.dbid_");
		sql.append(" and inst.type_ = 'task') acti");
		sql.append(" from jbpm4_hist_task t");
		sql.append(" where t.assignee_ = '" + usercode + "'");
		sql.append(" and t.execution_ like '" + key + ".%'");
		sql.append(" and state_ = 'completed'");
		sql.append(" )");
		sql.append(" where acti='" + activityId + "'");
		sql.append(" order by dbid_ desc");

		List<UserTodoListVo> wfidList = (List<UserTodoListVo>) sysWorkflowProcversionDao
				.findVoBySql(sql.toString(), UserTodoListVo.class);

		StringBuilder sb = new StringBuilder();
		for (UserTodoListVo v : wfidList) {
			sb.append(v.getWfid()).append(",");
		}

		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	@Deprecated
	@Override
	public List getUserHistoryTasks(String key) {

		SysUser user = SecureUtil.getCurrentUser();
		// 1取得当正在进行且key符合的流程
		ExecutionService es = processEngine.getExecutionService();
		HistoryService hs = processEngine.getHistoryService();

		ProcessInstanceQuery piq = es.createProcessInstanceQuery();

		// String tkey = key + ".";
		// List<HashMap> list = new ArrayList<HashMap>();
		List<String> execids = new ArrayList<String>(); // 符合的流程列表
		List<ProcessInstance> instances = piq.list();
		for (ProcessInstance pi : instances) {

			String execid = pi.getId();

			HistoryTaskQuery htq = hs.createHistoryTaskQuery();
			htq.executionId(execid);
			htq.orderDesc(HistoryTaskQuery.PROPERTY_ENDTIME);
			List<HistoryTask> htasks = htq.list();
			for (HistoryTask task : htasks) {
				if (task.getEndTime() != null) {
					if (user.getUsercode().equals(task.getAssignee())) {
						// HashMap map = new HashMap();
						// map.put("executionId", task.getExecutionId());
						// list.add(map);
						execids.add(task.getExecutionId());
					}
					break;
				}
			}

		}
		HashMap<String, String> activityNames = new HashMap<String, String>();
		HashMap<String, Date> endTimes = new HashMap<String, Date>();
		HashMap<String, String> outcome = new HashMap<String, String>();
		for (String execid : execids) {
			HistoryActivityInstanceQuery haiq = hs
					.createHistoryActivityInstanceQuery();
			haiq.executionId(execid);
			haiq.orderDesc(HistoryActivityInstanceQuery.PROPERTY_ENDTIME);

			List<HistoryActivityInstance> ins = haiq.list();
			for (HistoryActivityInstance hai : ins) {
				HistoryActivityInstanceImpl impl = (HistoryActivityInstanceImpl) hai;
				if ("task".equals(impl.getType())) {
					if (impl.getEndTime() != null) {
						activityNames.put(execid, impl.getActivityName());
						endTimes.put(execid, impl.getEndTime());
						outcome.put(execid, impl.getTransitionName());
						break;
					}
				}
			}
		}

		List<HistoryTaskVO> vos = new ArrayList<HistoryTaskVO>();
		for (String s : execids) {
			HistoryTaskVO vo = new HistoryTaskVO();
			vo.setExecid(s);
			vo.setActivityName(activityNames.get(s));
			vo.setEndTime(DateUtil.dateToString(endTimes.get(s),
					DateUtil.DATE_TIME_FORMAT));
			vo.setOutcome(outcome.get(s));
			vos.add(vo);
		}

		Collections.sort(vos, new Comparator<HistoryTaskVO>() {
			@Override
			public int compare(HistoryTaskVO o1, HistoryTaskVO o2) {
				return o2.getEndTime().compareTo(o1.getEndTime());
			}
		});

		return vos;
	}

	/**
	 * 撤回流程
	 */
	@Override
	public ResultMsg getBackWorkflow(String execId, String actiId,
			String usercode, Map variables) throws Exception {
		
		ExecutionService executionService = processEngine.getExecutionService();
		Execution es = executionService.findExecutionById(execId);
		if (es ==null) {
			return new ResultMsg(false, "WFERR-0107", new String[] {});
		}
		ResultMsg msg = this.isSuspended(execId);
		if (!msg.isSuccess()) {
			return msg;
		}
		Boolean bl = (Boolean) msg.getBody().get("isSuspended");
		if (bl) {
			// 流程已挂起 不可撤回
			return new ResultMsg(false, "WFERR-0703", new String[] {});
		}

		WfSpecialProcessCmd command = new WfSpecialProcessCmd(execId, actiId,
				usercode, variables);

		msg = processEngine.execute(command);
		if (msg.isSuccess()) {
			// 如果成功返回当前的工作流节点
			if (msg.getBody() == null) {
				msg.setBody(new HashMap());
			}
			msg.getBody().put("ACTIID", this.getCurrentActivityName(execId));
		}
		return msg;
	}

	@Override
	public List getUserCandidateTask() {

		RepositoryService rs = processEngine.getRepositoryService();
		ProcessDefinitionQuery pdq = rs.createProcessDefinitionQuery();
		List<ProcessDefinition> pdlist = pdq.list();
		HashMap<String, String> nameMap = new HashMap<String, String>();
		for (ProcessDefinition pd : pdlist) {
			nameMap.put(pd.getKey(), pd.getName());
		}

		TaskService ts = processEngine.getTaskService();

		SysUser u = SecureUtil.getCurrentUser();
		List<Task> tasks = ts.findGroupTasks(u.getUsercode());
		List result = new ArrayList();
		for (Task task : tasks) {
			HashMap map = new HashMap();
			map.put("id", task.getId());
			map.put("name", task.getName());
			map.put("activityName", task.getActivityName());
			map.put("assignee", task.getAssignee());
			map.put("execid", task.getExecutionId());
			map.put("outcomes", ts.getOutcomes(task.getId()).toString());
			int index = task.getExecutionId().indexOf(".");
			map.put("processName",
					nameMap.get(task.getExecutionId().substring(0, index)));
			result.add(map);
		}
		return result;
	}

	@Override
	public void takeTask(String taskid) throws Exception {

		TaskService ts = processEngine.getTaskService();
		SysUser u = SecureUtil.getCurrentUser();
		ts.takeTask(taskid, u.getUsercode());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<? extends Map> queryWorkflowInstances(String key) {
		ExecutionService es = processEngine.getExecutionService();
		ProcessInstanceQuery piq = es.createProcessInstanceQuery();

		List<ProcessInstance> p = piq.list();

		List list = new ArrayList();
		for (ProcessInstance o : p) {
			if (o.getId().indexOf(key + ".") == 0) {
				HashMap map = new HashMap();
				map.put("id", o.getId());
				Collection c = o.getExecutions();

				map.put("name", "");
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public String getExcutionName(Execution e) {
		ExecutionImpl impl = (ExecutionImpl) e;
		return impl.getActivityName();
	}

	@Override
	public Boolean isWorkflowTaskFormEditable(String execId, String actiId)
			throws Exception {

		ProcessDefinition pd = getProcessDefinition(execId);
		if (pd == null) {
			// 无法查询到流程定义
			throw new Exception(this.getExceptionMessage("WFERR-0102",
					new String[] { execId }));

		}
		return isWorkflowTaskFormEditable(pd.getKey(), pd.getVersion(), actiId);

	}

	@Override
	public Boolean isWorkflowTaskFormEditable(String key, Integer version,
			String actiId) throws Exception {

		String hql = " from SysWorkflowExtAttr where key =  ?  and version =? and category = 'TASK_FORM_EDITABLE' and srcacti = ? ";

		List<SysWorkflowExtAttr> list = (List<SysWorkflowExtAttr>) sysWorkflowExtAttrDao
				.find(hql, key, version, actiId);

		if (list.size() > 0) {
			String result = list.get(0).getAttrvalue1();
			if ("true".equals(result)) {
				return true;
			} else {
				return false;
			}
		} else {
			// 无法找到流程的表单可编辑配置
			throw new Exception(this.getExceptionMessage("WFERR-0103",
					new String[] {}));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getOutcomes(String execId, String actiId,
			String assignee, String outcomeType) {

		if (null == outcomeType) {
			outcomeType = "NORMAL";
		}

		TaskService taskService = processEngine.getTaskService();
		// 首先查询修行候选任务
		TaskQuery tq = taskService.createTaskQuery();
		tq.executionId(execId);
		tq.candidate(assignee);
		List<Task> candidateTasks = tq.list();
		// 需要限定任务查询范围为用户可以处理的任务

		// 查询已分配任务
		tq = taskService.createTaskQuery();
		tq.executionId(execId);
		tq.assignee(assignee);
		List<Task> tasks = tq.list();

		if (candidateTasks.size() == 0 && tasks.size() == 0) {
			System.out.println("取得用户任务时得到空的内容！");
			return new HashSet<String>();
		}

		if (candidateTasks.size() + tasks.size() > 1) {
			System.out.println("待办任务重复异常！");
			return new HashSet<String>();
		}
		Task task = null;
		if (candidateTasks.size() > 0) {
			task = candidateTasks.get(0);
		} else {
			task = tasks.get(0);
		}
		// 所有的路径
		Set<String> outcomes = taskService.getOutcomes(task.getId());

		if ("ALL".equals(outcomeType)) {
			return outcomes;
		}
		// 已注册的退回路径
		ExecutionService es = processEngine.getExecutionService();
		ProcessInstance pi = es.createProcessInstanceQuery()
				.processInstanceId(execId).uniqueResult();
		if (null == pi) {
			System.out.println("取得流程实例时发生异常，流程实例ID为：" + execId);
			return new HashSet<String>();
		}

		RepositoryService rs = processEngine.getRepositoryService();
		ProcessDefinition dm = rs.createProcessDefinitionQuery()
				.processDefinitionId(pi.getProcessDefinitionId())
				.uniqueResult();
		if (null == dm) {
			System.out.println("取得流程定义时发生异常，流程实例ID为：" + execId);
			return new HashSet<String>();
		}
		List<SysWorkflowBacklineRec> lines = (List<SysWorkflowBacklineRec>) sysWorkflowBacklineRecDao
				.find(" from SysWorkflowBacklineRec  where  key = ? and version = ? and srcacti = ? ",
						dm.getKey(), dm.getVersion(), actiId);
		//

		if ("NORMAL".equals(outcomeType)) {
			// 剔除在SysWorkflowBacklineRec中注册的路径
			for (SysWorkflowBacklineRec line : lines) {
				if (outcomes.contains(line.getTransname())) {
					outcomes.remove(line.getTransname());
				}
			}

			return outcomes;
		} else if ("RETURN".equals(outcomeType)) {
			// 退回上节点的路径
			// 与在SysWorkflowBacklineRec中注册的路径求交集
			Set<String> result = new HashSet<String>();
			for (SysWorkflowBacklineRec line : lines) {
				if (outcomes.contains(line.getTransname())) {
					result.add(line.getTransname());
				}
			}
			return result;
		} else if ("RETURN_FIRST".equals(outcomeType)) {
			// 退回首节点的路径
			Set<String> result = new HashSet<String>();
			for (SysWorkflowBacklineRec line : lines) {
				if (outcomes.contains(line.getTransname())
						&& "3".equals(line.getType())) {
					result.add(line.getTransname());
				}
			}
			return result;
		} else {
			System.out.println("非法的流出路径类型参数：" + outcomeType + "！");
			return new HashSet<String>();
		}

	}

	@Override
	public List<SysWorkflowOpinion> getOpinions(String execId, String dateOrder) {

		return getOpinions(execId, null, null, dateOrder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SysWorkflowOpinion> getOpinions(String execId, String srcacti,
			String transition, String dateOrder) {

		if (dateOrder == null) {
			dateOrder = "desc";
		} else {
			dateOrder = dateOrder.toLowerCase();
		}

		StringBuilder builder = new StringBuilder(
				" from SysWorkflowOpinion where execid = '" + execId + "'");

		if (null != srcacti) {
			builder.append(" and srcacti =  '" + srcacti + "'");

		}
		if (null != transition) {
			builder.append(" and transition =  " + transition + "'");

		}
		builder.append(" order by crdate ").append(dateOrder);
		return (List<SysWorkflowOpinion>) sysWorkflowOpinionDao.find(builder
				.toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public SysWorkflowOpinion getLatestOpinion(String execId) {

		String hql = " from SysWorkflowOpinion where execid = ?  order by crdate desc";
		List<SysWorkflowOpinion> opinions = (List<SysWorkflowOpinion>) sysWorkflowOpinionDao
				.find(hql, execId);

		if (opinions.size() > 0) {
			return opinions.get(0);
		} else
			return null;
	}

	@Override
	public ResultMsg isWorkflowWithdrawable(String execId, String actiId,
			String assignee) {
		// // 查询流程当前节点
		//
		// TaskService ts = processEngine.getTaskService();
		//
		// // 查询历史记录，找到上一处理节点id是否是actiid且处理人是assignee
		// HistoryService hs = processEngine.getHistoryService();
		// HistoryTaskQuery htq = hs.createHistoryTaskQuery();
		// htq.executionId(execid);
		// htq.orderDesc(HistoryTaskQuery.PROPERTY_ENDTIME);

		ResultMsg msg = this.isSuspended(execId);
		if (!msg.isSuccess()) {
			return msg;
		}
		Boolean bl = (Boolean) msg.getBody().get("isSuspended");
		if (bl) {
			// 流程已挂起 不可撤回
			return new ResultMsg(false, "WFERR-0703", new String[] {});
		}

		if (actiId == null || actiId.trim().length() == 0) {
			// 节点参数据异常
			return new ResultMsg(false, "WFERR-0104", new String[] {});
		}

		StringBuilder sql = new StringBuilder();
		sql.append(" select t.execution_     execid,");
		sql.append(" a.class_         actitype,");
		sql.append(" a.activity_name_ actiname,");
		sql.append(" t.assignee_      assignee,");
		sql.append(" to_char(t.create_,'yyyy-MM-dd hh:mi:ss')        createtime,");
		sql.append(" to_char(t.end_,'yyyy-MM-dd hh:mi:ss')        endtime");
		sql.append(" from jbpm4_hist_actinst a");
		sql.append(" left join jbpm4_hist_task t");
		sql.append(" on a.htask_ = t.dbid_");
		sql.append(" where t.execution_ = '" + execId + "'");
		sql.append(" order by t.create_ desc");

		List<HistoryActivityVO> list = (List<HistoryActivityVO>) sysWorkflowProcversionDao
				.findVoBySql(sql.toString(), HistoryActivityVO.class);

		if (list.size() < 2) {
			// 历史任务中没有发现可撤回的任务处理记录！
			return new ResultMsg(false, "WFERR-0602", new String[] {});
		}

		HistoryActivityVO lastActi = list.get(0);
		if (null != lastActi.getEndtime()) {
			// 历史任务中没有发现可撤回的任务处理记录！
			return new ResultMsg(false, "WFERR-0602", new String[] {});
		}
		if ("task".equals(lastActi.getActitype())
				|| "state".equals(lastActi.getActitype())) {
			for (int i = 1; i < list.size(); i++) {
				HistoryActivityVO v = list.get(i);
				if ("decision".equals(v.getActitype())) {
					continue;
				} else if ("task".equals(v.getActitype())
						&& assignee.equals(v.getAssignee())
						&& actiId.equals(v.getActiname())) {
					return new ResultMsg(true, "当前节点可撤回！");

				} else {
					// 流程当前所处节点超出可撤回的范围，不可撤回！
					return new ResultMsg(false, "WFERR-0603", new String[] {});
				}
			}
			// 历史任务中没有发现可撤回的任务处理记录！
			return new ResultMsg(false, "WFERR-0602", new String[] {});
		} else {
			// 流程当前节点类型 不可撤回！
			return new ResultMsg(false, "WFERR-0604",
					new String[] { lastActi.getActitype() });

		}

	}

	@Override
	public String getCurrentActivityName(String execId) {
		ExecutionService es = processEngine.getExecutionService();
		ProcessInstanceQuery piq = es.createProcessInstanceQuery();
		piq.processInstanceId(execId);
		ProcessInstance pi = piq.uniqueResult();
		ExecutionImpl impl = (ExecutionImpl) pi;
		return impl.getActivityName();

	}

	@Override
	public ResultMsg isWorkflowReturnable(String execId, String actiId,
			String assignee) {
		if (assignee == null) {
			assignee = SecureUtil.getCurrentUser().getUsercode();
		}

		// 查询流程定义确定是否可退回
		ExecutionService es = processEngine.getExecutionService();
		ProcessInstance pi = es.createProcessInstanceQuery()
				.processInstanceId(execId).uniqueResult();
		if (null == pi) {

			// 取得流程实例时发生异常
			return new ResultMsg(false, "WFERR-0101", new String[] { execId });
		}

		RepositoryService rs = processEngine.getRepositoryService();
		ProcessDefinition dm = rs.createProcessDefinitionQuery()
				.processDefinitionId(pi.getProcessDefinitionId())
				.uniqueResult();
		if (null == dm) {
			// 取得流程实例的版本定义时发生异常
			return new ResultMsg(false, "WFERR-0102", new String[] { execId });
		}
		String key = dm.getKey();
		int version = dm.getVersion();

		// 判断流程是否已挂起
		ResultMsg msg2 = this.isSuspended(execId);
		if (!msg2.isSuccess()) {
			return msg2;
		}
		Boolean bl = (Boolean) msg2.getBody().get("isSuspended");
		if (bl) {
			// 流程已挂起 不可退回
			return new ResultMsg(false, "WFERR-0702", new String[] {});
		}

		List<SysWorkflowExtAttr> backAttrs = (List<SysWorkflowExtAttr>) sysWorkflowExtAttrDao
				.find(" from SysWorkflowExtAttr where key = ? and version = ? and  category= 'TASK_BACKABLE' and srcacti= ?",
						key, version, actiId);
		if (backAttrs.size() == 0) {
			// 流程的退回属性未定义，请检查流程图定义！
			return new ResultMsg(false, "WFERR-0501", new String[] {});
		}
		SysWorkflowExtAttr attr = backAttrs.get(0);
		String backFlag = attr.getAttrvalue1();
		if ("false".equals(backFlag)) {
			// 根据流程节的退回属性定义，该任务不可退回！
			return new ResultMsg(false, "WFERR-0502", new String[] {});
		}

		// 分析流程图上可退回的节点
		Set<String> taskSet = new HashSet<String>();
		ProcessDefinitionImpl pdi = (ProcessDefinitionImpl) dm;
		Map actis = pdi.getActivitiesMap();
		analyzePreviousTaskNode(actis, actiId, taskSet);

		// 确定退回路径
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.execution_     execid,");
		sql.append(" a.class_         actitype,");
		sql.append(" a.activity_name_ actiname,");
		sql.append(" t.assignee_      assignee,");
		sql.append(" to_char(t.create_,'yyyy-MM-dd hh:mi:ss')        createtime,");
		sql.append(" to_char(t.end_,'yyyy-MM-dd hh:mi:ss')        endtime");
		sql.append(" from jbpm4_hist_actinst a");
		sql.append(" left join jbpm4_hist_task t");
		sql.append(" on a.htask_ = t.dbid_");
		sql.append(" where t.execution_ = '" + execId + "'");
		sql.append(" order by t.create_ desc");

		List<HistoryActivityVO> list = (List<HistoryActivityVO>) sysWorkflowProcversionDao
				.findVoBySql(sql.toString(), HistoryActivityVO.class);

		if (list.size() < 2) {
			// 历史任务中没有发现可退回的任务处理记录！
			return new ResultMsg(false, "WFERR-0503", new String[] {});
		}

		HistoryActivityVO lastActi = list.get(0);
		if (null != lastActi.getEndtime()) {
			// 历史任务中没有发现可退回的任务处理记录！
			return new ResultMsg(false, "WFERR-0503", new String[] {});
		}
		if ("task".equals(lastActi.getActitype())) {
			for (int i = 1; i < list.size(); i++) {
				HistoryActivityVO v = list.get(i);

				if (taskSet.contains(v.getActiname())) {
					// 可退回
					return new ResultMsg(true, "流程可退回！");
				}
			}
			// 历史任务中没有发现可退回的任务处理记录
			return new ResultMsg(false, "WFERR-0503", new String[] {});
		} else {
			// 流程当前节点类型 不可人工退回
			return new ResultMsg(false, "WFERR-0509",
					new String[] { lastActi.getActitype() });
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultMsg sendBackWorkflow(String execId, String actiId,
			Map variables, String assignee, String opinion) {

		if (assignee == null) {
			assignee = SecureUtil.getCurrentUser().getUsercode();
		}

		// 查询流程定义确定是否可退回
		ExecutionService es = processEngine.getExecutionService();
		ProcessInstance pi = es.createProcessInstanceQuery()
				.processInstanceId(execId).uniqueResult();
		if (null == pi) {

			// 取得流程实例时发生异常
			return new ResultMsg(false, "WFERR-0101", new String[] { execId });
		}

		RepositoryService rs = processEngine.getRepositoryService();
		ProcessDefinition dm = rs.createProcessDefinitionQuery()
				.processDefinitionId(pi.getProcessDefinitionId())
				.uniqueResult();
		if (null == dm) {
			// 取得流程实例的版本定义时发生异常
			return new ResultMsg(false, "WFERR-0102", new String[] { execId });
		}
		String key = dm.getKey();
		int version = dm.getVersion();

		// 判断流程是否已挂起
		ResultMsg msg2 = this.isSuspended(execId);
		if (!msg2.isSuccess()) {
			return msg2;
		}
		Boolean bl = (Boolean) msg2.getBody().get("isSuspended");
		if (bl) {
			// 流程已挂起 不可退回
			return new ResultMsg(false, "WFERR-0702", new String[] {});
		}

		List<SysWorkflowExtAttr> backAttrs = (List<SysWorkflowExtAttr>) sysWorkflowExtAttrDao
				.find(" from SysWorkflowExtAttr where key = ? and version = ? and  category= 'TASK_BACKABLE' and srcacti= ?",
						key, version, actiId);
		if (backAttrs.size() == 0) {
			// 流程的退回属性未定义，请检查流程图定义！
			return new ResultMsg(false, "WFERR-0501", new String[] {});
		}
		SysWorkflowExtAttr attr = backAttrs.get(0);
		String backFlag = attr.getAttrvalue1();
		if ("false".equals(backFlag)) {
			// 根据流程节的退回属性定义，该任务不可退回！
			return new ResultMsg(false, "WFERR-0502", new String[] {});
		}

		// 分析流程图上可退回的节点
		Set<String> taskSet = new HashSet<String>();
		ProcessDefinitionImpl pdi = (ProcessDefinitionImpl) dm;
		Map actis = pdi.getActivitiesMap();
		analyzePreviousTaskNode(actis, actiId, taskSet);

		// 确定退回路径
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.execution_     execid,");
		sql.append(" a.class_         actitype,");
		sql.append(" a.activity_name_ actiname,");
		sql.append(" t.assignee_      assignee,");
		sql.append(" to_char(t.create_,'yyyy-MM-dd hh:mi:ss')        createtime,");
		sql.append(" to_char(t.end_,'yyyy-MM-dd hh:mi:ss')        endtime");
		sql.append(" from jbpm4_hist_actinst a");
		sql.append(" left join jbpm4_hist_task t");
		sql.append(" on a.htask_ = t.dbid_");
		sql.append(" where t.execution_ = '" + execId + "'");
		sql.append(" order by t.create_ desc");

		List<HistoryActivityVO> list = (List<HistoryActivityVO>) sysWorkflowProcversionDao
				.findVoBySql(sql.toString(), HistoryActivityVO.class);

		if (list.size() < 2) {
			// 历史任务中没有发现可退回的任务处理记录！
			return new ResultMsg(false, "WFERR-0503", new String[] {});
		}

		HistoryActivityVO lastActi = list.get(0);
		if (null != lastActi.getEndtime()) {
			// 历史任务中没有发现可退回的任务处理记录！
			return new ResultMsg(false, "WFERR-0503", new String[] {});
		}
		if ("task".equals(lastActi.getActitype())) {
			for (int i = 1; i < list.size(); i++) {
				HistoryActivityVO v = list.get(i);

				if (taskSet.contains(v.getActiname())) {
					// 执行退回
					String oldAssign = v.getAssignee();
					String tgtActi = v.getActiname();
					// 退回路径确定为 actiId -> tgtActi

					// 取得退回路径名称

					String backTranstionName = getReturnTransitionName(key,
							version, actiId, tgtActi);

					if (null == backTranstionName) {
						// 取得退回路径时发生异常！
						return new ResultMsg(false, "WFERR-0504",
								new String[] {});
					}
					if (variables == null) {
						variables = new HashMap();
					}
					variables.put(WfEventListener.WF_BACK_FLAG, "RETURN");
					ResultMsg msg = completeTaskByType(execId, actiId,
							backTranstionName, variables, assignee, opinion,
							"RETURN");
					if (!msg.isSuccess()) {
						return msg;
					}
					// 流程已推回
					// 校验是否已到达目标活动节点
					String curActiName = this.getCurrentActivityName(execId);
					if (!tgtActi.equals(curActiName)) {
						// 退回操作未能到达目标节点，退回操作发生异常！
						return new ResultMsg(false, "WFERR-0505",
								new String[] {});
					}
					// 取得任务

					TaskService ts = processEngine.getTaskService();
					TaskQuery tq = ts.createTaskQuery();
					tq.activityName(tgtActi);
					tq.executionId(execId);
					List<Task> tks = tq.list();
					if (tks.size() == 0) {
						// 退回时发生异常，任务丢失
						return new ResultMsg(false, "WFERR-0506",
								new String[] {});
					}

					if (tks.size() > 1) {
						// 退回时发生异常，任务重复
						return new ResultMsg(false, "WFERR-0507",
								new String[] {});
					}

					Task t = tks.get(0);
					// 如果退回后，成为候选任务，则使用原处理人预占该任务
					if (null == t.getAssignee()) {
						ts.takeTask(t.getId(), oldAssign);
					}
					ResultMsg msg1 = new ResultMsg(true, "退回操作成功！");
					HashMap map = new HashMap();
					// 放入当前节点
					map.put("ACTIID", this.getCurrentActivityName(execId));
					msg1.setBody(map);
					return msg1;

				}
			}
			// 历史任务中没有发现可退回的任务处理记录
			return new ResultMsg(false, "WFERR-0503", new String[] {});
		} else {

			// 流程当前节点类型 不可人工退回
			return new ResultMsg(false, "WFERR-0509",
					new String[] { lastActi.getActitype() });

		}

	}

	/**
	 * 取得指定节点的合法前节点
	 * 
	 * @param list
	 * @param i
	 * @return
	 */

	/**
	 * 取得指定节点的合法前节点
	 * 
	 * @param list
	 *            流转历史
	 * @param i
	 *            指定节点
	 * @param pdi
	 *            流程定义
	 * @param recLines
	 *            流程定义的退回路径
	 * @return
	 */
	private String getValidPrevNode(List<HistoryActivityVO> list, int i,
			String tgtActiId, ProcessDefinitionImpl pdi,
			List<SysWorkflowBacklineRec> recLines) {
		if (i >= list.size())
			return null;
		// 前一节点
		HistoryActivityVO v = list.get(i);
		// 如果前一节点的类型为task
		if ("task".equals(v.getActitype()) || "state".equals(v.getActitype())) {
			ActivityImpl ai = pdi.getActivity(v.getActiname());
			List<Transition> trans = (List<Transition>) ai
					.getOutgoingTransitions();
			for (Transition t : trans) {
				if (tgtActiId.equals(t.getDestination().getName())) {
					if (isBackLine(t.getName(), ai.getName(), recLines)) {
						// 有路径，但是个退回路径，则该路径无需继续判断
						return null;
					}
					// 有流出路径且为前向流出路径，则该节点便是合法的前节点
					return ai.getName();
				}
			}
			// 断链的情况(说明可能是并行的其他路径的干扰节点)
			return getValidPrevNode(list, i + 1, tgtActiId, pdi, recLines);
		} else {
			// 如果前节点不是task，递归查找前节点的前节点

			ActivityImpl ai = pdi.getActivity(v.getActiname());
			List<Transition> trans = (List<Transition>) ai
					.getOutgoingTransitions();
			for (Transition t : trans) {
				if (tgtActiId.equals(t.getDestination().getName())) {
					// 有流出路径
					return getValidPrevNode(list, i + 1, ai.getName(), pdi,
							recLines);
				}
			}
			// 断链的情况(说明可能是并行的其他路径的干扰节点)
			return getValidPrevNode(list, i + 1, tgtActiId, pdi, recLines);
		}
	}

	/**
	 * 判断一条路径是不是退回路径
	 * 
	 * @param tranName
	 *            路径名
	 * @param actiId
	 *            源节点
	 * @param recLines
	 *            退回路径列表
	 * @return
	 */
	private boolean isBackLine(String tranName, String actiId,
			List<SysWorkflowBacklineRec> recLines) {
		for (SysWorkflowBacklineRec recLine : recLines) {
			if (actiId.equals(recLine.getSrcacti())
					&& tranName.equals(recLine.getTransname())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ResultMsg isFirstNodeBackable(String execId, String actiId,
			String assignee) {
		if (assignee == null) {
			assignee = SecureUtil.getCurrentUser().getUsercode();
		}

		// 查询流程定义确定是否可退回
		ExecutionService es = processEngine.getExecutionService();
		ProcessInstance pi = es.createProcessInstanceQuery()
				.processInstanceId(execId).uniqueResult();
		if (null == pi) {
			// 取得流程实例时发生异常
			return new ResultMsg(false, "WFERR-0101", new String[] { execId });
		}

		RepositoryService rs = processEngine.getRepositoryService();
		ProcessDefinition dm = rs.createProcessDefinitionQuery()
				.processDefinitionId(pi.getProcessDefinitionId())
				.uniqueResult();
		if (null == dm) {
			// 取得流程实例的版本定义时发生异常
			return new ResultMsg(false, "WFERR-0102", new String[] { execId });
		}
		String key = dm.getKey();
		int version = dm.getVersion();

		// 判断流程是否已挂起
		ResultMsg msg2 = this.isSuspended(execId);
		if (!msg2.isSuccess()) {
			return msg2;
		}
		Boolean bl = (Boolean) msg2.getBody().get("isSuspended");
		if (bl) {
			// 流程已挂起 不可退回
			return new ResultMsg(false, "WFERR-0702", new String[] {});
		}

		List<SysWorkflowExtAttr> backAttrs = (List<SysWorkflowExtAttr>) sysWorkflowExtAttrDao
				.find(" from SysWorkflowExtAttr where key = ? and version = ? and  category= 'TASK_BACKABLE' and srcacti= ?",
						key, version, actiId);
		if (backAttrs.size() == 0) {
			// 流程的退回属性未定义，请检查流程图定义！
			return new ResultMsg(false, "WFERR-0501", new String[] {});
		}
		SysWorkflowExtAttr attr = backAttrs.get(0);
		String backFlag = attr.getAttrvalue3();
		if ("false".equals(backFlag)) {
			// 根据流程节的退回属性定义，该任务不可退回首节点！
			return new ResultMsg(false, "WFERR-0510", new String[] {});
		}
		// 取得定义的首节点
		List<SysWorkflowProcversion> proclist = (List<SysWorkflowProcversion>) sysWorkflowProcversionDao
				.find(" from SysWorkflowProcversion where key = ? and version = ? ",
						key, version);
		if (proclist.size() == 0) {
			return new ResultMsg(false, "WFERR-0102", new String[] { execId });
		}

		// 取得首节点的历史处理人
		HistoryService hs = processEngine.getHistoryService();
		HistoryActivityInstanceQuery haiq = hs
				.createHistoryActivityInstanceQuery();
		haiq.executionId(execId);
		haiq.activityName(proclist.get(0).getFirstnode());
		haiq.orderDesc(HistoryActivityInstanceQuery.PROPERTY_ENDTIME);

		List<HistoryActivityInstance> htlist = haiq.list();
		if (htlist.size() == 0) {
			// 无法取得首节点的历史处理记录
			return new ResultMsg(false, "WFERR-0512", new String[] {});
		}
		HistoryTaskInstanceImpl hi = (HistoryTaskInstanceImpl) htlist.get(0);

		// 取得退回路径
		List<SysWorkflowBacklineRec> linelist = (List<SysWorkflowBacklineRec>) sysWorkflowBacklineRecDao
				.find(" from SysWorkflowBacklineRec where key = ? and version = ? and srcacti = ? and tgtacti = ? and type = '3' ",
						key, version, actiId, proclist.get(0).getFirstnode());

		if (linelist.size() == 0) {
			// 该节点不存在已注册的退回到首节点的路径！
			return new ResultMsg(false, "WFERR-0511", new String[] {});
		}
		return new ResultMsg(true, "流程可退回首节点！");
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public ResultMsg sendBackWorkflowToFirstNode(String execId, String actiId,
			Map variables, String assignee, String opinion) {

		if (assignee == null) {
			assignee = SecureUtil.getCurrentUser().getUsercode();
		}

		// 查询流程定义确定是否可退回
		ExecutionService es = processEngine.getExecutionService();
		ProcessInstance pi = es.createProcessInstanceQuery()
				.processInstanceId(execId).uniqueResult();
		if (null == pi) {

			// 取得流程实例时发生异常
			return new ResultMsg(false, "WFERR-0101", new String[] { execId });
		}

		RepositoryService rs = processEngine.getRepositoryService();
		ProcessDefinition dm = rs.createProcessDefinitionQuery()
				.processDefinitionId(pi.getProcessDefinitionId())
				.uniqueResult();
		if (null == dm) {
			// 取得流程实例的版本定义时发生异常
			return new ResultMsg(false, "WFERR-0102", new String[] { execId });
		}
		String key = dm.getKey();
		int version = dm.getVersion();

		// 判断流程是否已挂起
		ResultMsg msg2 = this.isSuspended(execId);
		if (!msg2.isSuccess()) {
			return msg2;
		}
		Boolean bl = (Boolean) msg2.getBody().get("isSuspended");
		if (bl) {
			// 流程已挂起 不可退回
			return new ResultMsg(false, "WFERR-0702", new String[] {});
		}

		List<SysWorkflowExtAttr> backAttrs = (List<SysWorkflowExtAttr>) sysWorkflowExtAttrDao
				.find(" from SysWorkflowExtAttr where key = ? and version = ? and  category= 'TASK_BACKABLE' and srcacti= ?",
						key, version, actiId);
		if (backAttrs.size() == 0) {
			// 流程的退回属性未定义，请检查流程图定义！
			return new ResultMsg(false, "WFERR-0501", new String[] {});
		}
		SysWorkflowExtAttr attr = backAttrs.get(0);
		String backFlag = attr.getAttrvalue3();
		if ("false".equals(backFlag)) {
			// 根据流程节的退回属性定义，该任务不可退回首节点！
			return new ResultMsg(false, "WFERR-0510", new String[] {});
		}
		// 取得定义的首节点
		List<SysWorkflowProcversion> proclist = (List<SysWorkflowProcversion>) sysWorkflowProcversionDao
				.find(" from SysWorkflowProcversion where key = ? and version = ? ",
						key, version);
		if (proclist.size() == 0) {
			return new ResultMsg(false, "WFERR-0102", new String[] { execId });
		}

		// 取得首节点的历史处理人
		HistoryService hs = processEngine.getHistoryService();
		HistoryActivityInstanceQuery haiq = hs
				.createHistoryActivityInstanceQuery();
		haiq.executionId(execId);
		haiq.activityName(proclist.get(0).getFirstnode());
		haiq.orderDesc(HistoryActivityInstanceQuery.PROPERTY_ENDTIME);

		String oldAssign = null;
		List<HistoryActivityInstance> htlist = haiq.list();
		if (htlist.size() == 0) {
			// 无法取得首节点的历史处理记录
			return new ResultMsg(false, "WFERR-0512", new String[] {});
		}
		HistoryTaskInstanceImpl hi = (HistoryTaskInstanceImpl) htlist.get(0);
		oldAssign = hi.getHistoryTask().getAssignee();

		// 取得退回路径
		List<SysWorkflowBacklineRec> linelist = (List<SysWorkflowBacklineRec>) sysWorkflowBacklineRecDao
				.find(" from SysWorkflowBacklineRec where key = ? and version = ? and srcacti = ? and tgtacti = ? and type = '3' ",
						key, version, actiId, proclist.get(0).getFirstnode());

		if (linelist.size() == 0) {
			// 该节点不存在已注册的退回到首节点的路径！
			return new ResultMsg(false, "WFERR-0511", new String[] {});
		}
		SysWorkflowBacklineRec rec = linelist.get(0);
		String backTranstionName = rec.getTransname();

		if (variables == null) {
			variables = new HashMap();
		}
		variables.put(WfEventListener.WF_BACK_FLAG, "RETURN");
		ResultMsg msg = completeTaskByType(execId, actiId, backTranstionName,
				variables, assignee, opinion, "RETURN");
		if (!msg.isSuccess()) {
			return msg;
		}
		// 流程已推回
		// 校验是否已到达目标活动节点
		String curActiName = this.getCurrentActivityName(execId);
		if (!proclist.get(0).getFirstnode().equals(curActiName)) {
			// 退回操作未能到达目标节点，退回操作发生异常！
			return new ResultMsg(false, "WFERR-0505", new String[] {});
		}
		// 取得任务

		TaskService ts = processEngine.getTaskService();
		TaskQuery tq = ts.createTaskQuery();
		tq.activityName(proclist.get(0).getFirstnode());
		tq.executionId(execId);
		List<Task> tks = tq.list();
		if (tks.size() == 0) {
			// 退回时发生异常，任务丢失
			return new ResultMsg(false, "WFERR-0506", new String[] {});
		}

		if (tks.size() > 1) {
			// 退回时发生异常，任务重复
			return new ResultMsg(false, "WFERR-0507", new String[] {});
		}

		Task t = tks.get(0);
		// 如果退回后，成为候选任务，则使用原处理人预占该任务
		if (null == t.getAssignee()) {
			ts.takeTask(t.getId(), oldAssign);
		}
		ResultMsg msg1 = new ResultMsg(true, "退回操作成功！");
		HashMap map = new HashMap();
		// 放入当前节点
		map.put("ACTIID", this.getCurrentActivityName(execId));
		msg1.setBody(map);
		return msg1;

		// 历史任务中没有发现可退回的任务处理记录
		// return new ResultMsg(false, "WFERR-0503", new String[] {});

	}

	/**
	 * 取得退回路径
	 * 
	 * @param key
	 * @param verison
	 * @param arcActi
	 * @param tgtActi
	 * @return
	 */
	private String getReturnTransitionName(String key, int version,
			String srcActi, String tgtActi) {

		List<SysWorkflowBacklineRec> list = (List<SysWorkflowBacklineRec>) sysWorkflowBacklineRecDao
				.find(" from SysWorkflowBacklineRec where key = ? and version = ? and  srcacti = ? and tgtacti = ?",
						key, version, srcActi, tgtActi);

		if (0 == list.size()) {
			return null;
		}
		return list.get(0).getTransname();
	}

	@Override
	public String getTransitionDoneStatus(String key, String version,
			String actiId, String transition) {

		String hql = " from SysWorkflowExtAttr where key = ? and version = ? and category = 'TRANS_BDATA_STATUS' and srcacti = ? and transition = ? ";

		List<SysWorkflowExtAttr> attrlist = (List<SysWorkflowExtAttr>) sysWorkflowExtAttrDao
				.find(hql, key, Long.parseLong(version), actiId, transition);

		if (0 == attrlist.size()) {
			return null;
		}

		return attrlist.get(0).getAttrvalue1();
	}

	/**
	 * 由流程的execid取得流程的定义
	 * 
	 * @param execId
	 * @return
	 */
	private ProcessDefinition getProcessDefinition(String execId) {

		HistoryService hs = processEngine.getHistoryService();
		HistoryProcessInstance hpi = hs.createHistoryProcessInstanceQuery()
				.processInstanceId(execId).uniqueResult();
		//
		// ExecutionService es = processEngine.getExecutionService();
		// ProcessInstance pi = es.createProcessInstanceQuery()
		// .processInstanceId(execId).uniqueResult();
		if (null == hpi) {
			return null;
		}

		RepositoryService rs = processEngine.getRepositoryService();
		ProcessDefinition dm = rs.createProcessDefinitionQuery()
				.processDefinitionId(hpi.getProcessDefinitionId())
				.uniqueResult();
		return dm;
	}

	public ProcessDefinitionVO getProcessDefinitionByExecId(String execId) {
		ProcessDefinition pd = getProcessDefinition(execId);
		if (pd == null) {
			return null;
		}

		return new ProcessDefinitionVO(pd.getKey(), pd.getVersion());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List getWorkflowHistoryOpinions(String execId, String order) {

		StringBuilder sql = new StringBuilder();
		sql.append(" select a.type_ actitype,");
		sql.append(" a.execution_ execid,");
		sql.append(" a.activity_name_ actiid,");
		sql.append(" a.transition_ transition,");
		sql.append(" a.htask_ htask,");
		sql.append(" to_char(a.start_, 'yyyy-MM-dd hh:mi:ss') startdate,");
		sql.append(" to_char(a.end_, 'yyyy-MM-dd hh:mi:ss') enddate,");
		sql.append(" (select assignee_ from jbpm4_hist_task t where t.dbid_=a.htask_) author,");
		sql.append(" o.opinion");
		sql.append(" from jbpm4_hist_actinst a");
		sql.append(" left join sys_workflow_opinion o");
		sql.append(" on a.htask_ = o.htask");
		sql.append(" where a.execution_ = '" + execId + "'");
		sql.append(" and a.type_ in ('task', 'state')");
		sql.append(" order by a.start_ " + order);

		List<HistoryOpinionVO> opinions = (List<HistoryOpinionVO>) sysWorkflowOpinionDao
				.findVoBySql(sql.toString(), HistoryOpinionVO.class);
		// 填入节点名称
		ProcessDefinition pd = this.getProcessDefinition(execId);
		ProcessDefinitionImpl definitionimpl = (ProcessDefinitionImpl) pd;
		List<? extends Activity> list = definitionimpl.getActivities();
		HashMap<String, String> map = new HashMap<String, String>();

		for (Activity activity : list) {
			ActivityImpl ai = (ActivityImpl) activity;
			map.put(ai.getName(), ai.getDescription());
		}
		StringBuilder usernames = new StringBuilder();

		// 放入节点名称并
		for (HistoryOpinionVO v : opinions) {
			v.setActiName(map.get(v.getActiId()));
			if (v.getAuthor() != null && v.getAuthor().trim().length() > 0) {
				usernames.append("'").append(v.getAuthor()).append("',");
			}
		}
		if (usernames.length() > 0) {
			usernames.deleteCharAt(usernames.length() - 1);
			List<SysUser> users = (List<SysUser>) sysUserDao
					.find(" from SysUser where usercode in ("
							+ usernames.toString() + ")");
			// 填入用户名
			HashMap<String, String> map2 = new HashMap<String, String>();
			for (SysUser user : users) {
				map2.put(user.getUsercode(), user.getUsername());
			}

			for (HistoryOpinionVO v : opinions) {
				if (null != v.getAuthor() && v.getAuthor().trim().length() > 0)
					v.setAuthorName(map2.get(v.getAuthor()));

				if (null == v.getEnddate()) {
					v.setAuthorName("");
				}
			}
		}
		return opinions;
	}

	@Override
	public ResultMsg suspend(String execId) {
		WfSuspendAndResumeCmd cmd = new WfSuspendAndResumeCmd(execId,
				WfSuspendAndResumeCmd.SUSPEND);
		return processEngine.execute(cmd);
	}

	@Override
	public ResultMsg resume(String execId) {
		WfSuspendAndResumeCmd cmd = new WfSuspendAndResumeCmd(execId,
				WfSuspendAndResumeCmd.RESUME);
		return processEngine.execute(cmd);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResultMsg isSuspended(String execId) {

		ResultMsg msg = new ResultMsg(false, "");
		HistoryService hs = processEngine.getHistoryService();
		HistoryProcessInstance hpi = hs.createHistoryProcessInstanceQuery()
				.processInstanceId(execId).uniqueResult();
		if (hpi != null) {
			msg.setSuccess(false);
			// 流程实例已结束
			msg.setTitle(this.getExceptionMessage("WFERR-0105",
					new String[] { execId }));
		}
		ExecutionService es = processEngine.getExecutionService();
		ProcessInstance pi = es.createProcessInstanceQuery()
				.processInstanceId(execId).uniqueResult();

		if (pi == null) {
			msg.setSuccess(false);
			// 流程实例不存在
			msg.setTitle(this.getExceptionMessage("WFERR-0101",
					new String[] { execId }));
		}
		boolean bl = false;
		try {
			bl = pi.isSuspended();
			msg.setSuccess(true);
			msg.setTitle("检查成功！");
			HashMap map = new HashMap();
			map.put("isSuspended", bl);
			msg.setBody(map);
			return msg;
		} catch (Exception e) {
			msg.setSuccess(false);
			// 流程实例不存在
			msg.setTitle(this.getExceptionMessage("WFERR-0101",
					new String[] { execId }));
			return msg;
		}
	}

	// 分析合法的前一task节点
	public void analyzePreviousTaskNode(Map actis, String curActiId,
			Set<String> taskSet) {

		// Set<String> taskSet = new HashSet<String>();

		// Map actis = pdi.getActivitiesMap();

		// 当前节点
		ActivityImpl curActi = (ActivityImpl) actis.get(curActiId);

		List<TransitionImpl> tiList = (List<TransitionImpl>) curActi
				.getIncomingTransitions();
		for (TransitionImpl ti : tiList) {
			if (!ti.getName().startsWith("退回")) {
				// 滤掉退回路线
				ActivityImpl prevAi = ti.getSource();
				if ("task".equals(prevAi.getType())) {
					// 如果前节点是task
					taskSet.add(prevAi.getName());
				} else if ("decision".equals(prevAi.getType())) {
					analyzePreviousTaskNode(actis, prevAi.getName(), taskSet);
				}
			}
		}
	}

	@Override
	public String getExceptionMessage(String code, String[] args) {
		return JbfContextLoaderListener.applicationContext.getMessage(code,
				args, "未知的异常编码！", Locale.getDefault());
	}

	@Override
	public String getGlobalConfig(String configCode) {
		return JbfContextLoaderListener.applicationContext.getMessage(
				configCode, null, null, Locale.getDefault());
	}

	@Override
	public List<Integer> getWorkflowVersions(String key) {
		RepositoryService re = processEngine.getRepositoryService();
		ProcessDefinitionQuery pdq = re.createProcessDefinitionQuery();
		pdq.processDefinitionKey(key);
		List<ProcessDefinition> pdlist = pdq.list();
		List<Integer> versions = new ArrayList<Integer>();
		for (int i = 0; i < pdlist.size(); i++) {
			versions.add(pdlist.get(i).getVersion());
		}
		return versions;
	}

	@Override
	public List<HashMap> getWorkflowDefinitionActivities(String key, int version) {
		RepositoryService re = processEngine.getRepositoryService();
		ProcessDefinitionQuery pdq = re.createProcessDefinitionQuery();
		pdq.processDefinitionKey(key);
		List<ProcessDefinition> pdlist = pdq.list();
		for (int i = 0; i <= pdlist.size(); i++) {
			ProcessDefinition pd = pdlist.get(i);
			if (version == pd.getVersion()) {

				// 找到版本
				ProcessDefinitionImpl pdi = (ProcessDefinitionImpl) pd;
				List<ActivityImpl> ailist = (List<ActivityImpl>) pdi
						.getActivities();
				List<HashMap> actiList = new ArrayList<HashMap>();
				for (ActivityImpl ai : ailist) {
					HashMap map = new HashMap();
					map.put("actiId", ai.getName());
					map.put("actiName", ai.getDescription());
					map.put("type", ai.getType());
					actiList.add(map);
				}
				return actiList;
			}
		}
		// 没有找到版本
		return null;
	}

	@Override
	public int getValidVersionByDate(String key, Date date) {

		List<SysWorkflowProcversion> verlist = (List<SysWorkflowProcversion>) sysWorkflowProcversionDao
				.find(" from SysWorkflowProcversion where key= ?", key);

		if (verlist.size() == 0) {
			return -1;
		}

		String curdate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		SysWorkflowProcversion sel = null;
		for (SysWorkflowProcversion v : verlist) {
			if (v.getStartdate() == null) {
				continue;
			}
			if (curdate.compareTo(v.getStartdate()) >= 0) {
				// 满足起始条件
				if (v.getEnddate() == null) {
					sel = v;
					break;
				}
				if (curdate.compareTo(v.getEnddate()) <= 0) {
					sel = v;
					break;
				}
			}
		}
		if (sel != null) {
			return sel.getVersion();
		} else {
			return -1;
		}
	}

}
