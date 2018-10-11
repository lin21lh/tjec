package com.wfzcx.ppms.mesNotification.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.mesNotification.dao.ProMessageCbDao;
import com.wfzcx.ppms.mesNotification.po.ProMessageCb;

@Scope("prototype")
@Repository
public class ProMessageCbDaoImpl extends GenericDao<ProMessageCb, Integer> implements ProMessageCbDao{

}
