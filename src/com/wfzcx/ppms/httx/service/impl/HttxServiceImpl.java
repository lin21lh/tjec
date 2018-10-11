package com.wfzcx.ppms.httx.service.impl;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
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
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.ppms.httx.dao.ProHttxDao;
import com.wfzcx.ppms.httx.po.ProHttx;
import com.wfzcx.ppms.httx.service.HttxService;

/**
 * 合同体系Service服务类
 * @author wang_yliang
 *
 */
@Scope("prototype")
@Service("com.wfzcx.ppms.httx.service.impl.HttxServiceImpl")
public class HttxServiceImpl implements HttxService{

	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProHttxDao proHttxDao;
	
	@Override
	public PaginationSupport queryHttx(Map<String, Object> param) throws AppException {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;

		SysUser user = SecureUtil.getCurrentUser();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.httxid,t.projectid,t.htmc,t.htlb,t.jfmc,t.jflxr,")
		.append(" t.jflxrdh,t.yfmc,t.yflxr,t.yflxrdh,t.htqdrq,t.htzxksrq,")
		.append(" t.htzxjsrq,t.htje,t.htnr,t.htfj,t.htzt,t.cjr,t.cjsj,t.xgr,")
		.append(" t.xgsj,t.sfzj,t.sjhttxid,t.orgcode,t.jfdz,t.yfdz")
		.append(" ,(select a.name from sys_dept a,pro_develop b,pro_project c where a.code=b.implement_organ and b.projectid = c.projectid and c.projectid=t.projectid) implement_organ")
		.append(" ,(select a.pro_name from pro_project a where a.projectid = t.projectid) xmmc")
		.append(" ,(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROHTLB' and a.status=0  and a.code=t.htlb) htlbname")
		.append(" ,(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROHTZT' and a.status=0  and a.code=t.htzt) htztname")
		.append(" ,(select a.username from sys_user a where a.userid=t.cjr) cjrname")
		.append(" ,(select a.name from sys_dept a where a.code=t.orgcode) orgcodename")
		.append(" ,(select a.username from sys_user a where a.userid=t.xgr) xgrname")
		.append(" from pro_httx t,pro_project p where t.projectid=p.projectid(+) and 1=1 ");
		sql.append(" and t.orgcode=" + user.getOrgcode());
		String htmc = StringUtil.stringConvert(param.get("htmc"));
		if(!"".equals(htmc)){
			sql.append(" and t.htmc like '%").append(htmc.trim()).append("%'");
		}
		String jfmc = StringUtil.stringConvert(param.get("jfmc"));
		if(!"".equals(jfmc)){
			sql.append(" and t.jfmc like '%").append(jfmc.trim()).append("%'");
		}
		String yfmc = StringUtil.stringConvert(param.get("yfmc"));
		if(!"".equals(yfmc)){
			sql.append(" and t.yfmc like '%").append(yfmc.trim()).append("%'");
		}
		String htqdrqBegin = StringUtil.stringConvert(param.get("htqdrqBegin"));
		if(!"".equals(htqdrqBegin)){
			sql.append(" and substr(t.htqdrq,0,10) >= '").append(htqdrqBegin.trim()).append("'");
		}
		String htqdrqEnd = StringUtil.stringConvert(param.get("htqdrqEnd"));
		if(!"".equals(htqdrqEnd)){
			sql.append(" and substr(t.htqdrq,0,10) <= '").append(htqdrqEnd.trim()).append("'");
		}
		String htlb = StringUtil.stringConvert(param.get("htlb"));
		if(!"".equals(htlb)){
			sql.append(" and t.htlb = '").append(htlb.trim()).append("'");
		}
		String xmmc = StringUtil.stringConvert(param.get("xmmc"));
		if(!"".equals(xmmc)){
			sql.append(" and p.pro_name like '%").append(xmmc.trim()).append("%'");
		}
		sql.append(" order by t.httxid desc");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);

	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String httxAddCommit(Map<String, Object> param) throws Exception{
		String msg = "";
		ProHttx proHttx = new ProHttx();
		BeanUtils.populate(proHttx, param);
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		proHttx.setCjr(user.getUserid().toString());
		proHttx.setCjsj(DateUtil.getCurrentDateTime());
		proHttx.setOrgcode(Integer.valueOf(user.getOrgcode()));
		
		proHttx.setSfzj("0");//非追加合同
		
		proHttxDao.save(proHttx);
		msg = "success";
		return msg;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String httxAppendCommit(Map<String, Object> param) throws Exception{
		String msg = "";
		ProHttx proHttx = new ProHttx();
		BeanUtils.populate(proHttx, param);
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		proHttx.setCjr(user.getUserid().toString());
		proHttx.setCjsj(DateUtil.getCurrentDateTime());
		proHttx.setOrgcode(Integer.valueOf(user.getOrgcode()));
		
		proHttx.setSfzj("1");//非追加合同
		
		proHttxDao.save(proHttx);
		msg = "success";
		return msg;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String httxEditCommit(Map<String, Object> param) throws Exception {
		ProHttx proHttx = proHttxDao.get(Integer.parseInt(StringUtil.stringConvert(param.get("httxid"))));
		BeanUtils.populate(proHttx, param);
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		proHttx.setXgr(user.getUserid().toString());
		proHttx.setXgsj(DateUtil.getCurrentDateTime());
		proHttxDao.update(proHttx);
		/*更新追加合同*/
		StringBuffer sql = new StringBuffer();
		sql.append("update pro_httx t set projectid='"+proHttx.getProjectid()+"',")
		.append("htmc='"+proHttx.getHtmc()+"',")
		.append("htlb='"+proHttx.getHtlb()+"',")
		.append("jfmc='"+proHttx.getJfmc()+"',")
		.append("yfmc='"+proHttx.getYfmc()+"',")
		.append("htzt='"+proHttx.getHtzt()+"'")
		.append(" where sjhttxid="+proHttx.getHttxid());
		proHttxDao.updateBySql(sql.toString());
		return "success";
	}
	
	@Override
	public PaginationSupport queryProject(Map<String, Object> param) throws AppException {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select t.projectid,t.pro_name,pro_type")
		.append(" ,(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROTYPE' and a.status=0  and a.code=t.pro_type) pro_type_name")
		.append(" ,(select s.name from sys_dept s,pro_develop a where s.code=a.implement_organ and a.projectid=t.projectid) organ_name")
		.append(" ,t.amount")
		.append(" from pro_project t where 1=1 ");
		
		String proName = StringUtil.stringConvert(param.get("proName"));
		if(!"".equals(proName)){
			sql.append(" and t.pro_name like '%").append(proName.trim()).append("%'");
		}
		
		sql.append(" order by t.projectid desc");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);

	}
}
