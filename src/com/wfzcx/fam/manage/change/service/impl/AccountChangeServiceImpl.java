package com.wfzcx.fam.manage.change.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
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
import com.jbf.sys.log.service.impl.SysLogApp;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.vo.HistoryOpinionVO;
import com.wfzcx.fam.common.DeptComponent;
import com.wfzcx.fam.common.MessageComponent;
import com.wfzcx.fam.dataPermission.FamDataPermissionFilter;
import com.wfzcx.fam.manage.RecordType;
import com.wfzcx.fam.manage.change.service.AccountChangeService;
import com.wfzcx.fam.manage.dao.FaAccountArchiveDao;
import com.wfzcx.fam.manage.dao.FaAccountsDao;
import com.wfzcx.fam.manage.dao.FaApplicationsDao;
import com.wfzcx.fam.manage.dao.FavApplAccountDao;
import com.wfzcx.fam.manage.po.FaAccounts;
import com.wfzcx.fam.manage.po.FaApplications;
import com.wfzcx.fam.manage.po.FavApplAccount;
import com.wfzcx.fam.workflow.BussinessWorkFlowComponent;

/**
 * 账户变更service实现类
 * @ClassName: AccountManageServiceImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年4月2日 上午11:40:27
 */
@Scope("prototype")
@Service("com.wfzcx.fam.manage.change.service.impl.AccountChangeServiceImpl")
public class AccountChangeServiceImpl implements AccountChangeService {
	
	@Autowired
	FaAccountsDao faAccountsDao;
	
	@Autowired
	FaApplicationsDao faApplicationsDao;
	
