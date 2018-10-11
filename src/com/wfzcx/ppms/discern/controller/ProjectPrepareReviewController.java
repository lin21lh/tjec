package com.wfzcx.ppms.discern.controller;

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
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.ppms.discern.service.ProjectPrepareReviewService;
/**
 * 评审准备（项目识别、项目准备共用）
 * @ClassName: ProjectPrepareReviewController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2016年3月7日 下午3:19:29
 */
@Scope("prototype")
@Controller
@RequestMapping("/ppms/discern/ProjectPrepareReviewController")
public class ProjectPrepareReviewController {
	@Autowired
	ProjectPrepareReviewService service;
	
	@RequestMapping({ "/init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/prepareReview/prepareReview_init");
		String menuid  = request.getParameter("menuid");
		String xmhj  = request.getParameter("xmhj");
		mav.addObject("menuid", menuid);
		mav.addObject("xmhj", xmhj);
		return mav;
	}
	/**
	 * 查询项目
	 * @Title: queryProject 
	 * @Description: (这里用一句话描述这个方法的作用) 
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
	 * 新增页面跳转
	 * @Title: prepareAdd 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/prepareAdd.do" })
	public ModelAndView prepareAdd(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/prepareReview/prepareReview_form");
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		return mav;
	}
	/**
	 * 专家查询
	 * @Title: qualExpertGrid 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/qualExpertGrid.do")
	@ResponseBody
	public EasyUITotalResult qualExpertGrid(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.qualExpertGrid(map);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 查询专家列表
	 * @Title: qryExpertByQ 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return EasyUITotalResult 返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping({ "/qryExpertByQ.do" })
	public EasyUITotalResult qryExpertByQ(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.qryExpertByQ(map);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 添加指标页面跳转
	 * @Title: showPszbList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @return
	 * @throws ServletException 设定文件
	 */
	
	@RequestMapping("/showPszbList.do")
	public ModelAndView showPszbList() throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/prepareReview/selectPszbInit");
		return mav;
	}
	/**
	 * 
	 * @Title: queryUnselectedUser 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/queryPszbList.do")
	@ResponseBody
	public List queryPszbList(HttpServletRequest request) throws AppException{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		return service.queryPszbList(map);
	}
	@RequestMapping("/pjzbSave.do")
	@ResponseBody
	public ResultMsg pjzbSave(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.pjzbSave(param);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	/**
	 * 查询项目是否已经录入评审准备
	 * @Title: queryIsExistPszb 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @return 设定文件
	 */
	@RequestMapping("/queryIsExistPszb.do")
	@ResponseBody
	public ResultMsg queryIsExistPszb(String projectid,String xmhj) {
		ResultMsg msg = null;
		try {
			if(projectid==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				List list = service.queryIsExistPszb(projectid,xmhj);
				if(list.isEmpty()){
					msg = new ResultMsg(true,"");
				}else {
					msg = new ResultMsg(false,"");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("操作失败！"));
		}
		return msg;
	}
	/**
	 * 查询评价准备定性分析指标
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
	/**
	 * 评审准备撤回
	 * @Title: revokePszb 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @param pszbid
	 * @return 设定文件
	 */
	@RequestMapping("/revokePszb.do")
	@ResponseBody
	public ResultMsg revokePszb(String projectid,String pszbid) {
		ResultMsg msg = null;
		try {
			if(projectid==null || pszbid ==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.revokePszb(projectid,pszbid);
				msg = new ResultMsg(true,"撤回成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("撤回失败！"));
		}
		return msg;
	}
	/**
	 *  提交评审准备
	 * @Title: sendPszb 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @param pszbid
	 * @return 设定文件
	 */
	@RequestMapping("/sendPszb.do")
	@ResponseBody
	public ResultMsg sendPszb(String projectid,String pszbid) {
		ResultMsg msg = null;
		try {
			if(projectid==null || pszbid ==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.sendPszb(projectid,pszbid);
				msg = new ResultMsg(true,"提交成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("提交失败！"));
		}
		return msg;
	}
	/**
	 * 详情跳转
	 * @Title: prepareDetail 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/prepareDetail.do" })
	public ModelAndView prepareDetail(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/prepareReview/prepareReview_form_detail");
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		return mav;
	}
	
	/**
	 * 评审准备等是否可撤回验证
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/recallYN.do")
	@ResponseBody
	public ResultMsg recallYN(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String returnMsg;
		try {
			returnMsg = service.recallYN(param);
			if ("1".equals(returnMsg)){
				msg = new ResultMsg(true,returnMsg);
			} else {
				msg = new ResultMsg(false,returnMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
}
