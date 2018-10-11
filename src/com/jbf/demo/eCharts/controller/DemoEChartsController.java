package com.jbf.demo.eCharts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"demo/DemoEChartsController/"})
public class DemoEChartsController {

	/**
	 * 页面入口
	 * 
	 * @return
	 */
	@RequestMapping("lineChart.do")
	public ModelAndView entry() {
		
		return new ModelAndView("/demo/eCharts/lineChart");
	}
}