	@Autowired
	FavApplAccountDao favApplAccountDao;
	@Autowired
	FaAccountArchiveDao faAccountArchiveDao;
	@Autowired
	BussinessWorkFlowComponent bwfc;
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	FileManageComponent fileManageComponent;
	@Autowired
	MessageComponent messageComponent;
	@Autowired
	ParamCfgComponent pcfg;
	@Autowired
	FamDataPermissionFilter fdpf;
	@Override
	public PaginationSupport queryAccount(Map<String, Object> param) throws AppException {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select t.*,");
		sql.append("       ( case status when 1 then '正常'when 9 then '撤销' end  )status_Name,");
		sql.append("        (case iszero when 1 then '是'when 0 then '否' end )  iszero_Name,");
		sql.append("       bank_code ||'-'|| bank_name Bank_Name_Cn,");
		sql.append("       (select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='ACCTNATURE' and t.status=0  and t.code=type02) type02_Name,");
		sql.append("       (select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='ACCTTYPE' and t.status=0  and t.code=ACCOUNT_TYPE)account_Type_Name,");
		sql.append("       bdgagencycode ||'-'|| bdgagencyname bdgagencycn,");
		sql.append("       (select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='AGENCYTYPE' and t.status=0  and t.code=DEPT_NATURE) dept_Nature_Name,");
		sql.append("       (select a.username from sys_user a where  a.userid=t.create_user) create_user_name,");
		sql.append("       (select a.username from sys_user a where  a.userid=t.update_user) update_user_name");
		sql.append("  from fa_account_archive t where ischange =0 and status=1 ");
		//页面选择预算单位
		String bdgagencycode = (String) param.get("bdgagencycode");
		if(StringUtil.isNotBlank(bdgagencycode)){
			sql.append(" and (t.bdgagencycode like '%").append(bdgagencycode).append("%'");
			sql.append(" or t.bdgagencyname like '%").append(bdgagencycode).append("%')");
		}
		//账户类型
		String accountType = (String) param.get("accountType");
		if(StringUtil.isNotBlank(accountType)){
			sql.append(" and t.account_Type = '").append(accountType).append("'");
		}
		//账户性质
		String type02 = (String) param.get("type02");
		if(StringUtil.isNotBlank(type02)){
			sql.append(" and t.type02 = '").append(type02).append("'");
		}
		//账号
		String accountNumber = (String) param.get("accountNumber");
		if(StringUtil.isNotBlank(accountNumber)){
			sql.append(" and t.account_Number like '%").append(accountNumber).append("%'");
		}
		//账号
		String accountName = (String) param.get("accountName");
		if(StringUtil.isNotBlank(accountName)){
			sql.append(" and t.account_Name like '%").append(accountName).append("%'");
		}
		String menuid = StringUtil.stringConvert(param.get("menuid"));
		if ("".equals(menuid)) {
			throw new AppException("查询参数：menuid不能为空！");
		}
		//数据权限
		String bdgagency = fdpf.getConditionFilter(Long.parseLong(menuid), "fa_account_archive", "t");
		if(StringUtil.isNotBlank(bdgagency)){
			sql.append(" and ").append(bdgagency);
		}
		sql.append(" order by bdgagencycode");
		System.err.println(sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
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
		String activityId = StringUtil.stringConvert(param.get("activityId"));
		String status = StringUtil.stringConvert(param.get("status"));
		String processedStatus = StringUtil.stringConvert(param.get("processedStatus"));
		//获取工作流状态
		boolean firstNode = param.get("firstNode")==null?false: Boolean.parseBoolean(param.get("firstNode").toString());
		boolean lastNode =   param.get("lastNode")==null?false: Boolean.parseBoolean(param.get("lastNode").toString());
		String wfids = bwfc.findCurrentWfids(menuid, activityId, status, processedStatus, firstNode, lastNode);
		where += " and "+wfids;
		sql.append(where);
		sql.append(" order by bdgagencycode ,create_time desc");
		System.err.println("sql.toString()---------------"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String changeAccountSaveAdd(Map<String, Object> param)throws AppException {
		int applicationId ;
		try {
			/**
			 * 保存步骤
			 * 1.保存账户主表
			 * 2.保存账户从表
			 * 3.更新账户档案表
			 * 4、如果是保存并送审，需要触发工作流
			 */
			applicationId = saveAdd(param);
			String workflowflag = param.get("workflowflag").toString();
			//更改档案表
			Integer itemid = Integer.parseInt(param.get("itemid").toString());
			faAccountArchiveDao.updateFaAccountArchiveByItemid(itemid, 1, applicationId);
			if(workflowflag.equals("1")){
				String menuid = param.get("menuid")==null?"":param.get("menuid").toString();
				//触发工作流
				String  msg =bwfc.sendWorkFlow(menuid, String.valueOf(applicationId),new HashMap(),"");
				if(!"".equals(msg)){
					throw new AppException(msg);
				}
			}
			boolean fileIsUpload =  fileUpload(applicationId, StringUtil.stringConvert(param.get("itemids")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
		return String.valueOf(applicationId);
	}
	public Integer saveAdd(Map<String, Object> param) throws Exception{
		FaApplications application = new FaApplications();
		FaAccounts account = new FaAccounts();
			BeanUtils.populate(application, param);
			BeanUtils.populate(account, param);
			account.setWfid(null);
			account.setWfstatus(null);
			//电话号码对照
			application.setPhonenumber(param.get("applPhonenumber").toString());
			//当前时间
			application.setCreateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			SysUser user = SecureUtil.getCurrentUser();
			application.setCreateUser(user.getUserid().toString());
			//变更--备案类型
			application.setType(RecordType.CHANGE.getIndex());
			String workflowflag = param.get("workflowflag").toString();
			//主表保存并返回保存后的主键
			Integer applicationId = (Integer) faApplicationsDao.save(application);
			account.setApplicationId(applicationId);
			//账户类型
			String accountType = StringUtil.stringConvert(param.get("accountType"));
			if ("2".equals(accountType) ||"3".equals(accountType)) {//3单位零余额2财政零余额
				account.setIszero(1);//是零余额账户
			}else {
				account.setIszero(0);//不是零余额账户
			}
			String wfstatus =application.getWfstatus();
			if (StringUtil.isBlank(wfstatus)) {//如果当前状态为空
				if(workflowflag.equals("1")){
						//当前状态
						application.setWfstatus("01"); //送审
				}else{
					//当前状态
					application.setWfstatus("00"); //保存
				}
			}
			//保存账户从表
			faAccountsDao.save(account);
			return applicationId;
	}
	public void saveUpdate(Map<String, Object> param) throws Exception{
		FaApplications application =faApplicationsDao.get(Integer.parseInt(String.valueOf(param.get("applicationId"))));
		FaAccounts account = faAccountsDao.get(Integer.parseInt(String.valueOf(param.get("accountId"))));
			BeanUtils.populate(application, param);
			BeanUtils.populate(account, param);
			account.setWfid(null);
			account.setWfstatus(null);
			//电话号码对照
			application.setPhonenumber(param.get("applPhonenumber").toString());
			//当前时间
			application.setUpdateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			SysUser user = SecureUtil.getCurrentUser();
			application.setUpdateUser(user.getUserid().toString());
			String workflowflag = param.get("workflowflag").toString();
			//主表保存并返回保存后的主键
			 faApplicationsDao.update(application);
			//账户类型
			String accountType = StringUtil.stringConvert(param.get("accountType"));
			if ("2".equals(accountType) ||"3".equals(accountType)) {//3单位零余额2财政零余额
				account.setIszero(1);//是零余额账户
			}else {
				account.setIszero(0);//不是零余额账户
			}
			String wfstatus =application.getWfstatus();
			if (StringUtil.isBlank(wfstatus)) {//如果当前状态为空
				if(workflowflag.equals("1")){
						//当前状态
						application.setWfstatus("01"); //送审
				}else{
					//当前状态
					application.setWfstatus("00"); //保存
				}
			}
			//保存账户从表
			faAccountsDao.update(account);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String changeAccountSaveEdit(Map<String, Object> param)
			throws AppException {
		try {
			saveUpdate(param);
			String workflowflag = param.get("workflowflag").toString();
			if(workflowflag.equals("1")){
				String menuid = param.get("menuid")==null?"":param.get("menuid").toString();
				String  wfid = StringUtil.stringConvert(param.get("wfid"));
				String applicationId =StringUtil.stringConvert(param.get("applicationId"));
				String msg="";
				if("".equals(StringUtil.stringConvert(wfid))){
					//触发工作流
					msg = bwfc.sendWorkFlow(menuid,applicationId,new HashMap(),"");
				}else {
					String activityId  =StringUtil.stringConvert(param.get("activityId"));
					msg = bwfc.completeTask(applicationId, new HashMap(), "", "", "", activityId, "");
				}
				if (!"".equals(msg)) {
					throw new AppException(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void changeAccountDelete(String applicationIds) throws AppException {
		//删除fa_applications
		String hql = " from FaApplications where applicationId in("+applicationIds+")";
		List<FaApplications> list = (List<FaApplications>) faApplicationsDao.find(hql);
		faApplicationsDao.deleteAll(list);
		//删除fa_accounts
		String hql1 = " from FaAccounts where applicationId in("+applicationIds+")";
		List<FaAccounts> list1 = (List<FaAccounts>) faAccountsDao.find(hql1);
		faAccountsDao.deleteAll(list1);
		//更新fa_account_archive
		faAccountArchiveDao.updateFaAccountArchiveByApplicationId(applicationIds);
		try {
			//删除附件,附件删除不要影响业务
			fileManageComponent.deleteFiles(applicationIds, "FAAPPLICATION");
		} catch (Exception e) {
			SysLogApp.writeLog("删除附件时发生异常，applicationId为"+applicationIds,5);
		}
	}

	@Override
	public void sendWorkFlow(String menuid, String applicationIds,String activityId)throws AppException {
		StringBuffer returnSql = new StringBuffer();
		String[] applicationIdArray =applicationIds.split(",");
		for (int i = 0; i < applicationIdArray.length; i++) {
			FaApplications dbApplication = faApplicationsDao.get(Integer.parseInt(applicationIdArray[i]));
			String wfid = StringUtil.stringConvert(dbApplication.getWfid());
			String msg ="";
			if("".equals(wfid)){//为发起工作流
				msg = bwfc.sendWorkFlow(menuid,applicationIdArray[i],new HashMap(),"");
			}else {//已发起工作流
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
	public String revokeWorkFlow(String menuid, String applicationIds,String activityId)throws AppException {
		//工作流撤销
		return bwfc.revokeWorkFlow(applicationIds, new HashMap(), "", activityId,"");
		
	}

	@Override
	public PaginationSupport queryChangeAccountForAudit(Map<String, Object> param) {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		DetachedCriteria dc = DetachedCriteria.forClass(FavApplAccount.class);
		dc.add(Property.forName("type").eq(RecordType.CHANGE.getIndex()));
		//增加权限本级及以下
		List list = DeptComponent.getCurAndLowerCode();
		//dc.add(Property.forName("bdgagencycode").in(list));
		dc.addOrder(Order.desc("applicationId"));
		return faAccountsDao.findByCriteria(dc, pageSize, pageIndex);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String changeAccountApprove(Map<String, Object> param)throws AppException {
			FaApplications application = new FaApplications();
			FaAccounts account = new FaAccounts();
			try {
				BeanUtils.populate(application, param);
				BeanUtils.populate(account, param);
				account.setWfstatus("");
				//电话号码转换
				account.setPhonenumber(param.get("acctPhonenumber").toString());
				application.setPhonenumber(param.get("applPhonenumber").toString());
				application.setUpdateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				SysUser user = SecureUtil.getCurrentUser();
				application.setUpdateUser(user.getUserid().toString());
				faApplicationsDao.update(application);
				//账户类型
				String accountType = StringUtil.stringConvert(param.get("accountType"));
				if ("2".equals(accountType) ||"3".equals(accountType)) {//3单位零余额2财政零余额
					account.setIszero(1);//是零余额账户
				}else {
					account.setIszero(0);//不是零余额账户
				}
				faAccountsDao.update(account);
				//是否退回标志
				String isback = StringUtil.stringConvert(param.get("isback"));
				//审批意见
				String opinion = StringUtil.stringConvert(param.get("opinion"));
				//流出路径，暂定
				String outcome = StringUtil.stringConvert(param.get("outcome"));
				String activityId = StringUtil.stringConvert(param.get("activityId"));
				String msg = bwfc.completeTask(String.valueOf(application.getApplicationId()), new HashMap(), outcome, opinion,isback,activityId,"");
				if(!"".equals(msg)){
					throw new AppException(msg);
				}
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	}

	@Override
	public String sendBackTask(String menuid, String applicationIds,String activityId,String opinion)throws AppException {
		//调查工作流退回
		return bwfc.sendBackTask(applicationIds, activityId, new HashMap(),opinion,"");
	}

	@Override
	public Set<String> getOutComes(String wfid, String activityId,
			String outcomeType) throws Exception {
		// TODO Auto-generated method stub
		return bwfc.getOutCome(wfid, activityId, outcomeType);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String verifyWorkFlow(Map<String, Object> param) throws AppException {
		String returnString = "";
		try {
			//是否退回标志
			String isback = StringUtil.stringConvert(param.get("isback"));
			//审批意见
			String opinion = StringUtil.stringConvert(param.get("opinion"));
			//流出路径，暂定
			String outcome = StringUtil.stringConvert(param.get("outcome"));
			String activityId = StringUtil.stringConvert(param.get("activityId"));
			String applicationId = StringUtil.stringConvert(param.get("applicationId"));
			String wfid = StringUtil.stringConvert(param.get("wfid"));
			
			//表单是否编辑
			boolean  isEdit = getTaskFormEditable(wfid, activityId);
			if (isEdit) {/*
				FaApplications application = faApplicationsDao.get(Integer.parseInt(StringUtil.stringConvert(param.get("applicationId"))));
				FaAccounts account = faAccountsDao.get(Integer.parseInt(StringUtil.stringConvert(param.get("accountId"))));
				BeanUtils.populate(application, param);
				BeanUtils.populate(account, param);
				account.setWfstatus("");
				//账户类型
				String accountType = StringUtil.stringConvert(param.get("accountType"));
				if ("2".equals(accountType) ||"3".equals(accountType)) {//3单位零余额2财政零余额
					account.setIszero(1);//是零余额账户
				}else {
					account.setIszero(0);//不是零余额账户
				}
				//电话号码转换
				application.setPhonenumber(param.get("applPhonenumber").toString());
				application.setUpdateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				SysUser user = SecureUtil.getCurrentUser();
				application.setUpdateUser(user.getUserid().toString());
				faApplicationsDao.update(application);
				faAccountsDao.update(account);*/
				saveUpdate(param);
				//附件上传
				fileUpload(Integer.parseInt(applicationId), StringUtil.stringConvert(param.get("itemids")));
			}
			returnString = bwfc.completeTask(applicationId, param, outcome, opinion,isback,activityId,"");
			String menuid =  StringUtil.stringConvert(param.get("menuid"));
			SysResource sr	=this.getResourceById(menuid);
			Integer wftasknode = sr.getWftasknode();//（0=首任务节点；1=中间任务节点；2=末任务节点）
			if (wftasknode == 2) {
				String sql = "update fa_applications t set t.operatenum=nvl(t.operatenum,0)+1 where t.application_id='"+param.get("applicationId")+"'";
				faApplicationsDao.updateBySql(sql);
			}
			if(!"".equals(returnString)){
				throw new AppException(returnString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return returnString;
}

	@Override
	public List<HistoryOpinionVO> getworkFlowList(String wfid) {
		// TODO Auto-generated method stub
		return bwfc.getworkFlowList(wfid);
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
		return messageComponent.saveMessage(applicationId, phone, message, activityId);
	}

	@Override
	public String findGeneralParamValue() {
		return pcfg.findGeneralParamValue("SYSTEM", "MESSAGEISUSE");
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
	@Override
	public String getChangeType(String wfid) {
		if("".equals(wfid)){
			return "";
		}
		String where =" wfid='"+wfid+"'";
		List<Map>  list = faAccountArchiveDao.queryBySQL("FA_APPLICATIONS",where);
		if(list.isEmpty()){
			return "";
		}else {
			Map map = list.get(0);
			BigDecimal changetype = map.get("changetype")==null? new BigDecimal(0): (BigDecimal) map.get("changetype");
			return String.valueOf(changetype);
		}
	}


	@Override
	public boolean getCanBackToFirstNode(String wfid, String activityId)
			throws Exception {
		// TODO Auto-generated method stub
		return bwfc.getCanBackToFirstNode(wfid, activityId);
	}
	public FavApplAccount getOldAccountInformation(String applicationId) {
		return  favApplAccountDao.get(Integer.parseInt(applicationId));
	}
	@Override
	public SysResource getResourceById(String menuid) throws Exception {
		// TODO Auto-generated method stub
		return bwfc.getResourceById(menuid);
	}
}
