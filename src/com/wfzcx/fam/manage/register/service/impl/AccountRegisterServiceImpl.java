package com.wfzcx.fam.manage.register.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.base.filemanage.component.FileManageComponent;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.log.service.impl.SysLogApp;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.vo.HistoryOpinionVO;
import com.wfzcx.fam.common.MessageComponent;
import com.wfzcx.fam.manage.RecordType;
import com.wfzcx.fam.manage.dao.FaAccountArchiveDao;
import com.wfzcx.fam.manage.dao.FaAccountsDao;
import com.wfzcx.fam.manage.dao.FaApplicationsDao;
import com.wfzcx.fam.manage.dao.FavApplAccountDao;
import com.wfzcx.fam.manage.po.FaAccounts;
import com.wfzcx.fam.manage.po.FaApplications;
import com.wfzcx.fam.manage.register.service.AccountRegisterService;
import com.wfzcx.fam.workflow.BussinessWorkFlowComponent;
/**
 * 账户开立Service实现类
 * @ClassName: AccountRegisterServiceImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author MaQingShuang
 * @date 2015年4月14日 上午11:40:34
 */
@Scope("prototype")
@Service
public class AccountRegisterServiceImpl implements AccountRegisterService {
	
	@Autowired
	FaAccountsDao faAccountsDao;
	
	@Autowired
	FaApplicationsDao faApplicationsDao;
	
