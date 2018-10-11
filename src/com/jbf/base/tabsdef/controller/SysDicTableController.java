/************************************************************
 * 类名：SysDicTableController.java
 *
 * 类别：Controller
 * 功能：数据表controller
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbf.base.dic.service.SysDicUISchemeService;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.base.tabsdef.service.SysDicTableService;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;

@Scope("prototype")
@Controller
@RequestMapping("/base/tabsdef/SysDicTableController")
public class SysDicTableController {

	@Autowired
	SysDicTableService sysDicTableService;

	@Autowired
	SysDicUISchemeService dicUISchemeService;

	@RequestMapping({ "/entry.do" })
	public String entry() throws ServletException {
		return "base/tabsdef/tabsDefEntry";
	}

	/**
	 * 字段定义弹出框页面
	 * 
	 * @return
	 */
	@RequestMapping({ "/colsDefDialog.do" })
	public String colsDefDialog() {
		return "base/tabsdef/colsDefDialog";
	}

	/**
	 * 查询数据表
	 * 
	 * @param request
	 * @return
	 * @throws AppException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/query.do")
	@ResponseBody
	public EasyUITotalResult query(HttpServletRequest request, HttpServletResponse response) throws AppException {
		EasyUITotalResult easyUiRs = null;
			Map<String, Object> params = ControllerUtil
					.getRequestParameterMap(request);
			easyUiRs = EasyUITotalResult.from(sysDicTableService.query(params));
			
		return easyUiRs;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/searchTables.do")
	@ResponseBody
	public List<SysDicTable> searchTables(HttpServletRequest request) {
		Map<String, Object> params = ControllerUtil
				.getRequestParameterMap(request);
		return sysDicTableService.searchTables(params);
	}

	/**
	 * 取得数据表的详情
	 * 
	 * @param tableid
	 * @return 数据表详情
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public SysDicTable get(Long tableid) {
		return sysDicTableService.get(tableid);
	}

	/**
	 * 修改数据表记录
	 * 
	 * @param dicTable
	 *            数据表对象
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/edit.do")
	@ResponseBody
	public ResultMsg edit(SysDicTable tab) {
		ResultMsg rsMsg = null;
		Long id;
		try {
			id = sysDicTableService.edit(tab);
			rsMsg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
			HashMap m = new HashMap();
			m.put("id", id);
			rsMsg.setBody(m);
		} catch (Exception e) {

			e.printStackTrace();
			if (e instanceof AppException) {
				rsMsg = new ResultMsg(false, e.getMessage());
			} else
				rsMsg = new ResultMsg(false,
						AppException.getMessage("crud.saveerr"));
		}
		return rsMsg;
	}

	/**
	 * 删除数据表记录
	 * 
	 * @param tableid
	 *            数据表ID
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public ResultMsg delete(Long tableid) {
		ResultMsg rsMsg = null;
		try {
			sysDicTableService.delete(tableid);
			rsMsg = new ResultMsg(true, AppException.getMessage("crud.delok"));
		} catch (Exception e) {
			e.printStackTrace();
			rsMsg = new ResultMsg(true, AppException.getMessage("crud.delerr"));
		}
		return rsMsg;
	}

	/**
	 * 查询所有的数据表
	 * 
	 * @return 数据表列表
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryAllTables.do")
	@ResponseBody
	public List queryAllTables(String tablecode, String tablename) {

		return sysDicTableService.queryAllTables(tablecode, tablename);
	}

	/**
	 * 查询复制所用的数据表
	 * 
	 * @return 数据表列表
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryTabsToCopyCol.do")
	@ResponseBody
	public List queryTabsToCopyCol(String tablecode, String tablename) {

		return sysDicTableService.queryTabsToCopyCol(tablecode, tablename);
	}
}
