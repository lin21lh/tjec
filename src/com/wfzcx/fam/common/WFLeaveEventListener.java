package com.wfzcx.fam.common;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.listener.WfEventListener;
import com.jbf.workflow.vo.EventSourceVO;
import com.wfzcx.fam.manage.dao.FaAccountArchiveDao;
import com.wfzcx.fam.manage.dao.FaAccountHisDao;
import com.wfzcx.fam.manage.po.FaAccountArchive;
import com.wfzcx.fam.manage.po.FaAccountHis;

/**
 * 工作流节点回调函数
 * @ClassName: WFLeaveEventListener 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年4月29日 上午10:54:10
 */
@Component("com.wfzcx.fam.common.WFLeaveEventListener")
public class WFLeaveEventListener {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	FaAccountArchiveDao faAccountArchiveDao;
	@Autowired
	FaAccountHisDao faAccountHisDao;
	/**
	 * 节点完成后的处理（更改退回和撤回的状态）
	 * @Title: nodeProcess 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid 工作流id
	 * @param pamam 节点设置参数
	 * @param variables 工作流韩靖变量
	 * @param eventSource 事件的源节点或源迁称路径的信息
	 * @throws Exception 设定文件
	 */
	public void nodeProcess(String wfid, Map pamam, Map variables,EventSourceVO eventSource) throws Exception {
		String backFlag = StringUtil.stringConvert(variables.get(WfEventListener.WF_BACK_FLAG));
		if("".equals(backFlag)){
			System.err.println("工作流回调WFLeaveEventListener--nodeProcess---backFlag标志为空，工作流id为"+wfid);
		}else if ("NORMAL".equals(backFlag)) {//正常 不做处理
			String sql ="update fa_applications set  wfisback ='' where wfid='"+wfid+"'";
			mapDataDao.updateTX(sql);
		}else if ("RETURN".equals(backFlag)) {//退回
			String sql ="update fa_applications set wfstatus='40' , wfisback ='1' where wfid='"+wfid+"'";
			mapDataDao.updateTX(sql);
		}else if ("WITHDRAW".equals(backFlag)) {//撤回
			String sql ="update fa_applications set wfstatus='99' , wfisback ='2' where wfid='"+wfid+"'";
			mapDataDao.updateTX(sql);
		}
	}
	
