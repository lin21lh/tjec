package com.wfzcx.ppms.transfer.service;

import java.util.List;
import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.sys.resource.po.SysResource;


public interface ProjectTransferService {
	/**
	 * 通过菜单id获取菜单所有属性
	 * @Title: getResourceById 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @return
	 * @throws Exception 设定文件
	 */
	public SysResource getResourceById(String menuid) throws Exception;
	/**
	 * 项目查询
	 * @Title: queryProject 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @return
	 * @throws AppException 设定文件
	 */
	public PaginationSupport queryProject(Map<String, Object> map)  throws AppException;
	/**
	 * 保存
	 * @Title: transferSave 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws Exception 设定文件
	 */
	public String transferSave(Map<String, Object> param) throws Exception;
	/**
	 * 单条查询
	 * @Title: queryTransfer 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param transferid
	 * @return
	 * @throws Exception 设定文件
	 */
	public List queryTransfer(String transferid) throws Exception;
	/**
	 * 撤回
	 * @Title: revokeWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @param transferid
	 * @param activityId
	 * @return
	 * @throws Exception 设定文件
	 */
	public String revokeWorkFlow(String wfid,String transferid,String activityId) throws Exception;
	/**
	 * 送审
	 * @Title: sendWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param transferid
	 * @param activityId
	 * @param wfid
	 * @return
	 * @throws Exception 设定文件
	 */
	public String sendWorkFlow(String menuid,String transferid,String activityId,String wfid) throws Exception ;
	/**
	 * 审批
	 * @Title: auditWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param transferid
	 * @param activityId
	 * @param wfid
	 * @return
	 * @throws Exception 设定文件
	 */
	public String auditWorkFlow(String transferid,String activityId,String wfid,String isback,String opinion) throws Exception ;
}
