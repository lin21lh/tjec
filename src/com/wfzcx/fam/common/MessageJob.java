package com.wfzcx.fam.common;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.jbf.common.util.WebContextFactoryUtil;

public class MessageJob  implements Job {
	@Autowired
	MessageComponent mcComponent;
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
 		try {
 			if (mcComponent == null) {
 				mcComponent = (MessageComponent) WebContextFactoryUtil.getBean("com.wfzcx.fam.common.MessageComponent");
 			}
 			List zhList = mcComponent.getSendMessage();
 			mcComponent.sendMessage(zhList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}

}
