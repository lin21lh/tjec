/************************************************************
 * 类名：WfStartEventListener
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jbpm.api.HistoryService;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.history.HistoryActivityInstance;
import org.jbpm.api.history.HistoryActivityInstanceQuery;
import org.jbpm.api.listener.EventListener;
import org.jbpm.api.listener.EventListenerExecution;
import org.jbpm.api.model.Activity;
import org.jbpm.api.model.Transition;
import org.jbpm.pvm.internal.history.model.HistoryActivityInstanceImpl;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.model.TransitionImpl;
import org.springframework.beans.factory.BeanFactory;
import com.jbf.workflow.common.BeanFactoryHelper;
import com.jbf.workflow.dao.SysWorkflowBacklineRecDao;
import com.jbf.workflow.dao.SysWorkflowExtAttrDao;
import com.jbf.workflow.po.SysWorkflowBacklineRec;
import com.jbf.workflow.po.SysWorkflowExtAttr;

public class WfStartEventListener extends WfEventListener implements
		EventListener {
	/**
	 * 事件处理
	 */
	@Override
	public void notify(EventListenerExecution execution) throws Exception {

		BeanFactory factory = BeanFactoryHelper.getBeanFactory();

		// 创建退回、撤回路径
		createBackLines(factory, execution);
		// 处理事件
		processEvent(factory, execution);
	}

	/**
	 * 根据配置动态创建退回、撤回路径
	 * 
	 * @param factory
	 *            spring的beanfactory
	 * @param execution
	 *            jbpm的 execution实例
	 */
	private void createBackLines(BeanFactory factory,
			EventListenerExecution execution) {

		Object bean = factory.getBean("processEngine");
		ProcessEngine processEngine = (ProcessEngine) bean;

		System.out.println("调用 DefaultAgentStartEventListener ...");
		// 1. 取得 当前活动
		Activity to = execution.getActivity();
		ActivityImpl ai = (ActivityImpl) to;
		// 2.取得上一步的活动
		// 2.1取得历史activity记录
		String execid = execution.getId();
		HistoryService hs = processEngine.getHistoryService();

		List<HistoryActivityInstance> ainstances = hs
				.createHistoryActivityInstanceQuery().executionId(execid)
				.orderDesc(HistoryActivityInstanceQuery.PROPERTY_STARTTIME)
				.list();
		HistoryActivityInstanceImpl fromActivityInstance = null;
		for (HistoryActivityInstance i : ainstances) {
			HistoryActivityInstanceImpl haii = (HistoryActivityInstanceImpl) i;
			if ("task".equals(haii.getType()) && haii.getEndTime() != null) {
				fromActivityInstance = haii;
				break;
			}
		}

		if (fromActivityInstance == null) {
			// 说明是第一个task,不需要进行退回，撤回路径生成
			return;
		}
		// 退回及搞撤回的操作，不进行退回路径创建
		Map map = execution.getVariables();
		String returnFlag = (String) map.get(WfEventListener.WF_BACK_FLAG);
		if ("RETURN".equals(returnFlag) || "WITHDRAW".equals(returnFlag)) {
			return;
		}
		String formActivityName = fromActivityInstance.getActivityName();

		// 是否存在路径判断
		List<Transition> trans = (List<Transition>) to.getOutgoingTransitions();
		boolean hasPath = false;
		for (Transition t : trans) {
			Activity acti = t.getDestination();
			if (acti.getName().equals(formActivityName)) {
				hasPath = true;
				break;
			}
		}
		// 如果已存在路径，则不需要再创建
		if (hasPath) {
			return;
		}
		String processDefId = execution.getProcessDefinitionId();
		RepositoryService rs = processEngine.getRepositoryService();

		ProcessDefinitionImpl processDefinitionImpl = (ProcessDefinitionImpl) rs
				.createProcessDefinitionQuery()
				.processDefinitionId(processDefId).list().get(0);
		String depid = processDefinitionImpl.getDeploymentId();

		SysWorkflowExtAttrDao sysWorkflowExtAttrDao = (SysWorkflowExtAttrDao) factory
				.getBean("com.jbf.workflow.dao.impl.SysWorkflowExtAttrDaoImpl");

		List<SysWorkflowExtAttr> attrlist = (List<SysWorkflowExtAttr>) sysWorkflowExtAttrDao
				.find(" from SysWorkflowExtAttr where key = ? and version = ? and category = 'TASK_BACKABLE'  ",
						processDefinitionImpl.getKey(),
						processDefinitionImpl.getVersion());

		HashMap<String, SysWorkflowExtAttr> attrmap = new HashMap<String, SysWorkflowExtAttr>();
		for (SysWorkflowExtAttr attr : attrlist) {
			attrmap.put(attr.getSrcacti(), attr);
		}

		boolean curActiReturnable = Boolean.parseBoolean(attrmap.get(
				to.getName()).getAttrvalue1());
		boolean prevActiWithdrawable = Boolean.parseBoolean(attrmap.get(
				formActivityName).getAttrvalue2());

		if (curActiReturnable || prevActiWithdrawable) {
			// 建立路径
			// 2.2 取得流程定义中的activityImpl

			ActivityImpl fromActivity = processDefinitionImpl
					.findActivity(formActivityName);

			// 3 动态建立由当前活动到原活动的路径
			TransitionImpl tt = ai.createOutgoingTransition();
			tt.setDestination(fromActivity);
			String desc = fromActivity.getDescription();
			if (desc != null) {
				tt.setName("退回至" + desc);
			} else {
				tt.setName("退回至" + fromActivity.getName());
			}
			System.out.println("正在动态建立由当前活动到原活动的路径:" + to.getName() + " 至 "
					+ fromActivity.getName());

			// 4 持久化到退回路线到表中，解决web容器重启后丢失的问题
			SysWorkflowBacklineRecDao sysWorkflowBacklineRecDao = (SysWorkflowBacklineRecDao) factory
					.getBean("com.jbf.workflow.dao.impl.SysWorkflowBacklineRecDaoImpl");

			// 取得流程定义
			String pdid = execution.getProcessDefinitionId();

			ProcessDefinition pd = rs.createProcessDefinitionQuery()
					.processDefinitionId(pdid).uniqueResult();

			List<SysWorkflowBacklineRec> list = (List<SysWorkflowBacklineRec>) sysWorkflowBacklineRecDao
					.find(" from SysWorkflowBacklineRec where key = ? and version = ? and srcacti = ? and tgtacti = ?",
							pd.getKey(), pd.getVersion(), to.getName(),
							fromActivity.getName());
			if (list == null || list.size() == 0) {
				SysWorkflowBacklineRec rec = new SysWorkflowBacklineRec();
				rec.setKey(pd.getKey());
				rec.setVersion(pd.getVersion());
				rec.setSrcacti(to.getName());
				rec.setTgtacti(fromActivity.getName());
				if (desc != null) {
					rec.setTransname("退回至" + desc);
				} else {
					rec.setTransname("退回至" + fromActivity.getName());
				}
				rec.setType("1");
				sysWorkflowBacklineRecDao.save(rec);
			}

		}
	}
}
