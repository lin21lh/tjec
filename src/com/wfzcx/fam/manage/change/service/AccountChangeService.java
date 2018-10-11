package com.wfzcx.fam.manage.change.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.workflow.vo.HistoryOpinionVO;
import com.wfzcx.fam.manage.po.FavApplAccount;
/**
 * 
 * @ClassName: AccountChangeService 
 * @Description: 账户变更interface
 * @author XinPeng
 * @date 2015年4月14日9:25:26
 */
public interface AccountChangeService {
	/**
	 * 
	 * @Title: queryAccount 
	 * @Description: 查询可变更列表
	 * @param @param param
	 * @param @return 设定文件 
	 * @return PaginationSupport 返回类型 
	 * @throws
	 */
	public PaginationSupport queryAccount(Map<String, Object> param)  throws AppException ;
	/**
	 * 
	 * @Title: queryChangeAccount 
	 * @Description: 查询已变更列表
	 * @param @param param
	 * @param @return 设定文件 
	 * @return PaginationSupport 返回类型 
	 * @throws
	 */
	public PaginationSupport queryChangeAccount(Map<String, Object> param)  throws AppException ;
	/**
	 * 
	 * @Title: changeAccountSaveAdd 
	 * @Description: TODO账户变更新增保存 
	 * @param @param param
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	public String changeAccountSaveAdd(Map<String, Object> param) throws AppException;
	/**
	 * 
	 * @Title: changeAccountSaveEdit 
	 * @Description: TODO账户变更修改保存 
	 * @param @param param
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	public String changeAccountSaveEdit(Map<String, Object> param) throws AppException;
	
	/**
	 * 
	 * @Title: changeAccountDelete 
	 * @Description: TODO账户变更删除
	 * @param @param applicationIds
	 * @param @return 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void changeAccountDelete(String applicationIds) throws AppException;
	/**
	 * 
	 * @Title: sendWorkFlow 
	 * @Description: TODO批量送审
	 * @param @param menuid
	 * @param @param applicationIds
	 * @param @throws AppException 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void sendWorkFlow(String menuid,String applicationIds,String activityId) throws AppException;
	/**
	 * 批量撤销
	 * @Title: revokeWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param menuid
	 * @param @param applicationIds
	 * @param @throws AppException 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public String revokeWorkFlow(String menuid,String applicationIds,String activityId) throws AppException;
	/**
	 * 
	 * @Title: queryChangeAccountForAudit 
	 * @Description: TODO查询变更审批
	 * @param @param param
	 * @param @return 设定文件 
	 * @return PaginationSupport 返回类型 
	 * @throws
	 */
	public PaginationSupport queryChangeAccountForAudit(Map<String, Object> param);
	
	/**
	 * 
	 * @Title: changeAccountApprove 
	 * @Description: TODO变更审批处理
	 * @param @param param
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return String 返回类型 
	 */
	public String changeAccountApprove(Map<String, Object> param) throws AppException;
	/**
	 * 工作流退回
	 * @Title: sendBackTask 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param applicationIds
	 * @param activityId
	 * @param opinion
	 * @return
	 * @throws AppException 设定文件
	 */
	public String sendBackTask(String menuid,String applicationIds,String activityId,String opinion) throws AppException;
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
	 * 除新增外的审核审批
	 * @Title: verifyWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws AppException 设定文件
	 */
	public String verifyWorkFlow(Map<String, Object> param) throws AppException;
	/**
	 * 获取工作流的流转信息
	 * @Title: getworkFlowList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @return 设定文件
	 */
	public List<HistoryOpinionVO> getworkFlowList(String wfid);
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
	 * 附件上传
	 * @Title: fileUpload 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param applicationId 业务id
	 * @param itemids 附件id
	 * @return
	 * @throws Exception 设定文件
	 */
	public boolean fileUpload(Integer applicationId,String itemids) throws Exception;
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
	 * 获取消息服务启用标志
	 * @Title: findGeneralParamValue 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @return 设定文件
	 */
	public String findGeneralParamValue();
	/**
	 * 获取是否可以退回首节点
	 * @Title: getCanBackToFirstNode 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @param activityId
	 * @return
	 * @throws Exception 设定文件
	 */
	public boolean getCanBackToFirstNode(String wfid,String activityId)  throws Exception;
	/**
	 * 获取原账户信息
	 * @Title: getOldAccountInformation 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param applicationId
	 * @return 设定文件
	 */
	public FavApplAccount getOldAccountInformation(String applicationId); 
	
	/**
	 * 通过菜单id获取菜单所有属性
	 * @Title: getResourceById 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @return
	 * @throws Exception 设定文件
	 */
	public SysResource getResourceById(String menuid) throws Exception;
	/**
	 * 获取wfid对应的业务数据的最后节点处理次数，如果次数大于0则返回true，否则false
	 * @Title: getOperatenum 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @return 设定文件
	 */
	public boolean getOperatenum(String wfid);
	
	/**
	 * 获取流程对应业务数据的变更事项
	 * @Title: getChangeType 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @return 设定文件
	 */
	public String getChangeType(String wfid);
}
