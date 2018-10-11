package com.jbf.base.report.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Scope("prototype")
@Controller
@RequestMapping({"/base/report/scTemplateConrtoller"})
public class ScTemplateConrtoller  {
	
	/**
	 * 报表模板管理页面
	 * @return
	 */
	@RequestMapping({"/entry.do"})
	public ModelAndView entry() {
	
		return new ModelAndView("/demo/supcanTemplate");
	}
}
