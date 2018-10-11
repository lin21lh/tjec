package com.wfzcx.ppms.execute.service;

import java.util.Map;

import com.jbf.common.dao.PaginationSupport;

public interface ProjectAssessServiceI {
	public PaginationSupport qryProAss(Map map);
	public PaginationSupport qryProAssess(Map map);
	public void saveProAss(Map map) throws Exception;
	public void delProAssess(Map map) throws Exception;
}
