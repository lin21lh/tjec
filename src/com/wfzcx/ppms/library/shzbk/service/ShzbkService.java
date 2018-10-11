package com.wfzcx.ppms.library.shzbk.service;

import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;

public interface ShzbkService {
	
	/**
	 * 查询社会资本库
	 * @param map
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryShzbk(Map<String, Object> map)  throws AppException;
	
	/**
	 * 社会资本新增保存
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String shzbkAddCommit(Map<String, Object> param) throws Exception;
	
	/**
	 * 社会资本修改保存
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String shzbkEditCommit(Map<String, Object> param) throws Exception;
	
	/**
	 * 社会资本删除
	 * @param socialid
	 * @throws AppException
	 */
	public void shzbkDelete(String socialid) throws AppException;
	
	/**
	 * 用户关联查询
	 * @param map
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryShzbkRelevance(Map<String, Object> map)  throws AppException;

	/**
	 * 标签页用户关联保存
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String shzbkRelevanceCommit(Map<String, Object> param) throws Exception;
	
	/**
	 * 录入验证
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String shzbkCheck(Map<String, Object> param) throws Exception;
}
