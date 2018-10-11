package com.wfzcx.ppms.execute.controller;

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
import com.wfzcx.ppms.discern.service.ProjectDiscernService;
import com.wfzcx.ppms.execute.service.ProjectImplementServiceI;

/**
 * 实施情况
 * @ClassName: ProjectImplementController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author LiuJunBo
 * @date 2015-10-13 下午04:51:11
 */
@Scope("prototype")
@Controller
@RequestMapping("/execute/controller/ProjectImplementController")
public class ProjectImplementController {
	
	@Autowired
	ProjectImplementServiceI projectImplementService;
	@Autowired
	ProjectDiscernService projectService;
	
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
		ModelAndView mav = new ModelAndView("ppms/execute/implement/proimp_init");
		mav.addObject("menuid",menuid);
		return mav;
	}
	
	/**
	 * 查询
	 * @Title: qryProImp 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return EasyUITotalResult 返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping({ "/qryProImp.do" })
	public EasyUITotalResult qryProImp(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = projectImplementService.qryProImp(map);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 查询项目融资情况表
	 * @Title: qryImlPlan 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return EasyUITotalResult 返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping({ "/qryProFinance.do" })
	public EasyUITotalResult qryProFinance(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = projectImplementService.qryProFinance(map);
		
		return EasyUITotalResult.from(ps);
		
	}
	/**
	 *页面跳转
	 * @Title: optProImpView 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping({"/optProImpView.do"})
	public ModelAndView optProImpView(HttpServletRequest request) throws Exception{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		String optFlag = map.get("optFlag")==null?"":map.get("optFlag").toString();
		ModelAndView mav = null;
		if("".equals(optFlag)){
			throw new Exception("没找到视图！");
		}else{
			if("add".equals(optFlag) || "edit".equals(optFlag)){
				mav = new ModelAndView("ppms/execute/implement/proimp_form");
			}else if("view".equals(optFlag)){
				mav = new ModelAndView("ppms/execute/implement/proimp_view");
			}else if("audit_single".equals(optFlag)){
				mav = new ModelAndView("ppms/execute/implement/proimp_form_audit");
			}else if("audit_multiple".equals(optFlag) || "back_multiple".equals(optFlag)){
				mav = new ModelAndView("ppms/prepare/audit_multiple");
			}else {
				throw new Exception("没找到视图！");
			}
		}
		return mav;
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
	@RequestMapping("/saveProImp.do")
	@ResponseBody
	public ResultMsg saveProImp(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			projectImplementService.saveProImp(map);
			String projectid = (String)map.get("projectid");
			projectService.updateXmdqhj(projectid, "4");//更新为项目执行
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
		return msg;
	}
	
	/**
	 * 提交
	 * @Title: subProImp 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/subProImp.do")
	@ResponseBody
	public ResultMsg subProImp(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			projectImplementService.subProImp(map);
			msg = new ResultMsg(true, AppException.getMessage("提交成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("提交失败！"));
		}
		return msg;
	}
	/**
	 * 取消提交
	 * @Title: subProImp 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/revokeProImp.do")
	@ResponseBody
	public ResultMsg revokeProImp(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			projectImplementService.revokeProImp(map);
			msg = new ResultMsg(true, AppException.getMessage("取消提交成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("取消提交失败！"));
		}
		return msg;
	}
	
	/**
	 * 删除实施情况
	 * @Title: delProImp 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/delProImp.do")
	@ResponseBody
	public ResultMsg delProImp(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			projectImplementService.delProImp(map);
			msg = new ResultMsg(true, AppException.getMessage("删除成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败！"));
		}
		return msg;
	}
	/**
	 * 删除融资机构
	 * @Title: delProImp 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/delProFinance.do")
	@ResponseBody
	public ResultMsg delProFinance(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			projectImplementService.delProFinance(map);
			msg = new ResultMsg(true, AppException.getMessage("删除成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败！"));
		}
		return msg;
	}
}
