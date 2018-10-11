/************************************************************
 * 类名：SysAppExceptionDao.java
 *
 * 类别：DAO接口
 * 功能：自定义异常DAO接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-12-6  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.appexception.dao;

import com.jbf.common.dao.IGenericDao;
import com.jbf.sys.appexception.po.SysAppException;

public interface SysAppExceptionDao extends IGenericDao<SysAppException, Long> {

}
