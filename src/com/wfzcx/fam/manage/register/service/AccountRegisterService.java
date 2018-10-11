package com.wfzcx.fam.manage.register.service;

import java.util.List;
import java.util.Map;

import com.jbf.common.exception.AppException;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.workflow.vo.HistoryOpinionVO;

/**
 * 账户开立Service接口
 * @ClassName: AccountRegisterService 
 * @Description: TODO(账户开立) 
 * @author MaQingShuang
 * @date 2015年4月14日 上午11:41:52
 */
public interface AccountRegisterService {

	/**
	 * 
	 * @Title: add 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param  param
	 * @return Integer 返回类型 
	 */
	public Integer add(Map<String, Object> param) throws Exception;
	
	/**
	 * 
	 * @Title: edit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param faAccount
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 */
	public String edit(Map<String, Object> param) throws AppException;
	
	/**
	 * 
	 * @Title: delete 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param applicationId
	 * @param @return 设定文件 
	 * @return  返回类型 
	 */
	public void delete(String applicationId) throws AppException;
	
	/**
	 * 工作流流程送审
	 * @Title: sendWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param key
	 * @param @param applicationId
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return void 返回类型 
	 */
	public void sendWorkFlow(String menuid, String applicationId,String activityId) throws Exception;
	
	/**
	 * 工作流审批
	 * @Title: auditWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws AppException 设定文件
	 */
	public String auditWorkFlow(Map<String, Object> param) throws AppException;
	
	/**
	 * 工作流流程撤回
	 * @Title: revokeWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param menuid 菜单id
	 * @param @param applicationId 账户开立业务id
	 * @param @param activityId 节点id
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return String 返回类型 
	 */
	public String revokeWorkFlow(String menuid, String applicationId,String activityId) throws AppException;
	
	public List<Map> getbdgagencyInformation(String bdgagencycode);
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
	 * 消息保存
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
	 * 获取工作流的流转信息
	 * @Title: getworkFlowList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @return 设定文件
	 */
	public List<HistoryOpinionVO> getworkFlowList(String wfid);
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
}
