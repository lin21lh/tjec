package com.wfzcx.aros.wtcl.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.wtcl.po.BWebquesbaseinfo;

@Scope("prototype")
@Repository
public class BWebquesbaseinfoDAO extends GenericDao<BWebquesbaseinfo, Integer> {

}
