/************************************************************
 * 类名：SysResourceController.java
 *
 * 类别：Controller
 * 功能：提供系统资源的页面入口和增删改查功能
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.resource.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.exception.AppException;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.resource.service.SysResourceService;
import com.jbf.sys.resource.vo.ResourceTreeVo;

@Scope("prototype")
@Controller
@RequestMapping("/sys/SysResourceController")
public class SysResourceController {

	@Autowired
	SysResourceService sysResourceService;

	/**
	 * 页面入口
	 * 
	 * @return
	 */
	@RequestMapping("/entry.do")
	public ModelAndView entry() {
		return new ModelAndView("/sys/resourceEntry");
	}

	/**
	 * 查询所有的菜单树
	 * 
	 * @return 菜单树列表
	 */
	@ResponseBody
	@RequestMapping("/query.do")
	public List query() {
		List list = null;
		try {
			list = sysResourceService.query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 查询所有业务类的功能菜单
	 * @Title: queryBussinessList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return 设定文件 
	 * @return List<ResourceTreeVo> 返回类型 
	 * @throws
	 */
	@RequestMapping("/queryBusiness.do")
	@ResponseBody
	public List<ResourceTreeVo> queryBussinessList() {
		
		return sysResourceService.queryBusinessList();
	}

	/**
	 * 取得特定角色的资源树
	 * 
	 * @param roleid
	 *            角色id
	 * @return 菜单树列表
	 */
	@ResponseBody
	@RequestMapping("/getResourceTreeByRole.do")
	public List getResourceTreeByRole(Long roleid) {
		List list = null;
		try {
			list = sysResourceService.queryResourceTreeByRole(roleid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 取得资源的详情
	 * 
	 * @param id
	 *            资源的id
	 * @return 资源的详情
	 */
	@ResponseBody
	@RequestMapping("/get.do")
	public SysResource get(Long id) {
		return sysResourceService.get(id);
	}

	/**
	 * 保存资源
	 * 
	 * @param resource
	 *            资源详情
	 * @return 是否成功标志
	 */
	@ResponseBody
	@RequestMapping("/save.do")
	public ResultMsg save(@ModelAttribute SysResource resource) {
		Long id = null;
		try {
			id = sysResourceService.save(resource);
			ResultMsg msg = new ResultMsg(true,
					AppException.getMessage("crud.saveok"));
			HashMap map = new HashMap();
			map.put("id", id);
			msg.setBody(map);
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
	}

	/**
	 * 删除资源
	 * 
	 * @param id
	 *            资源id
	 * @return 是否成功标志
	 */
	@ResponseBody
	@RequestMapping("/delete.do")
	public ResultMsg delMenu(Long id) {
		try {
			sysResourceService.delete(id);
			return new ResultMsg(true, AppException.getMessage("crud.delok"));
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AppException) {
				return new ResultMsg(false, e.getMessage());
			} else {
				return new ResultMsg(false,
						AppException.getMessage("crud.delerr"));
			}
		}
	}

	/**
	 * 菜单顺序调整 只允许同级调整
	 * 
	 * @param pid
	 * @param srcid
	 * @param tgtid
	 * @param point
	 * @return 是否成功标志
	 */
	@ResponseBody
	@RequestMapping("/resourceReorder.do")
	public ResultMsg resourceReorder(Long pid, Long srcid, Long tgtid,
			String point) {
		try {
			sysResourceService.resourceReorder(pid, srcid, tgtid, point);
			return new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
	}

}
