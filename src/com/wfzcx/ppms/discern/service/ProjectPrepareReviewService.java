package com.wfzcx.ppms.discern.service;

import java.util.List;
import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;


public interface ProjectPrepareReviewService {
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
	 * 专家查询
	 * @Title: qualExpertGrid 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @return
	 * @throws AppException 设定文件
	 */
	public PaginationSupport qualExpertGrid(Map map) throws AppException;
	/**
	 * 查询评价指标
	 * @Title: queryPjzbTable 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @return
	 * @throws AppException 设定文件
	 */
	
	public PaginationSupport queryPjzbTable(Map map) throws AppException;
	/**
	 * 查询专家
	 * @Title: qryExpertByQ 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @return 设定文件
	 */
	public PaginationSupport qryExpertByQ(Map map);
	
	/**
	 * 查询评审指标
	 * @Title: queryPszbList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws AppException 设定文件
	 */
	public List queryPszbList(Map<String, Object> param) throws AppException;
	/**
	 * 评审准保保存
	 * @Title: pjzbSave 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @throws Exception 设定文件
	 */
	public void pjzbSave(Map<String, Object> param) throws Exception;
	/**
	 * 查询该项目是否已经录入评审准备信息
	 * @Title: queryIsExistPszb 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @return
	 * @throws Exception 设定文件
	 */
	public List queryIsExistPszb(String projectid,String xmhj)throws Exception;
	
	/**
	 * 删除评审准备
	 * @Title: revokePszb 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @param pszbid
	 * @throws Exception 设定文件
	 */
	public void revokePszb(String projectid,String pszbid)throws Exception;
	/**
	 * 提交评审准备
	 * @Title: sendPszb 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @param pszbid
	 * @throws Exception 设定文件
	 */
	public void sendPszb(String projectid,String pszbid)throws Exception;
	
	/**
	 * 评审准备等是否可撤回验证
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String recallYN(Map<String, Object> param) throws Exception;
}
