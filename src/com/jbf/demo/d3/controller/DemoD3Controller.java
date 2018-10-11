package com.jbf.demo.d3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"demo/DemoD3Controller/"})
public class DemoD3Controller {

	/**
	 * 页面入口
	 * 
	 * @return
	 */
	@RequestMapping("hierarchicalBarChartEntry.do")
	public ModelAndView entry() {
		
		return new ModelAndView("/demo/d3/hierarchicalBarChart/hierarchicalBarChartEntry");
	}
}
