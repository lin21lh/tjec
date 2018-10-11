/************************************************************
 * 类名：ScopeItemParseComponent.java
 *
 * 类别：组件类
 * 功能：组装数据权限项条件
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-4-01  CFIT-PM   maqs         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.datascope.component;

import com.jbf.base.datascope.po.SysDatascopeitem;
import com.jbf.common.exception.AppException;

public interface ScopeItemParseComponent {

	/**
	 * 拼装SQL语句
	 * @param scopeitem 权限 条件
	 * @param tableAlias 表别名
	 * @param tableCode 表编码
	 * @param userid 用户ID
	 * @param ScopeType 应用字段类型
	 * @return
	 * @throws AppException
	 */
    public String getSql(SysDatascopeitem scopeitem, String tableAlias, String tableCode, Long userid, int scopeType) throws AppException;
    
    /**
     * 查找字段对象（依据表编码和源数据项编码）
     * @param tableCode 数据表编码
     * @param sourceElement 源数据项编码
     * @return 字段对象
     * @throws AppException
     */
    public String findColumnBySourceElement(String tableCode, String sourceElement) throws AppException;
}
