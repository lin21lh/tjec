package com.jbf.demo.workflow.eventlistener;

import org.springframework.stereotype.Component;

import com.jbf.workflow.common.BussinessDataStatusInjectionHandler;

@Component("com.jbf.demo.workflow.eventlistener.LeaveStatusInjectionHandler")
public class LeaveStatusInjectionHandler implements
		BussinessDataStatusInjectionHandler {

	@Override
	public void inject(String key, String verison, String actiId,
			String transition, String execId, String status,
			String handleUserName) throws Exception {

		System.out.println("将要写入的数据为:");
		System.out.println("流程实例:" + execId);
		System.out.println("节点:" + actiId);
		System.out.println("将要写入的数据为:"+status);
	}

}
