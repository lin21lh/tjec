/************************************************************
 * 类名：SysDeptDao.java
 *
 * 类别：DAO接口
 * 功能：机构DAO接口
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
import com.jbf.sys.dept.po.SysDept;


public interface SysDeptDao extends IGenericDao<SysDept, Long> {
	
	/**
	 * 机构列表
	 * @return 机构PO集合
	 */
	public List<SysDept> query();
	
	/**
	 * 获取机构详细信息
	 * @param agencycode 机构编码
	 * @return 机构PO
	 */
	public SysDept get(String agencycode);
}
