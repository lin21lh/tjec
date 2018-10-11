package com.wfzcx.ppms.prepare.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.prepare.dao.ProQualitativeExpertDaoI;
import com.wfzcx.ppms.prepare.po.ProQualitativeExpert;
@Scope("prototype")
@Repository
public class ProQualitativeExpertDaoImpl extends GenericDao<ProQualitativeExpert, Integer> implements ProQualitativeExpertDaoI {

}
