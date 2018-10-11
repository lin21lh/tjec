package com.wfzcx.aros.ajpj.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.ajgz.po.BCasetracebaseinfo;
import com.wfzcx.aros.ajpj.po.BCaseestbaseinfo;

@Scope("prototype")
@Repository
public class BCaseestbaseinfoDAO extends GenericDao<BCaseestbaseinfo, String> {

}
