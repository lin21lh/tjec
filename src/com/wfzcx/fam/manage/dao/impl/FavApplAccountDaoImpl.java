package com.wfzcx.fam.manage.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.fam.manage.dao.FavApplAccountDao;
import com.wfzcx.fam.manage.po.FavApplAccount;

@Scope("prototype")
@Repository
public class FavApplAccountDaoImpl extends GenericDao<FavApplAccount, Integer> implements FavApplAccountDao {

}