	/**
	 * 备案节点完成后的处理（更改退回和撤回的状态）
	 * @Title: nodeProcessForBa 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid 工作流id
	 * @param pamam 节点设置参数
	 * @param variables 工作流韩靖变量
	 * @param eventSource 事件的源节点或源迁称路径的信息
	 * @throws Exception 设定文件
	 */
	public void nodeProcessForBa(String wfid, Map pamam, Map variables,EventSourceVO eventSource) throws Exception {
		String backFlag = StringUtil.stringConvert(variables.get(WfEventListener.WF_BACK_FLAG));
		if("".equals(backFlag)){
			System.err.println("工作流回调WFLeaveEventListener--nodeProcess---backFlag标志为空，工作流id为"+wfid);
		}else if ("NORMAL".equals(backFlag)) {//正常 不做处理
			String sql ="update fa_accounts set  wfisback ='' where wfid='"+wfid+"'";
			mapDataDao.updateTX(sql);
		}else if ("RETURN".equals(backFlag)) {//退回
			String sql ="update fa_accounts set wfstatus='40' , wfisback ='1' where wfid='"+wfid+"'";
			mapDataDao.updateTX(sql);
		}else if ("WITHDRAW".equals(backFlag)) {//撤回
			String sql ="update fa_accounts set wfstatus='99' , wfisback ='2' where wfid='"+wfid+"'";
			mapDataDao.updateTX(sql);
		}
	}
	/**
	 * 下达后的处理 账户开立、变更、撤销(该方案已不再使用2015年6月4日10:13:18)
	 * @Title: auditManage 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @param pamam
	 * @param variables
	 * @param eventSource
	 * @throws Exception 设定文件
	 * @2015年5月9日15:10:38
	 */
	public void auditManage(String wfid, Map pamam, Map variables,EventSourceVO eventSource) throws Exception {
		/**
		 * 账户开立、变更、撤销后的下达处理
		 * 账户开立、变更下达后，需fa_accounts中新账户信息是否填写，如果没有填写，则不需处理，待备案完成再将相应信息写入档案表
		 * 账户撤销完成后，需要更改档案表对应的状态。
		 */
		System.err.println("wfid ==="+wfid);
		if("".equals(wfid) || wfid ==null){
			System.err.println("工作流回调下达后的处理--auditManage--，工作流id为"+wfid);
			return;
		}
		String sqlString = "select t.*,(select a.NAME from fav_bank a where t.bank_code=a.CODE) bank_name from fav_appl_account t where t.wfid='"+wfid+"'";
		List list  = mapDataDao.queryListBySQL(sqlString);
		if(!list.isEmpty()){
			Map map = (Map) list.get(0);
			String application_id = StringUtil.stringConvert(map.get("application_id"));
			String type = StringUtil.stringConvert(map.get("type"));
			if ("".equals(application_id)) {
				throw new Exception("wfid为"+wfid+"的未找到相应的application_id！");
			}
			String accountName = StringUtil.stringConvert(map.get("account_name"));
			String accountNumber = StringUtil.stringConvert(map.get("account_number"));
			String bankCode = StringUtil.stringConvert(map.get("bank_code"));
			String accountType = StringUtil.stringConvert(map.get("account_type"));
			String bdgagencycode = StringUtil.stringConvert(map.get("bdgagencycode"));
			//如果有一个为空，则表示没有备案
			boolean isregister = StringUtil.isNotBlank(accountName) && StringUtil.isNotBlank(accountNumber)&&StringUtil.isNotBlank(bankCode)
					&&StringUtil.isNotBlank(accountType)&&StringUtil.isNotBlank(bdgagencycode);
			if (!isregister) {
				if ("1".equals(type)||"2".equals(type)) {
					System.err.println("application_id="+application_id+"，没有账户信息，不需要备案！");
				}
			}
			if ("1".equals(type)) {//账户开立 
				/**
				 * 1.写入档案表
					ACCOUNT_NAME
					ACCOUNT_NUMBER
					BANK_CODE
					ACCOUNT_TYPE
					BDGAGENCYCODE
				 */
				if (isregister) {
					insertArchive(map);
					String sqlString2 ="update fa_applications set isregister=1 where application_id ='"+application_id+"'";
					mapDataDao.updateTX(sqlString2);
				}
			}else if ("2".equals(type)) {//或账户变更
				if (isregister) {
					String idSql = "select itemid from fa_account_archive t where t.application_id='"+application_id+"'";
					List idList = mapDataDao.queryListBySQL(idSql);
					if(idList.isEmpty()){
						throw new Exception("未找到application_id为"+application_id+"的档案记录！");
					}
					Map idMap = (Map) idList.get(0);
					String itemid = StringUtil.stringConvert(idMap.get("itemid"));
					insertArchiveHis(itemid);
					updateArchive(itemid, map);
					String sqlString2 ="update fa_applications set isregister=1 where application_id ='"+application_id+"'";
					mapDataDao.updateTX(sqlString2);
				}
			 /**
			  * 1、将档案表的记录写入历史
			  * 2、将账户信息写入、更改档案表状态
			  */
			}else if ("3".equals(type)){//账户撤销
				String idSql = "select itemid from fa_account_archive t where t.application_id='"+application_id+"'";
				List idList = mapDataDao.queryListBySQL(idSql);
				if(idList.isEmpty()){
					throw new Exception("未找到application_id为"+application_id+"的档案记录！");
				}
				Map idMap = (Map) idList.get(0);
				String itemid = StringUtil.stringConvert(idMap.get("itemid"));
				/**
				 * 1、将档案表的记录写入历史
				 * 2、将账户信息写入、更改档案表状态
				 */
				insertArchiveHis(itemid);
				updateArchiveForDelete(itemid, map);
			}
		}
	}
	/**
	 * 账户备案处理
	 * @Title: auditManageForBa 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @param pamam
	 * @param variables
	 * @param eventSource
	 * @throws Exception 设定文件
	 */
	public void auditManageForBa(String wfid, Map pamam, Map variables,EventSourceVO eventSource) throws Exception {
		/**
		 * 账户备案处理
		 * 账户备案处理，需fa_accounts中新账户信息是否填写，如果没有填写，则不需处理，待备案完成再将相应信息写入档案表
		 * 账户撤销完成后，需要更改档案表对应的状态。
		 */
		System.err.println("wfid ==="+wfid);
		if("".equals(wfid) || wfid ==null){
			System.err.println("工作流回调下达后的处理--auditManage--，工作流id为"+wfid);
			return;
		}
		String sqlString = "select t.*,(select a.NAME from fav_bank a where t.bank_code=a.CODE) bank_name from fav_appl_account t where t.acct_wfid='"+wfid+"'";
		List list  = mapDataDao.queryListBySQL(sqlString);
		if(!list.isEmpty()){
			Map map = (Map) list.get(0);
			String application_id = StringUtil.stringConvert(map.get("application_id"));
			String type = StringUtil.stringConvert(map.get("type"));
			if ("".equals(application_id)) {
				throw new Exception("wfid为"+wfid+"的未找到相应的application_id！");
			}
			if ("1".equals(type)) {//账户开立 
				/**
				 * 1.写入档案表
				 * 2.写入历史表
				 * 3.更新fa_applications的标志
				 */
				Integer itemid = insertArchive(map);
				insertArchiveHis(map,itemid);
				String sqlString2 ="update fa_applications set isregister=1 where application_id ='"+application_id+"'";
				mapDataDao.updateTX(sqlString2);
			}else if ("2".equals(type)) {//或账户变更
				String idSql = "select itemid from fa_account_archive t where t.application_id='"+application_id+"'";
				List idList = mapDataDao.queryListBySQL(idSql);
				if(idList.isEmpty()){
					throw new Exception("未找到application_id为"+application_id+"的档案记录！");
				}
				Map idMap = (Map) idList.get(0);
				String itemid = StringUtil.stringConvert(idMap.get("itemid"));
				insertArchiveHis(itemid);
				updateArchive(itemid, map);
				String sqlString2 ="update fa_applications set isregister=1 where application_id ='"+application_id+"'";
				mapDataDao.updateTX(sqlString2);
			 /**
			  * 1、将档案表的记录写入历史
			  * 2、将账户信息写入、更改档案表状态
			  */
			}else if ("3".equals(type)){//账户撤销
				String idSql = "select itemid from fa_account_archive t where t.application_id='"+application_id+"'";
				List idList = mapDataDao.queryListBySQL(idSql);
				if(idList.isEmpty()){
					throw new Exception("未找到application_id为"+application_id+"的档案记录！");
				}
				Map idMap = (Map) idList.get(0);
				String itemid = StringUtil.stringConvert(idMap.get("itemid"));
				/**
				 * 1、将档案表的记录写入历史
				 * 2、将账户信息写入、更改档案表状态
				 */
				insertArchiveHis(itemid);
				updateArchiveForDelete(itemid, map);
			}
		}
	}
	/**
	 * 档案表备份
	 * @Title: insertArchiveHis 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param itemid 档案表主键
	 */
	public void insertArchiveHis(String itemid){
		FaAccountArchive faa = faAccountArchiveDao.get(Integer.parseInt(itemid));
		FaAccountHis fah = new FaAccountHis();
		try {
			BeanUtils.copyProperties(fah, faa);
			fah.setItemid(null);
			fah.setOlditemid(String.valueOf(faa.getItemid()));
			faAccountHisDao.save(fah);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 更新档案表
	 * @Title: updateArchive 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param itemid
	 * @param map 设定文件
	 */
	public void updateArchive(String itemid,Map map) {
		FaAccountArchive faa = faAccountArchiveDao.get(Integer.parseInt(itemid));
		faa.setAccountName(StringUtil.stringConvert(map.get("account_name")));
		faa.setAccountType(StringUtil.stringConvert(map.get("account_type")));
		faa.setAccountNumber(StringUtil.stringConvert(map.get("account_number")));
		faa.setBankCode(StringUtil.stringConvert(map.get("bank_code")));
		faa.setBankName(StringUtil.stringConvert(map.get("bank_name")));
		faa.setType02(StringUtil.stringConvert(map.get("type02")));
		faa.setStatus(1);
		faa.setIschange(0);
		faa.setApplicationId(null);
		faa.setPhonenumber(StringUtil.stringConvert(map.get("appl_phonenumber")));
		faa.setLinkman(StringUtil.stringConvert(map.get("linkman")));
		faa.setDeptNature(StringUtil.stringConvert(map.get("dept_nature")));
		faa.setIszero(Integer.parseInt(StringUtil.stringConvert(map.get("iszero"))));
		faa.setUpdateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		faa.setDeptAddress(StringUtil.stringConvert(map.get("dept_address")));
		faa.setAccountContent(StringUtil.stringConvert(map.get("account_content")));
		faa.setFinancialOfficer(StringUtil.stringConvert(map.get("financial_officer")));
		faa.setLegalPerson(StringUtil.stringConvert(map.get("legal_person")));
		faa.setIdcardno(StringUtil.stringConvert(map.get("idcardno")));
		faa.setUpdateUser(StringUtil.stringConvert(map.get("create_user")));
		
		faAccountArchiveDao.update(faa);
	}
	/**
	 * 更新档案表（账户注销）
	 * @Title: updateArchive 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param itemid
	 * @param map 设定文件
	 */
	public void updateArchiveForDelete(String itemid,Map map) {
		FaAccountArchive faa = faAccountArchiveDao.get(Integer.parseInt(itemid));
		faa.setDeptNature(StringUtil.stringConvert(map.get("dept_nature")));
		faa.setIschange(0);
		faa.setApplicationId(null);
		faa.setStatus(9);//注销
		faa.setPhonenumber(StringUtil.stringConvert(map.get("appl_phonenumber")));
		faa.setLinkman(StringUtil.stringConvert(map.get("linkman")));
		faa.setDeptAddress(StringUtil.stringConvert(map.get("dept_address")));
		faa.setUpdateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		faa.setUpdateUser(StringUtil.stringConvert(map.get("create_user")));
		
		faAccountArchiveDao.update(faa);
	}
	/**
	 * 写入档案表
	 * @Title: insertArchive 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param itemid 档案表主键
	 */
	public Integer insertArchive(Map map){
		FaAccountArchive faa = new FaAccountArchive();
		Integer itemid =null;
		try {
			SysUser user = SecureUtil.getCurrentUser();
			faa.setItemid(null);
			faa.setElementcode("BANKACCOUNT");
			faa.setStartdate("");
			faa.setEnddate("");
			faa.setAccountName(StringUtil.stringConvert(map.get("account_name")));
			faa.setAccountNumber(StringUtil.stringConvert(map.get("account_number")));
			faa.setBankid(0);
			faa.setBankCode(StringUtil.stringConvert(map.get("bank_code")));
			faa.setBankName(StringUtil.stringConvert(map.get("bank_name")));
			faa.setAccountType(StringUtil.stringConvert(map.get("account_type")));
			faa.setType(Integer.parseInt(StringUtil.stringConvert(map.get("type"))));
			faa.setType01("");
			faa.setType02(StringUtil.stringConvert(map.get("type02")));
			faa.setStatus(1);
			faa.setBdgagency(0);
			faa.setBdgagencycode(StringUtil.stringConvert(map.get("bdgagencycode")));
			faa.setBdgagencyname(StringUtil.stringConvert(map.get("bdgagencyname")));
			faa.setRemark("");
			faa.setCreateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			faa.setCreateUser(StringUtil.stringConvert(map.get("create_user")));
			faa.setUpdateTime("");
			faa.setUpdateUser("");
			faa.setIschange(0);
			faa.setApplicationId(null);
			faa.setDeptAddress(StringUtil.stringConvert(map.get("dept_address")));
			faa.setPhonenumber(StringUtil.stringConvert(map.get("appl_phonenumber")));
			faa.setLinkman(StringUtil.stringConvert(map.get("linkman")));
			faa.setDeptNature(StringUtil.stringConvert(map.get("dept_nature")));
			faa.setIszero(Integer.parseInt(StringUtil.stringConvert(map.get("iszero"))));
			faa.setAccountContent(StringUtil.stringConvert(map.get("account_content")));
			faa.setFinancialOfficer(StringUtil.stringConvert(map.get("financial_officer")));
			faa.setLegalPerson(StringUtil.stringConvert(map.get("legal_person")));
			faa.setIdcardno(StringUtil.stringConvert(map.get("idcardno")));
			itemid = (Integer) faAccountArchiveDao.save(faa);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return itemid;
	}
	/**
	 * 写入档案表
	 * @Title: insertArchive 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param itemid 档案表主键
	 */
	public Integer insertArchiveHis(Map map,Integer oldItemid){
	
		FaAccountHis faa = new FaAccountHis();
		Integer itemid =null;
		try {
			SysUser user = SecureUtil.getCurrentUser();
			faa.setItemid(null);
			faa.setOlditemid(String.valueOf(oldItemid));
			faa.setElementcode("BANKACCOUNT");
			faa.setStartdate("");
			faa.setEnddate("");
			faa.setAccountName(StringUtil.stringConvert(map.get("account_name")));
			faa.setAccountNumber(StringUtil.stringConvert(map.get("account_number")));
			faa.setBankid(0);
			faa.setBankCode(StringUtil.stringConvert(map.get("bank_code")));
			faa.setBankName(StringUtil.stringConvert(map.get("bank_name")));
			faa.setAccountType(StringUtil.stringConvert(map.get("account_type")));
			faa.setType(Integer.parseInt(StringUtil.stringConvert(map.get("type"))));
			faa.setType01("");
			faa.setType02(StringUtil.stringConvert(map.get("type02")));
			faa.setStatus(1);
			faa.setBdgagency(0);
			faa.setBdgagencycode(StringUtil.stringConvert(map.get("bdgagencycode")));
			faa.setBdgagencyname(StringUtil.stringConvert(map.get("bdgagencyname")));
			faa.setRemark("");
			faa.setCreateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			faa.setCreateUser(StringUtil.stringConvert(map.get("create_user")));
			faa.setUpdateTime("");
			faa.setUpdateUser("");
			faa.setIschange(0);
			faa.setApplicationId(Integer.parseInt(StringUtil.stringConvert(map.get("application_id"))));
			faa.setDeptAddress(StringUtil.stringConvert(map.get("dept_address")));
			faa.setPhonenumber(StringUtil.stringConvert(map.get("appl_phonenumber")));
			faa.setLinkman(StringUtil.stringConvert(map.get("linkman")));
			faa.setDeptNature(StringUtil.stringConvert(map.get("dept_nature")));
			faa.setIszero(Integer.parseInt(StringUtil.stringConvert(map.get("iszero"))));
			faa.setAccountContent(StringUtil.stringConvert(map.get("account_content")));
			faa.setFinancialOfficer(StringUtil.stringConvert(map.get("financial_officer")));
			faa.setLegalPerson(StringUtil.stringConvert(map.get("legal_person")));
			faa.setIdcardno(StringUtil.stringConvert(map.get("idcardno")));
			itemid = (Integer) faAccountHisDao.save(faa);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return itemid;
	}
}
