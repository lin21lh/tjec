/************************************************************
 * 类名：TaskDTO.java
 *
 * 类别：DTO
 * 功能：定时任务DTO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-01-07  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.quartz.dto;

import java.util.Map;

import org.quartz.Trigger;


public class TaskDTO {

	private String group; //组
	private String name; //名称
	private String description; //描述
	private Class jobClass; //工作类 what to do
	private Map paramMap; //需要参数
	private Trigger trigger; //触发器 when to do
	
	public TaskDTO() {
		
	}
	
	public TaskDTO(String group, String name, String description, Class jobClass, Trigger trigger) {
		this.group = group;
		this.name = name;
		this.description = description;
		this.jobClass = jobClass;
		this.trigger = trigger;
	}
	
	public TaskDTO(String group, String name, String description, Class jobClass, Map paramMap, Trigger trigger) {
		this.group = group;
		this.name = name;
		this.description = description;
		this.jobClass = jobClass;
		this.paramMap = paramMap;
		this.trigger = trigger;
	}
	
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Class getJobClass() {
		return jobClass;
	}
	public void setJobClass(Class jobClass) {
		this.jobClass = jobClass;
	}
	public Map getParamMap() {
		return paramMap;
	}
	public void setParamMap(Map paramMap) {
		this.paramMap = paramMap;
	}
	public Trigger getTrigger() {
		return trigger;
	}
	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}
}
