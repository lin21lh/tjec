package com.jbf.workflow.dao.impl;

import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.workflow.dao.SysWorkflowBackattrDao;
import com.jbf.workflow.po.SysWorkflowBackattr;

@Repository("com.jbf.workflow.dao.impl.SysWorkflowBackattrDaoImpl")
public class SysWorkflowBackattrDaoImpl extends
		GenericDao<SysWorkflowBackattr, Long> implements SysWorkflowBackattrDao {

}
