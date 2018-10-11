package com.wfzcx.ppms.prepare.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.prepare.dao.ProSolutionDaoI;
import com.wfzcx.ppms.prepare.po.ProSolution;

@Scope("prototype")
@Repository
public class ProSolutionDaoImpl extends GenericDao<ProSolution, Integer> implements ProSolutionDaoI {

}
