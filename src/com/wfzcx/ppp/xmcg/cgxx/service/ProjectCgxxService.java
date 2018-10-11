package com.wfzcx.ppp.xmcg.cgxx.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
import com.wfzcx.ppp.xmcg.cgxx.dao.ProjectCgxxDao;
import com.wfzcx.ppp.xmcg.cgxx.po.TCgCgxx;

@Scope("prototype")
@Service("com.wfzcx.ppp.xmcg.cgxx.service.ProjectCgxxService")
public class ProjectCgxxService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProjectXmcbDao xmcbDao;
	@Autowired
	ProjectCgxxDao cgxxDao;
	@Autowired
	ProjectWorkFlowComponent pwfc;
	@Autowired
	SysWorkflowManageComponent workflowManageComponent;
	@Autowired
	ParamCfgComponent pcfg;
	
	public PaginationSupport queryProject(Map<String, Object> param)   throws AppException{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();

		sql.append(" select t.xmid, sjlx, xmmc, xmlx, xmzje, hznx, sshy, yzfs, hbjz, fqsj,");
		sql.append(" xmdqhj, t.wfid, orgcode, xmcbk, xmkfk, fqlx, fqrmc, xmgk, xmlxr,");
		sql.append(" t.lxrdh xmlxrdh, cbssfa, ssfafj, spjg, sftjxm, sfxm, sfsqbt, btje, spyj, xmzt,");
		sql.append(" c.cjr, c.cjsj, c.xgr, c.xgsj, sjxmxh, ghsjfafj, zfzyzc, ssxq,c.dqzt,");
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
		sql.append("     (select a.username from sys_user a where  a.userid=t.xgr) xgr_mc,");
		sql.append(" c.cgxxid, c.cglxr, c.lxrdh, c.ysje, c.cgfs, c.dljgid, c.zgbmyj, c.pppyj, c.zfcgglkyj ");
		sql.append(" from t_xmxx t, t_cg_cgxx c where t.xmid = c.xmid(+)");
		

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
		//采购信息状态
		String status = StringUtil.stringConvert(param.get("status"));
		if(status.equals("0")) {
			sql.append(" and (c.dqzt is null or c.dqzt = '0') ");
		}else if(status.equals("1")) {
			sql.append(" and c.dqzt = '1' ");
		}
		sql.append("  order by t.xmid desc");
		System.out.println("sql--"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String projectCgxxAddCommit(Map<String, Object> param) throws Exception{
		/**
		 * 1、保存项目采购信息表
		 */
		String workflowflag = param.get("workflowflag").toString();
		Integer cgxxidInteger = saveProjectCgxx(param,workflowflag);
		//采购信息保存完成后，将采购信息id更新至附件中的keyid中
		String fjkeyid = StringUtil.stringConvert(param.get("fjkeyid"));
		String updFjKeySql =" update sys_filemanage t set t.keyid='"+cgxxidInteger+"' where t.keyid='"+fjkeyid+"' ";
		xmcbDao.updateBySql(updFjKeySql);
		
		String msg = "";//返回值

		return msg;
	}
	/**
	 * 保存项目采购信息
	 * @Title: saveProjectCgxx 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @param workflowflag
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public  Integer saveProjectCgxx(Map<String, Object> param,String workflowflag) throws Exception{
		TCgCgxx cgxx = new TCgCgxx();
		BeanUtils.populate(cgxx, param);
		SysUser user = SecureUtil.getCurrentUser();
		Integer cgxxid = cgxx.getCgxxid();
		if("1".equals(workflowflag)){
			//提交
			cgxx.setDqzt("1");//提交状态
		}else {
			cgxx.setDqzt("0");//保存状态
		}		
		if(cgxxid>0) {
			//修改
			TCgCgxx oldcgxx =cgxxDao.get(cgxxid);
			String cjr = oldcgxx.getCjr();
			Date cjsj = oldcgxx.getCjsj();
			BeanUtils.copyProperties(oldcgxx, cgxx);
			oldcgxx.setXgr(user.getUserid().toString());//修改人
			oldcgxx.setXgsj(DateUtil.getDate("yyyy-MM-dd HH:mm:ss"));//修改时间
			oldcgxx.setCjr(cjr);//创建人
			oldcgxx.setCjsj(cjsj);//创建时间
			cgxxDao.update(oldcgxx);
		}else {
			//新增
			cgxx.setCjr(user.getUserid().toString());//创建人
			cgxx.setCjsj(DateUtil.getDate("yyyy-MM-dd HH:mm:ss"));//创建时间
			cgxxid = (Integer) cgxxDao.save(cgxx);
		}
		return cgxxid;
		
	}
	/**
	 * 项目采购信息提交
	 * @Title: sendWorkFlow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid
	 * @param cgxxid
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String sendWorkFlow(String menuid, Integer cgxxid) throws Exception {
		TCgCgxx cgxx = new TCgCgxx();
		cgxx = cgxxDao.get(cgxxid);
		if(cgxx==null) {
			return "采购信息不存在";
		}
		cgxx.setDqzt("1");//当前状态   已提交
		cgxxDao.update(cgxx);
		
		return "";
	}
	public SysResource getResourceById(String menuid) throws Exception {
		return pwfc.getResourceById(menuid);
	}
	
	
	/**
	 * 按照项目采购信息id删除
	 * @Title: deleteCgxxById 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param cgxxid
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String deleteCgxxById(Integer cgxxid) throws Exception {
		try{
			cgxxDao.delete(cgxxid);
		}catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "";
	}
}
