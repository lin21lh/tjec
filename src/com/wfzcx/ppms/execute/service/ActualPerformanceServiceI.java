package com.wfzcx.ppms.execute.service;

import java.util.Map;

import com.jbf.common.dao.PaginationSupport;

public interface ActualPerformanceServiceI {
	public PaginationSupport qryActPer(Map map);
	public PaginationSupport qryProPerformance(Map map);
	public void saveActPer(Map map) throws Exception;
	public void delProPerformance(Map map) throws Exception;
}
