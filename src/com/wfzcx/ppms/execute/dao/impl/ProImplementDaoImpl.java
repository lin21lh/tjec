package com.wfzcx.ppms.execute.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.execute.dao.ProImplementDaoI;
import com.wfzcx.ppms.execute.po.ProImplement;

@Scope("prototype")
@Repository
public class ProImplementDaoImpl extends GenericDao<ProImplement, Integer> implements ProImplementDaoI{

}
