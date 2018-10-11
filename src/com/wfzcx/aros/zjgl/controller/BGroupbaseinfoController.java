package com.wfzcx.aros.zjgl.controller;

import java.util.Calendar;
import java.util.List;
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
import com.jbf.common.web.ResultMsg;
import com.wfzcx.aros.zjgl.service.BGroupbaseinfoService;

/**
 * 
 * @author LinXF
 * @date 2016年8月18日 上午11:04:57
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/zjgl/BGroupbaseinfoController")
public class BGroupbaseinfoController {
	@Autowired
	BGroupbaseinfoService service;
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/zjgl/bgroupbaseinfo_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
	}
	
	@RequestMapping({ "/zjxzInit.do" })
	public ModelAndView zjxzInit(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/zjgl/zjxz_form");
		long timeLong = Calendar.getInstance().getTimeInMillis();
		String menuid = request.getParameter("menuid");
		String caseid = request.getParameter("caseid");
		mv.addObject("timeLong", timeLong);
		mv.addObject("caseid", caseid);
		mv.addObject("menuid", menuid);
		return mv;
	}
	
//	@RequestMapping({ "/div2.do" })
//	public ModelAndView div2(HttpServletRequest request) throws ServletException {
//		ModelAndView mv = new ModelAndView("aros/zjgl/roleEntry");
//		String menuid = request.getParameter("menuid");
//		mv.addObject("menuid", menuid);
//		return mv;
//	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/queryList.do")
	@ResponseBody
	public EasyUITotalResult queryList(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryList(map);
		return EasyUITotalResult.from(ps);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/querySpeList.do")
	@ResponseBody
	public List querySpeList(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		return service.querySpeList(map);
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/add.do" })
	public ModelAndView add(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/zjgl/bgroupbaseinfo_form");
		return mv;
	}
	
	@RequestMapping("/save.do")
	@ResponseBody
	public ResultMsg save(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String id = service.save(param);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 
	 * @param menuid
	 * @param speid
	 * @param request
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public ResultMsg delete(String menuid, String groupid, HttpServletRequest request) {
		ResultMsg msg = null;
		try {
			if (groupid == null || "null".equals(groupid) || "".equals(groupid.trim())) {
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			} else {
				String msgString = service.delete(groupid);
				if ("".equals(msgString)) {
					msg = new ResultMsg(true, AppException.getMessage("删除成功！"));
				} else {
					msg = new ResultMsg(false, AppException.getMessage("删除失败！" + msgString));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败！"));
		}
		return msg;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/view.do")
	public ModelAndView projectXqgsView(String groupid, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("aros/zjgl/bgroupbaseinfo_view");
		return mv;
	}
	
	@RequestMapping({ "/saveZjxz.do" })
	@ResponseBody
	public ResultMsg saveZjxz(HttpServletRequest request) throws ServletException {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.saveZjxz(param);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: discussListInit
	 * @Description: 复议研讨发起：返回复议研讨发起页面
	 * @author ybb
	 * @date 2016年11月15日 下午3:35:26
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/discussListInit.do" })
	public ModelAndView discussListInit(HttpServletRequest request) throws ServletException {
		
		ModelAndView mv = new ModelAndView("aros/zjgl/discuss_form");
		
		String menuid = request.getParameter("menuid");
		String caseid = request.getParameter("caseid");
		
		mv.addObject("caseid", caseid);
		mv.addObject("menuid", menuid);
		
		return mv;
	}
	
	/**
	 * @Title: querySpecialists
	 * @Description: 复议研讨发起-查询所有专家列表
	 * @author ybb
	 * @date 2016年11月15日 下午4:15:39
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/querySpecialists.do")
	@ResponseBody
	public List<?> querySpecialists(HttpServletRequest request) throws AppException {
		
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		
		return service.querySpecialists(map);
	}
}
