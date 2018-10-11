package com.wfzcx.ppms.procurement.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.procurement.dao.ProProcurementExpertDaoI;
import com.wfzcx.ppms.procurement.po.ProProcurementExpert;
@Scope("prototype")
@Repository
public class ProProcurementExpertDaoImpl extends GenericDao<ProProcurementExpert, Integer> implements ProProcurementExpertDaoI {

}
