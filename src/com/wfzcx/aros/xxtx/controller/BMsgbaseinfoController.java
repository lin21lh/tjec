package com.wfzcx.aros.xxtx.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.aros.xxtx.service.BMsgbaseinfoService;

@Scope("prototype")
@Controller
@RequestMapping("/aros/xxtx/controller/BMsgbaseinfoController")
public class BMsgbaseinfoController {
	
	@Autowired
	BMsgbaseinfoService service;

	
	/**
	 * 初始加载
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping("/init.do")
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/mesNotification/mesNotification_init");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		return mav;
	}
	
	/**
	 * 初始加载
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@ResponseBody
	@RequestMapping("/loginPageQueryMsg.do")
	public List<Map<String, Object>> loginPageQueryMsg(HttpServletRequest request) throws ServletException {
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		return service.loginPageQueryMsg(param);
	}
	
	/**
	 * 删除
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping("/modifyReadStatus.do")
	@ResponseBody
	public ResultMsg modifyReadStatus(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.modifyReadStatus(param);
			msg = new ResultMsg(true, "更新成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, "更新失败" + e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping("/excuteMsgThread.do")
	@ResponseBody
	public void excuteMsgThread(){
		service.overdueCase(null);
	}
}
