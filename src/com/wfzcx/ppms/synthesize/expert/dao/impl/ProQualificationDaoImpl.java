package com.wfzcx.ppms.synthesize.expert.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.synthesize.expert.dao.ProQualificationDaoI;
import com.wfzcx.ppms.synthesize.expert.po.ProQualification;

@Scope("prototype")
@Repository
public class ProQualificationDaoImpl extends GenericDao<ProQualification, Integer> implements ProQualificationDaoI {

}
