package com.wfzcx.aros.flow.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

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
import com.wfzcx.aros.flow.po.Pronodebaseinfo;
import com.wfzcx.aros.flow.service.PronodebaseinfoService;

/**
 * @ClassName: PronodebaseinfoController
 * @Description: 流程节点配置信息业务控制类
 * @author ybb
 * @date 2016年11月28日 上午9:59:05
 * @version V1.0
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/flow/controller/PronodebaseinfoController")
public class PronodebaseinfoController {

	@Autowired
	private PronodebaseinfoService pronodebaseinfoService;
	
	/**
	 * @Title: pronodebaseinfoInit
	 * @Description: 流程配置-返回流程配置grid页面
	 * @author ybb
	 * @date 2016年11月28日 上午10:45:56
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/pronodebaseinfoInit.do"})
	public ModelAndView pronodebaseinfoInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/flow/pronodebaseinfo_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryPronodebaseinfoList
	 * @Description: 流程配置-grid列表页面查询
	 * @author ybb
	 * @date 2016年11月28日 上午10:47:21
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryPronodebaseinfoList.do")
	@ResponseBody
	public EasyUITotalResult queryPronodebaseinfoList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = pronodebaseinfoService.queryPronodebaseinfoList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: pronodebaseinfoAddInit
	 * @Description: 流程配置-返回新增页面
	 * @author ybb
	 * @date 2016年11月28日 上午10:49:31
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/pronodebaseinfoAddInit.do"})
	public ModelAndView pronodebaseinfoAddInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/flow/pronodebaseinfo_form");
		
		return mav;
	}
	
	/**
	 * @Title: pronodebaseinfoSave
	 * @Description: 流程配置-新增流程配置信息
	 * @author ybb
	 * @date 2016年11月28日 上午10:55:47
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/pronodebaseinfoSave.do")
	@ResponseBody
	public ResultMsg pronodebaseinfoSave(HttpServletRequest request) {

		ResultMsg msg = null;

		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);

		try {
			pronodebaseinfoService.addPronodebaseinfo(param);
			msg = new ResultMsg(true, "保存成功");

		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}

		return msg;
	}
	
	/**
	 * @Title: pronodebaseinfoEditInit
	 * @Description: 流程配置-返回修改页面
	 * @author ybb
	 * @date 2016年11月28日 上午10:56:45
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/pronodebaseinfoEditInit.do"})
	public ModelAndView pronodebaseinfoEditInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/flow/pronodebaseinfo_form");
		
		return mav;
	}
	
	/**
	 * @Title: pronodebaseinfoEdit
	 * @Description: 流程配置-修改流程配置信息
	 * @author ybb
	 * @date 2016年11月28日 上午10:59:46
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/pronodebaseinfoEdit.do")
	@ResponseBody
	public ResultMsg pronodebaseinfoEdit(HttpServletRequest request){
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		try {
			pronodebaseinfoService.updatePronodebaseinfo(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: pronodebaseinfoDelete
	 * @Description: 流程配置-删除流程配置信息
	 * @author ybb
	 * @date 2016年11月28日 上午11:01:11
	 * @param id
	 * @return
	 */
	@RequestMapping("/pronodebaseinfoDelete.do")
	@ResponseBody
	public ResultMsg pronodebaseinfoDelete(String id) {
		
		ResultMsg msg = null;
		
		try {
			
			pronodebaseinfoService.delPronodebaseinfo(id);
			msg = new ResultMsg(true, AppException.getMessage("删除成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败"));
		}
		
		return msg;
	}
	
	/**
	 * @Title: pronodebaseinfoDetail
	 * @Description: 流程配置-查看流程配置信息
	 * @author ybb
	 * @date 2016年11月28日 上午11:05:32
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws AppException
	 */
	@RequestMapping({"/pronodebaseinfoDetail.do"})
	public ModelAndView pronodebaseinfoDetail(HttpServletRequest request) throws ServletException, AppException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/flow/pronodebaseinfo_detail");
		
		//流程配置ID
		String id = request.getParameter("id");
		mav.addObject("id", id);
		
		Pronodebaseinfo pronodebaseinfo = pronodebaseinfoService.queryPronodebaseinfoById(id);
		mav.addObject("pronodebaseinfo", pronodebaseinfo);
		
		if (pronodebaseinfo != null) {
			mav.addObject("nodeid", pronodebaseinfo.getNodeid());
			mav.addObject("roleid", pronodebaseinfo.getRoleid());
		}
		
		return mav;
	}
	
	/**
	 * @Title: queryRoleList
	 * @Description: 流程配置-查询角色列表
	 * @author ybb
	 * @date 2016年11月28日 下午5:24:57
	 * @return
	 */
	@RequestMapping("/queryRoleList.do")
	@ResponseBody
	public List<HashMap<String, String>> queryRoleList() {
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		try {
			
			list = pronodebaseinfoService.queryRoleList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
