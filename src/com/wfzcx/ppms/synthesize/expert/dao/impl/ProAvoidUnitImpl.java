package com.wfzcx.ppms.synthesize.expert.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.synthesize.expert.dao.ProAvoidUnitDaoI;
import com.wfzcx.ppms.synthesize.expert.po.ProAvoidUnit;

@Scope("prototype")
@Repository
public class ProAvoidUnitImpl  extends GenericDao<ProAvoidUnit, Integer> implements ProAvoidUnitDaoI {

}
