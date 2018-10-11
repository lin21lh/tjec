package com.wfzcx.fam.manage.record.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.workflow.vo.HistoryOpinionVO;
import com.wfzcx.fam.manage.po.FaAccounts;
import com.wfzcx.fam.manage.po.FaApplications;

/**
 * 
 * @ClassName:AccountRecordService
 * @Description: 账户备案interface
 * @author XinPeng
 * @date 2015年4月21日9:31:11
 */
public interface AccountRecordService {
	/**
	 * 
	 * @Title: queryAccount 
	 * @Description: 查询可变更列表
	 * @param @param param
	 * @param @return 设定文件 
	 * @return PaginationSupport 返回类型 
	 * @throws
	 */
	public PaginationSupport queryChangeAccount(Map<String, Object> param) throws AppException;
	/**
	 * 账户可备案查询
	 * @Title: queryAccount 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws AppException 设定文件
	 */
	public PaginationSupport queryAccount(Map<String, Object> param) throws AppException;
	
	/**
	 * 
	 * @Title: saveSubmitRecordInfo 
	 * @Description: TODO(保存账户信息表) 
	 * @param @param faAccounts 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveSubmitRecordInfo(Map<String, Object> map,FaAccounts faAccounts) throws Exception;
	
	/**
	 * 
	 * @Title: removeRecordInfo 
	 * @Description: TODO(账户备案-撤销) 
	 * @param @param map 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public String removeRecordInfo(Map<String, Object> map) throws Exception;
	
	/**
	 * 
	 * @Title: submitRecordInfo 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	public String submitRecordInfo(Map<String, Object> map) throws Exception;
	
	/**
	 * 
	 * @Title: getTaskFormEditable 
	 * @Description: TODO(获取是否可编辑) 
	 * @param @param wfid
	 * @param @param activityId
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return boolean 返回类型 
	 * @throws
	 */
	public boolean getTaskFormEditable(String wfid, String activityId) throws Exception;
	
	/**
	 * 
	 * @Title: getOutComes 
	 * @Description: TODO(获取流出路径) 
	 * @param @param wfid
	 * @param @param activityId
	 * @param @param outcomeType
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return Set<String> 返回类型 
	 * @throws
	 */
	public Set<String> getOutComes(String wfid, String activityId,String outcomeType) throws Exception;
	/**
	 * 
	 * @Title: verifyWorkFlow 
	 * @Description: TODO(除新增外的审核审批(单个明细)) 
	 * @param @param param
	 * @param @param faApplication
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return String 返回类型 
	 */
	public String verifyWorkFlow(Map<String, Object> param,FaApplications faApplication) throws AppException,Exception;
	
	/**
	 * 消息服务
	 * @Title: messageService 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param applicationId
	 * @param @param phonenumber
	 * @param @param message
	 * @param @param activityId
	 * @param @throws Exception 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void messageService(Integer applicationId, String phonenumber,String message,String activityId) throws Exception;
	
	/**
	 * 获取流程信息
	 * @Title: getworkFlowList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param wfid
	 * @param @return 设定文件 
	 * @return List<HistoryOpinionVO> 返回类型 
	 * @throws
	 */
	public List<HistoryOpinionVO> getworkFlowList(String wfid);
	
	/**
	 * 
	 * @Title: getResourceById 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param menuid
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return SysResource 返回类型 
	 * @throws
	 */
	public SysResource getResourceById(String menuid) throws Exception;
}
