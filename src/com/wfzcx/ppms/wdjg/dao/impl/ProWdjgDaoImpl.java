package com.wfzcx.ppms.wdjg.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.wdjg.dao.ProWdjgDao;
import com.wfzcx.ppms.wdjg.po.ProWdjg;

@Scope("prototype")
@Repository
public class ProWdjgDaoImpl extends GenericDao<ProWdjg, Integer> implements ProWdjgDao{

}
