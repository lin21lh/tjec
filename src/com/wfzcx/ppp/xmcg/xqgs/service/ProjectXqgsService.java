package com.wfzcx.ppp.xmcg.xqgs.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.wfzcx.ppp.xmcg.xqgs.dao.ProjectXqgsDao;
import com.wfzcx.ppp.xmcg.xqgs.po.TCgXqgs;

@Scope("prototype")
@Service("com.wfzcx.ppp.xmcg.xqgs.service.ProjectXqgsService")
public class ProjectXqgsService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProjectXmcbDao xmcbDao;
	@Autowired
	ProjectXqgsDao xqgsDao;
	@Autowired
	ProjectWorkFlowComponent pwfc;
	@Autowired
	SysWorkflowManageComponent workflowManageComponent;
	@Autowired
	ParamCfgComponent pcfg;
	@Autowired
	FileManageComponent fileManageComponent;
	
	/**
	 * 项目分页查询
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryProject(Map<String, Object> param)   throws AppException{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();

		sql.append(" select t.xmid, sjlx, xmmc, xmlx, xmzje, hznx, sshy, yzfs, hbjz, fqsj,");
		sql.append(" xmdqhj, c.wfid, orgcode, xmcbk, xmkfk, fqlx, fqrmc, xmgk, xmlxr,");
		sql.append(" t.lxrdh xmlxrdh, cbssfa, ssfafj, spjg, sftjxm, sfxm, sfsqbt, btje, spyj, xmzt,");
		sql.append(" t.cjr, t.cjsj, t.xgr, t.xgsj, sjxmxh, ghsjfafj, zfzyzc, ssxq,");
		sql.append("    (select a.name from SYS_YW_DICCODEITEM a where upper(a.elementcode)='PROTRADE' and a.status=0  and a.code=t.sshy) sshy_mc,");
		sql.append("     (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROOPERATE' and a.status=0  and a.code=t.yzfs) yzfs_mc,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PRORETURN' and a.status=0  and a.code=t.hbjz) hbjz_mc,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSENDTYPE' and a.status=0  and a.code=t.fqlx) fqlx_mc,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSFXM' and a.status=0  and a.code=t.sfxm) sfxm_name,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.SFTJXM) SFTJXM_mc,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.SFSQBT) SFSQBT_mc,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='WFSTATUS' and a.status=0  and a.code=t.xmzt) xmzt_name,");
		sql.append("     (select a.username from sys_user a where  a.userid=t.cjr) cjr_mc,");
		sql.append("     (select a.username from sys_user a where  a.userid=t.xgr) xgr_mc,");
		sql.append(" c.xqgsid, c.cgr, c.cgrdz, c.cglxr, c.cgrlxfs, c.cgxmmc, c.cgpmdm, c.cgpmmc, c.cgksrq, c.cgjsrq,c.gsts,c.bz");
		
		sql.append(" from t_xmxx t, t_cg_xqgs c where t.xmid = c.xmid(+)  ");

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
		String wfids = pwfc.findCurrentWfids(menuid, activityId, status, firstNode, lastNode,"c");
		sql.append(" and ");
		sql.append(wfids);
		sql.append("  order by t.xmid desc");
		System.out.println("sql--"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	/**
	 * 保存需求公示信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String projectXqgsAddCommit(Map<String, Object> param) throws Exception{
		/**
		 * 1、保存项目需求公示信息表
		 */
		String menuid = StringUtil.stringConvert(param.get("menuid"));
		String workflowflag = param.get("workflowflag").toString();
		Integer idInteger = saveProjectXqgs(param,workflowflag);
		//需求公示信息保存完成后，将公示信息id更新至附件中的keyid中
		String fjkeyid = StringUtil.stringConvert(param.get("fjkeyid"));
		String updFjKeySql =" update sys_filemanage t set t.keyid='"+idInteger+"' where t.keyid='"+fjkeyid+"' ";
		xqgsDao.updateBySql(updFjKeySql);
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
					String xqgsid = StringUtil.stringConvert((String)param.get("xqgsid"));
					if(!xqgsid.equals("")){
						TCgXqgs xqgs = xqgsDao.get(Integer.parseInt(xqgsid));
						if(xqgs != null) {							
							xqgs.setWfid(wfid);
							xqgs.setDqzt("1");//状态：已送审
							xqgsDao.update(xqgs);
						}
					}
	
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
	 * 保存项目需求公示信息
	 * @Title: saveProjectCgxx 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @param workflowflag
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public  Integer saveProjectXqgs(Map<String, Object> param,String workflowflag) throws Exception{
		TCgXqgs xqgs = null;
		Integer id = getIntObject(param.get("xqgsid"));
		if(id>0) {
			//修改
			xqgs =xqgsDao.get(id);
		}
		
		if(xqgs == null) {
			xqgs = new TCgXqgs();
		}
		
		//取值
		xqgs.setCgr(getStringObject(param.get("cgr")));
		xqgs.setCgrdz(getStringObject(param.get("cgrdz")));
		xqgs.setCglxr(getStringObject(param.get("cglxr")));
		xqgs.setCgrlxfs(getStringObject(param.get("cgrlxfs")));
		xqgs.setCgxmmc(getStringObject(param.get("cgxmmc")));
		xqgs.setCgpmdm(getStringObject(param.get("cgpmdm")));
		xqgs.setCgpmmc(getStringObject(param.get("cgpmmc")));
		xqgs.setCgksrq(getDateObject(param.get("cgksrq")));
		xqgs.setCgjsrq(getDateObject(param.get("cgjsrq")));
		xqgs.setGsts(getIntObject(param.get("gsts")));
		xqgs.setBz(getStringObject(param.get("bz")));
		
		SysUser user = SecureUtil.getCurrentUser();
		if("1".equals(workflowflag)){
			//送审
			xqgs.setDqzt("1");//送审状态
		}else {
			xqgs.setDqzt("0");//保存状态
		}		
		if(id>0) {
			//修改			
			xqgs.setXgr(user.getUserid().toString());//修改人
			xqgs.setXgsj(DateUtil.getDate("yyyy-MM-dd HH:mm:ss"));//修改时间
			xqgsDao.update(xqgs);
		}else {
			//新增
			xqgs.setXmid(getIntObject(param.get("xmid")));
			xqgs.setWfid(getStringObject(param.get("wfid")));
			xqgs.setCjr(user.getUserid().toString());//创建人
			xqgs.setCjsj(DateUtil.getDate("yyyy-MM-dd HH:mm:ss"));//创建时间
			id = (Integer) xqgsDao.save(xqgs);
		}
		return id;
		
	}
	/**
	 * 项目需求公示信息送审
	 * @Title: sendWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param cgxxid
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String sendWorkFlow(String menuid, String xqgsid,String activityId, String wfid,Map map) throws Exception {
		String wfkey = pwfc.getWfkeyByMenuid(menuid);
		String msg = "";
		ResultMsg rsgMsg ;
		if("".equals(StringUtil.stringConvert(wfid))||"null".equals(wfid)){
			 rsgMsg =	workflowManageComponent.startProcessByKeyAndPush(wfkey, new HashMap());
			 if(rsgMsg==null || rsgMsg.isSuccess()) {
				 //发送成功
				 wfid = StringUtil.stringConvert(rsgMsg.getBody().get("EXECID"));
				 TCgXqgs xqgs = xqgsDao.get(Integer.valueOf(xqgsid));
				 xqgs.setWfid(wfid);
				 xqgs.setDqzt("1");
				 xqgsDao.update(xqgs);
			 }
		}else {
			//审批意见
			String spyj = StringUtil.stringConvert(map.get("spyj"));
			SysUser user = SecureUtil.getCurrentUser();
			String usercode = user.getUsercode();
			//分支流出路径
			String outcome = "";
			rsgMsg =workflowManageComponent.completeTask(wfid, activityId, outcome, new HashMap(), usercode, spyj);
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
		String xqgsid = map.get("xqgsid") == null ? "":map.get("xqgsid").toString();
		String wfid = map.get("wfid") == null ? "":map.get("wfid").toString();
		String activityId = map.get("activityId") == null ? "":map.get("activityId").toString();
		String spyj = map.get("spyj") == null ? "":map.get("spyj").toString();
		ResultMsg rsgMsg = null;
		String[] xqgsids = xqgsid.split(",");
		String[] wfids = wfid.split(",");
		for(int j=0;j<xqgsids.length;j++){
			SysUser user = SecureUtil.getCurrentUser();
			String usercode = user.getUsercode();
			rsgMsg =workflowManageComponent.completeTask(wfids[j], activityId, new HashMap(), usercode, spyj);
			if(rsgMsg!=null&&!rsgMsg.isSuccess()){//处理失败
				msg = rsgMsg.getTitle();
				throw new Exception(msg);
			}
		}
		return msg;
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
	
	/**
	 * 按照项目需求公示信息id删除
	 * @Title: deleteCgxxById 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param cgxxid
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String deleteCgxxById(Integer xqgsid) throws Exception {
		try{
			xqgsDao.delete(xqgsid);
			//删除相关附件
			fileManageComponent.deleteFilesByKeyId(String.valueOf(xqgsid));
		}catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "";
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
	public String revokeWorkFlow(String wfid, String xqgsid,String activityId) throws Exception {
		SysUser user = SecureUtil.getCurrentUser();
		String usercode = user.getUsercode();
		String returnMsg = "";
		ResultMsg  msg = workflowManageComponent.getBackWorkflow(wfid, activityId, usercode, null);
		 if(!msg.isSuccess()){//处理失败
			 returnMsg = msg.getTitle();
		}
		return returnMsg;
	}
	
	private String getStringObject(Object object){
		
		if(object == null ) {
			return "";
		}
		if(object instanceof String) {
			return (String)object;
		}
		return "";
	}
	
	private Integer getIntObject(Object object){
		
		if(object == null ) {
			return 0;
		}
		String str = (String)object;
		if(str.equals("")) {
			return 0;
		}
		
		return Integer.valueOf(str);
	}
	private Date getDateObject(Object object){
		
		if(object == null ) {
			return null;
		}
		if(object instanceof Date) {
			return (Date)object;
		}
		if(object instanceof String){			
			String dateformat = "yyyy-MM-dd";
			SimpleDateFormat format = new SimpleDateFormat(dateformat);
			try {
				
				Date date = format.parse((String)object);
				return date;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
