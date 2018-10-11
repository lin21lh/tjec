/************************************************************
 * 类名：SysFileManageDao.java
 *
 * 类别：DAO接口
 * 功能：附件管理DAO接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-23  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.filemanage.dao;

import java.lang.reflect.InvocationTargetException;

import com.jbf.base.filemanage.po.SysFileManage;
import com.jbf.common.dao.IGenericDao;
import com.jbf.common.dao.PaginationSupport;


public interface SysFileManageDao extends IGenericDao<SysFileManage, Long> {

	/**
	 * 附件查询 分页
	 * @param keyid 业务数据ID
	 * @param elementcode 业务模块编码
	 * @param page 当前页
	 * @param rows 每页条数
	 * @return
	 */
	public PaginationSupport query(String keyid, String elementcode, Integer page, Integer rows) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;
	
	/**
	 * 更新
	 * @Title: updateByHql  依据HQL更新
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param hql
	 * @param @return 设定文件 
	 * @return int 返回类型 
	 * @throws
	 */
	public int updateByHql(String hql);
}
