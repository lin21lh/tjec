package com.wfzcx.ppms.transfer.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.alibaba.fastjson.JSONObject;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.resource.po.SysResource;
import com.wfzcx.ppms.discern.service.ProjectDiscernService;
import com.wfzcx.ppms.transfer.service.ProjectTransferService;

@Scope("prototype")
@Controller
@RequestMapping("/ppms/transfer/ProjectTransferController")
public class ProjectTransferController {
	
	@Autowired
	ProjectTransferService service;
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
		ModelAndView mav = new ModelAndView("ppms/transfer/transfer_init");
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
	 * 录入页面跳转
	 * @Title: transferAdd 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/transferAdd.do" })
	public ModelAndView transferAdd(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/transfer/transfer_form");
		return mav;
	}
	/**
	 * 
	 * @Title: transferSave 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/transferSave.do")
	@ResponseBody
	public ResultMsg transferSave(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String returnMsg = service.transferSave(param);
			String projectid = (String)param.get("projectid");
			projectService.updateXmdqhj(projectid, "5");
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
	 * 查询项目移交信息
	 * @Title: queryTransfer 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/queryTransfer.do")
	@ResponseBody
	public ResultMsg queryTransfer(HttpServletRequest request) {
		ResultMsg msg =new ResultMsg(true, "");
		String transferid =StringUtil.stringConvert(request.getParameter("transferid"));
		List approveList = new ArrayList();
		try {
			approveList = service.queryTransfer(transferid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		HashMap<String, Object> modelMap =new HashMap();
		if(!approveList.isEmpty()){
			JSONObject  json = (JSONObject) JSONObject.toJSON(approveList.get(0));
			modelMap.put("transfer", json);
			msg.setBody(modelMap);
		}else {
			modelMap.put("transfer", "");
			msg.setBody(modelMap);
		}
		return msg;
	}
	/**
	 * 工作流送审
	 * @Title: sendWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param transferid
	 * @param activityId
	 * @param wfid
	 * @return 设定文件
	 */
	@RequestMapping("/sendWorkFlow.do")
	@ResponseBody
	public ResultMsg sendWorkFlow(String menuid,String transferid,String activityId,String wfid) {
		ResultMsg msg = null;
		try {
			if(transferid==null||"null".equals(transferid)){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
			String msgString = service.sendWorkFlow(menuid,transferid,activityId,wfid);
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
	 * @param tranfserid
	 * @param activityId
	 * @return 设定文件
	 */
	@RequestMapping("/revokeWorkFlow.do")
	@ResponseBody
	public ResultMsg revokeWorkFlow(String wfid,String transferid,String activityId) {
		ResultMsg msg = null;
		try {
			if(transferid==null||"null".equals(transferid)){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				String msgString = service.revokeWorkFlow(wfid,transferid,activityId);
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
	@RequestMapping({ "/audit.do" })
	public ModelAndView audit(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/transfer/transfer_audit");
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
	 * 明细页面
	 * @Title: detail 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/detail.do" })
	public ModelAndView detail(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/transfer/transfer_detail");
		String audit = StringUtil.stringConvert(request.getParameter("audit"));
		mav.addObject("audit", audit);
		return mav;
	}
	/**
	 * 退回处理
	 * @Title: auditOpinion 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/auditOpinion.do" })
	public ModelAndView auditOpinion(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/transfer/transfer_audit_opinion");
		return mav;
	}
	/**
	 * 审批同意
	 * @Title: auditWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param transferid
	 * @param activityId
	 * @param wfid
	 * @return 设定文件
	 */
	@RequestMapping("/auditWorkFlow.do")
	@ResponseBody
	public ResultMsg auditWorkFlow(String transferid,String activityId,String wfid,String isback,String opinion) {
		ResultMsg msg = null;
		try {
			if(transferid==null||"null".equals(transferid)){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
			String msgString = service.auditWorkFlow(transferid,activityId,wfid,isback,opinion);
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
	@RequestMapping({ "/transferDetail.do" })
	public ModelAndView transferDetail(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/transfer/transfer_detail_form");
		return mav;
	}
}
