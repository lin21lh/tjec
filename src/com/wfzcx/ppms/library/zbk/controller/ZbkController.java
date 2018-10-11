package com.wfzcx.ppms.library.zbk.controller;

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
import com.wfzcx.ppms.library.zbk.service.ZbkService;


@Scope("prototype")
@Controller
@RequestMapping("/ppms/library/zbk/ZbkController")
public class ZbkController {

	@Autowired
	ZbkService service;
	
	/**
	 * 初始加载
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping("/init.do")
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/library/zbk/zbk_init");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * 指标查询
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryZbk.do")
	@ResponseBody
	public EasyUITotalResult queryZbk(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryZbk(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 跳转至新增页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/addZbk.do")
	public ModelAndView addInit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/library/zbk/zbk_form");
		return mav;
	}
	
	/**
	 * 指标新增保存
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addCommitZbk.do")
	@ResponseBody
	public ResultMsg zbkAddCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String returnMsg;
		try {
			returnMsg = service.zbkAddCommit(param);
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
	 * 指标录入验证
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/checkZbk.do")
	@ResponseBody
	public ResultMsg checkZbk(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String returnMsg;
		try {
			returnMsg = service.zbkCheck(param);
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
	 * 指标修改保存
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/editCommitZbk.do")
	@ResponseBody
	public ResultMsg zbkEditCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String returnMsg;
		try {
			returnMsg = service.zbkEditCommit(param);
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
	 * 指标删除
	 * @param dsfjgid
	 * @return
	 */
	@RequestMapping("/deleteZbk.do")
	@ResponseBody
	public ResultMsg zbkDelete(String zbkid) {
		ResultMsg msg = null;
		try {
			if(zbkid==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.zbkDelete(zbkid);
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
	@RequestMapping("/detailZbk.do")
	public ModelAndView detailInit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/library/zbk/zbk_detail");
		return mav;
	}
}
