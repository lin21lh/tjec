package com.wfzcx.ppp.xmcb.controller;

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
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.ppp.xmcb.service.ProjectXmcbService;

/**
 * 项目储备（项目申报、初步识别、发改委识别）
 * @ClassName: ProjectXmcb 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2016年7月26日 上午10:00:54
 */
@Scope("prototype")
@Controller
@RequestMapping("/ppp/projectXmcb/controller")
public class ProjectXmcbController {
	
	@Autowired
	ProjectXmcbService service;
	@Autowired
	ParamCfgComponent pcfg;
	/**
	 * 项目申报管理首页
	 * @Title: xmsbinit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/xmsbinit.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException{
		ModelAndView mav = new ModelAndView("ppp/xmcb/xmcb_init");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		SysUser user = SecureUtil.getCurrentUser();
		String dwmc = user.getOrgname();
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
	 * 项目查询
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
	 * 跳转至新增form
	 * @Title: changeAccountAddInit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/projectAdd.do")
	public ModelAndView projectAdd(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppp/xmcb/xmcb_form");
		UUID uuid = UUID.randomUUID();
		//附件提醒，项目申报时的附件提醒内容
		String fjts = pcfg.findGeneralParamValue("SYSTEM", "FJ_XMCB");
		//新增时产生临时的uuid放在附件的keyid中
		mav.addObject("fjkeyid", uuid);
		mav.addObject("fjts", fjts);
		return mav;
	}
	/**
	 * 项目保存或送审
	 * @Title: projectAddCommit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
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
	 * 项目修改送审
	 * @Title: projectEditCommit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
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
	 * 项目送审
	 * @Title: sendWorkFlow 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param xmid
	 * @param activityId
	 * @return 设定文件
	 */
	@RequestMapping("/sendWorkFlow.do")
	@ResponseBody
	public ResultMsg sendWorkFlow(String menuid,String xmid,String activityId,String wfid,HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			if(xmid==null||"null".equals(xmid)){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
			String msgString = service.sendWorkFlow(menuid,xmid,activityId,wfid,map);
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
	 * 项目初步识别（财政识别）
	 * @Title: audit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/xmcbsb.do" })
	public ModelAndView audit(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppp/xmcb/xmcb_cbsb_init");
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
		ModelAndView mav = new ModelAndView("ppp/xmcb/xmcb_cbsb_form");
		return mav;
	}
	
	/**
	 * 项目初步识别
	 * @Title: sendWorkFlow 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param xmid
	 * @param activityId
	 * @return 设定文件
	 */
	@RequestMapping("/auditWorkFlow.do")
	@ResponseBody
	public ResultMsg auditWorkFlow(String menuid,String xmid,String activityId,String wfid,HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			if(xmid==null||"null".equals(xmid)){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
			String msgString = service.auditWorkFlow(xmid,activityId,wfid,map);
				if("".equals(msgString)){
					msg = new ResultMsg(true, AppException.getMessage("审核成功！"));
				}else {
					msg = new ResultMsg(false, AppException.getMessage("审核失败！"+msgString));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("审核失败！"));
		}
		return msg;
	}
	/**
	 * 发改委识别
	 * @Title: auditForFgw 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/xmfgwsb.do" })
	public ModelAndView auditForFgw(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppp/xmcb/xmcb_fgwsb_init");
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
	 * 审核页面跳转 发改委识别
	 * @Title: auditEntryForFgw 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/auditEntryForFgw.do")
	public ModelAndView auditEntryForFgw(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppp/xmcb/xmcb_fgwsbsb_form");
		return mav;
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
	public ResultMsg projectDelete(String xmid) {
		ResultMsg msg = null;
		try {
			if(xmid==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.projectDelete(xmid);
				msg = new ResultMsg(true, AppException.getMessage("删除成功！"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败！"));
		}
		return msg;
	}
	/**
	 * 项目撤回
	 * @Title: revokeWorkFlow 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param applicationId
	 * @param activityId
	 * @return 设定文件
	 */
	@RequestMapping("/revokeWorkFlow.do")
	@ResponseBody
	public ResultMsg revokeWorkFlow(String wfid,String xmid,String activityId) {
		ResultMsg msg = null;
		try {
			if(xmid==null||"null".equals(xmid)){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				String msgString = service.revokeWorkFlow(wfid,xmid,activityId);
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
	 * 审批退回
	 * @Title: backWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/backWorkFlow.do")
	@ResponseBody
	public ResultMsg backWorkFlow(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String returnMsg = service.backWorkFlow(param);
			if("".equals(returnMsg)){
				msg = new ResultMsg(true, AppException.getMessage("退回成功！"));
			}else{
				msg = new ResultMsg(true, AppException.getMessage("退回失败！"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("退回失败！"+e.getMessage()));
		}
		return msg;
	}
	/**
	 * 退回时填写的审批意见页面跳转
	 * @Title: accountBatchOperation 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @param activityId
	 * @return 设定文件
	 */
	@RequestMapping("/auditOpinion.do")
	public ModelAndView accountBatchOperation(String wfid,String activityId){
		ModelAndView mav = new ModelAndView("ppp/common/audit_opinion");
		return mav;
	}
	/**
	 * 明细跳转
	 * @Title: projectDetail 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @param activityId
	 * @return 设定文件
	 */
	@RequestMapping("/projectDetail.do")
	public ModelAndView projectDetail(String wfid,String activityId){
		ModelAndView mav = new ModelAndView("ppp/xmcb/xmcb_detail_form");
		return mav;
	}
}
