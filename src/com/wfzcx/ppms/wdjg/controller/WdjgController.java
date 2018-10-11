package com.wfzcx.ppms.wdjg.controller;

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
import com.wfzcx.ppms.wdjg.service.WdjgService;

/**
 * 文档结构Controller类
 * @author wang_yliang
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/ppms/wdjg/WdjgController")
public class WdjgController {
	
	@Autowired
	WdjgService service;

	/**
	 * 初始加载
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping("/init.do")
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/wdjg/wdjg_init");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * 文档结构查询
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryWdjg.do")
	@ResponseBody
	public EasyUITotalResult queryWdjg(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryWdjg(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 跳转至新增页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/addWdjg.do")
	public ModelAndView addInit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/wdjg/wdjg_form");
		return mav;
	}
	
	/**
	 * 文档结构新增保存
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addCommitWdjg.do")
	@ResponseBody
	public ResultMsg wdjgAddCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String returnMsg;
		try {
			returnMsg = service.wdjgAddCommit(param);
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
	 * 文档结构修改保存
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/editCommitWdjg.do")
	@ResponseBody
	public ResultMsg wdjgEditCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String returnMsg;
		try {
			returnMsg = service.wdjgEditCommit(param);
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
	
	@RequestMapping("/deleteWdjg.do")
	@ResponseBody
	public ResultMsg wdjgDelete(String wdjgid) {
		ResultMsg msg = null;
		try {
			if(wdjgid==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.wdjgDelete(wdjgid);
				msg = new ResultMsg(true, AppException.getMessage("删除成功！"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败！"));
		}
		return msg;
	}
	
}
