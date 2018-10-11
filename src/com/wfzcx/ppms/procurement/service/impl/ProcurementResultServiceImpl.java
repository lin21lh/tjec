package com.wfzcx.ppms.procurement.service.impl;

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
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.component.SysWorkflowManageComponent;
import com.wfzcx.ppms.procurement.dao.ProNegotiationGroupDaoI;
import com.wfzcx.ppms.procurement.dao.ProProcurementExpertDaoI;
import com.wfzcx.ppms.procurement.dao.ProPurchaseResultDaoI;
import com.wfzcx.ppms.procurement.po.ProNegotiationGroup;
import com.wfzcx.ppms.procurement.po.ProProcurementExpert;
import com.wfzcx.ppms.procurement.po.ProPurchaseResult;
import com.wfzcx.ppms.procurement.service.ProcurementResultServiceI;
import com.wfzcx.ppms.workflow.component.ProjectWorkFlowComponent;

@Scope("prototype")
@Service("/procurement/service/impl/ProcurementResultServiceImpl")
public class ProcurementResultServiceImpl implements ProcurementResultServiceI {
	
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProjectWorkFlowComponent pwfc;
	@Autowired
	SysWorkflowManageComponent workflowManageComponent;
	@Autowired
	ProPurchaseResultDaoI proPurchaseResultDao;
	@Autowired
	ProProcurementExpertDaoI ProProcurementExpertDao;
	@Autowired
	ProNegotiationGroupDaoI ProNegotiationGroupDao;
	
