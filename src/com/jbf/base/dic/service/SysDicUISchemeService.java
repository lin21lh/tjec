/************************************************************
 * 类名：SysDicUISchemeService.java
 *
 * 类别：Service接口
 * 功能：界面方案服务接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-06  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.tabsdef.po.SysDicUIScheme;
import com.jbf.base.tabsdef.vo.TableColumnTreeVo;
import com.jbf.common.exception.AppException;

public interface SysDicUISchemeService {

	/**
	 * 字典表界面方案
	 * @param elementcode 数据项编码
	 * @param tablecode 表编码
	 * @return 界面方案Map
	 * @throws AppException
	 * @throws IOException
	 */
    public Map<String, Object> getScheme(String elementcode, String tablecode) throws AppException, IOException;

    /**
     * 字典表form界面方案
     * @param dicElement 数据项PO
     * @param tablecode 表编码
     * @param dicUIScheme 界面PO
     * @return 字典表form界面方案 字符串
     * @throws AppException
     * @throws IOException
     */
    public String getEditScheme(SysDicElement dicElement, String tablecode, SysDicUIScheme dicUIScheme) throws AppException, IOException;
    
    /**
     * 业务表form界面方案
     * @param tablecode 表编码
     * @return 业务表form界面方案字符串
     * @throws AppException
     * @throws IOException
     */
    public String getBusinessFormScheme(String tablecode) throws AppException, IOException;

    /**
     * 获取grid界面VM
     * @return
     */
    public String getGridSchemeVM();

    /**
     * 获取grid界面
     * @param elementcode 数据项编码
     * @param tablecode 表编码
     * @param dicUIScheme 界面PO
     * @return grid界面字符串
     * @throws AppException
     * @throws IOException
     */
    public String getGridScheme(String elementcode, String tablecode, SysDicUIScheme dicUIScheme) throws AppException, IOException;

    /**
     * 树状界面
     * @param elementcode 数据项编码
     * @param tablecode 表编码
     * @param dicUIScheme 界面PO
     * @return 树状界面字符串
     * @throws AppException
     */
    public String getTreeScheme(String elementcode, String tablecode, SysDicUIScheme dicUIScheme) throws AppException;

    /**
     * 界面方案保存
     * @param dicUISchema 界面方案PO
     */
    public void saveDicUISchema(SysDicUIScheme dicUISchema);
    
    /**
     * 查找组装的Tree的列
     * @param tablecode 表编码
     * @param schemeid 界面方案ID
     * @return 数据表列TreeVo
     * @throws IOException
     */
    public List<TableColumnTreeVo> findColumnsTree(String tablecode, Long schemeid) throws IOException;
    
    /**
     * 获取datagrid界面方案
     * @param tablecode 表编码
     * @param schemeid 界面方案ID
     * @return 界面方案Map
     * @throws IOException
     */
    public Map getDatagridUIScheme(String tablecode, Long schemeid) throws IOException;
    
    /**
     * 查询界面方案
     * @param tablecode 表编码
     * @return 界面方案集合
     */
    public List<Map> queryDicUIScheme(String tablecode);
    
    /**
     * 复制界面方案
     * @param schemeid 界面ID
     * @return
     */
    public Long copyUIToTable(Long schemeid);
}
