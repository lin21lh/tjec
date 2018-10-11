package com.wfzcx.aros.noticetmpManage.controller;

import java.util.Map;

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
import com.wfzcx.aros.noticetmpManage.service.NoticetmpService;

@Scope("prototype")
@Controller
@RequestMapping("aros/noticetmpManage/controller/NoticetmpController")
public class NoticetmpController
{
	@Autowired
	NoticetmpService service;
	
	/**
	 * 初始化
	 */
	@RequestMapping("/init.do")
	public ModelAndView init(HttpServletRequest request)
	{
		ModelAndView mv = new ModelAndView("aros/noticetmpManage/noticetmp_init");
		
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		
		return mv;
	}
	
	/**
	 * 查询
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryNoticetmp.do")
	@ResponseBody
	public EasyUITotalResult queryNoticetmp(HttpServletRequest request)
	{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryNoticetmp(map);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 编辑
	 */
	@RequestMapping("/addNoticetmp.do")
	public ModelAndView addNoticetmp(HttpServletRequest request)
	{
		ModelAndView mv = new ModelAndView("aros/noticetmpManage/noticetmp_form");
		return mv;
	}
	
	/**
	 * 保存
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/saveNoticetmp.do")
	@ResponseBody
	public ResultMsg saveNoticetmp(HttpServletRequest request)
	{
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try
		{
			service.saveNoticetmp(param);
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
	 * 删除
	 */
	@RequestMapping("/delNoticetmp.do")
	@ResponseBody
	public ResultMsg delNoticetmp(HttpServletRequest request) {
		ResultMsg msg = null;
		String tempid = request.getParameter("tempid");
		try
		{
			service.delNoticetmp(tempid);
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
	 * 详情
	 */
	@RequestMapping("/noticetmpDetail.do")
	public ModelAndView noticetmpDetail(HttpServletRequest request)
	{
		ModelAndView mv = new ModelAndView("aros/noticetmpManage/noticetmp_detail");
		return mv;
	}
	
	/**
	 * 查询clob字段内容
	 * @param messageType
	 * @return
	 */
	@RequestMapping("getClobContentVal.do")
	@ResponseBody
	public String getClobContentVal(String tempid)
	{  
		String contentStr = service.getClobContentVal(tempid);
		return contentStr;
	}
}
