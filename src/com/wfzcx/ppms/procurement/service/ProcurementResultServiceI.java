package com.wfzcx.ppms.procurement.service;

import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.sys.resource.po.SysResource;

public interface ProcurementResultServiceI {
	public SysResource getResourceById(String menuid) throws Exception;
	public PaginationSupport qryProRes(Map map);
	public PaginationSupport advanceOrganUrl(Map map);
	public void saveProRes(Map map) throws Exception; 
	public void saveAuditData(Map map) throws Exception; 
	public String sendWorkFlow(Map map) throws Exception;
	public void delProRes(Map map) throws  Exception;
	public String revokeWorkFlow(String wfid,String purchaseid,String activityId) throws Exception;
	public String backWorkFlow(Map<String, Object> param) throws Exception;
	public PaginationSupport resultExpertGrid(Map map);
	public PaginationSupport resultGroupGrid(Map map);
	public PaginationSupport qryExpertByQ(Map map);
}
