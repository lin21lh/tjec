/************************************************************
 * 类名：CalculateDatascopeBO
 *
 * 类别：组件类
 * 功能：获取数据权限
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-4-01  CFIT-PM   maqs         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.datascope;

import com.jbf.base.datascope.component.CalculateDatascopeComponent;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.WebContextFactoryUtil;
import com.jbf.sys.user.po.SysUser;


public class ConditionFilter  {

	
	static CalculateDatascopeComponent calculateDatascopeCp;
	
	private static void init() {
		if (calculateDatascopeCp == null)
			calculateDatascopeCp = (CalculateDatascopeComponent) WebContextFactoryUtil.getBean("base.datascope.component.CalculateDatascopeComponent");
	}
	
	/**
	 * 获取配置数据权限
	 * 返回空代表没有配置数据权限；超级管理员可以查看全部数据，不受数据权限约束（1=1）返回sql格式为：( (not) exist select 1 from xxxx where a=b)
	 * @param resourceid 菜单ID
	 * @param tablecode 主表编码
	 * @param tableAlias 主表别名
	 * @param scopeType （以字典表ID或CODE字段为基准）
	 * @return
	 * @throws AppException
	 */
	public static String getDataScope(Long resourceid, String tablecode, String tableAlias, int scopeType) throws AppException {
		return calculateDataRight(resourceid, SecureUtil.getCurrentUser().getUserid(), tableAlias, tablecode, scopeType);
	}

	private static String calculateDataRight(Long resourceid, String tableAlias, String tablecode, int scopeType) throws AppException {
		
		SysUser user = SecureUtil.getCurrentUser();
		if (user == null)
			throw new AppException("当前用户未获取到！");
		
		return calculateDataRight(resourceid, user.getUserid(), tableAlias, tablecode, scopeType);
	}
	
	private static String calculateDataRight(Long resourceid, Long userid, String tableAlias, String tablecode, int scopeType) throws AppException {
		init();
		return calculateDatascopeCp.calculateDataRight(resourceid, userid, tableAlias, tablecode, scopeType).toString();
	}
	
	public static String calculateDataRightByElementcode(Long resourceid, String elementcode, String tableAlias, String tablecode, int scopeType) throws AppException {
		init();
		SysUser user = SecureUtil.getCurrentUser();
		if (user == null)
			throw new AppException("当前用户未获取到！");
		return calculateDatascopeCp.calDataRightByElementcode(resourceid, elementcode, user.getUserid(), tableAlias, tablecode, scopeType);
	}
	
	/**
	 * 判断是否配置数据权限
	 * @Title: isHasDataRight 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param resourceid 资源（菜单）ID
	 * @param @param elementcode 数据项编码 如果数据项编码为null 则只判断有无配置数据权限
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return boolean 返回类型 
	 * @throws
	 */
	public static boolean isHasDataRight(Long resourceid, String elementcode) throws AppException {
		SysUser user = SecureUtil.getCurrentUser();
		if (user == null)
			throw new AppException("当前用户未获取到！");
		
		return isHasDataRight(resourceid, user.getUserid(), elementcode);
	}
	
	/**
	 * 判断是否配置数据权限
	 * @Title: isHasDataRight 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param resourceid 资源（菜单）ID
	 * @param @param userid 登录用户ID
	 * @param @param elementcode 数据项编码 如果数据项编码为null 则只判断有无配置数据权限
	 * @param @return 设定文件 
	 * @return boolean 返回类型 
	 * @throws
	 */
	public static boolean isHasDataRight(Long resourceid, Long userid, String elementcode) {
		init();
		return calculateDatascopeCp.isHasDataRight(resourceid, userid,elementcode);
	}
}
