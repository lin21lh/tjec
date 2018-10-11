package com.wfzcx.aros.wyInfoManage.controller;

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

import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.wyInfoManage.po.BContentbaseinfo;
import com.wfzcx.aros.wyInfoManage.service.ContentbaseinfoService;
import com.wfzcx.aros.zjgl.service.BSpesugbaseinfoService;

@Scope("prototype")
@Controller
@RequestMapping("aros/wyInfoManage/controller/ContentbaseinfoController")
public class ContentbaseinfoController
{
	@Autowired
	ContentbaseinfoService service;
	
	@Autowired
	private BSpesugbaseinfoService speService;
	
	@RequestMapping("/init.do")
	public ModelAndView init(HttpServletRequest request)
	{
		ModelAndView mv = new ModelAndView("aros/wyInfoManage/contentbaseinfo_init");
		
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		
		String contype = request.getParameter("contype");
		mv.addObject("contype", contype);
		
		String contypename = service.queryContypeName(contype);
		mv.addObject("contypename", contypename);
		
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryContent.do")
	@ResponseBody
	public EasyUITotalResult queryContent(HttpServletRequest request)
	{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryContent(map);
		
		return EasyUITotalResult.from(ps);
	}
	
	@RequestMapping("/addContent.do")
	public ModelAndView addContent(HttpServletRequest request)
	{
		ModelAndView mv = new ModelAndView("aros/wyInfoManage/contentbaseinfo_form");
		String contype = request.getParameter("contype");
		mv.addObject("contype", contype);
		return mv;
	}
	
	@RequestMapping("/showContent.do")
	public ModelAndView showContent(HttpServletRequest request)
	{
		ModelAndView mv = new ModelAndView("aros/wyInfoManage/contentbaseinfo_view");
		String conid = request.getParameter("conid");
		BContentbaseinfo info = service.queryInfo(conid);
		String typename = service.getTypeDic(info.getContype(), info.getType());
		mv.addObject("typename", typename);
			mv.addObject("info", info);
		return mv;
	}
	
	/**
	 * 展示接收人页面
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping("/showPersonList.do")
	public ModelAndView showPersonList(String noticeid) throws ServletException
	{
		ModelAndView mav = new ModelAndView("aros/wyInfoManage/choiseReceivePerson");
		return mav;
	}
	
	/**
	 * 查询未选中所有接收人
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryUnselectedUser.do")
	@ResponseBody
	public List<Map<String, Object>> queryUnselectedUser(HttpServletRequest request) throws AppException 
	{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		return service.queryUnselectedUser(map);
	}
	
	/**
	 * 查询已选中的接收人
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/querySelectedUser.do")
	@ResponseBody
	public List<Map<String, Object>> querySelectedUser(HttpServletRequest request)throws AppException 
	{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		return service.querySelectedUser(map);
	}
	
	/**
	 * 保存
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/saveContent.do")
	@ResponseBody
	public ResultMsg saveContent(HttpServletRequest request)
	{
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try
		{
			service.saveContent(param);
			msg = new ResultMsg(true, "保存成功！");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * 查询clob字段内容
	 * @param messageType
	 * @return
	 */
	@RequestMapping("getClobContentVal.do")
	@ResponseBody
	public String getClobContentVal(String conid)
	{  
		String contentStr = service.getClobContentVal(conid);
		return contentStr;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delContent.do")
	@ResponseBody
	public ResultMsg delContent(HttpServletRequest request) {
		ResultMsg msg = null;
		String conid = request.getParameter("conid");
		try
		{
			service.delContent(conid);
			msg = new ResultMsg(true, "删除成功！");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 发布或撤消
	 */
	@RequestMapping("/changeStatus.do")
	@ResponseBody
	public ResultMsg changeStatus(HttpServletRequest request) {
		ResultMsg msg = null;
		String conid = request.getParameter("conid");
		String status = request.getParameter("status");
		try
		{
			service.changeStatus(conid, status);
			msg = new ResultMsg(true, "成功！");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping("/showInit.do")
	public ModelAndView showInit(HttpServletRequest request)
	{
		ModelAndView mv = new ModelAndView("aros/wyInfoManage/contentbaseinfo_showInit");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		String contype = request.getParameter("contype");
		mv.addObject("contype", contype);
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryByCurrentUser.do")
	@ResponseBody
	public EasyUITotalResult queryByCurrentUser(HttpServletRequest request)
	{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryByCurrentUser(map);
		return EasyUITotalResult.from(ps);
	}
	
	@RequestMapping("/showView.do")
	public ModelAndView showView(HttpServletRequest request)
	{
		ModelAndView mv = new ModelAndView("aros/wyInfoManage/contentbaseinfo_showView");
		return mv;
	}
	
	@RequestMapping("/opinionSumInit.do")
	public ModelAndView opinionSumInit(HttpServletRequest request)
	{
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/wyInfoManage/opinionSum_Init");
		String menuid = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		return mav;
	}
	
	@RequestMapping("/opinionSum.do")
	public ModelAndView opinionSum(HttpServletRequest request)
	{
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/wyInfoManage/opinionSum_view");
		String caseid = request.getParameter("caseid");
		SysUser user = SecureUtil.getCurrentUser();
		List<Map<String, Object>> data = speService.querySpeSugByCaseIdAndOperId(caseid, user.getUserid());
		mav.addObject("data", data);
		mav.addObject("caseid", caseid);
		return mav;
	}
}
