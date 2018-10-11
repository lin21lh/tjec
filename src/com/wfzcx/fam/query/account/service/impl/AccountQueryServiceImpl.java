/**  
 * @Title: queryAccountServiceImpl.java  
 * @Package com.wfzcx.fam.query.account.service.impl  
 * @Description: TODO(用一句话描述该文件做什么)  
 * @author LiuJunBo  
 * @date 2015-4-22 下午03:33:57  
 * @version V1.0  
 */ 
 
 
package com.wfzcx.fam.query.account.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;
import com.jbf.workflow.vo.HistoryOpinionVO;
import com.wfzcx.fam.dataPermission.FamDataPermissionFilter;
import com.wfzcx.fam.manage.dao.FaAccountArchiveDao;
import com.wfzcx.fam.manage.dao.FaAccountHisDao;
import com.wfzcx.fam.manage.dao.FaApplicationsDao;
import com.wfzcx.fam.manage.po.FaApplications;
import com.wfzcx.fam.query.account.service.AccountQueryService;
import com.wfzcx.fam.workflow.BussinessWorkFlowComponent;

/** 
 * 
 * @ClassName: queryAccountServiceImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author LiuJunBo
 * @date 2015-4-22 下午03:35:47
 */
@Scope("prototype")
@Service("com.wfzcx.fam.query.account.service.impl.AccountRevokeServiceImpl")
public class AccountQueryServiceImpl implements AccountQueryService{
	
	@Autowired
	FaAccountArchiveDao faAccountArchiveDao;
	
	@Autowired
	FaAccountHisDao faAccountHisDao;
	
	@Autowired
	FaApplicationsDao faApplicationsDao;
	
	@Autowired
	MapDataDaoI mapDataDao;
	
	@Autowired
	FamDataPermissionFilter fdpf;
	@Autowired
	BussinessWorkFlowComponent bwfc;
	
