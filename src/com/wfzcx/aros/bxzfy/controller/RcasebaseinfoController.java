package com.wfzcx.aros.bxzfy.controller;

import java.math.BigDecimal;
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
import com.wfzcx.aros.bxzfy.po.Rcasebaseinfo;
import com.wfzcx.aros.bxzfy.service.RcasebaseinfoService;
import com.wfzcx.aros.flow.po.Probaseinfo;
import com.wfzcx.aros.flow.service.ProbaseinfoService;
import com.wfzcx.aros.util.GCC;

/**
 * @ClassName: RcasebaseinfoController
 * @Description: 用来处理被复议案件相关业务
 * @author ybb
 * @date 2016年9月20日 下午4:27:14
 * @version V1.0
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/bxzfy/controller/RcasebaseinfoController")
public class RcasebaseinfoController {

	@Autowired
	private RcasebaseinfoService rcasebaseinfoService;
	@Autowired
	private ProbaseinfoService probaseinfoService;
	
	/**
	 * @Title: rcasebaseinfoReqInit
	 * @Description: 案件维护-返回被复议案件维护gird列表页面
	 * @author ybb
	 * @date 2016年9月20日 下午5:26:00
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/rcasebaseinfoReqInit.do"})
	public ModelAndView rcasebaseinfoReqInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bxzfy/rcasebaseinforeq_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryRcasebaseinfoReqList
	 * @Description: 案件维护-查询被复议案件维护列表信息
	 * @author ybb
	 * @date 2016年9月20日 下午5:30:33
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryRcasebaseinfoReqList.do")
	@ResponseBody
	public EasyUITotalResult queryRcasebaseinfoReqList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = rcasebaseinfoService.queryRcasebaseinfoReqList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: rcasebaseinfoAddInit
	 * @Description: 案件维护-返回被复议案件维护gird列表页面
	 * @author ybb
	 * @date 2016年9月21日 上午9:07:44
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/rcasebaseinfoAddInit.do"})
	public ModelAndView rcasebaseinfoAddInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bxzfy/rcasebaseinforeq_form");
		//附件ID
		UUID uuid = UUID.randomUUID();
		
		//新增时产生临时的uuid放在附件的keyid中
		mav.addObject("fjkeyid", uuid);
		
		return mav;
	}
	
	/**
	 * @Title: rcasebaseinfoReqSave
	 * @Description: 案件维护-新增
	 * @author ybb
	 * @date 2016年9月21日 上午9:11:46
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/rcasebaseinfoReqSave.do")
	@ResponseBody
	public ResultMsg rcasebaseinfoReqSave(HttpServletRequest request) {

		ResultMsg msg = null;

		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);

		try {
			String caseid = rcasebaseinfoService.addRcasebaseinfoReq(param);
			if (!StringUtils.isBlank(caseid)) {
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
	 * @Title: rcasebaseinfoEditInit
	 * @Description: 案件维护-返回被复议案件修改页面
	 * @author ybb
	 * @date 2016年9月21日 上午9:24:45
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/rcasebaseinfoEditInit.do"})
	public ModelAndView rcasebaseinfoEditInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bxzfy/rcasebaseinforeq_form");
		
		return mav;
	}
	
	/**
	 * @Title: rcasebaseinfoReqEdit
	 * @Description: 案件维护-修改
	 * @author ybb
	 * @date 2016年9月21日 上午9:26:15
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/rcasebaseinfoReqEdit.do")
	@ResponseBody
	public ResultMsg rcasebaseinfoReqEdit(HttpServletRequest request){
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			rcasebaseinfoService.updateRcasebaseinfoReq(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: rcasebaseinfoReqDelete
	 * @Description: 案件维护-删除
	 * @author ybb
	 * @date 2016年9月21日 上午9:47:18
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/rcasebaseinfoReqDelete.do")
	@ResponseBody
	public ResultMsg rcasebaseinfoReqDelete(String rcaseid) {
		
		ResultMsg msg = null;
		
		try {
			
			rcasebaseinfoService.delRcasebaseinfoReq(rcaseid);
			msg = new ResultMsg(true, AppException.getMessage("删除成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败"));
		}
		
		return msg;
	}
	
	/**
	 * @Title: rcasebaseinfoReqDetail
	 * @Description: 案件查看-查看
	 * @author ybb
	 * @date 2016年9月21日 上午9:58:13
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws AppException
	 */
	@RequestMapping({"/rcasebaseinfoReqDetail.do"})
	public ModelAndView rcasebaseinfoReqDetail(HttpServletRequest request) throws ServletException, AppException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bxzfy/rcasebaseinforeq_detail");
		
		//案件ID
		String rcaseid  = request.getParameter("rcaseid");
		mav.addObject("rcaseid", rcaseid);
		
		//根据案件ID查询案件信息
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoService.queryRcasebaseinfoByRcaseid(rcaseid);
		if (StringUtils.isBlank(rcasebaseinfo.getLawyer())) {
			rcasebaseinfo.setLawyer("");
		}
		mav.addObject("rcasebaseinfo", rcasebaseinfo);
		
		//审批意见
		Probaseinfo auditPbi = probaseinfoService.queryProbaseinfoByCaseid(rcaseid,
				GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO, GCC.PROBASEINFO_OPTTYPE_ACCEPTED,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_AUDIT));
		if (auditPbi != null && !StringUtils.isBlank(auditPbi.getResultmsg())) {
			mav.addObject("auditResult", auditPbi.getResultmsg());
			mav.addObject("auditRemark", auditPbi.getRemark());
		} else {
			mav.addObject("auditResult", "");
		}
		
		//审批意见
		Probaseinfo pbi = probaseinfoService.queryProbaseinfoByCaseid(rcaseid,
				GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO, GCC.PROBASEINFO_OPTTYPE_ACCEPTED,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_RESULT));
		if (pbi != null && !StringUtils.isBlank(pbi.getResultmsg())) {
			mav.addObject("result", pbi.getResult());
			mav.addObject("resultRemark", pbi.getRemark());
			mav.addObject("resultmsg", pbi.getResultmsg());
		} else {
			mav.addObject("result", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: rcasebaseinfoFlowForReq
	 * @Description: 案件维护-发送
	 * @author ybb
	 * @date 2016年9月21日 上午10:28:11
	 * @param rcaseid
	 * @return
	 */
	@RequestMapping("/rcasebaseinfoFlowForReq.do")
	@ResponseBody
	public ResultMsg rcasebaseinfoFlowForReq(String rcaseid) {
		
		ResultMsg msg = null;

		try {
			
			rcasebaseinfoService.addFlowForReq(rcaseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: rcasebaseinfoDivisionInit
	 * @Description: 分案审批-返回分案审批gird列表页面
	 * @author ybb
	 * @date 2016年9月21日 下午1:42:23
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/rcasebaseinfoDivisionInit.do"})
	public ModelAndView rcasebaseinfoDivisionInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bxzfy/rcasebaseinfodivision_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryRcasebaseinfoDivisionList
	 * @Description: 分案审批-查询被复议分案审批列表信息
	 * @author ybb
	 * @date 2016年9月21日 下午1:43:59
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryRcasebaseinfoDivisionList.do")
	@ResponseBody
	public EasyUITotalResult queryRcasebaseinfoDivisionList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = rcasebaseinfoService.queryRcasebaseinfoDivisionList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: updateOpttypeByCaseid
	 * @Description: 案件接收处理
	 * @author ybb
	 * @date 2016年9月21日 下午2:59:05
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping({"/updateOpttypeByCaseid.do"})
	@ResponseBody
	public ResultMsg updateOpttypeByCaseid(HttpServletRequest request) throws ServletException{ 
		
		ResultMsg msg = null;
		
		//记录案件接收日志
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			rcasebaseinfoService.updateOpttypeByCaseid(param);
			msg = new ResultMsg(true, "接收成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: rcasebaseinfoDivisionEditInit
	 * @Description: 分案审批-返回分案审批页面
	 * @author ybb
	 * @date 2016年9月21日 下午2:46:54
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws AppException 
	 */
	@RequestMapping({"/rcasebaseinfoDivisionEditInit.do"})
	public ModelAndView rcasebaseinfoDivisionEditInit(HttpServletRequest request) throws ServletException, AppException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bxzfy/rcasebaseinfodivision_form");
		
		//案件ID
		String rcaseid  = request.getParameter("rcaseid");
		mav.addObject("rcaseid", rcaseid);
		
		//根据案件ID查询案件信息
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoService.queryRcasebaseinfoByRcaseid(rcaseid);
		mav.addObject("rcasebaseinfo", rcasebaseinfo);
		
		return mav;
	}
	
	/**
	 * @Title: sendToOtherUserInit
	 * @Description: 分案处理-返回选择用户页面
	 * @author ybb
	 * @date 2016年9月21日 下午4:03:56
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/selectUser.do"})
	public ModelAndView selectUser(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bxzfy/selectuser_form");
		
		return mav;
	}
	
	/**
	 * @Title: queryUserList
	 * @Description: 分案处理-返回用户列表
	 * @author ybb
	 * @date 2016年9月21日 下午3:46:51
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryUserList.do")
	@ResponseBody
	public EasyUITotalResult queryUserList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = rcasebaseinfoService.queryUserList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: rcasebaseinfoDivisionSave
	 * @Description: 分案处理-保存
	 * @author ybb
	 * @date 2016年9月21日 下午5:00:11
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/rcasebaseinfoDivisionSave.do")
	@ResponseBody
	public ResultMsg rcasebaseinfoDivisionSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			rcasebaseinfoService.addRcasebaseinfoDivision(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: rcasebaseinfoFlowForDivision
	 * @Description: 分案处理-发送
	 * @author ybb
	 * @date 2016年9月21日 下午5:58:05
	 * @param rcaseid
	 * @return
	 */
	@RequestMapping("/rcasebaseinfoFlowForDivision.do")
	@ResponseBody
	public ResultMsg rcasebaseinfoFlowForDivision(String rcaseid) {
		
		ResultMsg msg = null;

		try {
			
			rcasebaseinfoService.addFlowForDivision(rcaseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: rcasebaseinfoFlowForReturn
	 * @Description: 被行政复议-退回
	 * @author ybb
	 * @date 2016年9月22日 上午8:48:01
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/rcasebaseinfoFlowForReturn.do")
	@ResponseBody
	public ResultMsg rcasebaseinfoFlowForReturn(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			rcasebaseinfoService.addFlowForReturn(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: rcasebaseinfoDisposeInit
	 * @Description: 案件处理-返回grid页面
	 * @author ybb
	 * @date 2016年9月22日 上午9:03:29
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/rcasebaseinfoDisposeInit.do"})
	public ModelAndView rcasebaseinfoDisposeInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bxzfy/rcasebaseinfodispose_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryRcasebaseinfoDisposeList
	 * @Description: 案件处理-查询被复议案件处理列表信息
	 * @author ybb
	 * @date 2016年9月22日 上午9:04:20
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryRcasebaseinfoDisposeList.do")
	@ResponseBody
	public EasyUITotalResult queryRcasebaseinfoDisposeList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = rcasebaseinfoService.queryRcasebaseinfoDisposeList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: rcasebaseinfoDisposeEditInit
	 * @Description: 案件处理-返回案件处理页面
	 * @author ybb
	 * @date 2016年9月22日 上午9:06:31
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws AppException
	 */
	@RequestMapping({"/rcasebaseinfoDisposeEditInit.do"})
	public ModelAndView rcasebaseinfoDisposeEditInit(HttpServletRequest request) throws ServletException, AppException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bxzfy/rcasebaseinfodispose_form");
		
		//案件ID
		String rcaseid  = request.getParameter("rcaseid");
		mav.addObject("rcaseid", rcaseid);
		
		//根据案件ID查询案件信息
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoService.queryRcasebaseinfoByRcaseid(rcaseid);
		mav.addObject("rcasebaseinfo", rcasebaseinfo);
		
		return mav;
	}
	
	/**
	 * @Title: rcasebaseinfoDisposeSave
	 * @Description: 案件处理-保存案件处理信息
	 * @author ybb
	 * @date 2016年9月22日 上午9:08:46
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/rcasebaseinfoDisposeSave.do")
	@ResponseBody
	public ResultMsg rcasebaseinfoDisposeSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			rcasebaseinfoService.addRcasebaseinfoDispose(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: rcasebaseinfoFlowForDispose
	 * @Description: 案件处理-发送
	 * @author ybb
	 * @date 2016年9月22日 上午9:09:26
	 * @param rcaseid
	 * @return
	 */
	@RequestMapping("/rcasebaseinfoFlowForDispose.do")
	@ResponseBody
	public ResultMsg rcasebaseinfoFlowForDispose(String rcaseid) {
		
		ResultMsg msg = null;

		try {
			
			rcasebaseinfoService.addFlowForDispose(rcaseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: rcasebaseinfoAuditInit
	 * @Description: 案件审阅-返回grid页面
	 * @author ybb
	 * @date 2016年9月22日 上午9:25:37
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/rcasebaseinfoAuditInit.do"})
	public ModelAndView rcasebaseinfoAuditInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bxzfy/rcasebaseinfoaudit_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryRcasebaseinfoAuditList
	 * @Description: 案件审阅-查询被复议案件审阅列表信息
	 * @author ybb
	 * @date 2016年9月22日 上午9:26:34
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryRcasebaseinfoAuditList.do")
	@ResponseBody
	public EasyUITotalResult queryRcasebaseinfoAuditList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = rcasebaseinfoService.queryRcasebaseinfoAuditList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: rcasebaseinfoAuditEditInit
	 * @Description: 案件审阅-返回案件审阅页面
	 * @author ybb
	 * @date 2016年9月22日 上午9:27:36
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws AppException
	 */
	@RequestMapping({"/rcasebaseinfoAuditEditInit.do"})
	public ModelAndView rcasebaseinfoAuditEditInit(HttpServletRequest request) throws ServletException, AppException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bxzfy/rcasebaseinfoaudit_form");
		
		//案件ID
		String rcaseid  = request.getParameter("rcaseid");
		mav.addObject("rcaseid", rcaseid);
		
		//根据案件ID查询案件信息
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoService.queryRcasebaseinfoByRcaseid(rcaseid);
		mav.addObject("rcasebaseinfo", rcasebaseinfo);
		
		//审批意见
		Probaseinfo auditPbi = probaseinfoService.queryProbaseinfoByCaseid(rcaseid,
				GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO, GCC.PROBASEINFO_OPTTYPE_ACCEPTED, rcasebaseinfo.getNodeid());
		if(auditPbi != null){
			mav.addObject("result", auditPbi.getResult());
			mav.addObject("remark", auditPbi.getRemark());
			mav.addObject("resultmsg", auditPbi.getResultmsg());
		}
		
		return mav;
	}
	
	/**
	 * @Title: rcasebaseinfoAuditSave
	 * @Description: 案件审阅-保存案件审阅信息
	 * @author ybb
	 * @date 2016年9月22日 上午9:28:12
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/rcasebaseinfoAuditSave.do")
	@ResponseBody
	public ResultMsg rcasebaseinfoAuditSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			rcasebaseinfoService.addRcasebaseinfoAudit(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: rcasebaseinfoFlowForAudit
	 * @Description: 案件审阅-发送
	 * @author ybb
	 * @date 2016年9月22日 上午9:28:56
	 * @param rcaseid
	 * @return
	 */
	@RequestMapping("/rcasebaseinfoFlowForAudit.do")
	@ResponseBody
	public ResultMsg rcasebaseinfoFlowForAudit(String rcaseid) {
		
		ResultMsg msg = null;

		try {
			
			rcasebaseinfoService.addFlowForAudit(rcaseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: rcasebaseinfoResultInit
	 * @Description: 案件审阅-返回grid页面
	 * @author ybb
	 * @date 2016年9月22日 上午9:25:37
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/rcasebaseinfoResultInit.do"})
	public ModelAndView rcasebaseinfoResultInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bxzfy/rcasebaseinforesult_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryRcasebaseinfoResultList
	 * @Description: 案件结果-查询被复议案件结果列表信息
	 * @author ybb
	 * @date 2016年9月22日 上午10:01:36
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryRcasebaseinfoResultList.do")
	@ResponseBody
	public EasyUITotalResult queryRcasebaseinfoResultList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = rcasebaseinfoService.queryRcasebaseinfoResultList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: rcasebaseinfoResultEditInit
	 * @Description: 案件结果-返回案件结果页面
	 * @author ybb
	 * @date 2016年9月22日 上午10:02:18
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws AppException
	 */
	@RequestMapping({"/rcasebaseinfoResultEditInit.do"})
	public ModelAndView rcasebaseinfoResultEditInit(HttpServletRequest request) throws ServletException, AppException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bxzfy/rcasebaseinforesult_form");
		
		//案件ID
		String rcaseid  = request.getParameter("rcaseid");
		mav.addObject("rcaseid", rcaseid);
		
		//根据案件ID查询案件信息
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoService.queryRcasebaseinfoByRcaseid(rcaseid);
		mav.addObject("rcasebaseinfo", rcasebaseinfo);
		
		//审批意见
		Probaseinfo auditPbi = probaseinfoService.queryProbaseinfoByCaseid(rcaseid,
				GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO, GCC.PROBASEINFO_OPTTYPE_ACCEPTED,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_AUDIT));
		if (auditPbi != null) {
			mav.addObject("auditResult", auditPbi.getResultmsg());
			mav.addObject("auditRemark", auditPbi.getRemark());
		}
		
		//审批意见
		Probaseinfo pbi = probaseinfoService.queryProbaseinfoByCaseid(rcaseid,
				GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO, GCC.PROBASEINFO_OPTTYPE_ACCEPTED,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_RESULT));
		if (pbi != null) {
			mav.addObject("result", pbi.getResult());
			mav.addObject("resultRemark", pbi.getRemark());
			mav.addObject("resultmsg", pbi.getResultmsg());
		}
				
		return mav;
	}
	
	/**
	 * @Title: rcasebaseinfoResultSave
	 * @Description: 案件结果-保存案件结果信息
	 * @author ybb
	 * @date 2016年9月22日 上午10:02:57
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/rcasebaseinfoResultSave.do")
	@ResponseBody
	public ResultMsg rcasebaseinfoResultSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			rcasebaseinfoService.addRcasebaseinfoResult(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: rcasebaseinfoFlowForResult
	 * @Description: 案件结果-发送
	 * @author ybb
	 * @date 2016年9月22日 上午10:03:31
	 * @param rcaseid
	 * @return
	 */
	@RequestMapping("/rcasebaseinfoFlowForResult.do")
	@ResponseBody
	public ResultMsg rcasebaseinfoFlowForResult(String rcaseid) {
		
		ResultMsg msg = null;

		try {
			
			rcasebaseinfoService.addFlowForResult(rcaseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: rcasebaseinfoViewInit
	 * @Description: 案件查询-返回grid页面
	 * @author ybb
	 * @date 2016年9月24日 下午4:19:38
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/rcasebaseinfoViewInit.do"})
	public ModelAndView rcasebaseinfoViewInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/bxzfy/rcasebaseinfoview_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryRcasebaseinfoViewList
	 * @Description: 案件查询-查询被复议案件列表信息
	 * @author ybb
	 * @date 2016年9月24日 下午4:20:15
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryRcasebaseinfoViewList.do")
	@ResponseBody
	public EasyUITotalResult queryRcasebaseinfoRViewList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = rcasebaseinfoService.queryRcasebaseinfoViewList(param);
		
		return EasyUITotalResult.from(ps);
	}
}
