package com.wfzcx.aros.web.xxgk.controller;

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
import com.jbf.common.util.ControllerUtil;
import com.wfzcx.aros.web.xxgk.service.CasePubWebService;

@Scope("prototype")
@Controller
public class CasePubWebController {
	@Autowired
	CasePubWebService service;
	
	@RequestMapping({"/CasePubWebController_init.do"})	
	public ModelAndView init(HttpServletRequest request) throws ServletException
	{ 
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/web/xxgkweb/casePublishWeb_init");
		return mav;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping({"/CasePubWebController_query.do"})
	@ResponseBody
	public EasyUITotalResult query(HttpServletRequest request) throws ServletException
	{ 
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryList(param);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: dxalDetail
	 * @Description: 信息公开详情页面
	 * @author zhangtiantian
	 * @date 2016年9月23日
	 * @param request
	 * @return
	 */
	@RequestMapping({"/CasePubWebController_view.do"})
	public ModelAndView xxgkDetail(HttpServletRequest request)
	{ 
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/web/xxgkweb/casePublishWeb_view");
		return mav;
	}
	
	/**
	 * 查询clob字段内容
	 * @param messageType
	 * @return
	 */
	@RequestMapping("/CasePubWebController_getClobContentVal.do")
	@ResponseBody
	public String getClobContentVal(String conid)
	{  
		String contentStr = service.getClobContentVal(conid);
		return contentStr;
	}
}
