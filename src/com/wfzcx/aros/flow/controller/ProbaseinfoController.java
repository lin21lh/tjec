package com.wfzcx.aros.flow.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jbf.common.exception.AppException;
import com.wfzcx.aros.flow.service.ProbaseinfoService;
import com.wfzcx.aros.util.GCC;

/**
 * @ClassName: ProbaseinfoController
 * @Description: 流程过程业务处理
 * @author ybb
 * @date 2016年8月15日 下午3:11:29
 * @version V1.0
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/flow/controller/ProbaseinfoController")
public class ProbaseinfoController {

	@Autowired
	private ProbaseinfoService probaseinfoService;
	
	/**
	 * @Title: queryFlowForReq
	 * @Description:行政复议申请：查看流程信息
	 * @author ybb
	 * @date 2016年8月15日 下午3:14:31
	 * @param request
	 * @return
	 * @throws AppException 
	 */
	@RequestMapping({"/queryFlowForReq.do"})
	public ModelAndView queryFlowForReq(HttpServletRequest request) throws AppException {
		
		//返回页面路径 
		ModelAndView mav = new ModelAndView("aros/flow/flow_message");
		//案件ID
		mav.addObject("caseid", request.getParameter("caseid"));
		
		List<JSONObject> list = probaseinfoService.queryProbaseinfoListByParam(request.getParameter("caseid"),
				GCC.PROBASEINFO_PROTYPE_XZFYAUDIT);
		mav.addObject("wfList", list);
		
		//查询流程节点
		BigDecimal nodeid = probaseinfoService.queryNodeidByCaseid(request.getParameter("caseid"));
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	/**
	 * @Title: queryFlowForReq_simcase
	 * @Description:行政复议申请：相似案件查看流程信息
	 * @author ybb
	 * @date 2016年8月15日 下午3:14:31
	 * @param request
	 * @return
	 * @throws AppException 
	 */
	
	@RequestMapping({"/queryFlowForReq_simcase.do"})
	public ModelAndView queryFlowForReq_simcase(HttpServletRequest request) throws AppException {
		
		//返回页面路径 
		ModelAndView mav = new ModelAndView("aros/flow/flow_message_over");
		//案件ID
		mav.addObject("caseid", request.getParameter("caseid"));
		
		List<JSONObject> list = probaseinfoService.queryProbaseinfoListByParam(request.getParameter("caseid"),
				GCC.PROBASEINFO_PROTYPE_XZFYAUDIT);
		mav.addObject("wfList", list);
		
		//查询流程节点
		BigDecimal nodeid = probaseinfoService.queryNodeidByCaseid(request.getParameter("caseid"));
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	
	/**
	 * @Title: queryFlowForCompromise
	 * @Description: 行政复议和解：查看流程信息
	 * @author ybb
	 * @date 2016年8月23日 上午9:23:45
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping({"/queryFlowForCompromise.do"})
	public ModelAndView queryFlowForCompromise(HttpServletRequest request) throws AppException {
		
		//返回页面路径 
		ModelAndView mav = new ModelAndView("aros/flow/flow_msg");
		//案件ID
		mav.addObject("caseid", request.getParameter("caseid"));
		
		List<JSONObject> list = probaseinfoService.queryProbaseinfosByCaseid(request.getParameter("caseid"),
				GCC.PROBASEINFO_PROTYPE_XZFYCOMPROMISE);
		mav.addObject("wfList", list);
		
		//查询流程节点
		BigDecimal nodeid = probaseinfoService.queryNodeidByCaseid(request.getParameter("caseid"),
				GCC.PROBASEINFO_PROTYPE_XZFYCOMPROMISE);
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	/**
	 * @Title: queryFlowForRecover
	 * @Description: 行政复议恢复：查看流程信息
	 * @author ybb
	 * @date 2016年8月23日 上午9:24:46
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping({"/queryFlowForRecover.do"})
	public ModelAndView queryFlowForRecover(HttpServletRequest request) throws AppException {
		
		//返回页面路径 
		ModelAndView mav = new ModelAndView("aros/flow/flow_msg");
		//案件ID
		mav.addObject("caseid", request.getParameter("caseid"));
		
		List<JSONObject> list = probaseinfoService.queryProbaseinfosByCaseid(request.getParameter("caseid"),
				GCC.PROBASEINFO_PROTYPE_XZFYRECOVER);
		mav.addObject("wfList", list);
		
		//查询流程节点
		BigDecimal nodeid = probaseinfoService.queryNodeidByCaseid(request.getParameter("caseid"),
				GCC.PROBASEINFO_PROTYPE_XZFYRECOVER);
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	/**
	 * @Title: queryFlowForEnd
	 * @Description: 行政复议终止：查看流程信息
	 * @author ybb
	 * @date 2016年8月23日 上午9:25:21
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping({"/queryFlowForEnd.do"})
	public ModelAndView queryFlowForEnd(HttpServletRequest request) throws AppException {
		
		//返回页面路径 
		ModelAndView mav = new ModelAndView("aros/flow/flow_msg");
		//案件ID
		mav.addObject("caseid", request.getParameter("caseid"));
		
		List<JSONObject> list = probaseinfoService.queryProbaseinfosByCaseid(request.getParameter("caseid"),
				GCC.PROBASEINFO_PROTYPE_XZFYEND);
		mav.addObject("wfList", list);
		
		//查询流程节点
		BigDecimal nodeid = probaseinfoService.queryNodeidByCaseid(request.getParameter("caseid"),
				GCC.PROBASEINFO_PROTYPE_XZFYEND);
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	/**
	 * @Title: queryFlowForSuspend
	 * @Description: 行政复议中止：查看流程信息
	 * @author ybb
	 * @date 2016年8月23日 上午9:26:35
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping({"/queryFlowForSuspend.do"})
	public ModelAndView queryFlowForSuspend(HttpServletRequest request) throws AppException {
		
		//返回页面路径 
		ModelAndView mav = new ModelAndView("aros/flow/flow_msg");
		//案件ID
		mav.addObject("caseid", request.getParameter("caseid"));
		
		List<JSONObject> list = probaseinfoService.queryProbaseinfosByCaseid(request.getParameter("caseid"),
				GCC.PROBASEINFO_PROTYPE_XZFYSUSPEND);
		mav.addObject("wfList", list);
		
		//查询流程节点
		BigDecimal nodeid = probaseinfoService.queryNodeidByCaseid(request.getParameter("caseid"),
				GCC.PROBASEINFO_PROTYPE_XZFYSUSPEND);
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	/**
	 * @Title: queryFlowForView
	 * @Description: 案件：查看流程信息
	 * @author ybb
	 * @date 2016年8月26日 上午11:05:00
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping({"/queryFlowForView.do"})
	public ModelAndView queryFlowForView(HttpServletRequest request) throws AppException {
		
		//返回页面路径 
		ModelAndView mav = new ModelAndView("aros/flow/flow_message");
		//案件ID
		mav.addObject("caseid", request.getParameter("caseid"));
		
		List<JSONObject> list = probaseinfoService.queryProbaseinfoListByParam(request.getParameter("caseid"),
				request.getParameter("protype"));
		mav.addObject("wfList", list);
	
		//查询流程节点
		BigDecimal nodeid = probaseinfoService.queryNodeidByCaseid(request.getParameter("caseid"));
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	/**
	 * @Title: queryFlowForCancel
	 * @Description: 行政复议撤销：查看流程信息
	 * @author ybb
	 * @date 2016年8月26日 下午2:40:54
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping({"/queryFlowForCancel.do"})
	public ModelAndView queryFlowForCancel(HttpServletRequest request) throws AppException {
		
		//返回页面路径 
		ModelAndView mav = new ModelAndView("aros/flow/flow_msg");
		//案件ID
		mav.addObject("caseid", request.getParameter("caseid"));
		
		List<JSONObject> list = probaseinfoService.queryProbaseinfosByCaseid(request.getParameter("caseid"),
				GCC.PROBASEINFO_PROTYPE_XZFYCANCEL);
		mav.addObject("wfList", list);
		
		//查询流程节点
		BigDecimal nodeid = probaseinfoService.queryNodeidByCaseid(request.getParameter("caseid"),
				GCC.PROBASEINFO_PROTYPE_XZFYCANCEL);
		mav.addObject("nodeid", nodeid);
				
		return mav;
	}
	
	/**
	 * @Title: queryFlowForDelay
	 * @Description: 行政复议延期：查看流程信息
	 * @author ybb
	 * @date 2016年8月26日 下午2:41:48
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping({"/queryFlowForDelay.do"})
	public ModelAndView queryFlowForDelay(HttpServletRequest request) throws AppException {
		
		//返回页面路径 
		ModelAndView mav = new ModelAndView("aros/flow/flow_msg");
		//案件ID
		mav.addObject("caseid", request.getParameter("caseid"));
		
		List<JSONObject> list = probaseinfoService.queryProbaseinfosByCaseid(request.getParameter("caseid"),
				GCC.PROBASEINFO_PROTYPE_XZFYDELAY);
		mav.addObject("wfList", list);
		
		//查询流程节点
		BigDecimal nodeid = probaseinfoService.queryNodeidByCaseid(request.getParameter("caseid"),
				GCC.PROBASEINFO_PROTYPE_XZFYDELAY);
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	/**
	 * @Title: queryFlowForAvoid
	 * @Description: 行政复议回避：查看流程信息
	 * @author ybb
	 * @date 2016年8月26日 下午2:42:38
	 * @param request
	 * @return
	 * @throws AppException
	 */
	/*@RequestMapping({"/queryFlowForAvoid.do"})
	public ModelAndView queryFlowForAvoid(HttpServletRequest request) throws AppException {
		
		//返回页面路径 
		ModelAndView mav = new ModelAndView("aros/flow/flow_msg");
		//案件ID
		mav.addObject("caseid", request.getParameter("caseid"));
		
		List<JSONObject> list = probaseinfoService.queryProbaseinfosByCaseid(request.getParameter("caseid"),
				GCC.PROBASEINFO_PROTYPE_XZFYAVOID);
		mav.addObject("wfList", list);
		
		//查询流程节点
		BigDecimal nodeid = probaseinfoService.queryNodeidByCaseid(request.getParameter("caseid"),
				GCC.PROBASEINFO_PROTYPE_XZFYAVOID);
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}*/
	
	/**
	 * @Title: queryFlowForRcasebaseinfo
	 * @Description: 被复议案件管理：查看流程信息
	 * @author ybb
	 * @date 2016年9月21日 下午1:23:29
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping({"/queryFlowForRcasebaseinfo.do"})
	public ModelAndView queryFlowForRcasebaseinfo(HttpServletRequest request) throws AppException {
		
		//返回页面路径 
		ModelAndView mav = new ModelAndView("aros/flow/rcasebaseinfo_flow");
		//案件ID
		mav.addObject("rcaseid", request.getParameter("rcaseid"));
		
		List<JSONObject> list = probaseinfoService.queryFlowForRcasebaseinfo(request.getParameter("rcaseid"),
				GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO);
		mav.addObject("wfList", list);
		
		//查询流程节点
		BigDecimal nodeid = probaseinfoService.queryNodeidByRcaseid(request.getParameter("rcaseid"));
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	/**
	 * @Title: queryFlowForAdmlitbaseinfo
	 * @Description: 应诉案件基本信息：查看流程信息
	 * @author ybb
	 * @date 2016年9月22日 下午5:39:53
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping({"/queryFlowForAdmlitbaseinfo.do"})
	public ModelAndView queryFlowForAdmlitbaseinfo(HttpServletRequest request) throws AppException {
		
		//返回页面路径 
		ModelAndView mav = new ModelAndView("aros/flow/admlitbaseinfo_flow");
		//案件ID
		mav.addObject("acaseid", request.getParameter("acaseid"));
		
		List<JSONObject> list = probaseinfoService.queryFlowForAdmlitbaseinfo(request.getParameter("acaseid"),
				GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO);
		mav.addObject("wfList", list);
		
		//查询流程节点
		BigDecimal nodeid = probaseinfoService.queryNodeidByAcaseid(request.getParameter("acaseid"));
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
}
