package com.wfzcx.ppms.discern.service;

import java.util.Map;

import com.jbf.sys.resource.po.SysResource;

public interface ProjectVerifyService {
	/**
	 * 通过菜单id获取菜单所有属性
	 * @Title: getResourceById 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @return
	 */
	public SysResource getResourceById(String menuid) throws Exception;
	public String sendWorkFlow(Map map) throws Exception ;
	public String backWorkFlow(Map<String, Object> param) throws Exception;

}
