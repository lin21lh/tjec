package com.wfzcx.app.login.controller;

import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.freework.freedbm.util.GeneralTotalResult;
import com.freework.freedbm.util.TotalResult;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.wfzcx.app.login.service.AppLoginService;

/**
 * 用户相关
 * 
 * @author wang_yliang
 * @date 2016-9-22
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/login/AppLoginController")
public class AppLoginController {

	@Autowired
	AppLoginService service;
	
	/**
	 * 用户登录
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/login.do")
	@ResponseBody
	public GeneralTotalResult login(HttpServletRequest request) throws AppException {
		try {
			Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
			Map map = service.login(param);
			LinkedList<Map> list = new LinkedList<Map>();
			list.add(map);
			return new GeneralTotalResult<Map>(new TotalResult<Map>(list,list.size()), true, "");
		} catch (Exception e) {
			return new GeneralTotalResult<Map>(new TotalResult<Map>(new LinkedList<Map>(),0), false,e.getMessage());
		}
		
	}
	
	/**
	 * 修改密码
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/pwdchange.do")
	@ResponseBody
	public GeneralTotalResult pwdchange(HttpServletRequest request) throws AppException {
		try {
			Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
			Map map = service.pwdchange(param);
			LinkedList<Map> list = new LinkedList<Map>();
			list.add(map);
			return new GeneralTotalResult<Map>(new TotalResult<Map>(list,list.size()), true, "");
		} catch (Exception e) {
			return new GeneralTotalResult<Map>(new TotalResult<Map>(new LinkedList<Map>(),0), false,e.getMessage());
		}
		
	}
	
}
