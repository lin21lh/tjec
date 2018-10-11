package com.wfzcx.ppms.synthesize.expert.service;

import java.util.Map;

import com.jbf.common.dao.PaginationSupport;

public interface ExpertDatabaseServiceI {
	
	public PaginationSupport qryExpert(Map map);
	public PaginationSupport qryExpertWorked(Map map);
	public PaginationSupport qryQualification(Map map);
	public PaginationSupport qryAvoidUnitGrid(Map map);
	public void saveExpert(Map map) throws Exception;
	public void delExpert(Map map) throws Exception;

}
