package com.wfzcx.ppms.execute.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.execute.dao.ProFinanceDaoI;
import com.wfzcx.ppms.execute.po.ProFinance;

@Scope("prototype")
@Repository
public class ProFinanceDaoImpl extends GenericDao<ProFinance, Integer> implements ProFinanceDaoI {

}
