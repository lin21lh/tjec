package com.wfzcx.aros.ajbj.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.aros.ajbj.po.Casechangereq;
import com.wfzcx.aros.ajbj.service.FycxService;
import com.wfzcx.aros.flow.po.Probaseinfo;
import com.wfzcx.aros.flow.service.ProbaseinfoService;
import com.wfzcx.aros.util.GCC;

/**
 * @ClassName: FycxController
 * @Description: 复议程序逻辑控制类
 * @author ybb
 * @date 2017年3月23日 下午4:06:36
 * @version V1.0
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/ajbg/fycx/FycxController")
public class FycxController {

	@Autowired
	private FycxService fycxService;
	@Autowired
	private ProbaseinfoService probaseinfoService;
	
	/**
	 * @Title: ajzzInit
	 * @Description: 复议程序发起-初始化行政复议程序页面
	 * @author ybb
	 * @date 2017年3月23日16:07:52
	 * @param request
	 * @return
	 */
	@RequestMapping({"/fycxInit.do"})
	public ModelAndView fycxInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ajbg/fycx/fycx_init");
		String menuid = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		String key = request.getParameter("key");
		mav.addObject("key", key);
		
		//判断是否是第一节点，判断grid页面按钮显示
		int flag = fycxService.queryPronodebaseinfoByUserid();
		mav.addObject("flag", flag);
		
		return mav;
	}
	
	/**
	 * @Title: queryFycxList
	 * @Description: 复议程序发起-查询案件程序列表
	 * @author ybb
	 * @date 2017年3月23日16:07:56
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryFycxList.do")
	@ResponseBody
	public EasyUITotalResult queryFycxList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = fycxService.queryFycxList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: fycxAddInit
	 * @Description:  复议程序发起-初始化行政复议程序发起页面
	 * @author ybb
	 * @date 2017年3月23日16:08:00
	 * @param request
	 * @return
	 */
	@RequestMapping({"/fycxAddInit.do"})
	public ModelAndView fycxAddInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ajbg/fycx/fycxadd_form");
		//0不可用，1可用
		String showFile = "0";
		String ccrid = request.getParameter("ccrid");
		String nodeid = request.getParameter("nodeid");
		
		Casechangereq casechangereq = null;
		
		if (StringUtil.isNotBlank(ccrid)) {
			casechangereq = fycxService.queryCasechangereqByCcrid(ccrid);
			showFile = "1";
		}
		if (null != casechangereq) {
			mav.addObject("casechangereq", casechangereq);
		} else {
			mav.addObject("casechangereq", "");
		}
		mav.addObject("showFile", showFile);
		mav.addObject("nodeid", nodeid);
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYPROGRAM,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: fycxAddSave
	 * @Description: 复议程序发起-保存行政复议程序申请
	 * @author ybb
	 * @date 2017年3月23日16:08:27
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fycxAddSave.do")
	@ResponseBody
	public ResultMsg fycxAddSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			String ccrid = fycxService.addFycx(param);
			if (!StringUtils.isBlank(ccrid)) {
				HashMap<String, Object> body = new HashMap<String, Object>();
				body.put("ccrid", ccrid);
				msg = new ResultMsg(true, "保存成功", body);
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
	 * @Title: fycxAddFlow
	 * @Description: 复议程序发起-送审
	 * @author ybb
	 * @date 2017年3月23日16:08:30
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/fycxAddFlow.do")
	@ResponseBody
	public ResultMsg fycxAddFlow(String ccrid) {
		
		ResultMsg msg = null;

		try {
			fycxService.fycxAddFlow(ccrid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: fycxAuditInit
	 * @Description:复议程序审批-初始化审批页面
	 * @author ybb
	 * @date 2017年3月23日16:08:34
	 * @param request
	 * @return
	 */
	@RequestMapping({"/fycxAuditInit.do"})
	public ModelAndView fycxAuditInit(HttpServletRequest request) { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/ajbg/fycx/fycxaudit_form");
		
		// 案件变更申请ID
		String ccrid  = request.getParameter("ccrid");
		mav.addObject("ccrid", ccrid);
		
		// 案件变更基本信息
		Casechangereq casechangereq = fycxService.queryCasechangereqByCcrid(ccrid);
		if(casechangereq != null) {
			mav.addObject("casechangereq", casechangereq);
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYPROGRAM,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYPROGRAM, 
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_TWOAUDIT));
		if (section != null && !StringUtils.isBlank(section.getRemark()) ) {
			mav.addObject("sectionRemark", section.getRemark());
		} else {
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYPROGRAM,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_THREEAUDIT));
		if (organ != null && !StringUtils.isBlank(organ.getRemark()) ) {
			mav.addObject("organRemark", organ.getRemark());
		} else {
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYPROGRAM,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_FOURAUDIT));
		if (office != null && !StringUtils.isBlank(office.getRemark()) ) {
			mav.addObject("officeRemark", office.getRemark());
		} else {
			mav.addObject("officeRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: fycxAuditSave
	 * @Description: 复议程序审核——保存审核
	 * @author ybb
	 * @date 2017年3月23日16:08:38
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fycxAuditSave.do")
	@ResponseBody
	public ResultMsg fycxAuditSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			fycxService.updateFycxAudit(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: fycxAuditFlow
	 * @Description:  复议程序审核——发送流程
	 * @author ybb
	 * @date 2017年3月23日16:08:41
	 * @param ccrid
	 * @return
	 */
	@RequestMapping("/fycxAuditFlow.do")
	@ResponseBody
	public ResultMsg fycxAuditFlow(String ccrid) {
		ResultMsg msg = null;
		try {
			fycxService.fycxAuditFlow(ccrid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: fycxDecideInit
	 * @Description: 复议程序决定-初始化决定页面
	 * @author ybb
	 * @date 2017年3月23日16:08:44
	 * @param request
	 * @return
	 */
	@RequestMapping({"/fycxDecideInit.do"})
	public ModelAndView fycxDecideInit(HttpServletRequest request) { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/ajbg/fycx/fycxdecide_form");
		
		// 案件变更申请ID
		String ccrid = request.getParameter("ccrid");
		mav.addObject("ccrid", ccrid);
		
		// 案件变更基本信息
		Casechangereq casechangereq = fycxService.queryCasechangereqByCcrid(ccrid);
		if(casechangereq != null) {
			mav.addObject("casechangereq", casechangereq);
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYPROGRAM,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYPROGRAM, 
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_TWOAUDIT));
		if (section != null && !StringUtils.isBlank(section.getRemark()) ) {
			mav.addObject("sectionRemark", section.getRemark());
		} else {
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYPROGRAM,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_THREEAUDIT));
		if (organ != null && !StringUtils.isBlank(organ.getRemark()) ) {
			mav.addObject("organRemark", organ.getRemark());
		} else {
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYPROGRAM,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_FOURAUDIT));
		if (office != null && !StringUtils.isBlank(office.getRemark()) ) {
			mav.addObject("officeRemark", office.getRemark());
		} else {
			mav.addObject("officeRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: fycxDecideSave
	 * @Description: 处理决定-保存处理决定信息
	 * @author ybb
	 * @date 2017年3月23日16:08:59
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fycxDecideSave.do")
	@ResponseBody
	public ResultMsg fycxDecideSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			
			fycxService.updateFycxDecide(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: fycxDecideFlow
	 * @Description: 复议程序决定-发送
	 * @author ybb
	 * @date 2017年3月23日16:09:03
	 * @param ccrid
	 * @return
	 */
	@RequestMapping("/fycxDecideFlow.do")
	@ResponseBody
	public ResultMsg fycxDecideFlow(String ccrid) {
		ResultMsg msg = null;
		try {
			fycxService.fycxDecideFlow(ccrid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: fycxReturnFlow
	 * @Description: 复议程序-退回
	 * @author ybb
	 * @date 2017年3月23日16:09:06
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fycxReturnFlow.do")
	@ResponseBody
	public ResultMsg fycxReturnFlow(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			fycxService.fycxReturnFlow(param);
			msg = new ResultMsg(true, "回退成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: caseInit
	 * @Description: 复议程序发起-初始化案件选择页面
	 * @author ybb
	 * @date 2017年3月23日16:09:09
	 * @param request
	 * @return
	 */
	@RequestMapping({"/caseInit.do"})
	public ModelAndView caseInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ajbg/fycx/case_init");
		String menuid = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryCaseList
	 * @Description: 复议程序发起-查询可选复议案件列表
	 * @author ybb
	 * @date 2017年3月23日16:09:15
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryCaseList.do")
	@ResponseBody
	public EasyUITotalResult queryCaseList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = fycxService.queryCaseList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: fycxDelete
	 * @Description: 复议程序发起-删除复议程序申请
	 * @author ybb
	 * @date 2017年3月23日16:09:18
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fycxDelete.do")
	@ResponseBody
	public ResultMsg fycxDelete(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			fycxService.deleteFycxByCcrid(param);
			msg = new ResultMsg(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: fycxViewInit
	 * @Description: 初始化复议程序查看页面
	 * @author ybb
	 * @date 2017年3月28日 下午1:42:07
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping({"/fycxViewInit.do"})
	public ModelAndView fycxViewInit(HttpServletRequest request) throws AppException { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/ajbg/fycx/fycx_view");
		
		// 案件变更申请ID
		String ccrid = request.getParameter("ccrid");
		mav.addObject("ccrid", ccrid);
		
		// 案件变更基本信息
		Casechangereq casechangereq = fycxService.queryFyzzListByCcrid(ccrid);
		if(casechangereq != null) {
			mav.addObject("casechangereq", casechangereq);
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYPROGRAM,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYPROGRAM, 
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_TWOAUDIT));
		if (section != null && !StringUtils.isBlank(section.getRemark()) ) {
			mav.addObject("sectionRemark", section.getRemark());
		} else {
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYPROGRAM,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_THREEAUDIT));
		if (organ != null && !StringUtils.isBlank(organ.getRemark()) ) {
			mav.addObject("organRemark", organ.getRemark());
		} else {
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYPROGRAM,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_FOURAUDIT));
		if (office != null && !StringUtils.isBlank(office.getRemark()) ) {
			mav.addObject("officeRemark", office.getRemark());
		} else {
			mav.addObject("officeRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: backFlowPage
	 * @Description: 返回退回页面
	 * @author ybb
	 * @date 2017年3月29日 下午2:39:26
	 * @param request
	 * @return
	 */
	@RequestMapping({"/backFlowPage.do"})
	public ModelAndView backFlowPage(HttpServletRequest request){
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/backFlowPage");
		
		return mav;
	}
}
