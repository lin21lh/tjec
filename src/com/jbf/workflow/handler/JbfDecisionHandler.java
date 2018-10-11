/************************************************************
 * 类名：JbfDecisionHandler
 *
 * 类别：
 * 功能：
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-15  CFIT-PG     maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.handler;

import java.util.List;
import java.util.Map;

import org.jbpm.api.jpdl.DecisionHandler;
import org.jbpm.api.model.OpenExecution;

import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;
import com.jbf.common.util.WebContextFactoryUtil;
import com.jbf.workflow.service.SysWorkflowDeccondsService;

public class JbfDecisionHandler implements DecisionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	 SysWorkflowDeccondsService wfDeccondsService; 
	
	@Override
	public String decide(OpenExecution openexecution) {
		
		if (wfDeccondsService == null)
			wfDeccondsService = (SysWorkflowDeccondsService)WebContextFactoryUtil.getBean("com.jbf.workflow.service.impl.SysWorkflowDeccondsServiceImpl");
		String wfkey = (String)openexecution.getVariable("key");
		Integer wfversion = (Integer)openexecution.getVariable("version");
		String decisionname = openexecution.getActivity().getName(); //transition
		Long billid = Long.valueOf(openexecution.getVariable("billid").toString());
		Map<String, Object> map;
		try {
			map = wfDeccondsService.getWfTask(wfkey, Integer.valueOf(wfversion), decisionname, billid);
			String totask = (String) map.get("totask");
			if (StringUtil.isNotBlank(totask))
				return "发送至" + totask;
			else {
				List<String> alltask = (List<String>)map.get("alltask");
				for (String task : alltask) {
					
				}
				
				return "发送至" + alltask.get(0);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
