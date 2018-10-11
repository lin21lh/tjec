package com.wfzcx.ppms.wdjg.service;

import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;

public interface WdjgService {

	/**
	 * 文档结构查询
	 * @param map
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryWdjg(Map<String, Object> map) throws AppException;
	
	/**
	 * 文档结构添加
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String wdjgAddCommit(Map<String, Object> param) throws Exception;
	
	/**
	 * 文档结构修改
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String wdjgEditCommit(Map<String, Object> param) throws Exception;
	
	/**
	 * 文档结构删除
	 * @param wdjgid
	 * @throws AppException
	 */
	public void wdjgDelete(String wdjgid) throws AppException;
	
}
