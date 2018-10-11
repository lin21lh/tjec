package com.wfzcx.ppp.xmkf.controller;

import java.util.Map;
import java.util.UUID;

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
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.ControllerUtil;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.ppp.xmkf.service.ProjectLzbgService;
/**
 * 项目开发-论证报告（录入，送审、审核）
 * @ClassName: ProjectLzbgController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2016年8月4日 上午11:27:02
 */
@Scope("prototype")
@Controller
@RequestMapping("/ppp/xmkf/ProjectLzbgController/")
public class ProjectLzbgController {
	@Autowired
	ProjectLzbgService service;
	@Autowired
	ParamCfgComponent pcfg;
	/**
	 * 论证报告首页跳转
	 * @Title: init 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/lzbginit.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException{
		ModelAndView mav = new ModelAndView("ppp/xmkf/lzbg/lzbg_init");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		SysUser user = SecureUtil.getCurrentUser();
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
	 * 项目查询
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
	 * 论证报告新增
	 * @Title: lzbgAdd 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/lzbgAdd.do")
	public ModelAndView lzbgAdd(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppp/xmkf/lzbg/lzbg_form");
		//附件提醒，附件提醒内容
		mav.addObject("fjts",  pcfg.findGeneralParamValue("SYSTEM", "FJ_XMCB"));
		return mav;
	}
	/**
	 * 评审专家列表
	 * @Title: queryPjzjlbqueryPjzjlb 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/queryPjzjlb.do")
	@ResponseBody
	public EasyUITotalResult queryPjzjlbqueryPjzjlb(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryPjzjlb(map);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 评价指标查询
	 * @Title: queryPjzbTable 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/queryPjzbTable.do")
	@ResponseBody
	public EasyUITotalResult queryPjzbTable(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryPjzbTable(map);
		return EasyUITotalResult.from(ps);
	}
	@RequestMapping("/financeQuery.do")
	@ResponseBody
	public EasyUITotalResult financeQuery(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.financeQuery(map);
		return EasyUITotalResult.from(ps);
	}
}
