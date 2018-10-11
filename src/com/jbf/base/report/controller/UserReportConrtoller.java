package com.jbf.base.report.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.sys.user.po.SysUser;
import com.jbf.sys.user.service.SysUserService;

@Scope("prototype")
@Controller
@RequestMapping({"/base/report/userReportConrtoller"})
public class UserReportConrtoller  {
	
	@Autowired
	SysUserService userService;
	
	/**
	 * 报表模板管理页面
	 * @return
	 */
	@RequestMapping({"/entry.do"})
	public ModelAndView entry(HttpServletRequest request) {
	
		Map modelMap = new HashMap();
		
		javax.servlet.http.Cookie[] cookies = request.getCookies();
		String cookieStr = "";
		for (int i=0; i<cookies.length; i++) {
			if (cookieStr.length() > 0)
				cookieStr += ";";
			cookieStr += cookies[i].getName() + "=" + cookies[i].getValue();
		}
		System.err.println(cookieStr);
		modelMap.put("cookie", cookieStr);
		return new ModelAndView("/demo/userReport", "modelMap", modelMap);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/query.do")
	@ResponseBody
	public List<SysUser> query(HttpServletRequest request) {

		Map map = new HashMap();
		map.put("page", 1);
		map.put("rows", 100000);
		PaginationSupport ps = userService.query(map);
		return ps.getItems();
	}
}
