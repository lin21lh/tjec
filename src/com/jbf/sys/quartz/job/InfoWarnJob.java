package com.jbf.sys.quartz.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jbf.common.util.WebContextFactoryUtil;
import com.wfzcx.aros.xxtx.service.BMsgbaseinfoService;

/**
 * 消息提醒定时任务执行类
 * @author zhaoxd
 *
 */
public class InfoWarnJob implements Job {
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// 打印执行日志
		JobDataMap dataMap = context.getMergedJobDataMap();
		context.getJobDetail().getDescription();
		String billid = (String) dataMap.get("billid");
		System.out.println("组：" + context.getJobDetail().getKey().getGroup() + 
				"，名称：" + context.getJobDetail().getKey().getName()  + 
				"；传入参数：billid="  + billid + "定时任务开始执行！");
		BMsgbaseinfoService service = (BMsgbaseinfoService) WebContextFactoryUtil.getBean("com.wfzcx.aros.xxtx.service.BMsgbaseinfoService");
		service.overdueCase(null);
	}

}
