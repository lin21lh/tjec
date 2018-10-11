/**
 * @Description: 外网查看典型案例
 * @author zhangtiantian
 * @date 2016年9月23日
 */
package com.wfzcx.aros.web.dxal.controller;

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
import com.wfzcx.aros.web.dxal.service.DxalWebService;

@Scope("prototype")
@Controller
public class DxalWebController
{
	@Autowired
	DxalWebService dxalservice;
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/DxalWebController_init.do" })
	public ModelAndView init(HttpServletRequest request)
	{
		ModelAndView mv = new ModelAndView("aros/web/dxal/dxalinfo_init");
		return mv;
	}
	/**
	 * @Title: dxalqueryList
	 * @Description: 行政复议典型案例：查询典型案例（grid列表页面查询）
	 * @author zhangtiantian
	 * @date 2016年9月23日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/DxalWebController_queryList.do")
	@ResponseBody
	public EasyUITotalResult dxalqueryList(HttpServletRequest request)
	{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = dxalservice.queryList(map);
		return EasyUITotalResult.from(ps);
	}

	
	/**
	 * @Title: dxalDetail
	 * @Description: 行政复议申请：典型案例详情页面
	 * @author zhangtiantian
	 * @date 2016年9月23日
	 * @param request
	 * @return
	 */
	@RequestMapping({"/DxalWebController_view.do"})
	public ModelAndView dxalDetail(HttpServletRequest request)
	{ 
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/web/dxal/dxalinfo_view");
		return mav;
	}
	
	/**
	 * 查询clob字段内容
	 * @param messageType
	 * @return
	 */
	@RequestMapping("/DxalWebController_getClobContentVal.do")
	@ResponseBody
	public String getClobContentVal(String conid)
	{  
		String contentStr = dxalservice.getClobContentVal(conid);
		return contentStr;
	}
}
