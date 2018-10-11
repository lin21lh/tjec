package com.wfzcx.ppp.xmcb.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.base.filemanage.component.FileManageComponent;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.component.SysWorkflowManageComponent;
import com.wfzcx.ppms.workflow.component.ProjectWorkFlowComponent;
import com.wfzcx.ppp.xmcb.dao.ProjectXmcbDao;
import com.wfzcx.ppp.xmcb.po.TXmxx;

@Scope("prototype")
@Service("com.wfzcx.ppp.xmcb.service.ProjectXmcbService")
public class ProjectXmcbService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProjectXmcbDao xmcbDao;
	@Autowired
	ProjectWorkFlowComponent pwfc;
	@Autowired
	SysWorkflowManageComponent workflowManageComponent;
	@Autowired
	ParamCfgComponent pcfg;
	@Autowired
	FileManageComponent fileManageComponent;
	/**
	 * 项目查询（项目申报、初步识别、发改委识别共用）
	 * @Title: queryProject 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws AppException 设定文件
	 */
	public PaginationSupport queryProject(Map<String, Object> param)   throws AppException{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();

		sql.append(" select xmid, sjlx, xmmc, xmlx, xmzje, hznx, sshy, yzfs, hbjz, fqsj,");
		sql.append(" xmdqhj, wfid, orgcode, xmcbk, xmkfk, fqlx, fqrmc, xmgk, xmlxr,");
		sql.append(" lxrdh, cbssfa, ssfafj, spjg, sftjxm, sfxm, sfsqbt, btje, spyj, xmzt,");
		sql.append(" cjr, cjsj, xgr, xgsj, sjxmxh, ghsjfafj, zfzyzc, ssxq,dqzt,");
		sql.append(" (select a.name from SYS_DEPT a  where a.code=t.orgcode) orgname,");
		sql.append("    (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROTYPE' and a.status=0  and a.code=t.xmlx) xmlx_mc,");
		sql.append("    (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.zfzyzc) zfzyzc_mc,");
		sql.append("    (select a.name from SYS_YW_DICCODEITEM a where upper(a.elementcode)='PROTRADE' and a.status=0  and a.code=t.sshy) sshy_mc,");
		sql.append("     (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROOPERATE' and a.status=0  and a.code=t.yzfs) yzfs_mc,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PRORETURN' and a.status=0  and a.code=t.hbjz) hbjz_mc,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSENDTYPE' and a.status=0  and a.code=t.fqlx) fqlx_mc,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSFXM' and a.status=0  and a.code=t.sfxm) sfxm_name,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.SFTJXM) SFTJXM_mc,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.SFSQBT) SFSQBT_mc,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='WFSTATUS' and a.status=0  and a.code=t.dqzt) dqzt_name,");
		sql.append("     (select a.username from sys_user a where  a.userid=t.cjr) cjr_mc,");
		sql.append("     (select a.username from sys_user a where  a.userid=t.xgr) xgr_mc");
		sql.append(" from t_xmxx t where 1=1 ");

		String menuid = StringUtil.stringConvert(param.get("menuid"));
		String activityId = StringUtil.stringConvert(param.get("activityId"));
		//项目名称
		String xmmc = StringUtil.stringConvert(param.get("xmmc"));
		if(!"".equals(xmmc)){
			sql.append(" and t.xmmc like '%").append(xmmc).append("%'");
		}
		//所属行业
		String sshy = StringUtil.stringConvert(param.get("sshy"));
		if(!"".equals(sshy)){
			sql.append(" and t.sshy in ('").append(sshy.replaceAll(",", "','")).append("')");
		}
		//运作方式
		String yzfs = StringUtil.stringConvert(param.get("yzfs"));
		if(!"".equals(yzfs)){
			sql.append(" and t.yzfs in ('").append(yzfs.replaceAll(",", "','")).append("')");
		}
		//回报机制
		String hbjz = StringUtil.stringConvert(param.get("hbjz"));
		if(!"".equals(hbjz)){
			sql.append(" and t.hbjz in ('").append(hbjz.replaceAll(",", "','")).append("')");
		}
		//发起类型
		String fqlx = StringUtil.stringConvert(param.get("fqlx"));
		if(!"".equals(fqlx)){
			sql.append(" and t.fqlx in ('").append(fqlx.replaceAll(",", "','")).append("')");
		}
		//项目类型
		String xmlx = StringUtil.stringConvert(param.get("xmlx"));
		if(!"".equals(xmlx)){
			sql.append(" and t.xmlx in ('").append(xmlx.replaceAll(",", "','")).append("')");
		}
		//项目联系人
		String xmlxr = StringUtil.stringConvert(param.get("xmlxr"));
		if(!"".equals(xmlxr)){
			sql.append(" and t.xmlxr like  '%").append(xmlxr).append("%'");
		}
		String status = StringUtil.stringConvert(param.get("status"));
		//获取工作流状态
		boolean firstNode = param.get("firstNode")==null?false: Boolean.parseBoolean(param.get("firstNode").toString());
		boolean lastNode =   param.get("lastNode")==null?false: Boolean.parseBoolean(param.get("lastNode").toString());
		String wfids = pwfc.findCurrentWfids(menuid, activityId, status, firstNode, lastNode);
		sql.append(" and ");
		sql.append(wfids);
		sql.append("  order by t.xmid desc");
		System.out.println("sql--"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	/**
	 * 新增保存送审
	 * @Title: projectAddCommit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String projectAddCommit(Map<String, Object> param) throws Exception{
		/**
		 * 1、保存项目主表
		 */
		String menuid = StringUtil.stringConvert(param.get("menuid"));
		Integer projectidInteger = saveProject(param,"1");
		//项目保存完成后，将项目id更新至附件中的keyid中
		String fjkeyid = StringUtil.stringConvert(param.get("fjkeyid"));
		String updFjKeySql =" update sys_filemanage t set t.keyid='"+projectidInteger+"' where t.keyid='"+fjkeyid+"' ";
		xmcbDao.updateBySql(updFjKeySql);
		String workflowflag = param.get("workflowflag").toString();
		String msg = "";//返回值
		if(workflowflag.equals("1")){//是否送审
			String wfkey = pwfc.getWfkeyByMenuid(menuid);
			ResultMsg rsMsg = workflowManageComponent.startProcessByKeyAndPush(wfkey, new HashMap());
			if (rsMsg.isSuccess()) {
				//工作流启动后返回的id
				String wfid = StringUtil.stringConvert(rsMsg.getBody().get("EXECID"));
				if("".equals(wfid)){//返回的wfid为空
					msg = "启动工作流时返回的wfid为空！";
				}else{
					//更新wfid和状态
					TXmxx xmxx  = xmcbDao.get(projectidInteger);
					xmxx.setWfid(wfid);
					xmxx.setDqzt("01");
					xmcbDao.update(xmxx);
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
	 * 保存项目信息
	 * @Title: saveProject 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @param sjlx
	 * @return
	 * @throws Exception 设定文件
	 */
	public  Integer saveProject(Map<String, Object> param,String sjlx) throws Exception{
		TXmxx xmxx = new TXmxx();
		BeanUtils.populate(xmxx, param);
		SysUser user = SecureUtil.getCurrentUser();
		xmxx.setCjr(user.getUserid().toString());//创建人
		xmxx.setCjsj(DateUtil.getDate("yyyy-MM-dd HH:mm:ss"));//创建时间
		xmxx.setDqzt("00");//保存状态
		xmxx.setSjlx(sjlx);//新保存的
		xmxx.setXmcbk("0");//项目储备库，默认是0未进入项目储备库
		xmxx.setOrgcode(user.getOrgcode());
		return (Integer) xmcbDao.save(xmxx);
	}
	/**
	 * 项目送审
	 * @Title: sendWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param xmid
	 * @param activityId
	 * @param wfid
	 * @return
	 * @throws Exception 设定文件
	 */
	public String sendWorkFlow(String menuid, String xmid,String activityId, String wfid,Map map) throws Exception {
		String wfkey = pwfc.getWfkeyByMenuid(menuid);
		String msg = "";
		ResultMsg rsgMsg ;
		if("".equals(StringUtil.stringConvert(wfid))||"null".equals(wfid)){
			 rsgMsg =	workflowManageComponent.startProcessByKeyAndPush(wfkey, new HashMap());
			 wfid = StringUtil.stringConvert(rsgMsg.getBody().get("EXECID"));
			 TXmxx pp = xmcbDao.get(Integer.valueOf(xmid));
			 pp.setWfid(wfid);
			 pp.setDqzt("01");
			 xmcbDao.update(pp);
		}else {
			//审批意见
			String spyj = StringUtil.stringConvert(map.get("spyj"));
			SysUser user = SecureUtil.getCurrentUser();
			String usercode = user.getUsercode();
			String outcome = "";
			rsgMsg =workflowManageComponent.completeTask(wfid, activityId, outcome, new HashMap(), usercode, spyj);
			String lastNode = StringUtil.stringConvert(map.get("lastNode"));
			//如果是最终环节，审核同意，则将项目储备库标识置为1
			 if(lastNode.equals("true")){
				 TXmxx pp = xmcbDao.get(Integer.valueOf(xmid));
				 pp.setXmcbk("1");
				 xmcbDao.update(pp);
			 }
		}
		if(rsgMsg!=null&&!rsgMsg.isSuccess()){//处理失败
			msg = rsgMsg.getTitle();
		}
		return msg;
	}
	public SysResource getResourceById(String menuid) throws Exception {
		return pwfc.getResourceById(menuid);
	}
	/**
	 * 工作流审批
	 * @Title: sendWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String sendWorkFlow(Map map) throws Exception {
		String msg = "";
		String xmid = map.get("xmid") == null ? "":map.get("xmid").toString();
		String wfid = map.get("wfid") == null ? "":map.get("wfid").toString();
		String activityId = map.get("activityId") == null ? "":map.get("activityId").toString();
		String spyj = map.get("spyj") == null ? "":map.get("spyj").toString();
		String zzfyzc = map.get("zzfyzc") == null ? "":map.get("zzfyzc").toString();
		ResultMsg rsgMsg = null;
		String[] projectids = xmid.split(",");
		String[] wfids = wfid.split(",");
		for(int j=0;j<projectids.length;j++){
			SysUser user = SecureUtil.getCurrentUser();
			String usercode = user.getUsercode();
			TXmxx xmxx = xmcbDao.get(Integer.parseInt(xmid));
			xmxx.setZfzyzc(zzfyzc);
			xmxx.setSpyj(spyj);//审批意见
			xmcbDao.update(xmxx);
			rsgMsg =workflowManageComponent.completeTask(wfids[j], activityId, new HashMap(), usercode, spyj);
			if(rsgMsg!=null&&!rsgMsg.isSuccess()){//处理失败
				msg = rsgMsg.getTitle();
				throw new Exception(msg);
			}
		}
		return msg;
	}
	/**
	 * 项目初步识别
	 * @Title: auditWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param xmid
	 * @param activityId
	 * @param wfid
	 * @param map
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String auditWorkFlow(String xmid,String activityId, String wfid,Map map) throws Exception {
		String msg = "";
		if("".equals(StringUtil.stringConvert(wfid))||"null".equals(wfid)){
			throw new AppException("工作流id未传送到后台！");
		}
		//往项目信息表中保存是否资源支出以及审批意见
		String spyj = map.get("spyj") == null ? "":map.get("spyj").toString();
		String zfzyzc = map.get("zfzyzc") == null ? "":map.get("zfzyzc").toString();
		TXmxx xmxx = xmcbDao.get(Integer.parseInt(xmid));
		xmxx.setZfzyzc(zfzyzc);
		xmxx.setSpyj(spyj);//审批意见
		xmcbDao.update(xmxx);
		//工作流推进
		ResultMsg rsgMsg ;
		SysUser user = SecureUtil.getCurrentUser();
		String usercode = user.getUsercode();
		HashMap vmap = new HashMap();
		vmap.put("zzfyzc", zfzyzc);
		rsgMsg =workflowManageComponent.completeTask(wfid, activityId,new HashMap(), usercode, spyj);
		//是否涉及政府资源支出，当结果时1是，表示涉及政府资源支出，则流程直接结束；当结果时0是，表示不涉及政府资源支出，则流程发送至下一节点发改委识别
		//处理分支在，WFxmsbfzHandler类中实现
		if(rsgMsg!=null&&!rsgMsg.isSuccess()){//处理失败
			msg = rsgMsg.getTitle();
		}
		return msg;
	}
	/**
	 * 新增保存送审
	 * @Title: projectAddCommit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String projectEditCommit(Map<String, Object> param) throws Exception{
		/**
		 * 1、保存项目主表
		 */
		String menuid = StringUtil.stringConvert(param.get("menuid"));
		String xmid = StringUtil.stringConvert(param.get("xmid"));
		if("".equals(StringUtil.stringConvert(xmid))||"null".equals(xmid)){
			throw new AppException("项目id未传送到后台！");
		}
		TXmxx xmxx = xmcbDao.get(Integer.parseInt(xmid));
		BeanUtils.populate(xmxx, param);
		SysUser user = SecureUtil.getCurrentUser();
		xmxx.setXgr(user.getUserid().toString());//创建人
		xmxx.setXgsj(DateUtil.getDate("yyyy-MM-dd HH:mm:ss"));//创建时间
		xmxx.setDqzt("00");//保存状态
		xmcbDao.update(xmxx);
		String workflowflag = param.get("workflowflag").toString();
		String msg = "";//返回值
		if(workflowflag.equals("1")){//是否送审
			String wfkey = pwfc.getWfkeyByMenuid(menuid);
			String wfid = StringUtil.stringConvert(param.get("wfid"));
			if("".equals(wfid)){
				ResultMsg rsMsg = workflowManageComponent.startProcessByKeyAndPush(wfkey, new HashMap());
				if (rsMsg.isSuccess()) {
					//工作流启动后返回的id
					wfid = StringUtil.stringConvert(rsMsg.getBody().get("EXECID"));
					if("".equals(wfid)){//返回的wfid为空
						msg = "启动工作流时返回的wfid为空！";
					}else{
						//更新wfid和状态
						xmxx.setWfid(wfid);
						xmxx.setDqzt("01");
						xmcbDao.update(xmxx);
					}
				}else {//工作流启用失败
					msg = rsMsg.getTitle();
				}
			}else{
				 //已经发起过工作流
				String activityId = StringUtil.stringConvert(param.get("activityId"));
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
	 * 项目信息删除
	 * @Title: projectDelete 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param xmid
	 * @throws AppException 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void projectDelete(String xmid) throws AppException {
		//删除项目信息
		deleteProject(xmid);
		//删除相关附件
		fileManageComponent.deleteFilesByKeyId(xmid);
	}
	public void deleteProject(String xmid){
		String where = "xmid = "+xmid;
		xmcbDao.deleteBySQL("T_XMXX", where);
	}
	/**
	 * 项目撤回
	 * @Title: revokeWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid
	 * @param xmid
	 * @param activityId
	 * @return
	 * @throws Exception 设定文件
	 */
	public String revokeWorkFlow(String wfid, String xmid,String activityId) throws Exception {
		SysUser user = SecureUtil.getCurrentUser();
		String usercode = user.getUsercode();
		String returnMsg = "";
		ResultMsg  msg = workflowManageComponent.getBackWorkflow(wfid, activityId, usercode, null);
		 if(!msg.isSuccess()){//处理失败
			 returnMsg = msg.getTitle();
		}
		return returnMsg;
	}
	/**
	 * 审批退回
	 * @Title: backWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String backWorkFlow(Map<String, Object> param) throws Exception {
		String returnMsg = "";
		ResultMsg rsMsg = null;
		String wfid = StringUtil.stringConvert(param.get("wfid"));
		String activityId = StringUtil.stringConvert(param.get("activityId"));
		String spyj = StringUtil.stringConvert(param.get("spyj"));
		SysUser user = SecureUtil.getCurrentUser();
		String usercode = user.getUsercode();
		rsMsg = workflowManageComponent.sendBackWorkflow(wfid, activityId, new HashMap(), usercode, spyj);
		 if(!rsMsg.isSuccess()){//处理失败
			 returnMsg = rsMsg.getTitle();
			 throw new Exception(returnMsg);
		}
	 return returnMsg;
	}
}
