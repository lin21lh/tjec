package com.jbf.sys.sortfieldset.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.sortfieldset.po.SysSortFieldSet;
import com.jbf.sys.sortfieldset.service.SysSortFieldSetService;

@Scope("prototype")
@Controller
@RequestMapping("/sys/sortfieldset/SysSortFieldSetController")
public class SysSortFieldSetController {

	@Autowired
	SysSortFieldSetService sortFieldSetService;
	
	/**
	 * 排序字段入口界面
	 * @Title: entry 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping("/entry.do")
	public ModelAndView entry() {
		
		return new ModelAndView("/sys/sortfieldset/sortfieldsetEntry");
	}
	
	/**
	 * 排序字段Form表单界面
	 * @Title: formEntry 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping("/formEntry.do")
	public ModelAndView formEntry() {
		
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		return new ModelAndView("/sys/sortfieldset/sortfieldsetFormEntry", "modelMap", modelMap);
	}
	
	@RequestMapping("/query.do")
	@ResponseBody
	public EasyUITotalResult query(HttpServletRequest request) {
		
		String pageSizeStr = request.getParameter("rows");
		String pageIndexStr = request.getParameter("page");
		Integer pageSize = StringUtil.isBlank(pageSizeStr) ? 30 : Integer.valueOf(pageSizeStr);
		Integer pageIndex = StringUtil.isBlank(pageIndexStr) ? 1 : Integer.valueOf(pageIndexStr);
		Map<String, Object> paramMap = ControllerUtil.getRequestParameterMap(request);
		paramMap.remove("rows");
		paramMap.remove("page");
		
		PaginationSupport pSupport = sortFieldSetService.query(paramMap, pageSize, pageIndex);
		
		return EasyUITotalResult.from(pSupport);
	}
	
	/**
	 * 排序字段设置新增
	 * @Title: save 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/add.do")
	@ResponseBody
	public ResultMsg add(SysSortFieldSet sortFieldSet) {
		ResultMsg resultMsg = null;
		
		try {
			sortFieldSetService.add(sortFieldSet);
			resultMsg = new ResultMsg(true, AppException.getMessage("crud.addok"));
		} catch (Exception e) {
			// TODO: handle exception
			resultMsg = new ResultMsg(false, AppException.getMessage("crud.adderr") + "原因：" + e.getMessage());
		}
		
		return resultMsg;
	}
	
	/**
	 * 排序字段设置修改
	 * @Title: edit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param sortFieldSet
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/edit.do")
	@ResponseBody
	public ResultMsg edit(SysSortFieldSet sortFieldSet) {
		ResultMsg resultMsg = null;
		
		try {
			sortFieldSetService.edit(sortFieldSet);
			resultMsg = new ResultMsg(true, AppException.getMessage("crud.editok"));
		} catch (Exception e) {
			// TODO: handle exception
			resultMsg = new ResultMsg(false, AppException.getMessage("crud.editerr") + "原因：" + e.getMessage());
		}
		
		return resultMsg;
	}
	
	/**
	 * 排序字段设置删除
	 * @Title: delete 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public ResultMsg delete(Long sortid) {
		ResultMsg resultMsg = null;
		
		try {
			sortFieldSetService.delete(sortid);
			resultMsg = new ResultMsg(true, AppException.getMessage("crud.delok"));
		} catch (Exception e) {
			// TODO: handle exception
			resultMsg = new ResultMsg(false, AppException.getMessage("crud.delerr") + "原因：" + e.getMessage());
		}
		
		return resultMsg;
	}
	
	/**
	 * 当前模块下排序集合
	 * @Title: queryListByApp 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return List<SysSortFieldSet> 返回类型 
	 * @throws
	 */
	@RequestMapping("/queryListByApp.do")
	@ResponseBody
	public List<SysSortFieldSet> queryListByApp(HttpServletRequest request) {
		String menuid = request.getParameter("menuid");
		
		return sortFieldSetService.queryListByApp(Long.valueOf(menuid));
	}
	
	
	
}
