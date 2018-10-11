package com.wfzcx.ppp.xmcg.cgxx.controller;

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
import com.wfzcx.ppp.xmcg.cgxx.service.ProjectCgxxService;

/**
 * 项目采购信息备案
 * @ClassName: ProjectXmcb 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author WangZhengke
 * @date 2016年8月2日 17:45:09
 */
@Scope("prototype")
@Controller
@RequestMapping("/ppp/projectXmcg/cgxx/controller")
public class ProjectCgxx {
	
	@Autowired
	ProjectCgxxService service;
	@Autowired
	ParamCfgComponent pcfg;
	/**
	 * 采购信息备案管理首页
	 * @Title: cgxxinit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/cgxxinit.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException{
		ModelAndView mav = new ModelAndView("ppp/xmcg/cgxx/cgxx_init");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		return mav;
	}
	/**
	 * 采购信息查询
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
	 * 跳转至新增form
	 * @Title: changeAccountAddInit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/projectCgxxAdd.do")
	public ModelAndView projectCgxxAdd(HttpServletRequest request) {
		String operflag = StringUtil.stringConvert(request.getParameter("operflag"));
		ModelAndView mav = null;
		if("view".equals(operflag)) {
			mav = new ModelAndView("ppp/xmcg/cgxx/cgxx_view");
		}else {
			mav = new ModelAndView("ppp/xmcg/cgxx/cgxx_form");
		}
		
		UUID uuid = UUID.randomUUID();
		String fjts = pcfg.findGeneralParamValue("SYSTEM", "FJ_CGXX");
		//新增时产生临时的uuid放在附件的keyid中
		mav.addObject("fjkeyid", uuid);
		mav.addObject("fjts", fjts);

		return mav;
	}
	/**
	 * 采购信息保存或提交
	 * @Title: projectAddCommit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/projectCgxxAddCommit.do")
	@ResponseBody
	public ResultMsg projectCgxxAddCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String returnMsg = service.projectCgxxAddCommit(param);
			if(!"".equals(returnMsg)){
				msg = new ResultMsg(false, returnMsg);
			}else {
				String workflowflag = param.get("workflowflag").toString();
				msg = new ResultMsg(true, "1".equals(workflowflag)?"提交成功！":"保存成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	/**
	 * 项目采购信息提交
	 * @Title: sendWorkFlow 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param xmid
	 * @param activityId
	 * @return 设定文件
	 */
	@RequestMapping("/sendWorkFlow.do")
	@ResponseBody
	public ResultMsg sendWorkFlow(String menuid,String cgxxid,HttpServletRequest request) {
		ResultMsg msg = null;
		try {
			if(cgxxid==null||"null".equals(cgxxid)){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				Integer id = Integer.valueOf(cgxxid);
				String msgString = service.sendWorkFlow(menuid,id);
				if("".equals(msgString)){
					msg = new ResultMsg(true, AppException.getMessage("提交成功！"));
				}else {
					msg = new ResultMsg(false, AppException.getMessage("提交失败！"+msgString));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("提交失败！"));
		}
		return msg;
	}
	
	/**
	 * 项目采购信息删除
	 * @Title: sendWorkFlow 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param xmid
	 * @param activityId
	 * @return 设定文件
	 */
	@RequestMapping("/deleteCgxx.do")
	@ResponseBody
	public ResultMsg deleteCgxx(String menuid,String cgxxid,HttpServletRequest request) {
		ResultMsg msg = null;
		try {
			if(cgxxid==null||"null".equals(cgxxid)){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				Integer id = Integer.valueOf(cgxxid);
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
}
