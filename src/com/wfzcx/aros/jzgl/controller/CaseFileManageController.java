package com.wfzcx.aros.jzgl.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.wfzcx.aros.jzgl.service.CaseFileManageService;
import com.wfzcx.aros.util.GCC;

@SuppressWarnings("unchecked")
@Scope("prototype")
@Controller
@RequestMapping("/aros/jzgl/controller/CaseFileManageController")
public class CaseFileManageController {
	
	@Autowired
	private CaseFileManageService service;
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/jzgl/casefilemanage_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("cantAddFile", GCC.RCASEBASEINFO_PSTATE_FINISH);
		mv.addObject("menuid", menuid);
		return mv;
	}
	
	/**
	 * 新增案件文档
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/addCaseFile.do" })
	public ModelAndView addCaseFile(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/jzgl/casefilemanage_addFile");
		String caseid = request.getParameter("caseid");
		long timeLong = Calendar.getInstance().getTimeInMillis();
		mv.addObject("caseid", caseid);
		mv.addObject("timeLong", timeLong);
		return mv;
	}
	
	/**
	 * 保存新增的文档信息
	 * @param request
	 * @throws ServletException
	 */
	@RequestMapping({ "/saveAddCaseFile.do" })
	@ResponseBody
	public ResultMsg saveAddCaseFile(HttpServletRequest request) throws ServletException {
		ResultMsg msg = new ResultMsg(true,"保存成功");
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try{
			service.saveCaseFileKid(param);
		}catch (Exception e) {
			msg = new ResultMsg(false,"保存失败");
		}
		return msg;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/queryCaseBaseinfoList.do")
	@ResponseBody
	public EasyUITotalResult queryCaseBaseinfoList(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryCaseBaseinfoList(map);
		return EasyUITotalResult.from(ps);
	}
	
	@RequestMapping({ "/queryAllFile.do" })
	@ResponseBody
	public List<Map<String, Object>> queryAllFile(HttpServletRequest request){
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		List<Map<String, Object>> queryCaseFileByCaseId = service.queryCaseFileByCaseId(param);
		return 	queryCaseFileByCaseId;
	}
	
	@RequestMapping({ "/workflowCheckBox.do" })
	@ResponseBody
	public List<Map<String, Object>> workflowCheckBox(){
		return 	service.queryWorkflowCheckBox();
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping({ "/palceOnFile.do" })
	public ResultMsg palceOnFile(HttpServletRequest request){
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String  caseid = (String) param.get("caseid");
		ResultMsg msg = null;
		try{
			if(service.checkCaseIsClose(caseid)){
				service.palceOnFile(caseid);
				msg = new ResultMsg(true, AppException.getMessage("归档成功！"));
			}else{
				msg = new ResultMsg(false, AppException.getMessage("归档失败！结案后的案件才允许归档！"));
			}
		}catch (Exception e) {
			msg = new ResultMsg(false, AppException.getMessage("归档失败！"));
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * 下载当前案件所有卷宗文件 （zip格式）
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping({ "/downLoadAllFile.do" })
	public void downLoadAllFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		service.downloadAllFile(request, response, param);
	}
	
	/**
	 * 下载doc版通知书
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping({ "/downLoadFile.do" })
	public void downLoadFile(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		service.downCaseFile(response, param);
	}
	
	/**
	 * 案件卷宗查看列表
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/caseFileListPage.do" })
	public ModelAndView caseFileListPage(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/jzgl/casefilemanage_form");
		String menuid = request.getParameter("menuid");
		String caseid = request.getParameter("caseid");
		String state = service.getCaseStateByCaseid(caseid);
		mv.addObject("state", state);
		mv.addObject("caseid", caseid);
		return mv;
	}

	/**
	 * @Title: caseFileInit
	 * @Description: 案件查询-卷宗下载
	 * @author ybb
	 * @date 2016年12月12日 上午11:13:01
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/caseFileInit.do" })
	public ModelAndView caseFileInit(HttpServletRequest request) throws ServletException {
		
		ModelAndView mv = new ModelAndView("aros/jzgl/casefilelist_init");
		
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		
		return mv;
	}
}
