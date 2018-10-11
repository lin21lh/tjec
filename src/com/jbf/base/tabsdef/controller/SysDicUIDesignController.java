/************************************************************
 * 类名：SysDicUIDesignController.java
 *
 * 类别：Controller
 * 功能：界面设计器controller
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.base.dic.service.SysDicUISchemeService;
import com.jbf.base.tabsdef.po.SysDicUIScheme;
import com.jbf.base.tabsdef.service.SysDicUIDesignService;
import com.jbf.base.tabsdef.vo.TableColumnTreeVo;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;

@Scope("prototype")
@Controller
@RequestMapping("/base/tabsdef/SysDicUIDesignController")
public class SysDicUIDesignController {

	@Autowired
	SysDicUISchemeService dicUISchemeService;

	@Autowired
	SysDicUIDesignService dicUIDesignService;

	@Autowired
	SysDicUIDesignService sysDicUIDesignService;

	/**
	 * 保存界面方案
	 * 
	 * @param dicUIScheme
	 *            界面方案详情
	 * @return 成功标志
	 */
	@RequestMapping("/saveDicUIScheme.do")
	@ResponseBody
	public ResultMsg saveDicUIScheme(SysDicUIScheme dicUIScheme) {
		ResultMsg reMsg = null;
		String msg = dicUIDesignService.saveDicUIScheme(dicUIScheme);
		reMsg = new ResultMsg(true, msg);
		return reMsg;
	}

	/**
	 * 删除界面方案
	 * 
	 * @param schemeid
	 *            界面方案id
	 * @return
	 */
	@RequestMapping("/deleteDicUIScheme.do")
	@ResponseBody
	public ResultMsg deleteDicUIScheme(Long schemeid) {
		ResultMsg reMsg = null;
		String msg = dicUIDesignService.deleteDicUIScheme(schemeid);
		if (msg.length() > 0)
			reMsg = new ResultMsg(false, msg);
		else
			reMsg = new ResultMsg(true, AppException.getMessage("crud.delok"));
		return reMsg;
	}

	/**
	 * 查询数据表的界面方案
	 * 
	 * @param tablecode
	 *            表名
	 * @return
	 */
	@RequestMapping({ "/queryDicUIScheme.do" })
	@ResponseBody
	public List<Map> queryDicUIScheme(String tablecode) {

		List<Map> dicUISchemes = dicUISchemeService.queryDicUIScheme(tablecode);

		return dicUISchemes;
	}

	/**
	 * 取得数据表某一界面方案组装的Tree的字段列
	 * @param request tablecode 表编码  schemeid 界面方案ID
	 * @return 组装Tree字段列集合
	 * @throws IOException
	 */
	@RequestMapping({ "/getColumns.do" })
	@ResponseBody
	public List<TableColumnTreeVo> getColumns(HttpServletRequest request)
			throws IOException {

		String tablecode = request.getParameter("tablecode");
		String schemeidStr = request.getParameter("schemeid");
		Long schemeid = StringUtil.isNotBlank(schemeidStr) ? Long
				.valueOf(schemeidStr) : null;
		return dicUISchemeService.findColumnsTree(tablecode, schemeid);
	}

	/**
	 * 设计器界面入口
	 * 
	 * @param tablecode
	 *            数据表
	 * @param uischemaid
	 *            界面方案id
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping({ "/formDesignerEntry.do" })
	public ModelAndView formDesignerEntry(String tablecode, Long uischemaid) {
		HashMap map = new HashMap();
		map.put("tablecode", tablecode);
		map.put("uischemaid", uischemaid);
		return new ModelAndView("base/tabsdef/designer/UIDesignerPage", map);
	}

	/**
	 * 取得界面方案内容
	 * @param request schemeid 界面方案ID
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping({ "/getUiSchemaContent.do" })
	public void getUiSchemaContent(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String schemeid = request.getParameter("schemeid");
		response.setCharacterEncoding("UTF-8");
		String xml = "";
		if (StringUtil.isNotBlank(schemeid)) {
			xml = dicUIDesignService.getDicUIFormScheme(Long.valueOf(schemeid));
		}
		if (xml.length() > 0) {
			ServletOutputStream os;
			try {
				os = response.getOutputStream();
				OutputStreamWriter w = new OutputStreamWriter(os, "UTF-8");
				w.write(xml);
				w.flush();
				os.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 查询数据表所定义的字段
	 * @param tablecode 表编码
	 * @return 数据表定义字段集合
	 */
	@ResponseBody
	@RequestMapping({ "/queryTableColumns.do" })
	public List queryTableColumns(String tablecode) {
		return sysDicUIDesignService.queryTableColumns(tablecode);
	}

	/**
	 * 获取列表界面方案
	 * @param tablecode 表编码
	 * @param schemeid 界面方案ID
	 * @return Map 列表界面相关信息 
	 * @throws IOException
	 */
	@RequestMapping({ "/getDatagridUIDesign.do" })
	@ResponseBody
	public Map getDatagridUIDesign(String tablecode, Long schemeid)
			throws IOException {

		return dicUISchemeService.getDatagridUIScheme(tablecode, schemeid);
	}

	/**
	 * Datagrid执行后台刷新
	 * @return
	 */
	@RequestMapping({ "/getDatagrid.do" })
	@ResponseBody
	public List<Map> getDatagrid() {

		return new ArrayList<Map>();
	}

	/**
	 * 复制界面方案
	 * @param request schemeid 界面方案ID
	 * @return 新复制的界面方案ID
	 */
	@RequestMapping({ "/copyUIToTable.do" })
	@ResponseBody
	public Long copyUIToTable(HttpServletRequest request) {

		String schemeidStr = request.getParameter("schemeid");

		return dicUISchemeService.copyUIToTable(Long.valueOf(schemeidStr));
	}

	/**
	 * 判断是否存在界面方案
	 * 
	 * @param request
	 * @return 是否配置启用界面方案标志
	 */
	@RequestMapping({ "/isExistUI.do" })
	@ResponseBody
	public ResultMsg isExistUI(HttpServletRequest request) {
		String tablecode = request.getParameter("tablecode");
		HashMap<String, Object> map;
		try {
			map = dicUIDesignService.isExistUI(tablecode);
			boolean isExist = (Boolean)map.get("isExist");
			String title = isExist ? "" : "该数据项对应的表[" + tablecode + "]未配置启用的界面方案！";
			return new ResultMsg(isExist, title, map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResultMsg(false, "");
		}

	}
}
