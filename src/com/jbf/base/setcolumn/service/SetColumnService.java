/************************************************************
 * 类名：SetColumnService.java
 *
 * 类别：Service接口
 * 功能：列设置服务接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.setcolumn.service;

public interface SetColumnService {

	/**
	 * 保存列设置
	 * @param colopts 列属性
	 * @param filename 文件名称
	 * @param menuid 功能菜单ID
	 * @return 保存列结果
	 */
	public boolean saveColumnSet(String colopts, String filename, String menuid);
	
	/**
	 * 获取保存列
	 * @param datagrid datagrid ID
	 * @param filename 文件名称
	 * @param menuid 功能菜单ID
	 * @return
	 */
	public String getColModelJsFunction(String datagrid, String filename,String menuid);
	
	/**
	 * 还原列
	 * @param filename 文件名称
	 * @param menuid 功能菜单ID
	 */
	public boolean deleteColSetJsFile(String filename,String menuid);
}
