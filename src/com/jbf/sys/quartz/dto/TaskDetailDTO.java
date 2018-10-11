/************************************************************
 * 类名：TaskDetailDTO.java
 *
 * 类别：DTO
 * 功能：定时任务明细DTO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-01-07  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.quartz.dto;


public class TaskDetailDTO {

	private String group; //组名
	private String name; //名称
	private String description; //描述
	private String jobClass; //工作类
	private String finalFireTime; //最后一次执行时间
	private String previousFireTime; //上次执行时间
	private String nextFireTime; //下次执行时间
	private String triggerState; //状态
	private String triggerStateCn; //状态 中文
	private String startTime; //开始时间
	private String endTime; //结束时间
	private String triggerType; //触发器类型
	
	public TaskDetailDTO() {
		
	}
	
	public TaskDetailDTO(String group, String name, String description, String jobClass, String finalFireTime,
			String nextFireTime, String triggerState, String startTime, String endTime, String triggerType) {
		this.group = group;
		this.name = name;
		this.description = description;
		this.jobClass = jobClass;
		this.finalFireTime = finalFireTime;
		this.nextFireTime = nextFireTime;
		this.triggerState = triggerState;
		this.startTime = startTime;
		this.endTime = endTime;
		this.triggerType = triggerType;
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
	public String getJobClass() {
		return jobClass;
	}
	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}
	public String getFinalFireTime() {
		return finalFireTime;
	}
	public void setFinalFireTime(String finalFireTime) {
		this.finalFireTime = finalFireTime;
	}
	public String getPreviousFireTime() {
		return previousFireTime;
	}
	public void setPreviousFireTime(String previousFireTime) {
		this.previousFireTime = previousFireTime;
	}
	public String getNextFireTime() {
		return nextFireTime;
	}
	public void setNextFireTime(String nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	public String getTriggerState() {
		return triggerState;
	}
	public void setTriggerState(String triggerState) {
		this.triggerState = triggerState;
		this.triggerStateCn = TriggerStateCn.getNameByCode(triggerState);
	}
	public String getTriggerStateCn() {
		return triggerStateCn;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}
	
	public enum TriggerStateCn {
		
		NONE(0, "NONE", "未执行"), 
		NORMAL(1, "NORMAL", "运行中"),
		PAUSED(2, "PAUSED", "暂停中"),
		COMPLETE(3, "COMPLETE", "完成"),
		ERROR(4, "ERROR", "错误"), 
		BLOCKED(5, "BLOCKED", "受阻中");
		
		private int index;
		private String code;
		private String name;
		
		public static String getNameByCode(String code) {
			
			for (TriggerStateCn tsc : TriggerStateCn.values()) {
				if (code == tsc.getCode())
					return tsc.getName();
			}
			return null;
		}
		
		private TriggerStateCn(int index, String code, String name) {
			this.index = index;
			this.code = code;
			this.name = name;
		}
		
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
}


