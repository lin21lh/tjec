package com.wfzcx.ppms.library.shzbk.controller;

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
import com.wfzcx.ppms.library.shzbk.service.ShzbkService;


@Scope("prototype")
@Controller
@RequestMapping("/ppms/library/shzbk/ShzbkController")
public class ShzbkController {

	@Autowired
	ShzbkService service;
	
	/**
	 * 初始加载
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping("/init.do")
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/library/shzbk/shzbk_init");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * 社会资本库查询
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryShzbk.do")
	@ResponseBody
	public EasyUITotalResult queryShzbk(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryShzbk(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 跳转至社会资本新增页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/addShzbk.do")
	public ModelAndView addInit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/library/shzbk/shzbk_form");
		return mav;
	}
	
	/**
	 * 社会资本新增保存
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addCommitShzbk.do")
	@ResponseBody
	public ResultMsg shzbkAddCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String returnMsg;
		try {
			returnMsg = service.shzbkAddCommit(param);
			if ("success".equals(returnMsg)){
				msg = new ResultMsg(true,"保存成功!");
			} else {
				msg = new ResultMsg(false,"保存失败!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * 社会资本修改保存
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/editCommitShzbk.do")
	@ResponseBody
	public ResultMsg shzbkEditCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String returnMsg;
		try {
			returnMsg = service.shzbkEditCommit(param);
			if ("success".equals(returnMsg)){
				msg = new ResultMsg(true,"保存成功!");
			} else {
				msg = new ResultMsg(false,"保存失败!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * 社会资本删除
	 * @param socialid
	 * @return
	 */
	@RequestMapping("/deleteShzbk.do")
	@ResponseBody
	public ResultMsg shzbkDelete(String socialid) {
		ResultMsg msg = null;
		try {
			if(socialid==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.shzbkDelete(socialid);
				msg = new ResultMsg(true, AppException.getMessage("删除成功！"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败！"));
		}
		return msg;
	}
	
	/**
	 * 跳转至用户关联页面
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/relevanceShzbk.do")
	public ModelAndView relevanceInit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/library/shzbk/shzbk_relevance");
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		mav.addObject("socialid",map.get("socialid"));
		mav.addObject("status",map.get("status"));
//		request.setAttribute("socialid", map.get("socialid"));//方法无效
		return mav;
	}
	
	/**
	 * 用户关联查询
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryShzbkRelevance.do")
	@ResponseBody
	public EasyUITotalResult queryShzbkRelevance(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryShzbkRelevance(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 页面用户关联按钮选择
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/relevanceCommitShzbk.do")
	@ResponseBody
	public ResultMsg shzbkRelevanceCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String returnMsg;
		try {
			returnMsg = service.shzbkRelevanceCommit(param);
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
	 * 录入验证
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/checkShzbk.do")
	@ResponseBody
	public ResultMsg checkShzbk(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String returnMsg;
		try {
			returnMsg = service.shzbkCheck(param);
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
	 * 跳转至详情页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/detailShzbk.do")
	public ModelAndView detailInit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/library/shzbk/shzbk_detail");
		return mav;
	}
}
