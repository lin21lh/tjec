package com.wfzcx.ppms.discern.service;

import java.util.List;
import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;


public interface ProjectPrepareDxpjService {
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
	 * 查询是否已经存在定性评分
	 * @Title: queryIsExistDxpj 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @return
	 * @throws Exception 设定文件
	 */
	public List queryIsExistDxpj(String projectid,String xmhj) throws Exception;
	/**
	 * 获取指标评分的列
	 * @Title: getZbdfGridColumns 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param pszbid
	 * @return
	 * @throws Exception 设定文件
	 */
	public String getZbdfGridColumns(String pszbid)  throws Exception;
	
	/**
	 * 查询评价指标列表
	 * @Title: queryZbList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @return
	 * @throws AppException 设定文件
	 */
	
	public PaginationSupport queryZbList(Map map) throws AppException;
	
	public void zbdfSave(Map<String, Object> param) throws Exception;
	/**
	 * 提交定性分析
	 * @Title: sendPszb 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @param dxpjid
	 * @throws Exception 设定文件
	 */
	public void sendDxpj(String projectid,String dxpjid)throws Exception;
	
	public void revokeDxpj(String projectid,String dxpjid)throws Exception;
}
