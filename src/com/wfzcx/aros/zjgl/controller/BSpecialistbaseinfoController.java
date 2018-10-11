package com.wfzcx.aros.zjgl.controller;

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
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.aros.zjgl.po.BSpecialistbaseinfo;
import com.wfzcx.aros.zjgl.service.BSpecialistbaseinfoService;

/**
 * 
 * @author LinXF
 * @date 2016年8月11日 下午2:57:22
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/zjgl/controller")
public class BSpecialistbaseinfoController {
	@Autowired
	BSpecialistbaseinfoService service;

	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/zjgl/bspecialistbaseinfo_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/queryList.do")
	@ResponseBody
	public EasyUITotalResult queryList(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryList(map);
		return EasyUITotalResult.from(ps);
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/add.do" })
	public ModelAndView add(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/zjgl/bspecialistbaseinfo_form");
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String operflag = StringUtil.stringConvert(param.get("operflag"));
		boolean passflag = true;
		if(operflag.equals("edit")) {
			passflag = false;
		}
		mv.addObject("passflag",passflag);
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
	public ResultMsg delete(String menuid, String speid, HttpServletRequest request) {
		ResultMsg msg = null;
		try {
			if (speid == null || "null".equals(speid) || "".equals(speid.trim())) {
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			} else {
				String msgString = service.delete(speid);
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
	public ModelAndView projectXqgsView(String id, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("aros/zjgl/bspecialistbaseinfo_view");
		String speid = request.getParameter("speid");
		BSpecialistbaseinfo info = service.querySpeInfo(speid);
		mv.addObject("info", info);
		return mv;
	}
}
