package com.jbf.workflow.dao;

import com.jbf.common.dao.IGenericDao;
import com.jbf.common.exception.AppException;
import com.jbf.workflow.po.SysWorkflowProcdef;

public interface SysWorkflowProcdefDao extends IGenericDao<SysWorkflowProcdef, Long> {

	String getTabcodeByKey(String key)  throws AppException;

	String getPoClassByKey(String key);

}
