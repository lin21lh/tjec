/************************************************************
 * 类名：DatascopeController.java
 *
 * 类别：Controller
 * 功能：提供数据权限管理的页面入口、增删改查功能、数据权限分配功能和获取工作流执行条件功能
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-7-04  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.datascope.controller;

import java.lang.reflect.InvocationTargetException;
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

import com.jbf.base.datascope.po.SysRoleUserResDscope;
import com.jbf.base.datascope.service.DatascopeService;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.JsonUtil;
import com.jbf.common.web.ResultMsg;



@Scope("prototype")
@Controller
@RequestMapping({"/base/datascope/datascopeController"})
public class DatascopeController {

	@Autowired
	DatascopeService datascopeService;
	
	/**
	 * 数据权限页面
	 * @return
	 */
	@RequestMapping("/entry.do")
	public ModelAndView entry() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		return new ModelAndView("/base/datascope/datascopeEntry", "modelMap", modelMap);
	}
	
	/**
	 * 数据权限维护界面
	 * @return
	 */
	@RequestMapping({"/editEntry.do"})
	public ModelAndView editEntry() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		return new ModelAndView("/base/datascope/datascopeEditEntry", "modelMap", modelMap);
	}
	
	/**
	 * 角色、用户、功能菜单和数据权限关联关系保存
	 * @param data 角色、用户、功能菜单和数据权限关联关系 字符串
	 * @return ResultMsg 保存结果
	 */
	@ResponseBody
	@RequestMapping("/save.do")
	public ResultMsg save(String data) {
		try {
			SysRoleUserResDscope roleUserResDscope = (SysRoleUserResDscope) JsonUtil.createObjectByJsonString(data, SysRoleUserResDscope.class);
			
			datascopeService.save(roleUserResDscope);
			return new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
		
	}
	
	/**
	 * 保存数据权限
	 * @param str 数据权限字符串
	 * @return ResultMsg 保存结果
	 */
	@ResponseBody
	@RequestMapping("/saveDatascope.do")
	public ResultMsg saveDatascope(String str) {
		
		try {
			datascopeService.saveDatascope(str);
			return new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
	}
	
	/**
	 * 数据权限树
	 * @return List<Object> 数据权限集合
	 */
	@ResponseBody
	@RequestMapping({"/findDataScopeMain.do"})
	public List<Object> findDataScopeMain() {
		
		return datascopeService.findDataScopeMain();
	}
	
	/**
	 * 通过ID获取数据权限明细
	 * @param scopemainid 数据权限主ID
	 * @return ResultMsg 数据权限明细
	 */ 
	@ResponseBody
	@RequestMapping({"/getDataScopeDetailByID.do"})
	public ResultMsg getDataScopeDetailByID(Long scopemainid) {
		
		try {
			return new ResultMsg(true, "", datascopeService.getDataScopeDetailByID(scopemainid));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("data.query.fail", new String []{e.getMessage()}));
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("data.query.fail", new String []{e.getMessage()}));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("data.query.fail", new String []{e.getMessage()}));
		}
	}
	
	/**
	 * 根据分配的角色、资源和用户获取数据权限明细
	 * @param request 角色IDroleid、是否全部功能菜单 1=是 0=否 isallmenu、功能菜单ID resourceid、是否全部用户 1=是 0=否 isalluser、用户ID userid
	 * @return ResultMsg 数据权限明细
	 */
	@ResponseBody
	@RequestMapping({"/getDataScopeDetail.do"})
	public ResultMsg getDataScopeDetail(HttpServletRequest request) {
		Long roleid = Long.valueOf(request.getParameter("roleid"));
		Integer isallmenu = Integer.valueOf(request.getParameter("isallmenu"));
		Long resourceid = null;
		if (isallmenu == 0) 
			resourceid = Long.valueOf(request.getParameter("resourceid"));
		 
		Integer isalluser = Integer.valueOf(request.getParameter("isalluser"));
		Long userid = null;
		if (isalluser == 0)
			userid = Long.valueOf(request.getParameter("userid"));
			
		ResultMsg resultMsg = null;
		try {
			HashMap<String, Object> resultMap = datascopeService.getDataScopeDetail(roleid, isallmenu, resourceid, isalluser, userid);
			Object scopemainid = resultMap.get("escopemainid");
			
			if (scopemainid != null)
				resultMap.remove("escopemainid");
			else 
				scopemainid = "";
			resultMsg = new ResultMsg(true, scopemainid.toString(), resultMap);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("data.query.fail", new String []{e.getMessage()}));
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("data.query.fail", new String []{e.getMessage()}));
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("data.query.fail", new String []{e.getMessage()}));
		}
		return resultMsg;
	}
	
	/**
	 * 根据角色ID获取数据权限的分配关系
	 * @param roleid 角色ID
	 * @return 角色、用户、功能菜单和数据权限的关联关系PO
	 */
	@ResponseBody
	@RequestMapping({"/get.do"})
	public SysRoleUserResDscope get(Long roleid) {
		
		return datascopeService.getByRole(roleid);
	}
	
	/**
	 * 数据权限删除
	 * @param scopemainid 数据权限主ID
	 * @return ResultMsg 删除结果
	 */
	@ResponseBody
	@RequestMapping({"/deletedatascope.do"})
	public ResultMsg deleteDataScope(Long scopemainid) {
		
		try {
			String msg = datascopeService.deleteDataScope(scopemainid);
			if (msg.length() > 0)
				return new ResultMsg(false, msg);
			else
			return new ResultMsg(true, AppException.getMessage("crud.delok"));
		} catch (Exception e) {
			return new ResultMsg(false, AppException.getMessage("crud.delerr"));
		}
	}
	
	/**
	 * 删除分配的数据权限关系
	 * @param roleid 角色ID
	 * @param isallmenu 是否全部功能菜单 1=是 0=否
	 * @param menuid 功能菜单ID
	 * @param isalluser 是否全部用户 1=是 0=否
	 * @param userid 用户ID
	 * @return ResultMsg 删除结果
	 */
	@ResponseBody
	@RequestMapping({"/deleteDataScopeRelation.do"})
	public ResultMsg deleteDataScopeRelation(Long roleid, Integer isallmenu, Long resourceid, Integer isalluser, Long userid) {
		
		ResultMsg resultMsg = null;
		try {
			HashMap<String, Object> body = datascopeService.deleteDataScopeRelation(roleid, isallmenu, resourceid, isalluser, userid);
			resultMsg = new ResultMsg(true, AppException.getMessage("crud.delok"));
			resultMsg.setBody(body);
		} catch(Exception e) {
			return new ResultMsg(false, AppException.getMessage("crud.delerr"));
		}
		return resultMsg;
		
	}
	
	/**
	 * 根据数据权限主ID获取关联的角色、功能菜单和用户的关系
	 * @param scopemainid 数据权限主ID
	 * @return List 角色、用户、功能菜单和数据权限的关系集合
	 */
	@ResponseBody
	@RequestMapping({"/findRelationByScopemainID.do"})
	public List findRelationByScopemainID(Long scopemainid) {
		
		return datascopeService.findRelationByScopemainID(scopemainid);
	}
	
	/**
	 * 工作流执行条件保存
	 * @param str 执行条件字符串
	 * @return ResultMsg 保存结果
	 */
	@ResponseBody
	@RequestMapping({"/saveWFScope.do"})
	public ResultMsg saveWFScope(String str) {
		try {
			datascopeService.saveWFScope(str);
			return new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
	}
	
	/**
	 * 获取工作流执行条件
	 * @param wfkey 工作流key
	 * @param wfversion 工作流版本
	 * @param decisionname decision名称
	 * @param taskname 任务名称
	 * @return 工作流执行条件
	 */
	@ResponseBody
	@RequestMapping({"/getWFDataScopeDetail.do"})
	public ResultMsg getWFDataScope(String wfkey, Integer wfversion, String decisionname, String taskname) {
		
		ResultMsg resultMsg = null;
		 try {
			 resultMsg = new ResultMsg(true, "", datascopeService.getWFDataScope(wfkey, wfversion, decisionname, taskname));
		} catch (IllegalAccessException e) {
			resultMsg = new ResultMsg(false, AppException.getMessage("data.query.fail", new String []{e.getMessage()}));
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			resultMsg = new ResultMsg(false, AppException.getMessage("data.query.fail", new String []{e.getMessage()}));
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			resultMsg = new ResultMsg(false, AppException.getMessage("data.query.fail", new String []{e.getMessage()}));
			e.printStackTrace();
		}
		 
		return resultMsg;
	}
}
