package com.jbf.workflow.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.common.exception.AppException;
import com.jbf.workflow.dao.SysWorkflowProcdefDao;
import com.jbf.workflow.po.SysWorkflowProcdef;

@Repository("com.jbf.workflow.dao.impl.SysWorkflowProcdefDaoImpl")
public class SysWorkflowProcdefDaoImpl extends
		GenericDao<SysWorkflowProcdef, Long> implements SysWorkflowProcdefDao {

	@Override
	public String getTabcodeByKey(String key) throws AppException {
		String hql = " select tab.tablecode from SysWorkflowProcdef def, SysDicTable tab where def.tabid=tab.tableid and def.key= ?";
		List list = super.getHibernateTemplate().find(hql, key);
		if (list.isEmpty())
			throw new AppException("工作流标识" + key + "未定义表");
		return (String) list.get(0);
	}

	@Override
	public String getPoClassByKey(String key) {
		String hql = " select tab.entryname from SysWorkflowProcdef def, SysDicTable tab where def.tabid=tab.tableid and def.key= ?";
		List list = super.getHibernateTemplate().find(hql, key);
		return (String) list.get(0);
	}
}
