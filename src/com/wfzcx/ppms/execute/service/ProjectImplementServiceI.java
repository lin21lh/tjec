package com.wfzcx.ppms.execute.service;

import java.util.Map;

import com.jbf.common.dao.PaginationSupport;

public interface ProjectImplementServiceI {

	public PaginationSupport qryProImp(Map map);
	public PaginationSupport qryProFinance(Map map);
	public void saveProImp(Map map)throws Exception;
	public void subProImp(Map map) throws Exception;
	public void revokeProImp(Map map) throws Exception;
	public void delProImp(Map map) throws Exception;
	public void delProFinance(Map map) throws Exception;
	
}
