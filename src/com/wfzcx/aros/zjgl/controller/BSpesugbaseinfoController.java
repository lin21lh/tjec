package com.wfzcx.aros.zjgl.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.zjgl.po.BSpecialistbaseinfo;
import com.wfzcx.aros.zjgl.po.BSpesugbaseinfo;
import com.wfzcx.aros.zjgl.service.BSpecialistbaseinfoService;
import com.wfzcx.aros.zjgl.service.BSpesugbaseinfoService;

/**
 * 专家意见controller
 * @author zhaoXD
 * @data 2016年8月26日10:12:57
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/zjgl/controller/BSpesugbaseinfoController")
public class BSpesugbaseinfoController {
	
	@Autowired
	private BSpesugbaseinfoService service;
	@Autowired
	BSpecialistbaseinfoService bSpecialistbaseinfoService;
	
	@RequestMapping("/init.do")
	public ModelAndView init(HttpServletRequest request){
		ModelAndView modView = new ModelAndView("aros/zjgl/bspesugbaseinfo_init");
		String menuid = request.getParameter("menuid");
		modView.addObject("menuid", menuid);
		return modView;
	}
	/**
	 * 查询当前用户需要处理专家意见的案件列表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/querySpeDealCaseList.do")
	public EasyUITotalResult querySpeDealCaseList(HttpServletRequest request){
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		//执行查询
		PaginationSupport ps = service.querySpeDealCaseList(param);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 专家意见查看
	 * @param request
	 * @return
	 */
	@RequestMapping("/view.do")
	public ModelAndView view(HttpServletRequest request){
		ModelAndView modView = new ModelAndView("aros/zjgl/zjyj_view");
		String menuid = request.getParameter("menuid");
		String caseid = request.getParameter("caseid");
		List<Map<String, Object>> data = service.querySpeSugByCaseId(caseid);
		modView.addObject("data", data);
		modView.addObject("menuid", menuid);
		return modView;
	}
	
	@RequestMapping("/spesugEdit.do")
	public ModelAndView spesugEdit(HttpServletRequest request){
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/zjgl/bspesugbaseinfo_form");
		String caseid = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		return mav;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryZTList.do")
	@ResponseBody
	public List<Map<String, Object>> queryZTList(HttpServletRequest request){
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		return service.queryZTList(param);
		
	}
	
	/**
	 * 查询当前用户给当前案件提意见的所有主题
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryspesugbyZT.do")
	public ModelAndView queryspesugbyZT(HttpServletRequest request){
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		ModelAndView mv = new ModelAndView("aros/zjgl/editSpeSugPage");
		String caseid = (String) param.get("caseid");
		String groupid = (String) param.get("groupid");
		List<Map<String, Object>> list = service.queryspesugbyZT(param);
		mv.addObject("list", list);
		mv.addObject("caseid", caseid);
		mv.addObject("groupid", groupid);
		return mv;
		
	}
	
	/**
	 * 专家评论界面查看案件卷宗文件列表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryFiles.do")
	public List<Map<String, Object>> queryFiles(HttpServletRequest request){
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		return service.queryFiles(param);
	}
	
	/**
	 * 保存专家意见
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveSpeSugInfo.do")
	public ResultMsg saveSpeSugInfo(HttpServletRequest request){
		ResultMsg msg = null;
		SysUser user = SecureUtil.getCurrentUser();
		List<BSpesugbaseinfo> bSpesugbaseinfoList = new ArrayList<BSpesugbaseinfo>();
		@SuppressWarnings("unchecked")
		Map<String, String> paramMap = ControllerUtil.getRequestParameterMap(request);
		String caseid = paramMap.get("caseid");
		String groupid = paramMap.get("groupid");
		String remark = paramMap.get("remark");
		String timeStr = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		String userId = String.valueOf(user.getUserid());
		
		
		BSpecialistbaseinfo bSpecialistbaseinfo = bSpecialistbaseinfoService.getSpeInfoByUserId(user.getUserid());
		String speId =  bSpecialistbaseinfo.getSpeid();
		BSpesugbaseinfo bean = new BSpesugbaseinfo();
		bean.setOpttime(timeStr);
		bean.setSpeid(speId);
		bean.setOperator(userId);
		bean.setCaseid(caseid);
		bean.setRemark(remark);
		bean.setGroupid(groupid);
		if(StringUtils.isEmpty(remark)){
			msg = new ResultMsg(false, "您还没有填写意见！");
		}else{
			try {
				service.save(bean);
				msg = new ResultMsg(true, "保存成功！");
			} catch (Exception e) {
				e.printStackTrace();
				msg = new ResultMsg(false, "保存失败！");
			}
		}
		return msg;
	}
	
	/**
	 * @Title: sendCaseInit
	 * @Description: 复议研讨发起：返回复议研讨发起页面
	 * @author ybb
	 * @date 2016年11月15日 上午10:50:36
	 * @param request
	 * @return
	 */
	@RequestMapping({"/sendCaseInit.do"})
	public ModelAndView sendCaseInit(HttpServletRequest request) { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/zjgl/sendcaseinit_init");
		
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryXzfyProcessList
	 * @Description: 复议研讨发起：查询所有在途的行政复议案件
	 * @author ybb
	 * @date 2016年11月15日10:53:13
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
		PaginationSupport ps = bSpecialistbaseinfoService.queryXzfyProcessList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: saveSpegrouprelainfo
	 * @Description: 复议研讨发起：保存复议研讨发起信息
	 * @author ybb
	 * @date 2016年11月15日 下午5:10:31
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping({ "/saveSpegrouprelainfo.do" })
	@ResponseBody
	public ResultMsg saveSpegrouprelainfo(HttpServletRequest request) throws ServletException {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			
			service.saveSpegrouprelainfo(param);
			msg = new ResultMsg(true, "保存成功");
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: expertCommentInit
	 * @Description: 委员评论：返回委员评论录入页面
	 * @author ybb
	 * @date 2016年11月16日 上午11:39:38
	 * @param request
	 * @return
	 */
	@RequestMapping("/expertCommentInit.do")
	public ModelAndView expertCommentInit(HttpServletRequest request){
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/zjgl/spesugbaseinfo_form");
		
		String caseid = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
		
		return mav;
	}
	
	/**
	 * 查询当前用户给当前案件提意见的所有主题
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/querySpesugbaseinfo.do")
	public ModelAndView querySpesugbaseinfo(HttpServletRequest request) {
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		ModelAndView mv = new ModelAndView("aros/zjgl/spesugbaseinfo_form");
		
		String caseid = (String) param.get("caseid");
		List<Map<String, Object>> list = service.querySpesugbaseinfo(caseid);
		
		mv.addObject("list", list);
		mv.addObject("caseid", caseid);
		
		return mv;
	}
	
	/**
	 * @Title: saveSpesugbaseinfo
	 * @Description: 保存委员意见
	 * @author ybb
	 * @date 2016年11月16日 下午3:26:15
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/saveSpesugbaseinfo.do")
	public ResultMsg saveSpesugbaseinfo(HttpServletRequest request){
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			service.saveSpesugbaseinfo(param);
			msg = new ResultMsg(true, "保存成功");
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: spesugbaseinfoViewInit
	 * @Description: TODO
	 * @author Administrator
	 * @date 2016年11月16日 下午4:49:38
	 * @param request
	 * @return
	 */
	@RequestMapping("/spesugbaseinfoViewInit.do")
	public ModelAndView spesugbaseinfoViewInit(HttpServletRequest request){
		
		ModelAndView modView = new ModelAndView("aros/zjgl/spesugbaseinfo_view");
		
		String menuid = request.getParameter("menuid");
		String caseid = request.getParameter("caseid");
		
		List<Map<String, Object>> list = service.querySpesugbaseinfoByCaseid(caseid);
		
		modView.addObject("list", list);
		modView.addObject("menuid", menuid);
		
		return modView;
	}
}
