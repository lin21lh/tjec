package com.jbf.sys.quartz.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.jbf.common.util.StringUtil;
import com.jbf.common.util.WebContextFactoryUtil;
import com.jbf.sys.quartz.dto.TaskDTO;
import com.jbf.sys.quartz.service.ModulesTaskService;
import com.jbf.sys.quartz.service.TaskService;

public class TaskServiceImpl implements TaskService {
	
	private List<String> modulesTaskServices;
	public List<TaskDTO> findAllTasks() {
		
		List<TaskDTO> tasks = new ArrayList<TaskDTO>();
		List<TaskDTO> moduleTasks = null;
		for (String modulesTaskServiceRef : modulesTaskServices) {
			if (StringUtil.isBlank(modulesTaskServiceRef))
				continue;
			
			moduleTasks = getModuleTasks(modulesTaskServiceRef);
			if (moduleTasks != null && moduleTasks.size() > 0)
				tasks.addAll(moduleTasks);
		}
		
		return tasks;
	}
	
	public List<TaskDTO> getModuleTasks(String modulesTaskServiceRef) {
		ModulesTaskService mtService = (ModulesTaskService) WebContextFactoryUtil.getBean(modulesTaskServiceRef);
		return mtService.getTask();
	}
	
	public void setModulesTaskServices(List<String> modulesTaskServices) {
		this.modulesTaskServices = modulesTaskServices;
	}
	
}
