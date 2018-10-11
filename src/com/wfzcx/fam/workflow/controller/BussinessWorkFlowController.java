package com.wfzcx.fam.workflow.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.fam.workflow.service.BussinessWorkFlowService;
import com.wfzcx.fam.workflow.vo.ActivitiyVO;

@Scope("prototype")
@Controller
@RequestMapping({"/workflow/BussinessWorkFlowController"})
public class BussinessWorkFlowController {
	
	@Autowired
	BussinessWorkFlowService bussinessWorkFlowService;

	/**
	 * 查询当前工作流key下所有任务节点
	 * @Title: findActivitiesByKey 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return List<ActivitiyVO> 返回类型 
	 * @throws
	 */
	@RequestMapping("/findActivitiesByKey.do")
	@ResponseBody
	public List<ActivitiyVO> findActivitiesByKey(HttpServletRequest request) throws AppException {
		String key = request.getParameter("key");
		String wfversionStr = request.getParameter("wfversion");
		String activitiyid = request.getParameter("activitiyid");
		String activitiyname = request.getParameter("activitiyname");
		
		if (StringUtil.isBlank(key))
			return new ArrayList<ActivitiyVO>();
		Integer wfversion = null;
		if (StringUtil.isNotBlank(wfversionStr))
			wfversion = Integer.valueOf(wfversionStr);
		return bussinessWorkFlowService.findActivitiesByWfKey(key, wfversion, activitiyid, activitiyname);
	}
	
	/**
	 * @throws AppException 
	 * 
	 * @Title: findVersionListByKey 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param key
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/findVersionInfoByKey.do")
	@ResponseBody
	public ResultMsg findVersionListByKey(String key) {
		ResultMsg resultMsg = null;
		HashMap<String, Object> body = new HashMap<String, Object>();
		try {
			body.put("versionList", bussinessWorkFlowService.findVersionListByKey(key));
			body.put("currentVersion", bussinessWorkFlowService.getCurrentVersion(key));
			resultMsg = new ResultMsg(true, "", body);
			return resultMsg;
		} catch (AppException e) {
			resultMsg = new ResultMsg(false, e.getMessage(), body);
			return resultMsg;
		}
		
	}
	
	@RequestMapping("/findColumnByWfKey.do")
	@ResponseBody
	public List<SysDicColumn> findColumnByWfKey(String wfkey, String sourceElementFlag) throws AppException {
		
		return bussinessWorkFlowService.findColumnsByWfKey(wfkey, Boolean.valueOf(sourceElementFlag));
	}
}
