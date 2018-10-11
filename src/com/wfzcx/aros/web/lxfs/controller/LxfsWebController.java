package com.wfzcx.aros.web.lxfs.controller;

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
import com.wfzcx.aros.web.lxfs.service.LxfsWebService;

/**
 * @ClassName: CbiWebController
 * @Description: 联系方式外网部分，实现外网联系方式查询详情功能
 * @author czp
 * @date 2016年9月8日 上午10:26:00
 * @version V1.0
 */



@Scope("prototype")
@Controller
public class LxfsWebController {
	@Autowired
	LxfsWebService lxfswebservice;
	/**
	 * 外网通过点击方式进入联系方式详情（LxfsWebController_init.do）
	 * @param request
	 * @return mv
	 * @throws ServletException
	 */
	@RequestMapping({ "/LxfsWebController_init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/web/lxfs/lxfsinfo_init");
		return mv;
	}
	/**
	 * @Title: LxfsqueryList
	 * @Description: 行政复议典型案例：查询典型案例（grid列表页面查询）
	 * @author czp
	 * @date 2016年8月12日 上午11:34:09
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/LxfsWebController_queryList.do")
	@ResponseBody
	public EasyUITotalResult dxalqueryList(HttpServletRequest request) throws AppException {
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		//执行查询
		PaginationSupport ps = lxfswebservice.queryLxfsList(param);
		return EasyUITotalResult.from(ps);
	}
	
}
