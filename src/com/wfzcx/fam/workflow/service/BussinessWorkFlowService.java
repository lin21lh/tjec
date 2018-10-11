package com.wfzcx.fam.workflow.service;

import java.util.List;
import java.util.Map;

import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.common.exception.AppException;
import com.wfzcx.fam.workflow.vo.ActivitiyVO;

public interface BussinessWorkFlowService {

	/**
	 * 获取指定工作流KEY及版本号的所有任务节点集合
	 * @Title: findActivitiesByWfKey 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param key
	 * @param @param wfversion
	 * @param @param activitiyid
	 * @param @param activitiyname
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return List<ActivitiyVO> 返回类型 
	 * @throws
	 */
	public List<ActivitiyVO> findActivitiesByWfKey(String key, Integer wfversion, String activitiyid, String activitiyname) throws AppException;
	
	/**
	 * 获取指定工作流KEY的所有版本号
	 * @Title: findVersionListByKey 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param key
	 * @param @return 设定文件 
	 * @return List<Integer> 返回类型 
	 * @throws
	 */
	public List<Map<String, Object>> findVersionListByKey(String key) throws AppException;
	
	/**
	 * 获取指定工作流KEY的运行版本号
	 * @Title: getCurrentVersion 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param key
	 * @param @return 设定文件 
	 * @return Integer 返回类型 
	 * @throws
	 */
	public Integer getCurrentVersion(String key);
	
	/**
	 * 通过工作流查询当前业务表所有定义字段
	 * @Title: findColumnsByWfKey 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param wfkey
	 * @param @return 设定文件 
	 * @return List<SysDicColumn> 返回类型 
	 * @throws
	 */
	public List<SysDicColumn> findColumnsByWfKey(String wfkey, Boolean sourceElementFlag) throws AppException;
}
