/************************************************************
 * 类名：SysDicUIDesignService.java
 *
 * 类别：Service接口
 * 功能：界面设计器Service接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.jbf.base.tabsdef.po.SysDicUIScheme;

public interface SysDicUIDesignService {
	/**
	 * 取得界面方案详情
	 * 
	 * @param schemeid 界面方案ID
	 * @return 界面方案PO
	 */
	public SysDicUIScheme get(Long schemeid);

	/**
	 * 取得表单界面方案
	 * 
	 * @param schemeid 界面方案ID
	 * @return 表单界面方案
	 * @throws IOException
	 */
	public String getDicUIFormScheme(Long schemeid) throws IOException;

	/**
	 * 保存界面方案
	 * 
	 * @param id 界面方案ID
	 * @param tablecode 表编码
	 * @param name 界面方案名称
	 * @param content 界面方案内容
	 * @throws Exception
	 */
	//public void save(String id, String tablecode, String name, String content)
		//	throws Exception;

	/**
	 * 查询表列
	 * 
	 * @param tablecode
	 * @return
	 */
	public List queryTableColumns(String tablecode);

	/**
	 * 保存界面方案
	 * 
	 * @param dicUIScheme
	 * @return
	 */
	public String saveDicUIScheme(SysDicUIScheme dicUIScheme);

	/**
	 * 删除界面方案
	 * 
	 * @param schemeid 界面方案ID
	 * @return
	 */
	public String deleteDicUIScheme(Long schemeid);

	/**
	 * 判断数据表是否存在启用的界面方案 如果存在则返回对应的信息
	 * @param tablecode 表编码
	 * @return
	 */
	public HashMap<String, Object> isExistUI(String tablecode) throws IOException;

}
