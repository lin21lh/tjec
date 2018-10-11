package com.wfzcx.ppms.prepare.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.prepare.dao.ProFinanceUnitDaoI;
import com.wfzcx.ppms.prepare.po.ProFinanceUnit;
@Scope("prototype")
@Repository
public class ProFinanceUnitDaoImpl extends GenericDao<ProFinanceUnit, Integer> implements ProFinanceUnitDaoI {

}