	@Override
	public SysResource getResourceById(String menuid) throws Exception {
		// TODO Auto-generated method stub
		return pwfc.getResourceById(menuid);
	}
	@Override
	public PaginationSupport qryProRes(Map param) {
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
		String isAudit = param.get("isAudit")==null?"" :param.get("isAudit").toString();
		
		SysUser user = SecureUtil.getCurrentUser();
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT T.PROJECTID,");
		sql.append("       T.PRO_NAME,");
		sql.append("       T.PRO_TYPE,");
		sql.append("       f.purchaseid,");
		sql.append("       F.PURCHASE_NOTICE_TIME,");
		sql.append("       F.PURCHASE_NOTICE_MEDIA,");
		sql.append("       F.FILE_COMMIT_TIME,");
		sql.append("       F.FILE_JUDGE_TIME,");
		sql.append("       F.NEGOTIATE_TIME,");
		sql.append("       F.GOV_VERIFY_TIME,");
		sql.append("       F.CONTRACT_TIME,");
		sql.append("       F.CONTRACT_PUBLISH_TIME,");
		sql.append("       F.CONTRACT_PUBLISH_MEDIA,");
		sql.append("       F.CONTRACT_PATH,");
		sql.append("       F.WFID,");
		sql.append("       F.STATUS,");
		sql.append("        nvl((select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='WFSTATUS' and a.status=0  and a.code=f.status),'未录入') status_name,");
		sql.append("       F.ORGCODE,");
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
		sql.append("       s.ADVANCEID,");
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
		sql.append("  LEFT JOIN PRO_PURCHASE_RESULT F ");
		sql.append("    on f.projectid = t.projectid  ");
		sql.append(" INNER JOIN PRO_ADVANCE_RESULT s");
		if("1".equals(isAudit)){
			sql.append("    ON S.PROJECTID = T.PROJECTID and S.status='10' and s.INQUIRY_RESULT='1' ");
		}else{
			sql.append("    ON S.PROJECTID = T.PROJECTID and S.status='10' and s.INQUIRY_RESULT='1' and s.ORGCODE = '"+user.getOrgcode()+"'  ");
			
		}
		sql.append(" WHERE 1 = 1");
		
		if(!"1".equals(isAudit)){
			
			sql.append("and (f.createuser = '"+user.getUserid()+"' or f.createuser is null )");
		}
		if(!"".equals(dealStatus)){
			
				//获取工作流状态
				boolean firstNode = param.get("firstNode")==null?false: Boolean.parseBoolean(param.get("firstNode").toString());
				boolean lastNode =   param.get("lastNode")==null?false: Boolean.parseBoolean(param.get("lastNode").toString());
				String wfids="";
				try {
					wfids = pwfc.findCurrentWfids(menuid, activityId, dealStatus, firstNode, lastNode,"f");
				} catch (AppException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sql.append(" and ");
				sql.append(wfids);
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
		System.out.println("【采购结果查询打印sql：】"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public PaginationSupport advanceOrganUrl(Map map) {
		// TODO Auto-generated method stub
		String advanceid = map.get("advanceid")==null?"":map.get("advanceid").toString();
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		
        StringBuffer sql = new StringBuffer();
		sql.append("SELECT T.ORGANID,");
		sql.append("       T.ADVANCEID,");
		sql.append("       T.PROJECTID,");
		sql.append("       T.ORGAN_CODE,");
		sql.append("       T.ORGAN_NAME,");
		sql.append("       T.ISCOMBO,");
		sql.append("       T.ISPASS,");
		sql.append("       T.ISBID," );
		sql.append(	"t.score,");
		sql.append("       T.ORGAN_CONTENT,");
		sql.append("       T.REMARK");
		sql.append("  FROM PRO_ADVANCE_ORGAN T");
		sql.append(" WHERE 1=1 ");
		if("".equals(advanceid)){
			sql.append(" and 1<>1" );
		}else{
			sql.append(" and T.ADVANCEID = "+advanceid );
		}
		sql.append(" order by t.score desc ");
		System.out.println("【采购结果-采购单位查询sql：】"+sql.toString());
		return mapDataDao.queryPageBySQL(sql.toString(), pageIndex, pageSize);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveProRes(Map map) throws Exception {
		// TODO Auto-generated method stub
		String optFlag = map.get("optFlag")==null?"":map.get("optFlag").toString();
		String projectid = map.get("projectid")==null?"":map.get("projectid").toString();
		String purchaseid = map.get("purchaseid")==null?"":map.get("purchaseid").toString();
		String menuid = map.get("menuid")==null?"":map.get("menuid").toString();
		String activityId = map.get("activityId")==null?"":map.get("activityId").toString();
		String sendFlag = map.get("sendFlag")==null?"":map.get("sendFlag").toString();
		String subStr = map.get("subStr")==null?"":map.get("subStr").toString();
		String wfid = "";
		ProPurchaseResult dev = new ProPurchaseResult();
		BeanUtils.populate(dev, map);
		SysUser user = SecureUtil.getCurrentUser();
		if("add".equals(optFlag)){
			dev.setDatatype("1");
			dev.setOrgcode(user.getOrgcode());
			dev.setStatus("00");
			dev.setCreateuser(user.getUserid().toString());
			dev.setCreatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			proPurchaseResultDao.save(dev);
			purchaseid = String.valueOf(dev.getPurchaseid());
			map.put("purchaseid", purchaseid);
		}else if("edit".equals(optFlag)){
			if("".equals(purchaseid)){
				throw new AppException("信息主键没找到！");
			}
			dev.setUpdateuser(user.getUserid().toString());
			dev.setUpdatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			proPurchaseResultDao.update(dev);
			wfid = dev.getWfid();
			map.put("wfid", wfid);
		}
		
		//保存参与机构是否中选
		List list = (List) JSONObject.parse(subStr);
		mapDataDao.updateBatch(list, "PRO_ADVANCE_ORGAN");
		
		//保存专家列表
		
		String resultExpertData = StringUtil.stringConvert(map.get("resultExpertData"));
		List qualExpertList = (List) JSONObject.parse(resultExpertData);
		ProProcurementExpertDao.deleteBySQL("Pro_Procurement_Expert t", "t.advanceid = "+purchaseid+" and t.type='RESULT'");
		for(int i =0;i<qualExpertList.size();i++){
			ProProcurementExpert p = new ProProcurementExpert();
			BeanUtils.populate(p, (Map)qualExpertList.get(i));
			p.setAdvanceid(Integer.parseInt(purchaseid));
			p.setType("RESULT");
			ProProcurementExpertDao.save(p);
		}
		if("1".equals(sendFlag)){
			this.sendWorkFlow(map);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String sendWorkFlow(Map map) throws Exception {
		
		String menuid = map.get("menuid") == null ? "":map.get("menuid").toString();
		String purchaseid = map.get("purchaseid") == null ? "":map.get("purchaseid").toString();
		String activityId = map.get("activityId") == null ? "":map.get("activityId").toString();
		String opinion = map.get("opinion") == null ? "":map.get("opinion").toString();
		String wfid = map.get("wfid") == null ? "":map.get("wfid").toString();
		String resultGroupData = map.get("resultGroupData") == null ? "":map.get("resultGroupData").toString();
		
		
		String wfkey = pwfc.getWfkeyByMenuid(menuid);
		String msg = "";
		ResultMsg rsgMsg = null;
		String[] purchaseids = purchaseid.split(",");
		String[] wfids = wfid.split(",");
		for(int j=0;j<purchaseids.length;j++){
			if("".equals(StringUtil.stringConvert(wfid))||"null".equals(wfid)){
				 rsgMsg =	workflowManageComponent.startProcessByKeyAndPush(wfkey, new HashMap());
				 wfid = StringUtil.stringConvert(rsgMsg.getBody().get("EXECID"));
				 ProPurchaseResult pp = proPurchaseResultDao.get(Integer.valueOf(purchaseids[j]));
				 pp.setWfid(wfid);
				 pp.setStatus("01");
				 proPurchaseResultDao.update(pp);
			}else {
				if(!"".equals(resultGroupData)){//保存评判小组
					List qualExpertList = (List) JSONObject.parse(resultGroupData);
					ProNegotiationGroupDao.deleteBySQL("pro_negotiation_group t", "t.PURCHASEID = "+purchaseid);
					for(int i =0;i<qualExpertList.size();i++){
						ProNegotiationGroup p = new ProNegotiationGroup();
						BeanUtils.populate(p, (Map)qualExpertList.get(i));
						p.setPurchaseid(Integer.parseInt(purchaseid));
						ProNegotiationGroupDao.save(p);
					}
				}
				SysUser user = SecureUtil.getCurrentUser();
				String usercode = user.getUsercode();
				rsgMsg =workflowManageComponent.completeTask(wfids[j], activityId, new HashMap(), usercode, opinion);
			}
			
			if(rsgMsg!=null&&!rsgMsg.isSuccess()){//处理失败
				msg = rsgMsg.getTitle();
				throw new Exception(msg);
			}
		}
		
		return msg;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delProRes(Map map) throws Exception {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String purchaseids = map.get("purchaseids")==null?"":map.get("purchaseids").toString();
		
		if("".equals(purchaseids)){
			throw new AppException("信息主键没找到！");
		}
		String[] devs = purchaseids.split(","); 
		for(int i=0;i<devs.length;i++){
			int id = Integer.parseInt(devs[i]);
			proPurchaseResultDao.delete(id);
		}
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String revokeWorkFlow(String wfid, String purchaseid,
			String activityId) throws Exception {
		SysUser user = SecureUtil.getCurrentUser();
		String usercode = user.getUsercode();
		String returnMsg = "";
		ResultMsg  msg = null;
		String[] wfids = wfid.split(",");
		if(wfids.length>1){
			for(int i=0;i<wfids.length;i++){
				msg = workflowManageComponent.getBackWorkflow(wfids[i], activityId, usercode, null);
				 if(!msg.isSuccess()){//处理失败
					 returnMsg = msg.getTitle();
					 throw new Exception(returnMsg);
				}
			}
		}else{
			 msg = workflowManageComponent.getBackWorkflow(wfid, activityId, usercode, null);
			 if(!msg.isSuccess()){//处理失败
				 returnMsg = msg.getTitle();
				 throw new Exception(returnMsg);
			}
		}
		 
		return returnMsg;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String backWorkFlow(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		String returnMsg = "";
		ResultMsg rsMsg = null;
		String wfid = StringUtil.stringConvert(param.get("wfid"));
		String activityId = StringUtil.stringConvert(param.get("activityId"));
		String opinion = StringUtil.stringConvert(param.get("opinion"));
		SysUser user = SecureUtil.getCurrentUser();
		String usercode = user.getUsercode();
		String[] wfids = wfid.split(",");
		if(wfids.length>1){
			for(int i=0;i<wfids.length;i++){
				rsMsg = workflowManageComponent.sendBackWorkflow(wfids[i], activityId, new HashMap(), usercode, opinion);
				if(!rsMsg.isSuccess()){
					returnMsg = rsMsg.getTitle();
					throw new Exception(returnMsg);
				}
			}
		}else{
			rsMsg = workflowManageComponent.sendBackWorkflow(wfid, activityId, new HashMap(), usercode, opinion);
			 if(!rsMsg.isSuccess()){//处理失败
				 returnMsg = rsMsg.getTitle();
				 throw new Exception(returnMsg);
			}
		}
		 return returnMsg;
	}
	
	@Override
	public PaginationSupport resultExpertGrid(Map map) {
		String optFlag = map.get("optFlag")==null?"":map.get("optFlag").toString();
		String advanceid = map.get("advanceid")==null?"":map.get("advanceid").toString();
		String purchaseid = map.get("purchaseid")==null?"":map.get("purchaseid").toString();
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT T.purEXPERTID,");
        sql.append("       T.EXPERT_NAME,");
        sql.append("       T.EXPERTID,");
        sql.append("       T.EXPERT_TYPE,");
        sql.append("       T.EXPERT_PHONE,");
        sql.append("       T.BIDMAJOR,");
        sql.append("       T.RESPONSIBLE_AREA,");
        sql.append("       T.REMARK,");
        sql.append("       T.advanceid");
        sql.append("  FROM pro_procurement_expert T");
        sql.append("  where 1=1 ");
        if("add".equals(optFlag)){
        	if("".equals(advanceid)){
    			sql.append(" and 1<>1" );
    		}else{
    			sql.append(" and t.type='ADVANCE' and T.advanceid = "+advanceid );
    		}
    		
        }else{
        	if("".equals(purchaseid)){
    			sql.append(" and 1<>1" );
    		}else{
    			sql.append(" and t.type='RESULT' and T.advanceid = "+purchaseid );
    		}
        }
		
		System.out.println("【采购结果-专家sql：】"+sql.toString());
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public PaginationSupport qryExpertByQ(Map map) {
		// TODO Auto-generated method stub
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		String name = map.get("q")==null?"":map.get("q").toString();
		
		SysUser user = SecureUtil.getCurrentUser();
		StringBuffer sql = new StringBuffer();



		sql.append("SELECT T.EXPERTID,");
		sql.append("       T.CODE,");
		sql.append("       T.NAME,");
		sql.append("       T.SEX,");
		sql.append("       T.BIRTHDAY,");
		sql.append("       T.IDTYPE,");
		sql.append("       T.IDCARD,");
		sql.append("       T.REGION,");
		sql.append("       T.POLITICS_STATUS,");
		sql.append("       T.IS_EMERGENCY,");
		sql.append("       T.EXPERT_TYPE,");
		sql.append("       T.HIGHEST_DEGREE,");
		sql.append("       T.MAJOR,");
		sql.append("       T.GRADUATE_SCHOOL,");
		sql.append("       T.WORKING_CONDITION,");
		sql.append("       T.MAJOR_TYPE,");
		sql.append("       T.MAJOR_YEAR,");
		sql.append("       T.DUTY,");
		sql.append("       T.PROFESSIONAL_TITLE,");
		sql.append("       T.TITLE_NUMBER,");
		sql.append("       T.UNIT_NAME,");
		sql.append("       T.UNIT_ADDRESS,");
		sql.append("       T.AVOID_UNIT,");
		sql.append("       T.PHONE_NUMBER,");
		sql.append("       T.WECHAT,");
		sql.append("       T.QQ,");
		sql.append("       T.EMAIL,");
		sql.append("       T.HOME_TELEPHONE,");
		sql.append("       T.HOME_ADDRESS,");
		sql.append("       T.HOME_POSTCODE,");
		sql.append("       T.PHOTO,");
		sql.append("       T.BID_MAJOR,");
		sql.append("       (SELECT WMSYS.WM_CONCAT(L.FOREIGNKEY)");
		sql.append("          FROM PRO_ASSOCIATION L");
		sql.append("         WHERE L.MAJORKEY = T.EXPERTID");
		sql.append("           AND L.ELEMENTCODE = 'BIDMAJOR') BID_MAJOR_CODE,");
		sql.append("       (SELECT WMSYS.WM_CONCAT(A.CODE || '-' ||A.NAME)");
		sql.append("          FROM SYS_YW_DICCODEITEM A, PRO_ASSOCIATION L");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'BIDMAJOR'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = L.FOREIGNKEY");
		sql.append("           AND L.MAJORKEY = T.EXPERTID");
		sql.append("           AND L.ELEMENTCODE = 'BIDMAJOR') BID_MAJOR_NAME,");
		sql.append("       T.BID_AREA,");
		sql.append("       (SELECT WMSYS.WM_CONCAT(L.FOREIGNKEY)");
		sql.append("          FROM PRO_ASSOCIATION L");
		sql.append("         WHERE L.MAJORKEY = T.EXPERTID");
		sql.append("           AND L.ELEMENTCODE = 'BIDAREA') BID_AREA_CODE,");
		sql.append("       (SELECT WMSYS.WM_CONCAT(A.CODE || '-' ||A.NAME)");
		sql.append("          FROM SYS_YW_DICCODEITEM A, PRO_ASSOCIATION L");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'BIDAREA'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = L.FOREIGNKEY");
		sql.append("           AND L.MAJORKEY = T.EXPERTID");
		sql.append("           AND L.ELEMENTCODE = 'BIDAREA') BID_AREA_NAME,");
		sql.append("       T.QUALIFICATION,");
		sql.append("       T.CREATE_USER,");
		sql.append("       T.CREATE_TIME,");
		sql.append("       T.UPDATE_USER,");
		sql.append("       T.UPDATE_TIME,");
		sql.append("       T.IS_TRAIN,");
		sql.append("       T.RESEARCH,");
		sql.append("       T.HAS_BID_PROJECT,");
		sql.append("       T.INDUSTRY,");
		sql.append("       T.HIGHEST_OFFERING,");
		sql.append("       T.NATION,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'SEX'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.SEX) SEX_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'IDTYPE'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.IDTYPE) IDTYPE_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'POLITICSSTATUS'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.POLITICS_STATUS) POLITICS_STATUS_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'ISEMERGENCY'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.IS_EMERGENCY) IS_EMERGENCY_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'EXPERTTYPE'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.EXPERT_TYPE) EXPERT_TYPE_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'HIGHESTDEGREE'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.HIGHEST_DEGREE) HIGHEST_DEGREE_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'PROFESSIONALTITLE'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.PROFESSIONAL_TITLE) PROFESSIONAL_TITLE_NAME,");
		sql.append("       (SELECT WMSYS.WM_CONCAT(A.NAME)");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'QUALIFICATION'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE IN (SELECT F.ENUM_CODE");
		sql.append("                            FROM PRO_QUALIFICATION F");
		sql.append("                           WHERE F.EXPERTID = T.EXPERTID)) QUALIFICATION_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'ISORNOT'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.IS_TRAIN) IS_TRAIN_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'HIGHESTOFFERING'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.HIGHEST_OFFERING) HIGHEST_OFFERING_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'MAJORTYPE'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.MAJOR_TYPE) MAJOR_TYPE_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'CATEGORY'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.INDUSTRY) INDUSTRY_NAME,");
		sql.append("       (SELECT A.USERNAME FROM SYS_USER A WHERE A.USERID = T.CREATE_USER) CREATE_USER_NAME,");
		sql.append("       (SELECT A.USERNAME FROM SYS_USER A WHERE A.USERID = T.UPDATE_USER) UPDATE_USER_NAME");
		sql.append("  FROM PRO_EXPERT T");
		sql.append("  WHERE 1=1 and T.is_use = 1");
		
		if(!"".equals(name)){
			sql.append(" and t.name like '%").append(name.trim()).append("%'");
		}
		
		sql.append(" ORDER BY T.EXPERTID");

		System.out.println("【方案评审-过滤专家库打印sql：】"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public PaginationSupport resultGroupGrid(Map map) {
		// TODO Auto-generated method stub
		String purchaseid = map.get("purchaseid")==null?"":map.get("purchaseid").toString();
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT T.NEGOTIATIONID,");
        sql.append("       T.PURCHASEID,");
        sql.append("       T.NAME,");
        sql.append("       T.PHONE,");
        sql.append("       T.REMARK,t.duty");
       
        sql.append("  FROM PRO_NEGOTIATION_GROUP T");
        sql.append("  where 1=1 ");
        	if("".equals(purchaseid)){
    			sql.append(" and 1<>1" );
    		}else{
    			sql.append("  and T.PURCHASEID = "+purchaseid );
    		}
		
		System.out.println("【采购结果-确认工作组sql：】"+sql.toString());
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public void saveAuditData(Map map) throws Exception {
		// TODO Auto-generated method stub
		String purchaseid = map.get("purchaseid") == null ? "":map.get("purchaseid").toString();
		String resultGroupData = map.get("resultGroupData") == null ? "":map.get("resultGroupData").toString();
		List qualExpertList = (List) JSONObject.parse(resultGroupData);
		ProNegotiationGroupDao.deleteBySQL("pro_negotiation_group t", "t.PURCHASEID = "+purchaseid);
		for(int i =0;i<qualExpertList.size();i++){
			ProNegotiationGroup p = new ProNegotiationGroup();
			BeanUtils.populate(p, (Map)qualExpertList.get(i));
			p.setPurchaseid(Integer.parseInt(purchaseid));
			ProNegotiationGroupDao.save(p);
		}
	}
}
