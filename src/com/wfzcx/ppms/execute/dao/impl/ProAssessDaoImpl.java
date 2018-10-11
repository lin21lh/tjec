package com.wfzcx.ppms.execute.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.execute.dao.ProAssessDaoI;
import com.wfzcx.ppms.execute.po.ProAssess;

@Scope("prototype")
@Repository
public class ProAssessDaoImpl extends GenericDao<ProAssess, Integer> implements ProAssessDaoI {

}
