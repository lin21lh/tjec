package com.wfzcx.ppms.workflow.component;

import java.util.Set;

import com.jbf.common.exception.AppException;
import com.jbf.sys.resource.po.SysResource;

/**
 * PPP项目统一工作流方法调用
 * @ClassName: ProjectWorkFlowComponent 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年9月15日 上午11:21:43
 */
public interface ProjectWorkFlowComponent {
	
	 public String getWfkeyByMenuid(String menuid) throws AppException;
	 /**
	 * 通过菜单id获取菜单所有属性
	 * @Title: getResourceById 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @return 设定文件
	 */
	public SysResource getResourceById(String menuid) throws Exception;
	/**
	 * 查询当前节点（待处理和已处理）
	 * @Title: findCurrentWfids 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param activityId
	 * @param status
	 * @param firstNode
	 * @param LastNode
	 * @return
	 * @throws AppException 设定文件
	 */
	public String findCurrentWfids(String menuid, String activityId, String status, Boolean firstNode, Boolean LastNode) throws AppException;
	
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
	 * 带表别名的
	 * @Title: findCurrentWfids 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param activityId
	 * @param status
	 * @param firstNode
	 * @param LastNode
	 * @param alias
	 * @return
	 * @throws AppException 设定文件
	 */
	public String findCurrentWfids(String menuid, String activityId, String status, Boolean firstNode, Boolean LastNode,String alias) throws AppException;
}
