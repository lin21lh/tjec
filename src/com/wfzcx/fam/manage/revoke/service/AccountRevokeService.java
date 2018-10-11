package com.wfzcx.fam.manage.revoke.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.vo.HistoryOpinionVO;
import com.wfzcx.fam.manage.po.FaApplications;
import com.wfzcx.fam.manage.po.FavApplAccount;

/**
 * 
 * @ClassName: AccountRevokeService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author LiuJunBo
 * @date 2015-4-14 上午11:00:57
 */
public interface AccountRevokeService {
	/**
	 * 
	 * @Title: queryNoRevoke 
	 * @Description: TODO(查询-可注销的账户-业务层) 
	 * @param @param param
	 * @param @return 设定文件 
	 * @return PaginationSupport 返回类型 
	 */
	public PaginationSupport queryNoRevoke(Map<String, Object> param);
	
	
	/**
	 * 
	 * @Title: queryHasRevokeByView 
	 * @Description: TODO(查询(视图)-提出注销的账户-业务层) 
	 * @param @param param
	 * @param @return 设定文件 
	 * @return PaginationSupport 返回类型 
	 */
	public PaginationSupport queryHasRevokeByView(Map<String, Object> param);
	
	/**
	 * 
	 * @Title: editRevokeInfo 
	 * @Description: TODO(编辑——提出注销账户信息) 
	 * @param @param faApplications
	 * @param @param map
	 * @param @throws Exception 设定文件 
	 * @return void 返回类型 
	 */
	public void editRevokeInfo(FaApplications faApplications,Map<String,Object> map) throws Exception;
	public void saveEditRevokeInfoNoWF(FaApplications faApplications,Map<String,Object> map) throws Exception;
	
	/**
	 * 
	 * @Title: saveRevokeInfo 
	 * @Description: TODO(保存-新增的注销账户信息) 
	 * @param @param favApplAccount
	 * @param @throws Exception 设定文件 
	 * @return void 返回类型 
	 */
	public void saveRevokeInfo(Map<String,Object> map,String itemids) throws Exception;
	/**
	 * 不用工作流保存
	 * @Title: saveAddRevokeInfoNoWF 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @param itemids
	 * @throws Exception 设定文件
	 */
	public void saveAddRevokeInfoNoWF(Map<String,Object> map,String itemids) throws Exception;
	
	/**
	 * 
	 * @Title: deleteRevokeInfo 
	 * @Description: TODO(删除-账户注销) 
	 * @param @param applicationIds
	 * @param @throws Exception 设定文件 
	 * @return void 返回类型 
	 */
	public void deleteRevokeInfo(String applicationIds) throws Exception;
	
	/**
	 * 
	 * @Title: removeRevokeInfo 
	 * @Description: TODO(注销-账户注销) 
	 * @param @param map
	 * @param @throws Exception 设定文件 
	 * @return void 返回类型 
	 */
	public String removeRevokeInfo(String applicationIds,String key,String activityId,String isba) throws Exception;
	
	/**
	 * 
	 * @Title: submitRevokeInfo 
	 * @Description: TODO(送审-账户注销) 
	 * @param @param spplicationId
	 * @param @throws Exception 设定文件 
	 * @return void 返回类型 
	 */
	public void submitRevokeInfo(String spplicationIds,String key,String activityId) throws Exception;
	
	/**
	 * 获取流程节点是否可编辑
	 * @Title: getTaskFormEditable 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @param activityId
	 * @return
	 * @throws Exception 设定文件
	 */
	public boolean getTaskFormEditable(String wfid,String activityId)  throws Exception;
	/**
	 * 获取流程节点的流出路径
	 * @Title: getOutComes 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @param activityId
	 * @param outcomeType
	 * @return
	 * @throws Exception 设定文件
	 */
	public Set<String> getOutComes(String wfid,String activityId,String outcomeType)throws Exception;
	
	/**
	 * 
	 * @Title: verifyWorkFlow 
	 * @Description: TODO(除新增外的审核审批(单个明细)) 
	 * @param @param param
	 * @param @param faApplication
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return String 返回类型 
	 * @throws Exception 
	 */
	public String verifyWorkFlow(Map<String, Object> param,FaApplications faApplication) throws AppException, Exception;
	/**
	 * 除新增外的审核审批(批量)
	 * @Title: verifyWorkFlow 
	 * @Description: TODO(审核批量) 
	 * @param param
	 * @return
	 * @throws AppException 设定文件
	 */
	public String verifyWorkFlow(Map<String, Object> param) throws AppException;
	/**
	 * 保存消息并发送
	 * @Title: saveMessage 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param applicationId
	 * @param phone
	 * @param message
	 * @param activityId
	 * @return 设定文件
	 */
	public int saveMessage(Integer applicationId,String phone,String message,String activityId);
	
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
	
	/**
	 * 获取工作流中的参与人姓名及手机号信息
	 * @Title: getSendUser 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param wfid
	 * @param @param activityId
	 * @param @return 设定文件 
	 * @return List<Map> 返回类型 
	 * @throws
	 */
	public List<Map> getSendUser(String wfid,String activityId);
	
	/**
	 * 
	 * @Title: getMessageContent 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param applicationId 
	 * @param wfid 工作流id
	 * @param isBa 备案标志
	 * @param cllx 处理类型 1、审批、2退回，3退回首节点
	 * @param activityId 节点代码
	 * @return 设定文件
	 */
	public String getMessageContent(String applicationId,String wfid,String isBa,String cllx,String activityId);
	
	public PaginationSupport queryChangeAccount(Map<String, Object> param)  throws AppException ;
}
