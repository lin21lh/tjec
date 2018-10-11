package com.wfzcx.ppms.synthesize.expert.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.synthesize.expert.dao.ProExpertDaoI;
import com.wfzcx.ppms.synthesize.expert.po.ProExpert;

@Scope("prototype")
@Repository
public class ProExpertDaoImpl extends GenericDao<ProExpert, Integer> implements ProExpertDaoI {

}
