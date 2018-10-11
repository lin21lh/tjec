/************************************************************
 * 类名：SysResourceOperService.java
 *
 * 类别：Service
 * 功能：资源操作服务
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.resource.service;

import java.util.List;

import com.jbf.sys.resource.po.SysResourceOper;

public interface SysResourceOperService {

	/**
	 * 查询资源操作详情
	 * 
	 * @param id
	 *            资源操作id
	 * @return 资源操作详情
	 */
	public SysResourceOper get(Long id);

	/**
	 * 保存资源操作
	 * 
	 * @param sysResourceOper
	 *            资源操作
	 */
	public void save(SysResourceOper sysResourceOper);

	/**
	 * 删除资源操作
	 * 
	 * @param id
	 *            资源操作id
	 * @throws Exception
	 */
	public void delete(Long id) throws Exception;



	/**
	 * 查询资源未选的系统预定义的资源操作
	 * 
	 * @param resourceid
	 *            资源id
	 * @return 资源未选的系统预定义的资源操作列表
	 */
	public List queryPresetOper(Long resourceid);

	/**
	 * 查询资源的所有操作
	 * 
	 * @param resourceid
	 *            资源id
	 * @return  资源的所有操作列表
	 */
	public List queryOper(Long resourceid);

	/**
	 * 保存资源操作
	 * 
	 * @param oper
	 *            资源操作详情
	 * @throws Exception
	 */
	public void saveResourceOper(SysResourceOper oper) throws Exception;

}
