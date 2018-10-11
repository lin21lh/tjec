/************************************************************
 * 类名：DicElementValSetController.java
 *
 * 类别：Controller
 * 功能：提供数据项值集管理的页面入口和增删改查功能
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-7-04  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.base.dic.dto.CodeType;
import com.jbf.base.dic.service.DicElementValSetService;
import com.jbf.base.dic.service.SysDicElementService;
import com.jbf.base.dic.service.SysDicUISchemeService;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;

@Scope("prototype")
@Controller
@RequestMapping({"/base/dic/dicElementValSetController"})
public class DicElementValSetController {

	@Autowired
	DicElementValSetService dicElementValSetService;
	
	@Autowired
	SysDicUISchemeService dicUISchemeService;
	
	@Autowired
	SysDicElementService dicElementService;
	

	/**
	 * 数据项值集维护界面
	 * @param request elementcode 数据项编码 tablecode 表编码
	 * @return ModelAndView
	 * @throws AppException
	 * @throws IOException
	 */
	@RequestMapping({"/entry.do"})
	public ModelAndView elementValSetEntry(HttpServletRequest request) throws AppException, IOException  {
		
		String elementcode = request.getParameter("elementcode");
		String tablecode = request.getParameter("tablecode");
		Map<String, Object> modelMap = null;
		modelMap = dicUISchemeService.getScheme(elementcode, tablecode);
		String viewPage = "/base/dic/elementValSetEntry";
		switch ((CodeType)modelMap.get("codetype")) {
		case NoneCode:
			viewPage = "/base/dic/elementValSetEntry";
			break;
		case OrderCode:
			viewPage = "/base/dic/elementValSetEntry";
			break;
		case LayerCode:
			viewPage = "/base/dic/elementTreeValSetEntry";
			break;
		}

		return new ModelAndView(viewPage, "modelMap", modelMap);
	}
	
	/**
	 * 查询顺序码数据项值集
	 * @param request 查询参数
	 * @return EasyUITotalResult数据项值集集合
	 * @throws ParseException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 */
	@ResponseBody
	@RequestMapping({ "/queryDicElementVals.do" })
	public EasyUITotalResult queryDicElementVals(HttpServletRequest request) throws ParseException, SecurityException, ClassNotFoundException, NoSuchFieldException {
		Enumeration em = request.getParameterNames();
		Map paramMap = new HashMap();
		while (em.hasMoreElements()) {
			String key = (String) em.nextElement();
			String value = request.getParameter(key);
			if (value != null && value.length() > 0) {
				paramMap.put(key, value);
			}
		}
		
		return dicElementValSetService.queryPageDicElementVals(paramMap);
	}
	
	/**
	 * 层码值集查询
	 * @param request elementcode 数据项编码
	 * @return 层码数据项集合
	 * @throws NoSuchFieldException 
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 * @throws AppException 
	 */
	@ResponseBody
	@RequestMapping({ "/queryDicTreeElementVals.do" })
	public List<Object> queryDicTreeElementVals(HttpServletRequest request) throws SecurityException, ClassNotFoundException, NoSuchFieldException, AppException {
		String elementcode = request.getParameter("elementcode");
		String byElementcode = request.getParameter("byElementcode");
		String valueModel = request.getParameter("valueModel");
		String textModel = request.getParameter("textModel");
		String menuid = request.getParameter("menuid");
		String customSql = request.getParameter("customSql");
		
		return dicElementValSetService.queryDicTreeElementVals(elementcode, byElementcode, valueModel, textModel, menuid, customSql, false, false);
	}
	
	/**
	 * 数据权限范围内 层码值集查询
	 * @param request
	 * @return
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws AppException
	 */
	@RequestMapping({ "/queryDicTreeElementValsByCFilter.do" })
	@ResponseBody
	public List<Object> queryDicTreeElementValsByCFilter(HttpServletRequest request) throws SecurityException, ClassNotFoundException, NoSuchFieldException, AppException {
		String elementcode = request.getParameter("elementcode");
		String byElementcode = request.getParameter("byElementcode");
		String valueModel = request.getParameter("valueModel");
		String textModel = request.getParameter("textModel");
		String menuid = request.getParameter("menuid");
		String customSql = request.getParameter("customSql");
		
		return dicElementValSetService.queryDicTreeElementVals(elementcode, byElementcode, valueModel, textModel, menuid, customSql, false, true);
	}
	
	/**
	 * 数据权限查询数据项值集
	 * @param elementcode 数据项编码
	 * @param scopeitemid 数据权限条件ID
	 * @param scopevalues 数据权限值集ID
	 * @param currenttdid 当前td的ID
	 * @return 数据项值集集合
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping({"/queryDicToDataScope.do"})
	public Map<String, Object> queryDicToDataScope(String elementcode, Long scopeitemid, String currenttdid) throws NumberFormatException, Exception {
		
		return dicElementValSetService.queryDicValsToDataScope(elementcode, scopeitemid, currenttdid);
	}
	
	/**
	 * 顺序码数据项值集保存
	 * @param request 顺序码数据项值集属性参数
	 * @return ResultMsg 保存结果
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping("/saveElementVal.do")
	public ResultMsg saveElementVal(HttpServletRequest request) throws ParseException {
		
		Map paramMap = new HashMap();
		Enumeration em = request.getParameterNames();
		
		while (em.hasMoreElements()) {
			String key = (String) em.nextElement();
			String value = request.getParameter(key);
			if (value != null && value.length() > 0) {
				paramMap.put(key, value);	
			}	
		}
		try {
			dicElementValSetService.saveDicElementVal(paramMap);
			return new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
	}
	
	/**
	 * 层码数据项值集保存
	 * @param request 层码数据项值集属性参数
	 * @return ResultMsg 保存结果
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping({ "/saveTreeElementVal.do" })
	public ResultMsg saveTreeElementVal(HttpServletRequest request) throws ParseException {
		
		Map paramMap = new HashMap();
		Enumeration em = request.getParameterNames();
		
		while (em.hasMoreElements()) {
			String key = (String) em.nextElement();
			String value = request.getParameter(key);
			if (value != null && value.length() > 0) {
				paramMap.put(key, value);
			}	
		}
		try {
			dicElementValSetService.saveDicElementTreeVal(paramMap);
			return new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
	}
	
	/**
	 * 枚举项值集查询 通过elementcode
	 * @param request elementcode 数据项编码
	 * @return 数据项值集集合
	 */
	@ResponseBody
	@RequestMapping("/findEnumDicELementVals.do")
	public List<Map> findDicElementVals(HttpServletRequest request) {
		
		String elementcode = request.getParameter("elementcode");
		
		return dicElementValSetService.findDicElementVals(elementcode);
	} 
	
	/**
	 * 获取数据项值集明细
	 * @param tablecode 表编码
	 * @param id 数据项值集ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDetailById.do")
	public Object getDetailById(String tablecode, Long id) {
		
		return dicElementValSetService.getDetailByID(tablecode, id);
	}
	
	/**
	 * 数据项值集删除
	 * @param ids 数据项值集ID
	 * @param tablecode 表编码
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteDicElementVals.do")
	public ResultMsg deleteDicElementVals(HttpServletRequest request) {
		
		ResultMsg resultMsg = null;
		String ids = request.getParameter("ids");
		String tablecode = request.getParameter("tablecode");
		String msg = dicElementValSetService.deleteDicElementVal(ids, tablecode);
		if (ids == null || ids.length() == 0)
			return new ResultMsg(false, AppException.getMessage("crud.delerr"));
		
		if (StringUtil.isBlank(tablecode))
			return new ResultMsg(false, AppException.getMessage("crud.delerr"));
			
		if (msg.length() == 0)
			resultMsg = new ResultMsg(true, AppException.getMessage("crud.delok"));
		else
			resultMsg = new ResultMsg(false, msg);
		
		return resultMsg;
	}
	
	/**
	 * 层码数据项值集删除
	 * @param id 数据项值集ID
	 * @param elementcode 数据项编码
	 * @return ResultMsg
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/deleteDicTreeElementVals.do")
	public ResultMsg deleteDicTreeElementVals(HttpServletRequest request) {
		ResultMsg resultMsg = null;
		String msg;
		Long id = Long.valueOf(request.getParameter("id"));
		String elementcode = request.getParameter("elementcode");
		try {
			msg = dicElementValSetService.deleteDicTreeElementVal( request, id, elementcode);
			if (msg.length() > 0)
				resultMsg = new ResultMsg(false, msg);
			else
				resultMsg = new ResultMsg(true, AppException.getMessage("crud.delok"));
		} catch (Exception e) {
			resultMsg = new ResultMsg(false, e.getMessage());
			e.printStackTrace();
		}		
		return resultMsg;
	}
	
	/**
	 * 通过数据项编码查询值集
	 * @param request elementcode 数据项编码 idColumn id列  textColumn text 显示列
	 * @return 数据项值集集合
	 * @throws AppException
	 */
	@ResponseBody
	@RequestMapping("/queryByElementcode.do")	
	public List queryComboByElementcode(HttpServletRequest request) throws AppException {
		
		String elementcode = request.getParameter("elementcode");
		String idColumn = request.getParameter("idColumn");
		String textColumn = request.getParameter("textColumn");
		String menuid = request.getParameter("menuid");
		String byElementcode = request.getParameter("byElementcode");
		String customSql = request.getParameter("customSql");
		
		return dicElementValSetService.queryByElementcode(menuid, elementcode, byElementcode, idColumn, textColumn, customSql, false, false);
	}
	
	/**
	 * 查询数据权限范围内的 通过数据项编码查询值集
	 * @param request elementcode 数据项编码 idColumn id列  textColumn text 显示列
	 * @return 数据项值集集合
	 * @throws AppException
	 */
	@ResponseBody
	@RequestMapping("/queryCbByElementcodeByCFilter.do")	
	public List queryCbByElementcodeByCFilter(HttpServletRequest request) throws AppException {
		
		String elementcode = request.getParameter("elementcode");
		String idColumn = request.getParameter("idColumn");
		String textColumn = request.getParameter("textColumn");
		String menuid = request.getParameter("menuid");
		String byElementcode = request.getParameter("byElementcode");
		String customSql = request.getParameter("customSql");
		
		return dicElementValSetService.queryByElementcode(menuid, elementcode, byElementcode, idColumn, textColumn, customSql, true, false);
	}
}
