package com.wfzcx.ppms.procurement.controller;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

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
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.resource.po.SysResource;
import com.wfzcx.ppms.procurement.service.ProcurementResultServiceI;

@Scope("prototype")
@Controller
@RequestMapping("/procurement/controller/ProcurementResultController")
public class ProcurementResultController {

	@Autowired
	ProcurementResultServiceI procurementResultService;
	
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
		ModelAndView mav = new ModelAndView("ppms/procurement/result/prores_init");
		mav.addObject("menuid",menuid);
		try {
			SysResource sr	=procurementResultService.getResourceById(menuid);
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
	
	@ResponseBody
	@RequestMapping({ "/qryProRes.do" })
	public EasyUITotalResult qryProRes(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = procurementResultService.qryProRes(map);
		
		return EasyUITotalResult.from(ps);
		
	}
	
	/**
	 *页面跳转
	 * @Title: optProResView 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping({"/optProResView.do"})
	public ModelAndView optProResView(HttpServletRequest request) throws Exception{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		String optFlag = map.get("optFlag")==null?"":map.get("optFlag").toString();
		ModelAndView mav = null;
		if("".equals(optFlag)){
			throw new Exception("没找到视图！");
		}else{
			if("add".equals(optFlag) || "edit".equals(optFlag)){
				mav = new ModelAndView("ppms/procurement/result/prores_form");
			}else if("view".equals(optFlag)){
				mav = new ModelAndView("ppms/procurement/result/prores_view");
			}else if("audit_view".equals(optFlag)){
				mav = new ModelAndView("ppms/procurement/result/prores_view_audit");
			}else if("audit_single".equals(optFlag)){
				mav = new ModelAndView("ppms/procurement/result/prores_form_audit");
			}else if("audit_multiple".equals(optFlag) || "back_multiple".equals(optFlag)){
				mav = new ModelAndView("ppms/prepare/audit_multiple");
			}else {
				throw new Exception("没找到视图！");
			}
		}
		return mav;
	}
	
	/**
	 * 查询机构
	 * @Title: advanceOrganUrl 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return EasyUITotalResult 返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping({ "/advanceOrganUrl.do" })
	public EasyUITotalResult advanceOrganUrl(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = procurementResultService.advanceOrganUrl(map);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 保存
	 * @Title: saveProRes 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/saveProRes.do")
	@ResponseBody
	public ResultMsg saveProRes(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		String sendFlag = map.get("sendFlag")==null?"":map.get("sendFlag").toString();
		try {
			procurementResultService.saveProRes(map);
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
	 * 保存审核数据
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
			procurementResultService.saveAuditData(map);
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
	 * @Title: delProRes 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/delProRes.do")
	@ResponseBody
	public ResultMsg delProRes(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			procurementResultService.delProRes(map);
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
			String msgString = procurementResultService.sendWorkFlow(map);
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
		String purchaseid = map.get("purchaseid") == null ? "":map.get("purchaseid").toString();
		String activityId = map.get("activityId") == null ? "":map.get("activityId").toString();
		String wfid = map.get("wfid") == null ? "":map.get("wfid").toString();
		try {
			String msgString = procurementResultService.revokeWorkFlow(wfid, purchaseid, activityId);
			
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
		ModelAndView mav = new ModelAndView("ppms/procurement/result/prores_audit");
		mav.addObject("menuid",menuid);
		try {
			SysResource sr	=procurementResultService.getResourceById(menuid);
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
			String returnMsg = procurementResultService.backWorkFlow(param);
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
	 * 专家列表查询
	 * @Title: resultExpertGrid 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return EasyUITotalResult 返回类型 
	 * @throws
	 */
	@RequestMapping("/resultExpertGrid.do")
	@ResponseBody
	public EasyUITotalResult resultExpertGrid(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = procurementResultService.resultExpertGrid(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 采购结果确认谈判工作组
	 * @Title: resultGroupGrid 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return EasyUITotalResult 返回类型 
	 * @throws
	 */
	@RequestMapping("/resultGroupGrid.do")
	@ResponseBody
	public EasyUITotalResult resultGroupGrid(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = procurementResultService.resultGroupGrid(map);
		return EasyUITotalResult.from(ps);
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
		PaginationSupport ps = procurementResultService.qryExpertByQ(map);
		
		return EasyUITotalResult.from(ps);
		
	}
}
