package com.wfzcx.aros.bztx.controller;

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
import com.wfzcx.aros.bztx.service.StandBaseInfoService;

@Scope("prototype")
@Controller
@RequestMapping("/aros/bztx/controller/StandBaseInfoController")
public class StandBaseInfoController {
	@Autowired
	private StandBaseInfoService standBaseInfoService;
	
	/**
	 * 标准体系
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/init.do"})
	public ModelAndView init(HttpServletRequest request) throws ServletException{
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bztx/standBaseInfo_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		return mav;
	}
	
	/**
	 * 在线帮助
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/online.do"})
	public ModelAndView online(HttpServletRequest request) throws ServletException{
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bztx/standBaseInfo_online");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		return mav;
	}
	
	@ResponseBody
	@RequestMapping({"/queryStandbaseInfo.do"})
	public EasyUITotalResult queryStandbaseInfo(HttpServletRequest request) throws ServletException{
	
		//获取页面传递查询参数
		@SuppressWarnings("unchecked")
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		//执行查询
		PaginationSupport ps = standBaseInfoService.queryStandbaseInfo(param);
		return EasyUITotalResult.from(ps);
	}
	
	
	@RequestMapping({"/add.do"})
	public ModelAndView addPage(HttpServletRequest request){
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bztx/standBaseInfo_form");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		return mav;
	}
	
	@RequestMapping({"/detail.do"})
	public ModelAndView view(HttpServletRequest request){
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bztx/standBaseInfo_view");
		//菜单ID
		//获取页面传递查询参数
		@SuppressWarnings("unchecked")
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		mav.addObject("info", standBaseInfoService.detail(param));
		return mav;
	}
	
	/**
	 * 
	 * @param requestion
	 * @return
	 */
	@ResponseBody
	@RequestMapping({"/save.do"})
	public ResultMsg saveInfo(HttpServletRequest request){
		//获取页面传递查询参数
		@SuppressWarnings("unchecked")
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		return standBaseInfoService.save(param);
	}
	
	@ResponseBody
	@RequestMapping({"/delete.do"})
	public ResultMsg delete(HttpServletRequest request){
		@SuppressWarnings("unchecked")
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		standBaseInfoService.delete(param);
		return new ResultMsg(true, "删除成功");
	}

}
