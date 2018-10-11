/************************************************************
 * 类名：SysWorkflowTaskPageUrlService
 * 
 * 类别：Service
 * 功能：工作流任务节点对应页面的服务
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   hyf         初版
 *   
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.service;

import java.util.List;

public interface SysWorkflowTaskPageUrlService {
	/**
	 * 查询某个版本的工作流的所有节点对应的页面
	 * @param key 工作流key
	 * @param version 版本号
	 * @return 页面列表
	 */
	List queryTaskPageUrl(String key, Integer version);

}
