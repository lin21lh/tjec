package com.wfzcx.fam.manage.revoke.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.base.filemanage.component.FileManageComponent;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.component.SysWorkflowManageComponent;
import com.jbf.workflow.vo.HistoryOpinionVO;
import com.wfzcx.fam.common.MessageComponent;
import com.wfzcx.fam.dataPermission.FamDataPermissionFilter;
import com.wfzcx.fam.manage.dao.FaAccountArchiveDao;
import com.wfzcx.fam.manage.dao.FaAccountsDao;
import com.wfzcx.fam.manage.dao.FaApplicationsDao;
import com.wfzcx.fam.manage.dao.FavApplAccountDao;
import com.wfzcx.fam.manage.po.FaAccountArchive;
import com.wfzcx.fam.manage.po.FaAccounts;
import com.wfzcx.fam.manage.po.FaApplications;
import com.wfzcx.fam.manage.po.FavApplAccount;
import com.wfzcx.fam.manage.revoke.service.AccountRevokeService;
import com.wfzcx.fam.workflow.BussinessWorkFlowComponent;

/**
 * 
 * @ClassName: AccountRevokeServiceImpl
 * @Description: TODO(账户注销业务类)
 * @author LiuJunBo
 * @date 2015-4-14 上午11:01:03
 */
@Scope("prototype")
@Service("com.wfzcx.fam.manage.revoke.service.impl.AccountRevokeServiceImpl")
public class AccountRevokeServiceImpl implements AccountRevokeService {

	@Autowired
	FaAccountArchiveDao accountArchiveDao;

	@Autowired
	FaApplicationsDao applicationsDao;

	@Autowired
	FavApplAccountDao favApplAccountDao;

	@Autowired
	FaAccountsDao accountsDao;

	@Autowired
	SysWorkflowManageComponent workflowManageComponent;

	@Autowired
	FileManageComponent fileManageComponent;

	@Autowired
	BussinessWorkFlowComponent bwfc;
	@Autowired
	MessageComponent messageComponent;
	
	@Autowired
	FamDataPermissionFilter fdpf;
	@Autowired
	MapDataDaoI mapDataDao;
	
	@Override
	public PaginationSupport queryNoRevoke(Map<String, Object> param) {

		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer
				.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer
				.valueOf(param.get("page").toString()) : 1;

		DetachedCriteria dc = DetachedCriteria.forClass(FaAccountArchive.class);

		// 增加权限本级及以下
		// List list = DeptComponent.getCurAndLowerCode();
		// dc.add(Property.forName("bdgagencycode").in(list));

		// 添加预算单位code
		if (param.get("bdgagencycode") != null
				&& param.get("bdgagencycode").toString().trim().length() > 0) {
			dc.add(Property.forName("bdgagencycode").like(
					param.get("bdgagencycode").toString(), MatchMode.START));
		}
		// 添加账户类型
		if (param.get("accountType") != null
				&& param.get("accountType").toString().trim().length() > 0) {
			dc.add(Property.forName("accountType").like(
					param.get("accountType").toString(), MatchMode.ANYWHERE));
		}
		// 添加账户性质
		if (param.get("type02") != null
				&& param.get("type02").toString().trim().length() > 0) {
			dc.add(Property.forName("type02").like(
					param.get("type02").toString(), MatchMode.ANYWHERE));
		}
		// 添加账户名称
		if (param.get("accountName") != null
				&& param.get("accountName").toString().trim().length() > 0) {
			dc.add(Property.forName("accountName").like(
					param.get("accountName").toString(), MatchMode.ANYWHERE));
		}
		// 添加银行账号
		if (param.get("accountNumber") != null
				&& param.get("accountNumber").toString().trim().length() > 0) {
			dc.add(Property.forName("accountNumber").like(
					param.get("accountNumber").toString(), MatchMode.ANYWHERE));
		}

		// 添加status=1条件
		dc.add(Property.forName("status").eq(1));
		dc.add(Property.forName("ischange").eq(0));

		return accountArchiveDao.findByCriteria(dc, pageSize, pageIndex);
	}

