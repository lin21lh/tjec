/************************************************************
 * 类名：DicElementController.java
 *
 * 类别：Controller
 * 功能：提供数据项管理的页面入口和增删改查功能
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-7-04  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.dic.po.SysDicElementViewFilter;
import com.jbf.base.dic.service.SysDicElementService;
import com.jbf.base.setcolumn.service.SetColumnService;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;


@Scope("prototype")
@Controller
@RequestMapping({"/base/dic/dicElementController"})
public class DicElementController {

	@Autowired
	SysDicElementService dicElementService;
	
	@Autowired
	SetColumnService setColumnService;
	
	/**
	 * 数据项设置页面
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping({ "/entry.do" })
	public ModelAndView entry(HttpServletRequest request) {
		Map modelMap = new HashMap();
		modelMap.put("today", DateUtil.getCurrentDate());
		modelMap.put("menuid", request.getParameter("menuid"));
		modelMap.put("usercode", SecureUtil.getCurrentUser().getUsercode());
		modelMap.put("columnSet", "");
		modelMap.put("columnSet", setColumnService.getColModelJsFunction("dicElementDataGrid", "dicElement", request.getParameter("menuid")));
		return new ModelAndView("/base/dic/dicDefEntry", "modelMap", modelMap);
	}
	
	/**
	 * 数据项新增界面
	 * @return
	 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/addDicDefForm.do")
	public ModelAndView addDicDefForm() {
		Map modelMap = new HashMap();
		modelMap.put("today", DateUtil.getCurrentDate());
		return new ModelAndView("/base/dic/addDicDefEntry", "modelMap", modelMap);
	}
	*/
	
	/**
	 * 基础数据项定义维护界面
	 * @return
	 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/dicDefFormEntry.do")
	public ModelAndView dicDefFormEntry(HttpServletRequest request) {
		Map modelMap = new HashMap();
		modelMap.put("today", DateUtil.getCurrentDate());
		modelMap.put("elementcode", request.getParameter("elementcode"));
		return new ModelAndView("/base/dic/dicDefFormEntry", "modelMap", modelMap);
	}
	*/
	
	/**
	 * 数据项视图维护界面
	 * @return
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/dicViewFormEntry.do")
	public ModelAndView dicViewFormEntry(HttpServletRequest request) {
		Map modelMap = new HashMap();
		modelMap.put("today", DateUtil.getCurrentDate());
		modelMap.put("elementcode", request.getParameter("elementcode"));
		return new ModelAndView("/base/dic/dicViewFormEntry", "modelMap", modelMap);
	}
	 */
	
	/**
	 * 数据项删除
	 * @param elementids 数据项ID
	 * @return ResultMsg 删除结果
	 */
	@ResponseBody
	@RequestMapping({ "/deleteDicElement.do" })
	public ResultMsg deleteDicElement(String elementids) {
		
		try{
			String msg = dicElementService.delete(elementids);
			msg = msg.length() > 0 ? msg : AppException.getMessage("crud.delok");
			return new ResultMsg(true, msg);
		}catch(Exception e){
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.delerr"));
		}
	}
	
	/**
	 * 取得所有定义的表,如果 cntablename不为空，按表中文名进行查询
	 * 
	 * @param tablename
	 *            表中文名
	 * @return 列表
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping({ "/getElementClassTree.do" })
	public List<Map> getElementClassTree() {
		return dicElementService.queryElementClass();
	}
	
	/**
	 * 基础数据项查询列表 分页
	 * @param request
	 * @return EasyUITotalResult 数据项集合
	 */
	@ResponseBody
	@RequestMapping({ "/queryPageDicElement.do" })
	public EasyUITotalResult queryPage(HttpServletRequest request) {
		
		String elementclass = request.getParameter("elementclass");
		String elementcode = request.getParameter("elementcode");
		String elementname = request.getParameter("elementname");
		String pageNumber = request.getParameter("page");
		String pageSize = request.getParameter("rows");
		
		return EasyUITotalResult.from(dicElementService.queryPage(elementclass, elementcode, elementname, pageNumber, pageSize));
	}

	/**
	 * 查询数据项
	 * @param request
	 * @return 数据项集合
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping({ "/searchDicELes.do" })
	public List<SysDicElement> searchDicELes(HttpServletRequest request) {
		Map<String, Object> params = ControllerUtil.getRequestParameterMap(request);
		return dicElementService.searchDicEles(params);
	}
	
	/**
	 * 数据权限相关数据项
	 * @return 数据项集合
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping({"/findDicElementToDataScope.do"})
	public List<Map> findDicElementToDataScope() {
		
		return dicElementService.findDicElementToDataScope();
	} 
	
	/**
	 * 数据字典基础数据项保存
	 * @param dicElement 数据字典PO
	 * @return ResultMsg 保存结果
	 */
	@ResponseBody
	@RequestMapping("/saveDicElement.do")
	public ResultMsg saveDicElement(@ModelAttribute SysDicElement dicElement){
		try{
			dicElementService.saveDicElement(dicElement);
			return new ResultMsg(true, AppException.getMessage("crud.saveok"));
		}catch(Exception e){
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
	}
	
	/**
	 * 数据字典数据项视图保存
	 * @param request 数据项视图属性参数
	 * @return ResultMsg 保存结果
	 * @throws ParseException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping("/saveDicElementView.do")
	public ResultMsg saveDicElementView(HttpServletRequest request) throws ParseException {
		
		Map paramMap = new HashMap();
		Enumeration em = request.getParameterNames();
		
		String elementviewfilters = request.getParameter("elementviewfilters");
		while (em.hasMoreElements()) {
			String key = (String) em.nextElement();
			String value = request.getParameter(key);
			if (value != null && value.length() > 0 && !"elementviewfilters".equals(key))
				paramMap.put(key, value);
		}
		try {
			dicElementService.saveDicElementView(paramMap, elementviewfilters);
			return new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
		
		
	}
	
	/**
	 * 数据项视图过滤条件
	 * @param request elementcode数据项编码
	 * @return 数据项视图过滤条件集合
	 */
	@ResponseBody
	@RequestMapping({ "/queryDicElementViewFilters.do" })
	public List<SysDicElementViewFilter> queryDicElementViewFilters(HttpServletRequest request) {
		
		String elementcode = request.getParameter("elementcode");
		
		try {
			return dicElementService.getFilters(elementcode);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<SysDicElementViewFilter>();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<SysDicElementViewFilter>();
		}
	}
	
	/**
	 * 可选择的源数据项
	 * @return 数据项集合
	 */
	@ResponseBody
	@RequestMapping({"/getSourceDicElements.do"})
	public List<SysDicElement> getSourceDicElements() {
		
		return dicElementService.getSourceDicElements();
	}
	
	/**
	 * 获取源数据项对应表的字段
	 * @param elementcode 数据项编码
	 * @return 字段集合
	 */
	@ResponseBody
	@RequestMapping({"/getColumnsByElementcode.do"})
	public List<SysDicColumn> getColumnsByElementcode(String elementcode) {
		
		if (StringUtil.isBlank(elementcode))
			return new ArrayList<SysDicColumn>();
		
		return dicElementService.getColumnsByElementcode(elementcode);
	}
}
