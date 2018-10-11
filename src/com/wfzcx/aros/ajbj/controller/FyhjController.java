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
import com.wfzcx.aros.ajbj.service.FyhjService;
import com.wfzcx.aros.flow.po.Probaseinfo;
import com.wfzcx.aros.flow.service.ProbaseinfoService;
import com.wfzcx.aros.util.GCC;

/**
 * @ClassName: FyhjController
 * @Description: 复议和解逻辑控制类
 * @author ybb
 * @date 2017年3月23日 下午4:12:27
 * @version V1.0
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/ajbg/fyhj/FyhjController")
public class FyhjController {

	@Autowired
	private FyhjService fyhjService;
	@Autowired
	private ProbaseinfoService probaseinfoService;
	
	/**
	 * @Title: ajzzInit
	 * @Description: 复议和解发起-初始化行政复议和解页面
	 * @author ybb
	 * @date 2017年3月23日16:13:28
	 * @param request
	 * @return
	 */
	@RequestMapping({"/fyhjInit.do"})
	public ModelAndView fyhjInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ajbg/fyhj/fyhj_init");
		String menuid = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		String key = request.getParameter("key");
		mav.addObject("key", key);
		
		//判断是否是第一节点，判断grid页面按钮显示
		int flag = fyhjService.queryPronodebaseinfoByUserid();
		mav.addObject("flag", flag);
		
		return mav;
	}
	
	/**
	 * @Title: queryFyhjList
	 * @Description: 复议和解发起-查询案件和解列表
	 * @author ybb
	 * @date 2017年3月23日16:13:32
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryFyhjList.do")
	@ResponseBody
	public EasyUITotalResult queryFyhjList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = fyhjService.queryFyhjList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: fyhjAddInit
	 * @Description:  复议和解发起-初始化行政复议和解发起页面
	 * @author ybb
	 * @date 2017年3月23日16:13:35
	 * @param request
	 * @return
	 */
	@RequestMapping({"/fyhjAddInit.do"})
	public ModelAndView fyhjAddInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ajbg/fyhj/fyhjadd_form");
		//0不可用，1可用
		String showFile = "0";
		String ccrid = request.getParameter("ccrid");
		String nodeid = request.getParameter("nodeid");
		
		Casechangereq casechangereq = null;
		
		if (StringUtil.isNotBlank(ccrid)) {
			casechangereq = fyhjService.queryCasechangereqByCcrid(ccrid);
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
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCOMPROMISE,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: fyhjAddSave
	 * @Description: 复议和解发起-保存行政复议和解申请
	 * @author ybb
	 * @date 2017年3月23日16:13:55
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fyhjAddSave.do")
	@ResponseBody
	public ResultMsg fyhjAddSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			String ccrid = fyhjService.addFyhj(param);
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
	 * @Title: fyhjAddFlow
	 * @Description: 复议和解发起-送审
	 * @author ybb
	 * @date 2017年3月23日16:14:00
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/fyhjAddFlow.do")
	@ResponseBody
	public ResultMsg fyhjAddFlow(String ccrid) {
		
		ResultMsg msg = null;

		try {
			fyhjService.fyhjAddFlow(ccrid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: fyhjAuditInit
	 * @Description:复议和解审批-初始化审批页面
	 * @author ybb
	 * @date 2017年3月23日16:14:05
	 * @param request
	 * @return
	 */
	@RequestMapping({"/fyhjAuditInit.do"})
	public ModelAndView fyhjAuditInit(HttpServletRequest request) { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/ajbg/fyhj/fyhjaudit_form");
		
		// 案件变更申请ID
		String ccrid  = request.getParameter("ccrid");
		mav.addObject("ccrid", ccrid);
		
		// 案件变更基本信息
		Casechangereq casechangereq = fyhjService.queryCasechangereqByCcrid(ccrid);
		if(casechangereq != null) {
			mav.addObject("casechangereq", casechangereq);
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCOMPROMISE,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCOMPROMISE, 
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_TWOAUDIT));
		if (section != null && !StringUtils.isBlank(section.getRemark()) ) {
			mav.addObject("sectionRemark", section.getRemark());
		} else {
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCOMPROMISE,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_THREEAUDIT));
		if (organ != null && !StringUtils.isBlank(organ.getRemark()) ) {
			mav.addObject("organRemark", organ.getRemark());
		} else {
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCOMPROMISE,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_FOURAUDIT));
		if (office != null && !StringUtils.isBlank(office.getRemark()) ) {
			mav.addObject("officeRemark", office.getRemark());
		} else {
			mav.addObject("officeRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: fyhjAuditSave
	 * @Description: 复议和解审核——保存审核
	 * @author ybb
	 * @date 2017年3月23日16:14:09
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fyhjAuditSave.do")
	@ResponseBody
	public ResultMsg fyhjAuditSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			fyhjService.updateFyhjAudit(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: fyhjAuditFlow
	 * @Description:  复议和解审核——发送流程
	 * @author ybb
	 * @date 2017年3月23日16:14:13
	 * @param ccrid
	 * @return
	 */
	@RequestMapping("/fyhjAuditFlow.do")
	@ResponseBody
	public ResultMsg fyhjAuditFlow(String ccrid) {
		ResultMsg msg = null;
		try {
			fyhjService.fyhjAuditFlow(ccrid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: fyhjDecideInit
	 * @Description: 复议和解决定-初始化决定页面
	 * @author ybb
	 * @date 2017年3月23日16:14:16
	 * @param request
	 * @return
	 */
	@RequestMapping({"/fyhjDecideInit.do"})
	public ModelAndView fyhjDecideInit(HttpServletRequest request) { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/ajbg/fyhj/fyhjdecide_form");
		
		// 案件变更申请ID
		String ccrid = request.getParameter("ccrid");
		mav.addObject("ccrid", ccrid);
		
		// 案件变更基本信息
		Casechangereq casechangereq = fyhjService.queryCasechangereqByCcrid(ccrid);
		if(casechangereq != null) {
			mav.addObject("casechangereq", casechangereq);
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCOMPROMISE,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCOMPROMISE, 
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_TWOAUDIT));
		if (section != null && !StringUtils.isBlank(section.getRemark()) ) {
			mav.addObject("sectionRemark", section.getRemark());
		} else {
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCOMPROMISE,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_THREEAUDIT));
		if (organ != null && !StringUtils.isBlank(organ.getRemark()) ) {
			mav.addObject("organRemark", organ.getRemark());
		} else {
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCOMPROMISE,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_FOURAUDIT));
		if (office != null && !StringUtils.isBlank(office.getRemark()) ) {
			mav.addObject("officeRemark", office.getRemark());
		} else {
			mav.addObject("officeRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: fyhjDecideSave
	 * @Description: 处理决定-保存处理决定信息
	 * @author ybb
	 * @date 2017年3月23日16:14:23
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fyhjDecideSave.do")
	@ResponseBody
	public ResultMsg fyhjDecideSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			
			fyhjService.updateFyhjDecide(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: fyhjDecideFlow
	 * @Description: 复议和解决定-发送
	 * @author ybb
	 * @date 2017年3月23日16:14:26
	 * @param ccrid
	 * @return
	 */
	@RequestMapping("/fyhjDecideFlow.do")
	@ResponseBody
	public ResultMsg fyhjDecideFlow(String ccrid) {
		ResultMsg msg = null;
		try {
			fyhjService.fyhjDecideFlow(ccrid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: fyhjReturnFlow
	 * @Description: 复议和解-退回
	 * @author ybb
	 * @date 2017年3月23日16:14:29
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fyhjReturnFlow.do")
	@ResponseBody
	public ResultMsg fyhjReturnFlow(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			fyhjService.fyhjReturnFlow(param);
			msg = new ResultMsg(true, "回退成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: caseInit
	 * @Description: 复议和解发起-初始化案件选择页面
	 * @author ybb
	 * @date 2017年3月23日16:14:33
	 * @param request
	 * @return
	 */
	@RequestMapping({"/caseInit.do"})
	public ModelAndView caseInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ajbg/fyhj/case_init");
		String menuid = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryCaseList
	 * @Description: 复议和解发起-查询可选复议案件列表
	 * @author ybb
	 * @date 2017年3月23日16:14:38
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
		PaginationSupport ps = fyhjService.queryCaseList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: fyhjDelete
	 * @Description: 复议和解发起-删除复议和解申请
	 * @author ybb
	 * @date 2017年3月23日16:14:45
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/fyhjDelete.do")
	@ResponseBody
	public ResultMsg fyhjDelete(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			fyhjService.deleteFyhjByCcrid(param);
			msg = new ResultMsg(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: fyhjViewInit
	 * @Description: 初始化复议和解查看页面
	 * @author ybb
	 * @date 2017年3月28日 下午1:39:59
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping({"/fyhjViewInit.do"})
	public ModelAndView fyhjViewInit(HttpServletRequest request) throws AppException { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/ajbg/fyhj/fyhj_view");
		
		// 案件变更申请ID
		String ccrid = request.getParameter("ccrid");
		mav.addObject("ccrid", ccrid);
		
		// 案件变更基本信息
		Casechangereq casechangereq = fyhjService.queryFyzzListByCcrid(ccrid);
		if(casechangereq != null) {
			mav.addObject("casechangereq", casechangereq);
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCOMPROMISE,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));
		if(agent != null && !StringUtils.isBlank(agent.getRemark()) ){
			mav.addObject("agentRemark", agent.getRemark());
		}else{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCOMPROMISE, 
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_TWOAUDIT));
		if (section != null && !StringUtils.isBlank(section.getRemark()) ) {
			mav.addObject("sectionRemark", section.getRemark());
		} else {
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCOMPROMISE,
				new BigDecimal(GCC.CASECHANGEREQ_NODEID_THREEAUDIT));
		if (organ != null && !StringUtils.isBlank(organ.getRemark()) ) {
			mav.addObject("organRemark", organ.getRemark());
		} else {
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByProcessid(ccrid, GCC.PROBASEINFO_PROTYPE_XZFYCOMPROMISE,
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
