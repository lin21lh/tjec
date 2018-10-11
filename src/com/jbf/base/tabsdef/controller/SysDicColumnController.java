/************************************************************
 * 类名：SysDicColumnController.java
 *
 * 类别：Controller
 * 功能：数据表字段controller
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbf.base.dic.service.SysDicUISchemeService;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.base.tabsdef.service.SysDicColumnService;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.exception.AppException;
import com.jbf.common.web.ResultMsg;

@Scope("prototype")
@Controller
@RequestMapping("/base/tabsdef/SysDicColumnController")
public class SysDicColumnController {

	@Autowired
	SysDicColumnService dicColumnSerivice;

	@Autowired
	private SysDicUISchemeService dicUISchemaService;

	@RequestMapping("/tabsDefDialog.do")
	public String tabsDefDialog() {
		return "base/tabsdef/tabsDefForm";
	}

	@RequestMapping("/colsDefDialog.do")
	public String colsDefDialog() {
		return "base/tabsdef/colsDefForm";
	}

	/**
	 * 查询数据表的列
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/query.do")
	@ResponseBody
	public EasyUITotalResult query(HttpServletRequest request) {
		EasyUITotalResult easyUiRs = null;
		try {
			String tablecode = request.getParameter("tablecode");
			String page = request.getParameter("page");
			String rows = request.getParameter("rows");

			Integer pageNumber = Integer.valueOf(page == null ? "1" : page);
			Integer pageSize = Integer.valueOf(rows == null ? "100" : rows);
			easyUiRs = EasyUITotalResult.from(dicColumnSerivice.query(
					tablecode, pageNumber, pageSize));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return easyUiRs;
	}

	/**
	 * 取得数据表的列详情
	 * 
	 * @param id
	 *            列id
	 * @return 列详情
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public SysDicColumn get(Long id) {
		return dicColumnSerivice.get(id);

	}

	/**
	 * 修改列详情
	 * 
	 * @param dicColumn
	 * @return 成功标志
	 */
	@RequestMapping("/edit.do")
	@ResponseBody
	public ResultMsg edit(SysDicColumn dicColumn) {
		ResultMsg rsMsg = null;
		try {
			dicColumnSerivice.edit(dicColumn);
			rsMsg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AppException) {
				rsMsg = new ResultMsg(false, e.getMessage());
			} else {
				rsMsg = new ResultMsg(false,
						AppException.getMessage("crud.saveerr"));
			}
		}
		return rsMsg;
	}

	/**
	 * 删除列详情
	 * 
	 * @param columnid
	 * @return 成功标志
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public ResultMsg delete(Long columnid) {
		ResultMsg rsMsg = null;
		try {
			dicColumnSerivice.delete(columnid);
			rsMsg = new ResultMsg(true, AppException.getMessage("crud.delok"));
		} catch (Exception e) {
			e.printStackTrace();
			rsMsg = new ResultMsg(false, AppException.getMessage("crud.delerr"));
		}
		return rsMsg;
	}

	/**
	 * 取得表字段编码格式 主要用于层码的校验
	 * 
	 * @param elementcode
	 * @return 表字段编码格式
	 */
	@ResponseBody
	@RequestMapping({ "/getTableColumnFormat.do" })
	public Map<String, String> getTableColumnFormat(String tableName,
			String columnName) {
		Map<String, String> map = new HashMap<String, String>();
		// map.put("codeFormat", dicColumnSerivice.getTableColumnFormat(
		// tableName.toUpperCase(), columnName.toUpperCase()));
		return map;
	}

	/**
	 * 取得表的的编码字段信息
	 * 
	 * @param tablecode
	 *            表名
	 * @return 表的的编码字段信息
	 */
	@ResponseBody
	@RequestMapping({ "/getCodeColumnDetail.do" })
	public SysDicColumn getCodeColumnDetail(String tablecode) {
		return dicColumnSerivice.getCodeColumnDetail(tablecode);
	}

	/**
	 * 查询物理表的列
	 * 
	 * @param tablecode
	 *            数据表名
	 * @param page
	 *            页索引
	 * @param rows
	 *            每页数据条数
	 * @return 数据表的列
	 */
	@ResponseBody
	@RequestMapping("/queryDbTableColumns.do")
	public EasyUITotalResult queryDbTableColumns(String tablecode,
			Integer page, Integer rows) {
		EasyUITotalResult easyUiRs = null;
		try {

			Integer pageNumber = page == null ? 1 : page;
			Integer pageSize = rows == null ? 100 : rows;
			easyUiRs = EasyUITotalResult.from(dicColumnSerivice
					.queryDbTableColumns(tablecode, pageNumber, pageSize));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return easyUiRs;
	}

	/**
	 * 表列复制
	 * 
	 * @param sourceTablecode
	 *            源表
	 * @param targetTablecode
	 *            目标表
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping({ "/copyColToTargetTab.do" })
	public ResultMsg copyColToTargetTab(String sourceTablecode,
			String targetTablecode) {

		ResultMsg remsg = null;
		try {
			dicColumnSerivice.copyColToTargetTable(sourceTablecode,
					targetTablecode);
			remsg = new ResultMsg(true, "复制成功！");
			return remsg;
		} catch (Exception e) {
			remsg = new ResultMsg(false, "复制失败，失败原因：" + e.getMessage() + "！");
			return remsg;
		}
	}
}
