package com.wfzcx.ppms.library.dsfjgk.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.library.dsfjgk.dao.ProDsfjgkDao;
import com.wfzcx.ppms.library.dsfjgk.po.ProDsfjgk;

@Scope("prototype")
@Repository
public class ProDsfjgkDaoImpl extends GenericDao<ProDsfjgk, Integer> implements ProDsfjgkDao {

}
