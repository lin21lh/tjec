package com.jbf.workflow.dao.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.jbf.common.dao.impl.GenericDao;
import com.jbf.workflow.dao.SysWorkflowPrivilegeDao;
import com.jbf.workflow.po.SysWorkflowPrivilege;

@Repository("com.jbf.workflow.dao.impl.SysWorkflowPrivaligeDaoImpl")
public class SysWorkflowPrivaligeDaoImpl extends
		GenericDao<SysWorkflowPrivilege, Long> implements
		SysWorkflowPrivilegeDao {

	@Override
	public List queryByDeploymentId(String deloyid) {
		String hql = " select  new  com.jbf.workflow.vo.WorkflowPrivilegeVO(r.roleid, r.rolename, p.activityname) "
				+ "from SysWorkflowPrivilege p,SysRole r where p.roleid=r.id and p.deploymentid=?";
		return this.getHibernateTemplate().find(hql, deloyid);
	}

	@Override
	public List queryByKeyAndVersion(String key, Integer version) {
		String hql = " select  new  com.jbf.workflow.vo.WorkflowPrivilegeVO(r.roleid, r.rolename, p.activityname) "
				+ "from SysWorkflowPrivilege p,SysRole r where p.roleid=r.id and p.key=? and p.version=?";
		return this.getHibernateTemplate().find(hql, key, version);
	}

}
