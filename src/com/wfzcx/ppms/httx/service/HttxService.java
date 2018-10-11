package com.wfzcx.ppms.httx.service;

import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;

public interface HttxService {

	/**
	 * 合同查询
	 * @param map
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryHttx(Map<String, Object> map) throws AppException;
	
	/**
	 * 合同新增保存
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String httxAddCommit(Map<String, Object> param) throws Exception;
	
	/**
	 * 合同追加新增保存
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String httxAppendCommit(Map<String, Object> param) throws Exception;
	
	/**
	 * 合同修改保存
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String httxEditCommit(Map<String, Object> param) throws Exception;
	
	/**
	 * 查询项目列表
	 * @param map
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryProject(Map<String, Object> map) throws AppException;
}
