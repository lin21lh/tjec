/************************************************************
 * 类名：ModulesTaskService.java
 *
 * 类别：服务接口
 * 功能：模块定时任务服务接口（业务模块必须实现getTask方法）
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-01-07  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.quartz.service;

import java.util.List;

import com.jbf.sys.quartz.dto.TaskDTO;

public interface ModulesTaskService {

	/**
	 * 获取当前模块定时任务
	 * @return
	 */
	public List<TaskDTO> getTask();
}
