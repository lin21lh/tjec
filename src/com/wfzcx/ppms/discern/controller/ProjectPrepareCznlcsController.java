package com.wfzcx.ppms.discern.controller;

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
import com.wfzcx.ppms.discern.service.ProjectDiscernService;
import com.wfzcx.ppms.discern.service.ProjectPrepareCzcsnlService;
/**
 * 财政承受能力
 * @ClassName: ProjectPrepareDxfxController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2016年3月21日10:30:55
 */
@Scope("prototype")
@Controller
@RequestMapping("/ppms/discern/ProjectPrepareCznlcsController")
public class ProjectPrepareCznlcsController {
	@Autowired
	ProjectPrepareCzcsnlService service;
	@Autowired
	ProjectDiscernService projectService;
	/**
	 * 初始加载
	 * @Title: init 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/prepareCzcsnl/prepareCzcsnl_init");
		String menuid  = request.getParameter("menuid");
		String xmhj  = request.getParameter("xmhj");
		mav.addObject("menuid", menuid);
		mav.addObject("xmhj", xmhj);
		return mav;
	}
	/**
	 * 当前环节项目查询
	 * @Title: queryProject 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryProject.do")
	@ResponseBody
	public EasyUITotalResult queryProject(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryProject(map);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 查询是否已经录入了财政承受能力
	 * @Title: queryIsExistCzcsnl 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param dxpjid
	 * @param xmhj
	 * @param projectid
	 * @return 设定文件
	 */
	@RequestMapping("/queryIsExistCzcsnl.do")
	@ResponseBody
	public ResultMsg queryIsExistCzcsnl(String dxpjid,String xmhj,String projectid) {
		ResultMsg msg = null;
		try {
			if(projectid==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				List list = service.queryIsExistCzcsnl(dxpjid,xmhj,projectid);
				if(list.isEmpty()){
					msg = new ResultMsg(true,"");
				}else {
					msg = new ResultMsg(false,"");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("操作失败！"));
		}
		return msg;
	}
	/**
	 * 新增跳转
	 * @Title: czcsnlAdd 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/czcsnlAdd.do" })
	public ModelAndView czcsnlAdd(HttpServletRequest request) throws ServletException {
		String detial = StringUtil.stringConvert(request.getParameter("detail"));
		String pathString ="ppms/prepareCzcsnl/prepareCzcsnl_form";
		if ("1".equals(detial)) {
			pathString ="ppms/prepareCzcsnl/prepareCzcsnl_detail";
		}
		ModelAndView mav = new ModelAndView(pathString);
		return mav;
	}
	/**
	 * 财政承受能力保存提交
	 * @Title: thirdOrganSave 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/czcsnlSave.do")
	@ResponseBody
	public ResultMsg czcsnlSave(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.czcsnlSave(param);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	/**
	 * 查询财政承受能力评价信息
	 * @Title: queryCzcsnlForm 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/queryCzcsnlForm.do")
	@ResponseBody
	public ResultMsg queryCzcsnlForm(HttpServletRequest request) {
		ResultMsg msg =new ResultMsg(true, "");
		String czcsnlid =StringUtil.stringConvert(request.getParameter("czcsnlid"));
		List list = new ArrayList();
		try {
			list = service.queryCzcsnlForm(czcsnlid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		HashMap<String, Object> modelMap =new HashMap();
		if(!list.isEmpty()){
			JSONObject  json = (JSONObject) JSONObject.toJSON(list.get(0));
			modelMap.put("czcsnlForm", json);
			msg.setBody(modelMap);
		}else {
			modelMap.put("czcsnlForm", "");
			msg.setBody(modelMap);
		}
		return msg;
	}
	/**
	 * 查询财政预算
	 * @Title: financeQuery 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/financeQuery.do")
	@ResponseBody
	public EasyUITotalResult financeQuery(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.financeQuery(map);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 提交
	 * @Title: sendCzcsnl 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @param dlpjid
	 * @return 设定文件
	 */
	@RequestMapping("/sendCzcsnl.do")
	@ResponseBody
	public ResultMsg sendCzcsnl(String projectid,String czcsnlid) {
		ResultMsg msg = null;
		try {
			if(projectid==null || czcsnlid ==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.sendCzcsnl(projectid,czcsnlid);
				msg = new ResultMsg(true,"提交成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("提交失败！"));
		}
		return msg;
	}
	/**
	 * 财政承受能力撤回
	 * @Title: revokeCzcsnl 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @param dlpjid
	 * @return 设定文件
	 */
	@RequestMapping("/revokeCzcsnl.do")
	@ResponseBody
	public ResultMsg revokeCzcsnl(String projectid,String czcsnlid,String xmhj) {
		ResultMsg msg = null;
		try {
			if(projectid==null || czcsnlid ==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.revokeCzcsnl(projectid,czcsnlid);
				if ("1".equals(xmhj)){
					projectService.updateXmdqhj(projectid, "1");
				} else if ("2".equals(xmhj)){
					projectService.updateXmdqhj(projectid, "2");
				}
				msg = new ResultMsg(true,"撤回成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("撤回失败！"));
		}
		return msg;
	}
}
