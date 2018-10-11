/************************************************************
 * 类名：WfLineRebuildListener
 *
 * 类别：Spring事件监听器
 * 功能：在spring启动时，重建撤回、退回路径
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-15  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import org.springframework.web.context.support.XmlWebApplicationContext;

import com.jbf.workflow.service.SysWorkflowBackLineRebuilderService;

public class WfLineRebuildListener implements ApplicationListener {

	@Autowired
	SysWorkflowBackLineRebuilderService sysWorkflowBackLineRebuilderService;

	/**
	 * 在系统启动时恢复工作流中已注册的退回路径
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			Object source = event.getSource();
			XmlWebApplicationContext ctx = (XmlWebApplicationContext) source;
			if (ctx.getDisplayName().indexOf("applicationContext-mvc") >= 0) {
				// 执行工作流
				System.out.println("信息：正在恢复工作流中已注册的退回路径!");
				sysWorkflowBackLineRebuilderService.build();
			}
		}
	}
}
