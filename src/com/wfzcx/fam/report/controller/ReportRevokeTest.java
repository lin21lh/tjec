/**  
 * @Title: ReportRevokeTest.java  
 * @Package com.wfzcx.fam.report.controller  
 * @Description: TODO(用一句话描述该文件做什么)  
 * @author LiuJunBo  
 * @date 2015-6-12 下午03:37:35  
 * @version V1.0  
 */ 
 
 
package com.wfzcx.fam.report.controller;

import javax.servlet.ServletException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.sys.resource.po.SysResource;

/** 
 * @ClassName: ReportRevokeTest 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author LiuJunBo
 * @date 2015-6-12 下午03:37:35  
 */
@Scope("prototype")
@Controller
@RequestMapping("/report/controller/ReportRevokeTest")
public class ReportRevokeTest {
	
	@RequestMapping({ "/init.do" })
	public ModelAndView init() throws ServletException {
		
		ModelAndView mav = new ModelAndView("fam/manage/report/report");
		
		return mav;
	}
}
