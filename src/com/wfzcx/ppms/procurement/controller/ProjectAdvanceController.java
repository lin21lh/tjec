package com.wfzcx.ppms.procurement.controller;

import java.util.Map;

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
import com.jbf.sys.resource.po.SysResource;
import com.wfzcx.ppms.discern.service.ProjectDiscernService;
import com.wfzcx.ppms.procurement.service.ProjectAdvacneService;
/**
 * 项目采购预审
 * @ClassName: ProjectAdvanceController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年9月19日 上午10:51:34
 */
@Scope("prototype")
@Controller
@RequestMapping("/ppms/procurement/ProjectAdvanceController")
public class ProjectAdvanceController{
	@Autowired
	ProjectAdvacneService service;
	@Autowired
	ProjectDiscernService projectService;
	/**
	 * 初始加载
	 * @Title: init 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/procurement/advance/advance_init");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
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
	 * 查询项目
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
	 * 录入按钮跳转
	 * @Title: advanceAdd 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/advanceAdd.do")
	public ModelAndView advanceAdd(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/procurement/advance/advance_form");
		return mav;
	}
	/**
	 * 查询预审机构
	 * @Title: queryOrgan 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/queryOrgan.do")
	@ResponseBody
	public EasyUITotalResult queryOrgan(HttpServletRequest request) throws AppException {
		String projectid = StringUtil.stringConvert(request.getParameter("projectid"));
		String advanceid = StringUtil.stringConvert(request.getParameter("advanceid"));
		PaginationSupport ps = service.queryOrgan(projectid,advanceid);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 预审结果新增保存、提交
	 * @Title: advanceAddCommit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/advanceAddCommit.do")
	@ResponseBody
	public ResultMsg advanceAddCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String returnMsg = service.advanceAddCommit(param);
			String projectid = (String)param.get("projectid");
			projectService.updateXmdqhj(projectid, "3");//更新为项目采购
			if(!"".equals(returnMsg)){
				msg = new ResultMsg(false, returnMsg);
			}else {
				String workflowflag = param.get("workflowflag").toString();
				msg = new ResultMsg(true, "1".equals(workflowflag)?"送审成功！":"保存成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	/**
	 * 项目送审
	 * @Title: sendWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param projectid
	 * @param activityId
	 * @return 设定文件
	 */
	@RequestMapping("/sendWorkFlow.do")
	@ResponseBody
	public ResultMsg sendWorkFlow(String menuid,String advanceid,String activityId,String wfid) {
		ResultMsg msg = null;
		try {
			if(advanceid==null||"null".equals(advanceid)){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
			String msgString = service.sendWorkFlow(menuid,advanceid,activityId,wfid);
				if("".equals(msgString)){
					msg = new ResultMsg(true, AppException.getMessage("送审成功！"));
				}else {
					msg = new ResultMsg(false, AppException.getMessage("送审失败！"+msgString));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("送审失败！"));
		}
		return msg;
	}
	/**
	 * 撤销
	 * @Title: revokeWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param applicationId
	 * @param activityId
	 * @return 设定文件
	 */
	@RequestMapping("/revokeWorkFlow.do")
	@ResponseBody
	public ResultMsg revokeWorkFlow(String wfid,String advanceid,String activityId) {
		ResultMsg msg = null;
		try {
			if(advanceid==null||"null".equals(advanceid)){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				String msgString = service.revokeWorkFlow(wfid,advanceid,activityId);
				if("".equals(msgString)){
					msg = new ResultMsg(true, AppException.getMessage("撤回成功！"));
				}else {
					msg = new ResultMsg(false, AppException.getMessage("撤回失败！"+msgString));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("撤回失败！"+e.getMessage()));
		}
		return msg;
	}
	/**
	 * 审批跳转
	 * @Title: audit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/audit.do" })
	public ModelAndView audit(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/procurement/advance/advance_audit");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
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
	 * 审批页面跳转
	 * @Title: advanceAudit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/advanceAudit.do")
	public ModelAndView advanceAudit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/procurement/advance/advance_audit_form");
		return mav;
	}
	/**
	 * 审批同意
	 * @Title: auditWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param advanceid
	 * @param activityId
	 * @param wfid
	 * @return 设定文件
	 */
	@RequestMapping("/auditWorkFlow.do")
	@ResponseBody
	public ResultMsg auditWorkFlow(String advanceid,String activityId,String wfid,String isback,String opinion) {
		ResultMsg msg = null;
		try {
			if(advanceid==null||"null".equals(advanceid)){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
			String msgString = service.auditWorkFlow(advanceid,activityId,wfid,isback,opinion);
				if("".equals(msgString)){
					msg = new ResultMsg(true, AppException.getMessage("操作成功！"));
				}else {
					msg = new ResultMsg(false, AppException.getMessage("操作失败！"+msgString));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("送审失败！"));
		}
		return msg;
	}
	/**
	 * 查看详情
	 * @Title: advanceDetail 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/advanceDetail.do")
	public ModelAndView advanceDetail(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/procurement/advance/advance_detail_form");
		return mav;
	}
	
	/**
	 * 实时查询专家列表
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
	 * 专家
	 * @Title: advanceExpertGrid 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return EasyUITotalResult 返回类型 
	 * @throws
	 */
	@RequestMapping("/advanceExpertGrid.do")
	@ResponseBody
	public EasyUITotalResult advanceExpertGrid(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.advanceExpertGrid(map);
		return EasyUITotalResult.from(ps);
	}
}
