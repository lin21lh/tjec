/************************************************************
 * 类名：DicElementComponent.java
 *
 * 类别：组件接口
 * 功能：数据项组件接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-14  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.component;

import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.exception.AppException;

public interface DicElementComponent {
	
	/**
	 * 获取数据项SQL
	 * @param elementCode 数据项编码
	 * @param filter 过滤条件
	 * @return
	 * @throws AppException
	 */
    public String getElementCodeSql(String elementCode, String filter) throws AppException;
    
    /**
     * 查找源数据项
     * @param elementCode 数据项编码
     * @return 数据项PO
     * @throws AppException
     */
    public SysDicElement findPhysicalElement(String elementCode) throws AppException;
    
    /**
     * 查找数据项
     * @param elementcode 数据项编码
     * @return 数据项PO
     */
    public SysDicElement findElementByCode(String elementcode);
    
    /**
     * 获取数据项视图过滤条件
     * @param elementcode 数据项编码
     * @param tablecode 表编码
     * @return 过滤条件字符串
     * @throws AppException
     */
    public String getFilters(String elementcode, String tablecode) throws AppException;

    /**
     * 获取字段对象
     * @param tablecode 表编码
     * @param columncode 字段编码
     * @return 字段对象
     */
    public SysDicColumn getDicColumn(String tablecode, String columncode);
    
    /**
     * 获取字段对象
     * @param tablecode 表编码
     * @param sourceElement 源数据项编码
     * @return 字段对象
     * @throws AppException
     */
    public SysDicColumn getDicColumnBySourceElement(String tablecode, String sourceElement) throws AppException;
    
    /**
     * 获取数据项对象
     * @param elementcode 数据项编码
     * @return 数据项对象
     */
    public SysDicElement getDicElement(String elementcode);
    
    /**
     * 根据表编码获取数据表对象
     * @param tablecode 表编码
     * @return 数据表对象
     */
    public SysDicTable getDicTableByTablecode(String tablecode);
    
    /**
     * 根据数据项编码获取表对象
     * @param elementcode 数据项编码
     * @return 表对象
     */
    public SysDicTable getDicTableByElementcode(String elementcode);
}
