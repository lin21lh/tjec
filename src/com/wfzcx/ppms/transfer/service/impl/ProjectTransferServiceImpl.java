package com.wfzcx.ppms.transfer.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

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
import com.wfzcx.ppms.transfer.dao.ProTransferDao;
import com.wfzcx.ppms.transfer.po.ProTransfer;
import com.wfzcx.ppms.transfer.service.ProjectTransferService;
import com.wfzcx.ppms.workflow.component.ProjectWorkFlowComponent;

/**
 * 项目移交service实现类
 * @ClassName: ProjectTransferServiceImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年9月24日16:08:16
 */
@Scope("prototype")
@Service("com.wfzcx.ppms.transfer.service.impl.ProjectTransferServiceImpl")
public class ProjectTransferServiceImpl implements ProjectTransferService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProjectWorkFlowComponent pwfc;
	@Autowired
	SysWorkflowManageComponent workflowManageComponent;
	@Autowired
	ProTransferDao proTransferDao;
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
		sql.append("           t.pro_trade, t.pro_perate,t.pro_return,t.pro_sendtime,");
		sql.append("           t.pro_sendtype,t.pro_sendperson,t.pro_situation,t.pro_person,");
		sql.append("           t.pro_phone,t.pro_scheme,t.pro_schemepath,t.pro_reportpath,");
		sql.append("           t.pro_conditionpath,t.pro_article,t.pro_articlepath,t1.wfid,");
		sql.append("           t1.status,t1.createuser,t1.createtime,t1.updateuser,t1.updatetime,");
		sql.append("           (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROTYPE' and a.status=0  and a.code=t.pro_type) pro_type_name,");
		sql.append("           (select a.name from SYS_YW_DICCODEITEM a where upper(a.elementcode)='PROTRADE' and a.status=0  and a.code=t.pro_trade) pro_trade_name,");
		sql.append("           (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROOPERATE' and a.status=0  and a.code=t.pro_perate) pro_perate_name,");
		sql.append("     (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PRORETURN' and a.status=0  and a.code=t.pro_return) pro_return_name,");
		sql.append("    (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSENDTYPE' and a.status=0  and a.code=t.pro_sendtype) pro_sendtype_name,");
		sql.append("     nvl((select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='WFSTATUS' and a.status=0  and a.code=t1.status),'未录入') status_name,");
		sql.append("           (select a.username from sys_user a where  a.userid=t1.createuser) createusername,");
		sql.append("           (select a.username from sys_user a where  a.userid=t1.updateuser) updateusername,");
		sql.append("           t1.transferid,t1.transfer_start_time,t1.transfer_end_time,t1.assess_time");
		sql.append("      from PRO_PROJECT t, PRO_TRANSFER t1");
		sql.append("     where t.projectid = t1.projectid(+)");

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
		String wfids = pwfc.findCurrentWfids(menuid, activityId, status, firstNode, lastNode,"t1");
		sql.append(" and ");
		sql.append(wfids);
		String audit =   param.get("audit")==null?"":param.get("audit").toString();
		if(!"1".equals(audit)){//录入环节
			SysUser user = SecureUtil.getCurrentUser();
			String orgcode = user.getOrgcode();
			//匹配组织机构和创建人
			sql.append(" and t1.orgcode(+) = '").append(orgcode).append("' ");
			sql.append(" and (t1.createuser = ").append(user.getUserid()).append(" or t1.createuser is null )");
			//暂定为在pro_prichase_result表中status为10的数据
			sql.append("  and exists (select 1 from pro_purchase_result rt where t.projectid= rt.projectid and rt.status='10') ");
		}
		sql.append("  order by t1.createtime desc,t.projectid,t1.transferid desc");

		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public String transferSave(Map<String, Object> param) throws Exception {
		/**
		 * 1.保存移交信息表
		 * 2、判断是否启用工作流
		 */
		String menuid = StringUtil.stringConvert(param.get("menuid"));
		Integer transferid = saveOrUpdateTransfer(param);
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
						ProTransfer pp = proTransferDao.get(transferid);
						pp.setWfid(wfid);
						pp.setStatus("01");
						proTransferDao.update(pp);
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
		return msg;
	}
	/**
	 * 保存或更新项目移交数据
	 * @Title: saveOrUpdateTransfer 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws Exception 设定文件
	 */
	public  Integer saveOrUpdateTransfer(Map<String, Object> param) throws Exception{
		Integer returnValue ;
		String transferid = StringUtil.stringConvert(param.get("transferid"));
		if ("".equals(transferid)) {
			ProTransfer transfer = new ProTransfer();
			BeanUtils.populate(transfer, param);
			SysUser user = SecureUtil.getCurrentUser();
			transfer.setCreateuser(user.getUserid().toString());//创建人
			transfer.setCreatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//创建时间
			transfer.setStatus("00");//保存状态
			transfer.setOrgcode(user.getOrgcode());
			returnValue = (Integer) proTransferDao.save(transfer);
		}else {
			ProTransfer transfer = proTransferDao.get(Integer.parseInt(transferid));
			BeanUtils.populate(transfer, param);
			SysUser user = SecureUtil.getCurrentUser();
			transfer.setUpdateuser(user.getUserid().toString());//修改人
			transfer.setUpdatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//修改时间
			proTransferDao.update(transfer);
			returnValue = Integer.parseInt(transferid);
		}
		return returnValue;
	}
	@Override
	public List queryTransfer(String transferid) throws Exception {
		String sql = "select * from pro_transfer where transferid = "+transferid;
		return 	mapDataDao.queryListBySQLForConvert(sql);
	}
	@Override
	public String revokeWorkFlow(String wfid, String transferid,String activityId) throws Exception {
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
	public String sendWorkFlow(String menuid, String transferid,String activityId, String wfid) throws Exception {
		String wfkey = pwfc.getWfkeyByMenuid(menuid);
		String msg = "";
		ResultMsg rsgMsg ;
		if("".equals(StringUtil.stringConvert(wfid))||"null".equals(wfid)){
			 rsgMsg =	workflowManageComponent.startProcessByKeyAndPush(wfkey, new HashMap());
			 wfid = StringUtil.stringConvert(rsgMsg.getBody().get("EXECID"));
			 ProTransfer pp = proTransferDao.get(Integer.valueOf(transferid));
			 pp.setWfid(wfid);
			 pp.setStatus("01");
			 proTransferDao.update(pp);
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
	public String auditWorkFlow(String transferid, String activityId, String wfid,String isback,String opinion)
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
}
