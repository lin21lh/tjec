package com.wfzcx.ppms.execute.service.impl;

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

import com.alibaba.fastjson.JSONObject;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.dao.util.EasyUISumRowUtil;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.component.SysWorkflowManageComponent;
import com.wfzcx.ppms.execute.dao.ProFinanceDaoI;
import com.wfzcx.ppms.execute.dao.ProImplementDaoI;
import com.wfzcx.ppms.execute.po.ProFinance;
import com.wfzcx.ppms.execute.po.ProImplement;
import com.wfzcx.ppms.execute.service.ProjectImplementServiceI;
import com.wfzcx.ppms.workflow.component.ProjectWorkFlowComponent;

@Scope("prototype")
@Service("/execute/service/impl/ProjectImplementServiceImpl")
public class ProjectImplementServiceImpl implements ProjectImplementServiceI {
	
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProjectWorkFlowComponent pwfc;
	@Autowired
	SysWorkflowManageComponent workflowManageComponent;
	@Autowired
	ProImplementDaoI proImplementDao;
	@Autowired
	ProFinanceDaoI proFinanceDao;
	@Autowired
	EasyUISumRowUtil easyUISumRowUtil;
	
	@Override
	public PaginationSupport qryProImp(Map param) {
		// TODO Auto-generated method stub
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		String dealStatus = param.get("dealStatus")==null?"" :param.get("dealStatus").toString();
		String proName = param.get("proName")==null?"" :param.get("proName").toString();
		String proTrade = param.get("proTrade")==null?"" :param.get("proTrade").toString();
		String proPerate = param.get("proPerate")==null?"" :param.get("proPerate").toString();
		String proReturn = param.get("proReturn")==null?"" :param.get("proReturn").toString();
		String proSendtype = param.get("proSendtype")==null?"" :param.get("proSendtype").toString();
		String proType = param.get("proType")==null?"" :param.get("proType").toString();
		String proPerson = param.get("proPerson")==null?"" :param.get("proPerson").toString();
		String menuid = param.get("menuid")==null?"" :param.get("menuid").toString();
		String activityId = param.get("activityId")==null?"" :param.get("activityId").toString();
		
		SysUser sysUser = SecureUtil.getCurrentUser();
		
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT T.PROJECTID,");
		sql.append("       T.PRO_NAME,");
		sql.append("       T.PRO_TYPE,");
		sql.append("       F.IMPLEMENTID,");
		sql.append("       F.PROJECT_COMPANY,");
		sql.append("       F.ESTABLISH_TIME,");
		sql.append("       F.SAFEGUARD_MEASURE,");
		sql.append("       F.SAFEGUARD_MEASURE_PATH,");
		sql.append("       F.FINANCE_TIME,");
		sql.append("       F.PROPERTY_TIME,");
		sql.append("       F.START_TIME,");
		sql.append("       F.END_TIME,");
		sql.append("       F.STATUS,");
		sql.append("       CASE  WHEN F.STATUS IS NULL THEN '未录入' WHEN '0' = F.STATUS THEN '已录入' WHEN '1'=F.STATUS THEN '已提交' END STATUS_NAME,");
		sql.append("       F.DATATYPE,");
		sql.append("       F.REMARK,");
		sql.append("       F.CREATEUSER,");
		sql.append("       (SELECT A.USERNAME FROM SYS_USER A WHERE A.USERID = F.CREATEUSER) CREATEUSERNAME,");
		sql.append("       (SELECT A.USERNAME FROM SYS_USER A WHERE A.USERID = F.UPDATEUSER) UPDATEUSERNAME,");
		sql.append("       F.CREATETIME,");
		sql.append("       F.UPDATEUSER,");
		sql.append("       F.UPDATETIME,");
		sql.append("       T.PRO_YEAR,");
		sql.append("       T.AMOUNT,");
		sql.append("       T.PRO_TRADE,");
		sql.append("       T.PRO_PERATE,");
		sql.append("       T.PRO_RETURN,");
		sql.append("       T.PRO_SENDTIME,");
		sql.append("       T.PRO_SENDTYPE,");
		sql.append("       T.PRO_SENDPERSON,");
		sql.append("       T.PRO_SITUATION,");
		sql.append("       T.PRO_PERSON,");
		sql.append("       T.PRO_PHONE,");
		sql.append("       S.PURCHASEID,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'PROTYPE'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.PRO_TYPE) PRO_TYPE_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICCODEITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'PROTRADE'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.PRO_TRADE) PRO_TRADE_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'PROOPERATE'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.PRO_PERATE) PRO_PERATE_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'PRORETURN'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.PRO_RETURN) PRO_RETURN_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'PROSENDTYPE'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.PRO_SENDTYPE) PRO_SENDTYPE_NAME");
		sql.append("  FROM PRO_PROJECT T");
		sql.append("  LEFT JOIN PRO_IMPLEMENT F");
		sql.append("    ON F.PROJECTID = T.PROJECTID");
		sql.append(" INNER JOIN PRO_PURCHASE_RESULT S");
		sql.append("    ON S.PROJECTID = T.PROJECTID and s.status='10' and s.ORGCODE = '"+sysUser.getOrgcode()+"' ");
		sql.append(" WHERE 1 = 1");
		if(!"".equals(dealStatus)){
			if("2".equals(dealStatus)){
				sql.append("  AND EXISTS (SELECT 1 FROM PRO_IMPLEMENT H  WHERE H.PROJECTID = T.PROJECTID AND H.STATUS = '1') ");
			}else{
				sql.append("  AND NOT　EXISTS (SELECT 1 FROM PRO_IMPLEMENT H  WHERE H.PROJECTID = T.PROJECTID AND H.STATUS = '1') ");
			}
		}

		if(!"".equals(proName)){
			sql.append(" and t.pro_name like '%").append(proName.trim()).append("%'");
		}
		if(!"".equals(proPerson)){
			sql.append(" and t.pro_person like '%").append(proPerson.trim()).append("%'");
		}
		if(!"".equals(proTrade)){
			sql.append(" and t.pro_trade in ('").append(proTrade.replaceAll(",", "','")).append("')");
		}
		if(!"".equals(proPerate)){
			sql.append(" and t.pro_perate in ('").append(proPerate.replaceAll(",", "','")).append("')");
		}
		if(!"".equals(proReturn)){
			sql.append(" and t.pro_return in ('").append(proReturn.replaceAll(",", "','")).append("')");
		}
		if(!"".equals(proSendtype)){
			sql.append(" and t.pro_sendtype in ('").append(proSendtype.replaceAll(",", "','")).append("')");
		}
		if(!"".equals(proType)){
			sql.append(" and t.pro_type in ('").append(proType.replaceAll(",", "','")).append("')");
		}
		sql.append("  order by t.PROJECTID desc ");
		System.out.println("【实施情况查询打印sql：】"+sql.toString());
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public PaginationSupport qryProFinance(Map map) {
		// TODO Auto-generated method stub
		String projectid = map.get("projectid")==null?"":map.get("projectid").toString();
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT T.FINANCEID,");
        sql.append("       T.PROJECTID,");
        sql.append("       T.FINANCE_CODE,");
        sql.append("       T.FINANCE_NAME,");
        sql.append("       T.AMOUNT,");
        sql.append("       T.REMARK");
        sql.append("  FROM PRO_FINANCE T");
        sql.append("  WHERE 1=1");

		if("".equals(projectid)){
			sql.append(" and 1<>1" );
		}else{
			sql.append(" and T.PROJECTID = "+projectid );
		}
		
		System.out.println("【实施情况-融资机构sql：】"+sql.toString());
		
		PaginationSupport p = mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
		List<Map<String,String>> sumList = new ArrayList<Map<String,String>>();
		Map<String,String> sumMap = new HashMap<String,String>();
		/*sumMap.put("financeName", "本页小计");
		sumMap.put("amount", "AMOUNT");
		sumList.add(sumMap);
		sumMap = new HashMap<String,String>();*/
		sumMap.put("financeCode", "合计");
		sumMap.put("amount", "AMOUNT");
		sumList.add(sumMap);
		List reList = easyUISumRowUtil.getAllPageSumRow(sumMap,sql.toString());
		p.setSumFooter(reList);
		
		return p;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveProImp(Map map) throws Exception{
		// TODO Auto-generated method stub
		String optFlag = map.get("optFlag")==null?"":map.get("optFlag").toString();
		String projectid = map.get("projectid")==null?"":map.get("projectid").toString();
		String purchaseid = map.get("purchaseid")==null?"":map.get("purchaseid").toString();
		String menuid = map.get("menuid")==null?"":map.get("menuid").toString();
		String activityId = map.get("activityId")==null?"":map.get("activityId").toString();
		String sendFlag = map.get("sendFlag")==null?"":map.get("sendFlag").toString();
		String subStr = map.get("subStr")==null?"":map.get("subStr").toString();
		String wfid = "";
		ProImplement dev = new ProImplement();
		BeanUtils.populate(dev, map);
		SysUser user = SecureUtil.getCurrentUser();
		if("add".equals(optFlag)){
			dev.setDatatype("1");
			dev.setStatus("0");
			dev.setCreateuser(user.getUserid().toString());
			dev.setCreatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			proImplementDao.save(dev);
			purchaseid = String.valueOf(dev.getImplementid());
		}else if("edit".equals(optFlag)){
			if("".equals(purchaseid)){
				throw new AppException("信息主键没找到！");
			}
			dev.setUpdateuser(user.getUserid().toString());
			dev.setUpdatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			proImplementDao.update(dev);
		}
		
		//保存融资机构
		List list = (List) JSONObject.parse(subStr);
		//proFinanceDao.deleteBySQL("pro_finance t", "t.projectid = '"+projectid+"'");
		for(int i =0;i<list.size();i++){
			ProFinance p = new ProFinance();
			BeanUtils.populate(p, (Map)list.get(i));
			p.setProjectid(projectid);
			proFinanceDao.saveOrUpdate(p);
		}
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void subProImp(Map map) throws Exception {
		// TODO Auto-generated method stub
		String implementid = map.get("implementid")==null?"":map.get("implementid").toString();
	    String	updateSql = "update pro_implement t set t.status = '1' where t.implementid in ("+implementid+")";
	    proImplementDao.updateBySql(updateSql);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void revokeProImp(Map map) throws Exception {
		// TODO Auto-generated method stub
		String implementid = map.get("implementid")==null?"":map.get("implementid").toString();
	    String	updateSql = "update pro_implement t set t.status = '0' where t.implementid in ("+implementid+")";
	    proImplementDao.updateBySql(updateSql);
	}
	@Override
	public void delProFinance(Map map) throws Exception {
		// TODO Auto-generated method stub
		String financeid = map.get("financeid")==null?"0":map.get("financeid").toString();
		if("0".equals(financeid)){
			throw new Exception();
		}else{
			proFinanceDao.delete(Integer.parseInt(financeid));
		}
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delProImp(Map map) throws Exception {
		// TODO Auto-generated method stub
		String implementids = map.get("implementids")==null?"":map.get("implementids").toString();
		
		if("".equals(implementids)){
			throw new AppException("信息主键没找到！");
		}
		String[] devs = implementids.split(","); 
		for(int i=0;i<devs.length;i++){
			int id = Integer.parseInt(devs[i]);
			String projectId = String.valueOf(proImplementDao.get(id).getProjectid());
			proImplementDao.delete(id);
			proFinanceDao.deleteBySQL("PRO_FINANCE", "projectid = '"+projectId+"'");
		}
	}
	
}
