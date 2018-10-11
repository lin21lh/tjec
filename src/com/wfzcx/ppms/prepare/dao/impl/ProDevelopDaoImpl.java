package com.wfzcx.ppms.prepare.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.prepare.dao.ProDevelopDaoI;
import com.wfzcx.ppms.prepare.po.ProDevelop;

@Scope("prototype")
@Repository
public class ProDevelopDaoImpl extends GenericDao<ProDevelop, Integer> implements ProDevelopDaoI{

}
