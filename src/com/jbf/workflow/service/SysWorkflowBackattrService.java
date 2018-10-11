/************************************************************
 * 类名：SysWorkflowBackattr
 *
 * 类别：Service
 * 功能：回退属性Service
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.service;

import java.util.List;

public interface SysWorkflowBackattrService {
	/**
	 * 查询工作流各节点的退回撤回属性
	 * 
	 * @param key
	 *            工作流key
	 * @param version
	 *            工作流版本
	 * @return 各节点的退回撤回属性
	 */
	List queryBackAttr(String key, Integer version);

}
