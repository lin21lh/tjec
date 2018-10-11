package com.wfzcx.ppms.query.service;

import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;

public interface ProjectScheduleService {

	/**
	 * 项目进度列表查询
	 * @param map
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryProjectSchedule(Map<String, Object> map) throws AppException;
}
