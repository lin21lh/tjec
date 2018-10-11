package com.wfzcx.ppp.xmkf.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.base.filemanage.component.FileManageComponent;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.workflow.component.SysWorkflowManageComponent;
import com.wfzcx.ppms.workflow.component.ProjectWorkFlowComponent;

@Scope("prototype")
@Service("com.wfzcx.ppp.xmkf.service.ProjectLzbgService")
public class ProjectLzbgService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProjectWorkFlowComponent pwfc;
	@Autowired
	SysWorkflowManageComponent workflowManageComponent;
	@Autowired
	ParamCfgComponent pcfg;
	@Autowired
	FileManageComponent fileManageComponent;
	
	public SysResource getResourceById(String menuid) throws Exception {
		return pwfc.getResourceById(menuid);
	}
	/**
	 * 查询项目信息
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

		sql.append(" select t.xmid, t.sjlx, t.xmmc, t.xmlx, t.xmzje, t.hznx, t.sshy, t.yzfs, t.hbjz, t.fqsj,");
		sql.append(" t.xmdqhj, t.orgcode, t.xmcbk, t.xmkfk, t.fqlx, t.fqrmc, t.xmgk, t.xmlxr,");
		sql.append(" t.lxrdh, t.cbssfa, t.spjg, t.sftjxm, t.sfxm, t.sfsqbt, t.btje,  t.xmzt,");
		sql.append(" t.sjxmxh, t.ghsjfafj, t.zfzyzc, t.ssxq,");
		sql.append(" t1.lzbgid,t1.dxpg_jl,t1.dxpg_zjdfxq,t1.wyszyz_wcsj,t1.dlpg_yywhjcb,t1.dlpg_jzxzltzz,t1.");
		sql.append("       dlpg_xmqbfxcb,t1.dlpg_xj,t1.dlpg_ppp,t1.dlpg_vfm,t1.dxpg_jg,t1.dlpg_jg,t1.czcsnl_yzjl,");
		sql.append("       t1.czcsnl_wcsj,t1.czcsnl_yzjg,t1.bz,t1.wfid,t1.dqzt,t1.cjr,t1.cjsj,t1.xgr,t1.xgsj,t1.zjjgdm,");
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
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='WFSTATUS' and a.status=0  and a.code=t1.dqzt) dqzt_name,");
		sql.append("     (select a.username from sys_user a where  a.userid=t1.cjr) cjr_mc,");
		sql.append("     (select a.username from sys_user a where  a.userid=t1.xgr) xgr_mc");
		sql.append(" from t_xmxx t ,T_KF_LZBG_ZB t1 where t.xmid = t1.xmid(+) ");

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
		String wfids = pwfc.findCurrentWfids(menuid, activityId, status, firstNode, lastNode,"t1");
		sql.append(" and ");
		sql.append(wfids);
		//添加必须是指定的中介机构才能录入
		
		sql.append("  order by t.xmid desc");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	/**
	 * 评审专家列表
	 * @Title: queryPjzjlb 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @return
	 * @throws AppException 设定文件
	 */
	public PaginationSupport queryPjzjlb(Map map) throws AppException {
		String lzbgid = map.get("lzbgid")==null?"":map.get("lzbgid").toString();
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
        StringBuffer sql = new StringBuffer();
		sql.append("select zjlbid, lzbgid, zjmc, zjlx, pbzy, scly, zjlxfs, bz, zjkid");
		sql.append("  from t_kf_lzbg_zjlb t");
		sql.append(" where 1=1 ");
		if("".equals(lzbgid)){
			sql.append(" and 1<>1" );
		}else{
			sql.append(" and T.lzbgid = "+lzbgid );
		}
		System.out.println("【评审专家sql：】"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	/**
	 * 评价指标列表
	 * @Title: queryPjzbTable 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @return
	 * @throws AppException 设定文件
	 */
	public PaginationSupport queryPjzbTable(Map map) throws AppException {
		String lzbgid = map.get("lzbgid")==null?"":map.get("lzbgid").toString();
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select t.pjzbid, t.zbkid, t.zbmc, t.qz, t.bz, t.zblb, t.lzbgid,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROZBLB' and a.status=0  and a.code=t.zblb) zblb");
		sql.append("  from t_kf_lzbg_pjzb t where 1=1 ");

		if("".equals(lzbgid)){
			sql.append(" and 1<>1" );
		}else{
			sql.append(" and T.lzbgid = "+lzbgid );
		}
		System.out.println("【评价指标sql：】"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	/**
	 * 财政承受能力
	 * @Title: financeQuery 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @return
	 * @throws AppException 设定文件
	 */
	public PaginationSupport financeQuery(Map map) throws AppException {
		String lzbgid = map.get("lzbgid")==null?"":map.get("lzbgid").toString();
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from t_kf_lzbg_czcsnl t where t.lzbgid=").append(lzbgid);
		return mapDataDao.queryPageBySQL(sql.toString(), pageIndex, pageSize);
	}
}
