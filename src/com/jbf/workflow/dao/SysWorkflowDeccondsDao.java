package com.jbf.workflow.dao;

import java.util.List;

import com.jbf.common.dao.IGenericDao;
import com.jbf.workflow.po.SysWorkflowDecConds;

public interface SysWorkflowDeccondsDao extends IGenericDao<SysWorkflowDecConds, Long> {

	/**
	 * 获取工作流执行条件
	 * @param wfkey
	 * @param wfversion
	 * @param decisionname
	 * @param taskname
	 * @return
	 */
	public SysWorkflowDecConds getWfDecCond(String wfkey, Integer wfversion, String decisionname, String taskname);
	
	/**
	 * 获取工作流Decision 所有
	 * @param wfkey
	 * @param wfversion
	 * @param decisionname
	 * @return
	 */
	public List<SysWorkflowDecConds> finWfDecConds(String wfkey, Integer wfversion, String decisionname);
}
