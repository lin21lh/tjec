package com.wfzcx.fam.workflow.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.resource.dao.SysResourceDao;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.component.SysWorkflowManageComponent;
import com.jbf.workflow.vo.HistoryOpinionVO;
import com.jbf.workflow.vo.UserTodoListVo;
import com.wfzcx.fam.common.MessageComponent;
import com.wfzcx.fam.manage.dao.FaAccountsDao;
import com.wfzcx.fam.manage.dao.FaApplicationsDao;
import com.wfzcx.fam.manage.dao.FavApplAccountDao;
import com.wfzcx.fam.manage.po.FaAccounts;
import com.wfzcx.fam.manage.po.FaApplications;
import com.wfzcx.fam.workflow.BussinessWorkFlowComponent;
@Scope("prototype")
@Component
public class BussinessWorkFlowComponentImpl implements BussinessWorkFlowComponent {

	@Autowired
	SysWorkflowManageComponent workflowManageComponent;
	@Autowired
	FaApplicationsDao faApplicationsDao;
	@Autowired
	FaAccountsDao  faAccountsDao;
	@Autowired
	SysResourceDao sysResourceDao;
	@Autowired
	MessageComponent messageComponent;//短息服务
	@Autowired
	FavApplAccountDao  favApplAccountDao;
	@Autowired
	ParamCfgComponent pcfg;
	
	public String findFirstNodeWfids(String menuid, String activityId, String status, String processedStatus) throws AppException {
		return findCurrentWfids(menuid, activityId, status, processedStatus, true, false);
	}
	
	public String findLastNodeWfids(String menuid, String activityId, String status, String processedStatus) throws AppException {
		return findCurrentWfids(menuid, activityId, status, processedStatus, false, true);
	}
	
	public String findCurrentWfids(String menuid, String activityId, String status, String processedStatus) throws AppException {
		return findCurrentWfids(menuid, activityId, status, processedStatus, false, false);
	}
	
