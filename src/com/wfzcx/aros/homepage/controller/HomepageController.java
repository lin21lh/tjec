package com.wfzcx.aros.homepage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.wfzcx.aros.flow.service.ProbaseinfoService;
import com.wfzcx.aros.homepage.service.HomepageService;
import com.wfzcx.aros.xxtx.service.BMsgbaseinfoService;

@Scope("prototype")
@Controller
@RequestMapping("/aros/homepage/HomepageController")
public class HomepageController {

	@Autowired
	HomepageService service;
	@Autowired
	ProbaseinfoService chartService;
	@Autowired
	BMsgbaseinfoService msgbaseinfoService;
	
	/**
	 * 初始化首页提醒
	 * @return
	 */
	@RequestMapping("/entry.do")
	public ModelAndView entry() {
		return new ModelAndView("aros/homepage/homepage_init");
	}
	
	/**
	 * 行政案件统计
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/myChart1Query.do")
	@ResponseBody
	public Map<String,Object> myChart1Query(HttpServletRequest request) throws AppException {
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		Map<String,Object> map = new HashMap<String, Object>();
//		Object year = param.get("year");//当不传year时为null
		List<Map<String, Object>> list = chartService.queryCaseTotalAnalysis(param);
		map = service.myChart1Query(list);
		return map;
	}
	
	/**
	 * 待处理统计
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/myChart2Query.do")
	@ResponseBody
	public Map<String,Object> myChart2Query(HttpServletRequest request) throws AppException {
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		Map<String,Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = chartService.queryProbaseinfoSumForWait();
		map = service.myChart2Query(list);
		return map;
	}
	
	/**
	 * 行政按月统计
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/myChart3Query.do")
	@ResponseBody
	public Map<String,Object> myChart3Query(HttpServletRequest request) {
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		Map<String,Object> map = new HashMap<String, Object>();
		Object year = param.get("year");//当不传year时为null
		
		map = service.myChart3Query(param);
		return map;
	}
	
	/**
	 * 行政事项统计
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/myChart4Query.do")
	@ResponseBody
	public Map<String,Object> myChart4Query(HttpServletRequest request) {
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		Map<String,Object> map = new HashMap<String, Object>();
		Object year = param.get("year");//当不传year时为null
		
		map = service.myChart4Query(param);
		return map;
	}
	
	@RequestMapping("/myChart4entry.do")
	public ModelAndView myChart4entry() {
		return new ModelAndView("aros/homepage/fychart4_init");
	}
	
	@RequestMapping("/myChart3entry.do")
	public ModelAndView myChart3entry() {
		return new ModelAndView("aros/homepage/fychart3_init");
	}
	
	@RequestMapping("/myChart1entry.do")
	public ModelAndView myChart1entry() {
		return new ModelAndView("aros/homepage/fychart1_init");
	}
	
	/**
	 * 待处理任务
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@ResponseBody
	@RequestMapping("/todo.do")
	public List<Map<String, Object>> todo(HttpServletRequest request) throws ServletException {
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		return service.todo(param);
	}
	
	/**
	 * 消息详情
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping("/msgView.do")
	public ModelAndView msgView(HttpServletRequest request) throws ServletException {
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			msgbaseinfoService.modifyReadStatus(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("aros/homepage/msginfo_view");
	}
	
	/**
	 * @Title: myChart5entry
	 * @Description: 所属区域统计：返回统计页面
	 * @author ybb
	 * @date 2016年11月17日 下午3:14:55
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/myChart5entry.do")
	public ModelAndView myChart5entry(HttpServletRequest request) {
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		ModelAndView model = new ModelAndView("aros/homepage/fychart5_init");
		
		List<JSONObject> dicenumitemList = service.queryCasebaseinfoCount(param);
		model.addObject("list", dicenumitemList);
		
		return model;
	}
	
	/**
	 * @Title: myChart5Query
	 * @Description: 所属区域统计:查询案件情况数量以及占比
	 * @author ybb
	 * @date 2016年11月17日 下午4:30:05
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/myChart5Query.do")
	@ResponseBody
	public Map<String,Object> myChart5Query(HttpServletRequest request) {
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		map = service.myChart5Query(param);
		
		return map;
	}
}
