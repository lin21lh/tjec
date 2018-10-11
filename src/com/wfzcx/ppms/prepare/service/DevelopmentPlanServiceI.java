package com.wfzcx.ppms.prepare.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;

public interface DevelopmentPlanServiceI {
	/**
	 * 查询项目计划
	 * @Title: qryDevPlan 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param param
	 * @param @return 设定文件 
	 * @return PaginationSupport 返回类型 
	 * @throws
	 */
	public PaginationSupport qryDevPlan(Map<String, Object> param);
	/**
	 * b保存项目计划
	 * @Title: saveDevPlan 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param map
	 * @param @throws AppException
	 * @param @throws IllegalAccessException
	 * @param @throws InvocationTargetException 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveDevPlan(Map<String, Object> map) throws AppException,IllegalAccessException,InvocationTargetException;
	/**
	 * 删除开发计划
	 * @Title: delDevPlan 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param map
	 * @param @throws AppException 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void delDevPlan(Map<String, Object> map) throws AppException;
	
	/**
	 * 查询实施方案是否已审核通过
	 * @Title: qryImlplanIsAudit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param map
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return boolean 返回类型 
	 * @throws
	 */
	public boolean qryImlplanIsAudit(Map<String, Object> map) throws AppException;
}
