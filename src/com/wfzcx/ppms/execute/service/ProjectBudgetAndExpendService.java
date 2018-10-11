package com.wfzcx.ppms.execute.service;

import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;

/**
 * 项目执行预算和支出service
 * @ClassName: ProjectBudgetAndExpendService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年9月12日 上午8:20:33
 */
public interface ProjectBudgetAndExpendService {
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
	 * 查询项目的预算或支出情况
	 * @Title: budgetQueryUrl 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @param budgetType 1:预算,2:支出
	 * @return 设定文件
	 */
	public PaginationSupport budgetQueryUrl(String projectid,String budgetType);

	public void budgetSave(Map<String, Object> param) throws Exception;
}
