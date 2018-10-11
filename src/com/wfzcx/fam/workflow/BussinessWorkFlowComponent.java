package com.wfzcx.fam.workflow;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jbf.common.exception.AppException;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.workflow.vo.HistoryOpinionVO;


public interface BussinessWorkFlowComponent {
	
	/**
	 * 业务工作流首节点 流程实例查询
	 * @Title: findFirstNodeWfids 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param activityId
	 * @param status 待处理1和已处理2
	 * @param processedStatus
	 * @return String 返回类型 
	 */
	public String findFirstNodeWfids(String menuid, String activityId, String status, String processedStatus) throws AppException;
	
	/**
	 * 
	 * @Title: findCurrentWfids 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param menuid
	 * @param @param activityId
	 * @param @param status 待处理1和已处理2
	 * @param @param processedStatus
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 */
	public String findCurrentWfids(String menuid, String activityId, String status, String processedStatus) throws AppException;
	
	/**
	 * 
	 * @Title: findLastNodeWfids 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param menuid
	 * @param @param activityId
	 * @param @param status 待处理1和已处理2
	 * @param @param processedStatus
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 */
	public String findLastNodeWfids(String menuid, String activityId, String status, String processedStatus) throws AppException;

	/**
	 * 账户开立、变更、撤销工作流查询
	 * @Title: findCurrentWfids 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param menuid
	 * @param @param activityId
	 * @param @param status 待处理1和已处理2
	 * @param @param processedStatus
	 * @param @param firstNode
	 * @param @param LastNode
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 */
	public String findCurrentWfids(String menuid, String activityId, String status, String processedStatus, Boolean firstNode, Boolean LastNode) throws AppException;
	/**
	 * 备案工作流查询
	 * @Title: findCurrentWfidsForBa 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param menuid
	 * @param @param activityId
	 * @param @param status 待处理1和已处理2
	 * @param @param processedStatus
	 * @param @param firstNode
	 * @param @param LastNode
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 */
	public String findCurrentWfidsForBa(String menuid, String activityId, String status, String processedStatus, Boolean firstNode, Boolean LastNode) throws AppException;

	/**
	 * 账户新开立、变更、撤销工作流送审
	 * @Title: sendWorkFlow 
	 * @Description: TODO工作流送审
	 * @param @param 菜单menuid
	 * @param @param applicationIds 新开立、变更、撤销申请的id,多个用逗号分隔
	 * @param @param variables  流程环境变量 
	 * @param @param isba  是否备案 1是备案流程，其他时新开立、变更、撤销申请
	 * @param @return
	 * @return String 返回类型 
	 * @author XinPeng 2015年4月22日13:46:31
	 */
	public String sendWorkFlow(String menuid, String  applicationIds,Map variables,String isba) throws AppException;
	
	/**
	 * 
	 * @Title: completeTask 
	 * @Description: TODO工作流审批
	 * @param @param menuid 菜单menuid
	 * @param @param applicationIds applicationIds 新开立、变更、撤销申请的id,多个用逗号分隔
	 * @param @param variables 流程环境变量 
	 * @param @param outcome 流出路径
	 * @param @param opinion 审批意见
	 * @param @param isback 是否退回 1退回，其它是审批
	 * @param @param isba  是否备案 1是备案流程，其他时新开立、变更、撤销申请
	 * @return String 返回类型 
	 */
	public String completeTask(String applicationIds, Map variables,String outcome,String opinion,String isback,String activityId,String isba) throws AppException;
	/**
	 * 
	 * @Title: sendBackTask 
	 * @Description: TODO工作流退回
	 * @param @param applicationIds
	 * @param @param activityId
	 * @param @param variables
	 * @param @param opinion
	 * @param @param isba  是否备案 1是备案流程，其他时新开立、变更、撤销申请
	 * @param @return
	 * @return String 返回类型 
	 */
	public String sendBackTask(String applicationIds, String activityId,Map variables,String opinion,String isba) throws AppException;
	/**
	 * 工作流撤回
	 * @Title: revokeWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param applicationIds
	 * @param variables
	 * @param opinion
	 * @param activityId
	 * @param @param isba  是否备案 1是备案流程，其他时新开立、变更、撤销申请
	 * @return
	 * @throws AppException 设定文件
	 */
	public String revokeWorkFlow(String applicationIds, Map variables,String opinion,String activityId,String isba)throws AppException;
	/**
	 * 根据工作流和节点获取流出路径
	 * @Title: getOutCome 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @param activityId
	 * @param outcomeType
	 * @return
	 * @throws Exception 设定文件
	 */
	public Set<String> getOutCome(String wfid,String activityId,String outcomeType)throws Exception;
	/**
	 * 查询流程流转明细信息 
	 * @Title: getworkFlowList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid 工作流id
	 * @return 设定文件
	 */
	public List<HistoryOpinionVO> getworkFlowList(String wfid);
	/**
	 * 获取流程节点是否可编辑
	 * @Title: getTaskFormEditable 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid  工作流id
	 * @param activityId 节点id
	 * @return 设定文件
	 */
	public boolean getTaskFormEditable(String wfid,String activityId)  throws Exception;
	/**
	 * 退回首节点
	 * @Title: sendBackWorkflowToFirstNode 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param applicationIds
	 * @param activityId
	 * @param variables
	 * @param assignee
	 * @param opinion
	 * @param isba
	 * @return
	 * @throws AppException 设定文件
	 */
	public String sendBackWorkflowToFirstNode(String applicationIds, String activityId,Map variables, String assignee, String opinion,String isba)throws AppException;
	/**
	 * 获当前节点是否可以返回首节点
	 * @Title: getCanBackToFirstNode 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid 工作流id
	 * @param activityId 节点id
	 * @return
	 * @throws Exception 设定文件
	 */
	public boolean getCanBackToFirstNode(String wfid,String activityId)  throws Exception;
	/**
	 * 通过菜单id获取菜单所有属性
	 * @Title: getResourceById 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @return 设定文件
	 */
	public SysResource getResourceById(String menuid) throws Exception;
	/**
	 * 根据wfid获取该流程走过的节点人信息
	 * @Title: getSendUser 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @return 设定文件
	 */
	public List<Map> getSendUser(String wfid,String activityId);
	/**
	 * 查询消息内容
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
}
