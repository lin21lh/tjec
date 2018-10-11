package com.wfzcx.aros.xzys.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.xzys.po.Respreviewinfo;

@Scope("prototype")
@Repository
public class RespreviewinfoDao extends GenericDao<Respreviewinfo, String> {

}
