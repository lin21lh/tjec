package com.wfzcx.ppms.discern.controller;

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
/**
 * 项目申报
 * @ClassName: ProjectDiscernController 
 * @Description: (这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年9月12日 上午8:17:49
 */
@Scope("prototype")
@Controller
@RequestMapping("/ppms/discern/ProjectDiscernController")
public class ProjectDiscernController {
	
	@Autowired
	ProjectDiscernService service;
	/**
	 * 初始加载
	 * @Title: init 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/discern/discern_init");
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
	 * 跳转至新增
	 * @Title: changeAccountAddInit 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/projectAdd.do")
	public ModelAndView changeAccountAddInit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/discern/discern_form");
		return mav;
	}
	/**
	 * 项目新增保存、提交
	 * @Title: projectAddCommit 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/projectAddCommit.do")
	@ResponseBody
	public ResultMsg projectAddCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String returnMsg = service.projectAddCommit(param);
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
	 * 查询项目产出物
	 * @Title: queryProduct 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/queryProduct.do")
	@ResponseBody
	public EasyUITotalResult queryProduct(HttpServletRequest request) throws AppException {
		String projectid = StringUtil.stringConvert(request.getParameter("projectid"));
		PaginationSupport ps = service.queryProduct(projectid);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 项目修改保存
	 * @Title: projectEditCommit 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/projectEditCommit.do")
	@ResponseBody
	public ResultMsg projectEditCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String returnMsg = service.projectEditCommit(param);
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
	 * 项目删除
	 * @Title: projectDelete 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @return 设定文件
	 */
	@RequestMapping("/projectDelete.do")
	@ResponseBody
	public ResultMsg projectDelete(String projectid) {
		ResultMsg msg = null;
		try {
			if(projectid==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.projectDelete(projectid);
				msg = new ResultMsg(true, AppException.getMessage("删除成功！"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败！"));
		}
		return msg;
	}
	/**
	 * 项目送审
	 * @Title: sendWorkFlow 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param projectid
	 * @param activityId
	 * @return 设定文件
	 */
	@RequestMapping("/sendWorkFlow.do")
	@ResponseBody
	public ResultMsg sendWorkFlow(String menuid,String projectid,String activityId,String wfid) {
		ResultMsg msg = null;
		try {
			if(projectid==null||"null".equals(projectid)){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
			String msgString = service.sendWorkFlow(menuid,projectid,activityId,wfid);
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
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param applicationId
	 * @param activityId
	 * @return 设定文件
	 */
	@RequestMapping("/revokeWorkFlow.do")
	@ResponseBody
	public ResultMsg revokeWorkFlow(String wfid,String projectid,String activityId) {
		ResultMsg msg = null;
		try {
			if(projectid==null||"null".equals(projectid)){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				String msgString = service.revokeWorkFlow(wfid,projectid,activityId);
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
		ModelAndView mav = new ModelAndView("ppms/discern/discern_audit");
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
	 * 审核页面跳转
	 * @Title: auditEntry 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/auditEntry.do")
	public ModelAndView auditEntry(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/discern/discern_audit_form");
		return mav;
	}
	/**
	 * 第三方评审机构查询
	 * @Title: queryThirdOrg 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/queryThirdOrg.do")
	@ResponseBody
	public EasyUITotalResult queryThirdOrg(HttpServletRequest request) throws AppException {
		String projectid = StringUtil.stringConvert(request.getParameter("projectid"));
		PaginationSupport ps = service.queryThirdOrg(projectid);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 财政预算支出查询
	 * @Title: queryFinance 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/queryFinance.do")
	@ResponseBody
	public EasyUITotalResult queryFinance(HttpServletRequest request) throws AppException {
		String projectid = StringUtil.stringConvert(request.getParameter("projectid"));
		PaginationSupport ps = service.queryFinance(projectid);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 审批
	 * @Title: auditCommit 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/auditCommit.do")
	@ResponseBody
	public ResultMsg auditCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String returnMsg = service.auditCommit(param);
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
	@RequestMapping("/queryApprove.do")
	@ResponseBody
	public ResultMsg queryApprove(HttpServletRequest request) {
		ResultMsg msg =new ResultMsg(true, "");
		String projectid =StringUtil.stringConvert(request.getParameter("projectid"));
		List approveList = new ArrayList();
		try {
			approveList = service.queryApprove(projectid);
		} catch (Exception e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, Object> modelMap =new HashMap();
		if(!approveList.isEmpty()){
			JSONObject  json = (JSONObject) JSONObject.toJSON(approveList.get(0));
			modelMap.put("approve", json);
			msg.setBody(modelMap);
		}else {
			modelMap.put("approve", "");
			msg.setBody(modelMap);
		}
		return msg;
	}
	/**
	 * 审批意见
	 * @Title: accountBatchOperation 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @param activityId
	 * @return 设定文件
	 */
	@RequestMapping("/auditOpinion.do")
	public ModelAndView accountBatchOperation(String wfid,String activityId){
		ModelAndView mav = new ModelAndView("ppms/discern/discern_audit_opinion");
		return mav;
	}
	/**
	 * 退回
	 * @Title: auditOperate 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/auditOperate.do")
	@ResponseBody
	public ResultMsg auditOperate(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.auditOperate(param);
			msg = new ResultMsg(true, AppException.getMessage("操作成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("操作失败！"));
		}
		return msg;
	}
	/**
	 * 流程信息
	 * @Title: workFlowMessage 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/workFlowMessage.do" })
	public ModelAndView workFlowMessage(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/common/workflow_message");
		mav.addObject("wfid",request.getParameter("wfid"));
		List list =service.getworkFlowList(request.getParameter("wfid"));
		mav.addObject("wfList",list);
		return mav;
	}
	/**
	 * 附件上传
	 * @Title: showUploadifyView 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/uploadify.do")
	public ModelAndView showUploadifyView(HttpServletRequest request){
		//获取session用于附件上传
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView("ppms/common/ppms_uploadify");
		mav.addObject("sessionId", session.getId());//session
		return mav;
	}
	/**
	 * 跳转至明细
	 * @Title: projectDetail 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/projectDetail.do")
	public ModelAndView projectDetail(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/discern/discern_detail");
		return mav;
	}
	@RequestMapping("/projectSpDetail.do")
	public ModelAndView projectSpDetail(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/discern/discern_audit_detail_form");
		return mav;
	}
}
