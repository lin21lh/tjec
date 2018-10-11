package com.jbf.sys.regist.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.sys.regist.dao.ProSocialRegistDaoI;
import com.jbf.sys.regist.po.ProSocialRegist;

@Scope("prototype")
@Repository
public class ProSocialRegistDaoImpl extends GenericDao<ProSocialRegist, Integer> implements ProSocialRegistDaoI {

}
