package com.wfzcx.ppms.discern.service.impl;

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
import com.jbf.workflow.vo.HistoryOpinionVO;
import com.wfzcx.ppms.discern.dao.ProApproveDao;
import com.wfzcx.ppms.discern.dao.ProProjectDao;
import com.wfzcx.ppms.discern.po.ProApprove;
import com.wfzcx.ppms.discern.po.ProProject;
import com.wfzcx.ppms.discern.service.ProjectDiscernService;
import com.wfzcx.ppms.workflow.component.ProjectWorkFlowComponent;

/**
 * 项目申报service实现类
 * @ClassName: ProjectDiscernServiceImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年9月12日 上午8:22:07
 */
@Scope("prototype")
@Service("com.wfzcx.ppms.discern.service.impl.ProjectDiscernServiceImpl")
public class ProjectDiscernServiceImpl implements ProjectDiscernService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProProjectDao proProjectDao;
	@Autowired
	ProApproveDao proApproveDao;
	@Autowired
	ProjectWorkFlowComponent pwfc;
	@Autowired
	SysWorkflowManageComponent workflowManageComponent;
	@Autowired
	EasyUISumRowUtil easyUISumRowUtil;
	
	@Override
	public PaginationSupport queryProject(Map<String, Object> param)   throws AppException{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select projectid,datatype, pro_name,pro_type,amount, pro_year,");
		sql.append("       pro_trade, pro_perate,pro_return,pro_sendtime,");
		sql.append("       pro_sendtype,pro_sendperson,pro_situation,pro_person,");
		sql.append("       pro_phone,pro_scheme,pro_schemepath,pro_reportpath,");
		sql.append("       pro_conditionpath,pro_article,pro_articlepath,wfid,");
		sql.append("       status,createuser,createtime,updateuser,updatetime,IMPLEMENT_ORGAN,IMPLEMENT_PERSON,IMPLEMENT_PHONE,GOVERNMENT_PATH,cz_result,vfm_pjhj,opinion,");
		sql.append("       (select a.name from SYS_DEPT a where a.status=0  and a.code=t.implement_organ) implement_organ_name,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='APPROVERESULT' and a.status=0  and a.code=t.cz_result) cz_result_name,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROVFMPJHJ' and a.status=0  and a.code=t.VFM_PJHJ) VFM_PJHJ_NAME,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROTYPE' and a.status=0  and a.code=t.pro_type) pro_type_name,");
		sql.append("       (select a.name from SYS_YW_DICCODEITEM a where upper(a.elementcode)='PROTRADE' and a.status=0  and a.code=t.pro_trade) pro_trade_name,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROOPERATE' and a.status=0  and a.code=t.pro_perate) pro_perate_name,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PRORETURN' and a.status=0  and a.code=t.pro_return) pro_return_name,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSENDTYPE' and a.status=0  and a.code=t.pro_sendtype) pro_sendtype_name,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSFXM' and a.status=0  and a.code=t.sfxm) sfxm_name,sfxm,");
		sql.append("(select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.tjxm) tjxm_name,tjxm,");
		sql.append("(select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.sqbt) sqbt_name,sqbt,");
		sql.append(" btje,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='WFSTATUS' and a.status=0  and a.code=t.status) status_name,");
		sql.append("       (select a.username from sys_user a where  a.userid=t.createuser) createusername,");
		sql.append("       (select a.username from sys_user a where  a.userid=t.updateuser) updateusername ");
		sql.append("  from pro_project t where 1=1 ");
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
		String wfids = pwfc.findCurrentWfids(menuid, activityId, status, firstNode, lastNode);
		sql.append(" and ");
		sql.append(wfids);
		String audit =   param.get("audit")==null?"":param.get("audit").toString();
		if(!"1".equals(audit)){
			SysUser user = SecureUtil.getCurrentUser();
			String orgcode = user.getOrgcode();
			//匹配组织机构和创建人
			sql.append(" and t.orgcode = '").append(orgcode).append("' ");
			sql.append(" and t.createuser = ").append(user.getUserid());
		}
		sql.append("  order by t.projectid desc");

		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String projectAddCommit(Map<String, Object> param) throws Exception{
		/**
		 * 1、保存项目主表
		 * 2、保存项目产出物表
		 */
		String menuid = StringUtil.stringConvert(param.get("menuid"));
		Integer projectidInteger = saveProject(param,"1");
		param.put("projectid", projectidInteger);
		saveProduct(param);
		String workflowflag = param.get("workflowflag").toString();
		String wfkey = pwfc.getWfkeyByMenuid(menuid);
		String msg = "";//返回值
		if(workflowflag.equals("1")){//是否送审
			ResultMsg rsMsg = workflowManageComponent.startProcessByKeyAndPush(wfkey, new HashMap());
			if (rsMsg.isSuccess()) {
				//工作流启动后返回的id
				String wfid = StringUtil.stringConvert(rsMsg.getBody().get("EXECID"));
				if("".equals(wfid)){//返回的wfid为空
					msg = "启动工作流时返回的wfid为空！";
				}else{
					//更新wfid和状态
					ProProject pp = proProjectDao.get(projectidInteger);
					pp.setWfid(wfid);
					pp.setStatus("01");
					proProjectDao.update(pp);
				}
			}else {//工作流启用失败
				msg = rsMsg.getTitle();
			}
		}
		if (!"".equals(msg)) {
			throw new Exception(msg);
		}
		return msg;
	}
	/**
	 * 项目主表保存(pro_project)
	 * @Title: saveProject 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @datatype 数据类型 1:新增，2：变更
	 * @return
	 * @throws Exception 设定文件
	 */
	public  Integer saveProject(Map<String, Object> param,String datatype) throws Exception{
		ProProject project = new ProProject();
		BeanUtils.populate(project, param);
		SysUser user = SecureUtil.getCurrentUser();
		project.setCreateuser(user.getUserid().toString());//创建人
		project.setCreatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//创建时间
		project.setXmdqhj("1");//项目当前环节
		project.setStatus("00");//保存状态
		project.setDatatype(datatype);//新保存的
		project.setOrgcode(user.getOrgcode());
		return (Integer) proProjectDao.save(project);
	}
	/**
	 * 
	 * @Title: saveProduct 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param 设定文件
	 */
	public void saveProduct(Map<String, Object> param) {
		String projectduct = StringUtil.stringConvert(param.get("projectduct"));
		if(!"".equals(projectduct)){
			List list  = JSONArray.parseArray(projectduct);
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				map.remove("productid");
				map.put("projectid", param.get("projectid"));//放入projectid
			}
			proProjectDao.addBatchByList(list, "PRO_PRODUCT");
		}
	}
	@Override
	public PaginationSupport queryProduct(String projectid) throws AppException {
		StringBuffer sql = new StringBuffer();
		if("".equals(projectid)){
			sql.append("select * from PRO_PRODUCT where 1<>1");
		}else{
			sql.append("select * from PRO_PRODUCT where projectid= ");
			sql.append(projectid);
		}
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), 1, 10000);
	}
	@Override
	public SysResource getResourceById(String menuid) throws Exception {
		return pwfc.getResourceById(menuid);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String projectEditCommit(Map<String, Object> param) throws Exception {
		/**
		 * 1、保存项目主表
		 * 2、删除项目产出物表
		 * 3、保存项目产出物表
		 */
		String menuid = StringUtil.stringConvert(param.get("menuid"));
		updateProject(param,"1");
		String projectid = StringUtil.stringConvert(param.get("projectid"));
		deleteProduct(projectid);
		saveProduct(param);
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
						ProProject pp = proProjectDao.get(Integer.valueOf(projectid));
						pp.setWfid(wfid);
						pp.setStatus("01");
						proProjectDao.update(pp);
					}
				}else {//工作流启用失败
					msg = rsMsg.getTitle();
				}
			}else{
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
		if (!"".equals(msg)) {
			throw new Exception(msg);
		}
		return msg;
	}
	/**
	 * 更新项目信息表
	 * @Title: updateProject 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @param datatype
	 * @throws Exception 设定文件
	 */
	public  void updateProject(Map<String, Object> param,String datatype) throws Exception{
		ProProject project = proProjectDao.get(Integer.parseInt(StringUtil.stringConvert(param.get("projectid"))));
		BeanUtils.populate(project, param);
		SysUser user = SecureUtil.getCurrentUser();
		project.setUpdateuser(user.getUserid().toString());//创建人
		project.setUpdatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//创建时间
		project.setStatus("00");//保存状态
		proProjectDao.update(project);
	}
	/**
	 * 删除项目产出物
	 * @Title: deleteProduct 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid 设定文件
	 */
	public void deleteProduct(String projectid){
		String where = "projectid = "+projectid;
		proProjectDao.deleteBySQL("PRO_PRODUCT", where);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void projectDelete(String projectid) throws AppException {
		deleteProject(projectid);
		deleteProduct(projectid);
	}
	public void deleteProject(String projectid){
		String where = "projectid = "+projectid;
		proProjectDao.deleteBySQL("PRO_PROJECT", where);
	}
	@Override
	public String sendWorkFlow(String menuid, String projectid,String activityId, String wfid) throws Exception {
		String wfkey = pwfc.getWfkeyByMenuid(menuid);
		String msg = "";
		ResultMsg rsgMsg ;
		if("".equals(StringUtil.stringConvert(wfid))||"null".equals(wfid)){
			 rsgMsg =	workflowManageComponent.startProcessByKeyAndPush(wfkey, new HashMap());
			 wfid = StringUtil.stringConvert(rsgMsg.getBody().get("EXECID"));
			 ProProject pp = proProjectDao.get(Integer.valueOf(projectid));
			 pp.setWfid(wfid);
			 pp.setStatus("01");
			 proProjectDao.update(pp);
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
	public String revokeWorkFlow(String wfid, String projectid,String activityId) throws Exception {
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
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String auditCommit(Map<String, Object> param) throws Exception {
		/**
		 * 1、保存物有所值验证和财政能力验证 等PRO_APPROVE
		 * 2、保存第三方评审机构信息
		 * 3、保存财政预算支出信息
		 * 4、判断是否推送工作流
		 */
		String returnMsg = "";
		//保存PRO_APPROVE
		saveUpdateApprove(param);
		
		//保存第三方评审机构PRO_APPRORGAN
		saveApprorgan(param);
		//保存财政预算支出信息
		saveBudget(param, "1");
		String workflowflag = StringUtil.stringConvert(param.get("workflowflag"));
		if("1".equals(workflowflag)){
			String wfid = StringUtil.stringConvert(param.get("wfid"));
			String activityId = StringUtil.stringConvert(param.get("activityId"));
			String czOpinion = StringUtil.stringConvert(param.get("czOpinion"));
			SysUser user = SecureUtil.getCurrentUser();
			String usercode = user.getUsercode();
			ResultMsg rsMsg = workflowManageComponent.completeTask(wfid, activityId, new HashMap(), usercode, czOpinion);
			 if(!rsMsg.isSuccess()){//处理失败
				 returnMsg = rsMsg.getTitle();
			}
		}
		if (!"".equals(returnMsg)) {
			throw new Exception(returnMsg);
		}
		return returnMsg;
	}
	/**
	 * 保存物有所值验证和财政能力验证 等
	 * @Title: saveUpdateApprove 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @throws Exception 设定文件
	 */
	public void saveUpdateApprove(Map<String, Object> param) throws Exception{
		ProApprove approve = new ProApprove();
		SysUser user = SecureUtil.getCurrentUser();
		String vomid = StringUtil.stringConvert(param.get("vomid"));
		if("".equals(vomid)){//保存
			BeanUtils.populate(approve, param);
			approve.setCreateuser(user.getUserid().toString());//创建人
			approve.setCreatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//创建时间
			proApproveDao.save(approve);
		}else {//修改
			approve = proApproveDao.get(Integer.parseInt(vomid));
			BeanUtils.populate(approve, param);
			approve.setUpdateuser(user.getUserid().toString());//创建人
			approve.setUpdatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//创建时间
			proApproveDao.update(approve);
		}
	}
	/**
	 * 保存第三方评审机构
	 * @Title: saveApprorgan 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param 设定文件
	 */
	public void saveApprorgan(Map<String, Object> param) {
		String thirdGridList = StringUtil.stringConvert(param.get("thirdGridList"));
		if(!"".equals(thirdGridList)){
			List list  = JSONArray.parseArray(thirdGridList);
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				map.remove("approrganid");
				map.put("projectid", param.get("projectid"));//放入projectid
			}
			String where = "projectid = "+param.get("projectid");
			//先删除原先数据
			proProjectDao.deleteBySQL("PRO_APPRORGAN", where);
			//再保存页面数据
			proProjectDao.addBatchByList(list, "PRO_APPRORGAN");
		}
	}
	/**
	 * 保存财政预算支出
	 * @Title: saveBudget 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @param budgetType 设定文件
	 */
	public void saveBudget(Map<String, Object> param,String budgetType) {
		String financeGridList = StringUtil.stringConvert(param.get("financeGridList"));
		if(!"".equals(financeGridList)){
			List list  = JSONArray.parseArray(financeGridList);
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				map.remove("budgetid");
				map.put("projectid", param.get("projectid"));//放入projectid
				map.put("budget_type", budgetType);//放入projectid
			}
			String where = "projectid = "+param.get("projectid");
			//先删除原先数据
			proProjectDao.deleteBySQL("PRO_BUDGET", where);
			//再保存页面数据
			proProjectDao.addBatchByList(list, "PRO_BUDGET");
		}
	}
	@Override
	public List queryApprove(String projectid) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.*,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='JUDGERESULT' and a.status=0  and a.code=t.vom_result) vom_result_name,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='JUDGERESULT' and a.status=0  and a.code=t.fc_result) fc_result_name,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='APPROVERESULT' and a.status=0  and a.code=t.cz_result) cz_result_name");
		sql.append(" from pro_approve t where projectid = ").append(projectid);
		return 	mapDataDao.queryListBySQLForConvert(sql.toString());
	}
	@Override
	public String auditOperate(Map<String, Object> param) throws Exception {
		String returnMsg = "";
		String wfid = StringUtil.stringConvert(param.get("wfid"));
		String activityId = StringUtil.stringConvert(param.get("activityId"));
		String opinion = StringUtil.stringConvert(param.get("opinion"));
		SysUser user = SecureUtil.getCurrentUser();
		String usercode = user.getUsercode();
		ResultMsg rsMsg = workflowManageComponent.sendBackWorkflow(wfid, activityId, new HashMap(), usercode, opinion);
		 if(!rsMsg.isSuccess()){//处理失败
			 returnMsg = rsMsg.getTitle();
		}
		 return returnMsg;
	}
	@Override
	public List<HistoryOpinionVO> getworkFlowList(String wfid) {
		return  workflowManageComponent.getWorkflowHistoryOpinions(wfid, "asc");
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateXmdqhj(String projectid,String xmdqhj) throws Exception {
		String sqlString ="update pro_project set xmdqhj='" + xmdqhj + "' where projectid=" + projectid;
		mapDataDao.updateTX(sqlString);
	}
}
