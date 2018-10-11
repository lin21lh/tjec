package com.wfzcx.ppms.prepare.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

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
import com.wfzcx.ppms.prepare.service.ImplementationPlanServiceI;

/**
 * 实施方案
 * @ClassName: ImplementationPlanController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author LiuJunBo
 * @date 2015-9-17 上午10:58:46
 * @update by xinpeng 2016年3月24日11:49:11
 */
@Scope("prototype")
@Controller
@RequestMapping("/prepare/controller/ImplementationPlanController")
public class ImplementationPlanController {

	@Autowired
	ImplementationPlanServiceI implementationPlanService;
	
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
		ModelAndView mav = new ModelAndView("ppms/prepare/imlplan_init");
		mav.addObject("menuid",menuid);
		try {
			SysResource sr	=implementationPlanService.getResourceById(menuid);
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
	 * 查询项目计划
	 * @Title: qryImlPlan 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param param
	 * @param @return 设定文件 
	 * @return PaginationSupport 返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping({ "/qryImlPlan.do" })
	public EasyUITotalResult qryImlPlan(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = implementationPlanService.qryImlPlan(map);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 新增或修改跳转方法
	 * @Title: optImlPlanView 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping({"/optImlPlanView.do"})
	public ModelAndView optImlPlanView(HttpServletRequest request) throws Exception{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		String optFlag = map.get("optFlag")==null?"":map.get("optFlag").toString();
		ModelAndView mav = null;
		if("".equals(optFlag)){
			throw new Exception("没找到视图！");
		}else{
			if("add".equals(optFlag) || "edit".equals(optFlag)){
				mav = new ModelAndView("ppms/prepare/imlplan_form");
			}else if("view".equals(optFlag)){
				mav = new ModelAndView("ppms/prepare/imlplan_view");
			}else if("audit_single".equals(optFlag)){
				mav = new ModelAndView("ppms/prepare/imlplan_form_audit");
			}else if("audit_multiple".equals(optFlag) || "back_multiple".equals(optFlag)){
				mav = new ModelAndView("ppms/prepare/audit_multiple");
			}else if("audit_view".equals(optFlag)){
				mav = new ModelAndView("ppms/prepare/imlplan_view_audit");
			}else{
				throw new Exception("没找到视图！");
			}
		}
		return mav;
	}
	
	/**
	 * 保存
	 * @Title: saveImlPlan 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/saveImlPlan.do")
	@ResponseBody
	public ResultMsg saveImlPlan(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		String sendFlag = map.get("sendFlag")==null?"":map.get("sendFlag").toString();
		try {
			implementationPlanService.saveImlPlan(map);
			if("1".equals(sendFlag)){
				msg = new ResultMsg(true, AppException.getMessage("送审成功！"));
			}else{
				msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			if("1".equals(sendFlag)){
				msg = new ResultMsg(false, AppException.getMessage("送审失败！"));
			}else{
				msg = new ResultMsg(false, AppException.getMessage("crud.saveerr"));
			}
		}
		return msg;
	}
	
	/**
	 * 删除
	 * @Title: delImlPlan 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/delImlPlan.do")
	@ResponseBody
	public ResultMsg delImlPlan(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			implementationPlanService.delImlPlan(map);
			msg = new ResultMsg(true, AppException.getMessage("删除成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败！"));
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
	@RequestMapping("/sendWorkFlow.do")
	@ResponseBody
	public ResultMsg sendWorkFlow(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			String msgString = implementationPlanService.sendWorkFlow(map);
			if("".equals(msgString)){
				msg = new ResultMsg(true, AppException.getMessage("审批成功！"));
			}else{
				msg = new ResultMsg(false, AppException.getMessage("审批失败！"+msgString));
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("审批失败！"+e.getMessage()));
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
	 * @throws
	 */
	@RequestMapping("/revokeWorkFlow.do")
	@ResponseBody
	public ResultMsg revokeWorkFlow(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		String menuid = map.get("menuid") == null ? "":map.get("menuid").toString();
		String solutionid = map.get("solutionid") == null ? "":map.get("solutionid").toString();
		String activityId = map.get("activityId") == null ? "":map.get("activityId").toString();
		String wfid = map.get("wfid") == null ? "":map.get("wfid").toString();
		try {
			String msgString = implementationPlanService.revokeWorkFlow(wfid, solutionid, activityId);
			
			if("".equals(msgString)){
				msg = new ResultMsg(true, AppException.getMessage("撤回成功！"));
			}else{
				msg = new ResultMsg(false, AppException.getMessage("撤回失败！"+msgString));
			}
			msg = new ResultMsg(true, AppException.getMessage("撤回成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("撤回失败！"+e.getMessage()));
		}
		return msg;
	}
	
	/**
	 * 审核界面
	 * @Title: audit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param menuid
	 * @param @return
	 * @param @throws ServletException 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping({ "/audit.do" })
	public ModelAndView audit(String menuid) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/prepare/imlplan_audit");
		mav.addObject("menuid",menuid);
		try {
			SysResource sr	=implementationPlanService.getResourceById(menuid);
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
	 * 退回
	 * @Title: backWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/backWorkFlow.do")
	@ResponseBody
	public ResultMsg backWorkFlow(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String returnMsg = implementationPlanService.backWorkFlow(param);
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
		PaginationSupport ps = implementationPlanService.queryThirdOrg(projectid);
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
		PaginationSupport ps = implementationPlanService.queryFinance(projectid);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 查询定性分析组织单位
	 * @Title: queryFinance 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/qualUnitGrid.do")
	@ResponseBody
	public EasyUITotalResult qualUnitGrid(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = implementationPlanService.qualUnitGrid(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 定性分析专家列表
	 * @Title: qualUnitGrid 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return EasyUITotalResult 返回类型 
	 * @throws
	 */
	@RequestMapping("/qualExpertGrid.do")
	@ResponseBody
	public EasyUITotalResult qualExpertGrid(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = implementationPlanService.qualExpertGrid(map);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 查询财政承受能力组织单位
	 * @Title: queryFinance 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/financeUnitGrid.do")
	@ResponseBody
	public EasyUITotalResult financeUnitGrid(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = implementationPlanService.financeUnitGrid(map);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 审批意见查询
	 * @Title: queryApprove 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/queryApprove.do")
	@ResponseBody
	public ResultMsg queryApprove(HttpServletRequest request) {
		ResultMsg msg =new ResultMsg(true, "");
		String projectid =StringUtil.stringConvert(request.getParameter("projectid"));
		List approveList = new ArrayList();
		try {
			approveList = implementationPlanService.queryApprove(projectid);
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
	 * 实时查询专家列表
	 * @Title: qryExpertByQ 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return EasyUITotalResult 返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping({ "/qryExpertByQ.do" })
	public EasyUITotalResult qryExpertByQ(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = implementationPlanService.qryExpertByQ(map);
		
		return EasyUITotalResult.from(ps);
		
	}
	
	/**
	 * 保存审核信息
	 * @Title: saveAuditData 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/saveAuditData.do")
	@ResponseBody
	public ResultMsg saveAuditData(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		String sendFlag = map.get("sendFlag")==null?"":map.get("sendFlag").toString();
		try {
			implementationPlanService.saveAuditData(map);
			if("1".equals(sendFlag)){
				msg = new ResultMsg(true, AppException.getMessage("送审成功！"));
			}else{
				msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			if("1".equals(sendFlag)){
				msg = new ResultMsg(false, AppException.getMessage("送审失败！"));
			}else{
				msg = new ResultMsg(false, AppException.getMessage("crud.saveerr"));
			}
		}
		return msg;
	}
	/**
	 * 实施方案评审
	 * @Title: verify（审批）
	 * @Description: TODO(初始页面) 
	 * @param @return String 跳转页面
	 * @param @throws ServletException 设定文件 
	 * @return String 返回类型 
	 * @author XinPeng 2016年3月24日12:01:50
	 */
	@RequestMapping({ "/verify.do" })
	public ModelAndView verify(String menuid) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/prepare/ssfa/preparessfa_init");
		mav.addObject("menuid",menuid);
		try {
			SysResource sr	=implementationPlanService.getResourceById(menuid);
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
	 * 页面跳转
	 * @Title: pageForward 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws Exception 设定文件
	 */
	@RequestMapping({"/pageForward.do"})
	public ModelAndView pageForward(HttpServletRequest request) throws Exception{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		String pageName = map.get("pageName")==null?"":map.get("pageName").toString();
		ModelAndView mav = null;
		if("".equals(pageName)){
			throw new Exception("没找到视图！");
		}else{
			if("auditForm".equals(pageName)){
				mav = new ModelAndView("ppms/prepare/ssfa/preparessfa_audit_form");
			}else if("detailForm".equals(pageName)){
				mav = new ModelAndView("ppms/prepare/ssfa/preparessfa_detail_form");
			}else {
				throw new Exception("没找到视图！");
			}
		}
		return mav;
	}
	/**
	 * 2016年3月28日15:27:38启用的方案评审
	 * @Title: sendWorkFlowForVerify 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/sendWorkFlowForVerify.do")
	@ResponseBody
	public ResultMsg sendWorkFlowForVerify(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		String backFlag = map.get("backFlag") == null ? "":map.get("backFlag").toString();
		try {
			String msgString = implementationPlanService.sendWorkFlowForVerify(map);
			if("".equals(msgString)){
				msg = new ResultMsg(true, AppException.getMessage("3".equals(backFlag)?"退回成功！":"审批成功！"));
			}else{
				msg = new ResultMsg(false, AppException.getMessage("3".equals(backFlag)?"退回成功！":"退回失败！"+msgString));
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("审批失败！"+e.getMessage()));
		}
		return msg;
	}
}
