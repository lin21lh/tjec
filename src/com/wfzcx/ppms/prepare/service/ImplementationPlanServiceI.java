package com.wfzcx.ppms.prepare.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.sys.resource.po.SysResource;

public interface ImplementationPlanServiceI {
	public SysResource getResourceById(String menuid) throws Exception;
	public PaginationSupport qryImlPlan(Map<String, Object> param);
	public void saveImlPlan(Map<String, Object> map) throws AppException,IllegalAccessException,InvocationTargetException,Exception;
	public void saveAuditData(Map<String, Object> map) throws Exception;
	public void delImlPlan(Map<String, Object> map) throws AppException;
	public String sendWorkFlow(Map map) throws Exception ;
	public String revokeWorkFlow(String wfid,String projectid,String activityId) throws Exception;
	public String backWorkFlow(Map<String, Object> param) throws Exception;
	public PaginationSupport queryThirdOrg(String projectid)throws AppException;
	public PaginationSupport queryFinance(String projectid) throws AppException;
	public PaginationSupport qualUnitGrid(Map map) throws AppException;
	public PaginationSupport financeUnitGrid(Map map) throws AppException;
	public PaginationSupport qualExpertGrid(Map map) throws AppException;
	public List queryApprove(String projectid) throws Exception;
	public PaginationSupport qryExpertByQ(Map map);

	public String sendWorkFlowForVerify(Map map) throws Exception ;
}
