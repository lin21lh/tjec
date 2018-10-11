/************************************************************
 * 类名：LoadTask.java
 *
 * 类别：加载定时任务
 * 功能：加载、安排定时任务 How to do
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-01-07  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.quartz;

import java.util.List;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.core.jmx.JobDataMapSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.jbf.common.util.StringUtil;
import com.jbf.sys.quartz.dto.TaskDTO;
import com.jbf.sys.quartz.service.TaskService;
 public class LoadTask {
	 
	@Autowired
	 private SchedulerFactoryBean schedulerFactoryBean;
	
	private TaskService taskService;
	
	/**
	 * 初始化定时任务
	 */
	public void initTask() {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		List<TaskDTO> tasks = taskService.findAllTasks();
		for (TaskDTO task : tasks) {
			JobKey jobKey = JobKey.jobKey(task.getName(), task.getGroup());
			try {
				JobDetail jobDetail = scheduler.getJobDetail(jobKey);
				if (jobDetail == null) {
					
					JobBuilder job = JobBuilder.newJob(task.getJobClass()).withIdentity(jobKey);
					
					if (task.getParamMap() != null)
						job.setJobData(JobDataMapSupport.newJobDataMap(task.getParamMap()));
					
					if (StringUtil.isNotBlank(task.getDescription()))
						job.withDescription(task.getDescription());
					
					jobDetail = job.build();
						
					scheduler.scheduleJob(jobDetail, task.getTrigger());
				} else {
					scheduler.resumeJob(jobKey);
				}
			} catch (SchedulerException e) {
				System.err.println("任务安排失败！");
				e.printStackTrace();
			}

		}
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}
}