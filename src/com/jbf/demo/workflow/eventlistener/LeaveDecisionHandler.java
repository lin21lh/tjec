package com.jbf.demo.workflow.eventlistener;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.common.WfDecisionBusinessHandler;
import com.jbf.workflow.vo.ProcessDefinitionVO;

@Component("LeaveDecisionHandler")
public class LeaveDecisionHandler implements WfDecisionBusinessHandler {

	@Override
	public String decide(String execId, String actiId, Map variables,
			SysUser user, ProcessDefinitionVO def) {
		// 由环境变量裁定路径
		if (variables.get("TASK") != null) {
			String task = (String) variables.get("TASK");
			if (task.equals("task2")) {
				return "发送至task2";
			} else {
				return "发送至task3";
			}
		}
		return null;
	}

}
