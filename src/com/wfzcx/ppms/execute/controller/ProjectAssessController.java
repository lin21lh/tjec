package com.wfzcx.ppms.execute.controller;

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
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.ppms.execute.service.ProjectAssessServiceI;

@Scope("prototype")
@Controller
@RequestMapping("/execute/controller/ProjectAssessController")
public class ProjectAssessController {

	@Autowired
	ProjectAssessServiceI projectAssessService;
	
	/**
	 * 
	 * @Title: init 
	 * @Description: TODO(初始页面) 
	 * @param @return String 跳转页面
	 * @param @throws ServletException 设定文件 
	 * @return String 返回类型 
	 */
	@RequestMapping({ "/init.do" })
	public ModelAndView init(String menuid) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/execute/assess/proass_init");
		mav.addObject("menuid",menuid);
		return mav;
	}
	
	@ResponseBody
	@RequestMapping({ "/qryProAss.do" })
	public EasyUITotalResult qryProAss(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = projectAssessService.qryProAss(map);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 *页面跳转
	 * @Title: optProResView 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping({"/optProAssView.do"})
	public ModelAndView optProAssView(HttpServletRequest request) throws Exception{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		String optFlag = map.get("optFlag")==null?"":map.get("optFlag").toString();
		ModelAndView mav = null;
		if("".equals(optFlag)){
			throw new Exception("没找到视图！");
		}else{
			if("add".equals(optFlag) || "edit".equals(optFlag)){
				mav = new ModelAndView("ppms/execute/assess/proass_form");
			}else if("view".equals(optFlag)){
				mav = new ModelAndView("ppms/execute/assess/proass_view");
			}else if("audit_single".equals(optFlag)){
				mav = new ModelAndView("ppms/execute/assess/proass_form_audit");
			}else if("audit_multiple".equals(optFlag) || "back_multiple".equals(optFlag)){
				mav = new ModelAndView("ppms/prepare/audit_multiple");
			}else {
				throw new Exception("没找到视图！");
			}
		}
		return mav;
	}
	
	/**
	 * 查询评估
	 * @Title: qryProAssess 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return EasyUITotalResult 返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping({"/qryProAssess.do"})
	public EasyUITotalResult qryProAssess(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = projectAssessService.qryProAssess(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 保存
	 * @Title: saveProAss 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/saveProAss.do")
	@ResponseBody
	public ResultMsg saveProAss(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			projectAssessService.saveProAss(map);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
		return msg;
	}
	
	/**
	 * 删除
	 * @Title: delProAssess 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/delProAssess")
	@ResponseBody
	public ResultMsg delProAssess(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			projectAssessService.delProAssess(map);
			msg = new ResultMsg(true, AppException.getMessage("删除成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败！"));
		}
		return msg;
	}
}
