/************************************************************
 * 类名：SysWorkflowDeccondsService
 * 
 * 类别：Service
 * 功能：
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   maqs         初版
 *   
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.service;

import java.util.Map;

import com.jbf.common.exception.AppException;

public interface SysWorkflowDeccondsService {
	
	/**
	 * Map totask 下一个任务节点 ； alltask 已定义条件的所有节点
	 * @param wfkey
	 * @param wfversion
	 * @param decisionname
	 * @param billid
	 * @return
	 * @throws AppException
	 */
	public Map<String, Object> getWfTask(String wfkey, Integer wfversion, String decisionname, Long billid) throws AppException;
}
