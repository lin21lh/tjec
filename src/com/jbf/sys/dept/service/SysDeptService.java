/************************************************************
 * 类名：SysDeptService.java
 *
 * 类别：Service接口
 * 功能：机构管理服务接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.dept.service;

import java.util.List;
import java.util.Map;

import com.jbf.common.exception.AppException;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.dept.vo.DeptTreeVo;

public interface SysDeptService {

	/**
	 * 查询机构树
	 * @return 机构树VO集合
	 */
	public List<DeptTreeVo> queryDeptTree();
	
	/**
	 * 获取机构详细信息
	 * @param itemid 机构ID
	 * @return 机构Object
	 */
	public Object get(Long itemid);
	
	/**
	 * 机构保存
	 * @param value 机构信息Map
	 */
	public void save(Map value);
	
	/**
	 * 查询未配置的扩展属性列
	 * @param agencycat 机构类别
	 * @return 扩展属性列集合
	 */
	public List queryUnselectedExp(Long agencycat);
	
	/**
	 * 查询已配置的扩展属性列
	 * @param agencycat
	 * @return 扩展属性列集合
	 */
	public List querySelectedExp(Long agencycat);
	
	/**
	 *  机构扩展属性列配置保存
	 * @param str
	 */
	public void saveDeptExpCfg(String str);
	
	/**
	 *  机构删除
	 * @param itemid 机构ID
	 * @return ResultMsg
	 */
	public ResultMsg delete(Long itemid);
	
	/**
	 * 获取机构扩展属性配置列
	 * @param agencycat
	 * @param itemid
	 * @return
	 * @throws AppException
	 */
	public String getExpColumnsHTML(Long agencycat, Long itemid) throws AppException;
}
