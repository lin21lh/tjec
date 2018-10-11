package com.wfzcx.ppms.library.dsfjgk.service;

import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;

public interface DsfjgkService {

	/**
	 * 第三方资本查询
	 * @param map
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryDsfjgk(Map<String, Object> map) throws AppException;
	
	/**
	 * 第三方资本添加
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String dsfjgkAddCommit(Map<String, Object> param) throws Exception;
	
	/**
	 * 第三方资本修改
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String dsfjgkEditCommit(Map<String, Object> param) throws Exception;
	
	/**
	 * 第三方资本删除
	 * @param dsfjgid
	 * @throws AppException
	 */
	public void dsfjgkDelete(String dsfjgid) throws AppException;
	
	/**
	 * 第三方资本录入验证
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String dsfjgkCheck(Map<String, Object> param) throws Exception;
}
