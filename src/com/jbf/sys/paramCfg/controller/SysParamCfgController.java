package com.jbf.sys.paramCfg.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.paramCfg.po.SysParamCfg;
import com.jbf.sys.paramCfg.service.SysParamCfgService;
/**
 * 系统配置参数管理Controller
 * @ClassName: SysParamCfgController 
 * @Description: TODO(系统参数配置管理) 
 * @author MaQingShuang
 * @date 2015年5月13日 下午2:29:40
 */
@Controller
@RequestMapping({ "/sys/SysParamCfgController" })
public class SysParamCfgController {
	
	@Autowired
	SysParamCfgService sysParamCfgService;

	/**
	 * 参数配置界面入口
	 * @return
	 */
	@RequestMapping("/entry.do")
	public String entry() {
		
		return "/sys/ParamCfgEntry";
	}
	
	/**
	 * 参数配置表单维护界面入口
	 * @return
	 */
	@RequestMapping("/formEntry.do")
	public String formEntry() {
		
		return "/sys/ParamCfgFormEntry";
	}
	
	/**
	 * 参数查询
	 * @param request
	 * @return
	 */
	@RequestMapping("/query.do")
	@ResponseBody
	public EasyUITotalResult query(HttpServletRequest request) {
		
		String admivcode = request.getParameter("admivcode");
		String scenecode = request.getParameter("scenecode");
		String paramcode = request.getParameter("paramcode");
		String paramname = request.getParameter("paramname");
		String status = request.getParameter("status");
		String pageSizeStr = request.getParameter("pageSize");
		Integer pageSize = StringUtil.isNotBlank(pageSizeStr) ? Integer.valueOf(pageSizeStr) : 30;
		String pageIndexStr = request.getParameter("page");
		Integer pageIndex = StringUtil.isNotBlank(pageIndexStr) ? Integer.valueOf(pageIndexStr) : 1;
		
		PaginationSupport ps = sysParamCfgService.query(admivcode, scenecode, paramcode, paramname, status, pageSize, pageIndex);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 参数新增
	 * @param paramCfg
	 * @return
	 */
	@RequestMapping("/add.do")
	@ResponseBody
	public ResultMsg add(HttpServletRequest request) {
		ResultMsg msg = null;
		boolean isSuccess = false;
		Map paramMap = ControllerUtil.getRequestParameterMap(request);
		SysParamCfg sysParamCfg = new SysParamCfg();
		try {
			BeanUtils.populate(sysParamCfg, paramMap);
			isSuccess = sysParamCfgService.addParam(request, sysParamCfg);
			if (isSuccess) {
				msg = new ResultMsg(isSuccess, "新增成功！");
			} else {
				msg = new ResultMsg(isSuccess, "新增失败！");
			}
				
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			msg = new ResultMsg(false, "新增失败,失败原因：" + e.getMessage() + "。");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			msg = new ResultMsg(false, "新增失败,失败原因：" + e.getMessage() + "。");
			e.printStackTrace();
		}
		
		return msg;
	}
	
	/**
	 * 参数修改
	 * @param paramCfg
	 * @return
	 */
	@RequestMapping("/edit.do")
	@ResponseBody
	public ResultMsg edit(HttpServletRequest request) {
		ResultMsg msg = null;
		boolean isSuccess = false;
		Map paramMap = ControllerUtil.getRequestParameterMap(request);
		SysParamCfg sysParamCfg = new SysParamCfg();
		try {
			BeanUtils.populate(sysParamCfg, paramMap);
			isSuccess = sysParamCfgService.editParam(request, sysParamCfg);
			if (isSuccess) {
				msg = new ResultMsg(isSuccess, "修改成功！");
			} else {
				msg = new ResultMsg(isSuccess, "修改失败！");
			}
				
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			msg = new ResultMsg(false, "修改失败,失败原因：" + e.getMessage() + "。");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			msg = new ResultMsg(false, "修改失败,失败原因：" + e.getMessage() + "。");
			e.printStackTrace();
		}
		return msg;
		
	}
	
	/**
	 * 参数删除
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public ResultMsg delete(HttpServletRequest request) {
		ResultMsg msg = null;
		boolean isSuccess = false;
		String paramid = request.getParameter("paramid");
		isSuccess = sysParamCfgService.deleteParam(request, Long.valueOf(paramid));
		if (isSuccess)
			msg = new ResultMsg(isSuccess, "删除成功！");
		else
			msg = new ResultMsg(isSuccess, "删除失败！");
		return msg;
	}
}
