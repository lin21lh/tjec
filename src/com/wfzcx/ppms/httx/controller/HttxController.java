package com.wfzcx.ppms.httx.controller;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.ppms.httx.service.HttxService;

/**
 * 合同体系Controller控制类
 * @author wang_yliang
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/ppms/httx/HttxController")
public class HttxController {

	@Autowired
	HttxService service;
	
	/**
	 * 合同体系初始化加载
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping("/init.do")
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/httx/httx_init");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * 合同体系查询
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryHttx.do")
	@ResponseBody
	public EasyUITotalResult queryMessage(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryHttx(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 跳转至新增表单
	 * @param request
	 * @return
	 */
	@RequestMapping("/addHttx.do")
	public ModelAndView addInit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/httx/httx_form");
		
		return mav;
	}
	
	/**
	 * 跳转至追加合同表单
	 * @param request
	 * @return
	 */
	@RequestMapping("/appendHttx.do")
	public ModelAndView appendInit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/httx/httx_form_append");
		
		return mav;
	}
	
	/**
	 * 合同新增保存
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addCommitHttx.do")
	@ResponseBody
	public ResultMsg httxAddCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String returnMsg;
		try {
			returnMsg = service.httxAddCommit(param);
			if ("success".equals(returnMsg)){
				msg = new ResultMsg(true,"保存成功！");
			} else {
				msg = new ResultMsg(false,"保存失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * 合同追加新增保存
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/appendCommitHttx.do")
	@ResponseBody
	public ResultMsg httxAppendCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String returnMsg;
		try {
			returnMsg = service.httxAppendCommit(param);
			if ("success".equals(returnMsg)){
				msg = new ResultMsg(true,"追加保存成功！");
			} else {
				msg = new ResultMsg(false,"追加保存失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * 合同修改保存
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/editCommitHttx.do")
	@ResponseBody
	public ResultMsg httxEditCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String returnMsg;
		try {
			returnMsg = service.httxEditCommit(param);
			if ("success".equals(returnMsg)){
				msg = new ResultMsg(true,"保存成功！");
			} else {
				msg = new ResultMsg(false,"保存失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * 跳转至合同详情页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/detailHttx.do")
	public ModelAndView detailInit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/httx/httx_detail");
		return mav;
	}
	
	/**
	 * 跳转至查询项目页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/projectHttx.do")
	public ModelAndView projectInit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/httx/httx_project");
		return mav;
	}
	
	/**
	 * 合同查询相关项目
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryHttxProject.do")
	@ResponseBody
	public EasyUITotalResult queryProject(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryProject(map);
		return EasyUITotalResult.from(ps);
	}
	
}
