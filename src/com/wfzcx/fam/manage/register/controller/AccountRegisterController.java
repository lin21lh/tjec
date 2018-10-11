package com.wfzcx.fam.manage.register.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
import com.wfzcx.fam.manage.register.service.AccountRegisterService;

/**
 * 账户开立Controller
 * @ClassName: AccountRegisterController 
 * @Description: TODO(账户开立 登陆界面) 
 * @author MaQingShuang
 * @date 2015年4月14日 上午9:47:55
 */
@Controller
@RequestMapping({ "/manage/register/AccountRegisterController" })
public class AccountRegisterController {

	@Autowired
	AccountRegisterService accountRegisterService;
	@Autowired
	ParamCfgComponent pcfg;
	/**
	 * 录入界面
	 * @Title: entry 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("entry.do")
	public ModelAndView entry(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("fam/manage/register/registerEntry");
		HttpSession session = request.getSession();
		String menuid = request.getParameter("menuid");
		try {
			SysResource sr	=accountRegisterService.getResourceById(menuid);
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
		mav.addObject("type",RecordType.REGISTER.getIndex());
		mav.addObject("sessionId", session.getId());
		mav.addObject("menuid",menuid);
		//权限内的所有机构
		String allbdgagency = DeptComponent.getCurAndLowerCodeForYsdw(menuid,"code");
		mav.addObject("allbdgagency",allbdgagency);
		SysUser user = SecureUtil.getCurrentUser();
		mav.addObject("userid",user.getUserid());
		return mav;
	}
	
	/**
	 * 审核界面
	 * @Title: auditEntry 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("auditEntry.do")
	public ModelAndView auditEntry(HttpServletRequest request,String menuid,String supply) {
		ModelAndView mav = new ModelAndView("fam/manage/register/registerAuditEntry");
		mav.addObject("menuid",menuid);
		mav.addObject("supply",supply);
		mav.addObject("type",RecordType.REGISTER.getIndex());
		try {
			SysResource sr	=accountRegisterService.getResourceById(menuid);
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
		String allbdgagency = DeptComponent.getCurAndLowerCodeForYsdw(menuid,"code");
		mav.addObject("allbdgagency",allbdgagency);
		return mav;
	
	}
	
	/**
	 * 维护界面
	 * @Title: formEntry 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 */
	@RequestMapping("formEntry.do")
	public ModelAndView formEntry(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String viewName = "fam/manage/register/registerForm";
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("sessionId", session.getId());
		SysUser user = SecureUtil.getCurrentUser();
		String editFlag =StringUtil.stringConvert(request.getParameter("editFlag"));
		if(!"1".equals(editFlag)){//新增时查询单位相关属性
			String bdgagencycode = user.getOrgcode();
			String bdgagencyname = user.getOrgname();
			List<Map>  list = accountRegisterService.getbdgagencyInformation(bdgagencycode);
			modelMap.put("bdgagencycode", bdgagencycode);
			modelMap.put("bdgagencyname", bdgagencyname);
			if(!list.isEmpty()){
				Map map = list.get(0);
				modelMap.put("linkman", StringUtil.stringConvert(map.get("linkman")));
				modelMap.put("phonenumber", StringUtil.stringConvert(map.get("phonenumber")));
				modelMap.put("deptDddress", StringUtil.stringConvert(map.get("dept_address")));
				modelMap.put("deptNature", StringUtil.stringConvert(map.get("dept_nature")));
			}else {
				modelMap.put("linkman", "");
				modelMap.put("phonenumber", "");
				modelMap.put("deptDddress", "");
				modelMap.put("deptNature", "");
			}
		}
		String wfid =StringUtil.stringConvert(request.getParameter("wfid"));
		//节点是否能显示账户信息
		if(!"".equals(wfid)){
			String activityId =request.getParameter("activityId");
			boolean isEdit = accountRegisterService.getOperatenum(wfid);
			modelMap.put("zhxxdisplay",isEdit?"Y":pcfg.findGeneralParamValue("ZHKL", activityId.toUpperCase()));
		}
		return new ModelAndView(viewName, "modelMap", modelMap);
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
		String viewName = "fam/manage/register/registerDetailForm";
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String wfid =request.getParameter("wfid");
		List list =accountRegisterService.getworkFlowList(wfid);
		modelMap.put("wfList",list);
		return new ModelAndView(viewName, "modelMap", modelMap);
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
		String viewName  ="";
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String wfid =request.getParameter("wfid");
		String activityId =request.getParameter("activityId");
		String type =request.getParameter("type");
		HttpSession session = request.getSession();
		modelMap.put("sessionId", session.getId());
		//根据wfid和activityId查询能否编辑标志
		boolean formIsEdit = accountRegisterService.getTaskFormEditable(wfid, activityId);
		if (formIsEdit) {//表单可编辑
			viewName = "fam/manage/register/registerForm";
		}else {//表单不可编辑
			viewName = "fam/manage/register/registerDetailForm";
		}
		modelMap.put("comeFlag","audit");
		modelMap.put("formIsEdit",formIsEdit);
		//节点是否能显示账户信息
		boolean isEdit = accountRegisterService.getOperatenum(wfid);
		modelMap.put("zhxxdisplay",isEdit?"Y":pcfg.findGeneralParamValue("ZHKL", activityId.toUpperCase()));
		List list =accountRegisterService.getworkFlowList(wfid);
		modelMap.put("wfList",list);
		return new ModelAndView(viewName, "modelMap", modelMap);
	}
	/**
	 * 
	 * @Title: add 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param faAccount
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 */
	@RequestMapping("/add.do")
	@ResponseBody
	public ResultMsg add(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			Integer applicationId = accountRegisterService.add(param);
			HashMap<String, Object> body = new HashMap<String, Object>();
			body.put("applicationId", applicationId);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
			msg.setBody(body);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr")+e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * 修改
	 * @Title: edit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param faAccount
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/edit.do")
	@ResponseBody
	public ResultMsg edit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String retrunMsg =  accountRegisterService.edit(param);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr")+e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * 删除
	 * @Title: delete 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param applicationId
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping({"delete.do"})
	@ResponseBody
	public ResultMsg delete(String applicationId) {
		ResultMsg msg = null;
		try {
			accountRegisterService.delete(applicationId);
			msg = new ResultMsg(true, "删除成功！");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			msg = new ResultMsg(false,"删除失败！"+e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * 送审
	 * @Title: sendWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping({"sendWorkFlow.do"})
	@ResponseBody
	public ResultMsg sendWorkFlow(HttpServletRequest request) {
		ResultMsg msg = null;
		String menuid = request.getParameter("menuid");
		String applicationId = request.getParameter("applicationId");
		String activityId = request.getParameter("activityId");
		try {
			if(applicationId==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				accountRegisterService.sendWorkFlow(menuid,applicationId,activityId);
				msg = new ResultMsg(true, AppException.getMessage("送审成功！"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("送审失败！原因为："+e.getMessage()));
		}
		
		return msg;
	}
	
	/**
	 * 审核
	 * @Title: auditWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping({"auditWorkFlow.do"})
	@ResponseBody
	public ResultMsg auditWorkFlow(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String returnMsg = accountRegisterService.auditWorkFlow(param);
			if("".equals(returnMsg)){
				msg = new ResultMsg(true, "处理成功");
			}else {
				msg = new ResultMsg(false, "处理失败！" );
			}
		} catch (AppException e) {
			e.printStackTrace();
			msg = new ResultMsg(false, "处理失败，失败原因：" + e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * 撤回
	 * @Title: revokeWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 */
	@RequestMapping({"revokeWorkFlow.do"})
	@ResponseBody
	public ResultMsg revokeWorkFlow(HttpServletRequest request) {
		ResultMsg msg = null;
		String menuid = request.getParameter("menuid");
		String applicationId = request.getParameter("applicationId");
		String activityId = request.getParameter("activityId");
		
		try {
			String returnMsg = accountRegisterService.revokeWorkFlow(menuid, applicationId,activityId);
			if("".equals(returnMsg)){
				msg = new ResultMsg(true, "撤回成功");
			}else {
				msg = new ResultMsg(false, "撤回失败！");
			}
		} catch (AppException e) {
			e.printStackTrace();
			msg = new ResultMsg(false, "撤回失败，失败原因：" + e.getMessage());
		}
		return msg;
	}
	/**
	 * 审批页面跳转
	 * @Title: plEntryForm 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws Exception 设定文件
	 */
	@RequestMapping({ "/plEntryForm.do" })
	public ModelAndView plEntryForm(HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView("fam/manage/register/plclForm");
		return mav;
	}
	/**
	 * 根据单位获取单位相应信息
	 * @Title: getBdgagency 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping({ "/getBdgagency.do" })
	@ResponseBody
	public ResultMsg getBdgagency(HttpServletRequest request){
		ResultMsg msg =new ResultMsg(true, "");
		String bdgagencycode = request.getParameter("bdgagencycode");
		List<Map> list = accountRegisterService.getbdgagencyInformation(bdgagencycode);
		if(list.size()==1){
			Map map =list.get(0); 
			HashMap<String, Object> modelMap =new HashMap();
			modelMap.put("linkman", StringUtil.stringConvert(map.get("linkman")));
			modelMap.put("phonenumber", StringUtil.stringConvert(map.get("phonenumber")));
			modelMap.put("deptDddress", StringUtil.stringConvert(map.get("dept_address")));
			modelMap.put("deptNature", StringUtil.stringConvert(map.get("dept_nature")));
			msg.setBody(modelMap);
		}
		return msg;
	}
}
