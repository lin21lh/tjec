package com.wfzcx.aros.wtcl.controller;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.aros.wtcl.service.BWebquesbaseinfoService;

@Controller
public class BWebquesbaseinfoController {
	@Autowired
	BWebquesbaseinfoService service;

	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/BWebquesbaseinfoController_init.do" })
	public ModelAndView initW(HttpServletRequest request) throws ServletException {

		ModelAndView mv = new ModelAndView("aros/wtcl/bwebquesbaseinfo_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
	}

	@RequestMapping("/BWebquesbaseinfoController_queryList.do")
	@ResponseBody
	public EasyUITotalResult queryListW(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryListW(map,"01");
		return EasyUITotalResult.from(ps);
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/BWebquesbaseinfoController_add.do" })
	public ModelAndView addW(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/wtcl/bwebquesbaseinfo_form");
		return mv;
	}

	@RequestMapping("/BWebquesbaseinfoController_save.do")
	@ResponseBody
	public ResultMsg saveW(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String id = service.saveW(param);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/BWebquesbaseinfoController_view.do")
	public ModelAndView projectXqgsViewW(String id, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("aros/wtcl/bwebquesbaseinfo_view");
		return mv;
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/BquesbaseinfoController_init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {

		ModelAndView mv = new ModelAndView("aros/wtcl/bquesbaseinfo_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
	}

	@RequestMapping("/BquesbaseinfoController_queryList.do")
	@ResponseBody
	public EasyUITotalResult queryList(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryList(map, "01");
		return EasyUITotalResult.from(ps);
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/BquesbaseinfoController_add.do" })
	public ModelAndView add(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/wtcl/bquesbaseinfo_form");
		return mv;
	}

	@RequestMapping("/BquesbaseinfoController_save.do")
	@ResponseBody
	public ResultMsg save(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String id = service.save(param);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/yjfk_init.do" })
	public ModelAndView yjfk_init(HttpServletRequest request) throws ServletException {

		ModelAndView mv = new ModelAndView("aros/wtcl/bquesbaseinfo_fb_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/yjfk_queryList.do")
	@ResponseBody
	public EasyUITotalResult yjfk_queryListW(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryList(map, "02");
		return EasyUITotalResult.from(ps);
	}
	
	@RequestMapping("/yjfk_save.do")
	@ResponseBody
	public ResultMsg yjfk_save(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String id = service.save(param);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/yjfk_web_init.do" })
	public ModelAndView yjfk_web_init(HttpServletRequest request) throws ServletException {

		ModelAndView mv = new ModelAndView("aros/wtcl/bquesbaseinfo_web_fb_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
	}
	
	@RequestMapping("/yjfk_web_save.do")
	@ResponseBody
	public ResultMsg yjfk_web_save(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String id = service.save_web(param);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 反馈意见删除
	 * @Title: toDeleteSurvey 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/yjfk_web_del.do")
	@ResponseBody
	public ResultMsg yjfk_web_del(HttpServletRequest request) throws AppException {
		ResultMsg msg = null;
		try {
			Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
			service.deleteById(StringUtil.stringConvert(map.get("quesid")));
			msg = new ResultMsg(true, AppException.getMessage("删除成功"));
		}catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败"));
		}
		return msg;
	}
}
