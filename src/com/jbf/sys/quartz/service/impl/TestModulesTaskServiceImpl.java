package com.jbf.sys.quartz.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.sys.quartz.dto.TaskDTO;
import com.jbf.sys.quartz.service.ModulesTaskService;

@Scope("prototype")
@Service("com.jbf.sys.quartz.service.impl.TestModulesTaskServiceImpl")
public class TestModulesTaskServiceImpl implements ModulesTaskService {

	@Override
	public List<TaskDTO> getTask() {
		/**
		 * 当前模块的定时任务业务逻辑
		 * getTask 没必要传入参数 因为此方法只是在系统启动是执行
		 * 
		 */
		
//		Map paramMap = new HashMap();
//		paramMap.put("billid", "122");
//		Trigger trigger = TriggerUtil.getCronTrigger("测试模块", "测试定时任务1", "22 * * * * ?");
//		TaskDTO taskDto = new TaskDTO("测试模块", "测试定时任务1", "", TestJob.class, paramMap, trigger);
//		
//		return Arrays.asList(taskDto);
		
		return new ArrayList<TaskDTO>();
	}

}
