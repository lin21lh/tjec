package com.wfzcx.ppp.xmkf.controller;

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
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.ppp.xmkf.po.TKfYzfs;
import com.wfzcx.ppp.xmkf.service.ProYzfsService;

@Scope("prototype")
@Controller
@RequestMapping("/ppp/xmkf/controller")
public class ProYzfsController {

	@Autowired
	ProYzfsService service;
	@Autowired
	ParamCfgComponent pcfg;
	
	/**
	 * 运作方式备案录入
	 * 初始化
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/yzfslrInit.do" })
	public ModelAndView yzfslrInit(HttpServletRequest request) throws ServletException{
		ModelAndView mav = new ModelAndView("ppp/xmkf/yzfs/yzfslr_init");
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
	
	@RequestMapping({ "/yzfsshInit.do" })
	public ModelAndView yzfsshInit(HttpServletRequest request) throws ServletException{
		ModelAndView mav = new ModelAndView("ppp/xmkf/yzfs/yzfssh_init");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		return mav;
	}
	
	/**
	 * 运作方式备案录入
	 * 查询
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/yzfslrQuery.do")
	@ResponseBody
	public EasyUITotalResult yzfslrQuery(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.yzfslrQuery(map);
		return EasyUITotalResult.from(ps);
	}
	
	@RequestMapping("/yzfsshQuery.do")
	@ResponseBody
	public EasyUITotalResult yzfsshQuery(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.yzfsshQuery(map);
		return EasyUITotalResult.from(ps);
	}
	
	@RequestMapping("/yzfslrAdd.do")
	public ModelAndView yzfslrAdd(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppp/xmkf/yzfs/yzfslr_form");
		//附件提醒，项目申报时的附件提醒内容
		String fjts = pcfg.findGeneralParamValue("SYSTEM", "FJ_YZFS");
		mav.addObject("fjts", fjts);
		return mav;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/yzfslrAddSave.do")
	@ResponseBody
	public ResultMsg yzfslrAddSave(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String yzfsid = service.getTKfYzfsByXmid(param.get("xmid").toString());
			String returnMsg = "";
			if ("".equals(yzfsid)){
				service.yzfslrAddSave(param);
				returnMsg = "1";
			} else {
				service.yzfslrEditSave(yzfsid,param);
				returnMsg = "1";
			}
			if ("1".equals(returnMsg)){
				msg = new ResultMsg(true,"保存成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping("/yzfslrDelete.do")
	@ResponseBody
	public ResultMsg yzfslrDelete(String yzfsid) {
		ResultMsg msg = null;
		try {
			service.yzfslrDelete(yzfsid);
			msg = new ResultMsg(true, AppException.getMessage("删除成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败！"));
		}
		return msg;
	}
	
	@RequestMapping("/yzfslrDetail.do")
	public ModelAndView yzfslrDetail(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppp/xmkf/yzfs/yzfslr_detail");
		return mav;
	}
	
	@RequestMapping("/yzfsshDetail.do")
	public ModelAndView yzfsshDetail(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppp/xmkf/yzfs/yzfslr_detail");
		return mav;
	}
	
	@RequestMapping("/yzfsshAudit.do")
	public ModelAndView yzfsshAudit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppp/xmkf/yzfs/yzfssh_form");
		return mav;
	}
}
