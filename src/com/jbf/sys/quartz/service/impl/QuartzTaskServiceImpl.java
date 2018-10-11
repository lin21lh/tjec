/************************************************************
 * 类名：QuartzTaskServiceImpl.java
 *
 * 类别：服务实现类
 * 功能：提供定时任务管理的 查询、暂停、恢复、移除定时任务
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-01-07  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.quartz.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.quartz.dto.TaskDetailDTO;
import com.jbf.sys.quartz.service.QuartzTaskService;

@Scope("prototype")
@Service
public class QuartzTaskServiceImpl implements QuartzTaskService {

	@Autowired
	QuartzTaskBO quartzTaskBO;
	
	public List<TaskDetailDTO> query(String jobgroup) throws SchedulerException {
		
		Scheduler scheduler = quartzTaskBO.getScheduler();
		GroupMatcher<JobKey> groupMatcher = null;
		if (StringUtil.isNotBlank(jobgroup))
			groupMatcher =  GroupMatcher.jobGroupStartsWith(jobgroup);
		else
			groupMatcher = GroupMatcher.anyJobGroup();
		
		Set<JobKey> jobKeys = scheduler.getJobKeys(groupMatcher);
		Iterator<JobKey> it = jobKeys.iterator();
		TaskDetailDTO taskDetailDTO = null;
		List<TaskDetailDTO> tdList = new ArrayList<TaskDetailDTO>();
		while (it.hasNext()) {
			taskDetailDTO = new TaskDetailDTO();
			JobKey jobKey = it.next();
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			
			
			taskDetailDTO.setGroup(jobDetail.getKey().getGroup());
			taskDetailDTO.setName(jobDetail.getKey().getName());
			taskDetailDTO.setDescription(jobDetail.getDescription());
			taskDetailDTO.setJobClass(jobDetail.getJobClass().getName());
			
			List triggerList = scheduler.getTriggersOfJob(jobKey);
			if (triggerList.size() > 0) {
				Trigger trigger = (Trigger) triggerList.get(0);
				
				TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				
				taskDetailDTO.setTriggerState(triggerState.name());
				
				if (trigger.getFinalFireTime() != null)
					taskDetailDTO.setFinalFireTime(DateUtil.getDateTimeString(trigger.getFinalFireTime()));
				
				if (trigger.getPreviousFireTime() != null)
					taskDetailDTO.setPreviousFireTime(DateUtil.getDateTimeString(trigger.getPreviousFireTime()));
					
				if (trigger.getNextFireTime() != null)
					taskDetailDTO.setNextFireTime(DateUtil.getDateTimeString(trigger.getNextFireTime()));
				
				if (trigger.getStartTime() != null)
					taskDetailDTO.setStartTime(DateUtil.getDateTimeString(trigger.getStartTime()));
				
				if (trigger.getEndTime() != null)
					taskDetailDTO.setEndTime(DateUtil.getDateTimeString(trigger.getEndTime()));
				
				tdList.add(taskDetailDTO);
			}
		}
		
		return tdList;
	}
	
	public void pauseTask(String name, String group) {
		quartzTaskBO.pauseTask(name, group);
	}
	
	public void resumeTask(String name, String group) {
		quartzTaskBO.resumeTask(name, group);
	}
	
	public boolean removeTask(String name, String group) {
		return quartzTaskBO.removeTask(name, group);
	}
}
