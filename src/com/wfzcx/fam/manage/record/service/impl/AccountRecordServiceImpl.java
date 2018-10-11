package com.wfzcx.fam.manage.record.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.jbf.workflow.vo.HistoryOpinionVO;
import com.wfzcx.fam.common.MessageComponent;
import com.wfzcx.fam.dataPermission.FamDataPermissionFilter;
import com.wfzcx.fam.manage.dao.FaAccountArchiveDao;
import com.wfzcx.fam.manage.dao.FaAccountsDao;
import com.wfzcx.fam.manage.dao.FaApplicationsDao;
import com.wfzcx.fam.manage.dao.FavApplAccountDao;
import com.wfzcx.fam.manage.po.FaAccounts;
import com.wfzcx.fam.manage.po.FaApplications;
import com.wfzcx.fam.manage.po.FavApplAccount;
import com.wfzcx.fam.manage.record.service.AccountRecordService;
import com.wfzcx.fam.workflow.BussinessWorkFlowComponent;

/**
 * 账户备案service实现类
 * @ClassName: AccountRecordServiceImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年4月21日9:32:15
 */
@Scope("prototype")
@Service("com.wfzcx.fam.manage.record.service.impl.AccountRecordServiceImpl")
public class AccountRecordServiceImpl implements AccountRecordService {
	
	@Autowired
	FaAccountsDao faAccountsDao;
	
	@Autowired
	FaApplicationsDao faApplicationsDao;
	
	@Autowired
	FavApplAccountDao favApplAccountDao;
	@Autowired
	
	FaAccountArchiveDao faAccountArchiveDao;
	
	@Autowired
	FileManageComponent fileManageComponent;
	
	@Autowired
	BussinessWorkFlowComponent bwfc;
	
	@Autowired
	MessageComponent messageComponent;
	
	@Autowired
	MapDataDaoI mapDataDao;
	
	@Autowired
	FaApplicationsDao applicationsDao;
	
	@Autowired
	FamDataPermissionFilter fdpf;
	
