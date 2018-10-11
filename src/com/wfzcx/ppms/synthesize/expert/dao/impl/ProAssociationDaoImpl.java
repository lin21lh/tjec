package com.wfzcx.ppms.synthesize.expert.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.synthesize.expert.dao.ProAssociationDaoI;
import com.wfzcx.ppms.synthesize.expert.po.ProAssociation;
@Scope("prototype")
@Repository
public class ProAssociationDaoImpl extends GenericDao<ProAssociation, Integer> implements ProAssociationDaoI {

}
