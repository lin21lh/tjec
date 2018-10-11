package com.wfzcx.ppms.procurement.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.procurement.dao.ProPurchaseResultDaoI;
import com.wfzcx.ppms.procurement.po.ProPurchaseResult;

@Scope("prototype")
@Repository
public class ProPurchaseResultDaoImpl extends GenericDao<ProPurchaseResult, Integer> implements ProPurchaseResultDaoI {

}
