package com.wfzcx.ppms.execute.service.impl;

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
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.component.SysWorkflowManageComponent;
import com.wfzcx.ppms.execute.dao.ProAssessDaoI;
import com.wfzcx.ppms.execute.po.ProAssess;
import com.wfzcx.ppms.execute.service.ProjectAssessServiceI;
import com.wfzcx.ppms.workflow.component.ProjectWorkFlowComponent;

@Scope("prototype")
@Service("/execute/service/impl/ProjectAssessServiceImpl")
public class ProjectAssessServiceImpl implements ProjectAssessServiceI {
	
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProjectWorkFlowComponent pwfc;
	@Autowired
	SysWorkflowManageComponent workflowManageComponent;
	@Autowired
	ProAssessDaoI proAssessDao;
	
	@Override
	public PaginationSupport qryProAss(Map param) {
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
		
		StringBuffer sql = new StringBuffer();
		SysUser sysUser = SecureUtil.getCurrentUser();

		sql.append("SELECT T.PROJECTID,");
		sql.append("       T.PRO_NAME,");
		sql.append("       T.PRO_TYPE,");
		sql.append("       T.CREATEUSER,");
		sql.append("       T.CREATETIME,");
		sql.append("       T.UPDATEUSER,");
		sql.append("       T.UPDATETIME,");
		sql.append("       (SELECT A.USERNAME FROM SYS_USER A WHERE A.USERID = T.CREATEUSER) CREATEUSERNAME,");
		sql.append("       (SELECT A.USERNAME FROM SYS_USER A WHERE A.USERID = T.UPDATEUSER) UPDATEUSERNAME,");
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
		sql.append("       T.xmdqhj,");
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
		sql.append(" INNER JOIN PRO_PURCHASE_RESULT S");
		sql.append("    ON S.PROJECTID = T.PROJECTID and s.status='10' and s.ORGCODE = '"+sysUser.getOrgcode()+"' ");
		sql.append(" WHERE 1 = 1 ");

		/*if(!"".equals(dealStatus)){
			if("3".equals(dealStatus)){
				sql.append("  AND EXISTS (SELECT 1 FROM PRO_IMPLEMENT H  WHERE H.PROJECTID = T.PROJECTID AND (H.STATUS IS NULL OR H.STATUS = '0')) ");
			}else if("4".equals(dealStatus)){
				sql.append("  AND NOT EXISTS (SELECT 1 FROM PRO_IMPLEMENT H  WHERE H.PROJECTID = T.PROJECTID ) ");
			}else {
				sql.append("  AND EXISTS (SELECT 1 FROM PRO_IMPLEMENT H  WHERE H.PROJECTID = T.PROJECTID AND H.STATUS = '1') ");
			}
		}*/
		
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
		System.out.println("【中期评估查询打印sql：】"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public PaginationSupport qryProAssess(Map map) {
		// TODO Auto-generated method stub
		String projectid = map.get("projectid")==null?"":map.get("projectid").toString();
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		
        StringBuffer sql = new StringBuffer();



        sql.append("SELECT T.ASSESSID,");
        sql.append("       T.PROJECTID,");
        sql.append("       T.ASSESS_TIME,");
        sql.append("       T.ASSESS_CONTENT,");
        sql.append("       T.ASSESS_PATH");
        sql.append("  FROM PRO_ASSESS T");
        sql.append(" WHERE 1 = 1");


		if("".equals(projectid)){
			sql.append(" and 1<>1" );
		}else{
			sql.append(" and T.PROJECTID = "+projectid );
		}
		sql.append("  order by T.ASSESS_TIME desc ");
		System.out.println("【中期评估-评估信息sql：】"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveProAss(Map map) throws Exception {
		// TODO Auto-generated method stub
		String projectid = map.get("projectid")==null?"":map.get("projectid").toString();
		String subStr = map.get("subStr")==null?"":map.get("subStr").toString();
		//保存实际绩效
		List list = (List) JSONObject.parse(subStr);
		for(int i =0;i<list.size();i++){
			ProAssess p = new ProAssess();
			BeanUtils.populate(p, (Map)list.get(i));
			p.setProjectid(Integer.parseInt(projectid));
			proAssessDao.saveOrUpdate(p);
		}
	}
	@Override
	public void delProAssess(Map map) throws Exception {
		// TODO Auto-generated method stub
		String assessid = map.get("assessid")==null?"0":map.get("assessid").toString();
		if("0".equals(assessid)){
			throw new Exception();
		}else{
			proAssessDao.delete(Integer.parseInt(assessid));
		}
	}
	
}
