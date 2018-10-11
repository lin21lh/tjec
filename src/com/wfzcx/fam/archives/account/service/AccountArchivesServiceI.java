package com.wfzcx.fam.archives.account.service;

import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;

public interface AccountArchivesServiceI {
	
	/**
	 * 查询档案表
	 * @Title: queryAccountArchives 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param param
	 * @param @return 设定文件 
	 * @return PaginationSupport 返回类型 
	 * @throws
	 */
	public PaginationSupport queryAccountArchives(Map<String, Object> param) throws AppException;
	
	/**
	 * 保存附件
	 * @Title: saveFileInfo 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param param
	 * @param @throws Exception 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveFileInfo(Map<String, Object> param)throws Exception;

}
