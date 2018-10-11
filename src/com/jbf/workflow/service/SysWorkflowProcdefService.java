/************************************************************
 * 类名：SysWorkflowTaskPageUrlService
 * 
 * 类别：Service
 * 功能：工作流定义服务
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   hyf         初版
 *   
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jbf.common.exception.AppException;
import com.jbf.common.vo.TreeVo;
import com.jbf.common.web.ResultMsg;
import com.jbf.workflow.po.SysWorkflowProcdef;
import com.jbf.workflow.po.SysWorkflowProcversion;
import com.jbf.workflow.vo.WorkflowTreeVo;

public interface SysWorkflowProcdefService {
	/**
	 * 查询工作流列表
	 * 
	 * @param name
	 *            名称
	 * @param showVersion
	 *            "true"时显示工作流版本，"false"时不显示
	 * @return 工作流列表
	 */
	List<? extends TreeVo> queryWorkflowTree(String name, String showVersion);

	/**
	 * 取得工作流定义信息
	 * 
	 * @param id
	 *            工作流定义信息id
	 * @return 工作流定义信息
	 */
	SysWorkflowProcdef get(Long id);

	/**
	 * 保存工作流定义信息
	 * 
	 * @param def
	 *            工作流定义信息
	 * @throws AppException
	 */
	void saveWorkflowProcdef(SysWorkflowProcdef def) throws AppException;

	/**
	 * 删除工作流定义
	 * 
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 取得工作流定义的jpdl内容
	 * 
	 * @param deploymentId
	 * @return 工作流定义的jpdl内容
	 */
	Map getDeployedJpdlContent(String deploymentId);

	/**
	 * 查询工作流下面定义年版本
	 * 
	 * @param key
	 *            工作流key
	 * @return 工作流下面定义年版本
	 */
	List queryWorkflowVersionsByKey(String key);

	/**
	 * 
	 * @param key
	 * @return
	 */
	String getWorkflowTablecodeByKey(String key)  throws AppException;

	/**
	 * 查询工作流定义，以树型返回数据
	 * 
	 * @return 树型返回数据
	 */
	List<? extends TreeVo> queryWorkflowConfigTree();

	/**
	 * 查询工作流版本
	 * 
	 * @param key
	 * @return
	 */
	List<SysWorkflowProcversion> queryWorkflowVersion(String key);

	/**
	 * 工作流版本复制 maqs
	 * 
	 * @param sourceWfID
	 *            源工作流id
	 * @param sourceWfName
	 *            源工作流名称
	 * @param objectWfKey
	 *            目标工作流key
	 * @param objectWfName
	 *            目标工作流名称
	 * @return 成功标志
	 * @throws AppException
	 */
	ResultMsg wfcopyopt(Long sourceWfID, String sourceWfName,
			String objectWfKey, String objectWfName) throws AppException;

	/**
	 * 刪除工作流版本
	 * 
	 * @param id
	 *            版本id
	 * @throws AppException
	 */
	void deleteWfVersion(Long id) throws AppException;

}
