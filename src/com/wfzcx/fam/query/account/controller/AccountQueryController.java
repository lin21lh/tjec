/**  
 * @Title: queryAccountController.java  
 * @Package com.wfzcx.fam.query.account.controller  
 * @Description: TODO(用一句话描述该文件做什么)  
 * @author LiuJunBo  
 * @date 2015-4-22 下午03:32:31  
 * @version V1.0  
 */ 
 
 
package com.wfzcx.fam.query.account.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.ControllerUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.fam.common.DeptComponent;
import com.wfzcx.fam.manage.po.FaApplications;
import com.wfzcx.fam.query.account.service.AccountQueryService;

/** 
 * @ClassName: queryAccountController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author LiuJunBo
 * @date 2015-4-22 下午03:32:31  
 */
@Scope("prototype")
@Controller
@RequestMapping("/query/account/controller/AccountQueryController")
public class AccountQueryController {
	
	@Autowired
	AccountQueryService accountQueryService;
	
	/**
	 * 
	 * @Title: init 
	 * @Description: TODO(初始) 
	 * @param @return
	 * @param @throws ServletException 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping({ "/init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("fam/query/account_query_init");
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid",menuid);
		//权限内的所有机构
		String allbdgagency = DeptComponent.getCurAndLowerCodeForYsdw(menuid, "code");
		mav.addObject("allbdgagency",allbdgagency);
		SysUser user = SecureUtil.getCurrentUser();
		mav.addObject("userid",user.getUserid());
		return mav;
	}
	
	/**
	 * 
	 * @Title: queryAccount 
	 * @Description: TODO(档案表-查询) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return EasyUITotalResult 返回类型 
	 * @throws
	 */
	@RequestMapping({ "/queryAccount.do" })
	@ResponseBody
	public EasyUITotalResult queryAccountArchive(HttpServletRequest request) throws  Exception  {
		
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = accountQueryService.queryAccountArchive(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 
	 * @Title: addRevokeView 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping("/accountHisView.do")
	public ModelAndView addRevokeView(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/fam/query/account_query_history");
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid",menuid);
		//权限内的所有机构
		String allbdgagency = DeptComponent.getCurAndLowerCodeForYsdw(menuid, "code");
		mav.addObject("allbdgagency",allbdgagency);
		SysUser user = SecureUtil.getCurrentUser();
		mav.addObject("userid",user.getUserid());
		return mav;
	}
	
	/**
	 * 
	 * @Title: queryAccountArchive 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return EasyUITotalResult 返回类型 
	 * @throws
	 */
	@RequestMapping({ "/queryAccountHis.do" })
	@ResponseBody
	public EasyUITotalResult queryAccountHis(HttpServletRequest request) {
		
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		
		PaginationSupport ps = accountQueryService.queryAccountHis(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 
	 * @Title: loadApplicationView 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping({"/loadApplicationView.do"})
	public ModelAndView loadApplicationView(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/fam/query/account_query_app_view");
		String applicationId = request.getParameter("applicationId");
		FaApplications fa =accountQueryService.getFaApplication(applicationId);
		List list = new ArrayList();
		String wfid ="";
		if (fa !=null) {
			wfid =fa.getWfid();
			if (wfid!=null) {
				list =accountQueryService.getworkFlowList(wfid);
			}
		} 
		mav.addObject("type",fa.getType());
		mav.addObject("wfList",list);
		mav.addObject("wfid",wfid);
		return mav;
	}
	@RequestMapping({ "/getAccountForm.do" })
	@ResponseBody
	public JSONObject getAccountForm(HttpServletRequest request){
		String applicationId = request.getParameter("applicationId");
		List list = accountQueryService.queryApplicationInfo(applicationId);
		if(list.size()==1){
			return  (JSONObject) list.get(0); 
		}
		return null;
	}
}
