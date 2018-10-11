package com.wfzcx.ppms.wdjg.service;

import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.wfzcx.ppms.discern.po.ProProject;

public interface WdzsService {

	/**
	 * 项目查询
	 * @param map
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryProject(Map<String, Object> map)  throws AppException;
	
	/**
	 * 查询文档结构
	 * 展示详细
	 * @param projectid
	 * @return
	 */
	public List queryWdzs(String projectid);
	
	/**
	 * 获取项目类
	 * @param projectid
	 * @return
	 */
	public ProProject getProject(String projectid);
}
