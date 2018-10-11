package com.wfzcx.aros.gzgl.controller;

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
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.aros.gzgl.service.BRulebaseinfoService;

@Scope("prototype")
@Controller
@RequestMapping("/aros/gzgl/controller/BRulebaseinfoController")
public class BRulebaseinfoController {
	
	@Autowired
	private BRulebaseinfoService service;
	/**
	 * 规则配置初始化
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/gzgl/brulebaseinfo_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
	}
	
	/**
	 * 按条件查询规则列表
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@ResponseBody
	@RequestMapping({ "/queryList.do" })
	public EasyUITotalResult queryList(HttpServletRequest request) throws ServletException{
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryBRulebaseinfoList(param);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 新增规则
	 * @param request
	 * @return
	 */
	@RequestMapping({ "/addBRuleInfo.do" })
	public ModelAndView addBRuleInfo(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("aros/gzgl/brulebaseinfo_form");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
	}
	/**
	 * 规则详情
	 * @param request
	 * @return
	 */
	@RequestMapping({ "/view.do" })
	public ModelAndView view(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("aros/gzgl/brulebaseinfo_view");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
	}
	
	/**
	 * 保存新增规则
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping({ "/saveRuleInfo.do" })
	public ResultMsg saveRuleInfo(HttpServletRequest request){
		ResultMsg msg = new ResultMsg(true,"保存成功");
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try{
			service.saveRuleInfo(param);
		}catch (Exception e) {
			msg = new ResultMsg(false,"保存失败");
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * 删除规则
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping({ "/delete.do" })
	public ResultMsg delete(HttpServletRequest request){
		ResultMsg msg = new ResultMsg(true,"删除成功");
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try{
			service.delete(param);
		}catch (Exception e) {
			msg = new ResultMsg(false,"删除失败");
			e.printStackTrace();
		}
		return msg;
	}
	/**
	 * 发布规则
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping({ "/redo.do" })
	public ResultMsg redo(HttpServletRequest request){
		ResultMsg msg = new ResultMsg(true,"发布成功");
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try{
			service.redo(param);
		}catch (Exception e) {
			msg = new ResultMsg(false,"发布失败");
			e.printStackTrace();
		}
		return msg;
	}

}
