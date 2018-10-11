/************************************************************
 * 类名：SysRoleUserResDscopeDaoImpl.java
 *
 * 类别：DAO实现类
 * 功能：数据权限条件项DAO实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-23  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.datascope.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.base.datascope.dao.SysRoleUserResDscopeDao;
import com.jbf.base.datascope.po.SysRoleUserResDscope;
import com.jbf.common.dao.impl.GenericDao;

@Scope("prototype")
@Repository
public class SysRoleUserResDscopeDaoImpl extends GenericDao<SysRoleUserResDscope, Long>
		implements SysRoleUserResDscopeDao {

}
