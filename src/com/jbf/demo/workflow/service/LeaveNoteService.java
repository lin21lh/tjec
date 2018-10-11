package com.jbf.demo.workflow.service;

import java.util.List;
import java.util.Map;

public interface LeaveNoteService {

	public List query();

	public void startLeaveWorkflow(String leaveid) throws Exception;

	public void completeTaskByExecId(String execId, String outcome, String vars,String opinion)
			throws Exception;

	public String getUserTodoExecidsByWfKey(String usercode, String wfkey,
			String activityId);

	public String getUserHistoryExecidsByWfKey(String usercode, String wfkey,
			String activityId);
}
