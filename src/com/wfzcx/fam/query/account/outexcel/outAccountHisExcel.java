/**  
 * @Title: outAccountHisExcel.java  
 * @Package com.wfzcx.fam.query.account.outexcel  
 * @Description: TODO(用一句话描述该文件做什么)  
 * @author LiuJunBo  
 * @date 2015-4-24 上午08:35:04  
 * @version V1.0  
 */ 
 
 
package com.wfzcx.fam.query.account.outexcel;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbf.base.excel.outexcel.exceute.ExcelData;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.util.StringUtil;
import com.wfzcx.fam.manage.dao.FaAccountHisDao;

/** 
 * @ClassName: outAccountHisExcel 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author LiuJunBo
 * @date 2015-4-24 上午08:35:04  
 */
@Scope("prototype")
@Component("com.wfzcx.fam.query.account.outexcel.outAccountHisExcel")
public class outAccountHisExcel implements ExcelData {

	@Autowired
	FaAccountHisDao accountHisDao;
	@Autowired
	MapDataDaoI mapDataDao;
	
	@Override
	public List getExcelData(Map param) {
		// TODO Auto-generated method stub
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
		sql.append(" order by bdgagencycode,account_type,account_name,create_time desc ");
		return mapDataDao.queryListBySQLForConvert(sql.toString());
	}

}
