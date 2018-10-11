package com.wfzcx.ppms.indexPage.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.util.ControllerUtil;
import com.wfzcx.ppms.indexPage.service.IndexPageService;

@Scope("prototype")
@Controller
@RequestMapping("/ppms/indexPage/IndexPageController")
public class IndexPageController {

	@Autowired
	IndexPageService service;
	
	/**
	 * 初始化首页提醒
	 * @return
	 */
	@RequestMapping("/entry.do")
	public ModelAndView entry() {
		return new ModelAndView("ppms/indexPage/indexPage_init");
	}
	
	/**
	 * 图表1查询
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryMyChart1.do")
	@ResponseBody
	public Map<String,Object> myChart1Query(HttpServletRequest request) {
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		Map<String,Object> map = new HashMap<String, Object>();
//		String str = "{\"series\": [{\"name\":\"项目数量\",\"data\":[{\"value\":\"35\", \"name\":\"使用者\"},{\"value\":\"79\", \"name\":\"缺口\"},{\"value\":\"48\", \"name\":\"付费\"}]},{\"name\":\"项目金额\",\"data\":[{\"value\":\"35\", \"name\":\"使用者付费\"},{\"value\":\"30\", \"name\":\"缺口补助\"},{\"value\":\"4\", \"name\":\"政府付费\"}]}]}";
//		map.put("js", JSONObject.parse(str));
		//map = service.myChart1Query(param);
		return map;
	}
	
	/**
	 * 图表2查询
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryMyChart2.do")
	@ResponseBody
	public Map<String,Object> myChart2Query(HttpServletRequest request) {
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		Map<String,Object> map = new HashMap<String, Object>();
//		map = service.myChart2Query(param);
		return map;
	}
	
	/**
	 * 图表3查询
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryMyChart3.do")
	@ResponseBody
	public Map<String,Object> myChart3Query(HttpServletRequest request) {
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		Map<String,Object> map = new HashMap<String, Object>();
//		map = service.myChart3Query(param);
		return map;
	}
}