	@Override
	public PaginationSupport queryAccountArchive(Map<String, Object> param) throws  Exception {
		// TODO Auto-generated method stub
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select itemid,");
		sql.append("       elementcode,");
		sql.append("       startdate,");
		sql.append("       enddate,");
		sql.append("       account_name,");
		sql.append("       account_number,");
		sql.append("       bankid,");
		sql.append("       bank_code,");
		sql.append("       bank_name,");
		sql.append("       account_type,");
		sql.append("       type,");
		sql.append("       type01,");
		sql.append("       type02,");
		sql.append("       status,");
		sql.append("       bdgagency,");
		sql.append("       bdgagencycode,");
		sql.append("       bdgagencyname,");
		sql.append("       remark,");
		sql.append("       create_time,");
		sql.append("       create_user,");
		sql.append("       update_time,");
		sql.append("       update_user,");
		sql.append("       ischange,");
		sql.append("       application_id,");
		sql.append("       dept_address,");
		sql.append("       phonenumber,");
		sql.append("       linkman,");
		sql.append("       dept_nature,");
		sql.append("       iszero,");
		sql.append("       ( case status when 1 then '正常'when 9 then '撤销' end  )status_Name,");
		sql.append("        (case iszero when 1 then '是'when 0 then '否' end )  iszero_Name,");
		sql.append("       bank_code ||'-'|| bank_name Bank_Name_Cn,");
		sql.append("       (select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='ACCTNATURE' and t.status=0  and t.code=type02) type02_Name,");
		sql.append("       (select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='ACCTTYPE' and t.status=0  and t.code=ACCOUNT_TYPE)account_Type_Name,");
		sql.append("       bdgagencycode ||'-'|| bdgagencyname bdgagencycn,");
		sql.append("       (select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='AGENCYTYPE' and t.status=0  and t.code=DEPT_NATURE) dept_Nature_Name,");
		sql.append("       (select a.username from sys_user a where  a.userid=t.create_user) create_user_name,");
		sql.append("       (select a.username from sys_user a where  a.userid=t.update_user) update_user_name");
		sql.append("  from fa_account_archive t where 1=1 ");
		
		//添加预算单位code
		String bdgagencycode = (String) param.get("bdgagencycode");
		if(StringUtil.isNotBlank(bdgagencycode)){
			sql.append(" and (t.bdgagencycode like '%").append(bdgagencycode).append("%'");
			sql.append(" or t.bdgagencyname like '%").append(bdgagencycode).append("%')");
		}
		//添加开户行
		String bankCode = (String) param.get("bankCode");
		if(StringUtil.isNotBlank(bankCode)){
			sql.append(" and t.bank_Code ='").append(bankCode).append("' ");
		}
		//添加账户名称
		String accountName = (String) param.get("accountName");
		if(StringUtil.isNotBlank(accountName)){
			sql.append(" and t.account_Name like '%").append(accountName).append("%'");
		}
		//添加银行账号
		String accountNumber = (String) param.get("accountNumber");
		if(StringUtil.isNotBlank(accountNumber)){
			sql.append(" and t.account_Number like '%").append(accountNumber).append("%'");
		}
		//账户状态
		String status = (String) param.get("status");
		if(StringUtil.isNotBlank(status)){
			sql.append(" and t.status = '").append(status).append("'");
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
		sql.append(" order by bdgagencycode,account_type,account_name,create_time desc ");
		System.err.println("【账户查询】"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	@Override
	public PaginationSupport queryAccountHis(Map<String, Object> param) {
		// TODO Auto-generated method stub
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select itemid,");
		sql.append("       elementcode,");
		sql.append("       startdate,");
		sql.append("       enddate,");
		sql.append("       account_name,");
		sql.append("       account_number,");
		sql.append("       bankid,");
		sql.append("       bank_code,");
		sql.append("       bank_name,");
		sql.append("       account_type,");
		sql.append("       type,");
		sql.append("       type01,");
		sql.append("       type02,");
		sql.append("       status,");
		sql.append("       bdgagency,");
		sql.append("       bdgagencycode,");
		sql.append("       bdgagencyname,");
		sql.append("       remark,");
		sql.append("       create_time,");
		sql.append("       create_user,");
		sql.append("       update_time,");
		sql.append("       update_user,");
		sql.append("       ischange,");
		sql.append("       application_id,");
		sql.append("       dept_address,");
		sql.append("       phonenumber,");
		sql.append("       linkman,");
		sql.append("       dept_nature,");
		sql.append("       iszero,");
		sql.append("       ( case status when 1 then '正常'when 9 then '撤销' end  )status_Name,");
		sql.append("        (case iszero when 1 then '是'when 0 then '否' end )  iszero_Name,");
		sql.append("       bank_code ||'-'|| bank_name Bank_Name_Cn,");
		sql.append("       (select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='ACCTNATURE' and t.status=0  and t.code=type02) type02_Name,");
		sql.append("       (select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='ACCTTYPE' and t.status=0  and t.code=ACCOUNT_TYPE)account_Type_Name,");
		sql.append("       bdgagencycode ||'-'|| bdgagencyname bdgagencycn,");
		sql.append("       (select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='AGENCYTYPE' and t.status=0  and t.code=DEPT_NATURE) dept_Nature_Name,");
		sql.append("       (select a.username from sys_user a where  a.userid=t.create_user) create_user_name,");
		sql.append("       (select a.username from sys_user a where  a.userid=t.update_user) update_user_name");
		sql.append("  from fa_account_his t where 1=1 ");
		//添加itemid
		String itemid = StringUtil.stringConvert(param.get("itemid"));
		if (!"".equals(itemid)) {
			sql.append(" AND  olditemid =").append(itemid);
		}
		String bankCode = StringUtil.stringConvert(param.get("bankCode"));
		if (!"".equals(bankCode)) {
			sql.append(" and bank_Code ='").append(bankCode).append("' ");
		}
		String accountName = StringUtil.stringConvert(param.get("accountName"));
		if (!"".equals(accountName)) {
			sql.append(" and account_Name like '%").append(accountName).append("%' ");
		}
		String accountNumber = StringUtil.stringConvert(param.get("accountNumber"));
		if (!"".equals(accountNumber)) {
			sql.append(" and account_Number like '%").append(accountNumber).append("%' ");
		}
		sql.append(" order by bdgagencycode,account_type,account_name,update_time asc ");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	@Override
	public List queryApplicationInfo(String applicationId) {
		// TODO Auto-generated method stub
		FaApplications faApplications = faApplicationsDao.get(Integer.parseInt(applicationId));
			StringBuffer sql = new StringBuffer();
			sql.append("select t.*,");
			sql.append("           (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='REGISTERTYPE' and a.status=0  and a.code=t.type) TYPE_NAME,");
			sql.append("           (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='ACCTTYPE' and a.status=0  and a.code=t.OLD_ACCOUNT_TYPE) OLD_ACCOUNT_TYPE_NAME,");
			sql.append("           (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='ACCTTYPE' and a.status=0  and a.code=t.ACCOUNT_TYPE) ACCOUNT_TYPE_NAME,");
			sql.append("          bdgagencycode ||'-'|| bdgagencyname bdgagencycn,");
			sql.append("          (select a.NAME from fav_bank a where a.CODE=t.old_bank_code) OLD_BANK_NAME,");
			sql.append("          (select a.CODE||'-'||a.NAME from fav_bank a where a.CODE=t.old_bank_code) old_Bank_Name_Cn,");
			sql.append("          (select a.NAME from fav_bank a where a.CODE=t.bank_code) BANK_NAME,");
			sql.append("          (select a.CODE||'-'||a.NAME from fav_bank a where a.CODE=t.bank_code) Bank_Name_Cn,");
			sql.append("          (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='WFSTATUS' and a.status=0  and a.code=t.WFSTATUS) wfstatus_Name,");
			sql.append("          (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='AGENCYTYPE' and a.status=0  and a.code=t.DEPT_NATURE) dept_Nature_Name,");
			sql.append("       (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='ACCTNATURE' and a.status=0  and a.code=t.OLD_TYPE02) old_Type02_Name,");
			sql.append("       (select a.name from SYS_FA_DICENUMITEM a where upper(a.elementcode)='ACCTNATURE' and a.status=0  and a.code=t.TYPE02) Type02_Name,");
			sql.append("       (select a.username from sys_user a where  a.userid=t.create_user) create_user_name,");
			sql.append("       (select a.username from sys_user a where  a.userid=t.update_user) update_user_name,");
			sql.append("      (case when t.iszero =0 then '否' when  t.iszero =1 then '是' end ) iszero_name,");
			sql.append("      (case when t.old_iszero =0 then '否' when  t.old_iszero =1 then '是' end ) old_iszero_name");
			sql.append("      from fav_appl_account t where 1=1 ");
			if(StringUtil.isNotBlank(applicationId)){
				sql.append(" and t.application_id =").append(applicationId);
			}
		return mapDataDao.queryListBySQLForConvert(sql.toString());
	}

	@Override
	public List<HistoryOpinionVO> getworkFlowList(String wfid) {
		// TODO Auto-generated method stub
		return bwfc.getworkFlowList(wfid);
	}

	@Override
	public FaApplications getFaApplication(String applicationId) {
		FaApplications faApplications = faApplicationsDao.get(Integer.parseInt(applicationId));
		return faApplications;
	}

}
