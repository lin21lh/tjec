package com.jbf.sys.sortfieldset.service;

import java.util.List;
import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.sys.sortfieldset.po.SysSortFieldSet;

public interface SysSortFieldSetService {
	
	public PaginationSupport query(Map<String, Object> paramMap, Integer pageSize, Integer pageIndex);
	
	/**
	 * 
	 * @Title: add 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param sortFieldSet 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void add(SysSortFieldSet sortFieldSet);
	
	/**
	 * 
	 * @Title: edit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param sortFieldSet 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void edit(SysSortFieldSet sortFieldSet);
	
	/**
	 * 
	 * @Title: delete 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param sortid 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void delete(Long sortid);

	/**
	 * 
	 * @Title: queryListByApp 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param menuid
	 * @param @return 设定文件 
	 * @return List<SysSortFieldSet> 返回类型 
	 * @throws
	 */
	public List<SysSortFieldSet> queryListByApp(Long menuid);
}
