package com.wfzcx.aros.xzfy.controller;

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
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.wfzcx.aros.xzfy.service.BSurveyAndTrialService;

/**
 * 行政复议案件调查笔录和庭审笔录
 * @ClassName: BSurveyAndTrialController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author WangZhengke
 * @date 2016年8月8日 17:35:01
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/xzfy/controller/BSurveyAndTrialController")
public class BSurveyAndTrialController {
	
	@Autowired
	BSurveyAndTrialService service;
	@Autowired
	ParamCfgComponent pcfg;
	
	//调查笔录grid页面
	private static final String MV_MAIN_SURVEY = "aros/ajbl/survey/survey_init";
	//庭审笔录grid页面
	private static final String MV_MAIN_TRIAL = "aros/ajbl/trial/trialbaseinfo_init";
	//调查笔录维护页面
	private static final String MV_EDIT_SURVEY = "aros/ajbl/survey/survey_form";
	//庭审笔录维护页面
	private static final String MV_EDIT_TRIAL = "aros/ajbl/trial/trial_form";
	//调查笔录维护页面
	private static final String MV_VIEW_SURVEY = "aros/ajbl/survey/survey_view";
	//庭审笔录维护页面
	private static final String MV_VIEW_TRIAL = "aros/ajbl/trial/trial_view";

	
	/**
	 * 调查笔录首页
	 * @Title: init 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/surveyinit.do" })
	public ModelAndView surveyinit(HttpServletRequest request) throws ServletException{
		ModelAndView mav = null;

		mav = new ModelAndView(MV_MAIN_SURVEY);
		
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		return mav;
	}
	
	/**
	 * 庭审笔录首页
	 * @Title: trialinit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/trialinit.do" })
	public ModelAndView trialinit(HttpServletRequest request) throws ServletException
	{
		ModelAndView mav = new ModelAndView(MV_MAIN_TRIAL);
		
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		//附件ID
		UUID uuid = UUID.randomUUID();
		//新增时产生临时的uuid放在附件的keyid中
		mav.addObject("fjkeyid", uuid);
		
		return mav;
	}
	
	/**
	 * 审理记录grid查询
	 * @Title: querySurveyGrid 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/querySurveyGrid.do")
	@ResponseBody
	public EasyUITotalResult querySurveyGrid(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.querySurveyGrid(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 调查笔录grid查询
	 * @Title: queryProjectCgxx 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryTrialGrid.do")
	@ResponseBody
	public EasyUITotalResult queryTrialGrid(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryTrialGrid(map);
		return EasyUITotalResult.from(ps);
	}
	
	
	/**
	 * 跳转至调查新增、修改form
	 * @Title: toSurveyAdd 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/toSurveyAdd.do")
	public ModelAndView toSurveyAdd(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(MV_EDIT_SURVEY);

		return mav;
	}
	
	/**
	 * 跳转至庭审新增、修改form
	 * @Title: toTrialAdd 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/toTrialAdd.do")
	public ModelAndView toTrialAdd(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(MV_EDIT_TRIAL);

		return mav;
	}
	
	/**
	 * 跳转至调查详情form
	 * @Title: toSurveyView 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/toSurveyView.do")
	public ModelAndView toSurveyView(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(MV_VIEW_SURVEY);

		return mav;
	}
	
	/**
	 * 跳转至庭审详情form
	 * @Title: toTrialView 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/toTrialView.do")
	public ModelAndView toTrialView(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(MV_VIEW_TRIAL);

		return mav;
	}
	
	/**
	 * 调查笔录删除
	 * @Title: toDeleteSurvey 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/toDeleteSurvey.do")
	@ResponseBody
	public ResultMsg toDeleteSurvey(HttpServletRequest request) throws AppException {
		ResultMsg msg = null;
		try {
			Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
			service.deleteSurveyById(StringUtil.stringConvert(map.get("srid")));
			msg = new ResultMsg(true, AppException.getMessage("删除成功"));
		}catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败"));
		}
		return msg;
	}
	
	/**
	 * 庭审笔录删除
	 * @Title: toDeleteTrial 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/toDeleteTrial.do")
	@ResponseBody
	public ResultMsg toDeleteTrial(HttpServletRequest request) throws AppException {
		ResultMsg msg = null;
		try {
			Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
			service.deleteTrialById(StringUtil.stringConvert(map.get("trialid")));
			msg = new ResultMsg(true, AppException.getMessage("删除成功"));
		}catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败"));
		}
		return msg;
	}
	
	/**
	 * 调查笔录保存
	 * @Title: toSaveSurvey 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/toSaveSurvey.do")
	@ResponseBody
	public ResultMsg toSaveSurvey(HttpServletRequest request) throws AppException {
		ResultMsg msg = null;
		try {
			Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
			service.saveSurvey(map);
			msg = new ResultMsg(true, AppException.getMessage("保存成功"));
		}catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("保存失败"));
		}
		return msg;
	}
	
	/**
	 * 调查笔录保存
	 * @Title: toSaveTrial 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/toSaveTrial.do")
	@ResponseBody
	public ResultMsg toSaveTrial(HttpServletRequest request) throws AppException {
		ResultMsg msg = null;
		try {
			Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
			service.saveTrial(map);
			msg = new ResultMsg(true, AppException.getMessage("保存成功"));
		}catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("保存失败"));
		}
		return msg;
	}
}
