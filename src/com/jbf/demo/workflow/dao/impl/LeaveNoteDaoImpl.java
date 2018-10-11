package com.jbf.demo.workflow.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.demo.workflow.dao.LeaveNoteDao;
import com.jbf.demo.workflow.po.LeaveNote;

@Scope("prototype")
@Repository
public class LeaveNoteDaoImpl extends GenericDao<LeaveNote, String> implements
		LeaveNoteDao {

}
