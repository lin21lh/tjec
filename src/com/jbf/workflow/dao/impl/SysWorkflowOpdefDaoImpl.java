package com.jbf.workflow.dao.impl;

import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.workflow.dao.SysWorkflowOpdefDao;
import com.jbf.workflow.po.SysWorkflowOpdef;

@Repository("com.jbf.workflow.dao.impl.SysWorkflowOpdefDaoImpl")
public class SysWorkflowOpdefDaoImpl extends GenericDao<SysWorkflowOpdef, Long>
		implements SysWorkflowOpdefDao {

}
