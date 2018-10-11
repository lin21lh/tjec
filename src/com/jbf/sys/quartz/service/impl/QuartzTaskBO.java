/************************************************************
 * 类名：TaskService.java
 *
 * 类别：组件
 * 功能：提供任务创建、触发器创建、安排任务、暂停、恢复、移除任务
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-01-07  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.quartz.service.impl;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class QuartzTaskBO {

	@Autowired
	 private SchedulerFactoryBean schedulerFactoryBean;
	
	public Scheduler getScheduler() {
		return schedulerFactoryBean.getScheduler();
	}
	
	/**
	 * 暂停定时任务
	 * @param name 定时任务名称
	 * @param group 定时任务组
	 * @throws SchedulerException
	 */
	public void pauseTask(String name, String group) {
		Scheduler scheduler = getScheduler();
		try {
			scheduler.pauseJob(JobKey.jobKey(name, group)); // 停止工作
		} catch (SchedulerException e) {
			 throw new RuntimeException(e);
		}
	}
	
	/**
	 * 恢复定时任务
	 * @param name 定时任务名称
	 * @param group 定时任务组
	 */
	public void resumeTask(String name, String group)  {
		Scheduler scheduler = getScheduler();
		try {
			scheduler.resumeJob(JobKey.jobKey(name, group));
		} catch (SchedulerException e) {
			 throw new RuntimeException(e);
		}
	}
	
	/**
	 * 移除定时任务
	 * @param name 定时任务名称
	 * @param group 定死任务组
	 * @return
	 */
	public boolean removeTask(String name, String group) {  
		Scheduler scheduler = getScheduler();
	    try {
	    	scheduler.pauseTrigger(TriggerKey.triggerKey(name, group)); // 移除触发器
	        return scheduler.unscheduleJob(TriggerKey.triggerKey(name, group));  
	    } catch (SchedulerException e) {  
	        throw new RuntimeException(e);
	    }  
	} 
}
