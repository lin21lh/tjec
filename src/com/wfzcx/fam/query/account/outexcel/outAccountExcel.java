/**  
 * @Title: outAccountExcel.java  
 * @Package com.wfzcx.fam.query.account.outexcel  
 * @Description: TODO(用一句话描述该文件做什么)  
 * @author LiuJunBo  
 * @date 2015-4-23 上午10:35:10  
 * @version V1.0  
 */ 
 
 
package com.wfzcx.fam.query.account.outexcel;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbf.base.excel.outexcel.exceute.ExcelData;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;
import com.wfzcx.fam.dataPermission.FamDataPermissionFilter;
import com.wfzcx.fam.manage.dao.FaAccountArchiveDao;
import com.wfzcx.fam.manage.po.FaAccountArchive;

/** 
 * @ClassName: outAccountExcel 
 * @Description: TODO(账户导出excel) 
 * @author LiuJunBo
 * @date 2015-4-23 上午10:35:10  
 */
@Scope("prototype")
@Component("com.wfzcx.fam.query.account.outexcel.outAccountExcel")
public class outAccountExcel implements ExcelData {

	/* (非 Javadoc) 
	 * <p>Title:getExcelData</p> 
	 * <p>Description: </p> 
	 * @param paramMap
	 * @return 
	 * @see com.jbf.base.excel.outexcel.exceute.ExcelData#getExcelData(java.util.Map) 
	 */
	@Autowired
	FaAccountArchiveDao faAccountArchiveDao;
	
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	FamDataPermissionFilter fdpf;
	
	@Override
	public List getExcelData(Map param) {
		StringBuffer sql = new StringBuffer();
		sql.append("select　rownum as xh,a.* from(select itemid,");
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
			sql.append(" and t.bank_Code like '%").append(bdgagencycode).append("%'");
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
			try {
				throw new AppException("查询参数：menuid不能为空！");
			} catch (AppException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//数据权限
		String bdgagency ="";
		try {
			bdgagency = fdpf.getConditionFilter(Long.parseLong(menuid), "fa_account_archive", "t");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtil.isNotBlank(bdgagency)){
			sql.append(" and ").append(bdgagency);
		}
		sql.append(" order by bdgagencycode,account_type,account_name,create_time desc ) a ");
		System.err.println(sql.toString());
		return mapDataDao.queryListBySQL(sql.toString());
	}

}
