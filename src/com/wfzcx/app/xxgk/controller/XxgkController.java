package com.wfzcx.app.xxgk.controller;

import java.util.HashMap;
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
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.wfzcx.app.xxgk.service.XxgkService;
import com.wfzcx.aros.print.po.Noticebaseinfo;

/**
 * 信息公开
 * 
 * @author wang_yliang
 * @date 2016-9-22
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/xxgk/XxgkController")
public class XxgkController {

	@Autowired
	XxgkService service;
	
	/**
	 * 测试用例
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/test.do")
	@ResponseBody
	public GeneralTotalResult test(HttpServletRequest request) throws AppException {
		try {
			Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
			LinkedList<Map> list = new LinkedList<Map>();
			Map map = new HashMap<String, Object>();
			map.put("t1", "");
			map.put("t2", null);
			map.put("t3", "test");
			list.add(map);
			return new GeneralTotalResult<Map>(new TotalResult<Map>(list,list.size()), true, "");
		} catch (Exception e) {
			return new GeneralTotalResult<Map>(new TotalResult<Map>(new LinkedList<Map>(),0), false,e.getMessage());
		}
		
	}
	
	@RequestMapping("/mesDetail.do")
	@ResponseBody
	public GeneralTotalResult mesDetail(HttpServletRequest request) throws AppException {
		try {
			Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
			Noticebaseinfo info = service.mesDetail(param.get("noticeid").toString());
			LinkedList<Map> list = new LinkedList<Map>();
			Map map1 = BeanUtils.describe(info);
			list.add(map1);
			String contents = info.getContents();
			XStream xstream = new XStream(new DomDriver());
			Object obj = xstream.fromXML(contents);
			Map map2 = BeanUtils.describe(obj);
			list.add(map2);
			return new GeneralTotalResult<Map>(new TotalResult<Map>(list,list.size()), true, "");
		} catch (Exception e) {
			return new GeneralTotalResult<Map>(new TotalResult<Map>(new LinkedList<Map>(),0), false,e.getMessage());
		}
		
	}
}
