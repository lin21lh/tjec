package com.wfzcx.ppms.workflow.component.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.resource.dao.SysResourceDao;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.component.SysWorkflowManageComponent;
import com.jbf.workflow.vo.UserTodoListVo;
import com.wfzcx.fam.common.MessageComponent;
import com.wfzcx.ppms.workflow.component.ProjectWorkFlowComponent;
@Scope("prototype")
@Component
public class ProjectWorkFlowComponentImpl implements ProjectWorkFlowComponent {

	@Autowired
	SysWorkflowManageComponent workflowManageComponent;
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	SysResourceDao sysResourceDao;
	@Autowired
	MessageComponent messageComponent;//短息服务
	@Autowired
	ParamCfgComponent pcfg;
	@Override
	/**
	 * 根据菜单id获取工作流标识
	 * @Title: getWfkeyByMenuid 
	 * @Description: TODO根据菜单id获取工作流标识
	 * @param @param menuid
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return String 返回类型 
	 */
	 public String getWfkeyByMenuid(String menuid) throws AppException{
			//查找菜单对应的工作流标识
		   String wfKey ="";
			List list = sysResourceDao.findMapBySql("select wfkey from sys_resource where resourceid="+menuid);
			if(list.isEmpty()){
				throw new AppException("未找到菜单id为"+menuid+"的菜单（sys_resource）");
			}else{
				Map map = (Map) list.get(0);
				wfKey = map.get("wfkey")==null?"":map.get("wfkey").toString();
				if("".equals(wfKey)){
					throw new AppException("菜单id为"+menuid+"未设置工作流标识wfkey");
				}
			}
		return wfKey;
	}
	 /**
	  * 更新业务表的状态
	  * @Title: updateBusiTable 
	  * @Description: TODO(这里用一句话描述这个方法的作用) 
	  * @param tableName
	  * @param keyName
	  * @param keyId
	  * @param status 设定文件
	  */
	 public void updateBusiTable(String tableName,String keyName,String keyId,String status){
		 String sqlString = "UPDATE "+tableName +" set status ='"+status+"' where "+keyName+" = "+keyId;
		 mapDataDao.updateTX(sqlString);
	 }
	 @Override
	public SysResource getResourceById(String menuid) throws Exception{
		SysResource srResource = new SysResource();
		try {
			srResource  = sysResourceDao.get(Long.parseLong(menuid));
		} catch (Exception e) {
			throw new AppException("获取菜单id为"+menuid+"的菜单属性时发生异常！");
		} 
		return srResource;
	}
	 @Override
	public String findCurrentWfids(String menuid, String activityId, String status, Boolean firstNode, Boolean LastNode) throws AppException {
		String wfKey = getWfkeyByMenuid(menuid);
		SysUser user = SecureUtil.getCurrentUser();
		String usercode =user.getUsercode();
		StringBuffer wfidWhere = new StringBuffer(); //返回拼接wfid的条件
		//获取当前节点的所有任务
		String wfidsString = getCurrWorkFlow(usercode, wfKey, activityId);
		switch (Integer.valueOf(status)) {
		case 1: //待处理（未处理、被退回、被撤回）
			if (firstNode) {//首节点要判断未发送工作流的
				wfidWhere.append(" ((wfid  is null )");
				if(!"".equals(wfidsString)){
					wfidWhere.append(" or (wfid in('").append(wfidsString).append("'))");
				}
				wfidWhere.append(")");
			}else {
				if(!"".equals(wfidsString)){
					wfidWhere.append("  (wfid in('").append(wfidsString).append("'))");
				}
			}
			break;
		case 2: //已处理
			//获取已处理的数据
			String hisWfid  = workflowManageComponent.getUserHistoryExecidsByWfKey(user.getUsercode(), wfKey, activityId);
			if (StringUtil.isNotBlank(hisWfid)) {
				hisWfid = hisWfid.replaceAll(",", "','");
				wfidWhere.append(" (wfid in ('").append(hisWfid).append("')");
				wfidWhere.append(" )");
				//已处理的中排除未处理的（被退回的）
				if(!"".equals(wfidsString)){
					wfidWhere.append("  and (wfid not in('").append(wfidsString).append("'))");
				}
			}
			break;
		default:
			break;
		}
		if (StringUtil.isBlank(wfidWhere.toString())) {
			wfidWhere.append(" (1<>1) ");
		}
		return wfidWhere.toString();
	}
	 @Override
		public String findCurrentWfids(String menuid, String activityId, String status, Boolean firstNode, Boolean LastNode,String alias) throws AppException {
			String wfKey = getWfkeyByMenuid(menuid);
			SysUser user = SecureUtil.getCurrentUser();
			String usercode =user.getUsercode();
			alias = "".equals(alias)?"":alias+".";
			StringBuffer wfidWhere = new StringBuffer(); //返回拼接wfid的条件
			//获取当前节点的所有任务
			String wfidsString = getCurrWorkFlow(usercode, wfKey, activityId);
			switch (Integer.valueOf(status)) {
			case 1: //待处理（未处理、被退回、被撤回）
				if (firstNode) {//首节点要判断未发送工作流的
					wfidWhere.append(" (("+alias+"wfid  is null )");
					if(!"".equals(wfidsString)){
						wfidWhere.append(" or ("+alias+"wfid in('").append(wfidsString).append("'))");
					}
					wfidWhere.append(")");
				}else {
					if(!"".equals(wfidsString)){
						wfidWhere.append("  ("+alias+"wfid in('").append(wfidsString).append("'))");
					}
				}
				break;
			case 2: //已处理
				//获取已处理的数据
				String hisWfid  = workflowManageComponent.getUserHistoryExecidsByWfKey(user.getUsercode(), wfKey, activityId);
				if (StringUtil.isNotBlank(hisWfid)) {
					hisWfid = hisWfid.replaceAll(",", "','");
					wfidWhere.append(" ("+alias+"wfid in ('").append(hisWfid).append("')");
					wfidWhere.append(" )");
					//已处理的中排除未处理的（被退回的）
					if(!"".equals(wfidsString)){
						wfidWhere.append("  and ("+alias+"wfid not in('").append(wfidsString).append("'))");
					}
				}
				break;
			default:
				break;
			}
			if (StringUtil.isBlank(wfidWhere.toString())) {
				wfidWhere.append(" (1<>1) ");
			}
			return wfidWhere.toString();
		}
	 public String  getCurrWorkFlow(String usercode,String  wfKey, String activityId) {
			Set<String> set = new HashSet();
			// 回退标志过滤条件, NORMAL 代表正常流转, RETURN 代表退回 ,WITHDRAW 代表撤回,可多选
			set.add("NORMAL");
			set.add("RETURN");
			set.add("WITHDRAW");
			List<UserTodoListVo> list =  workflowManageComponent.getUserTodoListByWfKey(usercode, wfKey, activityId, set);
			String returnValueString ="";
			for (int i = 0; i < list.size(); i++) {
				UserTodoListVo utlListVo = list.get(i);
				returnValueString += utlListVo.getWfid();
				if(i!=list.size()-1){
					returnValueString +=",";
				}
			}
			returnValueString = returnValueString.replaceAll(",", "','");
			return returnValueString;
		}
	 @Override
	public Set<String> getOutCome(String wfid, String activityId,String outcomeType) throws Exception {
		SysUser user = SecureUtil.getCurrentUser();
		String usercode = user.getUsercode();
		return workflowManageComponent.getOutcomes(wfid, activityId, usercode, outcomeType);
	} 
}
