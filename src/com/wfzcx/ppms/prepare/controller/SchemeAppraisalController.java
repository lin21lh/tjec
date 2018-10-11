package com.wfzcx.ppms.prepare.controller;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wfzcx.ppms.prepare.service.SchemeAppraisalServiceI;

/**
 * 方案评审
 * @ClassName: SchemeAppraisalController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author LiuJunBo
 * @date 2015-9-17 上午10:58:30
 */
@Scope("prototype")
@Controller
@RequestMapping("/prepare/controller/SchemeAppraisalController")
public class SchemeAppraisalController {
	
	@Autowired
	SchemeAppraisalServiceI appraisalServiceI;

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
		return new ModelAndView("ppms/prepare/schapp_init");
	}
}
