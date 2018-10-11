package com.wfzcx.ppms.procurement.service;

import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.sys.resource.po.SysResource;

/**
 * 预审结果service
 * @ClassName: ProjectAdvacneService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年9月21日 上午8:15:48
 */
public interface ProjectAdvacneService {
	
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
	 * 查询预审机构
	 * @Title: queryOrgan 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @param advanceid
	 * @return
	 * @throws AppException 设定文件
	 */
	public PaginationSupport queryOrgan(String projectid,String advanceid)  throws AppException;
	/**
	 * 预审结果保存
	 * @Title: advanceAddCommit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws Exception 设定文件
	 */
	public String advanceAddCommit(Map<String, Object> param) throws Exception;
	/**
	 * 送审工作流
	 * @Title: sendWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param projectid
	 * @param activityId
	 * @param wfid
	 * @return
	 * @throws Exception 设定文件
	 */
	public String sendWorkFlow(String menuid,String advanceid,String activityId,String wfid) throws Exception ;
	/**
	 * 撤回工作流
	 * @Title: revokeWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @param projectid
	 * @param activityId
	 * @return
	 * @throws Exception 设定文件
	 */
	public String revokeWorkFlow(String wfid,String advanceid,String activityId) throws Exception;
	/**
	 * 审批
	 * @Title: auditWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param advanceid
	 * @param activityId
	 * @param wfid
	 * @return
	 * @throws Exception 设定文件
	 */
	public String auditWorkFlow(String advanceid,String activityId,String wfid,String isback,String opinion) throws Exception ;
	
	/**
	 * 查询专家表
	 * @Title: qryExpertByQ 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return PaginationSupport 返回类型 
	 */
	public PaginationSupport qryExpertByQ(Map map);
	
	/**
	 * 查询专家列表
	 * @Title: advanceExpertGrid 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param map
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return PaginationSupport 返回类型 
	 * @throws
	 */
	public PaginationSupport advanceExpertGrid(Map map) throws AppException;
}
