package com.wfzcx.aros.sqbl.controller;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.aros.sqbl.service.ApplyRecordInfoService;

/**
 * 申请笔录控制类
 * @author zhaoxd
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/sqbl/controller/ApplyRecordController")
public class ApplyRecordController {
	
	@Autowired
	private ApplyRecordInfoService applyRecordInfoService;
	

	/**
	 * 初始化
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/init.do"})
	public ModelAndView init(HttpServletRequest request) throws ServletException{
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/sqbl/applyRecordInfo_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		return mav;
	}
	
	@ResponseBody
	@RequestMapping({"/query.do"})
	public EasyUITotalResult queryStandbaseInfo(HttpServletRequest request) throws ServletException{
		//获取页面传递查询参数
		@SuppressWarnings("unchecked")
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		//执行查询
		PaginationSupport ps = applyRecordInfoService.query(param);
		return EasyUITotalResult.from(ps);
	}
	
	@RequestMapping({"/add.do"})
	public ModelAndView addPage(HttpServletRequest request){
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/sqbl/applyRecordInfo_form");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		return mav;
	}
	
	@RequestMapping({"/detail.do"})
	public ModelAndView detail(HttpServletRequest request){
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/sqbl/applyRecordInfo_view");
		//菜单ID
		//获取页面传递查询参数
		@SuppressWarnings("unchecked")
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		mav.addObject("info", applyRecordInfoService.detail(param));
		return mav;
	}
	
	@ResponseBody
	@RequestMapping({"/save.do"})
	public ResultMsg saveInfo(HttpServletRequest request) throws Exception{
		//获取页面传递查询参数
		@SuppressWarnings("unchecked")
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		return applyRecordInfoService.save(param);
	}
	
	@ResponseBody
	@RequestMapping({"/delete.do"})
	public ResultMsg delete(HttpServletRequest request){
		@SuppressWarnings("unchecked")
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		applyRecordInfoService.delete(param);
		return new ResultMsg(true, "删除成功");
	}
	
	/**
	 * 初始化
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/list.do"})
	public ModelAndView list(HttpServletRequest request) throws ServletException{
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/sqbl/applyRecordInfo_list");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		return mav;
	}
	
	@ResponseBody
	@RequestMapping({"/queryListByCase.do"})
	public EasyUITotalResult queryListByCase(HttpServletRequest request) throws ServletException{
		//获取页面传递查询参数
		@SuppressWarnings("unchecked")
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		//执行查询
		PaginationSupport ps = applyRecordInfoService.queryListByCase(param);
		return EasyUITotalResult.from(ps);
	}
}
