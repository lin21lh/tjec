package com.wfzcx.ppp.xmkf.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppp.xmkf.po.TKfYzfs;

@Scope("prototype")
@Repository
public class ProYzfsDao extends GenericDao<TKfYzfs, Integer>{

}
