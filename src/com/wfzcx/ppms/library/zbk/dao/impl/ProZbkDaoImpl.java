package com.wfzcx.ppms.library.zbk.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.library.zbk.dao.ProZbkDao;
import com.wfzcx.ppms.library.zbk.po.ProZbk;


@Scope("prototype")
@Repository
public class ProZbkDaoImpl extends GenericDao<ProZbk, Integer> implements ProZbkDao{

}
