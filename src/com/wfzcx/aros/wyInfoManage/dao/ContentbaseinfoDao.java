package com.wfzcx.aros.wyInfoManage.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.wyInfoManage.po.BContentbaseinfo;

@Scope("prototype")
@Repository
public class ContentbaseinfoDao extends GenericDao<BContentbaseinfo, String>{

}
