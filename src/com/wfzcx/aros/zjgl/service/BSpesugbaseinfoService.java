package com.wfzcx.aros.zjgl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.flow.po.Probaseinfo;
import com.wfzcx.aros.xzfy.dao.BCasesperelabaseinfoDao;
import com.wfzcx.aros.xzfy.po.BCasesperelabaseinfo;
import com.wfzcx.aros.zjgl.dao.BGroupbaseinfoDAO;
import com.wfzcx.aros.zjgl.dao.BSpecialistbaseinfoDAO;
import com.wfzcx.aros.zjgl.dao.BSpegrouprelainfoDAO;
import com.wfzcx.aros.zjgl.dao.BSpesugbaseinfoDAO;
import com.wfzcx.aros.zjgl.po.BGroupbaseinfo;
import com.wfzcx.aros.zjgl.po.BSpecialistbaseinfo;
import com.wfzcx.aros.zjgl.po.BSpegrouprelainfo;
import com.wfzcx.aros.zjgl.po.BSpesugbaseinfo;

@Scope("prototype")
@Service("com.wfzcx.aros.zjgl.service.BSpesugbaseinfoService")
public class BSpesugbaseinfoService {

	@Autowired
	BSpesugbaseinfoDAO spesugbaseinfoDAO;
	@Autowired
	BSpecialistbaseinfoDAO bSpecialistbaseinfoDAO;
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	private BGroupbaseinfoDAO bGroupbaseinfoDAO;
	@Autowired
	private BCasesperelabaseinfoDao bCasesperelabaseinfoDao;
	@Autowired
	private BSpegrouprelainfoDAO bSpegrouprelainfoDAO;
	
	/**
	 * 按照案件ID和操作员ID查询专家意见列表
	 * @param caseId
	 * @param operatorId
	 * @return
	 */
	public List<Map<String, Object>> querySpeSugByCaseIdAndOperId(String caseId ,Long operatorId)  {
		
		StringBuilder getGroupSql = new StringBuilder();
		getGroupSql.append("SELECT T.GROUPID,T.GROUPNAME FROM B_GROUPBASEINFO T,B_CASESPERELABASEINFO U");
		String speid = "";
		if(null != operatorId){
			BSpecialistbaseinfo bSpecialistbaseinfo = new BSpecialistbaseinfo();
			bSpecialistbaseinfo.setUserid(operatorId);
			List<BSpecialistbaseinfo> list = bSpecialistbaseinfoDAO.findByExample(bSpecialistbaseinfo);
			if(!CollectionUtils.isEmpty(list)){
				getGroupSql.append(", B_SPEGROUPRELAINFO F WHERE ");
				getGroupSql.append(" U.GROUPID = F.GROUPID");
				getGroupSql.append(" AND F.SPEID='");
				speid = list.get(0).getSpeid();
				getGroupSql.append(speid);
				getGroupSql.append("' AND ");
			}
		}else {
			getGroupSql.append(" WHERE ");
		}
		
		getGroupSql.append(" T.GROUPID=U.GROUPID AND U.CASEID='");
		getGroupSql.append(caseId);
		getGroupSql.append("' ");
		getGroupSql .append(" ORDER BY T.OPTTIME DESC");
		List<Map<String, Object>> queryListBySQL = mapDataDao.queryListBySQL(getGroupSql.toString());
		for (Map<String, Object> map : queryListBySQL) {
			String groupid = (String) map.get("groupid");
			if(StringUtils.isNotEmpty(groupid)){
				
				StringBuilder sql = new StringBuilder();
				sql.append("select t.*, (select u.spename from b_specialistbaseinfo u where u.speid=t.speid) AS spename from B_SPESUGBASEINFO t where t.groupid='").append(groupid).append("'");
				sql.append(" and t.caseid='").append(caseId).append("'");
				if (null != operatorId) {
					sql.append(" and t.speid='").append(speid).append("'");
				}
				sql.append(" ORDER BY t.OPTTIME");
				List<Map<String, Object>> list = mapDataDao.queryListBySQL(sql.toString());
				map.put("list", list);
			}
		}
		return queryListBySQL;
		
	}

	public void save(BSpesugbaseinfo bean) throws Exception {
		 spesugbaseinfoDAO.save(bean);
	}

	/**
	 * 查看案件专家意见
	 * @param caseid
	 * @return
	 */
	public List<Map<String, Object>> querySpeSugByCaseId(String caseid) {
		return querySpeSugByCaseIdAndOperId(caseid, null);
	}

