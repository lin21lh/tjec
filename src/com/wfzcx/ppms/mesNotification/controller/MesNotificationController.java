package com.wfzcx.ppms.mesNotification.controller;

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

import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.wfzcx.ppms.mesNotification.service.MesNotificationService;

/**
 * 消息通知Controller控制类
 * @author wang_yliang
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/ppms/mesNotification/MesNotificationController")
public class MesNotificationController {

	@Autowired
	MesNotificationService service;
	@Autowired
	ParamCfgComponent pcfg;

	
	/**
	 * 初始加载
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping("/init.do")
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/mesNotification/mesNotification_init");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * 消息通知查询
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryMessage.do")
	@ResponseBody
	public EasyUITotalResult queryMessage(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryMessage(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 跳转至新增页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/addMessage.do")
	public ModelAndView addInit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/mesNotification/mesNotification_form");
		//页面最大接收参数
		String maxRec = pcfg.findGeneralParamValue("SYSTEM", "MES_RECEIVERS_MAX");
		mav.addObject("maxRec", maxRec);
		return mav;
	}
	
	/**
	 * 消息通知新增保存
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addCommitMessage.do")
	@ResponseBody
	public ResultMsg messageAddCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String status = StringUtil.stringConvert(param.get("status"));
		String returnMsg;
		try {
			returnMsg = service.messageAddCommit(param);
			if ("success".equals(returnMsg)){
				if ("0".equals(status)){
					msg = new ResultMsg(true,"保存成功！");
				} else {
					msg = new ResultMsg(true,"保存并发送！");
				}
			} else {
				if ("0".equals(status)){
					msg = new ResultMsg(false,"保存失败！");
				} else {
					msg = new ResultMsg(false,"保存发送失败！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * 消息通知修改保存
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/editCommitMessage.do")
	@ResponseBody
	public ResultMsg messageEditCommit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String status = StringUtil.stringConvert(param.get("status"));
		String returnMsg;
		try {
			returnMsg = service.messageEditCommit(param);
			if ("success".equals(returnMsg)){
				if ("0".equals(status)){
					msg = new ResultMsg(true,"保存成功！");
				} else {
					msg = new ResultMsg(true,"保存并发送！");
				}
			} else {
				if ("0".equals(status)){
					msg = new ResultMsg(false,"保存失败！");
				} else {
					msg = new ResultMsg(false,"保存发送失败！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * 消息通知删除
	 * @param messageid
	 * @return
	 */
	@RequestMapping("/deleteMessage.do")
	@ResponseBody
	public ResultMsg messageDelete(String messageid) {
		ResultMsg msg = null;
		try {
			if(messageid==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.messageDelete(messageid);
				msg = new ResultMsg(true, AppException.getMessage("删除成功！"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败！"));
		}
		return msg;
	}
	
	/**
	 * 消息发送
	 * @param messageid
	 * @return
	 */
	@RequestMapping("/sendMessage.do")
	@ResponseBody
	public ResultMsg messageSend(String messageid) {
		ResultMsg msg = null;
		try {
			if(messageid==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.messageSend(messageid);
				msg = new ResultMsg(true, AppException.getMessage("发送成功！"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败！"));
		}
		return msg;
	}
	
	/**
	 * 跳转至消息详情
	 * @param request
	 * @return
	 */
	@RequestMapping("/detailMessage.do")
	public ModelAndView detailInit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("ppms/mesNotification/mesNotification_detail");
		return mav;
	}
	
	/**
	 * 跳转至选择接收界面
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping("/showPersonList.do")
	public ModelAndView showPersonList(HttpServletRequest request) throws ServletException
	{
		ModelAndView mav = new ModelAndView("ppms/mesNotification/chooseReceiver");
		
		return mav;
	}
	
	/**
	 * 查询未选中所有接收人
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryUnselectedUser.do")
	@ResponseBody
	public List queryUnselectedUser(HttpServletRequest request) throws AppException 
	{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		
		return service.queryUnselectedUser(map);
	}
	
	/**
	 * 查询已选中的接收人
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/querySelectedUser.do")
	@ResponseBody
	public List querySelectedUser(HttpServletRequest request)throws AppException 
	{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		return service.querySelectedUser(map);
	}
	
}
