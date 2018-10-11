package com.jbf.sys.quartz.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobDataMap dataMap = context.getMergedJobDataMap();
		context.getJobDetail().getDescription();
		String billid = (String) dataMap.get("billid");
		
		System.err.println("组：" + context.getJobDetail().getKey().getGroup() + "，名称：" + context.getJobDetail().getKey().getName()  + "；传入参数：billid="  + billid + "定时任务开始执行！");
		System.err.println("/** what to do */");
	}

}
