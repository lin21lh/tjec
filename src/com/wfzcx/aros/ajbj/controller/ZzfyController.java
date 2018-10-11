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
import com.wfzcx.aros.ajbj.service.ZzfyService;
import com.wfzcx.aros.flow.po.Probaseinfo;
import com.wfzcx.aros.flow.service.ProbaseinfoService;
import com.wfzcx.aros.util.GCC;

/**
 * @ClassName: ZzfyController
 * @Description: 复议终止逻辑控制类
 * @author ybb
 * @date 2017年3月23日 下午4:23:03
 * @version V1.0
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/ajbg/zzfy/ZzfyController")
public class ZzfyController {

	@Autowired
	private ZzfyService zzfyService;
	@Autowired
	private ProbaseinfoService probaseinfoService;
	
	/**
	 * @Title: zzfyInit
	 * @Description: 复议终止发起-初始化行政复议终止页面
	 * @author ybb
	 * @date 2017年3月23日16:25:23
	 * @param request
	 * @return
	 */
	@RequestMapping({"/zzfyInit.do"})
	public ModelAndView zzfyInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ajbg/zzfy/zzfy_init");
		String menuid = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		String key = request.getParameter("key");
		mav.addObject("key", key);
		
		
		//判断是否是第一节点，判断grid页面按钮显示
		int flag = zzfyService.queryPronodebaseinfoByUserid();
		mav.addObject("flag", flag);
		
		return mav;
	}
	
	/**
	 * @Title: queryZzfyList
	 * @Description: 复议终止发起-查询案件终止列表
	 * @author ybb
	 * @date 2017年3月23日16:25:27
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryZzfyList.do")
	@ResponseBody
	public EasyUITotalResult queryZzfyList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = zzfyService.queryZzfyList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: zzfyAddInit
	 * @Description:  复议终止发起-初始化行政复议终止发起页面
	 * @author ybb
	 * @date 2017年3月23日16:25:31
	 * @param request
	 * @return
	 */
	@RequestMapping({"/zzfyAddInit.do"})
	public ModelAndView zzfyAddInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ajbg/zzfy/zzfyadd_form");
		//0不可用，1可用
		String showFile = "0";
		String ccrid = request.getParameter("ccrid");
		String nodeid = request.getParameter("nodeid");
		
		Casechangereq casechangereq = null;
		
		if (StringUtil.isNotBlank(ccrid)) {
			casechangereq = zzfyService.queryCasechangereqByCcrid(ccrid);
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
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYEND,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: zzfyAddSave
	 * @Description: 复议终止发起-保存行政复议终止申请
	 * @author ybb
	 * @date 2017年3月23日16:25:50
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/zzfyAddSave.do")
	@ResponseBody
	public ResultMsg zzfyAddSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			String ccrid = zzfyService.addZzfy(param);
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
	 * @Title: zzfyAddFlow
	 * @Description: 复议终止发起-送审
	 * @author ybb
	 * @date 2017年3月23日16:25:53
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/zzfyAddFlow.do")
	@ResponseBody
	public ResultMsg zzfyAddFlow(String ccrid) {
		
		ResultMsg msg = null;

		try {
			zzfyService.zzfyAddFlow(ccrid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: zzfyAuditInit
	 * @Description:复议终止审批-初始化审批页面
	 * @author ybb
	 * @date 2017年3月23日16:25:56
	 * @param request
	 * @return
	 */
	@RequestMapping({"/zzfyAuditInit.do"})
	public ModelAndView zzfyAuditInit(HttpServletRequest request) { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/ajbg/zzfy/zzfyaudit_form");
		
		// 案件变更申请ID
		String ccrid  = request.getParameter("ccrid");
		mav.addObject("ccrid", ccrid);
		
		// 案件变更基本信息
		Casechangereq casechangereq = zzfyService.queryCasechangereqByCcrid(ccrid);
		if(casechangereq != null) {
			mav.addObject("casechangereq", casechangereq);
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYEND,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYEND, 
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_TWOAUDIT));
		if (section != null && !StringUtils.isBlank(section.getRemark()) ) {
			mav.addObject("sectionRemark", section.getRemark());
		} else {
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYEND,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_THREEAUDIT));
		if (organ != null && !StringUtils.isBlank(organ.getRemark()) ) {
			mav.addObject("organRemark", organ.getRemark());
		} else {
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYEND,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_FOURAUDIT));
		if (office != null && !StringUtils.isBlank(office.getRemark()) ) {
			mav.addObject("officeRemark", office.getRemark());
		} else {
			mav.addObject("officeRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: zzfyAuditSave
	 * @Description: 复议终止审核——保存审核
	 * @author ybb
	 * @date 2017年3月23日16:26:00
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/zzfyAuditSave.do")
	@ResponseBody
	public ResultMsg zzfyAuditSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			zzfyService.updateZzfyAudit(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: zzfyAuditFlow
	 * @Description:  复议终止审核——发送流程
	 * @author ybb
	 * @date 2017年3月23日16:26:05
	 * @param ccrid
	 * @return
	 */
	@RequestMapping("/zzfyAuditFlow.do")
	@ResponseBody
	public ResultMsg zzfyAuditFlow(String ccrid) {
		ResultMsg msg = null;
		try {
			zzfyService.zzfyAuditFlow(ccrid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: zzfyDecideInit
	 * @Description: 复议终止决定-初始化决定页面
	 * @author ybb
	 * @date 2017年3月23日16:26:10
	 * @param request
	 * @return
	 */
	@RequestMapping({"/zzfyDecideInit.do"})
	public ModelAndView zzfyDecideInit(HttpServletRequest request) { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/ajbg/zzfy/zzfydecide_form");
		
		// 案件变更申请ID
		String ccrid = request.getParameter("ccrid");
		mav.addObject("ccrid", ccrid);
		
		// 案件变更基本信息
		Casechangereq casechangereq = zzfyService.queryCasechangereqByCcrid(ccrid);
		if(casechangereq != null) {
			mav.addObject("casechangereq", casechangereq);
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYEND,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYEND, 
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_TWOAUDIT));
		if (section != null && !StringUtils.isBlank(section.getRemark()) ) {
			mav.addObject("sectionRemark", section.getRemark());
		} else {
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYEND,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_THREEAUDIT));
		if (organ != null && !StringUtils.isBlank(organ.getRemark()) ) {
			mav.addObject("organRemark", organ.getRemark());
		} else {
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYEND,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_FOURAUDIT));
		if (office != null && !StringUtils.isBlank(office.getRemark()) ) {
			mav.addObject("officeRemark", office.getRemark());
		} else {
			mav.addObject("officeRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: zzfyDecideSave
	 * @Description: 处理决定-保存处理决定信息
	 * @author ybb
	 * @date 2017年3月23日16:26:26
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/zzfyDecideSave.do")
	@ResponseBody
	public ResultMsg zzfyDecideSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			
			zzfyService.updateZzfyDecide(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: zzfyDecideFlow
	 * @Description: 复议终止决定-发送
	 * @author ybb
	 * @date 2017年3月23日16:26:29
	 * @param ccrid
	 * @return
	 */
	@RequestMapping("/zzfyDecideFlow.do")
	@ResponseBody
	public ResultMsg zzfyDecideFlow(String ccrid) {
		ResultMsg msg = null;
		try {
			zzfyService.zzfyDecideFlow(ccrid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: zzfyReturnFlow
	 * @Description: 复议终止-退回
	 * @author ybb
	 * @date 2017年3月23日16:26:32
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/zzfyReturnFlow.do")
	@ResponseBody
	public ResultMsg zzfyReturnFlow(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			zzfyService.zzfyReturnFlow(param);
			msg = new ResultMsg(true, "回退成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: caseInit
	 * @Description: 复议终止发起-初始化案件选择页面
	 * @author ybb
	 * @date 2017年3月23日16:26:36
	 * @param request
	 * @return
	 */
	@RequestMapping({"/caseInit.do"})
	public ModelAndView caseInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ajbg/zzfy/case_init");
		String menuid = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryCaseList
	 * @Description: 复议终止发起-查询可选复议案件列表
	 * @author ybb
	 * @date 2017年3月23日16:26:38
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
		PaginationSupport ps = zzfyService.queryCaseList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: zzfyDelete
	 * @Description: 复议终止发起-删除复议终止申请
	 * @author ybb
	 * @date 2017年3月23日16:26:42
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/zzfyDelete.do")
	@ResponseBody
	public ResultMsg zzfyDelete(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			zzfyService.deleteZzfyByCcrid(param);
			msg = new ResultMsg(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: zzfyViewInit
	 * @Description: 初始化复议终止查看页面
	 * @author ybb
	 * @date 2017年3月28日 下午1:34:13
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping({"/zzfyViewInit.do"})
	public ModelAndView zzfyViewInit(HttpServletRequest request) throws AppException { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/ajbg/zzfy/zzfy_view");
		
		// 案件变更申请ID
		String ccrid = request.getParameter("ccrid");
		mav.addObject("ccrid", ccrid);
		
		// 案件变更基本信息
		Casechangereq casechangereq = zzfyService.queryFyzzListByCcrid(ccrid);
		if(casechangereq != null) {
			mav.addObject("casechangereq", casechangereq);
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYEND,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYEND, 
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_TWOAUDIT));
		if (section != null && !StringUtils.isBlank(section.getRemark()) ) {
			mav.addObject("sectionRemark", section.getRemark());
		} else {
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYEND,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_THREEAUDIT));
		if (organ != null && !StringUtils.isBlank(organ.getRemark()) ) {
			mav.addObject("organRemark", organ.getRemark());
		} else {
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYEND,
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
