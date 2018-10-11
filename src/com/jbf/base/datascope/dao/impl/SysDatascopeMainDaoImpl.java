/************************************************************
 * 类名：SysDatascopeMainDaoImpl.java
 *
 * 类别：DAO实现类
 * 功能：数据权限主表DAO实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-23  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.datascope.dao.impl;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.base.datascope.dao.SysDatascopeMainDao;
import com.jbf.base.datascope.po.SysDatascopemain;
import com.jbf.common.TableNameConst;
import com.jbf.common.dao.impl.GenericDao;

@Scope("prototype")
@Repository
public class SysDatascopeMainDaoImpl extends GenericDao<SysDatascopemain, Long> implements SysDatascopeMainDao {

	@Override
	public Long saveDatascopeMain(SysDatascopemain datascopemain) {
		return (Long) super.save(datascopemain);
	}

	@Override
	public List findDataScopeMain() {
		String sql = "select scopemainid id, scopemainname text, 0 parentid, 1 isleaf, 1 levelno from " + TableNameConst.SYS_DATASCOPEMAIN + " where type='DATASCOPE' order by scopemainid";
		return super.findMapBySql(sql);
	}

}
