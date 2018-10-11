package com.wfzcx.app.fysq.controller;

import java.util.LinkedList;
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
import com.wfzcx.app.fysq.service.FysqService;
import com.wfzcx.aros.web.xzfy.service.CbiWebService;
/**
 * 复议申请
 * 
 * @author wang_yliang
 * @date 2016-9-22
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/fysq/FysqController")
public class FysqController {

	@Autowired
	FysqService service;
	@Autowired
	CbiWebService cbiWebService;
	
	@RequestMapping("/add.do")
	@ResponseBody
	public GeneralTotalResult add(HttpServletRequest request) throws AppException {
		try {
			Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
			Object obj = cbiWebService.addXzfyReqForApp(param);
			Map rel = BeanUtils.describe(obj);
			LinkedList<Map> list = new LinkedList<Map>();
			list.add(rel);
			return new GeneralTotalResult<Map>(new TotalResult<Map>(list,list.size()), true, "");
		} catch (Exception e) {
			return new GeneralTotalResult<Map>(new TotalResult<Map>(new LinkedList<Map>(),0), false,e.getMessage());
		}
		
	}
}
