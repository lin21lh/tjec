package com.wfzcx.fam.archives.account.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.wfzcx.fam.archives.account.service.AccountArchivesServiceI;
import com.wfzcx.fam.common.DeptComponent;

@Scope("prototype")
@Controller
@RequestMapping("/archives/account/controller/AccountArchivesController")
public class AccountArchivesController {
	
	@Autowired
	AccountArchivesServiceI accountArchivesService;
	
	@RequestMapping({ "/init.do" })
	public ModelAndView init(String menuid) throws ServletException {
		
		ModelAndView mav = new ModelAndView("fam/archives/account/archives_init");
		mav.addObject("menuid",menuid);
		
		//权限内的所有机构(add by XinPeng 2015年5月18日16:07:05)
		String allbdgagency = DeptComponent.getCurAndLowerCodeForYsdw(menuid,"code");
		mav.addObject("allbdgagency",allbdgagency);
		SysUser user = SecureUtil.getCurrentUser();
		mav.addObject("userid",user.getUserid());
		
		return mav;
	}
	
	/**
	 * 查询账户档案信息
	 * @Title: queryAccountArchives 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return EasyUITotalResult 返回类型 
	 * @throws
	 */
	@RequestMapping({ "/queryAccountArchives.do" })
	@ResponseBody
	public EasyUITotalResult queryAccountArchives(HttpServletRequest request) throws AppException{
		
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = accountArchivesService.queryAccountArchives(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 查询附件
	 * @Title: queryFileInfo 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping("/targetFileInfoForm.do")
	public ModelAndView queryFileInfo(HttpServletRequest request) {
		
		ModelAndView mav = null;
		//获取session用于附件上传
		HttpSession session = request.getSession();
		String optType = request.getParameter("optType");
		Map<String, Object> modelMap = new HashMap<String,Object>();
		modelMap.put("sessionId", session.getId());
		modelMap.put("optType", optType);
		if(optType.equals("view")){
			mav = new ModelAndView("/fam/archives/account/archives_files","modelMap",modelMap);
		}
		return mav;
	}
	
	@RequestMapping("/saveFileInfo.do")
	@ResponseBody
	public ResultMsg saveFileInfo(HttpServletRequest request) {
		
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		ResultMsg msg = null;
		try {
			accountArchivesService.saveFileInfo(map);
			msg = new ResultMsg(true, "保存成功！");
		} catch (Exception e1){
			e1.printStackTrace();
			msg = new ResultMsg(false, "保存失败！");
		}
		return msg;
	}
	
}
