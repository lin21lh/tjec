package com.wfzcx.aros.xzfy.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.util.JsonUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.aros.flow.po.Probaseinfo;
import com.wfzcx.aros.flow.service.ProbaseinfoService;
import com.wfzcx.aros.sqbl.service.ApplyRecordInfoService;
import com.wfzcx.aros.util.GCC;
import com.wfzcx.aros.xzfy.po.Casebaseinfo;
import com.wfzcx.aros.xzfy.service.CasebaseinfoService;
import com.wfzcx.aros.xzfy.service.ThirdbaseinfoService;
import com.wfzcx.aros.xzfy.vo.CasebaseinfoVo;
import com.wfzcx.aros.xzfy.vo.ThirdbaseinfoVo;

/**
 * @ClassName: CasebaseinfoController
 * @Description: 用来处理行政复议案件审批、中止、恢复、终止、和解等业务
 * @author ybb
 * @date 2016年8月12日 上午9:45:38
 * @version V1.0
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/xzfy/controller/CasebaseinfoController")
public class CasebaseinfoController {

	@Autowired
	private CasebaseinfoService casebaseinfoService;
	@Autowired
	private ThirdbaseinfoService thirdbaseinfoService;
	@Autowired
	private ProbaseinfoService probaseinfoService;
	@Autowired
	private ApplyRecordInfoService applyRecordInfoService;
	
	/**
	 * @Title: xzfyInit
	 * @Description: 行政复议主页面
	 * @author ztt
	 * @date 2016年11月8日
	 * @param request
	 * @return
	 */
	@RequestMapping({"/xzfyInit.do"})
	public ModelAndView xzfyInit(HttpServletRequest request) { 
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/casebaseinfo_init");
		String menuid = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		String key = request.getParameter("key");
		mav.addObject("key", key);
		
		return mav;
	}
	
	/**
	 * @Title: queryXzfyreqList
	 * @Description: 行政复议申请：grid列表页面查询
	 * @author ztt
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryXzfyList.do")
	@ResponseBody
	public EasyUITotalResult queryXzfyList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = casebaseinfoService.queryXzfyList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: xzfyReqReceiveEdit
	 * @Description: 行政复议接收材料：返回接收材料页面
	 * @author ztt
	 * @date 2016年11月8日
	 * @param request
	 * @return
	 */
	@RequestMapping({"/xzfyReceiveEdit.do"})
	public ModelAndView xzfyReqReceiveEdit(HttpServletRequest request) { 
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/casebaseinforeceive_form");
		// 0不可用，1可用
		String showFile = "0";
		String caseid = request.getParameter("caseid");
		String nodeid = request.getParameter("nodeid");
		Casebaseinfo casebaseinfo = null;
		String arid = "";
		if (StringUtil.isNotBlank(caseid)) {
			casebaseinfo = casebaseinfoService.queryCasebaseinfoByCaseid(caseid);
			arid = applyRecordInfoService.getAridByCaseId(caseid);
			showFile = "1";
		}
		if (null != casebaseinfo) {
			mav.addObject("casebaseinfo", casebaseinfo);
			mav.addObject("csaecode", casebaseinfo.getCsaecode());
		} else {
			mav.addObject("casebaseinfo", "");
			mav.addObject("csaecode", casebaseinfoService.getcaseCodeAuto());
		}
		mav.addObject("showFile", showFile);
		mav.addObject("nodeid", nodeid);
		mav.addObject("caseid", caseid);
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	/**
	 * @Title: xzfyReqReceiveSaveEdit
	 * @Description: 行政复议接收材料：保存
	 * @author ztt
	 * @date 2016年11月8日
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/xzfyReceiveSaveEdit.do")
	@ResponseBody
	public ResultMsg xzfyReqReceiveSaveEdit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try
		{
			String caseid = casebaseinfoService.updateXzfyReceiveByCaseid(param);
			if (!StringUtils.isBlank(caseid)) {
				HashMap<String, Object> body = new HashMap<String, Object>();
				body.put("caseid", caseid);
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
	 * @Title: xzfyReceiveDetail
	 * @Description: 行政复议接收材料：详情
	 * @author ztt
	 * @date 2016年11月8日
	 * @param request
	 * @return
	 */
	@RequestMapping({"/xzfyReceiveDetail.do"})
	public ModelAndView xzfyReqReceiveDetail(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/casebaseinforeceive_detail");
		String caseid = request.getParameter("caseid");
		Casebaseinfo casebaseinfo = casebaseinfoService.queryCasebaseinfoByCaseid(caseid);
		mav.addObject("casebaseinfo", casebaseinfo);
		mav.addObject("caseid", caseid);
		String nodeid = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		return mav;
	}
	
	/**
	 * @Title: xzfyReceiveFlow
	 * @Description: 行政复议接收材料：发送
	 * @author ztt
	 * @date 2016年11月2日
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/xzfyReceiveFlow.do")
	@ResponseBody
	public ResultMsg xzfyReqReceiveFlow(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.xzfyReceiveFlow(param);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: queryFileList
	 * @Description: 附件列表
	 * @author ztt
	 * @date 2016年11月8日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryFileList.do")
	@ResponseBody
	public EasyUITotalResult queryFileList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = casebaseinfoService.queryFileList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	
	/**
	 * @Title: xzfyReqInit
	 * @Description: 行政复议申请：返回申请列表页面
	 * @author ztt
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 */
	@RequestMapping({"/xzfyReqInit.do"})
	public ModelAndView xzfyReqInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/casebaseinforeq_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		return mav;
	}
	
	/**
	 * @Title: queryXzfyreqList
	 * @Description: 行政复议申请：grid列表页面查询
	 * @author ztt
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryXzfyReqList.do")
	@ResponseBody
	public EasyUITotalResult queryXzfyReqList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = casebaseinfoService.queryXzfyReqList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: queryNodeidsByCase
	 * @Description: 根据案件 caseID ,protype获取 经过流程节点nodeid的 list
	 * 
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryNodeidsByCase.do")
	@ResponseBody
	public List  queryNodeidsByCase(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		List list = casebaseinfoService.queryNodeidsByCase(param);
	
//		for (int i = 0; i < list.size(); i++) {
//			System.out.println(list.get(i));
//		}
		return list;
	}
	
	
	/**
	 * @Title: xzfyEdit
	 * @Description: 行政复议申请：返回行政复议申请修改页面
	 * @author ztt
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 * @throws AppException 
	 */
	@RequestMapping({"/xzfyReqEdit.do"})
	public ModelAndView xzfyReqEdit(HttpServletRequest request) throws AppException { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/casebaseinforeq_form");
		// 案件ID
		String caseid = request.getParameter("caseid");
		CasebaseinfoVo casebaseinfo = casebaseinfoService.queryXzfyreqByCaseid(caseid);
		mav.addObject("casebaseinfo", casebaseinfo);
		
		// 返回第三人信息
		ThirdbaseinfoVo thirdbaseinfo = thirdbaseinfoService.queryThirdByCaseid(caseid);
		if(thirdbaseinfo != null){
			mav.addObject("thirdbaseinfo", thirdbaseinfo);
		} else {
			mav.addObject("thirdbaseinfo", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: xzfyReqSaveEdit
	 * @Description: 行政复议申请：修改行政复议申请保存
	 * @author ztt
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/xzfyReqSaveEdit.do")
	@ResponseBody
	public ResultMsg xzfyReqSaveEdit(HttpServletRequest request){
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.updateXzfyReqByCaseid(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: xzfyReqDelete
	 * @Description: 行政复议申请：删除行政复议申请
	 * @author ztt
	 * @date 2016年11月2日
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/xzfyReqDelete.do")
	@ResponseBody
	public ResultMsg xzfyReqDelete(String caseid) {
		
		ResultMsg msg = null;
		
		try {
			casebaseinfoService.delXzfyReqByCaseid(caseid);
			msg = new ResultMsg(true, AppException.getMessage("删除成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败"));
		}
		return msg;
	}
	
	/**
	 * @Title: xzfyReqDetail
	 * @Description: 行政复议申请：返回行政复议申请详情页面
	 * @author ztt
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 * @throws AppException 
	 */
	@RequestMapping({"/xzfyReqDetail.do"})
	public ModelAndView xzfyReqDetail(HttpServletRequest request) throws AppException { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/casebaseinforeq_detail");
		// 案件ID
		String caseid = request.getParameter("caseid");
		CasebaseinfoVo casebaseinfo = casebaseinfoService.queryXzfyreqByCaseid(caseid);
		mav.addObject("casebaseinfo", casebaseinfo);
		
		return mav;
	}
	
	/**
	 * @Title: xzfyReqFlow
	 * @Description: 行政复议申请：发送
	 * @author ztt
	 * @date 2016年11月2日
	 * @param menuid
	 * @param activityId
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/xzfyReqFlow.do")
	@ResponseBody
	public ResultMsg xzfyReqFlow(HttpServletRequest request) {
		
		ResultMsg msg = null;
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.xzfyReqFlow(param);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: xzfyAcceptInit
	 * @Description: 行政复议受理：返回行政复议受理列表页面
	 * @author ztt
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/xzfyAcceptInit.do"})
	public ModelAndView xzfyAcceptInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/casebaseinfoaccept_init");
		
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		//节点ID
		String nodeid  = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	/**
	 * @Title: queryXzfyAcceptList
	 * @Description: 行政复议受理：行政复议受理grid页面列表查询
	 * @author ztt
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryXzfyAcceptList.do")
	@ResponseBody
	public EasyUITotalResult queryXzfyAcceptList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = casebaseinfoService.queryXzfyAcceptList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: xzfyAcceptEdit
	 * @Description: 行政复议受理：返回行政复议处理页面
	 * @author ztt
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/xzfyAcceptEdit.do"})
	public ModelAndView xzfyAcceptEdit(HttpServletRequest request) { 
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/xzfy/casebaseinfoaccept_form");
		
		// 案件ID
		String caseid  = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		
		// 案件基本信息
		Casebaseinfo casebaseinfo = casebaseinfoService.queryCasebaseinfoByCaseid(caseid);
		if(casebaseinfo != null)
		{
			mav.addObject("casebaseinfo", casebaseinfo);
		}
		
		// 第三人信息
		String  thirdnames = thirdbaseinfoService.queryThirdNamesByCaseid(caseid);
		mav.addObject("thirdnames", thirdnames);
		
		// 受理依据
		List<JSONObject> accbasis = casebaseinfoService.queryCaseaccbasisByCaseid(caseid);
		if(null != accbasis && !accbasis.isEmpty())
		{
			mav.addObject("accbasis", accbasis.get(0));
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCEPT));
		if(agent != null && null != agent.getRemark())
		{
			mav.addObject("agentRemark", agent.getRemark());
		}
		else
		{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, 
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCSECTION));
		if(section != null && null != section.getRemark())
		{
			mav.addObject("sectionRemark", section.getRemark());
		}
		else
		{
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCORGAN));
		if(organ != null && null != organ.getRemark())
		{
			mav.addObject("organRemark", organ.getRemark());
		}
		else
		{
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCOFFICE));
		if(office != null && null != office.getRemark())
		{
			mav.addObject("officeRemark", office.getRemark());
		}
		else
		{
			mav.addObject("officeRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: xzfyAcceptSaveEdit
	 * @Description: 行政复议受理：保存行政复议处理
	 * @author ztt
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/xzfyAcceptSaveEdit.do")
	@ResponseBody
	public ResultMsg xzfyAcceptSaveEdit(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.updateXzfyAcceptByCaseid(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: xzfyAcceptView
	 * @Description: 行政复议受理：详情
	 * @author ztt
	 * @date 2016年11月10日
	 * @param request
	 * @return
	 */
	@RequestMapping({"/xzfyAcceptDetail.do"})
	public ModelAndView xzfyAcceptDetail(HttpServletRequest request) { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/casebaseinfoaccept_detail");
		
		// 案件ID
		String caseid  = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		
		// 案件基本信息
		Casebaseinfo casebaseinfo = casebaseinfoService.queryCasebaseinfoByCaseid(caseid);
		if(casebaseinfo != null)
		{
			mav.addObject("casebaseinfo", casebaseinfo);
		}
		
		// 第三人信息
		ThirdbaseinfoVo thirdbaseinfo = thirdbaseinfoService.queryThirdByCaseid(caseid);
		if(thirdbaseinfo != null)
		{
			mav.addObject("thirdbaseinfo", thirdbaseinfo);
		}
		
		// 受理依据
		List<JSONObject> accbasis = casebaseinfoService.queryAccbasisByCaseid(caseid);
		if(null != accbasis && !accbasis.isEmpty())
		{
			mav.addObject("accbasis", accbasis.get(0));
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				 new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCEPT));
		if(null != agent)
		{
			mav.addObject("agentRemark", agent.getRemark());
		}
		else
		{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				 new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCSECTION));
		if(null != section)
		{
			mav.addObject("sectionRemark", section.getRemark());
		}
		else
		{
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				 new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCORGAN));
		if(null != organ)
		{
			mav.addObject("organRemark", organ.getRemark());
		}
		else
		{
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				 new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCOFFICE));
		if(null != office)
		{
			mav.addObject("officeRemark", office.getRemark());
		}
		else
		{
			mav.addObject("officeRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: xzfyAcceptFlow
	 * @Description: 行政复议受理：承办人->科室->机构->机关
	 * @author ztt
	 * @date 2016年11月2日
	 * @param menuid
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/xzfyAcceptFlow.do")
	@ResponseBody
	public ResultMsg xzfyAcceptFlow(HttpServletRequest request) {
		ResultMsg msg = null;
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.xzfyAcceptFlow(param);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: xzfyAccendInit
	 * @Description: 行政复议受理决定：返回受理决定grid页面
	 * @author ztt
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 */
	@RequestMapping({"/xzfyAccendInit.do"})
	public ModelAndView xzfyAccendInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/casebaseinfoaccend_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryXzfyAccendList
	 * @Description: 行政复议受理决定：行政复议受理决定grid页面列表查询
	 * @author ztt
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryXzfyAccendList.do")
	@ResponseBody
	public EasyUITotalResult queryXzfyAccendList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = casebaseinfoService.queryXzfyAccendList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: xzfyAcceptEdit
	 * @Description: 行政复议受理决定：返回行政复议处理页面
	 * @author ztt
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 */
	@RequestMapping({"/xzfyAccendEdit.do"})
	public ModelAndView xzfyAccendEdit(HttpServletRequest request) { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/casebaseinfoaccend_form");
		
		// 案件ID
		String caseid  = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		
		// 案件基本信息
		Casebaseinfo casebaseinfo = casebaseinfoService.queryCasebaseinfoByCaseid(caseid);
		if(casebaseinfo != null)
		{
			mav.addObject("casebaseinfo", casebaseinfo);
		}
		
		// 第三人信息
		String thirdnames = thirdbaseinfoService.queryThirdNamesByCaseid(caseid);
		mav.addObject("thirdnames", thirdnames);
		
		// 受理依据
		List<JSONObject> accbasis = casebaseinfoService.queryAccbasisByCaseid(caseid);
		if(null != accbasis && !accbasis.isEmpty())
		{
			mav.addObject("accbasis", accbasis.get(0));
		}
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				GCC.PROBASEINFO_OPTTYPE_PROCESSED, new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCEPT));
		if(agent != null && null != agent.getRemark())
		{
			mav.addObject("agentRemark", agent.getRemark());
		}
		else
		{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				GCC.PROBASEINFO_OPTTYPE_PROCESSED, new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCSECTION));
		if(section != null && null != section.getRemark())
		{
			mav.addObject("sectionRemark", section.getRemark());
		}
		else
		{
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				GCC.PROBASEINFO_OPTTYPE_PROCESSED, new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCORGAN));
		if(organ != null && null != organ.getRemark())
		{
			mav.addObject("organRemark", organ.getRemark());
		}
		else
		{
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				GCC.PROBASEINFO_OPTTYPE_PROCESSED, new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCOFFICE));
		if(office != null && null != office.getRemark())
		{
			mav.addObject("officeRemark", office.getRemark());
		}
		else
		{
			mav.addObject("officeRemark", "");
		}
		
		//查询受理结果
		Probaseinfo end = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCEND));
		if(end != null && null != end.getResult())
		{
			mav.addObject("result", end.getResult());
			mav.addObject("remark", end.getRemark());
		}
		else
		{
			mav.addObject("result", "");
			mav.addObject("remark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: xzfyAccendSaveEdit
	 * @Description: 行政复议受理决定：保存决定处理
	 * @author ztt
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/xzfyAccendSaveEdit.do")
	@ResponseBody
	public ResultMsg xzfyAccendSaveEdit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String caseid = (String)param.get("caseid");
		try {
			JSONObject remarks = new JSONObject();
			//查询受理承办人意见
			Probaseinfo agent = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
					GCC.PROBASEINFO_OPTTYPE_PROCESSED, new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCEPT));
			if(agent != null && null != agent.getRemark())
			{
				remarks.put("agentRemark", agent.getRemark());
			}
			else
			{
				remarks.put("agentRemark", "");
			}
			
			//查询受理科室负责人意见
			Probaseinfo section = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
					GCC.PROBASEINFO_OPTTYPE_PROCESSED, new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCSECTION));
			if(section != null && null != section.getRemark())
			{
				remarks.put("sectionRemark", section.getRemark());
			}
			else
			{
				remarks.put("sectionRemark", "");
			}	
			
			//查询受理机构负责人意见
			Probaseinfo organ = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
					GCC.PROBASEINFO_OPTTYPE_PROCESSED, new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCORGAN));
			if(organ != null && null != organ.getRemark())
			{
				remarks.put("organRemark", organ.getRemark());
			}
			else
			{
				remarks.put("organRemark", "");
			}	
			
			//查询受理机关负责人意见
			Probaseinfo office = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
					GCC.PROBASEINFO_OPTTYPE_PROCESSED, new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCOFFICE));
			if(office != null && null != office.getRemark())
			{
				remarks.put("officeRemark", office.getRemark());
			}
			else
			{
				remarks.put("officeRemark", "");
			}
			casebaseinfoService.updateXzfyAccendByCaseid(param, remarks);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: xzfyAccendDetail
	 * @Description: 行政复议受理决定：详情
	 * @author ztt
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 */
	@RequestMapping({"/xzfyAccendDetail.do"})
	public ModelAndView xzfyAccendDetail(HttpServletRequest request) { 
		
		// 返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/casebaseinfoaccend_detail");
		
		// 案件ID
		String caseid  = request.getParameter("caseid");
		Casebaseinfo casebaseinfo = casebaseinfoService.queryCasebaseinfoByCaseid(caseid);
		//查询受理结果
		Probaseinfo end = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCEND));
		if (null != end) {
			mav.addObject("result", end.getResult());
			mav.addObject("sendunit", end.getSendunit());
			mav.addObject("remark", end.getRemark());
			String result_mc = casebaseinfoService.getNameByCode("SYS_YW_DICENUMITEM", "PUB_PROBASEINFO_RESULT", end.getResult());
			mav.addObject("result_mc", result_mc);
			String isgreat_mc = casebaseinfoService.getNameByCode("SYS_DICENUMITEM", "SYS_TRUE_FALSE", casebaseinfo.getIsgreat());
			mav.addObject("isgreat_mc", isgreat_mc);
		}
		else {
			mav.addObject("result", "");
			mav.addObject("sendunit", "");
			mav.addObject("remark", "");
			mav.addObject("result_mc", "");
			mav.addObject("isgreat_mc", "");
		}
		
		return mav;
	}
	
	/**
	 * @Title: xzfyAccendFlow
	 * @Description: 行政复议受理决定：送审
	 * @author ztt
	 * @date 2016年11月2日 
	 * @param menuid
	 * @param activityId
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/xzfyAccendFlow.do")
	@ResponseBody
	public ResultMsg xzfyAccendFlow(String caseid) {
		ResultMsg msg = null;
		try {
			casebaseinfoService.xzfyAccendFlow(caseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: xzfyReviewInit
	 * @Description: 行政复议审理：返回审查grid页面
	 * @author ztt
	 * @date 2016年11月3日
	 * @param request
	 * @return
	 */
	@RequestMapping({"/xzfyReviewInit.do"})
	public ModelAndView xzfyReviewInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/casebaseinforeview_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		//节点ID
		String nodeid  = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	/**
	 * @Title: queryXzfyReviewList
	 * @Description: 行政复议审理：查询行政复议审查列表
	 * @author ztt
	 * @date 2016年11月3日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryXzfyReviewList.do")
	@ResponseBody
	public EasyUITotalResult queryXzfyReviewList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = casebaseinfoService.queryXzfyReviewList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: xzfyReviewEdit
	 * @Description: 行政复议审理：返回行政复议审查页面，相关页面参数值
	 * @author ztt
	 * @date 2016年11月3日
	 * @param request
	 * @return
	 * @throws AppException 
	 */
	@RequestMapping({"/xzfyReviewEdit.do"})
	public ModelAndView xzfyReviewEdit(HttpServletRequest request) throws AppException { 
		
		//案件ID
		String caseid  = request.getParameter("caseid");
		//流程类型
		String protype  = request.getParameter("protype");
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/casebaseinforeview_form");
		mav.addObject("caseid", caseid);
		
		//根据案件ID查询案件信息
		CasebaseinfoVo casebaseinfo = casebaseinfoService.queryXzfyreqByCaseid(caseid);
		mav.addObject("casebaseinfo", casebaseinfo);
				
		//返回第三人信息
		ThirdbaseinfoVo thirdbaseinfo = thirdbaseinfoService.queryThirdByCaseid(caseid);
		if(thirdbaseinfo != null){
			mav.addObject("thirdbaseinfo", thirdbaseinfo);
		} else {
			mav.addObject("thirdbaseinfo", "");
		}
		
		//查询审理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByCaseid(caseid, protype,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_HEAR));
		if(agent != null && null != agent.getRemark())
		{
			mav.addObject("agentRemark", agent.getRemark());
			
			// 非审理流程，审结结果value值拼装n
			String result = agent.getResult();
			if (!GCC.PROBASEINFO_PROTYPE_XZFYAUDIT.equals(agent.getProtype())) {
				result = "n" + result;
			}
			mav.addObject("result", result);
		}
		else
		{
			mav.addObject("agentRemark", "");
			mav.addObject("result", "");
		}
		
		//查询审理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByCaseid(caseid, protype, 
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_HEARSECTION));
		if(section != null && null != section.getRemark())
		{
			mav.addObject("sectionRemark", section.getRemark());
		}
		else
		{
			mav.addObject("sectionRemark", "");
		}	
		
		//查询审理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByCaseid(caseid, protype,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_HEARORGAN));
		if(organ != null && null != organ.getRemark())
		{
			mav.addObject("organRemark", organ.getRemark());
		}
		else
		{
			mav.addObject("organRemark", "");
		}	
		
		//查询审理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByCaseid(caseid, protype,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_HEAROFFICE));
		if(office != null && null != office.getRemark())
		{
			mav.addObject("officeRemark", office.getRemark());
		}
		else
		{
			mav.addObject("officeRemark", "");
		}
				
		return mav;
	}
	
	/**
	 * @Title: xzfyReviewSaveEdit
	 * @Description: 行政复议审理：保存行政复议审查信息
	 * @author ztt
	 * @date 2016年11月3日
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/xzfyReviewSaveEdit.do")
	@ResponseBody
	public ResultMsg xzfyReviewSaveEdit(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.updateXzfyReviewByCaseid(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: xzfyReviewDetail
	 * @Description: 行政复议审理：详情
	 * @author ztt
	 * @date 2016年11月3日
	 * @param request
	 * @return
	 * @throws AppException 
	 */
	@RequestMapping({"/xzfyReviewDetail.do"})
	public ModelAndView xzfyReviewDetail(HttpServletRequest request) throws AppException{ 
		
		//案件ID
		String caseid  = request.getParameter("caseid");
		//流程类型
		String protype  = request.getParameter("protype");
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/casebaseinforeview_detail");
		mav.addObject("caseid", caseid);
		
		//根据案件ID查询案件信息
		CasebaseinfoVo casebaseinfo = casebaseinfoService.queryXzfyreqByCaseid(caseid);
		mav.addObject("casebaseinfo", casebaseinfo);
				
		//返回第三人信息
		ThirdbaseinfoVo thirdbaseinfo = thirdbaseinfoService.queryThirdByCaseid(caseid);
		if(thirdbaseinfo != null){
			mav.addObject("thirdbaseinfo", thirdbaseinfo);
		} else {
			mav.addObject("thirdbaseinfo", "");
		}
		String result = "";
		//查询审理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByCaseid(caseid, protype,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_HEAR));
		if(null != agent)
		{
			mav.addObject("agentRemark", agent.getRemark());
			// 非审理流程，审结结果value值拼装n
			result = agent.getResult();
			if (!GCC.PROBASEINFO_PROTYPE_XZFYAUDIT.equals(agent.getProtype())) {
				result = "n" + result;
			}
			mav.addObject("result", result);
		}
		else
		{
			mav.addObject("agentRemark", "");
		}
		
		//查询审理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByCaseid(caseid, protype, 
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_HEARSECTION));
		if(null != section)
		{
			mav.addObject("sectionRemark", section.getRemark());
		}
		else
		{
			mav.addObject("sectionRemark", "");
		}	
		
		//查询审理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByCaseid(caseid, protype,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_HEARORGAN));
		if(null != organ)
		{
			mav.addObject("organRemark", organ.getRemark());
		}
		else
		{
			mav.addObject("organRemark", "");
		}	
		
		//查询审理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByCaseid(caseid, protype,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_HEAROFFICE));
		if(null != office)
		{
			mav.addObject("officeRemark", office.getRemark());
		}
		else
		{
			mav.addObject("officeRemark", "");
		}
		
		// 审结结果
		List<JSONObject> probaseinfoList = probaseinfoService.queryProbaseinfo(caseid, protype,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_HEAR), result);
		if(null != probaseinfoList && !probaseinfoList.isEmpty())
		{
			mav.addObject("result_mc", probaseinfoList.get(0).getString("result_mc"));
		}
		else
		{
			mav.addObject("result_mc", "");
		}
				
		return mav;
	}
	
	/**
	 * @Title: xzfyReviewFlow
	 * @Description: 行政复议审理：发送
	 * @author ztt
	 * @date 2016年11月3日
	 * @param menuid
	 * @param protype
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/xzfyReviewFlow.do")
	@ResponseBody
	public ResultMsg xzfyReviewFlow(String caseid, String protype) {
		
		ResultMsg msg = null;

		try {
			casebaseinfoService.xzfyReviewFlow(caseid, protype);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: xzfyDecisionInit
	 * @Description: 复议审理决定-返回复议决定grid页面
	 * @author ztt
	 * @date 2016年11月3日
	 * @param request
	 * @return
	 */
	@RequestMapping({"/xzfyDecisionInit.do"})
	public ModelAndView xzfyDecisionInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/casebaseinfodecision_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryXzfyDecisionList
	 * @Description: 复议审理决定-查询复议决定列表
	 * @author ztt
	 * @date 2016年11月3日 
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryXzfyDecisionList.do")
	@ResponseBody
	public EasyUITotalResult queryXzfyDecisionList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = casebaseinfoService.queryXzfyDecisionList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: xzfyDecisionEdit
	 * @Description: 复议审理决定-返回复议决定页面，相关页面参数值
	 * @author ztt
	 * @date 2016年11月3日
	 * @param request
	 * @return
	 * @throws AppException 
	 */
	@RequestMapping({"/xzfyDecisionEdit.do"})
	public ModelAndView xzfyDecisionEdit(HttpServletRequest request) throws  AppException{ 
		
		//案件ID
		String caseid  = request.getParameter("caseid");
		
		ModelAndView mav = new ModelAndView("aros/xzfy/casebaseinfodecision_form");
		mav.addObject("caseid", caseid);
		
		//根据案件ID查询案件信息
		CasebaseinfoVo casebaseinfo = casebaseinfoService.queryXzfyreqByCaseid(caseid);
		mav.addObject("casebaseinfo", casebaseinfo);
		
		// 第三人信息
		String  thirdnames = thirdbaseinfoService.queryThirdNamesByCaseid(caseid);
		mav.addObject("thirdnames", thirdnames);
		
		//流程类型
		String protype  = casebaseinfo.getProtype();
		mav.addObject("protype", protype);
		
		String result = "";
		//查询审理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByCaseid(caseid, protype,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_SLUNDERTAKERHEAR));
		if(agent != null && null != agent.getRemark())
		{
			mav.addObject("agentRemark", agent.getRemark());
			result = agent.getResult();
		}
		else
		{
			mav.addObject("agentRemark", "");
		}
		
		//查询审理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByCaseid(caseid, protype, 
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_SECTIONREMARKHEAR));
		if(section != null && null != section.getRemark())
		{
			mav.addObject("sectionRemark", section.getRemark());
		}
		else
		{
			mav.addObject("sectionRemark", "");
		}	
		
		//查询审理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByCaseid(caseid, protype,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ORGANREMARKHEAR));
		if(organ != null && null != organ.getRemark())
		{
			mav.addObject("organRemark", organ.getRemark());
		}
		else
		{
			mav.addObject("organRemark", "");
		}	
		
		//查询审理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByCaseid(caseid, protype,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_OFFICEREMARKHEAR));
		if(office != null && null != office.getRemark())
		{
			mav.addObject("officeRemark", office.getRemark());
		}
		else
		{
			mav.addObject("officeRemark", "");
		}
		
		
		//审批结果
		Probaseinfo info = probaseinfoService.queryProbaseinfoByCaseid(caseid, protype,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_DECISION));
		if(info != null && null != info.getResult())
		{
			mav.addObject("result", info.getResult());
			mav.addObject("remark", info.getRemark());
		}
		else
		{
			if (GCC.PROBASEINFO_PROTYPE_XZFYAUDIT.equals(protype)) {
				mav.addObject("result", result);
				mav.addObject("remark", "");
			}
			else {
				mav.addObject("result", "");
				mav.addObject("remark", "");
			}
		}
		
		if (!GCC.PROBASEINFO_PROTYPE_XZFYAUDIT.equals(protype)) {
			result = "n" + result;
		}
		
		// 审结结果
		List<JSONObject> probaseinfoList = probaseinfoService.queryProbaseinfo(caseid, protype,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_DECISION), result);
		if(probaseinfoList != null && !probaseinfoList.isEmpty())
		{
			mav.addObject("result_mc", probaseinfoList.get(0).getString("result_mc"));
		}
		else
		{
			mav.addObject("result_mc", "");
		}
				
		return mav;
	}
	
	/**
	 * @Title: xzfyDecisionSaveEdit
	 * @Description: 复议审理决定-保存复议决定信息
	 * @author ztt
	 * @date 2016年11月3日 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/xzfyDecisionSaveEdit.do")
	@ResponseBody
	public ResultMsg xzfyDecisionSaveEdit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String caseid = (String)param.get("caseid");
		try {
			JSONObject remarks = new JSONObject();
			//查询审理承办人意见
			Probaseinfo agent = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
					GCC.PROBASEINFO_OPTTYPE_PROCESSED, new BigDecimal(GCC.PRONODEBASEINFO_NODEID_DECISION));
			if(agent != null && null != agent.getRemark())
			{
				remarks.put("agentRemark", agent.getRemark());
			}
			else
			{
				remarks.put("agentRemark", "");
			}
			
			//查询审理科室负责人意见
			Probaseinfo section = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
					GCC.PROBASEINFO_OPTTYPE_PROCESSED, new BigDecimal(GCC.PRONODEBASEINFO_NODEID_SECTIONREMARKHEAR));
			if(section != null && null != section.getRemark())
			{
				remarks.put("sectionRemark", section.getRemark());
			}
			else
			{
				remarks.put("sectionRemark", "");
			}	
			
			//查询审理机构负责人意见
			Probaseinfo organ = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
					GCC.PROBASEINFO_OPTTYPE_PROCESSED, new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ORGANREMARKHEAR));
			if(organ != null && null != organ.getRemark())
			{
				remarks.put("organRemark", organ.getRemark());
			}
			else
			{
				remarks.put("organRemark", "");
			}	
			
			//查询审理机关负责人意见
			Probaseinfo office = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
					GCC.PROBASEINFO_OPTTYPE_PROCESSED, new BigDecimal(GCC.PRONODEBASEINFO_NODEID_OFFICEREMARKHEAR));
			if(office != null && null != office.getRemark())
			{
				remarks.put("officeRemark", office.getRemark());
			}
			else
			{
				remarks.put("officeRemark", "");
			}
			casebaseinfoService.updateXzfyDecisionByCaseid(param, remarks);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: xzfyDecisionDetail
	 * @Description: 复议审理决定：详情
	 * @author ztt
	 * @date 2016年11月3日
	 * @param request
	 * @return
	 * @throws AppException 
	 */
	@RequestMapping({"/xzfyDecisionDetail.do"})
	public ModelAndView xzfyDecisionDetail(HttpServletRequest request) throws  AppException{ 
		
		//案件ID
		String caseid  = request.getParameter("caseid");
		
		ModelAndView mav = new ModelAndView("aros/xzfy/casebaseinfodecision_detail");
		
		// 根据案件ID查询案件信息
		CasebaseinfoVo casebaseinfo = casebaseinfoService.queryXzfyreqByCaseid(caseid);
		// 流程类型
		String protype  = casebaseinfo.getProtype();
		// 是否重大案件
		String isgreat_mc  = casebaseinfoService.getNameByCode("SYS_DICENUMITEM", "SYS_TRUE_FALSE", casebaseinfo.getIsgreat());
		mav.addObject("isgreat_mc", isgreat_mc);
		
		String result = "";
		//审批结果
		Probaseinfo info = probaseinfoService.queryProbaseinfoByCaseid(caseid, protype,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_DECISION));
		if(info != null && null != info.getResult())
		{
			mav.addObject("result", info.getResult());
			mav.addObject("result_mc", info.getResult());
			mav.addObject("remark", info.getRemark());
		}
		else
		{
			if (GCC.PROBASEINFO_PROTYPE_XZFYAUDIT.equals(protype)) {
				mav.addObject("result", result);
				mav.addObject("remark", "");
			}
			else {
				mav.addObject("result", "");
				mav.addObject("remark", "");
			}
		}
		
		// 审批结果
		String result_mc = "";
		List<JSONObject> probaseinfoList = probaseinfoService.queryProbaseinfo(caseid, protype,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_HEAREND), result);
		if(null != probaseinfoList && !probaseinfoList.isEmpty())
		{
			if (GCC.PROBASEINFO_PROTYPE_XZFYAUDIT.equals(protype)) {
				result_mc = casebaseinfoService.getNameByCode("SYS_YW_DICENUMITEM", "HEARRESULT", probaseinfoList.get(0).getString("result"));
			} else {
				result_mc = casebaseinfoService.getNameByCode("SYS_YW_DICENUMITEM", "AUDITRESULT", probaseinfoList.get(0).getString("result"));
			}
		}
		
		mav.addObject("result_mc", result_mc);
				
		return mav;
	}
	
	/**
	 * @Title: xzfyDecisionFlow
	 * @Description: 行政复议审理决定：发送
	 * @author ztt
	 * @date 2016年11月3日
	 * @param menuid
	 * @param activityId
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/xzfyDecisionFlow.do")
	@ResponseBody
	public ResultMsg xzfyDecisionFlow(String caseid) {
		
		ResultMsg msg = null;

		try {
			
			casebaseinfoService.xzfyDecisionFlow(caseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: textProductionInit
	 * @Description: 返回文书制作页面（包含受理和审理模块）
	 * @author ybb
	 * @date 2016年11月14日 下午3:30:49
	 * @param request
	 * @return
	 */
	@RequestMapping({"/textProductionInit.do"})
	public ModelAndView textProductionInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/textproduction_init");
		
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		//节点ID
		String nodeid  = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	/**
	 * @Title: queryTextProductionList
	 * @Description: 文书制作（包含受理和审理模块）
	 * @author ybb
	 * @date 2016年11月14日 下午2:43:16
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryTextProductionList.do")
	@ResponseBody
	public EasyUITotalResult queryTextProductionList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = casebaseinfoService.queryXzfyAcceptList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: textProductionEdit
	 * @Description: 打开文书制作页面
	 * @author ztt
	 * @date 2016年11月15日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/textProductionEdit.do")
	public ModelAndView textProductionEdit(HttpServletRequest request) throws AppException {
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/textproduction_form");
		// 案件ID
		String caseid  = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		// 流程类型
		String protype  = request.getParameter("protype");
		mav.addObject("protype", protype);
		// 流程节点ID
		String nodeid  = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		return mav;
	}
	
	/**
	 * @Title: textProductionDetail
	 * @Description: 文书制作：详情
	 * @author ztt
	 * @date 2016年11月23日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/textProductionDetail.do")
	public ModelAndView textProductionDetail(HttpServletRequest request) throws AppException {
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/textproduction_detail");
		// 案件ID
		String caseid  = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		// 流程节点ID
		String nodeid  = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		// 流程类型
		String protype  = request.getParameter("protype");
		mav.addObject("protype", protype);
		return mav;
	}
	
	/**
	 * @Title: queryNoticeList
	 * @Description: 行政复议文书制作：查询已有文书列表
	 * @author ztt
	 * @date 2016年11月15日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryNoticeList.do")
	@ResponseBody
	public EasyUITotalResult queryNoticeList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = casebaseinfoService.queryNoticeList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: noticeInfoAdd
	 * @Description: 行政复议文书制作：新增
	 * @author ztt
	 * @date 2016年11月15日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/noticeInfoAdd.do")
	public ModelAndView noticeInfoAdd(HttpServletRequest request) throws AppException {
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/textproduction_add");
		return mav;
	}
	
	/**
	 * @Title: queryNoticeTmpList
	 * @Description: 行政复议文书制作：查询文书模板列表
	 * @author ztt
	 * @date 2016年11月15日
	 * @param request
	 * @return
	 * @throws AppException	
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryNoticeTmpList.do")
	@ResponseBody
	public EasyUITotalResult queryNoticeTmpList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = casebaseinfoService.queryNoticeTmpList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: getClobContentVal
	 * @Description: 行政复议文书制作：获取文书、文书送达内容
	 * @author ztt
	 * @date 2016年11月17日
	 * @return
	 */
	@RequestMapping("getClobContentVal.do")
	@ResponseBody
	public String getClobContentVal(String tempid, String caseid, String tableFlag)
	{  
		String contentStr = casebaseinfoService.getClobContentVal(tempid, caseid, tableFlag);
		return contentStr;
	}
	
	/**
	 * @Title: getClobContentValue
	 * @Description: 行政复议文书制作：获取回访单、备考表内容
	 * @author ztt
	 * @date 2016年11月22日
	 * @return
	 */
	@RequestMapping("getClobContentValue.do")
	@ResponseBody
	public String getClobContentValue(String caseid, String nodeid, String protype, String typeFlag)
	{  
		String contentStr = casebaseinfoService.getClobContentValue(caseid, nodeid, protype, typeFlag);
		return contentStr;
	}
	
	/**
	 * @Title: getContentsForDetail
	 * @Description: 行政复议文书制作：获取文书、文书送达内容
	 * @author ztt
	 * @date 2016年11月23日
	 * @return
	 */
	@RequestMapping("getContentsForDetail.do")
	@ResponseBody
	public String getContentsForDetail(String tempid, String tableFlag)
	{  
		String contentStr = casebaseinfoService.getContentsForDetail(tempid, tableFlag);
		return contentStr;
	}
	
	/**
	 * @Title: noticeInfoSave
	 * @Description: 行政复议文书制作：保存
	 * @author ztt
	 * @date 2016年11月17日
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/noticeInfoSave.do")
	@ResponseBody
	public ResultMsg noticeInfoSave(HttpServletRequest request){
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.updateNoticeInfo(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: xzfyNoticeFlow
	 * @Description: 行政复议文书制作：发送
	 * @author ztt
	 * @date 2016年11月2日
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/xzfyNoticeFlow.do")
	@ResponseBody
	public ResultMsg xzfyNoticeFlow(String caseid) {
		ResultMsg msg = null;
		try {
			casebaseinfoService.xzfyNoticeFlow(caseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: noticeInfoDelete
	 * @Description: 行政复议文书制作：删除
	 * @author ztt
	 * @date 2016年11月17日
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/noticeInfoDelete.do")
	@ResponseBody
	public ResultMsg noticeInfoDelete(HttpServletRequest request){
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.deleteNoticeInfoByCaseid(param);
			msg = new ResultMsg(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: noticeDownload
	 * @Description: 行政复议文书制作：已有文书下载
	 * @author ztt
	 * @date 2016年11月21日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/noticeDownload.do")
	@ResponseBody
	public void noticeDownload(HttpServletRequest request, HttpServletResponse response) throws AppException {
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		Map<String, Object> rMap = casebaseinfoService.noticeDownload(param);
		File tmpFile= (File)rMap.get("file");
		InputStream ins = null;
		BufferedInputStream bins = null;
		OutputStream outs = null;
		BufferedOutputStream bouts= null;
		try {
			ins = new FileInputStream(tmpFile);//构造一个读取文件的IO流对象
			String filename = (String)rMap.get("fileName") + ".doc";
            bins=new BufferedInputStream(ins);//放到缓冲流里面
            response.setContentType("application/octet-stream");
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {  
         	 filename = java.net.URLEncoder.encode(filename, "UTF-8");    
                 filename = StringUtils.replace(filename, "+", "%20");//替换空格    
            } else {  
                filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");  
            } 
            
            response.setHeader("Content-Disposition", "attachment; filename=" +filename);
            outs=response.getOutputStream();//获取文件输出IO流
            bouts=new BufferedOutputStream(outs);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            //开始向网络传输文件流
            while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {
                bouts.write(buffer, 0, bytesRead);
            }
            bouts.flush();//这里一定要调用flush()方法
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(ins!=null){
						ins.close();
				}
				if(bins!=null){
					bins.close();
				}
				if(outs!=null){
					outs.close();
				}
				if(bouts!=null){
					
					bouts.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			tmpFile.delete();
		}
	}
	
	/**
	 * @Title: documentDeliveryInit
	 * @Description: 返回文书送达页面（包含受理和审理模块）
	 * @author ybb
	 * @date 2016年11月14日15:39:39
	 * @param request
	 * @return
	 */
	@RequestMapping({"/documentDeliveryInit.do"})
	public ModelAndView documentDeliveryInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/documentdelivery_init");
		
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		//节点ID
		String nodeid  = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		//案件ID
		String caseid  = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		
		return mav;
	}
	
	/**
	 * @Title: queryDocumentDeliveryList
	 * @Description: 文书送达（包含受理和审理模块）
	 * @author ybb
	 * @date 2016年11月14日 下午2:55:00
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDocumentDeliveryList.do")
	@ResponseBody
	public EasyUITotalResult queryDocumentDeliveryList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = casebaseinfoService.queryXzfyAcceptList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 文书送达编辑界面
	 * @Title: documentDelivery
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/documentDeliveryEdit.do")
	public ModelAndView documentDeliveryEdit(HttpServletRequest request) throws AppException {
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/documentDelivery_form");
		// 案件ID
		String caseid  = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		// 节点ID
		String nodeid  = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		// 流程类型
		String protype  = request.getParameter("protype");
		mav.addObject("protype", protype);
		return mav;
	}
	
	/**
	 * @Description: 文书送达：详情
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/documentDeliveryDetail.do")
	public ModelAndView documentDeliveryDetail(HttpServletRequest request) throws AppException {
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/documentDelivery_detail");
		// 案件ID
		String caseid  = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		// 流程节点ID
		String nodeid  = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		// 流程类型
		String protype  = request.getParameter("protype");
		mav.addObject("protype", protype);
		return mav;
	}
	
	/**
	 * @Title: documentDeliverySaveEdit
	 * @Description: 行政复议文书送达：保存
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/documentDeliverySaveEdit.do")
	@ResponseBody
	public ResultMsg documentDeliverySaveEdit(HttpServletRequest request){
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.updateDelivery(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Description: 行政复议文书制作：发送
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/xzfyDeliveryFlow.do")
	@ResponseBody
	public ResultMsg xzfyDeliveryFlow(String caseid) {
		ResultMsg msg = null;
		try {
			casebaseinfoService.xzfyDeliveryFlow(caseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: documentReviewInit
	 * @Description: 返回收回访单页面（包含受理和审理模块）
	 * @author ybb
	 * @date 2016年11月14日15:45:04
	 * @param request
	 * @return
	 */
	@RequestMapping({"/documentReviewInit.do"})
	public ModelAndView documentReviewInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/documentreview_init");
		
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		//节点ID
		String nodeid  = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	
	/**
	 * @Title: queryDocumentReviewList
	 * @Description: 回访单（包含受理和审理模块）
	 * @author ybb
	 * @date 2016年11月14日 下午3:01:19
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDocumentReviewList.do")
	@ResponseBody
	public EasyUITotalResult queryDocumentReviewList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = casebaseinfoService.queryXzfyAcceptList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: documentDelivery
	 * @Description: 打开回访单页面
	 * @author ztt
	 * @date 2016年11月22日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/documentReviewEdit.do")
	public ModelAndView documentReviewEdit(HttpServletRequest request) throws AppException {
		// 返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/documentReview_form");
		// 案件ID
		String caseid  = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		// 节点ID
		String nodeid  = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		// 流程类型
		String protype  = request.getParameter("protype");
		mav.addObject("protype", protype);
		List<JSONObject> review = casebaseinfoService.queryNoticeInfoByCaseid(caseid, nodeid, protype);
		// 回访单信息
		if (!review.isEmpty()) {
			mav.addObject("review", review.get(0));
		}
		return mav;
	}
	
	/**
	 * @Title: documentReviewDetail
	 * @Description: 回访单：详情
	 * @author ztt
	 * @date 2016年11月23日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/documentReviewDetail.do")
	public ModelAndView documentReviewDetail(HttpServletRequest request) throws AppException {
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/documentReview_detail");
		// 案件ID
		String caseid  = request.getParameter("caseid");
		// 节点ID
		String nodeid  = request.getParameter("nodeid");
		// 流程类型
		String protype  = request.getParameter("protype");
		// 回访单信息
		List<JSONObject> info = casebaseinfoService.queryNoticeInfoByCaseid(caseid, nodeid, protype);
		String id = "";
		if (!info.isEmpty()) {
			id = info.get(0).getString("id");
		}
		mav.addObject("id", id);
		return mav;
	}
	
	/**
	 * @Title: documentReviewSaveEdit
	 * @Description: 回访单：保存
	 * @author ztt
	 * @date 2016年11月22日
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/documentReviewSaveEdit.do")
	@ResponseBody
	public ResultMsg documentReviewSaveEdit(HttpServletRequest request){
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.updateReview(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: xzfyDocReviewFlow
	 * @Description: 回访单：发送
	 * @author ztt
	 * @date 2016年11月22日
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/xzfyDocReviewFlow.do")
	@ResponseBody
	public ResultMsg xzfyDocReviewFlow(String caseid) {
		ResultMsg msg = null;
		try {
			casebaseinfoService.xzfyDocReviewFlow(caseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: summarySheetInit
	 * @Description: 返回收备考表页面（包含受理和审理模块）
	 * @author ybb
	 * @date 2016年11月14日17:34:06
	 * @param request
	 * @return
	 */
	@RequestMapping({"/summarySheetInit.do"})
	public ModelAndView summarySheetInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/summarysheet_init");
		
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		//节点ID
		String nodeid  = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	/**
	 * @Title: querySummarySheetList
	 * @Description: 备考表（包含受理和审理模块）
	 * @author ybb
	 * @date 2016年11月14日17:34:42
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/querySummarySheetList.do")
	@ResponseBody
	public EasyUITotalResult querySummarySheetList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = casebaseinfoService.queryXzfyAcceptList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: summaryEdit
	 * @Description: 打开备考表页面
	 * @author ztt
	 * @date 2016年11月22日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/summaryEdit.do")
	public ModelAndView summaryEdit(HttpServletRequest request) throws AppException {
		// 返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/summarysheet_form");
		// 案件ID
		String caseid  = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		// 节点ID
		String nodeid  = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		// 流程类型
		String protype  = request.getParameter("protype");
		mav.addObject("protype", protype);
		List<JSONObject> summary = casebaseinfoService.queryNoticeInfoByCaseid(caseid, nodeid, protype);
		// 备考表信息
		if (!summary.isEmpty()) {
			mav.addObject("summary", summary.get(0));
		}
		return mav;
	}
	
	/**
	 * @Title: summaryDetail
	 * @Description:备考表：详情
	 * @author ztt
	 * @date 2016年11月23日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/summaryDetail.do")
	public ModelAndView summaryDetail(HttpServletRequest request) throws AppException {
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/summarysheet_detail");
		// 案件ID
		String caseid  = request.getParameter("caseid");
		// 节点ID
		String nodeid  = request.getParameter("nodeid");
		// 流程类型
		String protype  = request.getParameter("protype");
		// 备考表信息
		List<JSONObject> info = casebaseinfoService.queryNoticeInfoByCaseid(caseid, nodeid, protype);
		String id = "";
		if (!info.isEmpty()) {
			id = info.get(0).getString("id");
		}
		mav.addObject("id", id);
		return mav;
	}
	
	/**
	 * @Title: summarySaveEdit
	 * @Description: 备考表：保存
	 * @author ztt
	 * @date 2016年11月22日
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/summarySaveEdit.do")
	@ResponseBody
	public ResultMsg summarySaveEdit(HttpServletRequest request){
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.updateSummary(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: xzfySummaryFlow
	 * @Description: 备考表：发送
	 * @author ztt
	 * @date 2016年11月22日
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/xzfySummaryFlow.do")
	@ResponseBody
	public ResultMsg xzfySummaryFlow(String caseid) {
		ResultMsg msg = null;
		try {
			casebaseinfoService.xzfySummaryFlow(caseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: xzfyReturn
	 * @Description: 复议回退：回退受理信息
	 * @author ztt
	 * @date 2016年11月14日
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/xzfyReturn.do")
	@ResponseBody
	public ResultMsg xzfyReturn(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.updateXzfyReturnByCaseid(param);
			msg = new ResultMsg(true, "回退成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: queryXzfyProcessList
	 * @Description: 专家小组管理：查询完结的行政复议申请列表
	 * @author ybb
	 * @date 2016年8月16日 上午11:03:57
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryXzfyProcessList.do")
	@ResponseBody
	public EasyUITotalResult queryXzfyProcessList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = casebaseinfoService.queryXzfyProcessList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	
	/**
	 * @Title: updateOpttypeByCaseid
	 * @Description: 案件接收处理
	 * @author ybb
	 * @date 2016年8月16日 下午1:26:59
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
			casebaseinfoService.updateOpttypeByCaseid(param);
			msg = new ResultMsg(true, "接收成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: xzfyViewInit
	 * @Description: 案件查询：返回行政复议查看列表页面
	 * @author ybb
	 * @date 2016年8月26日 上午10:35:02
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/xzfyViewInit.do"})
	public ModelAndView xzfyViewInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/cbiview_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryXzfyViewList
	 * @Description: 案件查询：grid列表页面查询
	 * @author ybb
	 * @date 2016年8月26日 上午10:35:57
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryXzfyViewList.do")
	@ResponseBody
	public EasyUITotalResult queryXzfyViewList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = casebaseinfoService.queryXzfyListForView(param);
		
		return EasyUITotalResult.from(ps);
	}

	/**
	 * 案件小组选择初始化
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/ajxzInit.do" })
	public ModelAndView ajxzInit(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/xzfy/ajxz_form");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
	}
	
	/**
	 * 案件小组grid
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryGroupList.do")
	@ResponseBody
	public EasyUITotalResult queryGroupList(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = casebaseinfoService.queryGroupList(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 保存案件小组信息
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping({ "/saveAjxz.do" })
	@ResponseBody
	public ResultMsg saveAjxz(HttpServletRequest request) throws ServletException {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.saveAjxz(param);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}

	/**
	 * 保存案件主题和对应专家信息
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping({ "/saveAjZt.do" })
	@ResponseBody
	public ResultMsg saveAjZt(HttpServletRequest request) throws ServletException {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.saveAjZt(param);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: sendToOtherUserInit
	 * @Description: 行政复议受理：返回转发页面
	 * @author ybb
	 * @date 2016年9月1日 下午2:00:52
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/sendToOtherUserInit.do"})
	public ModelAndView sendToOtherUserInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/sendflow_form");
		
		return mav;
	}
	
	/**
	 * @Title: queryUserListForSend
	 * @Description: 行政复议受理转发：分页查询机构列表
	 * @author ybb
	 * @date 2016年9月1日 上午11:48:07
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryUserListForSend.do")
	@ResponseBody
	public EasyUITotalResult queryUserListForSend(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		//执行查询
		PaginationSupport ps = casebaseinfoService.queryUserListForSend(param);
		
		return EasyUITotalResult.from(ps);
	} 
	
	/**
	 * @Title: queryRoleListForSend
	 * @Description: 行政复议受理转发：分页查询人员列表
	 * @author ybb
	 * @date 2016年9月1日 上午11:48:07
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryRoleListForSend.do")
	@ResponseBody
	public EasyUITotalResult queryRoleForSend(HttpServletRequest request) throws AppException {		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		//执行查询
		PaginationSupport ps = casebaseinfoService.queryRoleListForSend(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: saveCaseToOther
	 * @Description: 行政复议转发：保存转发信息
	 * @author ybb
	 * @date 2016年9月1日 下午4:00:35
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping({"/saveCaseToOther.do"})
	@ResponseBody
	public ResultMsg saveCaseToOther(HttpServletRequest request) throws ServletException{ 
		
		ResultMsg msg = null;
		
		//记录案件接收日志
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			casebaseinfoService.addSendCaseToOther(param);
			msg = new ResultMsg(true, "接收成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	/**
	 * @Title: saveCaseToOther
	 * @Description: 行政复议受理：相似案件管理
	 * @author czp
	 * @date 2016年10月14日 下午4:00:35
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/similcaseManagement_init.do"})
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/xzfy/similarcaseManagement_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		//被申请人类型
		String deftype  = request.getParameter("deftype");
		//行政管理类型
		String admtype  = request.getParameter("admtype");
		//申请复议事项类型
		String casetype  = request.getParameter("casetype");
		mv.addObject("menuid", menuid);
		mv.addObject("deftype", deftype);
		mv.addObject("admtype", admtype);
		mv.addObject("casetype", casetype);
		mv.addObject("cantAddFile", GCC.RCASEBASEINFO_PSTATE_FINISH);
		return mv;
	}
	/**
	 * @Title: querySimcaseManageList
	 * @Description: 案件查询：相似案件列表页面查询
	 * @author ybb
	 * @date 2016年8月26日 上午10:35:57
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/querySimcaseManageList.do")
	@ResponseBody
	public EasyUITotalResult querySimcaseManageList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		//执行查询
		PaginationSupport ps = casebaseinfoService.querySimcaseManageForView(param);
		
		return EasyUITotalResult.from(ps);
	}
	/**
	 * @Title: noticeview
	 * @Description: 行政复议受理：相似案件管理
	 * @author czp
	 * @date 2016年10月14日 下午4:00:35
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/notice_view.do"})
	public ModelAndView noticeview(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/xzfy/notice_view");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
	}
	
	/**
	 * @Title: querySimcaseManageList
	 * @Description: 案件查询：相似案件列表页面查询
	 * @author ybb
	 * @date 2016年8月26日 上午10:35:57
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryThirdByCaseid.do")
	@ResponseBody
	public EasyUITotalResult querythridList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		//执行查询
		PaginationSupport ps = thirdbaseinfoService.queryThirdByCaseid(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: saveCaseToOther
	 * @Description: 行政复议转发：保存转发信息
	 * @author ybb
	 * @date 2016年9月1日 下午4:00:35
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping({"/saveThirdInfo.do"})
	@ResponseBody
	public ResultMsg saveThirdInfo(HttpServletRequest request) throws ServletException{ 
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			thirdbaseinfoService.save(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: saveCaseToOther
	 * @Description: 行政复议转发：保存转发信息
	 * @author ybb
	 * @date 2016年9月1日 下午4:00:35
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping({"/deleteThirdInfo.do"})
	@ResponseBody
	public ResultMsg deleteThirdInfo(HttpServletRequest request) throws ServletException{ 
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			thirdbaseinfoService.delete(param);
			msg = new ResultMsg(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Description: 打开转送登记表
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/transregistEdit.do")
	public ModelAndView transregistEdit(HttpServletRequest request) throws AppException {
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		// 返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/documentReview_form");
		// 案件ID
		String caseid  = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		// 节点ID
		String nodeid  = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		// 流程类型
		String protype  = request.getParameter("protype");
		mav.addObject("protype", protype);
		List<JSONObject> review = casebaseinfoService.queryTransregistByCaseid(param);
		if (!review.isEmpty()) {
			mav.addObject("review", review.get(0));
		}
		return mav;
	}
	
	/**
	 * @Title: documentReviewDetail
	 * @Description: 转送登记表：详情
	 * @author ztt
	 * @date 2016年11月23日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/transregistDetail.do")
	public ModelAndView transregistDetail(HttpServletRequest request) throws AppException {

		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		// 返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/documentReview_form");
		// 案件ID
		String caseid  = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		// 节点ID
		String nodeid  = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		// 流程类型
		String protype  = request.getParameter("protype");
		mav.addObject("protype", protype);
		List<JSONObject> review = casebaseinfoService.queryTransregistByCaseid(param);
		if (!review.isEmpty()) {
			mav.addObject("review", review.get(0));
		}
		return mav;
	}
	
	/**
	 * @Title: documentReviewSaveEdit
	 * @Description: 转送登记表：保存
	 * @author ztt
	 * @date 2016年11月22日
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/transregistSaveEdit.do")
	@ResponseBody
	public ResultMsg transregistSaveEdit(HttpServletRequest request){
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.updateTransregist(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: xzfyDocReviewFlow
	 * @Description: 转送登记表：发送
	 * @author ztt
	 * @date 2016年11月22日
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/transregistFlow.do")
	@ResponseBody
	public ResultMsg transregistFlow(String caseid) {
		ResultMsg msg = null;
		try {
			casebaseinfoService.transregistFlow(caseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Description: 指定审理承办人
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/slundertakerEdit.do")
	public ModelAndView slundertakerEdit(HttpServletRequest request) throws AppException {
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		ModelAndView mav = new ModelAndView("aros/xzfy/slundertaker_form");
		String caseid = (String) param.get("caseid");
		Casebaseinfo caseInfo = casebaseinfoService.getCaseInfoByCaseId(caseid);
		mav.addObject("caseInfo", caseInfo);
		return mav;
	}
	
	/**
	 * @Description: 指定承办人
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/slundertakerDetail.do")
	public ModelAndView slundertakerDetail(HttpServletRequest request) throws AppException {
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		ModelAndView mav = new ModelAndView("aros/xzfy/slundertaker_detail");
		String caseid = (String) param.get("caseid");
		Casebaseinfo caseInfo = casebaseinfoService.getCaseInfoByCaseId(caseid);
		mav.addObject("caseInfo", caseInfo);
		return mav;
	}
	
	/**
	 * @Title: documentReviewSaveEdit
	 * @Description: 转送登记表：保存
	 * @author ztt
	 * @date 2016年11月22日
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/slundertakerSaveEdit.do")
	@ResponseBody
	public ResultMsg slundertakerSaveEdit(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.updateslundertaker(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: xzfyDocReviewFlow
	 * @Description: 转送登记表：发送
	 * @author ztt
	 * @date 2016年11月22日
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/slundertakerFlow.do")
	@ResponseBody
	public ResultMsg slundertakerFlow(String caseid) {
		ResultMsg msg = null;
		try {
			casebaseinfoService.slundertakerFlow(caseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/userListView.do")
	public ModelAndView userListView(HttpServletRequest request) {
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/user_list");
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String rolecode = (String) param.get("rolecode");
		mav.addObject("rolecode", rolecode);
		return mav;
	}
	
	/**
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
		PaginationSupport ps = thirdbaseinfoService.queryUserList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Description: 行政复议审理：返回行政复议处理页面
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/xzfyHearEdit.do"})
	public ModelAndView xzfyHearEdit(HttpServletRequest request) {
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/xzfy/casebaseinfoHear_form");
		
		// 案件ID
		String caseid  = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		
		// 案件基本信息
		Casebaseinfo casebaseinfo = casebaseinfoService.queryCasebaseinfoByCaseid(caseid);
		if(casebaseinfo != null)
		{
			mav.addObject("casebaseinfo", casebaseinfo);
		}
		
		// 第三人信息
		String  thirdnames = thirdbaseinfoService.queryThirdNamesByCaseid(caseid);
		mav.addObject("thirdnames", thirdnames);
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_SLUNDERTAKERHEAR));
		if(agent != null && null != agent.getRemark())
		{
			mav.addObject("agentRemark", agent.getRemark());
		}
		else
		{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, 
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_SECTIONREMARKHEAR));
		if(section != null && null != section.getRemark())
		{
			mav.addObject("sectionRemark", section.getRemark());
		}
		else
		{
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ORGANREMARKHEAR));
		if(organ != null && null != organ.getRemark())
		{
			mav.addObject("organRemark", organ.getRemark());
		}
		else
		{
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_OFFICEREMARKHEAR));
		if(office != null && null != office.getRemark())
		{
			mav.addObject("officeRemark", office.getRemark());
		}
		else
		{
			mav.addObject("officeRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Description: 行政复议审理：保存行政复议处理
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/xzfyHearSaveEdit.do")
	@ResponseBody
	public ResultMsg xzfyHearSaveEdit(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.updateXzfyHearByCaseid(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Description: 行政复议审理：详情
	 * @param request
	 * @return
	 */
	@RequestMapping({"/xzfyHearDetail.do"})
	public ModelAndView xzfyHearDetail(HttpServletRequest request) {
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/xzfy/casebaseinfoHear_detail");
		
		// 案件ID
		String caseid  = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		
		// 案件基本信息
		Casebaseinfo casebaseinfo = casebaseinfoService.queryCasebaseinfoByCaseid(caseid);
		if(casebaseinfo != null)
		{
			mav.addObject("casebaseinfo", casebaseinfo);
		}
		
		// 第三人信息
		String  thirdnames = thirdbaseinfoService.queryThirdNamesByCaseid(caseid);
		mav.addObject("thirdnames", thirdnames);
		
		//查询受理承办人意见
		Probaseinfo agent = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_SLUNDERTAKERHEAR));
		if(agent != null && null != agent.getRemark())
		{
			mav.addObject("agentRemark", agent.getRemark());
		}
		else
		{
			mav.addObject("agentRemark", "");
		}
		
		//查询受理科室负责人意见
		Probaseinfo section = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, 
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_SECTIONREMARKHEAR));
		if(section != null && null != section.getRemark())
		{
			mav.addObject("sectionRemark", section.getRemark());
		}
		else
		{
			mav.addObject("sectionRemark", "");
		}	
		
		//查询受理机构负责人意见
		Probaseinfo organ = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ORGANREMARKHEAR));
		if(organ != null && null != organ.getRemark())
		{
			mav.addObject("organRemark", organ.getRemark());
		}
		else
		{
			mav.addObject("organRemark", "");
		}	
		
		//查询受理机关负责人意见
		Probaseinfo office = probaseinfoService.queryProbaseinfoByCaseid(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_OFFICEREMARKHEAR));
		if(office != null && null != office.getRemark())
		{
			mav.addObject("officeRemark", office.getRemark());
		}
		else
		{
			mav.addObject("officeRemark", "");
		}
		
		return mav;
	}
	
	/**
	 * @Description: 行政复议审理：承办人->科室->机构->机关
	 * @param menuid
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/xzfyHearFlow.do")
	@ResponseBody
	public ResultMsg xzfyHearFlow(String caseid) {
		ResultMsg msg = null;	
		try {
			casebaseinfoService.xzfyHearFlow(caseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Description: 行政复议审理：返回行政复议处理页面
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/xzfyTrialEdit.do"})
	public ModelAndView xzfyTrialEdit(HttpServletRequest request) {
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/xzfy/trialbaseinfo_init");
		
		// 案件ID
		String caseid  = request.getParameter("caseid");
		String reviewtype  = request.getParameter("reviewtype");
		mav.addObject("caseid", caseid);
		mav.addObject("reviewtype", reviewtype);
		
		// 案件基本信息
		Casebaseinfo casebaseinfo = casebaseinfoService.queryCasebaseinfoByCaseid(caseid);
		if(casebaseinfo != null)
		{
			mav.addObject("casebaseinfo", casebaseinfo);
			mav.addObject("csaecode", casebaseinfo.getCsaecode());
		}
		
		return mav;
	}
	
	/**
	 * @Description: 行政复议审理：保存行政复议处理
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/xzfyTrialSaveEdit.do")
	@ResponseBody
	public ResultMsg xzfyTrialSaveEdit(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.updateXzfyTrialByCaseid(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Description: 行政复议审理：承办人->科室->机构->机关
	 * @param menuid
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/xzfyTrialFlow.do")
	@ResponseBody
	public ResultMsg xzfyTrialFlow(String caseid) {
		ResultMsg msg = null;
		try {
			casebaseinfoService.xzfyTrialFlow(caseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Description: 行政复议审理：返回行政复议处理页面
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/xzfyTrialDetail.do"})
	public ModelAndView xzfyTrialDetail(HttpServletRequest request) {
		// 返回页面路径
		ModelAndView mav = new ModelAndView( "aros/xzfy/trialbaseinfo_detail");
		
		// 案件ID
		String caseid  = request.getParameter("caseid");
		String reviewtype  = request.getParameter("reviewtype");
		mav.addObject("caseid", caseid);
		mav.addObject("reviewtype", reviewtype);
		
		// 案件基本信息
		Casebaseinfo casebaseinfo = casebaseinfoService.queryCasebaseinfoByCaseid(caseid);
		if(casebaseinfo != null)
		{
			mav.addObject("casebaseinfo", casebaseinfo);
		}
		
		return mav;
	}
	
	/**
	 * 文书送达编辑界面
	 * @Title: documentDelivery
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/callbackEdit.do")
	public ModelAndView callbackEdit(HttpServletRequest request) throws AppException {
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/callback_form");
		// 案件ID
		String caseid  = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		// 节点ID
		String nodeid  = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		// 流程类型
		String protype  = request.getParameter("protype");
		mav.addObject("protype", protype);
		return mav;
	}
	
	/**
	 * @Description: 廉政回访详情
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/callbackDetail.do")
	public ModelAndView callbackDetail(HttpServletRequest request) throws AppException {
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/callback_detail");
		// 案件ID
		String caseid  = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		// 流程节点ID
		String nodeid  = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		// 流程类型
		String protype  = request.getParameter("protype");
		mav.addObject("protype", protype);
		return mav;
	}
	
	/**
	 * @Title: documentDeliverySaveEdit
	 * @Description: 行政复议文书送达：保存
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/callbackSaveEdit.do")
	@ResponseBody
	public ResultMsg callbackSaveEdit(HttpServletRequest request){
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			casebaseinfoService.updatecallback(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Description: 行政复议文书制作：发送
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/xzfycallbackFlow.do")
	@ResponseBody
	public ResultMsg xzfycallbackFlow(String caseid) {
		ResultMsg msg = null;
		try {
			casebaseinfoService.xzfycallbackFlow(caseid);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	
	/**
	 * @Description: 结案归档
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/xzfyCaseEndNoticePlaceFlow.do")
	@ResponseBody
	public ResultMsg xzfyCaseEndNoticePlaceFlow(String caseid) {
		ResultMsg msg = null;
		try {
			casebaseinfoService.xzfyCaseEndNoticePlaceFlow(caseid);
			msg = new ResultMsg(true, AppException.getMessage("结案归档成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("结案归档失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 案件申请笔录信息
	 * @param request
	 * @return
	 */
	@RequestMapping({"/applyDetailByCase.do"})
	public ModelAndView applyDetailByCase(HttpServletRequest request){
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/sqbl/applyRecordInfo_view");
		//菜单ID
		//获取页面传递查询参数
		@SuppressWarnings("unchecked")
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		mav.addObject("info", applyRecordInfoService.applyDetailByCase(param));
		return mav;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping({"/backFlowPage.do"})
	public ModelAndView backFlowPage(HttpServletRequest request){
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzfy/backFlowPage");
		
		return mav;
	}
	
	/**
	 * @Description: 删除案件
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/deleteCase.do")
	@ResponseBody
	public ResultMsg deleteCase(String caseid) {
		ResultMsg msg = null;
		try {
			casebaseinfoService.deleteCase(caseid);
			msg = new ResultMsg(true, AppException.getMessage("删除成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败：") + e.getMessage());
		}
		return msg;
	}
}
