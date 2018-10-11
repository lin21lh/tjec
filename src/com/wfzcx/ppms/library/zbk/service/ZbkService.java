package com.wfzcx.ppms.library.zbk.service;

import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;

public interface ZbkService {

	/**
	 * 指标查询
	 * @param map
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryZbk(Map<String, Object> map) throws AppException;
	
	/**
	 * 指标添加
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String zbkAddCommit(Map<String, Object> param) throws Exception;
	
	/**
	 * 指标修改
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String zbkEditCommit(Map<String, Object> param) throws Exception;
	
	/**
	 * 指标删除
	 * @param dsfjgid
	 * @throws AppException
	 */
	public void zbkDelete(String zbkid) throws AppException;
	
	/**
	 * 指标录入验证
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String zbkCheck(Map<String, Object> param) throws Exception;
}
