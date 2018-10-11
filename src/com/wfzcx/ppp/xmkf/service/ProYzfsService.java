package com.wfzcx.ppp.xmkf.service;

import java.sql.Date;
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
import com.jbf.common.util.StringUtil;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.component.SysWorkflowManageComponent;
import com.wfzcx.ppms.workflow.component.ProjectWorkFlowComponent;
import com.wfzcx.ppp.xmcb.dao.ProjectXmcbDao;
import com.wfzcx.ppp.xmkf.dao.ProYzfsDao;
import com.wfzcx.ppp.xmkf.po.TKfYzfs;

@Scope("prototype")
@Service("com.wfzcx.ppp.xmkf.service.ProYzfsService")
public class ProYzfsService {

	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProjectXmcbDao xmcbDao;
	@Autowired
	ProjectWorkFlowComponent pwfc;
	@Autowired
	SysWorkflowManageComponent workflowManageComponent;
	@Autowired
	ProYzfsDao yzfsDao;
	
	public PaginationSupport yzfslrQuery(Map<String, Object> param) throws AppException{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();

		sql.append(" select t.xmid xmid_prode,t.sjlx sjlx_prode,t.xmmc xmmc_prode,t.xmlx xmlx_prode,t.xmzje xmzje_prode, t.hznx hznx_prode, t.sshy sshy_prode, t.yzfs yzfs_prode, t.hbjz hbjz_prode, t.fqsj fqsj_prode,");
		sql.append(" t.xmdqhj xmdqhj_prode, t.wfid wfid_prode, t.orgcode orgcode_prode, t.xmcbk xmcbk_prode, t.xmkfk xmkfk_prode, t.fqlx fqlx_prode, t.fqrmc fqrmc_prode, t.xmgk xmgk_prode, t.xmlxr xmlxr_prode,");
		sql.append(" t.lxrdh lxrdh_prode, t.cbssfa cbssfa_prode, t.ssfafj ssfafj_prode, t.spjg spjg_prode, t.sftjxm sftjxm_prode, t.sfxm sfxm_prode, t.sfsqbt sfsqbt_prode, t.btje btje_prode, t.spyj spyj_prode, t.xmzt xmzt_prode,");
		sql.append(" t.cjr cjr_prode, t.cjsj cjsj_prode, t.xgr xgr_prode, t.xgsj xgsj_prode, t.sjxmxh sjxmxh_prode, t.ghsjfafj ghsjfafj_prode, t.zfzyzc zfzyzc_prode, t.ssxq ssxq_prode,t.dqzt dqzt_prode,");
		sql.append(" (select a.name from SYS_DEPT a  where a.code=t.orgcode) orgname_prode,");
		sql.append("    (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROTYPE' and a.status=0  and a.code=t.xmlx) xmlx_mc_prode,");
		sql.append("    (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.zfzyzc) zfzyzc_mc_prode,");
		sql.append("    (select a.name from SYS_YW_DICCODEITEM a where upper(a.elementcode)='PROTRADE' and a.status=0  and a.code=t.sshy) sshy_mc_prode,");
		sql.append("     (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROOPERATE' and a.status=0  and a.code=t.yzfs) yzfs_mc_prode,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PRORETURN' and a.status=0  and a.code=t.hbjz) hbjz_mc_prode,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSENDTYPE' and a.status=0  and a.code=t.fqlx) fqlx_mc_prode,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSFXM' and a.status=0  and a.code=t.sfxm) sfxm_mc_prode,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.SFTJXM) SFTJXM_mc_prode,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.SFSQBT) SFSQBT_mc_prode,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='WFSTATUS' and a.status=0  and a.code=t.xmzt) xmzt_mc_prode,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_AREA' and a.status=0  and a.code=t.ssxq) ssxq_mc_prode,");
		sql.append("     (select a.username from sys_user a where  a.userid=t.cjr) cjr_mc_prode,");
		sql.append("     (select a.username from sys_user a where  a.userid=t.xgr) xgr_mc_prode,");
		//查询备案信息
		sql.append(" yz.yzfsid,yz.xmid,yz.sftyyzfs,yz.pzrq,yz.kfnd,yz.bz,yz.wfid,yz.dqzt,yz.cgfs,yz.spyj,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PURCHASE' and a.status=0  and a.code=yz.cgfs) cgfs_mc,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=yz.sftyyzfs) sftyyzfs_mc");
		sql.append(" from t_xmxx t left join t_kf_yzfs yz on t.xmid=yz.xmid where 1=1 ");

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
		
		String menuid = StringUtil.stringConvert(param.get("menuid"));
		String activityId = StringUtil.stringConvert(param.get("activityId"));
		//获取工作流状态，1待处理2已处理
		String status = StringUtil.stringConvert(param.get("status"));
		//获取工作流状态
		boolean firstNode = param.get("firstNode")==null?false: Boolean.parseBoolean(param.get("firstNode").toString());
		boolean lastNode =   param.get("lastNode")==null?false: Boolean.parseBoolean(param.get("lastNode").toString());
//		String wfids = pwfc.findCurrentWfids(menuid, activityId, status, firstNode, lastNode);
		String wfids = pwfc.findCurrentWfids(menuid, activityId, status, firstNode, lastNode, "yz");
		sql.append(" and ");
		sql.append(wfids);
		sql.append("  order by t.xmid desc");
		System.out.println("sql--"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	public PaginationSupport yzfsshQuery(Map<String, Object> param) throws AppException{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();

		sql.append(" select t.xmid xmid_prode,t.sjlx sjlx_prode,t.xmmc xmmc_prode,t.xmlx xmlx_prode,t.xmzje xmzje_prode, t.hznx hznx_prode, t.sshy sshy_prode, t.yzfs yzfs_prode, t.hbjz hbjz_prode, t.fqsj fqsj_prode,");
		sql.append(" t.xmdqhj xmdqhj_prode, t.wfid wfid_prode, t.orgcode orgcode_prode, t.xmcbk xmcbk_prode, t.xmkfk xmkfk_prode, t.fqlx fqlx_prode, t.fqrmc fqrmc_prode, t.xmgk xmgk_prode, t.xmlxr xmlxr_prode,");
		sql.append(" t.lxrdh lxrdh_prode, t.cbssfa cbssfa_prode, t.ssfafj ssfafj_prode, t.spjg spjg_prode, t.sftjxm sftjxm_prode, t.sfxm sfxm_prode, t.sfsqbt sfsqbt_prode, t.btje btje_prode, t.spyj spyj_prode, t.xmzt xmzt_prode,");
		sql.append(" t.cjr cjr_prode, t.cjsj cjsj_prode, t.xgr xgr_prode, t.xgsj xgsj_prode, t.sjxmxh sjxmxh_prode, t.ghsjfafj ghsjfafj_prode, t.zfzyzc zfzyzc_prode, t.ssxq ssxq_prode,t.dqzt dqzt_prode,");
		sql.append(" (select a.name from SYS_DEPT a  where a.code=t.orgcode) orgname_prode,");
		sql.append("    (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROTYPE' and a.status=0  and a.code=t.xmlx) xmlx_mc_prode,");
		sql.append("    (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.zfzyzc) zfzyzc_mc_prode,");
		sql.append("    (select a.name from SYS_YW_DICCODEITEM a where upper(a.elementcode)='PROTRADE' and a.status=0  and a.code=t.sshy) sshy_mc_prode,");
		sql.append("     (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROOPERATE' and a.status=0  and a.code=t.yzfs) yzfs_mc_prode,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PRORETURN' and a.status=0  and a.code=t.hbjz) hbjz_mc_prode,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSENDTYPE' and a.status=0  and a.code=t.fqlx) fqlx_mc_prode,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSFXM' and a.status=0  and a.code=t.sfxm) sfxm_mc_prode,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.SFTJXM) SFTJXM_mc_prode,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.SFSQBT) SFSQBT_mc_prode,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='WFSTATUS' and a.status=0  and a.code=t.xmzt) xmzt_mc_prode,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_AREA' and a.status=0  and a.code=t.ssxq) ssxq_mc_prode,");
		sql.append("     (select a.username from sys_user a where  a.userid=t.cjr) cjr_mc_prode,");
		sql.append("     (select a.username from sys_user a where  a.userid=t.xgr) xgr_mc_prode,");
		//查询备案信息
		sql.append(" yz.yzfsid,yz.xmid,yz.sftyyzfs,yz.pzrq,yz.kfnd,yz.bz,yz.wfid,yz.dqzt,yz.cgfs,yz.spyj,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PURCHASE' and a.status=0  and a.code=yz.cgfs) cgfs_mc,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=yz.sftyyzfs) sftyyzfs_mc");
		sql.append(" from t_xmxx t left join t_kf_yzfs yz on t.xmid=yz.xmid where 1=1 ");

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
		
		String menuid = StringUtil.stringConvert(param.get("menuid"));
		String activityId = StringUtil.stringConvert(param.get("activityId"));
		String status = StringUtil.stringConvert(param.get("status"));
		//获取工作流状态
//		boolean firstNode = param.get("firstNode")==null?false: Boolean.parseBoolean(param.get("firstNode").toString());
//		boolean lastNode =   param.get("lastNode")==null?false: Boolean.parseBoolean(param.get("lastNode").toString());
//		String wfids = pwfc.findCurrentWfids(menuid, activityId, status, firstNode, lastNode);
//		sql.append(" and ");
//		sql.append(wfids);
		sql.append("  order by t.xmid desc");
		System.out.println("sql--"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TKfYzfs yzfslrAddSave(Map<String, Object> param) throws Exception{
		TKfYzfs yz = new TKfYzfs();
		BeanUtils.populate(yz, param);
		SysUser user = SecureUtil.getCurrentUser();
		yz.setCjr(user.getUserid().toString());
		yz.setCjsj(new Date(new java.util.Date().getTime()));
		
		yzfsDao.save(yz);
		return yz;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TKfYzfs yzfslrEditSave(String yzfsid,Map<String, Object> param) throws Exception{
		TKfYzfs yz = yzfsDao.get(Integer.parseInt(StringUtil.stringConvert(yzfsid)));
		BeanUtils.populate(yz, param);
		SysUser user = SecureUtil.getCurrentUser();
		yz.setXgr(user.getUserid().toString());
		yz.setXgsj(new Date(new java.util.Date().getTime()));
		
		yzfsDao.update(yz);
		return yz;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void yzfslrDelete(String yzfsid){
		String where = "yzfsid = " + yzfsid;
		yzfsDao.deleteBySQL("T_KF_YZFS", where);
	}
	
	public String getTKfYzfsByXmid(String xmid){
		List<JSONObject> list = mapDataDao.queryListBySQL("select yzfsid from t_kf_yzfs yz where yz.xmid="+xmid);
		if(list.size()>0){
			String yzfsid = list.get(0).getString("yzfsid");
			return yzfsid;
		} else {
			return "";
		}
	}
	
	public SysResource getResourceById(String menuid) throws Exception {
		return pwfc.getResourceById(menuid);
	}
}
