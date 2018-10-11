package com.wfzcx.ppms.synthesize.expert.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.synthesize.expert.dao.ProExpertWorkedDaoI;
import com.wfzcx.ppms.synthesize.expert.po.ProExpertWorked;

@Scope("prototype")
@Repository
public class ProExpertWorkedImpl extends GenericDao<ProExpertWorked, Integer> implements ProExpertWorkedDaoI {

}
