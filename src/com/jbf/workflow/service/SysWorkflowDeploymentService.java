/************************************************************
 * 类名：SysWorkflowDeploymentService
 * 
 * 类别：Service
 * 功能：工作流部署管理服务
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   hyf         初版
 *   
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.service;

import java.awt.image.RenderedImage;
import java.util.HashMap;
import java.util.List;

import com.jbf.workflow.po.SysWorkflowProcversion;

public interface SysWorkflowDeploymentService {

	/**
	 * 部署工作流版本定义
	 * 
	 * @param xml
	 *            jpdl定义
	 * @param resourceName
	 *            图片资源名
	 * @param key
	 *            工作流key
	 * @param version
	 *            版本
	 * @param privilage
	 *            权限
	 * @throws Exception
	 */
	public void deployWfDef(String xml, String resourceName, String key,
			Integer version, String privilage) throws Exception;

	/**
	 * 取得工作流实例的实时流程图，包含当前活动节点的标示
	 * 
	 * @param instId
	 * @return 流程图图片
	 * @throws Exception
	 */
	public RenderedImage getImageByWfInstanceId(String instId) throws Exception;

	/**
	 * 部署工作流版本定义
	 * 
	 * @param xml
	 *            jpdl定义
	 * @param resourceName
	 *            图片资源名
	 * @param key
	 *            工作流key
	 * @param version
	 *            版本
	 * @param backAttr
	 *            退回撤回属性
	 * @param privilage
	 *            权限
	 * @param pageUrl
	 *            对应业务
	 * @param startdate
	 *            启用日期
	 * @param enddate
	 *            停用日期
	 * @throws Exception
	 */
	public void deployWfDef(String xml, String resourceName, String name,
			String key, String version,String firstnode,  String startdate, String enddate,
			String extAttrCache) throws Exception;

	/**
	 * 修改工作流版本定义的启用日期和停用日期
	 * 
	 * @param key
	 *            工作流key
	 * @param version
	 *            版本
	 * @param startdate
	 *            启用日期
	 * @param enddate
	 *            停用日期
	 * @throws Exception
	 */
	public void editWorkflowProcVersion(String key, String version,
			String startdate, String enddate) throws Exception;

	/**
	 * 取得工作流版本定义
	 * 
	 * @param key
	 *            工作流key
	 * @param version
	 *            版本
	 * @return 工作流版本定义
	 */
	public SysWorkflowProcversion getWorkflowProcversion(String key,
			String version);

	/**
	 * 查询扩展属性， 用于工作流设计器调用
	 * 
	 * @param key
	 * @param version
	 * @return
	 */
	List getExtendedAttributes(String key, Integer version);

	/**
	 * 扩展属性修改， 用于工作流设计器调用
	 * 
	 * @param key
	 * @param version
	 * @param actiName
	 * @param category
	 * @param transName
	 * @param fieldName
	 * @param fieldValue
	 * @throws Exception
	 */
	void workflowExtAttrUpdate(String key, Integer version, String actiName,
			String category, String transName, String fieldName,
			String fieldValue) throws Exception;

	/**
	 * 查询工作流任务节点表单是否可编辑，二次开发接口
	 * 
	 * @param key
	 * @param version
	 * @param actiId
	 * @return
	 */
	HashMap isWorkflowTaskFormEditable(String key, Integer version, String actiId);
}
