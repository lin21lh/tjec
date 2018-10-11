package com.wfzcx.ppms.mesNotification.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.mesNotification.dao.ProMessageZbDao;
import com.wfzcx.ppms.mesNotification.po.ProMessageZb;

@Scope("prototype")
@Repository
public class ProMessageZbDaoImpl extends GenericDao<ProMessageZb, Integer> implements ProMessageZbDao{

}
