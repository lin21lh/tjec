/************************************************************
 * 类名：SysDeptExpcfgDao.java
 *
 * 类别：DAO接口
 * 功能：机构扩展属性配置DAO接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.dept.dao;

import java.util.List;

import com.jbf.common.dao.IGenericDao;
import com.jbf.sys.dept.po.SysDeptexpcfg;

public interface SysDeptExpcfgDao extends IGenericDao<SysDeptexpcfg, Long> {

	/**
	 * 查询机构扩展属性
	 * @param agencycat 机构类别
	 * @return 机构扩展属性集合
	 */
	public List<SysDeptexpcfg> findByAgencycat(Long agencycat);
}
