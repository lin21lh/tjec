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
import com.wfzcx.aros.ajbj.service.FyhfService;
import com.wfzcx.aros.flow.po.Probaseinfo;
import com.wfzcx.aros.flow.service.ProbaseinfoService;
import com.wfzcx.aros.util.GCC;

/**
 * @ClassName: FyhfController
 * @Description: 复议恢复逻辑控制类
 * @author ybb
 * @date 2017年3月23日 下午4:09:59
 * @version V1.0
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/ajbg/fyhf/FyhfController")
public class FyhfController {

	@Autowired
	private FyhfService fyhfService;
	@Autowired
	private ProbaseinfoService probaseinfoService;
	
	/**
	 * @Title: fyhfInit
	 * @Description: 复议恢复发起-初始化行政复议恢复页面
	 * @author ybb
	 * @date 2017年3月23日16:11:16
	 * @param request
	 * @return
	 */
	@RequestMapping({"/fyhfInit.do"})
	public ModelAndView fyhfInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ajbg/fyhf/fyhf_init");
		String menuid = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		String key = request.getParameter("key");
		mav.addObject("key", key);
		
		//判断是否是第一节点，判断grid页面按钮显示
		int flag = fyhfService.queryPronodebaseinfoByUserid();
		mav.addObject("flag", flag);
		
		return mav;
	}
	
	/**
	 * @Title: queryFyhfList
	 * @Description: 复议恢复发起-查询案件恢复列表
	 * @author ybb
	 * @date 2017年3月23日16:11:24
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryFyhfList.do")
	@ResponseBody
	public EasyUITotalResult queryFyhfList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = fyhfService.queryFyhfList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: fyhfAddInit
	 * @Description:  复议恢复发起-初始化行政复议恢复发起页面
	 * @author ybb
	 * @date 2017年3月23日16:11:27
	 * @param request
	 * @return
	 */
	@RequestMapping({"/fyhfAddInit.do"})
	public ModelAndView fyhfAddInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ajbg/fyhf/fyhfadd_form");
		//0不可用，1可用
		String showFile = "0";
		String ccrid = request.getParameter("ccrid");
		String nodeid = request.getParameter("nodeid");
		
		Casechangereq casechangereq = null;
		
		if (StringUtil.isNotBlank(ccrid)) {
			casechangereq = fyhfService.queryCasechangereqByCcrid(ccrid);
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
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYRECOVER,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: fyhfAddSave
	 * @Description: 复议恢复发起-保存行政复议恢复申请
	 * @author ybb
	 * @date 2017年3月23日16:11:31
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fyhfAddSave.do")
	@ResponseBody
	public ResultMsg fyhfAddSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			String ccrid = fyhfService.addFyhf(param);
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
	 * @Title: fyhfAddFlow
	 * @Description: 复议恢复发起-送审
	 * @author ybb
	 * @date 2017年3月23日16:11:36
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/fyhfAddFlow.do")
	@ResponseBody
	public ResultMsg fyhfAddFlow(String ccrid) {
		
		ResultMsg msg = null;

		try {
			fyhfService.fyhfAddFlow(ccrid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: fyhfAuditInit
	 * @Description:复议恢复审批-初始化审批页面
	 * @author ybb
	 * @date 2017年3月23日16:11:39
	 * @param request
	 * @return
	 */
	@RequestMapping({"/fyhfAuditInit.do"})
	public ModelAndView fyhfAuditInit(HttpServletRequest request) { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/ajbg/fyhf/fyhfaudit_form");
		
		// 案件变更申请ID
		String ccrid  = request.getParameter("ccrid");
		mav.addObject("ccrid", ccrid);
		
		// 案件变更基本信息
		Casechangereq casechangereq = fyhfService.queryCasechangereqByCcrid(ccrid);
		if(casechangereq != null) {
			mav.addObject("casechangereq", casechangereq);
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYRECOVER,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYRECOVER, 
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_TWOAUDIT));
		if (section != null && !StringUtils.isBlank(section.getRemark()) ) {
			mav.addObject("sectionRemark", section.getRemark());
		} else {
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYRECOVER,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_THREEAUDIT));
		if (organ != null && !StringUtils.isBlank(organ.getRemark()) ) {
			mav.addObject("organRemark", organ.getRemark());
		} else {
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYRECOVER,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_FOURAUDIT));
		if (office != null && !StringUtils.isBlank(office.getRemark()) ) {
			mav.addObject("officeRemark", office.getRemark());
		} else {
			mav.addObject("officeRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: fyhfAuditSave
	 * @Description: 复议恢复审核——保存审核
	 * @author ybb
	 * @date 2017年3月23日16:11:44
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fyhfAuditSave.do")
	@ResponseBody
	public ResultMsg fyhfAuditSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			fyhfService.updateFyhfAudit(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: fyhfAuditFlow
	 * @Description:  复议恢复审核——发送流程
	 * @author ybb
	 * @date 2017年3月23日16:11:47
	 * @param ccrid
	 * @return
	 */
	@RequestMapping("/fyhfAuditFlow.do")
	@ResponseBody
	public ResultMsg fyhfAuditFlow(String ccrid) {
		ResultMsg msg = null;
		try {
			fyhfService.fyhfAuditFlow(ccrid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: fyhfDecideInit
	 * @Description: 复议恢复决定-初始化决定页面
	 * @author ybb
	 * @date 2017年3月23日16:11:51
	 * @param request
	 * @return
	 */
	@RequestMapping({"/fyhfDecideInit.do"})
	public ModelAndView fyhfDecideInit(HttpServletRequest request) { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/ajbg/fyhf/fyhfdecide_form");
		
		// 案件变更申请ID
		String ccrid = request.getParameter("ccrid");
		mav.addObject("ccrid", ccrid);
		
		// 案件变更基本信息
		Casechangereq casechangereq = fyhfService.queryCasechangereqByCcrid(ccrid);
		if(casechangereq != null) {
			mav.addObject("casechangereq", casechangereq);
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYRECOVER,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYRECOVER, 
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_TWOAUDIT));
		if (section != null && !StringUtils.isBlank(section.getRemark()) ) {
			mav.addObject("sectionRemark", section.getRemark());
		} else {
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYRECOVER,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_THREEAUDIT));
		if (organ != null && !StringUtils.isBlank(organ.getRemark()) ) {
			mav.addObject("organRemark", organ.getRemark());
		} else {
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYRECOVER,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_FOURAUDIT));
		if (office != null && !StringUtils.isBlank(office.getRemark()) ) {
			mav.addObject("officeRemark", office.getRemark());
		} else {
			mav.addObject("officeRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: fyhfDecideSave
	 * @Description: 处理决定-保存处理决定信息
	 * @author ybb
	 * @date 2017年3月23日16:11:55
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fyhfDecideSave.do")
	@ResponseBody
	public ResultMsg fyhfDecideSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			
			fyhfService.updateFyhfDecide(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: fyhfDecideFlow
	 * @Description: 复议恢复决定-发送
	 * @author ybb
	 * @date 2017年3月23日16:11:59
	 * @param ccrid
	 * @return
	 */
	@RequestMapping("/fyhfDecideFlow.do")
	@ResponseBody
	public ResultMsg fyhfDecideFlow(String ccrid) {
		ResultMsg msg = null;
		try {
			fyhfService.fyhfDecideFlow(ccrid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: fyhfReturnFlow
	 * @Description: 复议恢复-退回
	 * @author ybb
	 * @date 2017年3月23日16:12:02
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fyhfReturnFlow.do")
	@ResponseBody
	public ResultMsg fyhfReturnFlow(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			fyhfService.fyhfReturnFlow(param);
			msg = new ResultMsg(true, "回退成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: caseInit
	 * @Description: 复议恢复发起-初始化案件选择页面
	 * @author ybb
	 * @date 2017年3月23日16:12:06
	 * @param request
	 * @return
	 */
	@RequestMapping({"/caseInit.do"})
	public ModelAndView caseInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ajbg/fyhf/case_init");
		String menuid = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryCaseList
	 * @Description: 复议恢复发起-查询可选中止案件列表
	 * @author ybb
	 * @date 2017年3月23日16:12:09
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
		PaginationSupport ps = fyhfService.queryCaseList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: fyhfDelete
	 * @Description: 复议恢复发起-删除复议恢复申请
	 * @author ybb
	 * @date 2017年3月23日16:12:12
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fyhfDelete.do")
	@ResponseBody
	public ResultMsg fyhfDelete(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			fyhfService.deleteFyhfByCcrid(param);
			msg = new ResultMsg(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: fyhfViewInit
	 * @Description: 返回复议恢复查看页面
	 * @author ybb
	 * @date 2017年3月28日 下午1:40:57
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping({"/fyhfViewInit.do"})
	public ModelAndView fyhfViewInit(HttpServletRequest request) throws AppException { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/ajbg/fyhf/fyhf_view");
		
		// 案件变更申请ID
		String ccrid = request.getParameter("ccrid");
		mav.addObject("ccrid", ccrid);
		
		// 案件变更基本信息
		Casechangereq casechangereq = fyhfService.queryFyzzListByCcrid(ccrid);
		if(casechangereq != null) {
			mav.addObject("casechangereq", casechangereq);
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYRECOVER,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYRECOVER, 
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_TWOAUDIT));
		if (section != null && !StringUtils.isBlank(section.getRemark()) ) {
			mav.addObject("sectionRemark", section.getRemark());
		} else {
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYRECOVER,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_THREEAUDIT));
		if (organ != null && !StringUtils.isBlank(organ.getRemark()) ) {
			mav.addObject("organRemark", organ.getRemark());
		} else {
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYRECOVER,
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
