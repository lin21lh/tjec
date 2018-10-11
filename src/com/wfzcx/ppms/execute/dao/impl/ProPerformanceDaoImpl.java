package com.wfzcx.ppms.execute.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.execute.dao.ProPerformanceDaoI;
import com.wfzcx.ppms.execute.po.ProPerformance;

@Scope("prototype")
@Repository
public class ProPerformanceDaoImpl extends GenericDao<ProPerformance, Integer> implements ProPerformanceDaoI {

}
