package com.wfzcx.ppp.xmcg.xqgs.controller;

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
import com.jbf.sys.resource.po.SysResource;
import com.wfzcx.ppp.xmcg.xqgs.service.ProjectXqgsService;

/**
 * 项目需求公示备案
 * @ClassName: ProjectXmcb 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author WangZhengke
 * @date 2016年8月4日 09:12:15
 */
@Scope("prototype")
@Controller
@RequestMapping("/ppp/projectXmcg/xqgs/controller")
public class ProjectXqgs {
	
	@Autowired
	ProjectXqgsService service;
	@Autowired
	ParamCfgComponent pcfg;
	/**
	 * 需求公示备案管理首页
	 * @Title: cgxxinit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/xqgsinit.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException{
		String operFlag = StringUtil.stringConvert(request.getParameter("operFlag"));
		ModelAndView mav = null;
		if(operFlag.equals("audit")) {
			//审核
			mav = new ModelAndView("ppp/xmcg/xqgs/xqgs_audit");
		}else {			
			mav = new ModelAndView("ppp/xmcg/xqgs/xqgs_init");
		}
		
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
	 * 需求公示查询
	 * @Title: queryProjectCgxx 
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
	 * 跳转至新增、修改form
	 * @Title: projectXqgsAdd 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/projectXqgsAdd.do")
	public ModelAndView projectXqgsAdd(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppp/xmcg/xqgs/xqgs_form");
		
		UUID uuid = UUID.randomUUID();
		String fjts = pcfg.findGeneralParamValue("SYSTEM", "FJ_XQGS");
		//新增时产生临时的uuid放在附件的keyid中
		mav.addObject("fjkeyid", uuid);
		mav.addObject("fjts", fjts);

		return mav;
	}
	/**
	 * 跳转至详情form
	 * @Title: projectXqgsAdd 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/projectXqgsView.do")
	public ModelAndView projectXqgsView(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppp/xmcg/xqgs/xqgs_view");

		return mav;
	}
	/**
	 * 跳转至审核form
	 * @Title: projectXqgsAdd 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/projectXqgsAudit.do")
	public ModelAndView projectXqgsAudit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppp/xmcg/xqgs/xqgs_audit_form");

		return mav;
	}
	/**
	 * 需求公示保存或送审
	 * @Title: projectAddCommit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/projectXqgsAddCommit.do")
	@ResponseBody
	public ResultMsg projectXqgsAddCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String returnMsg = service.projectXqgsAddCommit(param);
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
	 * 项目需求公示送审
	 * @Title: sendWorkFlow 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param xmid
	 * @param activityId
	 * @return 设定文件
	 */
	@RequestMapping("/sendWorkFlow.do")
	@ResponseBody
	public ResultMsg sendWorkFlow(String menuid,String xqgsid,String activityId,String wfid,HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			if(xqgsid==null||"null".equals(xqgsid)){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				Integer id = Integer.valueOf(xqgsid);
				String msgString = service.sendWorkFlow(menuid,xqgsid,activityId,wfid,map);
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
	 * 需求公示撤回
	 * @Title: revokeWorkFlow 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param applicationId
	 * @param activityId
	 * @return 设定文件
	 */
	@RequestMapping("/revokeWorkFlow.do")
	@ResponseBody
	public ResultMsg revokeWorkFlow(String wfid,String xqgsid,String activityId) {
		ResultMsg msg = null;
		try {
			if(xqgsid==null||"null".equals(xqgsid)){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				String msgString = service.revokeWorkFlow(wfid,xqgsid,activityId);
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
	 * 项目需求公示删除
	 * @Title: sendWorkFlow 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param xmid
	 * @param activityId
	 * @return 设定文件
	 */
	@RequestMapping("/deleteXqgs.do")
	@ResponseBody
	public ResultMsg deleteXqgs(String menuid,String xqgsid,HttpServletRequest request) {
		ResultMsg msg = null;
		try {
			if(xqgsid==null||"null".equals(xqgsid)){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				Integer id = Integer.valueOf(xqgsid);
				String msgString = service.deleteCgxxById(id);
				if("".equals(msgString)){
					msg = new ResultMsg(true, AppException.getMessage("删除成功！"));
				}else {
					msg = new ResultMsg(false, AppException.getMessage("删除失败！"+msgString));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败！"));
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
}
