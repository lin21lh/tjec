/************************************************************
 * 类名：SysWorkflowBackLineRebuilderService
 * 
 * 类别：Service
 * 功能：工作流退回撤回路线重建服务，用于在应用服务器启动时重建
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   hyf         初版
 *   
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.service;

public interface SysWorkflowBackLineRebuilderService {
	/**
	 * 执行动态路线 重建
	 */
	public void build();
}
