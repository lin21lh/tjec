package com.wfzcx.aros.lxfs.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.lxfs.po.LxfsInfo;

@Scope("prototype")
@Repository
public class LxfsDao extends GenericDao<LxfsInfo,String>{

}
