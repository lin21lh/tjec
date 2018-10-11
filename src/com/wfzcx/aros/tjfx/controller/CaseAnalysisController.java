package com.wfzcx.aros.tjfx.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.util.FormatUtil;
import com.wfzcx.aros.tjfx.service.CaseAnalysisService;

/**
 * 
 * @author LinXF
 * @date 2016年8月15日 下午1:32:55
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/tjfx/controller")
public class CaseAnalysisController {
	@Autowired
	CaseAnalysisService service;

	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/init.do" })
	public ModelAndView init(String anaType,HttpServletRequest request) throws ServletException {
		String page="caseanalysis_admin";
		if (anaType != null && !anaType.trim().equals("")) {
			switch (Integer.parseInt(anaType)) {
			case 1:
				page = "caseanalysis_admin";
				break;
			case 2:
				page = "caseanalysis_region";
				break;
			case 3:
				page = "caseanalysis_def";
				break;
			case 4:
				page = "caseanalysis_case";
				break;
			case 5:
				page = "caseanalysis_res";
				break;
			case 6:
				page = "caseanalysis_trial";
				break;
			case 7:
				page = "caseanalysis_state";
				break;
			case 8:
				page = "caseanalysis_year";
				break;
			case 9:
				page = "caseanalysis_lit";
				break;
			case 10:
				page = "caseanalysis_great";
				break;
			case 11:
				page = "caseanalysis_spe";
				break;
			case 12:
				page = "caseanalysis_qs_def";
				break;
			}
		}
		
		ModelAndView mv = new ModelAndView("aros/tjfx/"+page);
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		
		// 获取系统当前日期
		String currentYear = FormatUtil.stringDate2().substring(0, 4);
		mv.addObject("startyear", currentYear);
		
		String currentDate = FormatUtil.stringDate2().substring(0, 4);
		mv.addObject("startdate", currentDate);		
		mv.addObject("enddate", currentDate);
		
		String sessionid=request.getSession().getId();
		String path = request.getContextPath();
		
		mv.addObject("sessionid", sessionid);
		mv.addObject("path", path);
		return mv;
	}	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/query.do")
	@ResponseBody
	public EasyUITotalResult query(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.query(map);
		return EasyUITotalResult.from(ps);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryList.do")
	@ResponseBody
	public void queryList(String anaType,HttpServletRequest request,HttpServletResponse response) throws AppException {		
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		@SuppressWarnings("rawtypes")
		List<Map> list = service.queryList(anaType,map);	
		ControllerUtil.responseWriter(JSONArray.toJSON(list).toString(), response);		
	}
	/**
	 * 案件列表查询（复议，被复议，应诉）
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping("/caseInfoSearchView.do")
	public ModelAndView caseInfoSearch(HttpServletRequest request) throws ServletException{
		ModelAndView mv = new ModelAndView("aros/tjfx/caseinfolist_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
		
	}
	
	/**
	 * @Title: queryXzfyViewList
	 * @Description: 案件查询：grid列表页面查询
	 * @author zhaoxingdi
	 * @date 2016年9月27日10:21:58
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryCaseViewList.do")
	@ResponseBody
	public EasyUITotalResult queryCaseViewList(HttpServletRequest request) throws AppException {
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		String flag = (String)param.get("flag");
		if (flag.equals("0")){
			return null;
		} 
		
		PaginationSupport ps = service.queryCaseListForView(param);
		
		return EasyUITotalResult.from(ps);
	}
	
}
