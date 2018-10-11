package com.jbf.sys.toRemind.service;

import java.util.List;

import com.jbf.common.exception.AppException;
import com.jbf.sys.toRemind.vo.ToRemindVo;
import com.jbf.sys.user.po.SysUser;

public interface ToRemindService {

	/**
	 * 查询工作流待办提醒
	 * @Title: findRemindResourceList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return List<SysResource> 返回类型 
	 * @throws
	 */
	public List<ToRemindVo> findRemindResourceList() throws AppException;
	
	public List<ToRemindVo> findRemindResourceListByUser(SysUser user) throws AppException;
}
