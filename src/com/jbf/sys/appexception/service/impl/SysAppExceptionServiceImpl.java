/************************************************************
 * 类名：SysAppExceptionServiceImpl.java
 *
 * 类别：Service实现类
 * 功能：自定义异常服务实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-12-6  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.appexception.service.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.sys.appexception.dao.SysAppExceptionDao;
import com.jbf.sys.appexception.po.SysAppException;
import com.jbf.sys.appexception.service.SysAppExceptionService;

@Scope("prototype")
@Service
public class SysAppExceptionServiceImpl implements SysAppExceptionService {

	@Autowired
	SysAppExceptionDao appExceptionDao;
	
	@Override
	public PaginationSupport query(Integer rows, Integer page) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(SysAppException.class);
		return appExceptionDao.findByCriteria(dc, rows, page);
	}

}
