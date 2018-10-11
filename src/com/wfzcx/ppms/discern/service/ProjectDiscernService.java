package com.wfzcx.ppms.discern.service;

import java.util.List;
import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.workflow.vo.HistoryOpinionVO;

/**
 * 项目申报service
 * @ClassName: ProjectDiscernService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年9月12日 上午8:20:33
 */
public interface ProjectDiscernService {
	/**
	 * 项目查询
	 * @Title: queryProject 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @return
	 * @throws AppException 设定文件
	 */
	public PaginationSupport queryProject(Map<String, Object> map)  throws AppException;
	/**
	 * 项目保存提交
	 * @Title: projectAddCommit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws Exception 设定文件
	 */
	public String projectAddCommit(Map<String, Object> param) throws Exception;
	/**
	 * 查询项目产出物
	 * @Title: queryProduct 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @return
	 * @throws AppException 设定文件
	 */
	public PaginationSupport queryProduct(String projectid)  throws AppException;
	
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
	 * 项目修改保存
	 * @Title: projectEditCommit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws Exception 设定文件
	 */
	public String projectEditCommit(Map<String, Object> param) throws Exception;
	/**
	 * 
	 * @Title: projectDelete 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @throws AppException 设定文件
	 */
	public void projectDelete(String projectid) throws AppException;
	/**
	 * 送审工作流
	 * @Title: sendWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param projectid
	 * @param activityId
	 * @param wfid
	 * @return
	 * @throws Exception 设定文件
	 */
	public String sendWorkFlow(String menuid,String projectid,String activityId,String wfid) throws Exception ;
	/**
	 * 撤回工作流
	 * @Title: revokeWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @param projectid
	 * @param activityId
	 * @return
	 * @throws Exception 设定文件
	 */
	public String revokeWorkFlow(String wfid,String projectid,String activityId) throws Exception;
	/**
	 * 第三方评审机构查询
	 * @Title: queryThirdOrg 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @return
	 * @throws AppException 设定文件
	 */
	public PaginationSupport queryThirdOrg(String projectid)  throws AppException;
	/**
	 * 财政预算支出查询
	 * @Title: queryFinance 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @return
	 * @throws AppException 设定文件
	 */
	public PaginationSupport queryFinance(String projectid)  throws AppException;
	/**
	 * 项目审批
	 * @Title: auditCommit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws Exception 设定文件
	 */
	public String auditCommit(Map<String, Object> param) throws Exception;
	/**
	 * 查询项目识别情况
	 * @Title: queryApprove 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @return
	 * @throws Exception 设定文件
	 */
	public List queryApprove(String projectid) throws Exception;
	/**
	 * 审批退回
	 * @Title: auditOperate 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws Exception 设定文件
	 */
	public String auditOperate(Map<String, Object> param) throws Exception;
	/**
	 * 获取工作流的流转信息
	 * @Title: getworkFlowList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @return 设定文件
	 */
	public List<HistoryOpinionVO> getworkFlowList(String wfid);
	
	/**
	 * 更新项目当前环节
	 * @param projectid
	 * @param xmdqhj
	 * @throws Exception
	 */
	public void updateXmdqhj(String projectid,String xmdqhj) throws Exception ;
}