	@Override
	public PaginationSupport queryChangeAccount(Map<String, Object> param) throws AppException{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		StringBuffer sql = new StringBuffer();
		sql.append("select APPLICATION_ID,");
		sql.append("       BDGAGENCYCODE,");
		sql.append("       TYPE,");
		sql.append("       DEPT_NATURE,");
		sql.append("       SUPERVISOR_DEPT,");
		sql.append("       DEPT_ADDRESS,");
		sql.append("       APPLY_REASON,");
		sql.append("       CHANGETYPE,");
		sql.append("       WFID,");
		sql.append("       WFSTATUS,");
		sql.append("       WFISBACK,");
		sql.append("       BDGAGENCYNAME,");
		sql.append("       APPL_PHONENUMBER,");
		sql.append("       LINKMAN,");
		sql.append("       CREATE_USER,");
		sql.append("       CREATE_TIME,");
		sql.append("       UPDATE_USER,");
		sql.append("       UPDATE_TIME,");
		sql.append("       ISREGISTER,");
		sql.append("       ACCOUNT_ID,");
		sql.append("       ACCOUNT_NAME,");
		sql.append("       ACCOUNT_TYPE,");
		sql.append("       ACCOUNT_NUMBER,");
		sql.append("       ACCOUNT_NUM,");
		sql.append("       BANK_CODE,");
		sql.append("       OPEN_TIME,");
		sql.append("       ACCOUNT_CONTENT,");
		sql.append("       old_account_name,");
		sql.append("       old_bank_code,");
		sql.append("       old_account_number,");
		sql.append("       old_account_content,");
		sql.append("       OLD_LEGAL_PERSON,");
		sql.append("       OLD_IDCARDNO,");
		sql.append("       OLD_FINANCIAL_OFFICER,");
		sql.append("       LEGAL_PERSON,");
		sql.append("       IDCARDNO,");
		sql.append("       FINANCIAL_OFFICER,");
		sql.append("       responsible_person,");
		sql.append("       acct_phonenumber,");
		sql.append("       iszero,");
		sql.append("       remark,");
		sql.append("       type02,");
		sql.append("       old_type02,");
		sql.append("       old_iszero,");
		sql.append("       old_account_type,");
		sql.append("       modify_user,");
		sql.append("       modify_time,");
		sql.append("       ( SELECT A.NAME FROM SYS_FA_DICENUMITEM A WHERE UPPER(A.ELEMENTCODE) = (CASE t.TYPE WHEN 2 THEN 'CHANGETYPE' WHEN 3 THEN 'REVOKETYPE' ELSE '' END ) AND A.STATUS = 0 AND A.CODE = t.CHANGETYPE) CHANGETYPE_NAME,");
		sql.append("       (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='ACCTTYPE' and a.status=0  and a.code=t.OLD_ACCOUNT_TYPE) OLD_ACCOUNT_TYPE_NAME,");
		sql.append("       (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='ACCTTYPE' and a.status=0  and a.code=t.ACCOUNT_TYPE) ACCOUNT_TYPE_NAME,");
		sql.append("       (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='REGISTERTYPE' and a.status=0  and a.code=t.TYPE) TYPE_NAME,");
		sql.append("      bdgagencycode ||'-'|| bdgagencyname bdgagencycn,");
		sql.append("      (select a.NAME from fav_bank a where a.CODE=t.old_bank_code) OLD_BANK_NAME,");
		sql.append("      (select a.CODE||'-'||a.NAME from fav_bank a where a.CODE=t.old_bank_code) old_Bank_Name_Cn,");
		sql.append("      (select a.NAME from fav_bank a where a.CODE=t.bank_code) BANK_NAME,");
		sql.append("      (select a.CODE||'-'||a.NAME from fav_bank a where a.CODE=t.bank_code) Bank_Name_Cn,");
		sql.append("      (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='WFSTATUS' and a.status=0  and a.code=t.WFSTATUS) wfstatus_Name,");
		sql.append("      (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='WFSTATUS' and a.status=0  and a.code=t.ACCT_WFSTATUS) acct_wfstatus_Name,");
		sql.append("      (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='AGENCYTYPE' and a.status=0  and a.code=t.DEPT_NATURE) dept_Nature_Name,");
		sql.append("   (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='ACCTNATURE' and a.status=0  and a.code=t.OLD_TYPE02) old_Type02_Name,");
		sql.append("   (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='ACCTNATURE' and a.status=0  and a.code=t.TYPE02) Type02_Name,");
		sql.append("   (select a.username from sys_user a where  a.userid=t.create_user) create_user_name,");
		sql.append("   (select a.username from sys_user a where  a.userid=t.update_user) update_user_name,");
		sql.append("   (select a.username from sys_user a where  a.userid=t.modify_user) modify_user_name,");
		sql.append("  (case when t.iszero =0 then '否' when  t.iszero =1 then '是' end ) iszero_name,");
		sql.append("  (case when t.old_iszero =0 then '否' when  t.old_iszero =1 then '是' end ) old_iszero_name,");
		sql.append("  acct_wfid, ");
		sql.append("  acct_wfstatus ");
		
		sql.append("  from fav_appl_account t where  acct_wfstatus is not null ");
		String where ="";
		String type = StringUtil.stringConvert(param.get("type"));//类型 1, "新开立",2, "变更",3, "撤销";
		if(StringUtil.isNotBlank(type)){
			where +=" and type in( "+type+")";
		}else{
			where +=" and type in(1,2,3) ";
		}
		String menuid = StringUtil.stringConvert(param.get("menuid"));
		if ("".equals(menuid)) {
			throw new AppException("查询参数：menuid不能为空！");
		}
		//数据权限
		String bdgagency = fdpf.getConditionFilter(Long.parseLong(menuid), "fav_appl_account", "t");
		if(StringUtil.isNotBlank(bdgagency)){
			sql.append(" and ").append(bdgagency);
		}
		
		String bdgagencycode = StringUtil.stringConvert(param.get("bdgagencycode"));
		if(StringUtil.isNotBlank(bdgagencycode)){
			where +=" and  bdgagencycode in( "+bdgagencycode+")";
		}
		String accountName = StringUtil.stringConvert(param.get("accountName"));
		if(StringUtil.isNotBlank(accountName)){
			//if(type.equals("1")){
				where +=" and  account_Name like '%"+accountName+"%'";
			//}else{
			//	where +=" and  old_account_Name like '%"+accountName+"%'";
			//}
		}
		String starttime = StringUtil.stringConvert(param.get("starttime"));
		if (StringUtil.isNotBlank(starttime)) {
			where +=" and  to_date(modify_time,'yyyy-mm-dd hh24:mi:ss') >= to_date('"+starttime+"','yyyy-mm-dd')";
		}
		String endtime = StringUtil.stringConvert(param.get("endtime"));
		if (StringUtil.isNotBlank(endtime)) {
			where +=" and  to_date(modify_time,'yyyy-mm-dd hh24:mi:ss') < trunc(to_date('"+endtime+"','yyyy-mm-dd')+1)";
		}
		//String menuid = StringUtil.stringConvert(param.get("menuid"));
		String activityId = StringUtil.stringConvert(param.get("activityId"));
		String status = StringUtil.stringConvert(param.get("status"));
		String processedStatus = StringUtil.stringConvert(param.get("processedStatus"));
		//获取工作流状态
		boolean firstNode = param.get("firstNode")==null?false: Boolean.parseBoolean(param.get("firstNode").toString());
		boolean lastNode =   param.get("lastNode")==null?false: Boolean.parseBoolean(param.get("lastNode").toString());
		String wfids = bwfc.findCurrentWfidsForBa(menuid, activityId, status, processedStatus, firstNode, lastNode);
		where += " and "+wfids;
		
		sql.append(where);
		System.err.println("已备案"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public PaginationSupport queryAccount(Map<String, Object> param) throws AppException{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select application_id,");
		sql.append("       bdgagencycode,");
		sql.append("       type,");
		sql.append("       dept_nature,");
		sql.append("       supervisor_dept,");
		sql.append("       dept_address,");
		sql.append("       apply_reason,");
		sql.append("       CHANGETYPE,");
		sql.append("       wfid,");
		sql.append("       wfstatus,");
		sql.append("       wfisback,");
		sql.append("       bdgagencyname,");
		sql.append("       appl_phonenumber,");
		sql.append("       linkman,");
		sql.append("       create_user,");
		sql.append("       create_time,");
		sql.append("       update_user,");
		sql.append("       update_time,");
		sql.append("       isregister,");
		sql.append("       account_id,");
		sql.append("       account_name,");
		sql.append("       account_type,");
		sql.append("       account_number,");
		sql.append("       account_num,");
		sql.append("       bank_code,");
		sql.append("       open_time,");
		sql.append("       account_content,");
		sql.append("       old_account_name,");
		sql.append("       old_bank_code,");
		sql.append("       old_account_number,");
		sql.append("       old_account_content,");
		sql.append("       OLD_LEGAL_PERSON,");
		sql.append("       OLD_IDCARDNO,");
		sql.append("       OLD_FINANCIAL_OFFICER,");
		sql.append("       LEGAL_PERSON,");
		sql.append("       IDCARDNO,");
		sql.append("       FINANCIAL_OFFICER,");
		sql.append("       responsible_person,");
		sql.append("       acct_phonenumber,");
		sql.append("       iszero,");
		sql.append("       remark,");
		sql.append("       type02,");
		sql.append("       old_type02,");
		sql.append("       old_iszero,");
		sql.append("       old_account_type,");
		sql.append("       modify_user,");
		sql.append("       modify_time,");
		sql.append("       ( SELECT A.NAME FROM SYS_FA_DICENUMITEM A WHERE UPPER(A.ELEMENTCODE) = (CASE t.TYPE WHEN 2 THEN 'CHANGETYPE' WHEN 3 THEN 'REVOKETYPE' ELSE '' END ) AND A.STATUS = 0 AND A.CODE = t.CHANGETYPE) CHANGETYPE_NAME,");
		sql.append("       (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='ACCTTYPE' and a.status=0  and a.code=t.OLD_ACCOUNT_TYPE) OLD_ACCOUNT_TYPE_NAME,");
		sql.append("       (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='ACCTTYPE' and a.status=0  and a.code=t.ACCOUNT_TYPE) ACCOUNT_TYPE_NAME,");
		sql.append("       (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='REGISTERTYPE' and a.status=0  and a.code=t.TYPE) TYPE_NAME,");
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
		sql.append("   (select a.username from sys_user a where  a.userid=t.modify_user) modify_user_name,");
		sql.append("  (case when t.iszero =0 then '否' when  t.iszero =1 then '是' end ) iszero_name,");
		sql.append("  (case when t.old_iszero =0 then '否' when  t.old_iszero =1 then '是' end ) old_iszero_name");
		sql.append("  from fav_appl_account t where   isregister is null  ");
		String where ="";
		String type = StringUtil.stringConvert(param.get("type"));//类型 1, "新开立",2, "变更",3, "撤销";
		if(StringUtil.isNotBlank(type)){
			if(type.indexOf("3")>0){//包含已撤销的
				where +=" and ((type =3 and wfstatus ='20') or (  type in("+type+") and  wfstatus ='10'))";
			}else {
				where +=" and type in("+type+") and  wfstatus ='10' ";
			}
		}else{
			where +=" and ((type =3 and wfstatus ='20') or (  type in(1,2) and  wfstatus ='10'))";
		}
		String menuid = StringUtil.stringConvert(param.get("menuid"));
		if ("".equals(menuid)) {
			throw new AppException("查询参数：menuid不能为空！");
		}
		//数据权限
		String bdgagency = fdpf.getConditionFilter(Long.parseLong(menuid), "fav_appl_account", "t");
		if(StringUtil.isNotBlank(bdgagency)){
			sql.append(" and ").append(bdgagency);
		}
		
		String bdgagencycode = StringUtil.stringConvert(param.get("bdgagencycode"));
		if(StringUtil.isNotBlank(bdgagencycode)){
			where +=" and  bdgagencycode in( "+bdgagencycode+")";
		}
		String accountType = StringUtil.stringConvert(param.get("accountType"));
		if(StringUtil.isNotBlank(accountType)){
			where +=" and  old_account_Type in( "+accountType+")";
		}
		String accountName = StringUtil.stringConvert(param.get("accountName"));
		if(StringUtil.isNotBlank(accountName)){
			where +=" and  old_account_Name like '%"+accountName+"%'";
		}
		sql.append(where);
		System.err.println("可备案："+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public void saveSubmitRecordInfo(Map<String, Object> map,FaAccounts faAccounts) throws Exception{
		// TODO Auto-generated method stub
		//获取当前用户
		SysUser user = SecureUtil.getCurrentUser();
		
		//获取参数
		String itemids = (String)map.get("itemids");//附件id
		String applicationId = (String)map.get("applicationId");//业务主键
		String accountId = (String)map.get("accountId");//账户信息主键
		String isSub = (String)map.get("isSub");//保存或保存送审标识
		String menuid = (String)map.get("menuid");//保存或保存送审标识
		String optType = (String)map.get("optType");//操作类型：新增、修改、详情
		String acctWfstatus = (String)map.get("acctWfstatus");//工作流状态
		String acctWfid = (String)map.get("acctWfid");//工作流ID
		String accountNumber = (String)map.get("accountNumber");//银行账号
		String openTime = (String)map.get("openTime");//开户日期
		String remark = (String)map.get("remark");//备注
		String activityId = (String)map.get("activityId");
		// 附件(新增账户信息附件)
		if (itemids != null && !"".equals(itemids)) {
			List<Long> l = new ArrayList<Long>();
			String[] it = itemids.split(",");
			for (int i = 0; i < it.length; i++) {
				l.add(Long.valueOf(it[i]));
			}
			fileManageComponent.updateKeyid(accountId, l);
		}
		
		//修改Fa_accounts表
		faAccounts = faAccountsDao.get(Integer.parseInt(accountId));
		faAccounts.setAccountNumber(accountNumber);
		faAccounts.setOpenTime(openTime);
		faAccounts.setRemark(remark);
		faAccounts.setModifyUser((user.getUserid().toString()));
		faAccounts.setModifyTime(DateUtil
				.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		if(!"edit".equals(optType)){
			
			if(isSub.equals("0")){//保存或保存送审
				faAccounts.setWfstatus("00");
			}else{
				faAccounts.setWfstatus("01");
			}
		}else{
			faAccounts.setWfid(acctWfid);
			faAccounts.setWfstatus(acctWfstatus);
		}
		faAccountsDao.update(faAccounts);
		
		//新增还是修改（新增的话要修改字段）
		FaApplications fa= applicationsDao.get(Integer.parseInt(applicationId));
		if(optType.equals("add")){
			//更新是否备案字段
			fa.setIsregister("1");
			applicationsDao.update(fa);
		}
		String msg ="";
		//工作流
		if (isSub.equals("1")) {// 保存并送审
			// 触发工作流
			if(StringUtil.isNotBlank(faAccounts.getWfid())){
				msg =bwfc.completeTask(applicationId, new HashMap(), "", "", "", activityId, "1");
			}else{
				msg = bwfc.sendWorkFlow(menuid,applicationId,new HashMap(),"1");
				
			}
		}
		if(!"".equals(msg)){
			throw new AppException(msg);
		}
	}
	@Override
	public String removeRecordInfo(Map<String, Object> map) throws Exception{
		// 工作流撤回
		String applicationIds = (String)map.get("applicationIds");
		String activityId = (String)map.get("activityId");
		
		String msg = bwfc.revokeWorkFlow(applicationIds, new HashMap(), "",activityId,"1");
		if(!"".equals(msg)){//撤回失败
			throw new Exception(msg);
		}
		return msg;
	}
	@Override
	public String submitRecordInfo(Map<String, Object> map) throws Exception {
		String applicationIds = (String)map.get("applicationIds");
		String menuid = (String)map.get("menuid");
		String activityId = (String)map.get("activityId");
		String msg ="";
		StringBuffer returnSql = new StringBuffer();
		String[] applicationIdArray =applicationIds.split(",");
		for (int i = 0; i < applicationIdArray.length; i++) {
			FavApplAccount dbApplication = favApplAccountDao.get(Integer.parseInt(applicationIdArray[i]));
			String wfid = StringUtil.stringConvert(dbApplication.getAcctWfid());
			if("".equals(wfid)){//为发起工作流
				msg = bwfc.sendWorkFlow(menuid,applicationIdArray[i],new HashMap(),"1");
			}else {//已发起工作流
				msg = bwfc.completeTask(applicationIdArray[i], new HashMap(), "", "", "", activityId, "1");
			}
			if (!"".equals(msg)) {
				returnSql.append(msg);
			}
		}
		if (!"".equals(returnSql.toString())) {
			throw new AppException(returnSql.toString());
		}
		return null;
	}
	
	@Override
	public boolean getTaskFormEditable(String wfid, String activityId) throws Exception {
		// TODO Auto-generated method stub
		return bwfc.getTaskFormEditable(wfid, activityId);
	}
	
	@Override
	public Set<String> getOutComes(String wfid, String activityId,String outcomeType) throws Exception {
		// TODO Auto-generated method stub
		return bwfc.getOutCome(wfid, activityId, outcomeType);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String verifyWorkFlow(Map<String, Object> param,FaApplications faApplication) throws AppException,Exception {
		String returnString = "";
		
		String itemids = param.get("itemids") == null ? "" : (String)param.get("itemids");
		// 获取当前用户
		SysUser user = SecureUtil.getCurrentUser();
		String acctWfid = param.get("acctWfid")==null?"":(String)param.get("acctWfid");
		String activityId = StringUtil.stringConvert(param.get("activityId"));
		String accountId = StringUtil.stringConvert(param.get("accountId"));
		String remark = StringUtil.stringConvert(param.get("remark"));
		
		boolean formIsEdit = getTaskFormEditable(acctWfid,activityId);
		
		if(formIsEdit){
			FaAccounts fac = faAccountsDao.get(Integer.parseInt(accountId));
			fac.setModifyUser(user.getUserid().toString());
			fac.setModifyTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			
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
		
		String applicationId = StringUtil.stringConvert(param
				.get("applicationId"));
		returnString = bwfc.completeTask(applicationId, param, "1",
				opinion, isback, activityId,"1");
		if (!"".equals(returnString)) {
			throw new AppException(returnString);
		}
		
		return returnString;
	}
	@Override
	public void messageService(Integer applicationId, String phonenumber,String message,String activityId) throws Exception{
		// TODO Auto-generated method stub
		
		messageComponent.saveMessage(applicationId, phonenumber, message,activityId);
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

	
}
