package com.wfzcx.aros.zlpc.controller;

import java.util.List;
import java.util.Map;

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
import com.wfzcx.aros.zlpc.service.ZlpcService;

@Scope("prototype")
@Controller
@RequestMapping("/aros/zlpc/controller/ZlpcController")
public class ZlpcController {

	@Autowired
	ZlpcService service;
	
	@RequestMapping("/zbInit.do")
	public ModelAndView zbInit(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("aros/zlpc/zb_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/zbQuery.do")
	@ResponseBody
	public EasyUITotalResult zbQuery(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.zbQuery(map);
		return EasyUITotalResult.from(ps);
	}
	
	@RequestMapping("/zbForm.do")
	public ModelAndView zbForm(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("aros/zlpc/zb_form");
		String standid = request.getParameter("standid");
		if(standid != null && !"".equals(standid)){
			mv.addObject("po", service.getById(standid));
		}
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/zbAdd.do")
	@ResponseBody
	public ResultMsg zbAdd(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String,Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.zbAdd(param);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e) {
			// TODO: handle exception
			msg = new ResultMsg(false, "保存失败！");
		}
		return msg;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/zbUpdate.do")
	@ResponseBody
	public ResultMsg zbUpdate(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String,Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.zbUpdate(param);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e) {
			// TODO: handle exception
			msg = new ResultMsg(false, "保存失败！");
		}
		return msg;
	}
	
	@RequestMapping("/zbDelete.do")
	@ResponseBody
	public ResultMsg zbDelete(String standid){
		ResultMsg msg = null;
		try {
			service.zbDelete(standid);
			msg = new ResultMsg(true, "删除成功！");
		} catch (Exception e) {
			// TODO: handle exception
			msg = new ResultMsg(false, "删除失败！");
		}
		return msg;
	}
	
	@RequestMapping("/zbDetail.do")
	public ModelAndView zbDetail(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("aros/zlpc/zb_detail");
		String standid = request.getParameter("standid");
		if(!"".equals(standid)){
			mv.addObject("po", service.getById(standid));
		}
		return mv;
	}
	
	@RequestMapping("/scoreInit.do")
	public ModelAndView scoreInit(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("aros/zlpc/score_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/scoreQuery.do")
	@ResponseBody
	public EasyUITotalResult scoreQuery(HttpServletRequest request){
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.scoreQuery(param);
		return EasyUITotalResult.from(ps);
	}
	
	@RequestMapping("/scoreForm.do")
	public ModelAndView scoreForm(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("aros/zlpc/score_form");
		String caseid = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		return mav;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/scoreTable.do")
	@ResponseBody
	public List scoreTable(HttpServletRequest request){
		String caseid = request.getParameter("caseid");
		List list = service.scoreTable(caseid);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/scoreSave.do")
	@ResponseBody
	public ResultMsg scoreSave(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String,Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.scoreSave(param);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			msg = new ResultMsg(false, "保存失败！");
		}
		return msg;
	}
	
	@RequestMapping("/scoreDelete.do")
	@ResponseBody
	public ResultMsg scoreDelete(String caseid){
		ResultMsg msg = null;
		try {
			service.scoreDelete(caseid);
			msg = new ResultMsg(true, "删除成功！");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			msg = new ResultMsg(false, "删除失败！");
		}
		return msg;
	}
	
	@RequestMapping("/scoreDetail.do")
	public ModelAndView scoreDetail(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("aros/zlpc/score_detail");
		String caseid = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		return mav;
	}
	
}
