package com.wfzcx.fam.manage.change.controller;

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
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.fam.common.DeptComponent;
import com.wfzcx.fam.manage.RecordType;
import com.wfzcx.fam.manage.change.service.AccountChangeService;
import com.wfzcx.fam.manage.po.FavApplAccount;
/**
 * 账户的变更
 * @ClassName: AccountChangeController 
 * @Description: TODO账户变更实现类
 * @author XinPeng
 * @date 2015年4月14日 上午9:18:02
 */
@Scope("prototype")
@Controller
@RequestMapping("/manage/change/AccountChangeController")
public class AccountChangeController {
	
	@Autowired
	AccountChangeService service;
	@Autowired
	ParamCfgComponent pcfg;
	
	@RequestMapping({ "/init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("fam/manage/change/change_init");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
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
		mav.addObject("menuid",menuid);
		mav.addObject("type",RecordType.CHANGE.getIndex());
		//权限内的所有机构
		String allbdgagency = DeptComponent.getCurAndLowerCodeForYsdw(menuid, "code");
		mav.addObject("allbdgagency",allbdgagency);
		SysUser user = SecureUtil.getCurrentUser();
		mav.addObject("userid",user.getUserid());
		mav.addObject("accountbgmodel", pcfg.findGeneralParamValue("SYSTEM","ACCOUNTBGMODEL"));
		return mav;
	}
	
	/**
	 *账户可变更列表查询
	 * @param request
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryAccount.do")
	@ResponseBody
	public EasyUITotalResult queryAccount(HttpServletRequest request)  throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryAccount(map);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 查询所有列表（新开立、变更、撤销、审核、审批）
	 * @param request
	 * @return 
	 * @throws AppException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryAllAccount.do")
	@ResponseBody
	public EasyUITotalResult queryChangeAccount(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryChangeAccount(map);
		return EasyUITotalResult.from(ps);
	}
	/**
	 *  
	 * @Title: changeAccountAddInit 
	 * @Description: TODO加载新增、修改form
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping("/changeAccountAddInit.do")
	public ModelAndView changeAccountAddInit(HttpServletRequest request) {
		//simple 1代表变更模式
		String simple = StringUtil.stringConvert(request.getParameter("simple"));
		String wfid = StringUtil.stringConvert(request.getParameter("wfid"));
		String activityId = StringUtil.stringConvert(request.getParameter("activityId"));
		String changeType = StringUtil.stringConvert(request.getParameter("changeType"));
		ModelAndView mav = new ModelAndView("/fam/manage/change/changeAccountForm");
		mav.addObject("type",RecordType.CHANGE.getIndex());//备案类型
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		boolean isEdit = service.getOperatenum(wfid);
		String flagString = isEdit?"Y":pcfg.findGeneralParamValue("ZHBG", activityId.toUpperCase());
		if("1".equals(simple)){
			if("3".equals(changeType)||"4".equals(changeType)){//银行账户、开户行
				if("Y".equals(flagString)){
					mav.addObject("display", "true");
				}
			}else {
				mav.addObject("display", "true");
			}
		}else {
		  mav.addObject("display", "true");
		}
		return mav;
	}
	/**
	 *  
	 * @Title: changeAccountTypeSelect 
	 * @Description: TODO加载新增、修改form
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping("/changeAccountTypeSelect.do")
	public ModelAndView changeAccountTypeSelect(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/fam/manage/change/changeTypeForm");
		return mav;
	}
	
	/**
	 * 
	 * @Title: changeAccountSave 
	 * @Description: TODO新增保存
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping("/changeAccountSaveAdd.do")
	@ResponseBody
	public ResultMsg changeAccountSaveAdd(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String applicationId =service.changeAccountSaveAdd(param);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr")+e.getMessage());
		}
		return msg;
	}
	/**
	 * 
	 * @Title: changeAccountSave 
	 * @Description: TODO新增保存
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping("/changeAccountSaveEdit.do")
	@ResponseBody
	public ResultMsg changeAccountSaveEdit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.changeAccountSaveEdit(param);
			String applicationId =StringUtil.stringConvert(param.get("applicationId"));
			boolean fileIsUpload =  service.fileUpload(Integer.parseInt(applicationId), StringUtil.stringConvert(param.get("itemids")));
			if (!fileIsUpload) {
				msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
			}else {
				msg = new ResultMsg(true, "账户变更操作成功，但附件关联失败！");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
		return msg;
	}
	/**
	 * 
	 * @Title: changeAccountDelete 
	 * @Description: TODO选中删除变更
	 * @param @param applicationId
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/changeAccountDelete.do")
	@ResponseBody
	public ResultMsg changeAccountDelete(String applicationId) {
		ResultMsg msg = null;
		try {
			if(applicationId==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.changeAccountDelete(applicationId);
				msg = new ResultMsg(true, AppException.getMessage("删除成功！"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败！"));
		}
		return msg;
	}
	/**
	 * 批量送审
	 * @Title: sendWorkFlow 
	 * @Description: TODO账户变更批量送审
	 * @param @param menuid
	 * @param @param applicationId
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/sendWorkFlow.do")
	@ResponseBody
	public ResultMsg sendWorkFlow(String menuid,String applicationId,String activityId) {
		ResultMsg msg = null;
		try {
			if(applicationId==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.sendWorkFlow(menuid,applicationId,activityId);
				msg = new ResultMsg(true, AppException.getMessage("送审成功！"));
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
	public ResultMsg revokeWorkFlow(String menuid,String applicationId,String activityId) {
		ResultMsg msg = null;
		try {
			if(applicationId==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				String msgString = service.revokeWorkFlow(menuid,applicationId,activityId);
				if("".equals(msgString)){
					msg = new ResultMsg(true, AppException.getMessage("撤回成功！"));
				}else {
					msg = new ResultMsg(false, AppException.getMessage("撤回失败！"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("撤回失败！"+e.getMessage()));
		}
		return msg;
	}
	/**
	 * 新增变更审核审批
	 * @Title: auditEntry 
	 * @Description: TODO变更审核审批入口
	 * @param @param menuid
	 * @param @param activityId
	 * @param @return
	 * @param @throws ServletException 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping({ "/auditEntry.do" })
	public ModelAndView auditEntry(String menuid,String supply) throws ServletException {
		ModelAndView mav = new ModelAndView("fam/manage/change/change_audit");
		mav.addObject("menuid",menuid);
		mav.addObject("supply",supply);
		mav.addObject("type",RecordType.CHANGE.getIndex());
		//权限内的所有机构
		String allbdgagency = DeptComponent.getCurAndLowerCodeForYsdw(menuid, "code");
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
		mav.addObject("allbdgagency",allbdgagency);
		mav.addObject("accountbgmodel", pcfg.findGeneralParamValue("SYSTEM","ACCOUNTBGMODEL"));
		return mav;
	}
	/**
	 * 
	 * @Title: auditEntryForm 
	 * @Description: 审批加载form
	 * @param @param request
	 * @param @return
	 * @param @throws ServletException 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping({ "/auditEntryForm.do" })
	public ModelAndView auditEntryForm(HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();
		String wfid =request.getParameter("wfid");
		String activityId =request.getParameter("activityId");
		String type =request.getParameter("type");
		//根据wfid和activityId查询能否编辑标志
		boolean formIsEdit = service.getTaskFormEditable(wfid, activityId);
		boolean backToFirstNode = service.getCanBackToFirstNode(wfid, activityId);
		if (formIsEdit) {
			mav = new ModelAndView("fam/manage/change/changeAccountForm");
		}else{
			mav = new ModelAndView("fam/manage/change/changeAccountDetailForm");
		}
		String messageIsUse = service.findGeneralParamValue();
		mav.addObject("backToFirstNode",backToFirstNode);
		mav.addObject("comeFlag","audit");
		mav.addObject("messageIsUse",messageIsUse);
		mav.addObject("formIsEdit",formIsEdit);
		List list =service.getworkFlowList(wfid);
		//节点是否能显示账户信息
		String changeType = service.getChangeType(wfid);
		if("".equals(changeType)){
			throw new Exception("流程未取到相应的变更事项！");
		}
		boolean isEdit = service.getOperatenum(wfid);
		String flagString = isEdit?"Y":pcfg.findGeneralParamValue("ZHBG", activityId.toUpperCase());
		String display = "";
		if("3".equals(changeType)||"4".equals(changeType)){
			if("Y".equals(flagString)){
				display = "true";
			}
		}else{
			display = "true";
		}
		mav.addObject("display",display);
		mav.addObject("wfList",list);
		return mav;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryChangeAccountForAudit.do")
	@ResponseBody
	public EasyUITotalResult queryChangeAccountForAudit(HttpServletRequest request) {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryChangeAccountForAudit(map);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 新增变更审批
	 * @Title: changeAccountApprove 
	 * @Description: TODO变更审批
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/changeAccountApprove.do")
	@ResponseBody
	public ResultMsg changeAccountApprove(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.changeAccountApprove(param);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("操作失败！"));
		}
		return msg;
	}
	/**
	 * 退回
	 * @Title: sendBackTask 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param applicationId
	 * @param activityId
	 * @return 设定文件
	 */
	@RequestMapping("/sendBackTask.do")
	@ResponseBody
	public ResultMsg sendBackTask(String menuid,String applicationId,String activityId,String opinion) {
		ResultMsg msg = null;
		try {
			if(applicationId==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				String s = service.sendBackTask(menuid,applicationId,activityId,opinion);
				if("".equals(s)){
					msg = new ResultMsg(true, AppException.getMessage("退回成功！"));
				}else {
					msg = new ResultMsg(false, AppException.getMessage("退回失败！"+s));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("退回失败！"));
		}
		return msg;
	}
	/**
	 * 除新增外的审核业务
	 * @Title: verifyWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/verifyWorkFlow.do")
	@ResponseBody
	public ResultMsg verifyWorkFlow(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String msgString = service.verifyWorkFlow(param);
			
			if("".equals(msgString)){
				msg = new ResultMsg(true, "处理成功");
			}else {
				msg = new ResultMsg(false, "处理失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("操作失败！"));
		}
		return msg;
	}
	/**
	 * 工作流流程信息
	 * @Title: workFlowMessage 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/workFlowMessage.do" })
	public ModelAndView workFlowMessage(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("fam/manage/common/workflow_message");
		mav.addObject("wfid",request.getParameter("wfid"));
		List list =service.getworkFlowList(request.getParameter("wfid"));
		mav.addObject("wfList",list);
		return mav;
	}
	/**
	 * 明细form
	 * @Title: detailFormEntry 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("detailFormEntry.do")
	public ModelAndView detailFormEntry(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("fam/manage/change/changeAccountDetailForm");
		mav.addObject("wfid",request.getParameter("wfid"));
		List list =service.getworkFlowList(request.getParameter("wfid"));
		mav.addObject("wfList",list);
		String wfid = StringUtil.stringConvert(request.getParameter("wfid"));
		String activityId = StringUtil.stringConvert(request.getParameter("activityId"));
		String simple = StringUtil.stringConvert(request.getParameter("simple"));
		String changeType = service.getChangeType(wfid);
		boolean isEdit = service.getOperatenum(wfid);
		String flagString = isEdit?"Y":pcfg.findGeneralParamValue("ZHBG", activityId.toUpperCase());
		if("1".equals(simple)){
			if("3".equals(changeType)||"4".equals(changeType)){//银行账户、开户行
				if("Y".equals(flagString)){
					mav.addObject("display", "true");
				}
			}else {
				mav.addObject("display", "true");
			}
		}else {
		  mav.addObject("display", "true");
		}
		return mav;
	}
	/**
	 * 原账户信息
	 * @Title: oldAccountDetial 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("oldAccountDetial.do")
	public ModelAndView oldAccountDetial(HttpServletRequest request) {
		String viewName = "fam/manage/common/accountDetial";
		HashMap<String, Object> modelMap =new HashMap();
		String applicationId = StringUtil.stringConvert(request.getParameter("applicationId"));
		if (StringUtil.isNotBlank(applicationId)) {
			FavApplAccount fav = service.getOldAccountInformation(applicationId);
			if (fav !=null) {
				modelMap.put("oldAccountName", StringUtil.stringConvert(fav.getOldAccountName()));
				modelMap.put("oldAccountNumber",StringUtil.stringConvert(fav.getOldAccountNumber()));
				modelMap.put("oldBankName",StringUtil.stringConvert(fav.getOldBankName()));
				modelMap.put("oldType02Name",StringUtil.stringConvert(fav.getOldType02Name()));
				modelMap.put("oldAccountTypeName",StringUtil.stringConvert(fav.getOldAccountTypeName()));
				modelMap.put("oldFinancialOfficer",StringUtil.stringConvert(fav.getOldFinancialOfficer()));
				modelMap.put("oldLegalPerson",StringUtil.stringConvert(fav.getOldLegalPerson()));
				modelMap.put("oldIdcardno",StringUtil.stringConvert(fav.getOldIdcardno()));
				modelMap.put("oldAccountContent",StringUtil.stringConvert(fav.getOldAccountContent()));
			}else {
				modelMap.put("oldAccountName", "");
				modelMap.put("oldAccountNumber","");
				modelMap.put("oldBankName","");
				modelMap.put("oldType02Name","");
				modelMap.put("oldAccountTypeName","");
				modelMap.put("oldFinancialOfficer","");
				modelMap.put("oldLegalPerson","");
				modelMap.put("oldIdcardno","");
				modelMap.put("oldAccountContent","");
			}
		}else {
			modelMap.put("oldAccountName", "");
			modelMap.put("oldAccountNumber","");
			modelMap.put("oldBankName","");
			modelMap.put("oldType02Name","");
			modelMap.put("oldAccountTypeName","");
			modelMap.put("oldFinancialOfficer","");
			modelMap.put("oldLegalPerson","");
			modelMap.put("oldIdcardno","");
			modelMap.put("oldAccountContent","");
		}
		return new ModelAndView(viewName, "modelMap", modelMap);
	}
	/**
	 * 获取表单是否编辑标志
	 * @Title: getFormIsEdit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping({ "/getFormIsEdit.do" })
	@ResponseBody
	public ResultMsg getFormIsEdit(HttpServletRequest request){
		ResultMsg msg =new ResultMsg(true, "");
		String wfid =request.getParameter("wfid");
		String activityId =request.getParameter("activityId");
		String type =request.getParameter("type");
		//根据wfid和activityId查询能否编辑标志
		boolean formIsEdit =false;
		boolean isBackToFirstNode =false;
		try {
			formIsEdit = service.getTaskFormEditable(wfid, activityId);
			isBackToFirstNode = service.getCanBackToFirstNode(wfid, activityId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, Object> modelMap =new HashMap();
		modelMap.put("formIsEdit", formIsEdit);
		modelMap.put("isBackToFirstNode",isBackToFirstNode);
		msg.setBody(modelMap);
		return msg;
	}
}
