package com.wfzcx.ppms.query.controller;

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
import com.jbf.sys.resource.po.SysResource;
import com.wfzcx.ppms.query.service.ProjectCountService;

@Scope("prototype")
@Controller
@RequestMapping("/query/controller/ProjectCountController")
public class ProjectCountController {
	
	@Autowired
	ProjectCountService projectCountService;

	/**
	 * 
	 * @Title: init 
	 * @Description: TODO(初始页面) 
	 * @param @return String 跳转页面
	 * @param @throws ServletException 设定文件 
	 * @return String 返回类型 
	 */
	@RequestMapping({ "/init.do" })
	public ModelAndView init(String menuid) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/query/count/procount_init");
		mav.addObject("menuid",menuid);
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping({ "/qryProCount.do" })
	public EasyUITotalResult qryProRes(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = projectCountService.qryProCount(map);
		
		return EasyUITotalResult.from(ps);
		
	}
}
