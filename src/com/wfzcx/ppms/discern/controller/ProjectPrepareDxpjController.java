package com.wfzcx.ppms.discern.controller;

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

import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.ppms.discern.service.ProjectPrepareDxpjService;
/**
 * VFM定性评价
 * @ClassName: ProjectPrepareDxfxController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2016年3月10日20:15:10
 */
@Scope("prototype")
@Controller
@RequestMapping("/ppms/discern/ProjectPrepareDxpjController")
public class ProjectPrepareDxpjController {
	@Autowired
	ProjectPrepareDxpjService service;
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
		ModelAndView mav = new ModelAndView("ppms/prepareDxpj/prepareDxpj_init");
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
	@RequestMapping("/queryIsExistDxpj.do")
	@ResponseBody
	public ResultMsg queryIsExistDxpj(String projectid,String xmhj) {
		ResultMsg msg = null;
		try {
			if(projectid==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				List list = service.queryIsExistDxpj(projectid,xmhj);
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
	 * 新增页面跳转
	 * @Title: dxpjAdd 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/dxpjAdd.do" })
	public ModelAndView dxpjAdd(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/prepareDxpj/prepareDxpj_form");
		return mav;
	}
	/**
	 * 获取指标评分的列（专家）
	 * @Title: getZbdfGridColumns 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @param xmhj
	 * @return 设定文件
	 */
	@RequestMapping("/getZbdfGridColumns.do")
	@ResponseBody
	public ResultMsg getZbdfGridColumns(String pszbid) {
		ResultMsg msg = null;
		try {
			if(pszbid==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				String column = service.getZbdfGridColumns(pszbid);
				msg = new ResultMsg(true,column);
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("操作失败！"));
		}
		return msg;
	}
	/**
	 * 获取指标
	 * @Title: qualExpertGrid 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/queryZbList.do")
	@ResponseBody
	public EasyUITotalResult queryZbList(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryZbList(map);
		return EasyUITotalResult.from(ps);
	}
	/**
	 * 指标得分保存
	 * @Title: zbdfSave 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return 设定文件
	 */
	@RequestMapping("/zbdfSave.do")
	@ResponseBody
	public ResultMsg zbdfSave(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.zbdfSave(param);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	@RequestMapping("/sendDxpj.do")
	@ResponseBody
	public ResultMsg sendDxpj(String projectid,String dxpjid) {
		ResultMsg msg = null;
		try {
			if(projectid==null || dxpjid ==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.sendDxpj(projectid,dxpjid);
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
	 * @param dxpjid
	 * @return 设定文件
	 */
	@RequestMapping("/revokeDxpj.do")
	@ResponseBody
	public ResultMsg revokeDxpj(String projectid,String dxpjid) {
		ResultMsg msg = null;
		try {
			if(projectid==null || dxpjid ==null){
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			}else{
				service.revokeDxpj(projectid,dxpjid);
				msg = new ResultMsg(true,"撤回成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("撤回失败！"));
		}
		return msg;
	}
	/**
	 * 详情
	 * @Title: dxpjDetail 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws ServletException 设定文件
	 */
	@RequestMapping({ "/dxpjDetail.do" })
	public ModelAndView dxpjDetail(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/prepareDxpj/prepareDetail_form");
		return mav;
	}
}
