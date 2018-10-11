package com.jbf.sys.quartz.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Trigger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.sys.quartz.dto.TaskDTO;
import com.jbf.sys.quartz.job.InfoWarnJob;
import com.jbf.sys.quartz.service.ModulesTaskService;
import com.jbf.sys.quartz.trigger.TriggerUtil;

/**
 * 消息提醒信息采集定时任务
 * @author Administrator
 *
 */
@Scope("prototype")
@Service("com.jbf.sys.quartz.service.impl.InfoWarnModulesTaskServiceImpl")

public class InfoWarnModulesTaskServiceImpl implements ModulesTaskService {

	@Override
	public List<TaskDTO> getTask() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("billid", "开始执行消息提醒定时任务");
		Trigger trigger = TriggerUtil.getCronTrigger("消息提醒", "消息提醒信息采集", "0 0 1 * * ?");
		TaskDTO taskDto = new TaskDTO("消息提醒", "消息提醒信息采集", "", InfoWarnJob.class, paramMap, trigger);
		return Arrays.asList(taskDto);
	}
}
