/************************************************************
 * 类名：SysAppExceptionDao.java
 *
 * 类别：DAO实现类
 * 功能：自定义异常DAO实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-12-6  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.appexception.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.sys.appexception.dao.SysAppExceptionDao;
import com.jbf.sys.appexception.po.SysAppException;

@Scope("prototype")
@Repository
public class SysAppExceptionDaoImpl extends GenericDao<SysAppException, Long> implements SysAppExceptionDao {

}
