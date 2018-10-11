/************************************************************
 * 类名：WfDecisionAgentHandler
 *
 * 类别：decision代理处理器
 * 功能：实现分支裁定功能(框架功能)
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-05-08  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.common;

import java.util.List;
import java.util.Map;

import org.jbpm.api.jpdl.DecisionHandler;
import org.jbpm.api.model.OpenExecution;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.springframework.beans.factory.BeanFactory;

import com.jbf.common.security.SecureUtil;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.dao.SysWorkflowExtAttrDao;
import com.jbf.workflow.po.SysWorkflowExtAttr;
import com.jbf.workflow.vo.ProcessDefinitionVO;

public class WfDecisionAgentHandler implements DecisionHandler {

	/**
	 * 决定流出路径
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	@Override
	public String decide(OpenExecution execution) {

		// 取得流程信息
		String execId = execution.getId();
		String actiId = execution.getActivity().getName();
		ExecutionImpl ei = (ExecutionImpl) execution;
		String key = ei.getProcessDefinition().getKey();
		int version = ei.getProcessDefinition().getVersion();

		ProcessDefinitionVO pd = new ProcessDefinitionVO(key, version);
		// 取得环境变量
		Map variables = execution.getVariables();
		// 取得当前用户
		SysUser user = SecureUtil.getCurrentUser();

		// 取得定义的处理器
		BeanFactory fac = BeanFactoryHelper.getBeanFactory();
		SysWorkflowExtAttrDao sysWorkflowExtAttrDao = (SysWorkflowExtAttrDao) fac
				.getBean("com.jbf.workflow.dao.impl.SysWorkflowExtAttrDaoImpl");
		List<SysWorkflowExtAttr> attrlist = (List<SysWorkflowExtAttr>) sysWorkflowExtAttrDao
				.find(" from SysWorkflowExtAttr where key = ? and version = ? and srcacti = ? and category= ? ",
						key, version, actiId, "DECISION_HANDLER");
		if (attrlist.size() == 0) {
			System.out.println("工作流" + key + "版本" + version + "的" + actiId
					+ "节点业务分支处理器未定义！");
			return null;
		}
		if (attrlist.size() > 1) {
			System.out.println("工作流" + key + "版本" + version + "的" + actiId
					+ "节点业务分支处理器存在重复！");
			return null;
		}
		String decisionBeanType = attrlist.get(0).getAttrvalue1();
		String decisionBeanName = attrlist.get(0).getAttrvalue2();
		Object bean = null;
		try {
			bean = fac.getBean(decisionBeanName);

			if (bean instanceof WfDecisionBusinessHandler) {
				WfDecisionBusinessHandler handler = (WfDecisionBusinessHandler) fac
						.getBean(decisionBeanName);
				return handler.decide(execId, actiId, variables, user, pd);

			} else {
				// 处理器未实现WfDecisionBusinessHandler接口
				System.out.println("工作流" + key + "版本" + version + "的" + actiId
						+ "节点定义的业务分支处理器" + decisionBeanName
						+ "未实现WfDecisionBusinessHandler接口！");
				return null;
			}

		} catch (Exception e) {
			// 处理器不存在
			System.out.println("工作流" + key + "版本" + version + "的" + actiId
					+ "节点定义的业务分支处理器" + decisionBeanName + "不存在！");
			return null;
		}

	}
}
