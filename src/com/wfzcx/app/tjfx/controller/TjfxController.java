package com.wfzcx.app.tjfx.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.freework.freedbm.util.GeneralTotalResult;
import com.freework.freedbm.util.TotalResult;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.wfzcx.app.tjfx.service.TjfxService;
import com.wfzcx.aros.flow.service.ProbaseinfoService;

/**
 * 统计分析
 * 
 * @author wang_yliang
 * @date 2016-9-22
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/tjfx/TjfxController")
public class TjfxController {

	@Autowired
	TjfxService service;
	@Autowired
	ProbaseinfoService chartService;
	
	@RequestMapping("/tjone.do")
	@ResponseBody
	public GeneralTotalResult tjone(HttpServletRequest request) throws AppException {
		try {
			Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
			LinkedList<Map> list = new LinkedList<Map>();
			List<Map<String, Object>> rel = chartService.queryCaseTotalAnalysis(param);
			for(Map map:rel){
				list.add(map);
			}
			return new GeneralTotalResult<Map>(new TotalResult<Map>(list,list.size()), true, "");
		} catch (Exception e) {
			return new GeneralTotalResult<Map>(new TotalResult<Map>(new LinkedList<Map>(),0), false,e.getMessage());
		}
		
	}
	
	@RequestMapping("/tjtwo.do")
	@ResponseBody
	public GeneralTotalResult tjtwo(HttpServletRequest request) throws AppException {
		try {
			Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
			List<Map<String, Object>> rel = chartService.queryProbaseinfoSumForWait();
			LinkedList<Map> list = new LinkedList<Map>();
			for(Map map:rel){
				list.add(map);
			}
			return new GeneralTotalResult<Map>(new TotalResult<Map>(list,list.size()), true, "");
		} catch (Exception e) {
			return new GeneralTotalResult<Map>(new TotalResult<Map>(new LinkedList<Map>(),0), false,e.getMessage());
		}
		
	}
	
	
}
