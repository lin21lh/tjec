package com.jbf.demo.workflow.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.api.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.common.security.SecureUtil;
import com.jbf.demo.workflow.dao.LeaveNoteDao;
import com.jbf.demo.workflow.po.LeaveNote;
import com.jbf.demo.workflow.service.LeaveNoteService;
import com.jbf.workflow.component.SysWorkflowManageComponent;

@Service
public class LeaveNoteServiceImpl implements LeaveNoteService {

	@Autowired
	LeaveNoteDao leaveNoteDao;

	@Autowired
	SysWorkflowManageComponent sysWorkflowManageComponent;

	@Override
	public List query() {
		return leaveNoteDao.find(" from LeaveNote order by crdate ");
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void startWf(String uuid) {
		// 查询对象数据

		// 启动流程

		// 回填流程信息到业务数据

		// 结束
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void startLeaveWorkflow(String leaveid) throws Exception {

		// LeaveNote ln = leaveNoteDao.get(leaveid);
		//
		// if (ln == null) {
		// throw new RuntimeException("请假单不存在！");
		// }
		// HashMap map = new HashMap();
		// // 放置环境变量
		// map.put("objid", ln.getUuid());
		// ProcessInstance pi = sysWorkflowManageComponent
		// .startProcessByKeyAndPush("LEAVE", map);
		//
		// if (pi == null) {
		// throw new RuntimeException("流程启动失败！");
		// }
		// System.out.println(pi.getKey());
		// System.out.println(pi.getId());
		// System.out.println(pi.getName());
		//
		// // 更新业务对象数据
		// ln.setWfid(pi.getId());
		// ln.setWfnode(sysWorkflowManageComponent.getExcutionName(pi));
		// leaveNoteDao.update(ln);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void completeTaskByExecId(String execId, String outcome,
			String vars, String opinion) throws Exception {
		String usercode = SecureUtil.getCurrentUser().getUsercode();
		// sysWorkflowManageComponent.completeTaskByExecId(execId, outcome,
		// vars,
		// usercode, opinion);
	}

	@Override
	public String getUserTodoExecidsByWfKey(String usercode, String wfkey,
			String activityId) {
		// return sysWorkflowManageComponent.getUserTodoExecidsByWfKey(usercode,
		// wfkey, activityId);
		return null;
	}

	@Override
	public String getUserHistoryExecidsByWfKey(String usercode, String wfkey,
			String activityId) {
		return sysWorkflowManageComponent.getUserHistoryExecidsByWfKey(
				usercode, wfkey, activityId);
	}
}
