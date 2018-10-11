package com.wfzcx.aros.ysaj.controller;

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
import com.wfzcx.aros.flow.po.Probaseinfo;
import com.wfzcx.aros.flow.service.ProbaseinfoService;
import com.wfzcx.aros.util.GCC;
import com.wfzcx.aros.ysaj.po.Admlitbaseinfo;
import com.wfzcx.aros.ysaj.service.AdmlitbaseinfoService;

/**
 * @ClassName: AdmlitbaseinfoController
 * @Description: 用来处理应诉案件相关业务
 * @author ybb
 * @date 2016年9月22日 下午1:58:36
 * @version V1.0
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/ysaj/controller/AdmlitbaseinfoController")
public class AdmlitbaseinfoController {

	@Autowired
	private AdmlitbaseinfoService admlitbaseinfoService;
	@Autowired
	private ProbaseinfoService probaseinfoService;
	
	/**
	 * @Title: admlitbaseinfoReqInit
	 * @Description: 案件维护-返回应诉案件维护gird列表页面
	 * @author ybb
	 * @date 2016年9月22日14:10:32
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/admlitbaseinfoReqInit.do"})
	public ModelAndView admlitbaseinfoReqInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ysaj/admlitbaseinforeq_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryAdmlitbaseinfoReqList
	 * @Description: 案件维护-查询应诉案件维护列表信息
	 * @author ybb
	 * @date 2016年9月22日14:10:39
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryAdmlitbaseinfoReqList.do")
	@ResponseBody
	public EasyUITotalResult queryAdmlitbaseinfoReqList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = admlitbaseinfoService.queryAdmlitbaseinfoReqList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: admlitbaseinfoAddInit
	 * @Description: 案件维护-返回应诉案件维护gird列表页面
	 * @author ybb
	 * @date 2016年9月22日14:10:45
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/admlitbaseinfoAddInit.do"})
	public ModelAndView admlitbaseinfoAddInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ysaj/admlitbaseinforeq_form");
		//附件ID
		UUID uuid = UUID.randomUUID();
		
		//新增时产生临时的uuid放在附件的keyid中
		mav.addObject("fjkeyid", uuid);
		
		return mav;
	}
	
	/**
	 * @Title: admlitbaseinfoReqSave
	 * @Description: 案件维护-新增
	 * @author ybb
	 * @date 2016年9月22日14:10:52
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/admlitbaseinfoReqSave.do")
	@ResponseBody
	public ResultMsg admlitbaseinfoReqSave(HttpServletRequest request) {

		ResultMsg msg = null;

		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);

		try {
			String caseid = admlitbaseinfoService.addAdmlitbaseinfoReq(param);
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
	 * @Title: admlitbaseinfoEditInit
	 * @Description: 案件维护-返回应诉案件修改页面
	 * @author ybb
	 * @date 2016年9月22日14:10:58
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/admlitbaseinfoEditInit.do"})
	public ModelAndView admlitbaseinfoEditInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ysaj/admlitbaseinforeq_form");
		
		mav.addObject("relaacaseid", request.getParameter("relaacaseid"));
		mav.addObject("defendant", request.getParameter("defendant"));
		
		return mav;
	}
	
	/**
	 * @Title: admlitbaseinfoReqEdit
	 * @Description: 案件维护-修改
	 * @author ybb
	 * @date 2016年9月22日14:11:34
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/admlitbaseinfoReqEdit.do")
	@ResponseBody
	public ResultMsg admlitbaseinfoReqEdit(HttpServletRequest request){
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			admlitbaseinfoService.updateAdmlitbaseinfoReq(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: admlitbaseinfoReqDelete
	 * @Description: 案件维护-删除
	 * @author ybb
	 * @date 2016年9月22日14:12:05
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/admlitbaseinfoReqDelete.do")
	@ResponseBody
	public ResultMsg admlitbaseinfoReqDelete(String acaseid) {
		
		ResultMsg msg = null;
		
		try {
			
			admlitbaseinfoService.delAdmlitbaseinfoReq(acaseid);
			msg = new ResultMsg(true, AppException.getMessage("删除成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败"));
		}
		
		return msg;
	}
	
	/**
	 * @Title: admlitbaseinfoReqDetail
	 * @Description: 案件查看-查看
	 * @author ybb
	 * @date 2016年9月22日14:12:14
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws AppException
	 */
	@RequestMapping({"/admlitbaseinfoReqDetail.do"})
	public ModelAndView admlitbaseinfoReqDetail(HttpServletRequest request) throws ServletException, AppException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ysaj/admlitbaseinforeq_detail");
		
		//案件ID
		String acaseid  = request.getParameter("acaseid");
		mav.addObject("acaseid", acaseid);
		
		//根据案件ID查询案件信息
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoService.queryAdmlitbaseinfoByAcaseid(acaseid);
		if (admlitbaseinfo != null && StringUtils.isBlank(admlitbaseinfo.getCourthead())) {
			admlitbaseinfo.setCourthead("");
		}
		mav.addObject("admlitbaseinfo", admlitbaseinfo);
		
		//审批意见
		Probaseinfo auditPbi = probaseinfoService.queryProbaseinfoByCaseid(acaseid,
				GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO, GCC.PROBASEINFO_OPTTYPE_ACCEPTED,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_AUDIT));
		if (auditPbi != null) {
			mav.addObject("auditResult", auditPbi.getResultmsg());
			mav.addObject("auditRemark", auditPbi.getRemark());
		} else {
			mav.addObject("auditResult", "");
		}
		
		//审批意见
		Probaseinfo pbi = probaseinfoService.queryProbaseinfoByCaseid(acaseid,
				GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO, GCC.PROBASEINFO_OPTTYPE_ACCEPTED,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_RESULT));
		if (pbi != null) {
			mav.addObject("result", pbi.getResult());
			mav.addObject("resultRemark", pbi.getRemark());
			mav.addObject("resultmsg", pbi.getResultmsg());
		} else {
			mav.addObject("result", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: admlitbaseinfoFlowForReq
	 * @Description: 案件维护-发送
	 * @author ybb
	 * @date 2016年9月22日14:12:23
	 * @param acaseid
	 * @return
	 */
	@RequestMapping("/admlitbaseinfoFlowForReq.do")
	@ResponseBody
	public ResultMsg admlitbaseinfoFlowForReq(String acaseid) {
		
		ResultMsg msg = null;

		try {
			
			admlitbaseinfoService.addFlowForReq(acaseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: admlitbaseinfoDivisionInit
	 * @Description: 分案审批-返回分案审批gird列表页面
	 * @author ybb
	 * @date 2016年9月22日14:12:30
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/admlitbaseinfoDivisionInit.do"})
	public ModelAndView admlitbaseinfoDivisionInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ysaj/admlitbaseinfodivision_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryAdmlitbaseinfoDivisionList
	 * @Description: 分案审批-查询被复议分案审批列表信息
	 * @author ybb
	 * @date 2016年9月22日14:12:35
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryAdmlitbaseinfoDivisionList.do")
	@ResponseBody
	public EasyUITotalResult queryAdmlitbaseinfoDivisionList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = admlitbaseinfoService.queryAdmlitbaseinfoDivisionList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: updateOpttypeByCaseid
	 * @Description: 案件接收处理
	 * @author ybb
	 * @date 2016年9月22日14:12:39
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
			admlitbaseinfoService.updateOpttypeByCaseid(param);
			msg = new ResultMsg(true, "接收成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: admlitbaseinfoDivisionEditInit
	 * @Description: 分案审批-返回分案审批页面
	 * @author ybb
	 * @date 2016年9月22日14:12:45
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws AppException 
	 */
	@RequestMapping({"/admlitbaseinfoDivisionEditInit.do"})
	public ModelAndView admlitbaseinfoDivisionEditInit(HttpServletRequest request) throws ServletException, AppException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ysaj/admlitbaseinfodivision_form");
		
		//案件ID
		String acaseid  = request.getParameter("acaseid");
		mav.addObject("acaseid", acaseid);
		
		//根据案件ID查询案件信息
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoService.queryAdmlitbaseinfoByAcaseid(acaseid);
		mav.addObject("admlitbaseinfo", admlitbaseinfo);
		
		return mav;
	}
	
	/**
	 * @Title: sendToOtherUserInit
	 * @Description: 分案处理-返回选择用户页面
	 * @author ybb
	 * @date 2016年9月22日14:12:49
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/selectUser.do"})
	public ModelAndView selectUser(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ysaj/selectuser_form");
		
		return mav;
	}
	
	/**
	 * @Title: queryUserList
	 * @Description: 分案处理-返回用户列表
	 * @author ybb
	 * @date 2016年9月22日14:12:53
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
		PaginationSupport ps = admlitbaseinfoService.queryUserList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: admlitbaseinfoDivisionSave
	 * @Description: 分案处理-保存
	 * @author ybb
	 * @date 2016年9月22日14:12:57
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/admlitbaseinfoDivisionSave.do")
	@ResponseBody
	public ResultMsg admlitbaseinfoDivisionSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			admlitbaseinfoService.addAdmlitbaseinfoDivision(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: admlitbaseinfoFlowForDivision
	 * @Description: 分案处理-发送
	 * @author ybb
	 * @date 2016年9月22日14:13:01
	 * @param acaseid
	 * @return
	 */
	@RequestMapping("/admlitbaseinfoFlowForDivision.do")
	@ResponseBody
	public ResultMsg admlitbaseinfoFlowForDivision(String acaseid) {
		
		ResultMsg msg = null;

		try {
			
			admlitbaseinfoService.addFlowForDivision(acaseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: admlitbaseinfoFlowForReturn
	 * @Description: 被行政复议-退回
	 * @author ybb
	 * @date 2016年9月22日14:13:05
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/admlitbaseinfoFlowForReturn.do")
	@ResponseBody
	public ResultMsg admlitbaseinfoFlowForReturn(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			admlitbaseinfoService.addFlowForReturn(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: admlitbaseinfoDisposeInit
	 * @Description: 案件处理-返回grid页面
	 * @author ybb
	 * @date 2016年9月22日14:13:08
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/admlitbaseinfoDisposeInit.do"})
	public ModelAndView admlitbaseinfoDisposeInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ysaj/admlitbaseinfodispose_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryAdmlitbaseinfoDisposeList
	 * @Description: 案件处理-查询应诉案件处理列表信息
	 * @author ybb
	 * @date 2016年9月22日14:13:13
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryAdmlitbaseinfoDisposeList.do")
	@ResponseBody
	public EasyUITotalResult queryAdmlitbaseinfoDisposeList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = admlitbaseinfoService.queryAdmlitbaseinfoDisposeList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: admlitbaseinfoDisposeEditInit
	 * @Description: 案件处理-返回案件处理页面
	 * @author ybb
	 * @date 2016年9月22日14:13:16
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws AppException
	 */
	@RequestMapping({"/admlitbaseinfoDisposeEditInit.do"})
	public ModelAndView admlitbaseinfoDisposeEditInit(HttpServletRequest request) throws ServletException, AppException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ysaj/admlitbaseinfodispose_form");
		
		//案件ID
		String acaseid  = request.getParameter("acaseid");
		mav.addObject("acaseid", acaseid);
		
		//根据案件ID查询案件信息
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoService.queryAdmlitbaseinfoByAcaseid(acaseid);
		mav.addObject("admlitbaseinfo", admlitbaseinfo);
		
		return mav;
	}
	
	/**
	 * @Title: admlitbaseinfoDisposeSave
	 * @Description: 案件处理-保存案件处理信息
	 * @author ybb
	 * @date 2016年9月22日14:13:20
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/admlitbaseinfoDisposeSave.do")
	@ResponseBody
	public ResultMsg admlitbaseinfoDisposeSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			admlitbaseinfoService.addAdmlitbaseinfoDispose(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: admlitbaseinfoFlowForDispose
	 * @Description: 案件处理-发送
	 * @author ybb
	 * @date 2016年9月22日14:13:24
	 * @param acaseid
	 * @return
	 */
	@RequestMapping("/admlitbaseinfoFlowForDispose.do")
	@ResponseBody
	public ResultMsg admlitbaseinfoFlowForDispose(String acaseid) {
		
		ResultMsg msg = null;

		try {
			
			admlitbaseinfoService.addFlowForDispose(acaseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: admlitbaseinfoAuditInit
	 * @Description: 案件审阅-返回grid页面
	 * @author ybb
	 * @date 2016年9月22日14:13:27
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/admlitbaseinfoAuditInit.do"})
	public ModelAndView admlitbaseinfoAuditInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ysaj/admlitbaseinfoaudit_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryAdmlitbaseinfoAuditList
	 * @Description: 案件审阅-查询应诉案件审阅列表信息
	 * @author ybb
	 * @date 2016年9月22日14:13:31
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryAdmlitbaseinfoAuditList.do")
	@ResponseBody
	public EasyUITotalResult queryAdmlitbaseinfoAuditList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = admlitbaseinfoService.queryAdmlitbaseinfoAuditList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: admlitbaseinfoAuditEditInit
	 * @Description: 案件审阅-返回案件审阅页面
	 * @author ybb
	 * @date 2016年9月22日14:13:38
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws AppException
	 */
	@RequestMapping({"/admlitbaseinfoAuditEditInit.do"})
	public ModelAndView admlitbaseinfoAuditEditInit(HttpServletRequest request) throws ServletException, AppException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ysaj/admlitbaseinfoaudit_form");
		
		//案件ID
		String acaseid  = request.getParameter("acaseid");
		mav.addObject("acaseid", acaseid);
		
		//根据案件ID查询案件信息
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoService.queryAdmlitbaseinfoByAcaseid(acaseid);
		mav.addObject("admlitbaseinfo", admlitbaseinfo);
		
		//审批意见
		Probaseinfo auditPbi = probaseinfoService.queryProbaseinfoByCaseid(acaseid,
				GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO, GCC.PROBASEINFO_OPTTYPE_ACCEPTED, admlitbaseinfo.getNodeid());
		if(auditPbi != null){
			mav.addObject("result", auditPbi.getResult());
			mav.addObject("remark", auditPbi.getRemark());
			mav.addObject("resultmsg", auditPbi.getResultmsg());
		}
		
		return mav;
	}
	
	/**
	 * @Title: admlitbaseinfoAuditSave
	 * @Description: 案件审阅-保存案件审阅信息
	 * @author ybb
	 * @date 2016年9月22日14:13:42
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/admlitbaseinfoAuditSave.do")
	@ResponseBody
	public ResultMsg admlitbaseinfoAuditSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			admlitbaseinfoService.addAdmlitbaseinfoAudit(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: admlitbaseinfoFlowForAudit
	 * @Description: 案件审阅-发送
	 * @author ybb
	 * @date 2016年9月22日14:13:45
	 * @param acaseid
	 * @return
	 */
	@RequestMapping("/admlitbaseinfoFlowForAudit.do")
	@ResponseBody
	public ResultMsg admlitbaseinfoFlowForAudit(String acaseid) {
		
		ResultMsg msg = null;

		try {
			
			admlitbaseinfoService.addFlowForAudit(acaseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: admlitbaseinfoResultInit
	 * @Description: 案件审阅-返回grid页面
	 * @author ybb
	 * @date 2016年9月22日14:13:49
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/admlitbaseinfoResultInit.do"})
	public ModelAndView admlitbaseinfoResultInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ysaj/admlitbaseinforesult_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryAdmlitbaseinfoResultList
	 * @Description: 案件结果-查询应诉案件结果列表信息
	 * @author ybb
	 * @date 2016年9月22日14:13:52
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryAdmlitbaseinfoResultList.do")
	@ResponseBody
	public EasyUITotalResult queryAdmlitbaseinfoResultList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = admlitbaseinfoService.queryAdmlitbaseinfoResultList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: admlitbaseinfoResultEditInit
	 * @Description: 案件结果-返回案件结果页面
	 * @author ybb
	 * @date 2016年9月22日14:13:56
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws AppException
	 */
	@RequestMapping({"/admlitbaseinfoResultEditInit.do"})
	public ModelAndView admlitbaseinfoResultEditInit(HttpServletRequest request) throws ServletException, AppException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ysaj/admlitbaseinforesult_form");
		
		//案件ID
		String acaseid  = request.getParameter("acaseid");
		mav.addObject("acaseid", acaseid);
		
		//根据案件ID查询案件信息
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoService.queryAdmlitbaseinfoByAcaseid(acaseid);
		mav.addObject("admlitbaseinfo", admlitbaseinfo);
		
		//审批意见
		Probaseinfo auditPbi = probaseinfoService.queryProbaseinfoByCaseid(acaseid,
				GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO, GCC.PROBASEINFO_OPTTYPE_ACCEPTED,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_AUDIT));
		if (auditPbi != null) {
			mav.addObject("auditResult", auditPbi.getResultmsg());
			mav.addObject("auditRemark", auditPbi.getRemark());
		}
		
		//审批意见
		Probaseinfo pbi = probaseinfoService.queryProbaseinfoByCaseid(acaseid,
				GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO, GCC.PROBASEINFO_OPTTYPE_ACCEPTED,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_RESULT));
		if (pbi != null) {
			mav.addObject("result", pbi.getResult());
			mav.addObject("resultRemark", pbi.getRemark());
			mav.addObject("resultmsg", pbi.getResultmsg());
		}
				
		return mav;
	}
	
	/**
	 * @Title: admlitbaseinfoResultSave
	 * @Description: 案件结果-保存案件结果信息
	 * @author ybb
	 * @date 2016年9月22日14:14:06
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/admlitbaseinfoResultSave.do")
	@ResponseBody
	public ResultMsg admlitbaseinfoResultSave(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			admlitbaseinfoService.addAdmlitbaseinfoResult(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: admlitbaseinfoFlowForResult
	 * @Description: 案件结果-发送
	 * @author ybb
	 * @date 2016年9月22日14:14:09
	 * @param acaseid
	 * @return
	 */
	@RequestMapping("/admlitbaseinfoFlowForResult.do")
	@ResponseBody
	public ResultMsg admlitbaseinfoFlowForResult(String acaseid) {
		
		ResultMsg msg = null;

		try {
			
			admlitbaseinfoService.addFlowForResult(acaseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: admlitbaseinfoViewInit
	 * @Description: 案件查询-返回行政复议查询页面
	 * @author ybb
	 * @date 2016年9月24日 下午3:32:17
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/admlitbaseinfoViewInit.do"})
	public ModelAndView admlitbaseinfoViewInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ysaj/admlitbaseinfoview_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryAdmlitbaseinfoViewList
	 * @Description: 案件查询-查询行政应诉案件信息
	 * @author ybb
	 * @date 2016年9月24日 下午3:21:08
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryAdmlitbaseinfoViewList.do")
	@ResponseBody
	public EasyUITotalResult queryAdmlitbaseinfoViewList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = admlitbaseinfoService.queryAdmlitbaseinfoViewList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: selectCase
	 * @Description: 案件维护-返回选择关联案件页面
	 * @author ybb
	 * @date 2016年9月27日 下午1:47:51
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/selectCase.do"})
	public ModelAndView selectCase(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/ysaj/selectcase_form");
		
		return mav;
	}
	
	/**
	 * @Title: queryCaseList
	 * @Description: 案件维护-根据条件查询关联应诉案件列表
	 * @author ybb
	 * @date 2016年9月27日 下午1:49:13
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryCaseList.do")
	@ResponseBody
	public EasyUITotalResult queryCaseList(HttpServletRequest request) throws AppException {
		
		//获取审理阶段
		String stage = request.getParameter("stage");
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = admlitbaseinfoService.queryCaseList(param, stage);
		
		return EasyUITotalResult.from(ps);
	}
}
