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
import com.wfzcx.ppms.discern.service.ProjectPrepareDlpjService;
/**
 * VFM定量评价
 * @ClassName: ProjectPrepareDxfxController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2016年3月15日9:38:25
 */
@Scope("prototype")
@Controller
@RequestMapping("/ppms/discern/ProjectPrepareDlpjController")
public class ProjectPrepareDlpjController {
	@Autowired
	ProjectPrepareDlpjService service;
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
		ModelAndView mav = new ModelAndView("ppms/prepareDlpj/prepareDlpj_init");
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
	 * 查询是否已经录入
	 * @Title: queryIsExistDxpj 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @param xmhj
	 * @return 设定文件
	 */
	@RequestMapping("/queryIsExistDlpj.do")
	@ResponseBody
	public ResultMsg queryIsExistDlpj(String dxpjid,String xmhj) {
		ResultMsg msg = null;
		try {
			if(dxpjid==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				List list = service.queryIsExistDlpj(dxpjid,xmhj);
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
	 * @Title: dlpjAdd 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/dlpjAdd.do" })
	public ModelAndView dlpjAdd(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/prepareDlpj/prepareDlpj_form");
		String menuid  = request.getParameter("menuid");
		String xmhj  = request.getParameter("xmhj");
		mav.addObject("menuid", menuid);
		mav.addObject("xmhj", xmhj);
		return mav;
	}
	/**
	 * 查询第三方机构
	 * @Title: queryDsfJg 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@ResponseBody
	@RequestMapping({ "/queryDsfJg.do" })
	public EasyUITotalResult queryDsfJg(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryDsfJg(map);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 定量分析保存
	 * @Title: thirdOrganSave 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/dlpjSave.do")
	@ResponseBody
	public ResultMsg dlpjSave(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.dlpjSave(param);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	/**
	 * 查询第三方机构库
	 * @Title: querythirdOrgan 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/querythirdOrgan.do")
	@ResponseBody
	public ResultMsg querythirdOrgan(HttpServletRequest request) {
		ResultMsg msg =new ResultMsg(true, "");
		String dlpjid =StringUtil.stringConvert(request.getParameter("dlpjid"));
		List approveList = new ArrayList();
		try {
			approveList = service.querythirdOrgan(dlpjid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		HashMap<String, Object> modelMap =new HashMap();
		if(!approveList.isEmpty()){
			JSONObject  json = (JSONObject) JSONObject.toJSON(approveList.get(0));
			modelMap.put("thirdOrgan", json);
			msg.setBody(modelMap);
		}else {
			modelMap.put("thirdOrgan", "");
			msg.setBody(modelMap);
		}
		return msg;
	}
	/**
	 * 第三方机构查询
	 * @Title: thirdOrgQuery 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/thirdOrgQuery.do")
	@ResponseBody
	public EasyUITotalResult thirdOrgQuery(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.thirdOrgQuery(map);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 提交
	 * @Title: sendDxpj 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @param dlpjid
	 * @return 设定文件
	 */
	@RequestMapping("/sendDlpj.do")
	@ResponseBody
	public ResultMsg sendDlpj(String projectid,String dlpjid) {
		ResultMsg msg = null;
		try {
			if(projectid==null || dlpjid ==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.sendDlpj(projectid,dlpjid);
				msg = new ResultMsg(true,"提交成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("提交失败！"));
		}
		return msg;
	}
	/**
	 * 评审准备撤回
	 * @Title: revokePszb 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @param dlpjid
	 * @return 设定文件
	 */
	@RequestMapping("/revokeDlpj.do")
	@ResponseBody
	public ResultMsg revokeDlpj(String projectid,String dlpjid) {
		ResultMsg msg = null;
		try {
			if(projectid==null || dlpjid ==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.revokeDlpj(projectid,dlpjid);
				msg = new ResultMsg(true,"撤回成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("撤回失败！"));
		}
		return msg;
	}
	@RequestMapping({ "/dlpjDetail.do" })
	public ModelAndView dlpjDetail(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/prepareDlpj/prepareDlpj_detail");
		String menuid  = request.getParameter("menuid");
		String xmhj  = request.getParameter("xmhj");
		mav.addObject("menuid", menuid);
		mav.addObject("xmhj", xmhj);
		return mav;
	}
}
