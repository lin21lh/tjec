/************************************************************
 * 类名：QuartzTaskService.java
 *
 * 类别：服务接口
 * 功能：提供定时任务管理的 查询、暂停、恢复、移除定时任务
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-01-07  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.quartz.service;

import java.util.List;

import org.quartz.SchedulerException;

import com.jbf.sys.quartz.dto.TaskDetailDTO;

public interface QuartzTaskService {

	/**
	 * 查询当前所有定时任务
	 * @param jobgroup 定时任务组
	 * @return
	 * @throws SchedulerException
	 */
	public List<TaskDetailDTO> query(String jobgroup) throws SchedulerException;
	
	/**
	 * 暂停定时任务
	 * @param name 定时任务名称
	 * @param group 定时任务组
	 */
	public void pauseTask(String name, String group);
	
	/**
	 * 恢复定时任务
	 * @param name 定时任务名称
	 * @param group 定时任务组
	 */
	public void resumeTask(String name, String group);
	
	/**
	 * 移除定时任务
	 * @param name 定时任务名称
	 * @param group 定时任务组
	 * @return
	 */
	public boolean removeTask(String name, String group);
}
