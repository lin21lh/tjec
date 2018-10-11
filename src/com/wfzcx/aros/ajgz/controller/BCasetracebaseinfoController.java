package com.wfzcx.aros.ajgz.controller;

import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.wfzcx.aros.ajgz.po.BCasetracebaseinfo;
import com.wfzcx.aros.ajgz.service.BCasetracebaseinfoService;
import com.wfzcx.aros.xzfy.vo.CasebaseinfoVo;
import com.wfzcx.aros.xzfy.vo.ThirdbaseinfoVo;

/**
 * 
 * @author LinXF
 * @date 2016年8月17日 上午10:25:22
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/ajgz/BCasetracebaseinfoController")
public class BCasetracebaseinfoController {
	@Autowired
	BCasetracebaseinfoService service;

	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/ajgz/bcasetracebaseinfo_list");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws AppException
	 */
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
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryCaseBaseinfoList.do")
	@ResponseBody
	public EasyUITotalResult queryCaseBaseinfoList(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryCaseBaseinfoList(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/add.do" })
	public ModelAndView add(String id, String caseid, HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/ajgz/bcasetracebaseinfo_form");
		//附件ID
		UUID uuid = UUID.randomUUID();
		//新增时产生临时的uuid放在附件的keyid中
		mv.addObject("fjkeyid", uuid);
		
		CasebaseinfoVo casebaseinfo = service.getCaseBaseinfoVo(caseid);
		mv.addObject("po", casebaseinfo);
		
		ThirdbaseinfoVo third = service.getThirdbaseinfoByCaseid(caseid);
		mv.addObject("third", third);

		if (id != null && !id.trim().equals("")) {
			BCasetracebaseinfo bCasetracebaseinfo = new BCasetracebaseinfo();
			bCasetracebaseinfo = service.getBCasetracebaseinfo(id);
			mv.addObject("ct", bCasetracebaseinfo);
		}

		return mv;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/save.do")
	@ResponseBody
	public ResultMsg save(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String id = service.save(param);
			
			if (!StringUtils.isBlank(id)) {
				msg = new ResultMsg(true, "保存成功");
			} else {
				msg = new ResultMsg(false, "保存失败");
			}
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
	public ResultMsg delete(String menuid, String id, HttpServletRequest request) {
		ResultMsg msg = null;
		try {
			if (id == null || "null".equals(id) || "".equals(id.trim())) {
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			} else {
				String msgString = service.delete(id);
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
	public ModelAndView view(String id, String caseid, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("aros/ajgz/bcasetracebaseinfo_view");

		CasebaseinfoVo casebaseinfo = service.getCaseBaseinfoVo(caseid);
		mv.addObject("po", casebaseinfo);
		
		ThirdbaseinfoVo third = service.getThirdbaseinfoByCaseid(caseid);
		mv.addObject("third", third);

		if (id != null && !id.trim().equals("")) {
			BCasetracebaseinfo bCasetracebaseinfo = new BCasetracebaseinfo();
			bCasetracebaseinfo = service.getBCasetracebaseinfo(id);
			mv.addObject("ct", bCasetracebaseinfo);
		}
		
		return mv;
	}
}
