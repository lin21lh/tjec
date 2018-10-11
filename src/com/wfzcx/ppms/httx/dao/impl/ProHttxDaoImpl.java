package com.wfzcx.ppms.httx.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.httx.dao.ProHttxDao;
import com.wfzcx.ppms.httx.po.ProHttx;


@Scope("prototype")
@Repository
public class ProHttxDaoImpl extends GenericDao<ProHttx, Integer> implements ProHttxDao{

}
