/************************************************************
 * 类名：SysExcelImpCfgService
 *
 * 类别：Service
 * 功能：数据导入配置Service
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-29  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.service;

import java.util.List;

import com.jbf.base.excel.core.vo.CellVo;
import com.jbf.base.excel.core.vo.ListVo;
import com.jbf.base.excel.core.vo.SheetVo;
import com.jbf.base.excel.po.SysExcelImpCfg;

public interface SysExcelImpCfgService {

	/**
	 * 查询配置模板
	 * 
	 * @param cfgcategory
	 *            配置类型
	 * @param cfgname
	 *            配置名称
	 * @return 配置模板列表
	 */
	List queryDataImpCfg(String cfgcategory, String cfgname);

	/**
	 * 加载配置模板详情
	 * 
	 * @param id
	 *            配置模板id
	 * @return 配置模板详情
	 */
	SysExcelImpCfg loadDataImpCfgForForm(Long id);

	/**
	 * 删除配置模板
	 * 
	 * @param id
	 *            配置模板id
	 * @throws Exception
	 */
	void delDataImpCfg(Long id) throws Exception;

	/**
	 * 修改配置模板
	 * 
	 * @param cfg
	 * @throws Exception
	 */
	void editDataImpCfg(SysExcelImpCfg cfg) throws Exception;

	/**
	 * 添加配置模板
	 * 
	 * @param cfg
	 *            配置模板详情
	 * @throws Exception
	 */
	void addDataImpCfg(SysExcelImpCfg cfg) throws Exception;

	/**
	 * 查询配置模板中的工作表
	 * 
	 * @param cfgid
	 * @return 工作表列表
	 */
	List<SheetVo> querySheets(Long cfgid);

	/**
	 * 在配置模板添加工作表
	 * 
	 * @param vo
	 * @throws Exception
	 */
	void addSheet(SheetVo vo) throws Exception;

	/**
	 * 在配置模板修改工作表
	 * 
	 * @param vo
	 * @param oldid
	 * @throws Exception
	 */
	void updateSheet(SheetVo vo, String oldid) throws Exception;

	/**
	 * 在配置模板删除工作表
	 * 
	 * @param id
	 * @param cfgid
	 * @throws Exception
	 */
	void delSheet(String id, Long cfgid) throws Exception;

	/**
	 * 在工作表中添加列表
	 * 
	 * @param cfgid
	 * @param sheetid
	 * @param vo
	 * @throws Exception
	 */
	void addSheetList(String cfgid, String sheetid, ListVo vo) throws Exception;

	/**
	 * 在工作表中查询列表
	 * 
	 * @param cfgid
	 * @param sheetid
	 * @return
	 */
	List querySheetList(String cfgid, String sheetid);

	/**
	 * 在工作表中修改列表
	 * 
	 * @param cfgid
	 * @param ownersheetid
	 * @param typeid
	 * @param vo
	 * @param flag
	 * @throws Exception
	 */
	void editSheetCol(String cfgid, String ownersheetid, String typeid,
			CellVo vo, String flag) throws Exception;

	/**
	 * 在工作表中修改列表
	 * 
	 * @param cfgid
	 * @param sheetid
	 * @param vo
	 * @throws Exception
	 */
	void editSheetList(String cfgid, String sheetid, ListVo vo)
			throws Exception;

	/**
	 * 
	 * @param cfgid
	 * @param sheetid
	 * @param id
	 * @throws Exception
	 */
	void delSheetList(String cfgid, String sheetid, String id) throws Exception;

	/**
	 * 在列表中删除列
	 * 
	 * @param cfgid
	 * @param sheetid
	 * @param typeid
	 * @param typecode
	 * @param fieldname
	 * @throws Exception
	 */
	void delSheetCol(String cfgid, String sheetid, String typeid,
			String typecode, String fieldname) throws Exception;

	/**
	 * 在列表中查询列
	 * 
	 * @param cfgid
	 * @param sheetid
	 * @param typeid
	 * @param typecode
	 * @param vo
	 * @return 查询到的列
	 */
	List querySheetCol(String cfgid, String sheetid, String typeid,
			String typecode, CellVo vo);
}
