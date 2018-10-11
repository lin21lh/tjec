package com.jbf.workflow.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.workflow.dao.SysWorkflowDeccondsDao;
import com.jbf.workflow.po.SysWorkflowDecConds;

@Repository("com.jbf.workflow.dao.impl.SysWorkflowDeccondsDaoImpl")
public class SysWorkflowDeccondsDaoImpl extends GenericDao<SysWorkflowDecConds, Long> implements SysWorkflowDeccondsDao {

	public SysWorkflowDecConds getWfDecCond(String wfkey, Integer wfversion, String decisionname, String taskname) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(SysWorkflowDecConds.class);
		dc.add(Property.forName("wfkey").eq(wfkey));
		dc = dc.add(Property.forName("wfversion").eq(wfversion));
		dc = dc.add(Property.forName("decisionname").eq(decisionname));
		dc = dc.add(Property.forName("taskname").eq(taskname));
		
		List<SysWorkflowDecConds> list = (List<SysWorkflowDecConds>)findByCriteria(dc);
		
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	public List<SysWorkflowDecConds> finWfDecConds(String wfkey, Integer wfversion, String decisionname) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysWorkflowDecConds.class);
		dc.add(Property.forName("wfkey").eq(wfkey));
		dc = dc.add(Property.forName("wfversion").eq(wfversion));
		dc = dc.add(Property.forName("decisionname").eq(decisionname));
		
		List<SysWorkflowDecConds> list = (List<SysWorkflowDecConds>)findByCriteria(dc);
		return list;
	}
}
