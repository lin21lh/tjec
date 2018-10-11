package com.jbf.base.datascope.service;

import java.util.HashMap;
import java.util.List;

import com.jbf.base.datascope.vo.RoleDataScopeVo;
import com.jbf.common.exception.AppException;

public interface DataRightDetailsService {

	/**
	 * 
	 * @Title: findRoleDataScopeList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param resourceid
	 * @param @return 设定文件 
	 * @return List<RoleDataScopeVo> 返回类型 
	 * @throws
	 */
	public List<RoleDataScopeVo> findRoleDataRightList(Long resourceid);
	
	/**
	 * 查询默认数据权限
	 * @Title: getDefDataRightDetails 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param resourceid
	 * @param @return 设定文件 
	 * @return HashMap<String,Object> 返回类型  isTree 判断是否展示单位树； treeList 单位树集合；filterMsg 不展示单位树时，要展示的信息
	 * @throws
	 */
	public HashMap<String, Object> getDefDataRightDetails(Long resourceid) throws AppException;
}
