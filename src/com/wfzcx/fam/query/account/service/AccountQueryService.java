/**  
 * @Title: queryAccountService.java  
 * @Package com.wfzcx.fam.query.account.service  
 * @Description: TODO(用一句话描述该文件做什么)  
 * @author LiuJunBo  
 * @date 2015-4-22 下午03:34:59  
 * @version V1.0  
 */ 
 
 
package com.wfzcx.fam.query.account.service;

import java.util.List;
import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.workflow.vo.HistoryOpinionVO;
import com.wfzcx.fam.manage.po.FaApplications;

/** 
 * @ClassName: queryAccountService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author LiuJunBo
 * @date 2015-4-22 下午03:34:59  
 */
public interface AccountQueryService {
	
	/**
	 * 
	 * @Title: queryAccountArchive 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param param
	 * @param @return 设定文件 
	 * @return PaginationSupport 返回类型 
	 * @throws
	 */
	public PaginationSupport queryAccountArchive(Map<String, Object> param) throws  Exception ;
	
	/**
	 * 
	 * @Title: queryAccountHis 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param param
	 * @param @return 设定文件 
	 * @return PaginationSupport 返回类型 
	 * @throws
	 */
	public PaginationSupport queryAccountHis(Map<String, Object> param);
	
	/**
	 * 
	 * @Title: queryApplicationInfo 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param applicationId
	 * @param @return 设定文件 
	 * @return JSONObject 返回类型 
	 * @throws
	 */
	public List queryApplicationInfo(String applicationId);
	
	/**
	 * 获取工作流的流转信息
	 * @Title: getworkFlowList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @return 设定文件
	 */
	public List<HistoryOpinionVO> getworkFlowList(String wfid);
	
	public FaApplications getFaApplication(String applicationId);
}
