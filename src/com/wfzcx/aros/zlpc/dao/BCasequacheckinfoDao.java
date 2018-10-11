package com.wfzcx.aros.zlpc.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.zlpc.po.BCasequacheckinfo;

@Scope("prototype")
@Repository
public class BCasequacheckinfoDao extends GenericDao<BCasequacheckinfo, String>{

}
