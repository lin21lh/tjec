/************************************************************
 * 类名：TriggerUtil.java
 *
 * 类别：工具类
 * 功能：quartz 触发器工具类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-01-07  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.quartz.trigger;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

public class TriggerUtil {

	/**
	 * 获取触发器 SimpleTrigger
	 * @param name 触发器名称
	 * @param group 触发器组
	 * @param minutes 每个多少分钟触发一次
	 * @param count 触发次数 0 表示无限次触发
	 * @return
	 */
	public static Trigger getSimpleTrigger(String name, String group, int minutes, int count) {
		TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
		TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity(triggerKey);
		SimpleScheduleBuilder ssdb;
		if (count > 0) {
			ssdb = SimpleScheduleBuilder.repeatMinutelyForTotalCount(count, minutes);
		}else {
			ssdb = SimpleScheduleBuilder.repeatMinutelyForever(minutes);
		}
		
//		if (count != -1)
//			ssdb.withRepeatCount(count);
		
		triggerBuilder.withSchedule(ssdb);
		return triggerBuilder.build();
	}
	
	/**
	 * 获取触发器 CronTrigger
	 * @param name 触发器名称
	 * @param group 触发器组
	 * @param cronExpression 配置表达式
	 * @return
	 */
	public static Trigger getCronTrigger(String name, String group, String cronExpression) {
		
		if (CronExpression.isValidExpression(cronExpression)) { //验证表达式
			TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
			CronTrigger cronTrigger = TriggerBuilder.newTrigger().
					withIdentity(triggerKey).withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
			
			return cronTrigger;
		} else {
			return null;
		}
	}
}