	/**
	 * 查询当前用户要处理专家评论的案件列表
	 * @param param
	 * @return
	 */
	public PaginationSupport querySpeDealCaseList(Map<String, Object> param) {
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		SysUser user = SecureUtil.getCurrentUser();
		String userId = String.valueOf(user.getUserid());
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.caseid, t.csaecode, t.appname, t.apptype, t.idtype, t.idcode, t.mobile, t.phone, ");
		sql.append("t.address, t.email, t.postcode, t.deftype, t.defname, t.depttype, t.defidtype, t.defidcode, t.defmobile, ");
		sql.append("t.defphone, t.defmailaddress, t.defaddress, t.defemail, t.defpostcode, t.noticeddate, t.actnoticeddate, ");
		sql.append("t.admtype, t.casetype, t.ifcompensation, t.amount, t.appcase, t.factreason, t.annex, t.appdate, t.operator, ");
		sql.append("t.optdate, t.protype, t.opttype, t.nodeid, t.lasttime, t.userid, t.oldprotype, t.receivedate, t.receiveway, ");
		sql.append("t.expresscom, t.couriernum, t.datasource, t.mailaddress, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_APPTYPE' and a.status=0 and a.code = t.apptype) apptype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PUB_PROBASEINFO_RECEIVEWAY' and a.status=0 and a.code = t.receiveway) receivewayname,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_IDTYPE' and a.status=0 and a.code = t.idtype) idtype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DEFTYPE' and a.status=0 and a.code = t.deftype) deftype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ADMINLEVEL' and a.status=0 and a.code = t.admtype) admtype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_CASETYPE' and a.status=0 and a.code = t.casetype) casetype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DATASOURCE' and a.status=0 and a.code = t.datasource) datasource_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_PROTYPE' and a.status=0 and a.code = t.protype) protype_mc, ");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0 and a.code = t.ifcompensation) ifcompensation_mc ");
		sql.append("from B_CASEBASEINFO t where 1=1");
		sql.append(" and t.caseid in (SELECT T2.CASEID FROM B_SPECIALISTBASEINFO T1 , B_CASESPERELABASEINFO T2, B_SPEGROUPRELAINFO T3  WHERE t1.SPEID=T3.SPEID AND T2.GROUPID = T3.GROUPID AND T1.USERID='");
		sql.append(userId);
		sql.append("')");
		
		//流程类型
		String protype = StringUtil.stringConvert(param.get("protype"));
		if (!StringUtils.isBlank(protype)) {
			sql.append(" and t.protype = '").append(protype).append("'");
		} 
		
		//申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		if (!StringUtils.isBlank(appname)) {
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		//被申请人
		String defname = StringUtil.stringConvert(param.get("defname"));
		if (!StringUtils.isBlank(defname)) {
			sql.append(" and t.defname like '%").append(defname).append("%'");
		}
		
		//行政管理类型
		String admtype = StringUtil.stringConvert(param.get("admtype"));
		if (!StringUtils.isBlank(admtype)) {
			sql.append(" and t.admtype = '").append(admtype).append("'");
		}
		
		//申请复议事项
		String casetype = StringUtil.stringConvert(param.get("casetype"));
 		if (!StringUtils.isBlank(casetype)) {
			sql.append(" and t.casetype = '").append(casetype).append("'");
		}
		
		//被申请人类型
		String deftype = StringUtil.stringConvert(param.get("deftype"));
		if (!StringUtils.isBlank(deftype)) {
			sql.append(" and t.deftype = '").append(deftype).append("'");
		}
		
