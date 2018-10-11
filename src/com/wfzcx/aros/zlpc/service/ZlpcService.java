package com.wfzcx.aros.zlpc.service;

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
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.zlpc.dao.BCasequacheckindicatorDao;
import com.wfzcx.aros.zlpc.dao.BCasequacheckinfoDao;
import com.wfzcx.aros.zlpc.po.BCasequacheckindicator;
import com.wfzcx.aros.zlpc.po.BCasequacheckinfo;

@Scope("prototype")
@Service("com.wfzcx.aros.zlpc.service.ZlpcService")
public class ZlpcService {

	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	BCasequacheckindicatorDao zbDao;
	@Autowired
	BCasequacheckinfoDao scoreDao;
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public BCasequacheckindicator zbAdd(Map<String, Object> param) throws Exception{
		BCasequacheckindicator info = new BCasequacheckindicator();
		BeanUtils.populate(info, param);
		zbDao.save(info);
		return info;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void zbDelete(String standid){
		zbDao.delete(standid);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public BCasequacheckindicator zbUpdate(Map<String,Object> param) throws Exception{
		BCasequacheckindicator info = zbDao.get(StringUtil.stringConvert(param.get("standid")));
		BeanUtils.populate(info, param);
		zbDao.update(info);
		return info;
	}
	
	public PaginationSupport zbQuery(Map<String,Object> param){
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select t.*, ");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ZB_STAGETYPE' and a.status=0  and a.code=t.stagetype) stagetype_mc,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ZB_INDITYPE' and a.status=0  and a.code=t.inditype) inditype_mc");
		sql.append(" from b_casequacheckindicator t");
		sql.append(" where 1=1");
		
		String standardname = StringUtil.stringConvert(param.get("standardname"));
		if(!"".equals(standardname)){
			sql.append(" and t.standardname like '%").append(standardname).append("%'");
		}
		String stagetype = StringUtil.stringConvert(param.get("stagetype"));
		if(!"".equals(stagetype)){
			sql.append(" and t.stagetype in ('").append(stagetype.replaceAll(",", "','")).append("')");
		}
		String inditype = StringUtil.stringConvert(param.get("inditype"));
		if(!"".equals(inditype)){
			sql.append(" and t.inditype in ('").append(inditype.replaceAll(",", "','")).append("')");
		}
		sql.append(" order by t.stagetype asc,t.inditype asc,t.seqno asc");
		System.out.println("sql--" + sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	public BCasequacheckindicator getById(String standid){
		return zbDao.get(standid);
	}
	
	public PaginationSupport scoreQuery(Map<String, Object> param) {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		String isbigcase=(String)param.get("isbigcase");
		String casetype=(String)param.get("casetype");
		String casestatus=(String)param.get("casestatus");
		String admtype=(String)param.get("admtype");
		String protype=(String)param.get("protype");
		String csaecode=(String)param.get("csaecode");
		String appname=(String)param.get("appname");
		String defname=(String)param.get("defname");
		String dealpeople=(String)param.get("dealpeople");
		String acceptname=(String)param.get("acceptname");
		String yearstart=(String)param.get("yearstart");
		String yearend=(String)param.get("yearend");
		String appdatestart=(String)param.get("appdatestart");
		String appdateend=(String)param.get("appdateend");
		String dealdatastart=(String)param.get("dealdatastart");
		String dealdataend=(String)param.get("dealdataend");
		
		StringBuilder xzfySql = new StringBuilder();
		
		xzfySql.append("select t.caseid, t.csaecode, t.appname, t.apptype, t.idtype, t.idcode, t.mobile, t.phone, ");
		xzfySql.append("t.address, t.email, t.postcode, t.deftype, t.defname, t.depttype, t.defidtype, t.defidcode, t.defmobile, ");
		xzfySql.append("t.defphone, t.defmailaddress, t.defaddress, t.defemail, t.defpostcode, t.noticeddate, t.actnoticeddate, ");
		xzfySql.append("t.admtype, t.casetype, t.ifcompensation, t.amount, t.appcase, t.factreason, t.annex, t.appdate, t.operator, ");
		xzfySql.append("t.optdate, t.protype, t.opttype, t.nodeid, t.lasttime, t.userid, t.oldprotype, t.receivedate, t.receiveway, ");
		xzfySql.append("t.expresscom, t.couriernum, t.datasource, t.mailaddress, ");
		xzfySql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_APPTYPE' and a.status=0 and a.code = t.apptype) apptype_mc, ");
		xzfySql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_IDTYPE' and a.status=0 and a.code = t.idtype) idtype_mc, ");
		xzfySql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DEFTYPE' and a.status=0 and a.code = t.deftype) deftype_mc, ");
		xzfySql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ADMINLEVEL' and a.status=0 and a.code = t.admtype) admtype_mc, ");
		xzfySql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_CASETYPE' and a.status=0 and a.code = t.casetype) casetype_mc, ");
		xzfySql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DATASOURCE' and a.status=0 and a.code = t.datasource) datasource_mc, ");
		xzfySql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_PROTYPE' and a.status=0 and a.code = t.protype) protype_mc, ");
		xzfySql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0 and a.code = t.ifcompensation) ifcompensation_mc, ");
		xzfySql.append(" (select count(*) from b_casequacheckinfo b where b.caseid=t.caseid) zlpcnum");
		xzfySql.append(" from B_CASEBASEINFO t where 1=1 ");
		
		if (!StringUtils.isEmpty(isbigcase)) {
			xzfySql.append(" and isgreat='").append(isbigcase).append("'");
		}
		if (!StringUtils.isEmpty(casetype)) {
			xzfySql.append(" and casetype='").append(casetype).append("'");
		}
		if (!StringUtils.isEmpty(casestatus)) {
			xzfySql.append(" and state='").append(casestatus).append("'");
		}
		if (!StringUtils.isEmpty(admtype)) {
			xzfySql.append(" and admtype='").append(admtype).append("'");
		}
		if (!StringUtils.isEmpty(protype)) {
			xzfySql.append(" and protype='").append(protype).append("'");
		}
		if (!StringUtils.isEmpty(csaecode)) {
			xzfySql.append(" and csaecode like '%").append(csaecode).append("%'");
		}
		if (!StringUtils.isEmpty(appname)) {
			xzfySql.append(" and appname like '%").append(appname).append("%'");
		}
		if (!StringUtils.isEmpty(defname)) {
			xzfySql.append(" and defname like '%").append(defname).append("%'");
		}
		if (!StringUtils.isEmpty(dealpeople)) {
			xzfySql.append(" and operator='").append(dealpeople).append("'");
		}
		if (!StringUtils.isEmpty(acceptname)) {
			xzfySql.append(" and operator='").append(acceptname).append("'");
		}
		if (!StringUtils.isEmpty(yearstart)) {
			xzfySql.append(" and receivedate >='").append(yearstart).append("'");
		}
		if (!StringUtils.isEmpty(yearend)) {
			xzfySql.append(" and receivedate <='").append(yearend).append("'");
		}
		if (!StringUtils.isEmpty(appdatestart)) {
			xzfySql.append(" and appdate >= '").append(appdatestart).append("'");
		}
		if (!StringUtils.isEmpty(appdateend)) {
			xzfySql.append(" and appdate <= '").append(appdateend).append("'");
		}
		if (!StringUtils.isEmpty(dealdatastart)) {
			//xzfySql.append(" and casetype='").append(casetype).append("'");
		}
		if (!StringUtils.isEmpty(dealdataend)) {
			//xzfySql.append(" and casetype='").append(casetype).append("'");
		}
		
		return mapDataDao.queryPageBySQLForConvert(xzfySql.toString(), pageIndex, pageSize);
	}
	
	@SuppressWarnings("unchecked")
	public List scoreTable(String caseid){
		StringBuffer sql = new StringBuffer("select zb.*,sc.id scoreid,sc.actscore actscore,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ZB_STAGETYPE' and a.status=0  and a.code=zb.stagetype) stagetypename,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ZB_INDITYPE' and a.status=0  and a.code=zb.inditype) inditypename");
		sql.append(" from b_casequacheckindicator zb left join b_casequacheckinfo sc on sc.caseid='"+caseid+"' and zb.standid=sc.standid");
		sql.append(" order by zb.stagetype asc,zb.inditype asc,zb.seqno asc");
		List list = mapDataDao.queryListBySQL(sql.toString());
		return list;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void scoreSave(Map<String,Object> param){
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		String data = StringUtil.stringConvert(param.get("data"));
		String where = "caseid = '"+caseid+"'";
		scoreDao.deleteBySQL("b_casequacheckinfo", where);//删除已有信息
		SysUser user = SecureUtil.getCurrentUser();
		String[] datas = data.split(";");
		for(String s:datas){
			String[] ss = s.split(",");
			BCasequacheckinfo info = new BCasequacheckinfo();
			info.setCaseid(caseid);
			info.setStandid(ss[0]);
			info.setActscore(Integer.valueOf(ss[1]));
			info.setOperator(user.getUserid().toString());
			info.setOptdate(DateUtil.getCurrentDate());
			scoreDao.save(info);
		}
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void scoreDelete(String caseid){
		String where = "caseid = '"+caseid+"'";
		scoreDao.deleteBySQL("b_casequacheckinfo", where);
	}
	
}
