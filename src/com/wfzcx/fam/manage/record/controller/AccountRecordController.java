package com.wfzcx.fam.manage.record.controller;

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

import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.fam.common.DeptComponent;
import com.wfzcx.fam.manage.po.FaAccounts;
import com.wfzcx.fam.manage.po.FaApplications;
import com.wfzcx.fam.manage.record.service.AccountRecordService;
/**
 * 账户备案
 * 
 * @ClassName: AccountRecordController
 * @Description: TODO账户备案
 * @author XinPeng
 * @date 2015年4月21日9:28:42
 */
@Scope("prototype")
@Controller
@RequestMapping("/manage/record/AccountRecordController")
public class AccountRecordController {

	@Autowired
	AccountRecordService service;

	@RequestMapping({ "/init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {

		ModelAndView mav = new ModelAndView("fam/manage/record/record_init");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid",menuid);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//权限内的所有机构
		String allbdgagency = DeptComponent.getCurAndLowerCodeForYsdw(menuid, "code");
		mav.addObject("allbdgagency",allbdgagency);
		SysUser user = SecureUtil.getCurrentUser();
		mav.addObject("userid",user.getUserid());

		return mav;
	}

	/**
	 * 账户备案查询
	 * 
	 * @param request
	 * @return
	 * @throws AppException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryChangeAccount.do")
	@ResponseBody
	public EasyUITotalResult queryChangeAccount(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryChangeAccount(map);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 账户可备案查询
	 * 
	 * @param request
	 * @return
	 * @throws AppException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryAccount.do")
	@ResponseBody
	public EasyUITotalResult queryAccount(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryAccount(map);
		return EasyUITotalResult.from(ps);
	}

	/**
	 * 申请弹出（修改、详情）框
	 * @Title: recordAddInit
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param request
	 * @param @return 设定文件
	 * @return ModelAndView 返回类型
	 */
	@RequestMapping("/recordAddInit.do")
	public ModelAndView recordAddInit(HttpServletRequest request) {
		ModelAndView mav =null;
		// 获取session用于附件上传
		HttpSession session = request.getSession();
		String optType = request.getParameter("optType");
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("sessionId", session.getId());
		modelMap.put("optType", optType);
		
		if(optType.equals("view")){
			mav = new ModelAndView("/fam/manage/record/recordAccountView", "modelMap", modelMap);
		}else{
			mav = new ModelAndView("/fam/manage/record/recordAccountForm", "modelMap", modelMap);
		}
		
		return mav;
	}

	/**
	 * 新增保存
	 * @Title: saveSubmitRecordInfo
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param request
	 * @param @param faAccounts
	 * @param @return 设定文件
	 * @return ResultMsg 返回类型
	 */
	@ResponseBody
	@RequestMapping("/recordSaveAdd.do")
	public ResultMsg saveSubmitRecordInfo(HttpServletRequest request,
			FaAccounts faAccounts) {
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			service.saveSubmitRecordInfo(map, faAccounts);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
		return msg;
	}

	/**撤回
	 * 
	 * @Title: removeRecordInfo
	 * @Description: TODO(账户备案-撤回)
	 * @param @param request
	 * @param @return 设定文件
	 * @return ResultMsg 返回类型
	 */
	@RequestMapping("/removeRecordInfo.do")
	@ResponseBody
	public ResultMsg removeRecordInfo(HttpServletRequest request) {

		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			service.removeRecordInfo(map);
			msg = new ResultMsg(true, "撤回成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, "撤回失败," );
		}
		return msg;
	}

	/**
	 * 
	 * @Title: submitRecordInfo
	 * @Description: TODO(送审-账户备案)
	 * @param @param HttpServletRequest request
	 * @param @return 设定文件
	 * @return ResultMsg 返回类型
	 */
	@RequestMapping("/submitRecordInfo.do")
	@ResponseBody
	public ResultMsg submitRecordInfo(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			service.submitRecordInfo(map);
			msg = new ResultMsg(true, "送审成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, "送审失败");
		}
		return msg;
	}

	/**
	 * 
	 * @Title: initAudit
	 * @Description: TODO(账户备案-审核主界面)
	 * @param @param menuid
	 * @param @param activityId
	 * @param @param firstNode
	 * @param @param lastNode
	 * @param @return
	 * @param @throws ServletException 设定文件
	 * @return ModelAndView 返回类型
	 */
	@RequestMapping({ "/audit_init.do" })
	public ModelAndView initAudit(String menuid) throws ServletException {

		ModelAndView mav = new ModelAndView("fam/manage/record/record_audit_init");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//权限内的所有机构
		String allbdgagency = DeptComponent.getCurAndLowerCodeForYsdw(menuid, "code");
		mav.addObject("allbdgagency",allbdgagency);
		return mav;
	}

	/**
	 * 初始化初审弹出页面
	 * @Title: recordAuditInit
	 * @Description: TODO(初始化初审弹出页面)
	 * @param @param request
	 * @param @return 设定文件
	 * @return ModelAndView 返回类型	 
	 */
	@RequestMapping("/recordAuditInitUrl.do")
	public ModelAndView recordAuditInit(HttpServletRequest request) {
		
		ModelAndView mav =null;
		//获取参数
		String wfid = request.getParameter("wfid");
		String activityId = request.getParameter("activityId");
		String optType = request.getParameter("optType");
		
		if(optType.equals("view")){
			mav = new ModelAndView("fam/manage/record/record_audit_view");
		}else{
			mav = new ModelAndView("fam/manage/record/record_audit_form");
		}
		//根据wfid和activityId查询能否编辑标志
		boolean formIsEdit = true;
		try {
			formIsEdit = service.getTaskFormEditable(wfid, activityId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List list =service.getworkFlowList(wfid);
		mav.addObject("optType", optType);
		mav.addObject("comeFlag", "audit");
		mav.addObject("formIsEdit", formIsEdit);
		mav.addObject("wfList",list);
		return mav;
	}

	/**
	 * 单个审批
	 * @Title: operateRecordInfo
	 * @Description: TODO(单个审批)
	 * @param @param request
	 * @param @param faApplication
	 * @param @return 设定文件
	 * @return ResultMsg 返回类型
	 */
	@RequestMapping("/operateRecordInfo.do")
	@ResponseBody
	public ResultMsg operateRecordInfo(HttpServletRequest request,
			FaApplications faApplication) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.verifyWorkFlow(param, faApplication);
			msg = new ResultMsg(true, AppException.getMessage("操作成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("操作失败！"));
		}
		return msg;
	}
}
