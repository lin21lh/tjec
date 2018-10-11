package com.wfzcx.ppms.procurement.service.impl;

import java.lang.reflect.InvocationTargetException;
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
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.component.SysWorkflowManageComponent;
import com.wfzcx.ppms.procurement.dao.ProAdvanceDao;
import com.wfzcx.ppms.procurement.dao.ProProcurementExpertDaoI;
import com.wfzcx.ppms.procurement.po.ProAdvanceResult;
import com.wfzcx.ppms.procurement.po.ProProcurementExpert;
import com.wfzcx.ppms.procurement.service.ProjectAdvacneService;
import com.wfzcx.ppms.workflow.component.ProjectWorkFlowComponent;

/**
 * 项目预审service实现类
 * @ClassName: ProjectAdvacneServiceImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年9月21日 上午8:16:15
 */
@Scope("prototype")
@Service(" com.wfzcx.ppms.procurement.service.impl.ProjectAdvacneServiceImpl")
public class ProjectAdvacneServiceImpl implements ProjectAdvacneService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProjectWorkFlowComponent pwfc;
	@Autowired
	SysWorkflowManageComponent workflowManageComponent;
	@Autowired
	ProAdvanceDao proAdvanceDao;
	@Autowired
	ProProcurementExpertDaoI ProProcurementExpertDao;
	
	@Override
	public SysResource getResourceById(String menuid) throws Exception {
		return pwfc.getResourceById(menuid);
	}
	@Override
	public PaginationSupport queryProject(Map<String, Object> param)   throws AppException{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select t.projectid, t.pro_name,t.pro_type,t.amount, t.pro_year,");
		sql.append("       t.pro_trade, t.pro_perate,t.pro_return,t.pro_sendtime,");
		sql.append("       t.pro_sendtype,t.pro_sendperson,t.pro_situation,t.pro_person,");
		sql.append("       t.pro_phone,t.pro_scheme,t.pro_schemepath,t.pro_reportpath,");
		sql.append("       t.pro_conditionpath,t.pro_article,t.pro_articlepath,t2.wfid,");
		sql.append("       t2.status,t2.createuser,t2.createtime,t2.updateuser,t2.updatetime,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROTYPE' and a.status=0  and a.code=t.pro_type) pro_type_name,");
		sql.append("       (select a.name from SYS_YW_DICCODEITEM a where upper(a.elementcode)='PROTRADE' and a.status=0  and a.code=t.pro_trade) pro_trade_name,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROOPERATE' and a.status=0  and a.code=t.pro_perate) pro_perate_name,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PRORETURN' and a.status=0  and a.code=t.pro_return) pro_return_name,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSENDTYPE' and a.status=0  and a.code=t.pro_sendtype) pro_sendtype_name,");
		sql.append(" nvl((select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='WFSTATUS' and a.status=0  and a.code=t2.status),'未录入') status_name,");
		sql.append("       (select a.username from sys_user a where  a.userid=t2.createuser) createusername,");
		sql.append("       (select a.username from sys_user a where  a.userid=t2.updateuser) updateusername,t2.advanceid ");
		sql.append(",t2.notice_time,t2.publish_media,t2.inquiry_time,t2.inquiry_result,t2.inquiry_declare,t2.inquiry_declare_path");
		sql.append(",decode(t2.inquiry_result,'1','通过','0','未通过','') inquiry_result_name ");
		/*关联修改，添加关联pro_develop,pro_czcsnl表，原关联字段不作修改*/
		sql.append("  from PRO_PROJECT t, PRO_SOLUTION t1, PRO_ADVANCE_RESULT t2,pro_approve t3,pro_develop t4,pro_czcsnl t5 ");
		sql.append(" where t.projectid = t1.projectid  and t.projectid = t2.projectid(+) and t.projectid=t3.projectid(+) and t.projectid=t4.projectid and t.projectid=t5.projectid ");
		
		//项目预审应查询实施方案已经完成的项目
		String menuid = StringUtil.stringConvert(param.get("menuid"));
		String activityId = StringUtil.stringConvert(param.get("activityId"));
		String proName = StringUtil.stringConvert(param.get("proName"));
		if(!"".equals(proName)){
			sql.append(" and t.pro_name like '%").append(proName.trim()).append("%'");
		}
		String proPerson = StringUtil.stringConvert(param.get("proPerson"));
		if(!"".equals(proPerson)){
			sql.append(" and t.pro_person like '%").append(proPerson.trim()).append("%'");
		}
		String proTrade = StringUtil.stringConvert(param.get("proTrade"));
		if(!"".equals(proTrade)){
			sql.append(" and t.pro_trade in ('").append(proTrade.replaceAll(",", "','")).append("')");
		}
		String proPerate = StringUtil.stringConvert(param.get("proPerate"));
		if(!"".equals(proPerate)){
			sql.append(" and t.pro_perate in ('").append(proPerate.replaceAll(",", "','")).append("')");
		}
		String proReturn = StringUtil.stringConvert(param.get("proReturn"));
		if(!"".equals(proReturn)){
			sql.append(" and t.pro_return in ('").append(proReturn.replaceAll(",", "','")).append("')");
		}
		String proSendtype = StringUtil.stringConvert(param.get("proSendtype"));
		if(!"".equals(proSendtype)){
			sql.append(" and t.pro_sendtype in ('").append(proSendtype.replaceAll(",", "','")).append("')");
		}
		String proType = StringUtil.stringConvert(param.get("proType"));
		if(!"".equals(proType)){
			sql.append(" and t.pro_type in ('").append(proType.replaceAll(",", "','")).append("')");
		}
		
		String status = StringUtil.stringConvert(param.get("status"));
		//获取工作流状态
		boolean firstNode = param.get("firstNode")==null?false: Boolean.parseBoolean(param.get("firstNode").toString());
		boolean lastNode =   param.get("lastNode")==null?false: Boolean.parseBoolean(param.get("lastNode").toString());
		String wfids = pwfc.findCurrentWfids(menuid, activityId, status, firstNode, lastNode,"t2");
		sql.append(" and ");
		sql.append(wfids);
		String audit =   param.get("audit")==null?"":param.get("audit").toString();
		if(!"1".equals(audit)){
			SysUser user = SecureUtil.getCurrentUser();
			String orgcode = user.getOrgcode();
			//匹配组织机构和创建人
			sql.append(" and t4.implement_organ='"+orgcode+"'");
			/*sql.append(" and t1.orgcode = '").append(orgcode).append("' ");
			sql.append(" and t2.orgcode(+) = '").append(orgcode).append("' ");
			sql.append(" and (t2.createuser = ").append(user.getUserid()).append(" or t2.createuser is null) ");*/
			sql.append(" and t1.status='10'  ");
		}
		sql.append(" and t5.xmhj='2'");
		sql.append(" and t5.fc_result='1'");
		sql.append(" and t5.status='2'");
		sql.append("  order by t.projectid desc");

		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public PaginationSupport queryOrgan(String projectid, String advanceid) throws AppException {
		StringBuffer sql = new StringBuffer();
		if("".equals(projectid)||"".equals(advanceid)){
			sql.append("select * from PRO_ADVANCE_ORGAN where 1<>1");
		}else{
			sql.append("select * from PRO_ADVANCE_ORGAN where projectid= ");
			sql.append(projectid);
			sql.append(" and advanceid =");
			sql.append(advanceid);
		}
		sql.append(" order by score desc");
		return mapDataDao.queryPageBySQL(sql.toString(), 1, 10000);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String advanceAddCommit(Map<String, Object> param) throws Exception {
		/**
		 * 1、保存预审结果主表
		 * 2、保存预审机构
		 * 3、保存专家表
		 */
		String menuid = StringUtil.stringConvert(param.get("menuid"));
		Integer advanceid = saveOrUpdateAdvance(param,"1",StringUtil.stringConvert(param.get("advanceid")));
		param.put("advanceid", advanceid);
		saveOrgan(param);
		this.saveExpert(param);
		String workflowflag = param.get("workflowflag").toString();
		String wfkey = pwfc.getWfkeyByMenuid(menuid);
		String msg = "";//返回值
		if(workflowflag.equals("1")){//是否送审
			String wfid = StringUtil.stringConvert(param.get("wfid"));
			//没有发起过工作流
			if("".equals(wfid)){
				ResultMsg rsMsg = workflowManageComponent.startProcessByKeyAndPush(wfkey, new HashMap());
				if (rsMsg.isSuccess()) {
					//工作流启动后返回的id
					wfid = StringUtil.stringConvert(rsMsg.getBody().get("EXECID"));
					if("".equals(wfid)){//返回的wfid为空
						msg = "启动工作流时返回的wfid为空！";
					}else{
						//更新wfid和状态
						ProAdvanceResult pr = proAdvanceDao.get(advanceid);
						pr.setWfid(wfid);
						pr.setStatus("01");
						proAdvanceDao.update(pr);
					}
				}else {//工作流启用失败
					msg = rsMsg.getTitle();
				}
			}else {
			   //已经发起过工作流
				String activityId = StringUtil.stringConvert(param.get("activityId"));
				SysUser user = SecureUtil.getCurrentUser();
				String usercode = user.getUsercode();
				ResultMsg rsgMsg = workflowManageComponent.completeTask(wfid, activityId, new HashMap(), usercode, "");
				if(rsgMsg!=null&&!rsgMsg.isSuccess()){//处理失败
					msg = rsgMsg.getTitle();
				}
			}
		}
		return msg;
	}
	/**
	 * 保存预审结果主表
	 * @Title: saveOrUpdateAdvance 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @param datatype
	 * @param advanceid 如果为空则表示新增，否则表示修改
	 * @return
	 * @throws Exception 设定文件
	 */
	public Integer saveOrUpdateAdvance(Map<String, Object> param,String datatype,String advanceid) throws Exception{
		Integer returnInteger;
		SysUser user = SecureUtil.getCurrentUser();
		if("".equals(advanceid)){//新增
			ProAdvanceResult advance = new ProAdvanceResult();
			BeanUtils.populate(advance, param);
			advance.setCreateuser(user.getUserid().toString());//创建人
			advance.setCreatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//创建时间
			advance.setStatus("00");//保存状态
			advance.setDatatype(datatype);//新保存的
			advance.setOrgcode(user.getOrgcode());
			returnInteger = (Integer) proAdvanceDao.save(advance);
		}else {//修改
			ProAdvanceResult advance = proAdvanceDao.get(Integer.parseInt(advanceid));
			BeanUtils.populate(advance, param);
			advance.setUpdatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			advance.setUpdateuser(user.getUserid().toString());
			proAdvanceDao.update(advance);
			returnInteger = Integer.parseInt(advanceid);
		}
		return returnInteger;
	}
	/**
	 * 预审组织机构
	 * @Title: saveOrgan 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param 设定文件
	 */
	public void saveOrgan(Map<String, Object> param) {
		String projectid = StringUtil.stringConvert(param.get("projectid"));
		String advanceid = StringUtil.stringConvert(param.get("advanceid"));
		//如果projectid 和advanceid不为空，则删除原数据
		if(!"".equals(advanceid)&&!"".equals(projectid)){
			String where = "projectid = "+projectid+" and advanceid="+advanceid;
			proAdvanceDao.deleteBySQL("PRO_ADVANCE_ORGAN", where);
		}
		String advanceorgan = StringUtil.stringConvert(param.get("advanceorgan"));
		if(!"".equals(advanceorgan)){
			List list  = JSONArray.parseArray(advanceorgan);
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				map.remove("productid");
				map.put("projectid", projectid);//放入projectid
				map.put("advanceid", advanceid);//放入advanceid
			}
			proAdvanceDao.addBatchByList(list, "PRO_ADVANCE_ORGAN");
		}
	}
	/**
	 * 保存专家表
	 * @Title: saveExpert 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param map
	 * @param @throws Exception 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveExpert(Map map) throws Exception {
		String advanceExpertData = StringUtil.stringConvert(map.get("advanceExpertData"));
		String advanceid = StringUtil.stringConvert(map.get("advanceid"));
		List qualExpertList = (List) JSONObject.parse(advanceExpertData);
		ProProcurementExpertDao.deleteBySQL("Pro_Procurement_Expert t", "t.advanceid = "+advanceid+" and t.type='ADVANCE'");
		for(int i =0;i<qualExpertList.size();i++){
			ProProcurementExpert p = new ProProcurementExpert();
			BeanUtils.populate(p, (Map)qualExpertList.get(i));
			p.setAdvanceid(Integer.parseInt(advanceid));
			p.setType("ADVANCE");
			ProProcurementExpertDao.save(p);
		}
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String sendWorkFlow(String menuid, String advanceid,String activityId, String wfid) throws Exception {
		String wfkey = pwfc.getWfkeyByMenuid(menuid);
		String msg = "";
		ResultMsg rsgMsg ;
		if("".equals(StringUtil.stringConvert(wfid))||"null".equals(wfid)){
			 rsgMsg =	workflowManageComponent.startProcessByKeyAndPush(wfkey, new HashMap());
			 wfid = StringUtil.stringConvert(rsgMsg.getBody().get("EXECID"));
			 ProAdvanceResult pp = proAdvanceDao.get(Integer.valueOf(advanceid));
			 pp.setWfid(wfid);
			 pp.setStatus("01");
			 proAdvanceDao.update(pp);
		}else {
			SysUser user = SecureUtil.getCurrentUser();
			String usercode = user.getUsercode();
			 rsgMsg =workflowManageComponent.completeTask(wfid, activityId, new HashMap(), usercode, "");
		}
		if(rsgMsg!=null&&!rsgMsg.isSuccess()){//处理失败
			msg = rsgMsg.getTitle();
		}
		return msg;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String revokeWorkFlow(String wfid, String advanceid,String activityId) throws Exception {
		SysUser user = SecureUtil.getCurrentUser();
		String usercode = user.getUsercode();
		String returnMsg = "";
		ResultMsg  msg = workflowManageComponent.getBackWorkflow(wfid, activityId, usercode, null);
		 if(!msg.isSuccess()){//处理失败
			 returnMsg = msg.getTitle();
		}
		return returnMsg;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String auditWorkFlow(String advanceid, String activityId, String wfid,String isback,String opinion)
			throws Exception {
		String msg = "";
		ResultMsg rsgMsg ;
		SysUser user = SecureUtil.getCurrentUser();
		String usercode = user.getUsercode();
		if("1".equals(isback)){//退回
			rsgMsg = workflowManageComponent.sendBackWorkflow(wfid, activityId, new HashMap(), usercode, opinion);
		}else {
			rsgMsg =workflowManageComponent.completeTask(wfid, activityId, new HashMap(), usercode,opinion);
		}
		if(rsgMsg!=null&&!rsgMsg.isSuccess()){//处理失败
			msg = rsgMsg.getTitle();
		}
		return msg;
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
	public PaginationSupport advanceExpertGrid(Map map) throws AppException {
		String advanceid = map.get("advanceid")==null?"":map.get("advanceid").toString();
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
        sql.append("  where 1=1 and t.TYPE='ADVANCE' ");

		if("".equals(advanceid)){
			sql.append(" and 1<>1" );
		}else{
			sql.append(" and T.advanceid = "+advanceid );
		}
		
		System.out.println("【预审结果-专家sql：】"+sql.toString());
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
}
