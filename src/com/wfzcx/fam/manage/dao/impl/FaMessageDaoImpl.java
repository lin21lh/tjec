package com.wfzcx.fam.manage.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.fam.manage.dao.FaMessageDao;
import com.wfzcx.fam.manage.po.FaMessage;

@Scope("prototype")
@Repository("fam.manage.dao.impl.FaMessageDao")
public class FaMessageDaoImpl extends GenericDao<FaMessage, Integer> implements FaMessageDao {

}
