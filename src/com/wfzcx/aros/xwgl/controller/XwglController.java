package com.wfzcx.aros.xwgl.controller;

import java.util.Map;
import java.util.UUID;

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
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.aros.wyInfoManage.po.BContentbaseinfo;
import com.wfzcx.aros.xwgl.service.XwglService;

@Scope("prototype")
@Controller
@RequestMapping("aros/xwgl/controller/XwglController")
public class XwglController {
	@Autowired
	XwglService xwglService;
	@RequestMapping("/init.do")
	public ModelAndView init(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("aros/xwgl/contentbaseinfo_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		String contype = request.getParameter("contype");
		mv.addObject("contype", contype);
		return mv;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryContent.do")
	@ResponseBody
	public EasyUITotalResult queryContent(HttpServletRequest request)
	{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = xwglService.queryContent(map);
		return EasyUITotalResult.from(ps);
	}
	
	
	
	
	
	@RequestMapping("/addContent.do")
	public ModelAndView addContent(HttpServletRequest request)throws ServletException
	{
		ModelAndView mv = new ModelAndView("aros/xwgl/contentbaseinfo_form");
		//附件ID
		UUID uuid = UUID.randomUUID();
		
		//新增时产生临时的uuid放在附件的keyid中
		mv.addObject("fjkeyid", uuid);
		
		String contype = request.getParameter("contype");
		mv.addObject("contype", contype);
		return mv;
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
			xwglService.saveContent(param);
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
		String contentStr = xwglService.getClobContentVal(conid);
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
			xwglService.delContent(conid);
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
			xwglService.changeStatus(conid, status);
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
		ModelAndView mv = new ModelAndView("aros/xwgl/contentbaseinfo_showInit");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		String contype = request.getParameter("contype");
		mv.addObject("contype", contype);
		return mv;
	}
	
	@RequestMapping("/showContent.do")
	public ModelAndView showContent(HttpServletRequest request)
	{
		ModelAndView mv = new ModelAndView("aros/xwgl/contentbaseinfo_view");
		String conid = request.getParameter("conid");
		BContentbaseinfo info = xwglService.queryInfo(conid);
		mv.addObject("info", info);
		return mv;
	}
	
}