		// 案件编号
		String casecode = StringUtil.stringConvert(param.get("casecode"));
		if (!StringUtils.isBlank(casecode)) {
			sql.append(" and t.csaecode like '%").append(casecode).append("%'");
		}
		// 申请日期起始日期
		String startTime = StringUtil.stringConvert(param.get("startTime"));
		if (!StringUtils.isBlank(startTime)) {
			sql.append(" and (t.appdate > '").append(startTime)
				.append("' or t.appdate = '").append(startTime).append("') ");
		}
		// 申请日期截至日期
		String endTime = StringUtil.stringConvert(param.get("endTime"));
		if (!StringUtils.isBlank(endTime)) {
			sql.append(" and (t.appdate < '").append(endTime)
				.append("' or t.appdate = '").append(endTime).append("')");;
		}
		
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
		
	}

	/**
	 * 查询主题可查看的卷宗
	 * @param param
	 */
	public List<Map<String, Object>> queryFiles(Map<String, Object> param) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		String elementcode= (String) param.get("elementcode");
		String groupid = (String) param.get("groupid");
		StringBuffer sql1 = new StringBuffer();
		sql1.append("SELECT ('aros/jzgl/controller/CaseFileManageController/downLoadFile.do?noticeid=' ||　T.NOTICEID ) AS url, T.NOTICEID, (SELECT A.NAME FROM SYS_YW_DICENUMITEM A WHERE UPPER(A.ELEMENTCODE)='DOCTYPE' AND A.STATUS=0 AND A.CODE = T.DOCTYPE) AS NOTICENAME, T.DOCTYPE, U.USERCODE FROM B_NOTICEBASEINFO T, SYS_FILEMANAGE U WHERE T.NOTICEID = U.REMARK AND U.STEPID is null AND U.ELEMENTCODE='");
		sql1.append(elementcode);
		sql1.append("' AND U.KEYID='");
		sql1.append(groupid);
		sql1.append("'");
		
		result = mapDataDao.queryListBySQL(sql1.toString());
		
		StringBuffer sql2 = new StringBuffer();
		sql2.append("SELECT ('base/filemanage/fileManageController/downLoadFile.do?itemid=' ||　T.ITEMID ) AS URL, T.ITEMID AS NOTICEID,  T.FILENAME AS NOTICENAME, T.USERCODE FROM  SYS_FILEMANAGE T WHERE T.STEPID='JZ_FJ' AND T.ELEMENTCODE='");
		sql2.append(elementcode);
		sql2.append("' AND T.KEYID='");
		sql2.append(groupid);
		sql2.append("'");
		
		result.addAll(mapDataDao.queryListBySQL(sql2.toString()));
		return result;
	}
	/**
	 * 查询当前用户的该案件的所有处理的主题
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> queryZTList(Map<String, Object> param) {

		String caseid = (String) param.get("caseid");
		String speid = "";
		Long userid = SecureUtil.getCurrentUser().getUserid();
		
		BSpecialistbaseinfo bSpecialistbaseinfo = new BSpecialistbaseinfo();
		bSpecialistbaseinfo.setUserid(userid);
		List<BSpecialistbaseinfo> list = bSpecialistbaseinfoDAO.findByExample(bSpecialistbaseinfo);
		if(!CollectionUtils.isEmpty(list)){
			speid = list.get(0).getSpeid();
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select t1.groupid, t1.groupname, t1.opttime,t3.caseid, t2.speid from  b_groupbaseinfo t1, b_spegrouprelainfo t2 ,b_casesperelabaseinfo t3 where t3.groupid = t2.groupid and t1.groupid = t2.groupid and t3.caseid='");
		sql.append(caseid).append("' ");
		sql.append("and t2.speid='").append(speid).append("'").append(" order by t1.opttime");
		return mapDataDao.queryListBySQL(sql.toString());
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> queryspesugbyZT(Map<String, Object> param) {
		String caseid = (String) param.get("caseid");
		String groupid = (String) param.get("groupid");
		String speid = "";
		Long userid = SecureUtil.getCurrentUser().getUserid();
		
		BSpecialistbaseinfo bSpecialistbaseinfo = new BSpecialistbaseinfo();
		bSpecialistbaseinfo.setUserid(userid);
		List<BSpecialistbaseinfo> list = bSpecialistbaseinfoDAO.findByExample(bSpecialistbaseinfo);
		if(!CollectionUtils.isEmpty(list)){
			speid = list.get(0).getSpeid();
		}
		BSpesugbaseinfo bean = new BSpesugbaseinfo();
		bean.setGroupid(groupid);
		bean.setCaseid(caseid);
		bean.setSpeid(speid);
		StringBuilder sql = new StringBuilder();
		sql.append("select t.* from B_SPESUGBASEINFO t where t.groupid='").append(groupid).append("'");
		sql.append(" and t.caseid='").append(caseid).append("'");
		sql.append(" and t.speid='").append(speid).append("'");
		sql.append(" ORDER BY OPTTIME");
		return mapDataDao.queryListBySQL(sql.toString());
	}
	
	/**
	 * @Title: saveSpegrouprelainfo
	 * @Description: 复议研讨发起：保存案件专家对应关系
	 * @author ybb
	 * @date 2016年11月15日 下午5:13:21
	 * @param param
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveSpegrouprelainfo(Map<String, Object> param) throws Exception{
		
		String speids = (String) param.get("speids");
		String caseid = (String) param.get("caseid");
		
		//获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		//小组ID
		String groupid = null;
		
		//判断是否已经存在案件与小组对应关系
		String sql = "select id, groupid, caseid, processid from b_casesperelabaseinfo where caseid = '" + caseid + "'";
		List<?> casesperelabaseinfos = bCasesperelabaseinfoDao.findVoBySql(sql, BCasesperelabaseinfo.class);
		if(casesperelabaseinfos != null && !casesperelabaseinfos.isEmpty()){
			
			//获取案件与小组对应关系
			BCasesperelabaseinfo casesperelabaseinfo  = (BCasesperelabaseinfo) casesperelabaseinfos.get(0);
			groupid = casesperelabaseinfo.getGroupid();
			
		} else {
			
			//保存专家小组信息
			BGroupbaseinfo bean = new BGroupbaseinfo();
			
			bean.setOperator(user.getUserid().toString());
			bean.setOpttime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			bean.setGroupname(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			bGroupbaseinfoDAO.save(bean);
			
			groupid = bGroupbaseinfoDAO.findByExample(bean).get(0).getGroupid();
			
			//保存小组对应案件
			BCasesperelabaseinfo bCasesperelabaseinfo = new BCasesperelabaseinfo();
			
			bCasesperelabaseinfo.setCaseid(caseid);
			bCasesperelabaseinfo.setGroupid(groupid);
			
			bCasesperelabaseinfoDao.save(bCasesperelabaseinfo);
		}
		
		//保存小组对应专家信息
		String[] speIds = speids.split(";");
        for (String speid : speIds) {
			if(StringUtils.isNotEmpty(speid)){
				
				//删除专家
				bSpegrouprelainfoDAO.deleteBySQL("B_SPEGROUPRELAINFO", 
						"groupid = '" + groupid + "' and speid = '" + speid + "'");
				
				//新增专家
				BSpegrouprelainfo bSpegrouprelainfo = new BSpegrouprelainfo();
				
				bSpegrouprelainfo.setGroupid(groupid);
				bSpegrouprelainfo.setSpeid(speid);
				
				bSpegrouprelainfoDAO.save(bSpegrouprelainfo);
			}
		}
	}
	
	/**
	 * @Title: querySpesugbaseinfo
	 * @Description: 委员评论-查询案件对应的委员评论
	 * @author ybb
	 * @date 2016年11月16日 上午11:59:03
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> querySpesugbaseinfo(String caseid) {
		
		String speid = "";
		
		//根据当前登录用户ID获取委员信息
		Long userid = SecureUtil.getCurrentUser().getUserid();
		
		BSpecialistbaseinfo bSpecialistbaseinfo = new BSpecialistbaseinfo();
		bSpecialistbaseinfo.setUserid(userid);
		List<BSpecialistbaseinfo> list = bSpecialistbaseinfoDAO.findByExample(bSpecialistbaseinfo);
		if(list != null && !list.isEmpty()){
			speid = list.get(0).getSpeid();
		}
		
		//根据委员ID和案件ID获取案件评论信息
		StringBuilder sql = new StringBuilder();
		sql.append("select t.* from B_SPESUGBASEINFO t where ");
		sql.append(" t.caseid = '").append(caseid).append("'");
		sql.append(" and t.speid = '").append(speid).append("'");
		sql.append(" order by opttime");
		
		return mapDataDao.queryListBySQL(sql.toString());
	}
	
	/**
	 * @Title: saveSpesugbaseinfo
	 * @Description: 委员评论-保存委员意见
	 * @author ybb
	 * @date 2016年11月16日 下午3:28:31
	 * @param param
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveSpesugbaseinfo(Map<String, Object> param) throws Exception{
		
		//案件ID
		String caseid = (String) param.get("caseid");
		//委员意见
		String remark = (String) param.get("remark");
				
		//获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		//获取专家ID
		String speid = null;
		BSpecialistbaseinfo bSpecialistbaseinfo = new BSpecialistbaseinfo();
		bSpecialistbaseinfo.setUserid(user.getUserid());
		List<BSpecialistbaseinfo> list = bSpecialistbaseinfoDAO.findByExample(bSpecialistbaseinfo);
		if(list != null && !list.isEmpty()){
			speid = list.get(0).getSpeid();
		}
		
		//获取小组ID
		String groupid = null;
		BCasesperelabaseinfo cpi = new BCasesperelabaseinfo();
		cpi.setCaseid(caseid);
		List<BCasesperelabaseinfo> cpis = bCasesperelabaseinfoDao.findByExample(cpi);
		if(cpis != null && !cpis.isEmpty()){
			groupid = cpis.get(0).getGroupid();
		}
		
		//保存专家评论信息
		BSpesugbaseinfo bean = new BSpesugbaseinfo();
		
		bean.setSpeid(speid);
		bean.setOperator(user.getUsername());
		bean.setOpttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss"));
		bean.setCaseid(caseid);
		bean.setRemark(remark);
		bean.setGroupid(groupid);
		
		spesugbaseinfoDAO.save(bean);
	}
	
	/**
	 * @Title: querySpesugbaseinfoByCaseid
	 * @Description: 根据案件ID查询委员评论信息
	 * @author ybb
	 * @date 2016年11月16日 下午4:57:07
	 * @param caseId
	 * @param operatorId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> querySpesugbaseinfoByCaseid(String caseid)  {
		
		//根据委员ID和案件ID获取案件评论信息
		StringBuilder sql = new StringBuilder();
		sql.append("select t.id,t.processid,t.caseid,t.speid,t.remark,t.operator,t.opttime,t.groupid from B_SPESUGBASEINFO t");
		sql.append(" where t.caseid = '").append(caseid).append("'");
		sql.append(" order by opttime");
		
		return mapDataDao.queryListBySQL(sql.toString());
	}
}