	@Override
	public PaginationSupport queryHasRevokeByView(Map<String, Object> param) {
		// TODO Auto-generated method stub

		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer
				.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer
				.valueOf(param.get("page").toString()) : 1;

		DetachedCriteria dc = DetachedCriteria.forClass(FavApplAccount.class);
		// 增加权限本级及以下
		// List list = DeptComponent.getCurAndLowerCode();
		// dc.add(Property.forName("bdgagencycode").in(list));

		String key = (String) param.get("key");
		String activityId = (String) param.get("activityId");
		String wfStatus = (String) param.get("wfstatus");
		wfStatus = StringUtil.isNotBlank(wfStatus) ? wfStatus : "0";
		SysUser user = SecureUtil.getCurrentUser();

		switch (Integer.parseInt(wfStatus)) {
		case 0: // 待处理（未处理、被撤回、被退回）
			String wfids = "";// workflowManageComponent.getUserTodoExecidsByWfKey(user.getUsercode(),
								// key, activityId);
			if (StringUtil.isNotBlank(wfids))
				dc = dc.add(Property.forName("wfid").in(wfids.split(",")));
			else
				dc = dc.add(Property.forName("wfid").eq("0"));
			break;
		case 1: // 已处理
			wfids = workflowManageComponent.getUserHistoryExecidsByWfKey(
					user.getUsercode(), key, activityId);
			if (StringUtil.isNotBlank(wfids))
				dc = dc.add(Property.forName("wfid").in(wfids.split(",")));
			else
				dc = dc.add(Property.forName("wfid").eq("0"));
			break;

		default:
			break;
		}

		// 添加预算单位code
		if (param.get("bdgagencycode") != null
				&& param.get("bdgagencycode").toString().trim().length() > 0) {
			dc.add(Property.forName("bdgagencycode").like(
					param.get("bdgagencycode").toString(), MatchMode.START));
		}
		// 添加账户类型
		if (param.get("oldAccountType") != null
				&& param.get("oldAccountType").toString().trim().length() > 0) {
			dc.add(Property.forName("oldAccountType").like(
					param.get("oldAccountType").toString(), MatchMode.ANYWHERE));
		}
		/*
		 * //添加节点code if (param.get("wfstatus") != null &&
		 * param.get("wfstatus").toString().trim().length() > 0) {
		 * dc.add(Property.forName("wfstatus").like(
		 * param.get("wfstatus").toString(), MatchMode.ANYWHERE)); }
		 */
		// 添加备案类型
		dc.add(Property.forName("type").eq(3));
		return favApplAccountDao.findByCriteria(dc, pageSize, pageIndex);
	}

