/************************************************************
 * 类名：SysWorkflowOpdefService
 * 
 * 类别：Service
 * 功能：工作流操作定义服务
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   hyf         初版
 *   
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.service;

import java.util.List;

import com.jbf.workflow.po.SysWorkflowOpdef;

public interface SysWorkflowOpdefService {
	/**
	 * 按id取得工作流操作定义
	 * 
	 * @param id
	 * @return
	 */
	public SysWorkflowOpdef get(Long id);

	/**
	 * 查询工作流定义下面的操作
	 * 
	 * @param key
	 *            工作流key
	 * @return 工作流定义下面的操作
	 */
	public List query(String key);

	/**
	 * 保存工作流定义下面的操作
	 * 
	 * @param workflowOpdef
	 *            工作流操作详情
	 */
	public void save(SysWorkflowOpdef workflowOpdef);

	/**
	 * 删除工作流定义下面的操作
	 * 
	 * @param id
	 *            工作流定义操作id
	 * @throws Exception
	 */
	public void del(Long id) throws Exception;
}
