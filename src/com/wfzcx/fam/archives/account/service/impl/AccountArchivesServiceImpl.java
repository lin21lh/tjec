package com.wfzcx.fam.archives.account.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.base.filemanage.component.FileManageComponent;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;
import com.wfzcx.fam.archives.account.service.AccountArchivesServiceI;
import com.wfzcx.fam.dataPermission.FamDataPermissionFilter;
import com.wfzcx.fam.manage.dao.FaAccountArchiveDao;

@Scope("prototype")
@Service("com.wfzcx.fam.archives.account.service.impl.AccountArchivesServiceImpl")
public class AccountArchivesServiceImpl implements AccountArchivesServiceI {

	@Autowired
	FaAccountArchiveDao accountArchiveDao;
	
	@Autowired
	FamDataPermissionFilter fdpf;
	
	@Autowired
	MapDataDaoI mapDataDao;
	
	@Autowired
	FileManageComponent fileManageComponent;
	
	@Override
	public PaginationSupport queryAccountArchives(Map<String, Object> param) throws AppException{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select t.*,");
		sql.append("(select WM_CONCAT(f.application_id) from fa_account_his f where f.olditemid = t.itemid ) appids,");
		sql.append("(select WM_CONCAT(h.account_id) from fa_account_his f,fa_accounts h where f.olditemid = t.itemid and f.application_id = h.application_id) acctids,");
		sql.append("       ( case status when 1 then '正常'when 9 then '撤销' end  )status_Name,");
		sql.append("        (case iszero when 1 then '是'when 0 then '否' end )  iszero_Name,");
		sql.append("       bank_code ||'-'|| bank_name Bank_Name_Cn,");
		sql.append("       (select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='ACCTNATURE' and t.status=0  and t.code=type02) type02_Name,");
		sql.append("       (select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='ACCTTYPE' and t.status=0  and t.code=ACCOUNT_TYPE)account_Type_Name,");
		sql.append("       bdgagencycode ||'-'|| bdgagencyname bdgagencycn,");
		sql.append("       (select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='AGENCYTYPE' and t.status=0  and t.code=DEPT_NATURE) dept_Nature_Name,");
		sql.append("       (select a.username from sys_user a where  a.userid=t.create_user) create_user_name,");
		sql.append("       (select a.username from sys_user a where  a.userid=t.update_user) update_user_name");
		sql.append("  from fa_account_archive t where status=1 ");
		//页面选择预算单位
		String bdgagencycode = (String) param.get("bdgagencycode");
		if(StringUtil.isNotBlank(bdgagencycode)){
			sql.append(" and (t.bdgagencycode = '").append(bdgagencycode).append("'");
			//sql.append(" or t.bdgagencyname like '%").append(bdgagencycode).append("%')");
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
	public void saveFileInfo(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		String itemids =  param.get("itemids") == null?"":(String)param.get("itemids");
		String itemid =  param.get("itemid") == null?"":(String)param.get("itemid");
		if("".equals(itemid)){
			throw new Exception("主业务信息主键为空！");
		}
		if (!"".equals(itemids)) {
			List<Long> l = new ArrayList<Long>();
			String[] it = itemids.split(",");
			for (int i = 0; i < it.length; i++) {
				l.add(Long.valueOf(it[i]));
			}
			fileManageComponent.updateKeyid(itemid, l);
		}
	}

}
