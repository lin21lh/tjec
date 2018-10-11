package com.wfzcx.aros.dxal.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.dxal.po.Dxalinfo;

@Scope("prototype")
@Repository
public class DxalDao extends GenericDao<Dxalinfo,String> {

}
