package com.wfzcx.ppms.prepare.controller;

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
import com.wfzcx.ppms.prepare.service.DevelopmentPlanServiceI;

/**
 * 开发计划
 * @ClassName: DevelopmentPlanController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author LiuJunBo
 * @date 2015-9-17 上午10:59:10
 * @update by XinPeng 2016年3月22日19:30:45
 */
@Scope("prototype")
@Controller
@RequestMapping("/prepare/controller/DevelopmentPlanController")
public class DevelopmentPlanController {

	@Autowired
	DevelopmentPlanServiceI developmentPlanServiceI ;
	
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
		ModelAndView mav = new ModelAndView("ppms/prepare/devplan_init");
		mav.addObject("menuid",menuid);
		return mav;
	}
	
	/**
	 * 查询项目计划
	 * @Title: qryDevPlan 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param param
	 * @param @return 设定文件 
	 * @return PaginationSupport 返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping({ "/qryDevPlan.do" })
	public EasyUITotalResult qryDevPlan(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = developmentPlanServiceI.qryDevPlan(map);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 新增或修改跳转方法
	 * @Title: optDevPlanView 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping({"/optDevPlanView.do"})
	public ModelAndView optDevPlanView(HttpServletRequest request) throws Exception{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		String optFlag = map.get("optFlag")==null?"":map.get("optFlag").toString();
		ModelAndView mav = null;
		if("".equals(optFlag)){
			throw new Exception("没找到视图！");
		}else{
			if("add".equals(optFlag) || "edit".equals(optFlag)){
				mav = new ModelAndView("ppms/prepare/devplan_form");
			}else if("view".equals(optFlag)){
				mav = new ModelAndView("ppms/prepare/devplan_view");
			}else{
				throw new Exception("没找到视图！");
			}
		}
		return mav;
	}
	
	@RequestMapping("/saveDevPlan.do")
	@ResponseBody
	public ResultMsg saveDevPlan(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			developmentPlanServiceI.saveDevPlan(map);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
		return msg;
	}
	
	@RequestMapping("/delDevPlan.do")
	@ResponseBody
	public ResultMsg delDevPlan(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			developmentPlanServiceI.delDevPlan(map);
			msg = new ResultMsg(true, AppException.getMessage("删除成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败！"));
		}
		return msg;
	}
	
	/**
	 * 查询实施计划是否已经下达
	 * @Title: qryImlplanIsAudit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/qryImlplanIsAudit.do")
	@ResponseBody
	public ResultMsg qryImlplanIsAudit(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			boolean flag = developmentPlanServiceI.qryImlplanIsAudit(map);
			if(flag){
				msg = new ResultMsg(true, AppException.getMessage("下达！"));
			}else{
				msg = new ResultMsg(false, AppException.getMessage("未下达！"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(true, AppException.getMessage("下达！"));//如果查询报错，默认为下达，以防止数据被改掉没有记录
		}
		return msg;
	}
}
