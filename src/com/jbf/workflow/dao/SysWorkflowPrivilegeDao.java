package com.jbf.workflow.dao;

import java.util.List;

import com.jbf.common.dao.IGenericDao;
import com.jbf.workflow.po.SysWorkflowPrivilege;

public interface SysWorkflowPrivilegeDao extends
		IGenericDao<SysWorkflowPrivilege, Long> {

	List queryByDeploymentId(String deloyid);

	List queryByKeyAndVersion(String key, Integer version);

}
