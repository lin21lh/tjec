package com.wfzcx.fam.manage.revoke.controller;

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
import com.wfzcx.fam.manage.RecordType;
import com.wfzcx.fam.manage.po.FaApplications;
import com.wfzcx.fam.manage.revoke.service.AccountRevokeService;

/**
 * 
 * @ClassName: AccountRevokeController 
 * @Description: TODO(账户注销控制类) 
 * @author LiuJunBo
 * @date 2015-4-14 上午11:01:08
 */

@Scope("prototype")
@Controller
@RequestMapping("/manage/revoke/controller/AccountRevokeController")
public class AccountRevokeController {
	
	@Autowired
	AccountRevokeService accountRevokeService ;
	
	/**
	 * 
	 * @Title: init 
	 * @Description: TODO(初始页面) 
	 * @param @return String 跳转页面
	 * @param @throws ServletException 设定文件 
	 * @return String 返回类型 
	 */
	@RequestMapping({ "/init.do" })
	public ModelAndView init(String menuid) throws ServletException {
		
		ModelAndView mav = new ModelAndView("fam/manage/revoke/revoke_init");
		mav.addObject("menuid",menuid);
		try {
			SysResource sr	=accountRevokeService.getResourceById(menuid);
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
		
		mav.addObject("type",RecordType.REVOKE.getIndex());
		//权限内的所有机构(add by XinPeng 2015年5月18日16:07:05)
		String allbdgagency = DeptComponent.getCurAndLowerCodeForYsdw(menuid,"code");
		mav.addObject("allbdgagency",allbdgagency);
		SysUser user = SecureUtil.getCurrentUser();
		mav.addObject("userid",user.getUserid());
		//HttpSession session = request.getSession();
		//String viewName = "fam/manage/revoke/revoke_init";
		//Map<String, Object> modelMap = new HashMap<String,Object>();
		//modelMap.put("sessionId", session.getId());
		return mav;
	}
	/**
	 * 账户撤销，不用工作流
	 * @Title: initByNoWrokFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @date 2015年8月24日17:17:51
	 * @return
	 */
	@RequestMapping({ "/initByNoWrokFlow.do" })
	public ModelAndView initByNoWrokFlow(String menuid) throws ServletException {
		ModelAndView mav = new ModelAndView("fam/manage/revoke/revoke_init_nowf");
		mav.addObject("menuid",menuid);
		mav.addObject("type",RecordType.REVOKE.getIndex());
		String allbdgagency = DeptComponent.getCurAndLowerCodeForYsdw(menuid,"code");
		mav.addObject("allbdgagency",allbdgagency);
		SysUser user = SecureUtil.getCurrentUser();
		mav.addObject("userid",user.getUserid());
		return mav;
	}
	
	/**
	 * 
	 * @Title: queryNoRevoke 
	 * @Description: TODO(查询-可注销账户) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return EasyUITotalResult 返回类型 
	 */
	@RequestMapping({ "/queryNoRevoke.do" })
	@ResponseBody
	public EasyUITotalResult queryNoRevoke(HttpServletRequest request) {
		
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = accountRevokeService.queryNoRevoke(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 
	 * @Title: queryNoRevoke 
	 * @Description: TODO(查询-提交注销的账户) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return EasyUITotalResult 返回类型 
	 */
	@RequestMapping({ "/queryHasRevoke.do" })
	@ResponseBody
	public EasyUITotalResult queryHasRevoke(HttpServletRequest request) throws AppException{
		
		/*Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryChangeAccount(map);*/
		
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = accountRevokeService.queryHasRevokeByView(map);
		
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 
	 * @Title: revokeAdd 
	 * @Description: TODO(新增-账户注销) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ModelAndView 返回类型 
	 */
	@RequestMapping("/revoke_add.do")
	public ModelAndView addRevokeView(HttpServletRequest request) {
		
		ModelAndView mav = null;
		//获取session用于附件上传
		HttpSession session = request.getSession();
		String optType = request.getParameter("optType");
		Map<String, Object> modelMap = new HashMap<String,Object>();
		modelMap.put("sessionId", session.getId());
		modelMap.put("optType", optType);
		if(optType.equals("view")){
			mav = new ModelAndView("/fam/manage/revoke/revoke_view","modelMap",modelMap);
		}else {
			mav = new ModelAndView("/fam/manage/revoke/revoke_add","modelMap",modelMap);
		}
		return mav;
	}
	
	@RequestMapping("/editRevokeInfo.do")
	@ResponseBody
	public ResultMsg saveEditRevokeInfo(HttpServletRequest request,FaApplications faApplication) {
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			accountRevokeService.editRevokeInfo(faApplication,map);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
		return msg;
	}
	@RequestMapping("/saveEditRevokeInfoNoWF.do")
	@ResponseBody
	public ResultMsg saveEditRevokeInfoNoWF(HttpServletRequest request,FaApplications faApplication) {
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			accountRevokeService.saveEditRevokeInfoNoWF(faApplication,map);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
		return msg;
	}
	
	/**
	 * 
	 * @Title: saveRevokeInfo 
	 * @Description: TODO(保存-新增的注销账户) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 */
	@RequestMapping("/addRevokeInfo.do")
	@ResponseBody
	public ResultMsg saveAddRevokeInfo(HttpServletRequest request) {
		
		Map<String,Object> map = request.getParameterMap();
		String itemids = request.getParameter("itemids");
		ResultMsg msg = null;
		try {
			accountRevokeService.saveRevokeInfo(map,itemids);
			msg = new ResultMsg(true, "操作成功！");
		} catch (Exception e1){
			e1.printStackTrace();
			msg = new ResultMsg(false, "操作失败！");
		}
		return msg;
	}
	/**
	 * 保存不用工作流
	 * @Title: saveAddRevokeInfoNoWF 
	 * @Description: TODO(保存-新增的注销账户) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 */
	@RequestMapping("/saveAddRevokeInfoNoWF.do")
	@ResponseBody
	public ResultMsg saveAddRevokeInfoNoWF(HttpServletRequest request) {
		
		Map<String,Object> map = request.getParameterMap();
		String itemids = request.getParameter("itemids");
		ResultMsg msg = null;
		try {
			accountRevokeService.saveAddRevokeInfoNoWF(map, itemids);
			msg = new ResultMsg(true, "操作成功！");
		} catch (Exception e1){
			e1.printStackTrace();
			msg = new ResultMsg(false, "操作失败！");
		}
		return msg;
	}
	/**
	 * 
	 * @Title: deleteRevokeInfo 
	 * @Description: TODO(删除-账户注销) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 */
	@RequestMapping("/deleteRevokeInfo.do")
	@ResponseBody
	public ResultMsg deleteRevokeInfo(String applicationIds){
		ResultMsg msg = null;
		try {
			accountRevokeService.deleteRevokeInfo(applicationIds);
			msg = new ResultMsg(true, "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, "删除失败！");
		}
		return msg;
	}
	
	/**
	 * 
	 * @Title: removeRevokeInfo 
	 * @Description: TODO(账户注销-撤回) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/removeRevokeInfo.do")
	@ResponseBody
	public ResultMsg removeRevokeInfo(String applicationIds,String menuid,String activityId,String isba){
		
		ResultMsg msg = null;
		try {
			accountRevokeService.removeRevokeInfo(applicationIds,menuid,activityId,isba);
			msg = new ResultMsg(true, "撤回成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, "撤回失败！");
		}
		return msg;
	}
	/**
	 * 
	 * @Title: submitRevokeInfo 
	 * @Description: TODO(送审-账户注销) 
	 * @param @param applicationId
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 */
	@RequestMapping("/submitRevokeInfo.do")
	@ResponseBody
	public ResultMsg submitRevokeInfo(HttpServletRequest request){
		ResultMsg msg = null;
		String applicationIds = request.getParameter("applicationIds");
		String menuid = request.getParameter("menuid");
		String activityId = request.getParameter("activityId");
		try {
			accountRevokeService.submitRevokeInfo(applicationIds,menuid,activityId);
			msg = new ResultMsg(true, "送审成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, "送审失败！");
		}
		return msg;
	}
	/**
	 * 
	 * @Title: initFirstTrial 
	 * @Description: TODO(初始化初审界面) 
	 * @param @return
	 * @param @throws ServletException 设定文件 
	 * @return String 返回类型 
	 */
	@RequestMapping({ "/initFirstTrial.do" })
	public ModelAndView initFirstTrial(String menuid) throws ServletException {
		
		ModelAndView mav = new ModelAndView("fam/manage/revoke/revoke_first_trial");
		mav.addObject("menuid",menuid);
		try {
			SysResource sr	=accountRevokeService.getResourceById(menuid);
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
		mav.addObject("type",RecordType.REVOKE.getIndex());
		//权限内的所有机构(add by XinPeng 2015年5月18日16:07:05)
		String allbdgagency = DeptComponent.getCurAndLowerCodeForYsdw(menuid,"code");
		mav.addObject("allbdgagency",allbdgagency);
		
		return mav;
	}
	
	/**
	 * 
	 * @Title: revokeAuditForm 
	 * @Description: TODO(初审加载form) 
	 * @param @param request
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return ModelAndView 返回类型 
	 */
	@RequestMapping({ "/revokeAuditForm.do" })
	public ModelAndView revokeAuditForm(HttpServletRequest request) {
		ModelAndView mav=null;
		String wfid =request.getParameter("wfid");
		String activityId =request.getParameter("activityId");
		String type =request.getParameter("type");
		String optType = request.getParameter("optType");
		
		if(optType.equals("view")){
			mav = new ModelAndView("fam/manage/revoke/revoke_first_trial_view");
		}else{
			mav = new ModelAndView("fam/manage/revoke/revoke_first_trial_add");
		}
		//根据wfid和activityId查询能否编辑标志
		boolean formIsEdit=true;
		try {
			formIsEdit = accountRevokeService.getTaskFormEditable(wfid, activityId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List list =accountRevokeService.getworkFlowList(wfid);
		mav.addObject("optType",optType);
		mav.addObject("comeFlag","audit");
		mav.addObject("formIsEdit",formIsEdit);
		mav.addObject("wfList",list);
		return mav;
	}
	
	/**
	 * 
	 * @Title: operateRevokeInfo 
	 * @Description: TODO(初审、终审，同意跟退回) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 */
	@RequestMapping("/operateRevokeInfo.do")
	@ResponseBody
	public ResultMsg operateRevokeInfo(HttpServletRequest request,FaApplications faApplication) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			accountRevokeService.verifyWorkFlow(param,faApplication);
			/*try {
				//短信服务
				String phonenumber = (String)param.get("savephonenumbers");
				String message = (String)param.get("savemessage");
				String activityId = StringUtil.stringConvert(param.get("activityId"));
				if(!"".equals(phonenumber) && !"".equals(message)){
					accountRevokeService.saveMessage(faApplication.getApplicationId(), phonenumber, message,activityId);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg = new ResultMsg(true, AppException.getMessage("操作成功，但消息发送失败。"));
			}*/
			msg = new ResultMsg(true, AppException.getMessage("操作成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("操作失败！"));
		}
		return msg;
	}
	
	/**
	 * 
	 * @Title: refuseRevokeInfo 
	 * @Description: TODO(初审、终审，同意跟退回（批量）) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 */
	@RequestMapping("/refuseRevokeInfo.do")
	@ResponseBody
	public ResultMsg refuseRevokeInfo(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			accountRevokeService.verifyWorkFlow(param);
			msg = new ResultMsg(true, AppException.getMessage("操作成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("操作失败！"));
		}
		return msg;
	}
	
	/**
	 * 
	 * @Title: accountBatchOperation 
	 * @Description: TODO(审批批量操作弹出框) 
	 * @param @return 设定文件 
	 * @return ModelAndView 返回类型 
	 */
	@RequestMapping("/accountBatchOperation.do")
	public ModelAndView accountBatchOperation(String wfid,String activityId){
		
		ModelAndView mav = new ModelAndView("fam/manage/common/account_batch_operation");
		
		return mav;
	}
	
	/**
	 * 
	 * @Title: showMessageView 
	 * @Description: TODO(消息服务) 
	 * @param @return 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping("/message.do")
	public ModelAndView showMessageView(){
		
		ModelAndView mav = new ModelAndView("fam/manage/common/message_form");
		
		return mav;
	}
	
	/**
	 * 
	 * @Title: showUploadifyView 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping("/uploadify.do")
	public ModelAndView showUploadifyView(HttpServletRequest request){
		
		String fileItemmids = request.getParameter("fileItemmids");
		String tdId = request.getParameter("tdId");
		String elementcode = request.getParameter("elementcode");
		if(fileItemmids==null || fileItemmids.equals("") || fileItemmids.equals("undefined")){
			fileItemmids="itemids";
		}
		//获取session用于附件上传
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView("fam/manage/common/uploadify");
		mav.addObject("fileItemmids",fileItemmids);//隐藏域
		mav.addObject("sessionId", session.getId());//session
		mav.addObject("tdId", tdId);//附件展示框
		mav.addObject("elementcode", elementcode);//业务类型
		return mav;
	}
	
	/**
	 * 
	 * @Title: sendMessage 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("/sendMessage.do")
	public ResultMsg sendMessage(HttpServletRequest request){
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			//短信服务
			String phonenumber = (String)param.get("savephonenumbers");
			String message = (String)param.get("savemessage");
			String activityId = StringUtil.stringConvert(param.get("activityId"));
			Integer applicationId = param.get("applicationId")==null? 0: Integer.parseInt(((String)param.get("applicationId")));
			
			if(!"".equals(phonenumber) && !"".equals(message)){
				accountRevokeService.saveMessage(applicationId, phonenumber, message,activityId);
				msg = new ResultMsg(true, AppException.getMessage("发送成功！"));
			}else{
				msg = new ResultMsg(false, AppException.getMessage("发送失败！"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败！"));
		}
		
		return msg;
	}
	
	@RequestMapping("/accountSingleOperation.do")
	public ModelAndView showOperationView(HttpServletRequest request){
		
		ModelAndView mav = new ModelAndView("fam/manage/common/account_single_operation");
		String wfid = request.getParameter("wfid");
		String activityId = request.getParameter("activityId");
		String applicationId = request.getParameter("applicationId");
		String isBa = request.getParameter("isBa");
		String cllx = request.getParameter("cllx");
		
		String messageContent = accountRevokeService.getMessageContent(applicationId, wfid, isBa, cllx, activityId);
		List<Map> phonenumbers = accountRevokeService.getSendUser(wfid, activityId);
		mav.addObject("phonenumbers", phonenumbers);
		mav.addObject("messageContent", messageContent);
		return mav;
	}
	/**
	 * 查询所有列表撤销
	 * @param request
	 * @return 
	 * @throws AppException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryAllAccount.do")
	@ResponseBody
	public EasyUITotalResult queryChangeAccount(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = accountRevokeService.queryChangeAccount(map);
		return EasyUITotalResult.from(ps);
	}
}
