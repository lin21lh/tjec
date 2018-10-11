/************************************************************
 * 类名：WfSpecialProcessCmd
 *
 * 类别：jbpm命令
 * 功能：jbpm工作流流程撤回处理
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.jbpm.api.Execution;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessDefinitionQuery;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskQuery;
import org.jbpm.api.TaskService;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.history.HistoryActivityInstance;
import org.jbpm.api.history.HistoryActivityInstanceQuery;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.history.model.HistoryActivityInstanceImpl;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;
import org.jbpm.pvm.internal.history.model.HistoryTaskInstanceImpl;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.task.TaskDefinitionImpl;
import org.jbpm.pvm.internal.task.TaskImpl;
import org.springframework.beans.factory.BeanFactory;

import com.jbf.common.exception.AppException;
import com.jbf.common.web.ResultMsg;
import com.jbf.workflow.dao.SysWorkflowExtAttrDao;
import com.jbf.workflow.dao.SysWorkflowOpdefDao;
import com.jbf.workflow.dao.SysWorkflowOpinionDao;
import com.jbf.workflow.dao.SysWorkflowProcversionDao;
import com.jbf.workflow.listener.WfEventListener;
import com.jbf.workflow.po.SysWorkflowExtAttr;
import com.jbf.workflow.po.SysWorkflowOpdef;
import com.jbf.workflow.po.SysWorkflowOpinion;
import com.jbf.workflow.po.SysWorkflowProcversion;
import com.jbf.workflow.vo.EventSourceVO;
import com.jbf.workflow.vo.HistoryActivityVO;

public class WfSpecialProcessCmd implements Command {

	String execid;
	String actiId;
	Map var;
	String usercode; // 撤回申请人

	// 暂存节点ID
	Long hisActiId = null;
	String hisTaskId = null;

	/**
	 * 
	 * @param execid
	 *            流程ID
	 * @param activityName
	 *            要撤回的目的活动节点
	 * @param variables
	 *            变量，可选
	 */
	public WfSpecialProcessCmd(String execid, String actiId, String usercode,
			Map variables) {
		this.execid = execid;
		this.actiId = actiId;
		this.usercode = usercode;
		this.var = variables;

	}

	@Override
	public ResultMsg execute(Environment environment) throws Exception {
		// 判断是否可以撤回
		if (actiId == null || actiId.trim().length() == 0) {
			// 要撤回的目的活动节点参数据异常！
			return new ResultMsg(false, "WFERR-0601", new String[] {});
		}

		StringBuilder sql = new StringBuilder();
		sql.append(" select t.execution_     execid,");
		sql.append(" a.class_         actitype,");
		sql.append(" a.activity_name_ actiname,");
		sql.append(" t.assignee_      assignee,");
		sql.append(" to_char(t.create_,'yyyy-MM-dd hh:mm:ss')        createtime,");
		sql.append(" to_char(t.end_,'yyyy-MM-dd hh:mm:ss')        endtime");
		sql.append(" from jbpm4_hist_actinst a");
		sql.append(" left join jbpm4_hist_task t");
		sql.append(" on a.htask_ = t.dbid_");
		sql.append(" where t.execution_ = '" + execid + "'");
		sql.append(" order by t.create_ desc");
		BeanFactory fc = BeanFactoryHelper.getBeanFactory();
		SysWorkflowProcversionDao sysWorkflowProcversionDao = (SysWorkflowProcversionDao) fc
				.getBean("com.jbf.workflow.dao.impl.SysWorkflowProcversionDaoImpl");
		List<HistoryActivityVO> list = (List<HistoryActivityVO>) sysWorkflowProcversionDao
				.findVoBySql(sql.toString(), HistoryActivityVO.class);

		if (list.size() < 2) {
			// 历史任务中没有发现可撤回的任务处理记录
			return new ResultMsg(false, "WFERR-0602", new String[] {});

		}

		HistoryActivityVO lastActi = list.get(0);
		if (null != lastActi.getEndtime()) {
			// 历史任务中没有发现可撤回的任务处理记录
			return new ResultMsg(false, "WFERR-0602", new String[] {});

		}
		if ("task".equals(lastActi.getActitype())
				|| "state".equals(lastActi.getActitype())) {
			for (int i = 1; i < list.size(); i++) {
				HistoryActivityVO v = list.get(i);
				if ("decision".equals(v.getActitype())) {
					continue;
				} else if ("task".equals(v.getActitype())
						&& usercode.equals(v.getAssignee())
						&& actiId.equals(v.getActiname())) {
					// 执行撤回
					ResultMsg rmsg = doGetBack(environment, fc);

					// 执行两个字段的强制更新,通过hibernate无法更新
					if (hisTaskId != null) {
						String sql1 = "update jbpm4_hist_task set end_='',duration_=0 where dbid_="
								+ hisTaskId;
						sysWorkflowProcversionDao.updateBySql(sql1);
					}
					if (hisActiId != null) {
						String sql2 = "update jbpm4_hist_actinst set end_='',duration_=0  where dbid_="
								+ hisActiId;
						sysWorkflowProcversionDao.updateBySql(sql2);
					}
					return rmsg;

				} else {
					// 流程当前所处节点超出可撤回的范围
					return new ResultMsg(false, "WFERR-0603", new String[] {});
				}
			}
			// 历史任务中没有发现可撤回的任务处理记录
			return new ResultMsg(false, "WFERR-0602", new String[] {});

		} else {
			// 流程当前节点类型 不可撤回
			return new ResultMsg(false, "WFERR-0604",
					new String[] { lastActi.getActitype() });
		}

	}

	/**
	 * 执行撤回
	 * 
	 * @param environment
	 * @return
	 */
	private ResultMsg doGetBack(Environment environment, BeanFactory fc)
			throws Exception {
		// 取得各服务
		Session session = environment.get(Session.class);
		ExecutionService es = environment.get(ExecutionService.class);
		TaskService ts = environment.get(TaskService.class);
		HistoryService hs = environment.get(HistoryService.class);
		RepositoryService rs = environment.get(RepositoryService.class);
		Execution e = es.findExecutionById(execid);

		String procDefId = e.getProcessDefinitionId();

		ProcessDefinitionQuery pdq = rs.createProcessDefinitionQuery();

		pdq.processDefinitionId(procDefId);
		ProcessDefinition pd = pdq.uniqueResult();

		// 2 删除现有的任务

		Task t = ts.createTaskQuery().executionId(execid).uniqueResult();
		TaskImpl tt = (TaskImpl) t;
		session.delete(tt);

		// // 设置流程回退标志为“撤回”,用于在EventListener业务处理参考
		// // EventListener业务处理时，应该清除这个标志
		// var.put(WfEventListener.WF_BACK_FLAG, "WITHDRAW");
		// ts.completeTask(t.getId(), "退回至" + activityName, var);

		// 3.2清除之前发送的痕迹

		// 3.2.1还原jbpm4_hist_actinst表

		// 取得所有的历史活动，最后一个task为当前任务，倒数第二个task为要撤回的目的活动
		// 倒数第二个task的执行人必须与当前的撤回申请人相同
		List<HistoryActivityInstance> hinsts2 = hs
				.createHistoryActivityInstanceQuery().executionId(execid)
				.orderAsc(HistoryActivityInstanceQuery.PROPERTY_STARTTIME)
				.list();
		String srcActi = null, destActi = null;
		int totalSize = hinsts2.size() - 1;
		for (int i = totalSize; i >= 0; i--) {
			HistoryActivityInstanceImpl hai = (HistoryActivityInstanceImpl) hinsts2
					.get(i);
			if (i == totalSize) {
				session.delete(hai);
				hinsts2.remove(hai);
				srcActi = hai.getActivityName();
				continue;
			}

			if ("task".equals(hai.getType())) {

				// 当循环到task任务时，停止
				destActi = hai.getActivityName();
				// 还原结束时间为null
				Field endTimeField = HistoryActivityInstanceImpl.class
						.getDeclaredField("endTime");
				endTimeField.setAccessible(true);
				endTimeField.set(hai, null);
				Field durationField = HistoryActivityInstanceImpl.class
						.getDeclaredField("duration");
				durationField.setAccessible(true);
				durationField.set(hai, 0L);
				session.update(hai);
				hisActiId = hai.getDbid();
				System.out.println("hisActiId=" + hisActiId);
				// 还原其对应的历史任的结束时间为null
				HistoryTaskImpl hi = ((HistoryTaskInstanceImpl) hai)
						.getHistoryTask();

				Field endTimeField1 = HistoryTaskImpl.class
						.getDeclaredField("endTime");
				endTimeField1.setAccessible(true);
				endTimeField1.set(hi, null);
				Field durationField1 = HistoryTaskImpl.class
						.getDeclaredField("duration");
				durationField1.setAccessible(true);
				durationField1.set(hi, 0L);

				session.update(hi);
				hisTaskId = hi.getId();
				System.out.println("hisTaskId=" + hisTaskId);

				// 还原Task
				TaskImpl ti = new TaskImpl();
				ti.setDbid(Long.parseLong(hi.getId()));
				ti.setName(hai.getActivityName());
				ti.setState("open");
				ti.setAssignee(hi.getAssignee());
				ti.setPriority(0);
				ti.setCreateTime(hi.getCreateTime());
				ti.setSignalling(true);
				ti.setExecution((ExecutionImpl) e);
				ti.setActivityName(hai.getActivityName());
				ti.setProcessInstance((ExecutionImpl) e);

				if (pd != null) {
					TaskDefinitionImpl td = ((ProcessDefinitionImpl) pd)
							.getTaskDefinition(hai.getActivityName());
					ti.setTaskDefinition(td);
				}
				session.save(ti);

				// 删除原意见
				SysWorkflowOpinionDao opinionDao = environment
						.get(SysWorkflowOpinionDao.class);
				List<SysWorkflowOpinion> oldOpinion = (List<SysWorkflowOpinion>) opinionDao
						.find(" from SysWorkflowOpinion where htask = ?",
								Long.parseLong(hi.getId()));
				opinionDao.deleteAll(oldOpinion);
				// 还原exceution
				ExecutionImpl eei = (ExecutionImpl) e;

				ActivityImpl ai = new ActivityImpl();
				ai.setName(hai.getActivityName());
				eei.setActivity(ai);
				eei.setHistoryActivityInstanceDbid(hai.getDbid());
				session.update(eei);
				eei.setVariable(WfEventListener.WF_BACK_FLAG, "WITHDRAW");
				break;
			} else {
				session.delete(hai);
				hinsts2.remove(hai);
			}
		}
		if (srcActi == null) {
			return new ResultMsg(false, "无法找到开始结点！");
		}

		// A执行节点的事后事件
		executeActivityEvent((ExecutionImpl) e, pd, srcActi, 2, fc);

		// if (hinsts2.size() > 0) {
		// 3.2.2 还原HistoryTaskInstance
		// HistoryTaskInstanceImpl last = (HistoryTaskInstanceImpl) hinsts2
		// .get(hinsts2.size() - 1);
		// last.setTransitionName("");
		// session.update(last);

		// 3.2.3 还原HistoryTask
		// HistoryTaskImpl ta = last.getHistoryTask();
		// ta.setOutcome("");
		// ta.setState("");
		// ta.setDuration(0L);
		// session.update(ta);

		// } else {
		// return new ResultMsg(false, "历史活动列表为空，撤回失败!");
		// }

		// B执行节点的事前事件
		executeActivityEvent((ExecutionImpl) e, pd, destActi, 1, fc);

		// 取得任务

		TaskQuery tq = ts.createTaskQuery();
		tq.activityName(destActi);
		tq.executionId(e.getId());
		List<Task> tks = tq.list();
		if (tks.size() == 0) {
			return new ResultMsg(false, "退回后任务丢失，请检查流程数据！");
		}

		if (tks.size() > 1) {
			return new ResultMsg(false, "退回后任务重复，请检查流程数据！");
		}

		Task ttt = tks.get(0);
		// 如果退回后，成为候选任务，则使用原处理人预占该任务
		if (null == ttt.getAssignee()) {
			ts.takeTask(ttt.getId(), usercode);
		}

		// 请求hibernate执行sql,为后面的执行强制更新做准备
		session.flush();
		return new ResultMsg(true, "流程撤回成功！");
	}

	private void executeActivityEvent(ExecutionImpl exec, ProcessDefinition pd,
			String actiId, int type, BeanFactory fc) throws Exception {

		// 查询配置的参数
		String[] operationCfg = getOperationConfig(fc, type, pd.getKey(),
				pd.getVersion(), actiId, null);
		if (null == operationCfg) {
			throw new Exception("取得流程节点或迁移路径的操作配置时发生异常，请检查流程图配置！");
		}

		// 如果没有配置操作
		if (null == operationCfg[0] || 0 == operationCfg[0].trim().length()) {
			return;
		}
		Map<String, String> argMap = parseArgs(operationCfg[1]);

		// 2.操作定义查询
		// 2.1 取得流程部署id
		String deploymentId = pd.getDeploymentId();
		// 2.2 取得流程定义id
		SysWorkflowProcversionDao workflowProcversionDao = (SysWorkflowProcversionDao) fc
				.getBean("com.jbf.workflow.dao.impl.SysWorkflowProcversionDaoImpl");

		List<SysWorkflowProcversion> versionList = (List<SysWorkflowProcversion>) workflowProcversionDao
				.find(" from SysWorkflowProcversion where deploymentid =? ",
						deploymentId);
		if (versionList == null || versionList.size() == 0) {
			throw new Exception("deploymentid为" + deploymentId
					+ "的流程定义未找到，无法继续执行工作流业务操作！");
		}
		SysWorkflowProcversion procver = versionList.get(0);

		// 2.3按流程版本取得流程事件对应的操作类型

		SysWorkflowOpdefDao workflowOpdefDao = (SysWorkflowOpdefDao) fc
				.getBean("com.jbf.workflow.dao.impl.SysWorkflowOpdefDaoImpl");

		List<SysWorkflowOpdef> oplist = (List<SysWorkflowOpdef>) workflowOpdefDao
				.find(" from SysWorkflowOpdef where key = ?  and name = ?",
						procver.getKey(), operationCfg[0]);
		if (oplist == null || oplist.size() == 0) {
			throw new Exception("流程" + procver.getKey() + "-"
					+ procver.getVersion() + "定义的操作" + operationCfg[0]
					+ "未找到，无法继续执行工作流业务操作！");
		}

		// 选中指定操作名称的操作

		if (oplist.size() == 0) {
			throw new Exception("流程" + procver.getKey() + "-"
					+ procver.getVersion() + "定义的操作'" + operationCfg[0]
					+ "'未找到，无法继续执行工作流业务操作！");
		}

		EventSourceVO eventvo = new EventSourceVO();
		eventvo.setSrcActi(actiId);

		eventvo.setType("" + type);
		// 2.4 按流程定义进行查询
		SysWorkflowOpdef op = oplist.get(0);
		try {
			Object handlerBean = fc.getBean(op.getClassname());
			Class handlerBeanClass = handlerBean.getClass();
			Method method = handlerBeanClass.getMethod(op.getMethodname(),
					String.class, Map.class, Map.class, EventSourceVO.class);
			Map vars = exec.getVariables();
			vars.put(WfEventListener.WF_BACK_FLAG, "WITHDRAW");
			method.invoke(handlerBean, exec.getId(), argMap, vars, eventvo);
			exec.setVariables(vars);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("流程" + procver.getKey() + "-"
					+ procver.getVersion() + "定义的操作'" + operationCfg[0]
					+ "'执行过程中发生异常，执行失败!");
		}
	}

	/**
	 * 取得操作配置
	 * 
	 * @param factory
	 * @param type
	 * @param key
	 * @param version
	 * @param srcacti
	 * @param transitionName
	 * @return 配置内容 ,String[0]为操作名称，String[1]为操作参数
	 */
	private String[] getOperationConfig(BeanFactory factory, int type,
			String key, int version, String srcacti, String transitionName) {

		SysWorkflowExtAttrDao sysWorkflowExtAttrDao = (SysWorkflowExtAttrDao) factory
				.getBean("com.jbf.workflow.dao.impl.SysWorkflowExtAttrDaoImpl");

		String hql = " from SysWorkflowExtAttr where  key = ? and version = ? and srcacti = ? ";
		List<SysWorkflowExtAttr> attrlist = null;
		if (type == 1) {
			hql += " and category = 'TASK_START_EVENT'";
			attrlist = (List<SysWorkflowExtAttr>) sysWorkflowExtAttrDao.find(
					hql, key, version, srcacti);
		} else if (type == 2) {
			hql += " and category = 'TASK_END_EVENT' ";
			attrlist = (List<SysWorkflowExtAttr>) sysWorkflowExtAttrDao.find(
					hql, key, version, srcacti);
		} else {
			hql += " and category = 'TRANS_EVENT' and transition = ? ";
			attrlist = (List<SysWorkflowExtAttr>) sysWorkflowExtAttrDao.find(
					hql, key, version, srcacti, transitionName);
		}
		if (attrlist.size() > 0) {
			SysWorkflowExtAttr attr = attrlist.get(0);
			return new String[] { attr.getAttrvalue1(), attr.getAttrvalue2() };
		} else {
			return null;
		}

	}

	/**
	 * 解析事件传入的参数
	 * 
	 * @param args
	 *            参数的字符串形式
	 * @return map形的解析后结果
	 */
	private Map<String, String> parseArgs(String args) {
		HashMap<String, String> argMap = new HashMap<String, String>();
		if (null != args && 0 < args.trim().length()) {
			String[] pairs = args.split(";");
			for (String str : pairs) {
				String[] pair = str.split(":");
				if (pair.length == 2) {
					argMap.put(pair[0], pair[1]);
				} else {
					System.out.println("处理操作时发现错误的参数'" + str + "'，已忽略!");
				}
			}
		}
		return argMap;
	}

}
