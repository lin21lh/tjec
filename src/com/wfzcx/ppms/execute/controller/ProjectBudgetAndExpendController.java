package com.wfzcx.ppms.execute.controller;

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

import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.ppms.execute.service.ProjectBudgetAndExpendService;
/**
 * 项目执行 预算和支出
 * @ClassName: ProjectBudgetAndExpendController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date2015年9月23日11:05:47
 */
@Scope("prototype")
@Controller
@RequestMapping("/ppms/execute/controller/ProjectBudgetAndExpendController")
public class ProjectBudgetAndExpendController {
	
	@Autowired
	ProjectBudgetAndExpendService service;
	/**
	 * 初始加载
	 * @Title: init 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/execute/budgetAndexpend/budget_init");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		String budgetType  = StringUtil.stringConvert(request.getParameter("budgetType"));
		//预算标志，用于区分预算还是支出，1预算2 支出
		budgetType = "".equals(budgetType)?"1":budgetType;
		mav.addObject("menuid", menuid);
		mav.addObject("budgetType", budgetType);
		return mav;
	}
	/**
	 * 查询项目
	 * @Title: queryProject 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryProject.do")
	@ResponseBody
	public EasyUITotalResult queryProject(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryProject(map);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 预算和支出页面跳转
	 * @Title: budgetAdd 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/budgetAdd.do" })
	public ModelAndView budgetAdd(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/execute/budgetAndexpend/budget_form");
		return mav;
	}	
	/**
	 * 查询已录入的预算或支出情况
	 * @Title: budgetQueryUrl 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/budgetQuery.do")
	@ResponseBody
	public EasyUITotalResult budgetQuery(HttpServletRequest request) throws AppException {
		String projectid = StringUtil.stringConvert(request.getParameter("projectid"));
		String budgetType = StringUtil.stringConvert(request.getParameter("budgetType"));
		PaginationSupport ps = service.budgetQueryUrl(projectid,budgetType);
		return EasyUITotalResult.from(ps);
	}
	
	@RequestMapping("/budgetSave.do")
	@ResponseBody
	public ResultMsg budgetSave(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.budgetSave(param);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
}
