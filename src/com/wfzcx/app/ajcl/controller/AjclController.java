package com.wfzcx.app.ajcl.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.freework.freedbm.util.GeneralTotalResult;
import com.freework.freedbm.util.TotalResult;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.wfzcx.app.ajcl.service.AjclService;
import com.wfzcx.aros.web.xzfy.service.CbiWebService;
import com.wfzcx.aros.xzfy.po.Casebaseinfo;
import com.wfzcx.aros.xzfy.service.WebDealService;

@Scope("prototype")
@Controller
@RequestMapping("/app/ajcl/AjclController")
public class AjclController {

	@Autowired
	AjclService service;
	@Autowired
	CbiWebService cbiWebService;
	@Autowired
	WebDealService webDealService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/saveInfo.do")
	@ResponseBody
	public GeneralTotalResult saveInfo(HttpServletRequest request) {
		try {
			Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
			param.put("mobileFlag", "1");
			Map<String,Object> map = webDealService.saveInfo(param);
			LinkedList<Map> list = new LinkedList<Map>();
			list.add(map);
			return new GeneralTotalResult<Map>(new TotalResult<Map>(list,list.size()), true, "");
		} catch (Exception e) {
			return new GeneralTotalResult<Map>(new TotalResult<Map>(new LinkedList<Map>(),0), false,e.getMessage());
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/sendInfo.do")
	@ResponseBody
	public GeneralTotalResult sendInfo(HttpServletRequest request) {
		try {
			Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
			param.put("mobileFlag", "1");
			Map<String,Object> map = webDealService.sendInfo(param);
			LinkedList<Map> list = new LinkedList<Map>();
			list.add(map);
			return new GeneralTotalResult<Map>(new TotalResult<Map>(list,list.size()), true, "");
		} catch (Exception e) {
			return new GeneralTotalResult<Map>(new TotalResult<Map>(new LinkedList<Map>(),0), false,e.getMessage());
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/webAddCaseInfo.do")
	@ResponseBody
	public GeneralTotalResult webAddCaseInfo(HttpServletRequest request) {
		try {
			Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
			param.put("mobileFlag", "1");
			Map<String,Object> map = webDealService.webAddCaseInfo(param);
			LinkedList<Map> list = new LinkedList<Map>();
			list.add(map);
			return new GeneralTotalResult<Map>(new TotalResult<Map>(list,list.size()), true, "");
		} catch (Exception e) {
			return new GeneralTotalResult<Map>(new TotalResult<Map>(new LinkedList<Map>(),0), false,e.getMessage());
		}
		
	}
	
	@RequestMapping("/handle.do")
	@ResponseBody
	public GeneralTotalResult handle(HttpServletRequest request) throws AppException {
		try {
			Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
			String caseid = param.get("caseid").toString();
			String nodeid = param.get("nodeid").toString();
			String result = param.get("result").toString();
			String resultmsg = param.get("resultmsg").toString();
			String remark = param.get("remark").toString();
			Casebaseinfo info = null;
			if("2".equals(nodeid)){
				info = cbiWebService.addCasebaseinfoAcceptForApp(caseid, nodeid, result, resultmsg, remark);
			} else if ("3".equals(nodeid)){
				info = cbiWebService.addCasebaseinfoReviewForApp(caseid, nodeid, result, resultmsg, remark);
			} else if ("4".equals(nodeid)){
				info = cbiWebService.addCasebaseinfoDecisionForApp(caseid, nodeid, result, resultmsg, remark);
			} else {
				return new GeneralTotalResult<Map>(new TotalResult<Map>(new LinkedList<Map>(),0), false,"环节信息错误！");
			}
			Map rel = BeanUtils.describe(info);
			LinkedList<Map> list = new LinkedList<Map>();
			list.add(rel);
			return new GeneralTotalResult<Map>(new TotalResult<Map>(list,list.size()), true, "");
		} catch (Exception e) {
			return new GeneralTotalResult<Map>(new TotalResult<Map>(new LinkedList<Map>(),0), false,e.getMessage());
		}
		
	}
	
	@RequestMapping("/opinionAll.do")
	@ResponseBody
	public GeneralTotalResult opinionAll(HttpServletRequest request) throws AppException {
		try {
			Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
			String caseid = param.get("caseid").toString();
			List li = cbiWebService.queryProbaseinfoListForApp(caseid);
			LinkedList<Map> list = new LinkedList<Map>();
			for(int i=0;i<li.size();i++){
				Map rel = BeanUtils.describe(li.get(i));
				list.add(rel);
			}
			return new GeneralTotalResult<Map>(new TotalResult<Map>(list,list.size()), true, "");
		} catch (Exception e) {
			return new GeneralTotalResult<Map>(new TotalResult<Map>(new LinkedList<Map>(),0), false,e.getMessage());
		}
		
	}
	
	
}