	@Override
	public String findCurrentWfids(String menuid, String activityId, String status, String processedStatus, Boolean firstNode, Boolean LastNode) throws AppException {
		String wfKey = getWfkeyByMenuid(menuid);
		//解决工作统一查询问题
		if(wfKey.equals("ZHBA")){
			return	findCurrentWfidsForBa(menuid, activityId, status, processedStatus, firstNode, LastNode);
		}
		SysUser user = SecureUtil.getCurrentUser();
		String usercode =user.getUsercode();
		StringBuffer wfidWhere = new StringBuffer(); //返回拼接wfid的条件
		//获取当前节点的所有任务
		String wfidsString = getCurrWorkFlow(usercode, wfKey, activityId, processedStatus);
		switch (Integer.valueOf(status)) {
		case 1: //待处理（未处理、被退回、被撤回）
			if (firstNode) {//首节点要判断未发送工作流的
				if ("".equals(StringUtil.stringConvert(processedStatus))||processedStatus.indexOf("11") != -1) {
					wfidWhere.append(" ((wfid  is null )");
					if(!"".equals(wfidsString)){
						wfidWhere.append(" or (wfid in('").append(wfidsString).append("'))");
					}
					wfidWhere.append(")");
				}else{
					if(!"".equals(wfidsString)){
						wfidWhere.append("  (wfid in('").append(wfidsString).append("'))");
					}
				}
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
				//已处理的中排除未处理的（被退回的）
				if(!"".equals(wfidsString)){
					wfidWhere.append("  and (wfid not in('").append(wfidsString).append("'))");
				}
				if(processedStatus.equals("21")) {//审批同意
					wfidWhere.append("  and  ((wfisback is  null or wfisback =0) ");
					if (firstNode) {//如果是首节点
						wfidWhere.append(" and wfstatus ='01'");
					}
					wfidWhere.append(")");
				}else if (processedStatus.equals("22")) {//审批退回
					if (firstNode) {//如果是首节点
						wfidWhere.append(" and wfstatus <>'01'");
					}else {
						wfidWhere.append("  and (wfisback = 1 or wfisback = 2) ");
					}
				}
				wfidWhere.append(" )");
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
	public String findCurrentWfidsForBa(String menuid, String activityId, String status, String processedStatus, Boolean firstNode, Boolean LastNode) throws AppException {
		String wfKey = getWfkeyByMenuid(menuid);
		SysUser user = SecureUtil.getCurrentUser();
		String usercode =user.getUsercode();
		StringBuffer wfidWhere = new StringBuffer(); //返回拼接wfid的条件
		//获取当前节点的所有任务
		String wfidsString = getCurrWorkFlow(usercode, wfKey, activityId, processedStatus);
		switch (Integer.valueOf(status)) {
		case 1: //待处理（未处理、被退回、被撤回）
			if (firstNode) {//首节点要判断未发送工作流的
				if ("".equals(StringUtil.stringConvert(processedStatus))||processedStatus.indexOf("11") != -1) {
					wfidWhere.append(" ((acct_wfid  is null )");
					if(!"".equals(wfidsString)){
						wfidWhere.append(" or (acct_wfid in('").append(wfidsString).append("'))");
					}
					wfidWhere.append(")");
				}else{
					if(!"".equals(wfidsString)){
						wfidWhere.append("  (acct_wfid in('").append(wfidsString).append("'))");
					}
				}
			}else {
				if(!"".equals(wfidsString)){
					wfidWhere.append("  (acct_wfid in('").append(wfidsString).append("'))");
				}
			}
			break;
		case 2: //已处理
			//获取已处理的数据
			String hisWfid  = workflowManageComponent.getUserHistoryExecidsByWfKey(user.getUsercode(), wfKey, activityId);
			if (StringUtil.isNotBlank(hisWfid)) {
				hisWfid = hisWfid.replaceAll(",", "','");
				wfidWhere.append(" (acct_wfid in ('").append(hisWfid).append("')");
				//已处理的中排除未处理的（被退回的）
				if(!"".equals(wfidsString)){
					wfidWhere.append("  and (acct_wfid not in('").append(wfidsString).append("'))");
				}
				if(!"".equals(wfidsString)){
					wfidWhere.append("  and (wfid not in('").append(wfidsString).append("'))");
				}
				if (processedStatus.equals("21")) {//审批同意
					wfidWhere.append("  and  ((acct_wfisback is  null or acct_wfisback =0) ");
					if (firstNode) {//如果是首节点
						wfidWhere.append(" and acct_wfstatus ='01'");
					}
					wfidWhere.append(")");
				}else if (processedStatus.equals("22")) {//审批退回
					if (firstNode) {//如果是首节点
						wfidWhere.append(" and acct_wfstatus <>'01'");
					}else {
						wfidWhere.append("  and (acct_wfisback = 1 or acct_wfisback = 2) ");
					}
				}
				wfidWhere.append(" )");
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
	
//	public sendWrokFlow() {
//		
//	}
	
	String append(String wfid) {
		if (StringUtil.isBlank(wfid))
			return wfid;
		if (StringUtil.isNotBlank(wfid))
			wfid += ",";
		return wfid;
	}
	/**
	 * 工作流启用
	 */
	public String sendWorkFlow(String menuid, String applicationIds,Map variables,String isba) throws AppException {
		StringBuffer returnString = new StringBuffer();
			try {
				//启用工作流
				String wfKey =getWfkeyByMenuid(menuid);
				String[] applicationIdArray =applicationIds.split(",");
				for (int i = 0; i < applicationIdArray.length; i++) {
					if ("1".equals(isba)) {//备案//备案申请
						String hql1 = " from FaAccounts where applicationId in("+applicationIdArray[i]+")";
						List<FaAccounts> list1 = (List<FaAccounts>) faAccountsDao.find(hql1);
						if (list1.size()==1) {
							FaAccounts accounts = list1.get(0);
							String oldwfid = StringUtil.stringConvert(accounts.getWfid());
							if ("".equals(oldwfid)) {
								ResultMsg rsMsg = workflowManageComponent.startProcessByKeyAndPush(wfKey, variables);
								if (!rsMsg.isSuccess()) {
									returnString.append(rsMsg.getTitle());
									continue;
								}
								//工作流启动后返回的id
								String wfid = StringUtil.stringConvert(rsMsg.getBody().get("EXECID"));
								if("".equals(wfid)){
									returnString.append("启用工作流是返回的wfid是空");
									continue;
								}
								accounts.setWfid(wfid);
								accounts.setWfstatus("01");
								faAccountsDao.update(accounts);
							}else{
								returnString.append("该数据已经被送审，不能再次送审！");
							}
						}else {
							returnString.append("在Fa_Accounts表中applicationId为"+applicationIdArray[i]+"未找到相应的数据或有多条数据！");
						}
					}else {
						FaApplications application = faApplicationsDao.get(Integer.parseInt(applicationIdArray[i]));
						String oldwfid = StringUtil.stringConvert(application.getWfid());
						if ("".equals(oldwfid)) {
							ResultMsg rsMsg = workflowManageComponent.startProcessByKeyAndPush(wfKey, variables);
							if (!rsMsg.isSuccess()) {
								returnString.append(rsMsg.getTitle());
								continue;
							}
							//工作流启动后返回的id
							String wfid = StringUtil.stringConvert(rsMsg.getBody().get("EXECID"));
							if("".equals(wfid)){
								returnString.append("启用工作流是返回的wfid是空");
								continue;
							}
							application.setWfid(wfid);//返回的wfid
							application.setWfstatus("01");//送审时将更改工作流状态
							faApplicationsDao.update(application);
						}else {
							returnString.append("该数据已经被送审，不能再次送审！");
						}
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new AppException(e.getMessage());
			}
	   return returnString.toString();
	}
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
			List list = faApplicationsDao.findMapBySql("select wfkey from sys_resource where resourceid="+menuid);
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
	  * 完成待办
	  */
	@Override
	public String completeTask(String applicationIds, Map variables,String outcome,String opinion,String isback,String activityId,String isba)throws AppException {
		StringBuffer returnString = new StringBuffer();
		Map map = new HashMap();
		try {
			//启用工作流
			String[] applicationIdArray =applicationIds.split(",");
			SysUser user = SecureUtil.getCurrentUser();
			String userCode = user.getUsercode();
			ResultMsg rsgMsg = null;
			for (int i = 0; i < applicationIdArray.length; i++) {
				String wfid  ="";
				FaApplications application = faApplicationsDao.get(Integer.parseInt(applicationIdArray[i]));
				if ("1".equals(isba)) {//备案申请
					String hql1 = " from FaAccounts where applicationId in("+applicationIdArray[i]+")";
					List<FaAccounts> list1 = (List<FaAccounts>) faAccountsDao.find(hql1);
					if (list1.size()==1) {
						FaAccounts accounts = list1.get(0);
						wfid = accounts.getWfid();
					}else {
						returnString.append("在Fa_Accounts表中applicationId为"+applicationIdArray[i]+"未找到相应的数据或有多条数据！");
					}
				}else {
					//账户开立 、变更、注销
					wfid =application.getWfid();
				}
				if ("".equals(wfid)||wfid==null) {
					returnString.append("applicationId为"+applicationIdArray[i]+"流程主键wfid为空");
					continue;
				}
				
				String phonenumbers = StringUtil.stringConvert(variables.get("singlePhonenumbers"));
				String message = StringUtil.stringConvert(variables.get("singleMessage"));
				phonenumbers=phonenumbers.replaceAll("#,", "").replaceAll(",#","").replaceAll("#", "");
				if(!"".equals(phonenumbers)&&!"".equals(message)){
					//启用消息服务
					messageComponent.saveMessage(Integer.parseInt(applicationIdArray[i]), phonenumbers, message, activityId);
				}
				if("".equals(isback)){//审批
					Set outcomeSet =this.getOutCome(wfid, activityId, null);
					if(outcomeSet.size()==1){
						outcome= (String) outcomeSet.iterator().next();
						rsgMsg = workflowManageComponent.completeTask(wfid, activityId,outcome, map, userCode, opinion);
					}else {
						returnString.append("该数据已经处理或没有操作权限！");
						returnString.append("\n");
					}
				}else if("1".equals(isback)){//退回上岗
					rsgMsg = workflowManageComponent.sendBackWorkflow(wfid, activityId, map, userCode, opinion);
				}else if("2".equals(isback)){//退回首节点
					rsgMsg = workflowManageComponent.sendBackWorkflowToFirstNode(wfid, activityId, map, userCode, opinion);
				}else {
					rsgMsg = null;
				}
				if(rsgMsg!=null&&!rsgMsg.isSuccess()){//处理失败
					returnString.append(rsgMsg.getTitle());
					returnString.append("\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
		return returnString.toString();
	}
	 /**
	  * 退回
	  */
	@Override
	public String sendBackTask(String applicationIds,String activityId, Map variables,String opinion,String isba)throws AppException {
		StringBuffer returnString = new StringBuffer();
		try {
			//启用工作流
			String[] applicationIdArray =applicationIds.split(",");
			SysUser user = SecureUtil.getCurrentUser();
			String userCode = user.getUsercode();
			ResultMsg rsgMsg;
			for (int i = 0; i < applicationIdArray.length; i++) {
				String wfid  ="";
				if ("1".equals(isba)) {//备案申请
					String hql1 = " from FaAccounts where applicationId in("+applicationIdArray[i]+")";
					List<FaAccounts> list1 = (List<FaAccounts>) faAccountsDao.find(hql1);
					if (list1.size()==1) {
						FaAccounts accounts = list1.get(0);
						wfid = accounts.getWfid();
					}else {
						returnString.append("在Fa_Accounts表中applicationId为"+applicationIdArray[i]+"未找到相应的数据或有多条数据！");
					}
				}else {
					FaApplications application = faApplicationsDao.get(Integer.parseInt(applicationIdArray[i]));
					wfid =application.getWfid();
				}
				if ("".equals(wfid)||wfid==null) {
					returnString.append("applicationId为"+applicationIdArray[i]+"流程主键wfid为空");
					continue;
				}
				rsgMsg = workflowManageComponent.sendBackWorkflow(wfid, activityId, variables, userCode, opinion);
				if(!rsgMsg.isSuccess()){//处理失败
					returnString.append(rsgMsg.getTitle());
					returnString.append("\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
		return returnString.toString();
	}
	
	/**
	 * 获取工作流当前节点的未处理的流程
	 * @Title: getCurrWorkFlow 
	 * @Description:获取工作流当前节点的未处理的流程
	 * @param usercode  用户code
	 * @param wfKey 工作流标志
	 * @param activityId  节点
	 * @param backFlags  处理类型 11未处理、12退回、13 退回多个用,隔开
	 * @return String 工作的id用,隔开
	 */
	public String  getCurrWorkFlow(String usercode,String  wfKey, String activityId, String processedStatus) {
		Set<String> set = new HashSet();
		// 回退标志过滤条件, NORMAL 代表正常流转, RETURN 代表退回 ,WITHDRAW 代表撤回,可多选
		if(processedStatus.indexOf("11") != -1){
			set.add("NORMAL");
		}
		if(processedStatus.indexOf("12") != -1){
			set.add("RETURN");
		}
		if(processedStatus.indexOf("13") != -1){
			set.add("WITHDRAW");
		}
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
	public String revokeWorkFlow(String applicationIds, Map variables,String opinion, String activityId,String isba) throws AppException {
		StringBuffer returnString = new StringBuffer();
		try {
			//撤销
			String[] applicationIdArray =applicationIds.split(",");
			SysUser user = SecureUtil.getCurrentUser();
			String usercode = user.getUsercode();
			ResultMsg msg=null;
			for (int i = 0; i < applicationIdArray.length; i++) {
				String wfid  ="";
				if ("1".equals(isba)) {//备案申请
					String hql1 = " from FaAccounts where applicationId in("+applicationIdArray[i]+")";
					List<FaAccounts> list1 = (List<FaAccounts>) faAccountsDao.find(hql1);
					if (list1.size()==1) {
						FaAccounts accounts = list1.get(0);
						wfid = accounts.getWfid();
					}else {
						returnString.append("在Fa_Accounts表中applicationId为"+applicationIdArray[i]+"未找到相应的数据或有多条数据！");
					}
				}else {
					FaApplications application = faApplicationsDao.get(Integer.parseInt(applicationIdArray[i]));
					wfid =application.getWfid();
				}
				if ("".equals(wfid)||wfid==null) {
					returnString.append("applicationId为"+applicationIdArray[i]+"流程主键wfid为空");
					continue;
				}
				 msg = workflowManageComponent.getBackWorkflow(wfid, activityId, usercode, null);
				 if(!msg.isSuccess()){//处理失败
						returnString.append(msg.getTitle());
						returnString.append("\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
		return returnString.toString();
	}

	@Override
	public Set<String> getOutCome(String wfid, String activityId,String outcomeType) throws Exception {
		SysUser user = SecureUtil.getCurrentUser();
		String usercode = user.getUsercode();
		return workflowManageComponent.getOutcomes(wfid, activityId, usercode, outcomeType);
	}

	@Override
	public List<HistoryOpinionVO> getworkFlowList(String wfid) {
		// TODO Auto-generated method stub
		List<HistoryOpinionVO> list = new ArrayList();
		try {
			list = workflowManageComponent.getWorkflowHistoryOpinions(wfid, "asc");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean getTaskFormEditable(String wfid, String activityId) throws Exception {
		// TODO Auto-generated method stub
		return workflowManageComponent.isWorkflowTaskFormEditable(wfid, activityId);
	}

	@Override
	public String sendBackWorkflowToFirstNode(String applicationIds, String activityId,
			Map variables, String assignee, String opinion,String isba) throws AppException {
		StringBuffer returnString = new StringBuffer();
		try {
			//启用工作流
			String[] applicationIdArray =applicationIds.split(",");
			SysUser user = SecureUtil.getCurrentUser();
			String userCode = user.getUsercode();
			ResultMsg rsgMsg;
			for (int i = 0; i < applicationIdArray.length; i++) {
				String wfid  ="";
				if ("1".equals(isba)) {//备案申请
					String hql1 = " from FaAccounts where applicationId in("+applicationIdArray[i]+")";
					List<FaAccounts> list1 = (List<FaAccounts>) faAccountsDao.find(hql1);
					if (list1.size()==1) {
						FaAccounts accounts = list1.get(0);
						wfid = accounts.getWfid();
					}else {
						returnString.append("在Fa_Accounts表中applicationId为"+applicationIdArray[i]+"未找到相应的数据或有多条数据！");
					}
				}else {
					FaApplications application = faApplicationsDao.get(Integer.parseInt(applicationIdArray[i]));
					wfid =application.getWfid();
				}
				if ("".equals(wfid)||wfid==null) {
					returnString.append("applicationId为"+applicationIdArray[i]+"流程主键wfid为空");
					continue;
				}
				rsgMsg = workflowManageComponent.sendBackWorkflowToFirstNode(wfid, activityId, variables, assignee, opinion);
				if(!rsgMsg.isSuccess()){//处理失败
					returnString.append("流程主键为:"+wfid).append("的流程处理失败，").append(rsgMsg.getTitle());
					returnString.append("\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
		return returnString.toString();
	}
	public boolean getCanBackToFirstNode(String wfid,String activityId)  throws Exception{
		SysUser user = SecureUtil.getCurrentUser();
		String userCode = user.getUsercode();
		ResultMsg  rMsg = workflowManageComponent.isFirstNodeBackable(wfid, activityId, userCode);
		return rMsg.isSuccess();
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
	public  List<Map>  getSendUser(String wfid,String activityId){
		List<HistoryOpinionVO> histList = workflowManageComponent.getWorkflowHistoryOpinions(wfid, "asc");
		List<Map> returnList = new ArrayList<Map>();
		List<String> isHasList = new ArrayList<String>();
		if(histList.size()>=1){
			for (int i = histList.size()-1; i >=0; i--) {
				HistoryOpinionVO hoo = histList.get(i);
				Map map =new HashMap();
				String userCode = hoo.getAuthor();
				String actiid = hoo.getActiId();
				String userName = hoo.getAuthorName();
				String actiName = hoo.getActiName();
				String transition =hoo.getTransition();
				if (!actiid.equals(activityId)&&!transition.startsWith("退回至")){
					
					String phone = getUserPhone(userCode);
					map.put("name", actiName+"-"+userName);
					map.put("enddate", hoo.getEnddate());
					map.put("phone", "".equals(phone)?"#":phone);
					
					if(isHasList.isEmpty()){
						isHasList.add(actiid);
						returnList.add(map);
					}else{
						if(!isHasList.contains(actiid)){
							isHasList.add(actiid);
							returnList.add(map);
						}
					}

				}
				/*if (!actiid.equals(activityId)&&!transition.startsWith("退回至")) {
				String phone = getUserPhone(userCode);
				map.put("name", actiName+"-"+userName);
				map.put("enddate", hoo.getEnddate());
				map.put("phone", "".equals(phone)?"#":phone);
				if (returnList.isEmpty()) {
					returnList.add(map);
				}else {
					for (int j = 0; j < returnList.size(); j++) {
						Map returnMap =returnList.get(j);
						String enddate =StringUtil.stringConvert(returnMap.get("enddate"));
						String name =StringUtil.stringConvert(returnMap.get("name"));
						if (name.equals(actiName+"-"+userName)){
							if(hoo.getEnddate().compareTo(enddate)>0){
								returnList.remove(j);
								returnList.add(map);
							}
						}else {
							returnList.add(map);
						}
					}
				}
			}*/
			}
		}
		return returnList;
	}
	/**
	 * 根据usercode获取其手机号码
	 * @Title: getUserPhone 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param userCode
	 * @return 设定文件
	 */
	public String getUserPhone(String userCode){
		String sql ="select t.userid,t1.usercode,t.phone from sys_userexp t, sys_user t1 where t.userid = t1.userid and t1.usercode='"+userCode+"'";
		List list  =faApplicationsDao.findMapBySql(sql);
		if (list.isEmpty()) {
			return "";
		}else {
			Map map = (Map) list.get(0);
			return StringUtil.stringConvert(map.get("phone"));
		}
	}

	@Override
	public String getMessageContent(String applicationId,String wfid, String isBa, String cllx,String activityId) {
		String messageContent ="";
		try {
			if ("1".equals(isBa)) {//备案
				messageContent = pcfg.findGeneralParamValue("SYSTEM", "ZHBACONTENT");
				if ("".equals(messageContent) || messageContent==null) {
					messageContent = "'您在账户管理系统发起的账户名称为：' || t.account_name || '的备案申请，在' || to_char(sysdate, 'yyyy-mm-dd') || '已经被#USERNAME##ACTIVITY#。'";
				}
			}else {//开立、变更、撤销
				messageContent = pcfg.findGeneralParamValue("SYSTEM", "ZHGLCONTENT");
				if ("".equals(messageContent) || messageContent==null) {
					messageContent = "'您在账户管理系统发起的账户名称为：' || t.account_name || '的' || (select t.name from SYS_FA_DICENUMITEM t   where upper(t.elementcode) = 'REGISTERTYPE'";
					messageContent += "and t.status = 0 and t.code = TYPE) || '申请，在' || to_char(sysdate, 'yyyy-mm-dd') || '已经被#USERNAME##ACTIVITY#。'";
				}
			}
			SysUser user = SecureUtil.getCurrentUser();
			messageContent = messageContent.replaceAll("#USERNAME#", user.getUsername());
			String activeName ="";
			if("1".equals(cllx)){//同意
				activeName ="审批同意";
			}else if("2".equals(cllx)){//退回
				Set outcomeSet =null;
				try {
					outcomeSet = this.getOutCome(wfid, activityId, "RETURN");
				} catch (Exception e) {
				}
				if(outcomeSet.size()>0){
					activeName= (String) outcomeSet.iterator().next();
				}
				activeName = activeName;
				if(StringUtil.isBlank(activeName)){//如果获取退回路径发生错误
					activeName = "退回";
				}
			}else if("3".equals(cllx)){//退回首节点
				Set outcomeSet =null;
				try {
					outcomeSet = this.getOutCome(wfid, activityId, "RETURN_FIRST");
				} catch (Exception e) {
				}
				if(outcomeSet.size()>0){
					activeName= (String) outcomeSet.iterator().next();
				}
				activeName = activeName;
				if(StringUtil.isBlank(activeName)){//如果获取退回路径发生错误
					activeName = "退回";
				}
			}
			messageContent = messageContent.replaceAll("#ACTIVITY#", activeName);
			String sqlString = "select "+messageContent+" message from fav_appl_account t where application_id ="+applicationId;
			List list  =faApplicationsDao.findMapBySql(sqlString);
			if (list.isEmpty()) {
				return "";
			}else {
				Map map = (Map) list.get(0);
				return StringUtil.stringConvert(map.get("message"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			messageContent="";
		}
		return messageContent;
	}
	
}
