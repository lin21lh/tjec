package com.wfzcx.ppms.procurement.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.procurement.dao.ProNegotiationGroupDaoI;
import com.wfzcx.ppms.procurement.po.ProNegotiationGroup;

@Scope("prototype")
@Repository
public class ProNegotiationGroupDaoImpl extends GenericDao<ProNegotiationGroup, Integer> implements ProNegotiationGroupDaoI {

}
