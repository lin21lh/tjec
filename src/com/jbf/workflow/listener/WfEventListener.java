/************************************************************
 * 类名：WfEventListener
 *
 * 类别：JBPM事件处理器
 * 功能：处理节点开始事件，委托到业务操作上，同时创建退回、撤回路径
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-15  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.listener;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessDefinitionQuery;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.listener.EventListener;
import org.jbpm.api.listener.EventListenerExecution;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.EventImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.TransitionImpl;
import org.springframework.beans.factory.BeanFactory;

import com.jbf.workflow.common.BeanFactoryHelper;

import com.jbf.workflow.dao.SysWorkflowExtAttrDao;
import com.jbf.workflow.dao.SysWorkflowOpdefDao;
import com.jbf.workflow.dao.SysWorkflowProcversionDao;
import com.jbf.workflow.po.SysWorkflowExtAttr;
import com.jbf.workflow.po.SysWorkflowOpdef;
import com.jbf.workflow.po.SysWorkflowProcversion;
import com.jbf.workflow.vo.EventSourceVO;

public class WfEventListener implements EventListener {
	// 事件中可能使用到的退回标志的环境变量名，可选值为
	// 1 WITHDRAW 代表撤回
	// 2 RETURN 代表退回
	// 为null时代表正常流转
	public static final String WF_BACK_FLAG = "WF_BACK_FLAG";

	/**
	 * 事件处理
	 */
	@Override
	public void notify(EventListenerExecution execution) throws Exception {
		BeanFactory factory = BeanFactoryHelper.getBeanFactory();
		processEvent(factory, execution);
	}

	/**
	 * 在执行完退回后，清除环境变量中的退回标志
	 * 
	 * @param execution
	 */
	// private void clearReturnFlag(EventListenerExecution execution) {
	// execution.setVariable(WF_BACK_FLAG, "NORMAL");
	//
	// }

	/**
	 * 处理
	 * 
	 * @param factory
	 *            spring的bean factory
	 * @param execution
	 *            jbpm的executon实例
	 * @throws Exception
	 */
	protected void processEvent(BeanFactory factory,
			EventListenerExecution execution) throws Exception {

		// 1.取得并解析事件参数

		ProcessDefinition pd = getProcessDefinition(execution);
		if (null == pd) {
			throw new Exception("在处理事件过程时，取得流程定义失败！");
		}
		// 取得版本
		String key = pd.getKey();
		int version = pd.getVersion();

		// 判断类型 类型1节点起始事件 ，类型2节点结束事件 3迁移线事件
		int type = -1;
		// 事件节点
		String srcacti = null;
		String transitionName = null;
		ExecutionImpl ei = (ExecutionImpl) execution;

		EventImpl event = ei.getEvent();
		Object eventSource = ei.getEventSource();

		EventSourceVO eventvo = new EventSourceVO();
		if (eventSource instanceof ActivityImpl) {
			if ("start".equals(event.getName())) {
				type = 1;
				srcacti = ((ActivityImpl) eventSource).getName();
				eventvo.setType(event.getName());
				eventvo.setSrcActi(srcacti);

			} else if ("end".equals(event.getName())) {
				type = 2;
				srcacti = ((ActivityImpl) eventSource).getName();
				eventvo.setType(event.getName());
				eventvo.setSrcActi(srcacti);
			} else {
				type = -1;
			}
		} else if (eventSource instanceof TransitionImpl) {
			type = 3;
			srcacti = ((TransitionImpl) eventSource).getSource().getName();
			transitionName = ((TransitionImpl) eventSource).getName();

			eventvo.setType(event.getName());
			eventvo.setSrcActi(srcacti);
			eventvo.setTransition(transitionName);
			eventvo.setTgtActi(((TransitionImpl) eventSource).getDestination()
					.getName());

		} else {
			type = -1;
		}
		if (type == -1) {
			// 未知的事件
			return;
		}
		// 查询配置的参数
		String[] operationCfg = getOperationConfig(factory, type, key, version,
				srcacti, transitionName);
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
		SysWorkflowProcversionDao workflowProcversionDao = (SysWorkflowProcversionDao) factory
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

		SysWorkflowOpdefDao workflowOpdefDao = (SysWorkflowOpdefDao) factory
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
		// 2.4 按流程定义进行查询
		SysWorkflowOpdef op = oplist.get(0);
		try {
			Object handlerBean = factory.getBean(op.getClassname());
			Class handlerBeanClass = handlerBean.getClass();
			Method method = handlerBeanClass.getMethod(op.getMethodname(),
					String.class, Map.class, Map.class, EventSourceVO.class);
			Map vars = execution.getVariables();
			method.invoke(handlerBean, execution.getId(), argMap, vars, eventvo);
			execution.setVariables(vars);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("流程" + procver.getKey() + "-"
					+ procver.getVersion() + "定义的操作'" + operationCfg[0]
					+ "'执行过程中发生异常，执行失败!");
		}
		// 事件处理完毕，自动清除退回，撤回标志
		// clearReturnFlag(execution);
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
	 * 从execution取得流程部署定义
	 * 
	 * @param execution
	 * @return 部署定义id
	 */
	private ProcessDefinition getProcessDefinition(
			EventListenerExecution execution) {
		String procDefId = execution.getProcessDefinitionId();

		RepositoryService repositoryService = (RepositoryService) EnvironmentImpl
				.getFromCurrent(RepositoryService.class);

		ProcessDefinitionQuery pdq = repositoryService
				.createProcessDefinitionQuery();

		pdq.processDefinitionId(procDefId);
		ProcessDefinition pd = pdq.uniqueResult();

		return pd;
	}

	// 不成功，暂不启用
	// public String getAssignee(EventListenerExecution execution) {
	// TaskService taskService = (TaskService) EnvironmentImpl
	// .getFromCurrent(TaskService.class);
	// TaskQuery taskQuery = taskService.createTaskQuery();
	// taskQuery.executionId(execution.getId());
	// taskQuery.activityName(execution.getActivity().getName());
	// List<Task> list = taskQuery.list();
	// return list.get(0).getAssignee();
	// }

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
