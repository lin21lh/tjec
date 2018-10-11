package com.wfzcx.ppms.library.dsfjgk.controller;

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
import com.wfzcx.ppms.library.dsfjgk.service.DsfjgkService;

@Scope("prototype")
@Controller
@RequestMapping("/ppms/library/dsfjgk/DsfjgkController")
public class DsfjgkController {

	@Autowired
	DsfjgkService service;
	
	/**
	 * 初始加载
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping("/init.do")
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/library/dsfjgk/dsfjgk_init");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * 第三方机构查询
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDsfjgk.do")
	@ResponseBody
	public EasyUITotalResult queryDsfjgk(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryDsfjgk(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 跳转至新增页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/addDsfjgk.do")
	public ModelAndView addInit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/library/dsfjgk/dsfjgk_form");
		return mav;
	}
	
	/**
	 * 录入验证
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/checkDsfjgk.do")
	@ResponseBody
	public ResultMsg checkDsfjgk(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String returnMsg;
		try {
			returnMsg = service.dsfjgkCheck(param);
			if ("success".equals(returnMsg)){
				msg = new ResultMsg(true,returnMsg);
			} else {
				msg = new ResultMsg(false,returnMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * 第三方机构新增保存
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addCommitDsfjgk.do")
	@ResponseBody
	public ResultMsg dsfjgkAddCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String returnMsg;
		try {
			returnMsg = service.dsfjgkAddCommit(param);
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
	 * 第三方机构修改保存
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/editCommitDsfjgk.do")
	@ResponseBody
	public ResultMsg dsfjgkEditCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String returnMsg;
		try {
			returnMsg = service.dsfjgkEditCommit(param);
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
	 * 第三方机构删除
	 * @param dsfjgid
	 * @return
	 */
	@RequestMapping("/deleteDsfjgk.do")
	@ResponseBody
	public ResultMsg dsfjgkDelete(String dsfjgid) {
		ResultMsg msg = null;
		try {
			if(dsfjgid==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.dsfjgkDelete(dsfjgid);
				msg = new ResultMsg(true, AppException.getMessage("删除成功！"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败！"));
		}
		return msg;
	}
	
	/**
	 * 跳转至详情页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/detailDsfjgk.do")
	public ModelAndView detailInit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/library/dsfjgk/dsfjgk_detail");
		return mav;
	}
	
}
