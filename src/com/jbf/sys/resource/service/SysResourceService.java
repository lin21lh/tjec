/************************************************************
 * 类名：SysResourceService.java
 *
 * 类别：Service
 * 功能：资源服务
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.resource.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jbf.sys.resource.po.SysResource;

public interface SysResourceService {

	/**
	 * 取得资源详情
	 * 
	 * @param id
	 *            资源ID
	 * @return 资源详情
	 */
	public SysResource get(Long id);

	/**
	 * 保存资源
	 * 
	 * @param sysResource
	 *            资源详情
	 * @return 资源id
	 */
	public Long save(SysResource sysResource);

	/**
	 * 删除资源
	 * 
	 * @param id
	 *            资源id
	 * @throws Exception
	 */
	public void delete(Long id) throws Exception;

	/**
	 * 查询所有资源
	 * 
	 * @return 所有的资源列表
	 * @throws Exception
	 */
	public List query() throws Exception;
	
	/**
	 * 查询所有业务类的功能菜单
	 * @Title: queryBusinessList  
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return 设定文件 
	 * @return List 返回类型 
	 * @throws
	 */
	public List queryBusinessList();

	/**
	 * 查询角色拥有的资源
	 * 
	 * @param roleid
	 *            角色id
	 * @return 角色资源列表
	 * @throws Exception
	 */
	public List queryResourceTreeByRole(Long roleid) throws Exception;

	/**
	 * 菜单顺序调整 只允许同级调整
	 * 
	 * @param pid
	 * @param srcid
	 * @param tgtid
	 * @param point
	 * 
	 */
	public void resourceReorder(Long pid, Long srcid, Long tgtid, String point)
			throws Exception;
	
	/**
	 * @Title: queryResource
	 * @Description: 根据用户ID查询角色对应的菜单列表
	 * @author ybb
	 * @date 2017年3月25日 下午3:32:10
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public List<JSONObject> queryResource(Long userid);

}
