package com.wfzcx.ppms.discern.controller;

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

import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.resource.po.SysResource;
import com.wfzcx.ppms.discern.service.ProjectVerifyService;

@Scope("prototype")
@Controller
@RequestMapping("/ppms/discern/ProjectVerifyController")
public class ProjectVerifyController {
	@Autowired
	ProjectVerifyService service;
	
	@RequestMapping({ "/verify.do" })
	public ModelAndView audit(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/discern/discern_verify");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		try {
			SysResource sr	=service.getResourceById(menuid);
			mav.addObject("activityId",sr.getActivityid());
			Integer wftasknode = sr.getWftasknode();//（0=首任务节点；1=中间任务节点；2=末任务节点）
			boolean firstNode = false;
			boolean lastNode = false;
			if (wftasknode==0) {
				firstNode = true;
			}else if (wftasknode==2) {
				lastNode = true;
			}
			mav.addObject("firstNode",firstNode);
			mav.addObject("lastNode",lastNode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	/**
	 * 审核页面跳转
	 * @Title: auditEntry 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/auditEntry.do")
	public ModelAndView auditEntry(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/discern/discern_verify_form");
		return mav;
	}
	@RequestMapping("/projectView.do")
	public ModelAndView projectView(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("ppms/discern/discern_verify_view");
		return mav;
	}
	/**
	 * 送审
	 * @Title: sendWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 */
	@RequestMapping("/sendWorkFlow.do")
	@ResponseBody
	public ResultMsg sendWorkFlow(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		
		try {
			String msgString = service.sendWorkFlow(map);
			if("".equals(msgString)){
				msg = new ResultMsg(true, AppException.getMessage("审批成功！"));
			}else{
				msg = new ResultMsg(false, AppException.getMessage("审批失败！"+msgString));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("审批失败！"+e.getMessage()));
		}
		return msg;
	}
	
	/**
	 * 退回
	 * @Title: backWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/backWorkFlow.do")
	@ResponseBody
	public ResultMsg backWorkFlow(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String returnMsg = service.backWorkFlow(param);
			if("".equals(returnMsg)){
				msg = new ResultMsg(true, AppException.getMessage("退回成功！"));
			}else{
				msg = new ResultMsg(true, AppException.getMessage("退回失败！"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("退回失败！"+e.getMessage()));
		}
		return msg;
	}
}