	@Autowired
	FavApplAccountDao favApplAccountDao;
	
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	FaAccountArchiveDao faAccountArchiveDao;
	@Autowired
	BussinessWorkFlowComponent bwfc;
	@Autowired
	FileManageComponent fileManageComponent;
	@Autowired
	MessageComponent messageComponent;
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer add(Map<String, Object> param) throws Exception {
		FaApplications application = new FaApplications();
		FaAccounts account = new FaAccounts();
		try {
			BeanUtils.populate(application, param);
			BeanUtils.populate(account, param);
			application.setPhonenumber(param.get("applPhonenumber").toString());
			application.setCreateTime(DateUtil.getCurrentDateTime());
			SysUser user = SecureUtil.getCurrentUser();
			application.setCreateUser(user.getUserid().toString());
			application.setType(RecordType.REGISTER.getIndex());
			application.setWfstatus("00"); //
			Integer applicationId = (Integer) faApplicationsDao.save(application);
			account.setApplicationId(applicationId);
			//账户类型
			String accountType = StringUtil.stringConvert(param.get("accountType"));
			if ("2".equals(accountType) ||"3".equals(accountType)) {//3单位零余额2财政零余额
				account.setIszero(1);//是零余额账户
			}else {
				account.setIszero(0);//不是零余额账户
			}
			String workflowflag =StringUtil.stringConvert(param.get("workflowflag"));
			faAccountsDao.save(account);
			if("1".equals(workflowflag)){//如果保存并发送，则启动工作流
				HashMap map = new HashMap();
				map.put("applicationId", applicationId);
				String menuid = StringUtil.stringConvert(param.get("menuid"));
				String msg = bwfc.sendWorkFlow(menuid, String.valueOf(applicationId), map,"");
				if(!"".equals(msg)){
					throw new AppException(msg);
				}
			}
			//附件上传
			boolean fileIsUpload =  fileUpload(applicationId, StringUtil.stringConvert(param.get("itemids")));
			return applicationId;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String edit(Map<String, Object> param) throws AppException {
		
		FaApplications application = new FaApplications();
		String wfReturnValue = "";
		try {
			updateSave(param);
			Integer applicationId = Integer.parseInt(StringUtil.stringConvert(param.get("applicationId")));
			FaApplications dbApplication = faApplicationsDao.get(applicationId);
			//送审标志
			String workflowflag =StringUtil.stringConvert(param.get("workflowflag"));
			//工作流节点
			String activityId =StringUtil.stringConvert(param.get("activityId"));
			if("1".equals(workflowflag)){//如果保存并发送，则启动工作流
				HashMap map = new HashMap();
				String menuid = StringUtil.stringConvert(param.get("menuid"));
				String wfid = StringUtil.stringConvert(dbApplication.getWfid());
				if ("".equals(wfid)) {
					wfReturnValue = bwfc.sendWorkFlow(menuid, String.valueOf(dbApplication.getApplicationId()), map,"");
				}else{
					wfReturnValue = bwfc.completeTask(String.valueOf(dbApplication.getApplicationId()), map, "", "", "", activityId,"");
				}
				if (!"".equals(wfReturnValue)) {
					throw new AppException(wfReturnValue);
				}
			}
			//保存附件
			boolean fileIsUpload =  fileUpload(Integer.parseInt(StringUtil.stringConvert(param.get("applicationId"))), StringUtil.stringConvert(param.get("itemids")));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
		return wfReturnValue;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(String applicationId) {
		//删除fa_applications
		String hql = " from FaApplications where applicationId in("+applicationId+")";
		List<FaApplications> list = (List<FaApplications>) faApplicationsDao.find(hql);
		faApplicationsDao.deleteAll(list);
		//删除fa_accounts
		String hql1 = " from FaAccounts where applicationId in("+applicationId+")";
		List<FaAccounts> list1 = (List<FaAccounts>) faAccountsDao.find(hql1);
		faAccountsDao.deleteAll(list1);
		try {
			//删除附件,附件删除不要影响业务
			fileManageComponent.deleteFiles(applicationId, "FAAPPLICATION");
		} catch (Exception e) {
			SysLogApp.writeLog("删除附件时发生异常，applicationId为"+applicationId,5);
		}
	}
	

	public void sendWorkFlow(String menuid, String applicationId,String activityId) throws Exception {
		StringBuffer returnSql = new StringBuffer();
		String[] applicationIdArray =applicationId.split(",");
		for (int i = 0; i < applicationIdArray.length; i++) {
			FaApplications dbApplication = faApplicationsDao.get(Integer.parseInt(applicationIdArray[i]));
			String wfid = StringUtil.stringConvert(dbApplication.getWfid());
			String msg ="";
			if("".equals(wfid)){
				msg = bwfc.sendWorkFlow(menuid,applicationIdArray[i],new HashMap(),"");
			}else {
				msg = bwfc.completeTask(applicationIdArray[i], new HashMap(), "", "", "", activityId, "");
			}
			if (!"".equals(msg)) {
				returnSql.append(msg);
			}
		}
		if (!"".equals(returnSql.toString())) {
			throw new Exception(returnSql.toString());
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String auditWorkFlow(Map<String, Object> param) throws AppException {
		String returnString = "";
		try {
			String formIsEdit = StringUtil.stringConvert(param.get("formIsEdit"));
			if ("true".equals(formIsEdit)) {
				updateSave(param);
				fileUpload(Integer.parseInt(StringUtil.stringConvert(param.get("applicationId"))), StringUtil.stringConvert(param.get("itemids")));
			}
			//工作流节点
			String activityId =StringUtil.stringConvert(param.get("activityId"));
			//是否退回标志
			String isback = StringUtil.stringConvert(param.get("isback"));
			//审批意见
			String opinion = StringUtil.stringConvert(param.get("opinion"));
			String applicationId = StringUtil.stringConvert(param.get("applicationId"));
			returnString = bwfc.completeTask(applicationId, param, "", opinion,isback,activityId,"");
			String menuid =  StringUtil.stringConvert(param.get("menuid"));
			SysResource sr	=this.getResourceById(menuid);
			Integer wftasknode = sr.getWftasknode();//（0=首任务节点；1=中间任务节点；2=末任务节点）
			if (wftasknode == 2) {
				String sql = "update fa_applications t set t.operatenum=nvl(t.operatenum,0)+1 where t.application_id='"+param.get("applicationId")+"'";
				faApplicationsDao.updateBySql(sql);
			}
			if (!"".equals(returnString)) {
				throw new AppException(returnString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return returnString;
 }
	/**
	 * 开立、审核修改保存
	 * @Title: updateSave 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 	
	 * @param param
	 * @throws Exception 设定文件
	 */
	public void updateSave(Map<String, Object> param) throws Exception{
		FaApplications application = new FaApplications();
		FaAccounts account = new FaAccounts();
		BeanUtils.populate(application, param);
		application.setPhonenumber(param.get("applPhonenumber").toString());
		FaApplications dbApplication = faApplicationsDao.get(application.getApplicationId());
		dbApplication.setBdgagencycode(application.getBdgagencycode());
		dbApplication.setBdgagencyname(application.getBdgagencyname());
		dbApplication.setDeptNature(application.getDeptNature());
		dbApplication.setSupervisorDept(application.getSupervisorDept());
		dbApplication.setPhonenumber(application.getPhonenumber());
		dbApplication.setDeptAddress(application.getDeptAddress());
		dbApplication.setIsregister(application.getIsregister());
		dbApplication.setApplyReason(application.getApplyReason());
		dbApplication.setUpdateTime(DateUtil.getCurrentDateTime());
		SysUser user = SecureUtil.getCurrentUser();
		dbApplication.setUpdateUser(user.getUserid().toString());
		faApplicationsDao.update(dbApplication);
		BeanUtils.populate(account, param);
		//账户类型
		String accountType = StringUtil.stringConvert(param.get("accountType"));
		if ("2".equals(accountType) ||"3".equals(accountType)) {//3单位零余额2财政零余额
			account.setIszero(1);//是零余额账户
		}else {
			account.setIszero(0);//不是零余额账户
		}
		FaAccounts dbAccount = faAccountsDao.get(account.getAccountId());
		dbAccount.setType02(account.getType02());
		dbAccount.setAccountType(account.getAccountType());
		dbAccount.setIszero(account.getIszero());
		dbAccount.setAccountName(account.getAccountName());
		dbAccount.setBankCode(account.getBankCode());
		dbAccount.setAccountNum(account.getAccountNum());
		dbAccount.setAccountNumber(account.getAccountNumber());
		dbAccount.setOpenTime(account.getOpenTime());
		dbAccount.setResponsiblePerson(account.getResponsiblePerson());
		dbAccount.setAccountContent(account.getAccountContent());
		dbAccount.setRemark(account.getRemark());
		dbAccount.setPhonenumber(account.getPhonenumber());
		dbAccount.setLegalPerson(account.getLegalPerson());
		dbAccount.setIdcardno(account.getIdcardno());
		dbAccount.setFinancialOfficer(account.getFinancialOfficer());
		faAccountsDao.save(dbAccount);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String revokeWorkFlow(String menuid, String applicationId,String activityId) throws AppException {
		return bwfc.revokeWorkFlow(applicationId, new HashMap(), "", activityId,"");
	}

	@Override
	public List<Map> getbdgagencyInformation(String bdgagencycode) {
		String where =" bdgagencycode='"+bdgagencycode+"' and status=1 and rownum=1";
		List<Map>  list = faAccountArchiveDao.queryBySQL("FA_ACCOUNT_ARCHIVE",where);
		return list;
	}
	@Override
	public boolean getTaskFormEditable(String wfid, String activityId)
			throws Exception {
		return bwfc.getTaskFormEditable(wfid, activityId);
	}

	@Override
	public boolean fileUpload(Integer applicationId, String itemids)
			throws Exception {
		// TODO Auto-generated method stub
		boolean isSucuess = false;
		if (itemids != null && !"".equals(itemids)) {
			List<Long> l = new ArrayList<Long>();
			String[] it = itemids.split(",");
			for (int i = 0; i < it.length; i++) {
				l.add(Long.valueOf(it[i]));
			}
			isSucuess = fileManageComponent.updateKeyid(String.valueOf(applicationId), l);
		}
		return isSucuess;
	}

	@Override
	public int saveMessage(Integer applicationId, String phone, String message,String activityId) {
		return  messageComponent.saveMessage(applicationId, phone, message, activityId);
	}
	@Override
	public List<HistoryOpinionVO> getworkFlowList(String wfid) {
		// TODO Auto-generated method stub
		return bwfc.getworkFlowList(wfid);
	}

	@Override
	public SysResource getResourceById(String menuid) throws Exception {
		// TODO Auto-generated method stub
		return bwfc.getResourceById(menuid);
	}

	@Override
	public boolean getOperatenum(String wfid) {
		if("".equals(wfid)){
			return false;
		}
		String where =" wfid='"+wfid+"'";
		List<Map>  list = faAccountArchiveDao.queryBySQL("FA_APPLICATIONS",where);
		if(list.isEmpty()){
			return false;
		}else {
			Map map = list.get(0);
			BigDecimal operatenum = map.get("operatenum")==null? new BigDecimal(0): (BigDecimal) map.get("operatenum");
			if(operatenum.compareTo(new BigDecimal(0))>0){
				return true;
			}
		}
		return false;
	}

}
