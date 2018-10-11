package com.wfzcx.ppms.prepare.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.prepare.dao.ProQualitativeUnitDaoI;
import com.wfzcx.ppms.prepare.po.ProQualitativeUnit;

@Scope("prototype")
@Repository
public class ProQualitativeUnitDaoImpl extends GenericDao<ProQualitativeUnit, Integer> implements ProQualitativeUnitDaoI {

}