	@Override
	public void editRevokeInfo(FaApplications faApplications,
			Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		// 获取参数
		String isSub = param.get("isSub") == null ? "" : param.get("isSub")
				.toString();
		String menuid = param.get("menuid") == null ? "" : param.get("menuid")
				.toString();
		String itemids = param.get("itemids") == null ? "" : param.get(
				"itemids").toString();
		String activityId = param.get("activityId") == null ? "" : param.get(
		"activityId").toString();
		// 获取当前用户
		SysUser user = SecureUtil.getCurrentUser();

		faApplications.setUpdateUser(user.getUserid().toString());
		faApplications.setUpdateTime(DateUtil
				.getCurrentDate("yyyy-MM-dd HH:mm:ss"));

		String wfid = faApplications.getWfid();

		/*// 判断是送审保存还是只保存
		if (isSub.equals("1")) {// 保存并送审
			faApplications.setWfstatus("01");
		} else {
			faApplications.setWfstatus("00");
		}*/
		applicationsDao.update(faApplications);

		// 附件
		if (itemids != null && !"".equals(itemids)) {
			List<Long> l = new ArrayList<Long>();
			String[] it = itemids.split(",");
			for (int i = 0; i < it.length; i++) {
				l.add(Long.valueOf(it[i]));
			}
			fileManageComponent.updateKeyid(
					String.valueOf(faApplications.getApplicationId()), l);
		}

		// 判断是送审保存还是只保存
		if (isSub.equals("1")) {// 保存并送审
			// 判断是否是从未送审的记录（从未送审与送审过退回的记录不一样）
			if ("".equals(StringUtil.stringConvert(wfid))) {
				// 触发工作流
				String msg= bwfc.sendWorkFlow(menuid,String.valueOf(faApplications.getApplicationId()),new HashMap(),"");
				if(!"".equals(msg)){
					throw new Exception(msg);
				}
			} else {
				bwfc.completeTask(String.valueOf(faApplications.getApplicationId()), new HashMap(), "", "", "", activityId, "");
			}
		}
	}
	@Override
	public void saveEditRevokeInfoNoWF(FaApplications faApplications,
			Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		// 获取参数
		String isSub = param.get("isSub") == null ? "" : param.get("isSub")
				.toString();
		String menuid = param.get("menuid") == null ? "" : param.get("menuid")
				.toString();
		String itemids = param.get("itemids") == null ? "" : param.get(
				"itemids").toString();
		String activityId = param.get("activityId") == null ? "" : param.get(
				"activityId").toString();
		// 获取当前用户
		SysUser user = SecureUtil.getCurrentUser();
		
		faApplications.setUpdateUser(user.getUserid().toString());
		faApplications.setUpdateTime(DateUtil
				.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		
		String wfid = faApplications.getWfid();
		//此处不走工作流，所以直接把工作流状态改为20已注销
		if("1".equals(isSub)){
			faApplications.setWfstatus("20");
		}else if ("0".equals(isSub)) {
			faApplications.setWfstatus("00");
		}
		applicationsDao.update(faApplications);
		
		// 附件
		if (itemids != null && !"".equals(itemids)) {
			List<Long> l = new ArrayList<Long>();
			String[] it = itemids.split(",");
			for (int i = 0; i < it.length; i++) {
				l.add(Long.valueOf(it[i]));
			}
			fileManageComponent.updateKeyid(
					String.valueOf(faApplications.getApplicationId()), l);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveRevokeInfo(Map<String, Object> map, String itemids)
			throws AppException,Exception {

		String applicationId = "";
		FaApplications faApplications = new FaApplications();
		FaAccounts accounts = new FaAccounts();
		Map variables = new HashMap();

		// 获取当前用户
		SysUser user = SecureUtil.getCurrentUser();
		// 获取菜单ID，为了获取工作流标识
		String menuid = map.get("menuid") == null ? "" : ((String[]) map
				.get("menuid"))[0];
		// 获取保存或保存送审的标识
		String isSub = map.get("isSub") == null ? "" : ((String[]) map
				.get("isSub"))[0];

		// 将map中的值放到po对象中
		BeanUtils.populate(faApplications, map);

		// 判断是送审保存还是只保存
		if (isSub.equals("1")) {// 保存并送审
			faApplications.setWfstatus("01");
		} else {// 保存
			faApplications.setWfstatus("00");
		}
		faApplications.setCreateUser(user.getUserid().toString());
		faApplications.setType(3);
		faApplications.setCreateTime(DateUtil
				.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		applicationsDao.save(faApplications);

		// 获取保存后的主键
		applicationId = String.valueOf(faApplications.getApplicationId());

		// 2.保存到FA_ACCOUNTS表中
		accounts.setApplicationId(faApplications.getApplicationId());
		String accountName = map.get("accountName") == null ? "": ((String[]) map.get("accountName"))[0];
		accounts.setOldAccountName(accountName);
		accounts.setAccountName(accountName);
		String accountNumber = map.get("accountNumber") == null ? "": ((String[]) map.get("accountNumber"))[0];
		accounts.setOldAccountNumber(accountNumber);
		accounts.setAccountNumber(accountNumber);
		String accountType = map.get("accountType") == null ? "": ((String[]) map.get("accountType"))[0];
		accounts.setOldAccountType(accountType);
		accounts.setAccountType(accountType);
		String bankCode = map.get("bankCode") == null ? "": ((String[]) map.get("bankCode"))[0];
		accounts.setOldBankCode(bankCode);
		accounts.setBankCode(bankCode);
		String type02 = map.get("type02") == null ? "" : ((String[]) map.get("type02"))[0];
		accounts.setOldType02(type02);
		accounts.setType02(type02);
		Integer iszero =map.get("iszero") == null ? 0 : Integer.parseInt((((String[]) map.get("iszero"))[0]));
		accounts.setOldIszero(iszero);
		accounts.setIszero(iszero);
		String legalPerson = map.get("legalPerson") == null ? "": ((String[]) map.get("legalPerson"))[0];
		accounts.setOldLegalPerson(legalPerson);
		accounts.setLegalPerson(legalPerson);
		String idcardno = map.get("idcardno") == null ? "": ((String[]) map.get("idcardno"))[0];
		accounts.setOldIdcardno(idcardno);
		accounts.setIdcardno(idcardno);
		String financialOfficer = map.get("financialOfficer") == null ? "": ((String[]) map.get("financialOfficer"))[0];
		accounts.setOldFinancialOfficer(financialOfficer);
		accounts.setFinancialOfficer(financialOfficer);
		String accountContent =  map.get("accountContent") == null ? "": ((String[]) map.get("accountContent"))[0];
		accounts.setOldAccountContent(accountContent);
		accounts.setAccountContent(accountContent);
		
		accountsDao.save(accounts);

		// 3.修改FA_ACCOUNT_ARCHIVEDAO
		DetachedCriteria dc = DetachedCriteria.forClass(FaAccountArchive.class);
		int itemid;
		if (map.get("itemid") != null
				&& ((String[]) map.get("itemid"))[0] != null) {
			itemid = Integer.parseInt((((String[]) map.get("itemid"))[0]));
		} else {
			throw new Exception("在更新FA_ACCOUNT_ARCHIVE表时，没有获取到主键值！");
		}
		dc.add(Property.forName("itemid").eq(itemid));
		List<?> faAccountArchives = accountArchiveDao.findByCriteria(dc);
		
		FaAccountArchive fa = null;
		if (faAccountArchives.size() == 1) {
			fa = (FaAccountArchive) faAccountArchives.get(0);
			
			//判断是否ischange已改变，若已改变抛出异常
			if("1".equals(fa.getIschange())){
				throw new Exception("该账户信息已被变更或注销，不能再次变更！");
			}
			
			fa.setIschange(1);
			fa.setApplicationId(faApplications.getApplicationId());
			fa.setUpdateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			fa.setUpdateUser(user.getUserid().toString());
		} else {
			throw new Exception("更新FA_ACCOUNT_ARCHIVE表时出错！");
		}
		accountArchiveDao.update(fa);

		// 4.保存附件
		if (itemids != null && !"".equals(itemids)) {
			List<Long> l = new ArrayList<Long>();
			String[] it = itemids.split(",");
			for (int i = 0; i < it.length; i++) {
				l.add(Long.valueOf(it[i]));
			}
			fileManageComponent.updateKeyid(
					String.valueOf(faApplications.getApplicationId()), l);
		}

		// 5.如果是保存并送审，需要触发工作流
		if (isSub.equals("1")) {// 保存并送审
			// 触发工作流
			String msg = bwfc.sendWorkFlow(menuid, String.valueOf(applicationId),
					new HashMap(),"");
			if(!"".equals(msg)){//撤回失败
				throw new AppException(msg);
			}
		}
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveAddRevokeInfoNoWF(Map<String, Object> map, String itemids)
			throws AppException,Exception {
		
		String applicationId = "";
		FaApplications faApplications = new FaApplications();
		FaAccounts accounts = new FaAccounts();
		Map variables = new HashMap();
		
		// 获取当前用户
		SysUser user = SecureUtil.getCurrentUser();
		// 获取菜单ID，为了获取工作流标识
		String menuid = map.get("menuid") == null ? "" : ((String[]) map
				.get("menuid"))[0];
		// 获取保存或保存送审的标识
		String isSub = map.get("isSub") == null ? "" : ((String[]) map
				.get("isSub"))[0];
		
		// 将map中的值放到po对象中
		BeanUtils.populate(faApplications, map);
		//此处不走工作流，所以直接把工作流状态改为20已注销
		if("1".equals(isSub)){
			faApplications.setWfstatus("20");
		}else if ("0".equals(isSub)) {
			faApplications.setWfstatus("00");
		}
		faApplications.setCreateUser(user.getUserid().toString());
		faApplications.setType(3);
		faApplications.setCreateTime(DateUtil
				.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		applicationsDao.save(faApplications);
		
		// 获取保存后的主键
		applicationId = String.valueOf(faApplications.getApplicationId());
		
		// 2.保存到FA_ACCOUNTS表中
		accounts.setApplicationId(faApplications.getApplicationId());
		String accountName = map.get("accountName") == null ? "": ((String[]) map.get("accountName"))[0];
		accounts.setOldAccountName(accountName);
		accounts.setAccountName(accountName);
		String accountNumber = map.get("accountNumber") == null ? "": ((String[]) map.get("accountNumber"))[0];
		accounts.setOldAccountNumber(accountNumber);
		accounts.setAccountNumber(accountNumber);
		String accountType = map.get("accountType") == null ? "": ((String[]) map.get("accountType"))[0];
		accounts.setOldAccountType(accountType);
		accounts.setAccountType(accountType);
		String bankCode = map.get("bankCode") == null ? "": ((String[]) map.get("bankCode"))[0];
		accounts.setOldBankCode(bankCode);
		accounts.setBankCode(bankCode);
		String type02 = map.get("type02") == null ? "" : ((String[]) map.get("type02"))[0];
		accounts.setOldType02(type02);
		accounts.setType02(type02);
		Integer iszero =map.get("iszero") == null ? 0 : Integer.parseInt((((String[]) map.get("iszero"))[0]));
		accounts.setOldIszero(iszero);
		accounts.setIszero(iszero);
		String legalPerson = map.get("legalPerson") == null ? "": ((String[]) map.get("legalPerson"))[0];
		accounts.setOldLegalPerson(legalPerson);
		accounts.setLegalPerson(legalPerson);
		String idcardno = map.get("idcardno") == null ? "": ((String[]) map.get("idcardno"))[0];
		accounts.setOldIdcardno(idcardno);
		accounts.setIdcardno(idcardno);
		String financialOfficer = map.get("financialOfficer") == null ? "": ((String[]) map.get("financialOfficer"))[0];
		accounts.setOldFinancialOfficer(financialOfficer);
		accounts.setFinancialOfficer(financialOfficer);
		String accountContent =  map.get("accountContent") == null ? "": ((String[]) map.get("accountContent"))[0];
		accounts.setOldAccountContent(accountContent);
		accounts.setAccountContent(accountContent);
		
		accountsDao.save(accounts);
		
		// 3.修改FA_ACCOUNT_ARCHIVEDAO
		DetachedCriteria dc = DetachedCriteria.forClass(FaAccountArchive.class);
		int itemid;
		if (map.get("itemid") != null
				&& ((String[]) map.get("itemid"))[0] != null) {
			itemid = Integer.parseInt((((String[]) map.get("itemid"))[0]));
		} else {
			throw new Exception("在更新FA_ACCOUNT_ARCHIVE表时，没有获取到主键值！");
		}
		dc.add(Property.forName("itemid").eq(itemid));
		List<?> faAccountArchives = accountArchiveDao.findByCriteria(dc);
		
		FaAccountArchive fa = null;
		if (faAccountArchives.size() == 1) {
			fa = (FaAccountArchive) faAccountArchives.get(0);
			//判断是否ischange已改变，若已改变抛出异常
			if("1".equals(fa.getIschange())){
				throw new Exception("该账户信息已被变更或注销，不能再次变更！");
			}
			fa.setIschange(1);
			fa.setApplicationId(faApplications.getApplicationId());
			fa.setUpdateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			fa.setUpdateUser(user.getUserid().toString());
		} else {
			throw new Exception("更新FA_ACCOUNT_ARCHIVE表时出错！");
		}
		accountArchiveDao.update(fa);
		
		// 4.保存附件
		if (itemids != null && !"".equals(itemids)) {
			List<Long> l = new ArrayList<Long>();
			String[] it = itemids.split(",");
			for (int i = 0; i < it.length; i++) {
				l.add(Long.valueOf(it[i]));
			}
			fileManageComponent.updateKeyid(
					String.valueOf(faApplications.getApplicationId()), l);
		}
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteRevokeInfo(String applicationIds) throws Exception {
		// TODO Auto-generated method stub

		// //删除fa_APPLICATIONS中的记录
		String hql = " from FaApplications where applicationId in("
				+ applicationIds + ")";
		List<FaApplications> list = (List<FaApplications>) applicationsDao.find(hql);
		applicationsDao.deleteAll(list);
		
		
		// 删除fa_accounts表中的记录
		String hql1 = " from FaAccounts where applicationId in("
				+ applicationIds + ")";
		List<FaAccounts> list1 = (List<FaAccounts>) accountsDao.find(hql1);
		accountsDao.deleteAll(list1);

		// 恢复FA_ACCOUNT_ARCHIVE中的状态
		accountArchiveDao.updateFaAccountArchiveByApplicationId(applicationIds);
		
		//删除附件
		fileManageComponent.deleteFiles(applicationIds, "FAAPPLICATION");

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String removeRevokeInfo(String applicationIds, String menuid,
			String activityId,String isba) throws Exception {
		String msg = "";
		if("".equals(isba) || isba==null){
			// 工作流撤回
			msg = bwfc.revokeWorkFlow(applicationIds, new HashMap(), "",activityId,"");
		}else{
			msg = bwfc.revokeWorkFlow(applicationIds, new HashMap(), "",activityId,"1");
		}
		if(!"".equals(msg)){//撤回失败
			throw new Exception(msg);
		}
		return msg;
	}

	@Override
	public void submitRevokeInfo(String applicationIds, String menuid,String activityId)throws Exception {
		StringBuffer returnSql = new StringBuffer();
		String[] applicationIdArray =applicationIds.split(",");
		for (int i = 0; i < applicationIdArray.length; i++) {
			FaApplications dbApplication = applicationsDao.get(Integer.parseInt(applicationIdArray[i]));
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
			throw new AppException(returnSql.toString());
		}
	}

	@Override
	public boolean getTaskFormEditable(String wfid, String activityId)
			throws Exception {
		// TODO Auto-generated method stub
		return bwfc.getTaskFormEditable(wfid, activityId);
	}

	@Override
	public Set<String> getOutComes(String wfid, String activityId,
			String outcomeType) throws Exception {
		// TODO Auto-generated method stub
		return bwfc.getOutCome(wfid, activityId, outcomeType);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String verifyWorkFlow(Map<String, Object> param,FaApplications faApplication) throws AppException,Exception {
		String returnString = "";
		
		String activityId = StringUtil.stringConvert(param.get("activityId"));
		String itemids = param.get("itemids") == null ? "" : param.get("itemids").toString();
		String wfid = faApplication.getWfid();
		
		boolean formIsEdit = getTaskFormEditable(wfid,activityId);
		
		if(formIsEdit){
			// 获取当前用户
			SysUser user = SecureUtil.getCurrentUser();
			faApplication.setUpdateUser(user.getUserid().toString());
			faApplication.setUpdateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));

			//主要重新更新保存注销原因
			applicationsDao.update(faApplication);
			
			//更新附件
			if (itemids != null && !"".equals(itemids)) {
				List<Long> l = new ArrayList<Long>();
				String[] it = itemids.split(",");
				for (int i = 0; i < it.length; i++) {
					l.add(Long.valueOf(it[i]));
				}
				fileManageComponent.updateKeyid(
						String.valueOf(faApplication.getApplicationId()), l);
			}
		}

		// 是否退回标志
		String isback = StringUtil.stringConvert(param.get("isback"));
		// 审批意见
		String opinion = StringUtil.stringConvert(param.get("opinion"));
		// 流出路径，暂定
		//String outcome = StringUtil.stringConvert(param.get("outcome"));
		String applicationId = StringUtil.stringConvert(param
				.get("applicationId"));
		returnString = bwfc.completeTask(applicationId, param, "",
				opinion, isback, activityId,"");
		if (!"".equals(returnString)) {
			throw new AppException(returnString);
		}
		
		return returnString;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String verifyWorkFlow(Map<String, Object> param) throws AppException {
		String returnString = "";

		// 是否退回标志
		String isback = StringUtil.stringConvert(param.get("isback"));
		// 审批意见
		String opinion = StringUtil.stringConvert(param.get("opinion"));
		// 流出路径，暂定
		String outcome = StringUtil.stringConvert(param.get("outcome"));
		String activityId = StringUtil.stringConvert(param.get("activityId"));
		String isba = StringUtil.stringConvert(param.get("isba"));
		String applicationId = StringUtil.stringConvert(param
				.get("applicationId"));
		if("".equals(isba) || isba == null){
			
			returnString = bwfc.completeTask(applicationId, new HashMap(), outcome,
					opinion, isback, activityId,"");
			String menuid =  StringUtil.stringConvert(param.get("menuid"));
			try {
				SysResource	sr = bwfc.getResourceById(menuid);
				Integer wftasknode = sr.getWftasknode();//（0=首任务节点；1=中间任务节点；2=末任务节点）
				if (wftasknode == 2) {
					String sql = "update fa_applications t set t.operatenum=nvl(t.operatenum,0)+1 where t.application_id='"+param.get("applicationId")+"'";
					accountArchiveDao.updateBySql(sql);
				}	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			returnString = bwfc.completeTask(applicationId, new HashMap(), outcome,
					opinion, isback, activityId,"1");
		}
		if (!"".equals(returnString)) {
			throw new AppException(returnString);
		}
		return returnString;
	}
	@Override
	public int saveMessage(Integer applicationId, String phone, String message,String activityId) {
		return messageComponent.saveMessage(applicationId, phone, message, activityId);
	}

	@Override
	public List<HistoryOpinionVO> getworkFlowList(String wfid) {
		// TODO Auto-generated method stub
		return bwfc.getworkFlowList(wfid);
	}
	@Override
	public PaginationSupport queryChangeAccount(Map<String, Object> param) throws AppException {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select t.*,");
		sql.append("       (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='ACCTTYPE' and a.status=0  and a.code=t.OLD_ACCOUNT_TYPE) OLD_ACCOUNT_TYPE_NAME,");
		sql.append("       (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='ACCTTYPE' and a.status=0  and a.code=t.ACCOUNT_TYPE) ACCOUNT_TYPE_NAME,");
		sql.append("      bdgagencycode ||'-'|| bdgagencyname bdgagencycn,");
		sql.append("      (select a.NAME from fav_bank a where a.CODE=t.old_bank_code) OLD_BANK_NAME,");
		sql.append("      (select a.CODE||'-'||a.NAME from fav_bank a where a.CODE=t.old_bank_code) old_Bank_Name_Cn,");
		sql.append("      (select a.NAME from fav_bank a where a.CODE=t.bank_code) BANK_NAME,");
		sql.append("      (select a.CODE||'-'||a.NAME from fav_bank a where a.CODE=t.bank_code) Bank_Name_Cn,");
		sql.append("      (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='WFSTATUS' and a.status=0  and a.code=t.WFSTATUS) wfstatus_Name,");
		sql.append("      (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='AGENCYTYPE' and a.status=0  and a.code=t.DEPT_NATURE) dept_Nature_Name,");
		sql.append("   (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='ACCTNATURE' and a.status=0  and a.code=t.OLD_TYPE02) old_Type02_Name,");
		sql.append("   (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='ACCTNATURE' and a.status=0  and a.code=t.TYPE02) Type02_Name,");
		sql.append("   (select a.username from sys_user a where  a.userid=t.create_user) create_user_name,");
		sql.append("   (select a.username from sys_user a where  a.userid=t.update_user) update_user_name,");
		sql.append("  (case when t.iszero =0 then '否' when  t.iszero =1 then '是' end ) iszero_name,");
		sql.append("  (case when t.old_iszero =0 then '否' when  t.old_iszero =1 then '是' end ) old_iszero_name ");
		
		String type = StringUtil.stringConvert(param.get("type"));//类型 1, "新开立",2, "变更",3, "撤销";
		if("2".equals(type)){
			sql.append("  , (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='CHANGETYPE' and a.status=0  and a.code=t.changetype) changetype_name ");
		}else if("3".equals(type)){
			sql.append("  , (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='REVOKETYPE' and a.status=0  and a.code=t.changetype) changetype_name ");
		}
		
		sql.append("  from fav_appl_account t where 1=1 ");
		String where ="";
		if(StringUtil.isNotBlank(type)){
			where +=" and type = "+type;
		}else{
			throw new AppException("查询参数：备案类型为空！");
		}
		String menuid = StringUtil.stringConvert(param.get("menuid"));
		if ("".equals(menuid)) {
			throw new AppException("查询参数：menuid不能为空！");
		}
		String changetype =StringUtil.stringConvert(param.get("changetype"));
		if(!"".equals(changetype)){
			where += " and changetype = '"+changetype+"' ";
		}
		//数据权限
		String bdgagency = fdpf.getConditionFilter(Long.parseLong(menuid), "fav_appl_account", "t");
		if(StringUtil.isNotBlank(bdgagency)){
			where +=" and  "+bdgagency;
		}
		String bdgagencycode = StringUtil.stringConvert(param.get("bdgagencycode"));
		if(StringUtil.isNotBlank(bdgagencycode)){
			where +=" and  bdgagencycode in( "+bdgagencycode+")";
		}
		String accountType = StringUtil.stringConvert(param.get("accountType"));
		if(StringUtil.isNotBlank(accountType)){
			if ("1".equals(type)) {
				where +=" and  account_type ='"+accountType+"'";
			}else {
				where +=" and  old_account_type ='"+accountType+"'";
			}
		}
		String type02 = StringUtil.stringConvert(param.get("type02"));
		if(StringUtil.isNotBlank(type02)){
			if ("1".equals(type)) {
				where +=" and  type02 ='"+type02+"'";
			}else {
				where +=" and  old_type02 ='"+type02+"'";
			}
		}
		String accountNumber = StringUtil.stringConvert(param.get("accountNumber"));
		if(StringUtil.isNotBlank(accountNumber)){
			if ("1".equals(type)) {
				where +=" and  account_Number like '%"+accountNumber+"%'";
			}else {
				where +=" and  old_account_number like '%"+accountNumber+"%'";
			}
		}
		String accountName = StringUtil.stringConvert(param.get("accountName"));
		if(StringUtil.isNotBlank(accountName)){
			if ("1".equals(type)) {
				where +=" and  account_Number like '%"+accountName+"%'";
			}else {
				where +=" and  old_account_Name like '%"+accountName+"%'";
			}
		}
		String starttime = StringUtil.stringConvert(param.get("starttime"));
		if (StringUtil.isNotBlank(starttime)) {
			where +=" and  to_date(create_time,'yyyy-mm-dd hh24:mi:ss') >= to_date('"+starttime+"','yyyy-mm-dd')";
		}
		String endtime = StringUtil.stringConvert(param.get("endtime"));
		if (StringUtil.isNotBlank(endtime)) {
			where +=" and  to_date(create_time,'yyyy-mm-dd hh24:mi:ss') < trunc(to_date('"+endtime+"','yyyy-mm-dd')+1)";
		}
		String status = StringUtil.stringConvert(param.get("status"));
		if("1".equals(status)){//待处理
			where += " and wfstatus = '00'";
		}else if ("2".equals(status)) {//已处理
			where += " and wfstatus = '20'";
		}
		sql.append(where);
		sql.append(" order by bdgagencycode ,create_time desc");
		System.err.println("sql.toString()---------------"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
		
	}

	@Override
	public SysResource getResourceById(String menuid) throws Exception {
		// TODO Auto-generated method stub
		
		return bwfc.getResourceById(menuid);
	}
	
	public List<Map> getSendUser(String wfid,String activityId){
		return bwfc.getSendUser(wfid, activityId);
	}

	@Override
	public String getMessageContent(String applicationId, String wfid,
			String isBa, String cllx, String activityId) {
		// TODO Auto-generated method stub
		
		return bwfc.getMessageContent(applicationId, wfid, isBa, cllx, activityId);
	}
}
