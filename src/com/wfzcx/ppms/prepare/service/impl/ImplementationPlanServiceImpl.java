package com.wfzcx.ppms.prepare.service.impl;

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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.dao.util.EasyUISumRowUtil;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.component.SysWorkflowManageComponent;
import com.wfzcx.ppms.discern.dao.ProApproveDao;
import com.wfzcx.ppms.discern.dao.ProProjectDao;
import com.wfzcx.ppms.discern.po.ProApprove;
import com.wfzcx.ppms.prepare.dao.ProFinanceUnitDaoI;
import com.wfzcx.ppms.prepare.dao.ProQualitativeExpertDaoI;
import com.wfzcx.ppms.prepare.dao.ProQualitativeUnitDaoI;
import com.wfzcx.ppms.prepare.dao.ProSolutionDaoI;
import com.wfzcx.ppms.prepare.po.ProFinanceUnit;
import com.wfzcx.ppms.prepare.po.ProQualitativeExpert;
import com.wfzcx.ppms.prepare.po.ProQualitativeUnit;
import com.wfzcx.ppms.prepare.po.ProSolution;
import com.wfzcx.ppms.prepare.service.ImplementationPlanServiceI;
import com.wfzcx.ppms.workflow.component.ProjectWorkFlowComponent;

@Scope("prototype")
@Service("/prepare/service/impl/ImplementationPlanServiceImpl")
public class ImplementationPlanServiceImpl implements
		ImplementationPlanServiceI {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProSolutionDaoI proSolutionDao;
	@Autowired
	ProjectWorkFlowComponent pwfc;
	@Autowired
	ProApproveDao proApproveDao;
	@Autowired
	SysWorkflowManageComponent workflowManageComponent;
	@Autowired
	EasyUISumRowUtil easyUISumRowUtil;
	@Autowired
	ProProjectDao proProjectDao;
	@Autowired
	ProQualitativeUnitDaoI qualitativeUnitDao;
	@Autowired
	ProQualitativeUnitDaoI proQualitativeUnitDao;
	@Autowired
	ProFinanceUnitDaoI proFinanceUnitDao;
	@Autowired
	ProQualitativeExpertDaoI proQualitativeExpertDao;
	
	/**
	 * @throws AppException 
	 * 查询实施方案
	 * @Title: qryImlPlan 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param param
	 * @param @return 设定文件 
	 * @return PaginationSupport 返回类型 
	 * @throws
	 */
	public PaginationSupport qryImlPlan(Map<String, Object> param){
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
		
		StringBuffer sql = new StringBuffer();
		SysUser user = SecureUtil.getCurrentUser();
		sql.append("select t.projectid,");
		sql.append("       t.pro_name,");
		sql.append("       t.pro_type,");
		sql.append("       t.pro_year,");
		sql.append("       t.amount,");
		sql.append("       t.pro_trade,");
		sql.append("       t.pro_perate,");
		sql.append("       t.pro_return,");
		sql.append("       t.pro_sendtime,");
		sql.append("       t.pro_sendtype,");
		sql.append("       t.pro_sendperson,");
		sql.append("       t.pro_situation,");
		sql.append("       t.pro_person,");
		sql.append("       t.pro_phone,");
		sql.append("       t.pro_scheme,");
		sql.append("       f.solutionid,");
		sql.append("       f.datatype,");
		sql.append("       f.risk_allocation,");
		sql.append("       f.project_finance,");
		sql.append("       f.repay_mechanism,");
		sql.append("       f.suited_plan,");
		sql.append("       f.contract_system,");
		sql.append("       f.contract_core_content,");
		sql.append("       f.supervisory_regime,");
		sql.append("       f.advance_publish_time,");
		sql.append("       f.purchase_notice_time,");
		sql.append("       f.implementation_plan_path,");
		sql.append("       f.wfid,");
		sql.append("       f.status,");
		sql.append("        nvl((select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='WFSTATUS' and a.status=0  and a.code=f.status),'未录入') status_name,");
		sql.append("       f.ORGCODE,");
		sql.append("       f.createuser,");
		sql.append("       (select a.username from sys_user a where a.userid = f.createuser) createusername,");
		sql.append("       (select a.username from sys_user a where a.userid = f.updateuser) updateusername,");
		sql.append("       (select a.name");
		sql.append("          from SYS_YW_DICENUMITEM a");
		sql.append("         where upper(a.elementcode) = 'PROTYPE'");
		sql.append("           and a.status = 0");
		sql.append("           and a.code = t.pro_type) pro_type_name,");
		sql.append("       (select a.name");
		sql.append("          from SYS_YW_DICCODEITEM a");
		sql.append("         where upper(a.elementcode) = 'PROTRADE'");
		sql.append("           and a.status = 0");
		sql.append("           and a.code = t.pro_trade) pro_trade_name,");
		sql.append("       (select a.name");
		sql.append("          from SYS_YW_DICENUMITEM a");
		sql.append("         where upper(a.elementcode) = 'PROOPERATE'");
		sql.append("           and a.status = 0");
		sql.append("           and a.code = t.pro_perate) pro_perate_name,");
		sql.append("       (select a.name");
		sql.append("          from SYS_YW_DICENUMITEM a");
		sql.append("         where upper(a.elementcode) = 'PRORETURN'");
		sql.append("           and a.status = 0");
		sql.append("           and a.code = t.pro_return) pro_return_name,");
		sql.append("       (select a.name");
		sql.append("          from SYS_YW_DICENUMITEM a");
		sql.append("         where upper(a.elementcode) = 'PROSENDTYPE'");
		sql.append("           and a.status = 0");
		sql.append("           and a.code = t.pro_sendtype) pro_sendtype_name,");
		sql.append("       f.createtime,");
		sql.append("       f.updateuser,");
		sql.append("       f.updatetime");
		sql.append("  from pro_project t, pro_solution f,pro_develop p");
		sql.append(" where t.projectid=p.projectid and  t.projectid = f.projectid(+) ");
		if(!"1".equals(isAudit)){
			sql.append(" and p.IMPLEMENT_ORGAN = '"+user.getOrgcode()+"'");
			sql.append(" and (f.createuser = '"+user.getUserid()+"' or f.createuser is null )");
		}
		/*if("1".equals(isAudit)){
			sql.append("   and exists");
			sql.append(" (select 1 from pro_develop s where s.projectid = t.projectid ) ");
		}*/
		if(!"".equals(dealStatus)){
			//获取工作流状态
			boolean firstNode = param.get("firstNode")==null?false: Boolean.parseBoolean(param.get("firstNode").toString());
			boolean lastNode =   param.get("lastNode")==null?false: Boolean.parseBoolean(param.get("lastNode").toString());
			String wfids="";
			try {
				wfids = pwfc.findCurrentWfids(menuid, activityId, dealStatus, firstNode, lastNode,"f");
			} catch (AppException e) {
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
		
		System.out.println("【实施方案打印sql：】"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveImlPlan(Map<String, Object> map) throws Exception {
		String optFlag = map.get("optFlag")==null?"":map.get("optFlag").toString();
		String projectid = map.get("projectid")==null?"":map.get("projectid").toString();
		String solutionid = map.get("solutionid")==null?"":map.get("solutionid").toString();
		String menuid = map.get("menuid")==null?"":map.get("menuid").toString();
		String activityId = map.get("activityId")==null?"":map.get("activityId").toString();
		String sendFlag = map.get("sendFlag")==null?"":map.get("sendFlag").toString();
		String wfid = "";
		ProSolution dev = new ProSolution();
		BeanUtils.populate(dev, map);
		SysUser user = SecureUtil.getCurrentUser();
		if("add".equals(optFlag)){
			dev.setDatatype("1");
			dev.setStatus("00");
			dev.setOrgcode(user.getOrgcode());
			dev.setCreateuser(user.getUserid().toString());
			dev.setCreatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			proSolutionDao.save(dev);
			solutionid = String.valueOf(dev.getSolutionid());
			map.put("solutionid", solutionid);
		}else if("edit".equals(optFlag)){
			if("".equals(solutionid)){
				throw new AppException("信息主键没找到！");
			}
			dev.setUpdateuser(user.getUserid().toString());
			dev.setUpdatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			proSolutionDao.update(dev);
			wfid = dev.getWfid();
		}
		
		if("1".equals(sendFlag)){
			this.sendWorkFlow(map);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delImlPlan(Map<String, Object> map) throws AppException {
		// TODO Auto-generated method stub
		String solutionids = map.get("solutionids")==null?"":map.get("solutionids").toString();
		
		if("".equals(solutionids)){
			throw new AppException("信息主键没找到！");
		}
		String[] devs = solutionids.split(","); 
		for(int i=0;i<devs.length;i++){
			int id = Integer.parseInt(devs[i]);
			proSolutionDao.delete(id);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String sendWorkFlow(Map map) throws Exception {
		String menuid = map.get("menuid") == null ? "":map.get("menuid").toString();
		String solutionid = map.get("solutionid") == null ? "":map.get("solutionid").toString();
		String activityId = map.get("activityId") == null ? "":map.get("activityId").toString();
		String opinion = map.get("opinion") == null ? "":map.get("opinion").toString();
		String wfid = map.get("wfid") == null ? "":map.get("wfid").toString();
		String flag = map.get("flag") == null ? "":map.get("flag").toString();
		String qualExpertData = map.get("qualExpertData")==null?"":map.get("qualExpertData").toString();
		String qualUnitData = map.get("qualUnitData")==null?"":map.get("qualUnitData").toString();
		String projectid = map.get("projectid")==null?"":map.get("projectid").toString();
		String wfkey = pwfc.getWfkeyByMenuid(menuid);
		String msg = "";
		ResultMsg rsgMsg = null;
		String[] solutionids = solutionid.split(",");
		String[] wfids = wfid.split(",");
		for(int j=0;j<solutionids.length;j++){
			if("".equals(StringUtil.stringConvert(wfid))||"null".equals(wfid)){
				 rsgMsg =	workflowManageComponent.startProcessByKeyAndPush(wfkey, new HashMap());
				 wfid = StringUtil.stringConvert(rsgMsg.getBody().get("EXECID"));
				 ProSolution pp = proSolutionDao.get(Integer.valueOf(solutionids[j]));
				 pp.setWfid(wfid);
				 pp.setStatus("01");
				 proSolutionDao.update(pp);
			}else {
				if("1".equals(flag)){
					this.saveAuditData(map);//保存数据
					
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
	public String revokeWorkFlow(String wfid, String projectid,
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
	public SysResource getResourceById(String menuid) throws Exception {
		// TODO Auto-generated method stub
		return pwfc.getResourceById(menuid);
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
			this.saveAuditData(param);//保存信息
			rsMsg = workflowManageComponent.sendBackWorkflow(wfid, activityId, new HashMap(), usercode, opinion);
			 if(!rsMsg.isSuccess()){//处理失败
				 returnMsg = rsMsg.getTitle();
				 throw new Exception(returnMsg);
			}
		}
		 return returnMsg;
	}
	
	@Override
	public PaginationSupport queryThirdOrg(String projectid)
			throws AppException {
		StringBuffer sql = new StringBuffer();
		if("".equals(projectid)){
			sql.append("select * from PRO_APPRORGAN where 1<>1");
		}else{
			sql.append("select * from PRO_APPRORGAN where projectid= ");
			sql.append(projectid);
		}
		return mapDataDao.queryPageBySQL(sql.toString(), 1, 10000);
	}
	@Override
	public PaginationSupport queryFinance(String projectid) throws AppException {
		StringBuffer sql = new StringBuffer();
		if("".equals(projectid)){
			sql.append("select * from PRO_BUDGET where 1<>1");
		}else{
			sql.append("select * from PRO_BUDGET where BUDGET_type =1 and projectid= ");
			sql.append(projectid);
		}
		
		PaginationSupport p = mapDataDao.queryPageBySQL(sql.toString(), 1, 10000);
		
		List<Map<String,String>> sumList = new ArrayList<Map<String,String>>();
		Map<String,String> sumMap = new HashMap<String,String>();
		sumMap.put("budget_year", "合计");
		sumMap.put("budget_gqtz", "budget_Gqtz");
		sumMap.put("budget_yybt", "budget_Yybt");
		sumMap.put("budget_fxcd", "budget_Fxcd");
		sumMap.put("budget_pttr", "budget_Pttr");
		sumMap.put("total", "total");
		sumList.add(sumMap);
		List reList = easyUISumRowUtil.getAllPageSumRow(sumMap,sql.toString());
		p.setSumFooter(reList);
		
		return p;
		
	}
	
	@Override
	public List queryApprove(String projectid) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.*,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='JUDGERESULT' and a.status=0  and a.code=t.qual_result) qual_result_name,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='JUDGERESULT' and a.status=0  and a.code=t.vom_result) vom_result_name,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='JUDGERESULT' and a.status=0  and a.code=t.fc_result) fc_result_name,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='JUDGERESULT' and a.status=0  and a.code=t.cz_result) cz_result_name");
		sql.append(" from pro_approve t where projectid = ").append(projectid);
		return 	mapDataDao.queryListBySQLForConvert(sql.toString());
	}
	
	@Override
	public PaginationSupport qualUnitGrid(Map map) throws AppException {
		String projectid = map.get("projectid")==null?"":map.get("projectid").toString();
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT TT.QUALUNITID,");
        sql.append("       TT.UNIT_NAME,");
        sql.append("       TT.UNIT_PERSON,");
        sql.append("       TT.UNIT_TELPHONE,");
        sql.append("       TT.REMARK,");
        sql.append("       TT.PROJECTID");
        sql.append("  FROM PRO_QUALITATIVE_UNIT TT");
        sql.append("  where 1=1 ");

		if("".equals(projectid)){
			sql.append(" and 1<>1" );
		}else{
			sql.append(" and tT.PROJECTID = "+projectid );
		}
		System.out.println("【方案评审-定性分析组织单位sql：】"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	@Override
	public PaginationSupport financeUnitGrid(Map map) throws AppException {
		String projectid = map.get("projectid")==null?"":map.get("projectid").toString();
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT TT.finunitid,");
        sql.append("       TT.UNIT_NAME,");
        sql.append("       TT.UNIT_PERSON,");
        sql.append("       TT.UNIT_TELPHONE,");
        sql.append("       TT.REMARK,");
        sql.append("       TT.PROJECTID");
        sql.append("  FROM PRO_finance_UNIT TT");
        sql.append("  where 1=1 ");

		if("".equals(projectid)){
			sql.append(" and 1<>1" );
		}else{
			sql.append(" and tT.PROJECTID = "+projectid );
		}
		
		System.out.println("【方案评审-财政承受能力组织单位sql：】"+sql.toString());
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public PaginationSupport qualExpertGrid(Map map) throws AppException {
		String projectid = map.get("projectid")==null?"":map.get("projectid").toString();
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT T.QUALEXPERTID,");
        sql.append("       T.EXPERT_NAME,");
        sql.append("       T.EXPERT_ID,");
        sql.append("       T.EXPERT_TYPE,");
        sql.append("       T.EXPERT_PHONE,");
        sql.append("       T.BIDMAJOR,");
        sql.append("       T.RESPONSIBLE_AREA,");
        sql.append("       T.REMARK,");
        sql.append("       T.PROJECTID");
        sql.append("  FROM PRO_QUALITATIVE_EXPERT T");
        sql.append("  where 1=1 ");

		if("".equals(projectid)){
			sql.append(" and 1<>1" );
		}else{
			sql.append(" and T.PROJECTID = "+projectid );
		}
		
		System.out.println("【方案评审-定性分析专家sql：】"+sql.toString());
		
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
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveAuditData(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		String qualExpertData = map.get("qualExpertData")==null?"":map.get("qualExpertData").toString(); 
		String qualUnitData = map.get("qualUnitData")==null?"":map.get("qualUnitData").toString(); 
		String thirdOrganGridData = map.get("thirdOrganGridData")==null?"":map.get("thirdOrganGridData").toString(); 
		String financeGridData = map.get("financeGridData")==null?"":map.get("financeGridData").toString(); 
		String financeUnitData = map.get("financeUnitData")==null?"":map.get("financeUnitData").toString(); 
		String projectid = map.get("projectid")==null?"":map.get("projectid").toString(); 
		String vomid = StringUtil.stringConvert(map.get("vomid"));
		
		//保存定性分析数据
		//1.专家数据
		List qualExpertList = (List) JSONObject.parse(qualExpertData);
		proQualitativeExpertDao.deleteBySQL("PRO_QUALITATIVE_EXPERT t", "t.projectid = "+projectid+"");
		for(int i =0;i<qualExpertList.size();i++){
			ProQualitativeExpert p = new ProQualitativeExpert();
			BeanUtils.populate(p, (Map)qualExpertList.get(i));
			p.setProjectid(Integer.parseInt(projectid));
			proQualitativeExpertDao.save(p);
		}
		//2.组织单位
		List qualUnitList = (List) JSONObject.parse(qualUnitData);
		proQualitativeUnitDao.deleteBySQL("PRO_QUALITATIVE_UNIT t", "t.projectid = "+projectid+"");
		for(int i =0;i<qualUnitList.size();i++){
			ProQualitativeUnit p = new ProQualitativeUnit();
			BeanUtils.populate(p, (Map)qualUnitList.get(i));
			p.setProjectid(Integer.parseInt(projectid));
			proQualitativeUnitDao.save(p);
		}
		
		//保存定量分析数据
		//3.第三方机构
		List thirdOrganList  = JSONArray.parseArray(thirdOrganGridData);
		for (int i = 0; i < thirdOrganList.size(); i++) {
			Map param = (Map) thirdOrganList.get(i);
			param.remove("approrganid");
			param.put("projectid", projectid);//放入projectid
		}
		String where = "projectid = "+projectid;
		//先删除原先数据
		proProjectDao.deleteBySQL("PRO_APPRORGAN", where);
		//再保存页面数据
		proProjectDao.addBatchByList(thirdOrganList, "PRO_APPRORGAN");
		
		//保存财政承受能力验证
		//4.支出情况
		List financeGridList  = JSONArray.parseArray(financeGridData);
		for (int i = 0; i < financeGridList.size(); i++) {
			Map param = (Map) financeGridList.get(i);
			param.remove("budgetid");
			param.put("projectid", projectid);//放入projectid
			param.put("budget_type", 1);
		}
		where = "projectid = "+projectid+" and budget_type=1 ";
		//先删除原先数据
		proProjectDao.deleteBySQL("PRO_BUDGET", where);
		//再保存页面数据
		proProjectDao.addBatchByList(financeGridList, "PRO_BUDGET");
		
		//5.组织单位
		List financeUnitList = (List) JSONObject.parse(financeUnitData);
		proFinanceUnitDao.deleteBySQL("PRO_FINANCE_UNIT t", "t.projectid = "+projectid+"");
		for(int i =0;i<financeUnitList.size();i++){
			ProFinanceUnit p = new ProFinanceUnit();
			BeanUtils.populate(p, (Map)financeUnitList.get(i));
			p.setProjectid(Integer.parseInt(projectid));
			proFinanceUnitDao.save(p);
		}
		
		ProApprove approve = new ProApprove();
		SysUser user = SecureUtil.getCurrentUser();
		if("".equals(vomid)){//保存
			BeanUtils.populate(approve, map);
			approve.setCreateuser(user.getUserid().toString());//创建人
			approve.setCreatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//创建时间
			proApproveDao.save(approve);
		}else {//修改
			approve = proApproveDao.get(Integer.parseInt(vomid));
			BeanUtils.populate(approve, map);
			approve.setUpdateuser(user.getUserid().toString());//创建人
			approve.setUpdatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//创建时间
			proApproveDao.update(approve);
		}
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String sendWorkFlowForVerify(Map map) throws Exception {
		String activityId = map.get("activityId") == null ? "":map.get("activityId").toString();
		String opinion = map.get("opinion") == null ? "":map.get("opinion").toString();
		String wfid = map.get("wfid") == null ? "":map.get("wfid").toString();
		String backFlag = map.get("backFlag") == null ? "":map.get("backFlag").toString();
		String msg = "";
		ResultMsg rsgMsg = null;
		if(!"".equals(StringUtil.stringConvert(wfid))&&!"null".equals(wfid)){
			SysUser user = SecureUtil.getCurrentUser();
			String usercode = user.getUsercode();
			if("3".equals(backFlag)){//退回
				rsgMsg = workflowManageComponent.sendBackWorkflow(wfid, activityId, new HashMap(), usercode, opinion);
			}else {//推进
				rsgMsg = workflowManageComponent.completeTask(wfid, activityId, new HashMap(), usercode, opinion);
			}
		}
		if(rsgMsg!=null&&!rsgMsg.isSuccess()){//处理失败
			msg = rsgMsg.getTitle();
			throw new Exception(msg);
		}
		
		return msg;
	}
}
