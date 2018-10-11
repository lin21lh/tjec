package com.jbf.workflow.dao.impl;

import org.springframework.stereotype.Repository;
import com.jbf.common.dao.impl.GenericDao;
import com.jbf.workflow.dao.SysWorkflowExtAttrDao;
import com.jbf.workflow.po.SysWorkflowExtAttr;

@Repository("com.jbf.workflow.dao.impl.SysWorkflowExtAttrDaoImpl")
public class SysWorkflowExtAttrDaoImpl extends
		GenericDao<SysWorkflowExtAttr, Long> implements SysWorkflowExtAttrDao {

}
