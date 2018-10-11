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
import com.wfzcx.aros.ajbj.service.FychService;
import com.wfzcx.aros.flow.po.Probaseinfo;
import com.wfzcx.aros.flow.service.ProbaseinfoService;
import com.wfzcx.aros.util.GCC;

/**
 * @ClassName: FychController
 * @Description: 复议撤回逻辑控制类
 * @author ybb
 * @date 2017年3月23日 下午4:02:55
 * @version V1.0
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/ajbg/fych/FychController")
public class FychController {

	@Autowired
	private FychService fychService;
	@Autowired
	private ProbaseinfoService probaseinfoService;
	
	/**
	 * @Title: fychInit
	 * @Description: 复议撤回发起-初始化行政复议撤回页面
	 * @author ybb
	 * @date 2017年3月23日16:04:35
	 * @param request
	 * @return
	 */
	@RequestMapping({"/fychInit.do"})
	public ModelAndView fychInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ajbg/fych/fych_init");
		String menuid = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		String key = request.getParameter("key");
		mav.addObject("key", key);
		
		//判断是否是第一节点，判断grid页面按钮显示
		int flag = fychService.queryPronodebaseinfoByUserid();
		mav.addObject("flag", flag);
		
		return mav;
	}
	
	/**
	 * @Title: queryFychList
	 * @Description: 复议撤回发起-查询案件撤回列表
	 * @author ybb
	 * @date 2017年3月23日16:04:44
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryFychList.do")
	@ResponseBody
	public EasyUITotalResult queryFychList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = fychService.queryFychList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: fychAddInit
	 * @Description:  复议撤回发起-初始化行政复议撤回发起页面
	 * @author ybb
	 * @date 2017年3月23日16:04:47
	 * @param request
	 * @return
	 */
	@RequestMapping({"/fychAddInit.do"})
	public ModelAndView fychAddInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ajbg/fych/fychadd_form");
		//0不可用，1可用
		String showFile = "0";
		String ccrid = request.getParameter("ccrid");
		String nodeid = request.getParameter("nodeid");
		
		Casechangereq casechangereq = null;
		
		if (StringUtil.isNotBlank(ccrid)) {
			casechangereq = fychService.queryCasechangereqByCcrid(ccrid);
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
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCANCEL,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: fychAddSave
	 * @Description: 复议撤回发起-保存行政复议撤回申请
	 * @author ybb
	 * @date 2017年3月23日16:05:26
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fychAddSave.do")
	@ResponseBody
	public ResultMsg fychAddSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			String ccrid = fychService.addFych(param);
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
	 * @Title: fychAddFlow
	 * @Description: 复议撤回发起-送审
	 * @author ybb
	 * @date 2017年3月23日16:05:32
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/fychAddFlow.do")
	@ResponseBody
	public ResultMsg fychAddFlow(String ccrid) {
		
		ResultMsg msg = null;

		try {
			fychService.fychAddFlow(ccrid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: fychAuditInit
	 * @Description:复议撤回审批-初始化审批页面
	 * @author ybb
	 * @date 2017年3月23日16:05:36
	 * @param request
	 * @return
	 */
	@RequestMapping({"/fychAuditInit.do"})
	public ModelAndView fychAuditInit(HttpServletRequest request) { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/ajbg/fych/fychaudit_form");
		
		// 案件变更申请ID
		String ccrid  = request.getParameter("ccrid");
		mav.addObject("ccrid", ccrid);
		
		// 案件变更基本信息
		Casechangereq casechangereq = fychService.queryCasechangereqByCcrid(ccrid);
		if(casechangereq != null) {
			mav.addObject("casechangereq", casechangereq);
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCANCEL,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCANCEL, 
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_TWOAUDIT));
		if (section != null && !StringUtils.isBlank(section.getRemark()) ) {
			mav.addObject("sectionRemark", section.getRemark());
		} else {
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCANCEL,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_THREEAUDIT));
		if (organ != null && !StringUtils.isBlank(organ.getRemark()) ) {
			mav.addObject("organRemark", organ.getRemark());
		} else {
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCANCEL,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_FOURAUDIT));
		if (office != null && !StringUtils.isBlank(office.getRemark()) ) {
			mav.addObject("officeRemark", office.getRemark());
		} else {
			mav.addObject("officeRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: fychAuditSave
	 * @Description: 复议撤回审核——保存审核
	 * @author ybb
	 * @date 2017年3月23日16:05:44
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fychAuditSave.do")
	@ResponseBody
	public ResultMsg fychAuditSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			fychService.updateFychAudit(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: fychAuditFlow
	 * @Description:  复议撤回审核——发送流程
	 * @author ybb
	 * @date 2017年3月23日16:05:47
	 * @param ccrid
	 * @return
	 */
	@RequestMapping("/fychAuditFlow.do")
	@ResponseBody
	public ResultMsg fychAuditFlow(String ccrid) {
		ResultMsg msg = null;
		try {
			fychService.fychAuditFlow(ccrid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: fychDecideInit
	 * @Description: 复议撤回决定-初始化决定页面
	 * @author ybb
	 * @date 2017年3月23日16:05:51
	 * @param request
	 * @return
	 */
	@RequestMapping({"/fychDecideInit.do"})
	public ModelAndView fychDecideInit(HttpServletRequest request) { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/ajbg/fych/fychdecide_form");
		
		// 案件变更申请ID
		String ccrid = request.getParameter("ccrid");
		mav.addObject("ccrid", ccrid);
		
		// 案件变更基本信息
		Casechangereq casechangereq = fychService.queryCasechangereqByCcrid(ccrid);
		if(casechangereq != null) {
			mav.addObject("casechangereq", casechangereq);
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCANCEL,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCANCEL, 
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_TWOAUDIT));
		if (section != null && !StringUtils.isBlank(section.getRemark()) ) {
			mav.addObject("sectionRemark", section.getRemark());
		} else {
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCANCEL,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_THREEAUDIT));
		if (organ != null && !StringUtils.isBlank(organ.getRemark()) ) {
			mav.addObject("organRemark", organ.getRemark());
		} else {
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCANCEL,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_FOURAUDIT));
		if (office != null && !StringUtils.isBlank(office.getRemark()) ) {
			mav.addObject("officeRemark", office.getRemark());
		} else {
			mav.addObject("officeRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: fychDecideSave
	 * @Description: 处理决定-保存处理决定信息
	 * @author ybb
	 * @date 2017年3月23日16:05:56
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fychDecideSave.do")
	@ResponseBody
	public ResultMsg fychDecideSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			
			fychService.updateFychDecide(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: fychDecideFlow
	 * @Description: 复议撤回决定-发送
	 * @author ybb
	 * @date 2017年3月23日16:06:00
	 * @param ccrid
	 * @return
	 */
	@RequestMapping("/fychDecideFlow.do")
	@ResponseBody
	public ResultMsg fychDecideFlow(String ccrid) {
		ResultMsg msg = null;
		try {
			fychService.fychDecideFlow(ccrid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: fychReturnFlow
	 * @Description: 复议撤回-退回
	 * @author ybb
	 * @date 2017年3月23日16:06:03
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fychReturnFlow.do")
	@ResponseBody
	public ResultMsg fychReturnFlow(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			fychService.fychReturnFlow(param);
			msg = new ResultMsg(true, "回退成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: caseInit
	 * @Description: 复议撤回发起-初始化案件选择页面
	 * @author ybb
	 * @date 2017年3月23日16:06:06
	 * @param request
	 * @return
	 */
	@RequestMapping({"/caseInit.do"})
	public ModelAndView caseInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ajbg/fych/case_init");
		String menuid = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryCaseList
	 * @Description: 复议撤回发起-查询可选复议案件列表
	 * @author ybb
	 * @date 2017年3月23日16:06:10
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
		PaginationSupport ps = fychService.queryCaseList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: fychDelete
	 * @Description: 复议撤回发起-删除复议撤回申请
	 * @author ybb
	 * @date 2017年3月23日16:06:13
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fychDelete.do")
	@ResponseBody
	public ResultMsg fychDelete(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			fychService.deleteFychByCcrid(param);
			msg = new ResultMsg(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: fychViewInit
	 * @Description: 初始化复议撤回查看页面
	 * @author ybb
	 * @date 2017年3月28日 下午1:43:05
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping({"/fychViewInit.do"})
	public ModelAndView fychViewInit(HttpServletRequest request) throws AppException { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/ajbg/fych/fych_view");
		
		// 案件变更申请ID
		String ccrid = request.getParameter("ccrid");
		mav.addObject("ccrid", ccrid);
		
		// 案件变更基本信息
		Casechangereq casechangereq = fychService.queryFyzzListByCcrid(ccrid);
		if(casechangereq != null) {
			mav.addObject("casechangereq", casechangereq);
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCANCEL,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCANCEL, 
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_TWOAUDIT));
		if (section != null && !StringUtils.isBlank(section.getRemark()) ) {
			mav.addObject("sectionRemark", section.getRemark());
		} else {
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCANCEL,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_THREEAUDIT));
		if (organ != null && !StringUtils.isBlank(organ.getRemark()) ) {
			mav.addObject("organRemark", organ.getRemark());
		} else {
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCANCEL,
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
