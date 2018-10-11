/************************************************************
 * 类名：SysExcelImpCfgController.java
 *
 * 类别：Controller
 * 功能：提供数据导入相关配置功能页面入口和数据导入相关配置功能
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbf.base.excel.core.vo.CellVo;
import com.jbf.base.excel.core.vo.ListVo;
import com.jbf.base.excel.core.vo.SheetVo;
import com.jbf.base.excel.po.SysExcelImpCfg;
import com.jbf.base.excel.service.SysExcelImpCfgService;
import com.jbf.common.exception.AppException;
import com.jbf.common.web.ResultMsg;

@Controller
@RequestMapping("/base/excel/SysExcelImpCfgController")
public class SysExcelImpCfgController {

	@Autowired
	SysExcelImpCfgService sysExcelImpCfgService;

	/**
	 * 页面入口
	 * 
	 * @return 页面入口地址
	 */
	@RequestMapping("/impCfgEntry.do")
	public String entry() {
		return "base/excel/dataImpCfgEntry";
	}

	/**
	 * 配置模板页面入口
	 * 
	 * @return
	 */
	@RequestMapping("/dataImpCfgTmplFormEntry.do")
	public String formEntry() {
		return "base/excel/dataImpCfgTmplFormEntry";
	}

	/**
	 * 配置模板中的sheet列表配置页面入口
	 * 
	 * @return
	 */
	@RequestMapping("/dataImpCfgSheetDetailEntry.do")
	public String dataImpCfgSheetDetailEntry() {
		return "base/excel/dataImpCfgSheetDetailEntry";
	}

	/**
	 * 查询数据导入配置列表
	 * 
	 * @param cfgcategory
	 *            配置模板类别查询条件
	 * @param cfgname
	 *            配置模板名称查询条件
	 * @return 数据导入配置列表
	 */
	@ResponseBody
	@RequestMapping("/queryDataImpCfg.do")
	public List queryDataImpCfg(String cfgcategory, String cfgname) {
		return sysExcelImpCfgService.queryDataImpCfg(cfgcategory, cfgname);
	}

	/**
	 * 加载数据导入配置详情
	 * 
	 * @param id
	 *            配置模板id
	 * @return 数据导入配置详情
	 */
	@ResponseBody
	@RequestMapping("/loadDataImpCfg.do")
	public SysExcelImpCfg loadDataImpCfg(Long id) {
		return sysExcelImpCfgService.loadDataImpCfgForForm(id);
	}

	/**
	 * 删除配置模板
	 * 
	 * @param id
	 *            配置模板id
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/delDataImpCfg.do")
	public ResultMsg delDataImpCfg(Long id) {
		try {
			sysExcelImpCfgService.delDataImpCfg(id);
			return new ResultMsg(true, AppException.getMessage("crud.delok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.delerr"));
		}
	}

	/**
	 * 修改对象配置模板
	 * 
	 * @param cfg
	 *            配置模板详情
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/editDataImpCfg.do")
	public ResultMsg editDataImpCfg(@ModelAttribute SysExcelImpCfg cfg) {
		try {
			sysExcelImpCfgService.editDataImpCfg(cfg);
			return new ResultMsg(true, AppException.getMessage("crud.editok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.editerr"));
		}
	}

	/**
	 * 添加对象配置模板
	 * 
	 * @param cfg
	 *            配置模板详情
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/addDataImpCfg.do")
	public ResultMsg addDataImpCfg(@ModelAttribute SysExcelImpCfg cfg) {
		try {
			sysExcelImpCfgService.addDataImpCfg(cfg);
			return new ResultMsg(true, AppException.getMessage("crud.addok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.adderr"));
		}
	}

	/**
	 * 查询所有配置对应的sheet
	 * 
	 * @param cfgid
	 * @return 所有配置对应的sheet
	 */
	@ResponseBody
	@RequestMapping("/querySheets.do")
	public List<SheetVo> querySheets(Long cfgid) {
		return sysExcelImpCfgService.querySheets(cfgid);
	}

	/**
	 * 在配置模板中添加sheet页
	 * 
	 * @param vo
	 *            SheetVo配置详情
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/addSheet.do")
	public ResultMsg addSheet(@ModelAttribute SheetVo vo) {
		try {
			sysExcelImpCfgService.addSheet(vo);
			return new ResultMsg(true, AppException.getMessage("crud.addok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.adderr"));
		}
	}

	/**
	 * 修改配置模板中的sheet页
	 * 
	 * @param vo
	 *            SheetVo配置详情
	 * @param oldid
	 *            原sheetvoid
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/updateSheet.do")
	public ResultMsg updateSheet(@ModelAttribute SheetVo vo, String oldid) {
		try {
			sysExcelImpCfgService.updateSheet(vo, oldid);
			return new ResultMsg(true, AppException.getMessage("crud.editok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.editerr"));
		}
	}

	/**
	 * 删除sheet页
	 * 
	 * @param id
	 *            sheet页id
	 * @param cfgid
	 *            配置模板id
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/delSheet.do")
	public ResultMsg delSheet(String id, Long cfgid) {
		try {
			sysExcelImpCfgService.delSheet(id, cfgid);
			return new ResultMsg(true, AppException.getMessage("crud.delok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.delerr"));
		}
	}

	/**
	 * 配置模板中工作表中的列表的查询
	 * 
	 * @param cfgid
	 *            配置模板id
	 * @param sheetid
	 *            工作表id
	 * @return 配置模板中工作表中的列表
	 */
	@ResponseBody
	@RequestMapping("/querySheetList.do")
	public List querySheetList(String cfgid, String sheetid) {
		return sysExcelImpCfgService.querySheetList(cfgid, sheetid);
	}

	/**
	 * 配置模板中工作表中的列表的添加
	 * 
	 * @param cfgid
	 *            配置模板id
	 * @param sheetid
	 *            工作表id
	 * @param vo
	 *            列表详情
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/addSheetList.do")
	public ResultMsg addSheetList(String cfgid, String sheetid,
			@ModelAttribute ListVo vo) {
		try {
			sysExcelImpCfgService.addSheetList(cfgid, sheetid, vo);
			return new ResultMsg(true, AppException.getMessage("crud.addok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.adderr"));
		}
	}

	/**
	 * 配置模板中工作表中的列表的修改
	 * 
	 * @param cfgid
	 *            配置模板id
	 * @param sheetid
	 *            工作表id
	 * @param vo
	 *            列表详情
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/editSheetList.do")
	public ResultMsg editSheetList(String cfgid, String sheetid,
			@ModelAttribute ListVo vo) {
		try {
			sysExcelImpCfgService.editSheetList(cfgid, sheetid, vo);
			return new ResultMsg(true, AppException.getMessage("crud.editok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.editerr"));
		}
	}

	/**
	 * 配置模板中工作表中的列表的删除
	 * 
	 * @param cfgid
	 *            配置模板id
	 * @param sheetid
	 *            工作表id
	 * @param vo
	 *            列表详情
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/delSheetList.do")
	public ResultMsg delSheetList(String cfgid, String sheetid, String id) {
		try {
			sysExcelImpCfgService.delSheetList(cfgid, sheetid, id);
			return new ResultMsg(true, AppException.getMessage("crud.delok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.delerr"));
		}
	}

	/**
	 * 工作表中列表中的字段查询
	 * 
	 * @param cfgid
	 *            配置模板id
	 * @param sheetid
	 *            工作表id
	 * @param typeid
	 *            列表id
	 * @param typecode
	 *            列表code
	 * @param vo
	 *            字段vo详情
	 * @return 列表中的字段列表
	 */
	@ResponseBody
	@RequestMapping("/querySheetCol.do")
	public List querySheetCol(String cfgid, String sheetid, String typeid,
			String typecode, @ModelAttribute CellVo vo) {
		return sysExcelImpCfgService.querySheetCol(cfgid, sheetid, typeid,
				typecode, vo);
	}

	/**
	 * 工作表中列表中的字段修改
	 * 
	 * @param cfgid
	 *            配置模板id
	 * @param sheetid
	 *            工作表id
	 * @param typeid
	 *            列表id
	 * @param vo
	 *            字段vo详情
	 * 
	 * @param flag
	 *            操作标志，add为新增， edit为修改
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/editSheetCol.do")
	public ResultMsg editSheetCol(String cfgid, String sheetid, String typeid,
			@ModelAttribute CellVo vo, String flag) {
		try {
			sysExcelImpCfgService
					.editSheetCol(cfgid, sheetid, typeid, vo, flag);
			if ("add".equals(flag)) {
				return new ResultMsg(true,
						AppException.getMessage("crud.addok"));
			} else {
				return new ResultMsg(true,
						AppException.getMessage("crud.editok"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			if ("add".equals(flag)) {
				return new ResultMsg(false,
						AppException.getMessage("crud.adderr"));
			} else {
				return new ResultMsg(false,
						AppException.getMessage("crud.editerr"));
			}
		}
	}

	/**
	 * 删除字段
	 * 
	 * @param cfgid
	 *            配置模板id
	 * @param sheetid
	 *            工作表id
	 * @param typeid
	 *            列表id
	 * 
	 * @param typecode
	 *            列表code
	 * @param fieldname
	 *            字段名
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delSheetCol.do")
	public ResultMsg delSheetCol(String cfgid, String sheetid, String typeid,
			String typecode, String fieldname) {
		try {
			sysExcelImpCfgService.delSheetCol(cfgid, sheetid, typeid, typecode,
					fieldname);
			return new ResultMsg(true, AppException.getMessage("crud.delok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.delerr"));
		}
	}
}
