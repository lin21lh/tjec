/************************************************************
 * 类名：SysDeptController.java
 *
 * 类别：Controller
 * 功能：提供机构管理的页面入口和增删改查功能
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.dept.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.base.dic.service.DicElementValSetService;
import com.jbf.base.dic.service.SysDicElementService;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.DateUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.dept.service.SysDeptService;
import com.jbf.sys.dept.vo.DeptTreeVo;

@Scope("prototype")
@Controller
@RequestMapping({"/sys/dept/sysDeptController"})
public class SysDeptController {

	@Autowired
	SysDeptService deptService;
	
	@Autowired
	DicElementValSetService dicElementValSetService;
	
	@Autowired
	SysDicElementService dicElementService;
	
	/**
	 * 机构管理界面
	 * @return
	 */
	@RequestMapping({"/entry.do"})
	public ModelAndView entry() {
		Map modelMap = new HashMap();
		modelMap.put("today", DateUtil.getCurrentDate());
		modelMap.put("codeformat", dicElementService.getDicElement("agency").getCodeformat());
	
		return new ModelAndView("/sys/agencyEntry", "modelMap", modelMap);
	}
	
	/**
	 * 查询机构树
	 * @return
	 */
	@ResponseBody
	@RequestMapping({"/queryDeptTree.do"})
	public List<DeptTreeVo> queryDeptTree() {
		List<DeptTreeVo> treeDatas = null;
		
		treeDatas = deptService.queryDeptTree();
		return treeDatas;
	}
	
	/**
	 * 获取机构明细信息
	 * @param request
	 * @return 机构Object
	 */
	@ResponseBody
	@RequestMapping({"/get.do"})
	public Object get(HttpServletRequest request) {
		
		return deptService.get(Long.valueOf(request.getParameter("id")));
	}
	
	/**
	 * 保存机构信息
	 * @param request
	 * @return ResultMsg
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping({"/save.do"})
	public ResultMsg save(HttpServletRequest request) throws ParseException {
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
			deptService.save(paramMap);
			return new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
	}
	
	/**
	 * 查询未选机构扩展属性
	 * @param agencycat 机构类别
	 * @return
	 */
	@ResponseBody
	@RequestMapping({"/queryUnselectedExp.do"})
	public List<Object> queryUnselectedExp(Long agencycat) {
		if (agencycat == null)
			return new ArrayList<Object>();
		else
			return deptService.queryUnselectedExp(agencycat);
	}
	
	/**
	 * 查询已选机构扩展属性
	 * @param agencycat 机构类别
	 * @return
	 */
	@ResponseBody
	@RequestMapping({"/querySelectedExp.do"})
	public List<Object> querySelectedExp(Long agencycat) {
		if (agencycat == null)
			return new ArrayList<Object>();
		else
			return deptService.querySelectedExp(agencycat);
	}
	
	/**
	 * 机构扩展属性配置保存
	 * @param str 机构扩展属性字符串
	 * @return ResultMsg
	 */
	@ResponseBody
	@RequestMapping({"/saveDeptExpCfg.do"})
	public ResultMsg saveDeptExpCfg(String str) {
		
		ResultMsg resultMsg = null;
		
		deptService.saveDeptExpCfg(str);
		resultMsg = new ResultMsg(true, "设置成功！");
		return resultMsg;
	}
	
	/**
	 * 获取机构扩展属性列
	 * @param agencycat 机构类别
	 * @param itemid 机构ID
	 * @return ResultMsg
	 */
	@ResponseBody
	@RequestMapping({"/getExpColumnsHTML.do"})
	public ResultMsg getExpColumnsHTML(Long agencycat, Long itemid) {
		
		ResultMsg resultMsg = null;
		try {
			resultMsg = new ResultMsg(true, deptService.getExpColumnsHTML(agencycat, itemid));
			return resultMsg;
		} catch (AppException e) {
			e.printStackTrace();
			resultMsg = new ResultMsg(false, e.getMessage());
			return resultMsg;
		}
	}
	
	/**
	 * 机构删除
	 * @param itemid 机构ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping({"/delete.do"})
	public ResultMsg delete(Long itemid) {
		
		return deptService.delete(itemid);
	}
}
