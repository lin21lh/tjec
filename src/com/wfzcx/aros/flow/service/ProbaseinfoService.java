package com.wfzcx.aros.flow.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.FormatUtil;
import com.jbf.common.util.StringUtil;
import com.wfzcx.aros.bxzfy.dao.RcasebaseinfoDao;
import com.wfzcx.aros.bxzfy.po.Rcasebaseinfo;
import com.wfzcx.aros.flow.dao.ProbaseinfoDao;
import com.wfzcx.aros.flow.po.Probaseinfo;
import com.wfzcx.aros.util.GCC;
import com.wfzcx.aros.xzfy.dao.CasebaseinfoDao;
import com.wfzcx.aros.xzfy.po.Casebaseinfo;
import com.wfzcx.aros.ysaj.dao.AdmlitbaseinfoDao;
import com.wfzcx.aros.ysaj.po.Admlitbaseinfo;

/**
 * @ClassName: ProbaseinfoService
 * @Description: 处理流程过程具体业务实现
 * @author ybb
 * @date 2016年8月15日 下午3:09:06
 * @version V1.0
 */
@Scope("prototype")
@Service("com.wfzcx.aros.flow.service.ProbaseinfoService")
public class ProbaseinfoService {

	@Autowired
	private MapDataDaoI mapDataDao;
	@Autowired
	private ProbaseinfoDao probaseinfoDao;
	@Autowired
	private CasebaseinfoDao casebaseinfoDao;
	@Autowired
	private RcasebaseinfoDao rcasebaseinfoDao;
	@Autowired
	private AdmlitbaseinfoDao admlitbaseinfoDao;
	
	/**
	 * @Title: queryProbaseinfoListByParam
	 * @Description: 查询流程列表信息（通过案件ID）
	 * @author ybb
	 * @date 2016年8月15日 下午3:23:58
	 * @param caseid 案件ID
	 * @param protype 流程类型
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	public List<JSONObject> queryProbaseinfoListByParam(String caseid, String protype) throws AppException{
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg, ");
		sql.append(" (select a.nodename from PUB_PRONODEBASEINFO a where protype = '01' and a.nodeid = nvl(t.nodeid, 0)) nodeid_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'PUB_PROBASEINFO_OPTTYPE' and a.status = 0 and a.code = t.opttype) opttype_mc ");
		sql.append(" from pub_probaseinfo t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + protype + "'");
		sql.append(" order by t.nodeid asc, t.opttype desc");
		
		//执行查询
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		
		return probaseinfoList;
	}
	
	/**
	 * @Title: queryProbaseinfoByCaseid
	 * @Description: 根据条件返回某条流程记录
	 * @author ybb
	 * @date 2016年8月17日 上午11:26:08
	 * @param caseid 案件ID
	 * @param protype 流程类型
	 * @param opttype 处理标志
	 * @param nodeid 节点编号
	 * @return
	 * @throws AppException
	 */
	public Probaseinfo queryProbaseinfoByCaseid(String caseid, String protype, String opttype, BigDecimal nodeid) {
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + protype + "'");
		sql.append(" and t.opttype = '" + opttype + "'");
		
		if(nodeid != null){
			sql.append(" and t.nodeid = " + nodeid);
		}
		sql.append(" order by t.endtime desc"); 
		
		List<?> probaseinfoList = probaseinfoDao.findVoBySql(sql.toString(), Probaseinfo.class);
		if(probaseinfoList != null && !probaseinfoList.isEmpty()){
			return (Probaseinfo) probaseinfoList.get(0);
		}
		
		return null;
	}
	
	/**
	 * @Title: queryProbaseinfoByCaseid
	 * @Description: 根据条件返回某条流程记录
	 * @author ztt
	 * @date 2016年11月2日
	 * @param caseid 案件ID
	 * @param protype 流程类型
	 * @param nodeid 节点编号
	 * @return
	 * @throws AppException
	 */
	public Probaseinfo queryProbaseinfoByCaseid(String caseid, String protype, BigDecimal nodeid) {
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + protype + "'");
		
		if(nodeid != null){
			sql.append(" and t.nodeid = " + nodeid);
		}
		sql.append(" order by t.endtime desc"); 
		
		List<?> probaseinfoList = probaseinfoDao.findVoBySql(sql.toString(), Probaseinfo.class);
		if(probaseinfoList != null && !probaseinfoList.isEmpty()){
			return (Probaseinfo) probaseinfoList.get(0);
		}
		
		return null;
	}
	
	/**
	 * @Title: queryProbaseinfoByCaseid
	 * @Description: 根据条件返回某条流程记录
	 * @author ztt
	 * @date 2016年11月2日
	 * @param caseid 案件ID
	 * @param protype 流程类型
	 * @param nodeid 节点编号
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	public List<JSONObject> queryProbaseinfo(String caseid, String protype, BigDecimal nodeid, String result) {
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='HEAR' and a.status=0 and a.code = '").append(result).append("') result_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PUB_PROBASEINFO_END' and a.status=0 and a.code = t.reason) reason_mc ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + protype + "'");
		
		if(nodeid != null){
			sql.append(" and t.nodeid = " + nodeid);
		}
		sql.append(" order by t.endtime desc"); 
		
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		
		return probaseinfoList;
	}
	
	/**
	 * @Title: queryProbaseinfoSumForWait
	 * @Description:系统首页饼状图：统计待办业务条数
	 * @author ybb
	 * @date 2016年8月25日 上午9:26:09
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryProbaseinfoSumForWait() throws AppException{
		
		//获取List，用来返回结果
		List<Map<String, Object>> probaseinfoList = new ArrayList<Map<String, Object>>();
		
		//用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("select '审批' name,102 value from dual ");
		sql.append("union all ");
		sql.append("select '中止' name,45 value from dual ");
		sql.append("union all ");
		sql.append("select '恢复' name,70 value from dual ");
		sql.append("union all ");
		sql.append("select '终止' name,28 value from dual ");
		sql.append("union all ");
		sql.append("select '和解' name,9 value from dual ");
		sql.append("union all ");
		sql.append("select '延期' name,76 value from dual ");
		sql.append("union all ");
		sql.append("select '撤销' name,15 value from dual ");
		sql.append("union all ");
		sql.append("select '回避' name,19 value from dual ");
		
		List<JSONObject> jsonList = mapDataDao.queryListBySQL(sql.toString());
		for (JSONObject obj : jsonList) {
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("name", obj.getString("name"));
			map.put("value", obj.getString("value"));
			
			probaseinfoList.add(map);
		}
		
		return probaseinfoList;
	}
	
	/**
	 * @Title: queryProbaseinfoSumForWait
	 * @Description:系统首页柱状图：统计复议案件总数
	 * @author LXF
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<Map<String, Object>> queryCaseTotalAnalysis(Map<String, Object> param) throws AppException{
		// 获取系统当前日期
		String currentDate = FormatUtil.stringDate2().substring(0, 4);
		String startyear = currentDate;
		String qStartYear = StringUtil.stringConvert(param.get("year"));
		if (qStartYear != null && !qStartYear.trim().equals("")) {
			startyear = qStartYear;
		}
		
		//获取List，用来返回结果
		List<Map<String, Object>> probaseinfoList = new ArrayList<Map<String, Object>>();
		
		String sql="select '案件总数' name, count(*) value                                                       "+
					"  from b_casebaseinfo b                                                                      "+
					" where 1 = 1                                                                                 "+
					"   and substr(b.appdate, 1, 4) = '"+startyear+"'                                                      "+
					"                                                                                             "+
					"union all                                                                                    "+
					"select '已完成' name, count(*) value                                                         "+
					"  from pub_resultbaseinfo p, b_casebaseinfo b                                                "+
					" where p.caseid = b.caseid                                                                   "+
					"   and substr(b.appdate, 1, 4) = '"+startyear+"'                                                      "+
					"   and p.protype = '01'                                                                      "+
					"                                                                                             "+
					"union all                                                                                    "+
					"select '已受理' name, count(*) value                                                         "+
					"  from b_casebaseinfo b                                                                      "+
					" where substr(b.appdate, 1, 4) = '"+startyear+"'                                                      "+
					"   and b.caseid in                                                                           "+
					"       (select distinct p.caseid                                                             "+
					"          from pub_probaseinfo p                                                             "+
					"         where p.protype = '01'                                                              "+
					"           and p.nodeid = 2                                                                  "+
					"           and p.result = '01'                                                               "+
					"           and p.caseid not in                                                               "+
					"               (select distinct caseid from pub_resultbaseinfo))                             "+
					"union all                                                                                    "+
					"select '未受理' name, (tcount - nvl(fcount, 0) - nvl(rcount，0)) value                       "+
					"  from (select count(*) tcount                                                               "+
					"          from b_casebaseinfo b                                                              "+
					"         where 1 = 1                                                                         "+
					"           and substr(b.appdate, 1, 4) = '"+startyear+"') tt                                          "+
					"  left join (select count(*) fcount                                                          "+
					"               from pub_resultbaseinfo p, b_casebaseinfo b                                   "+
					"              where p.caseid = b.caseid                                                      "+
					"                and substr(b.appdate, 1, 4) = '"+startyear+"'                                         "+
					"                and p.protype = '01') ff                                                     "+
					"    on 1 = 1                                                                                 "+
					"  left join (select count(*) rcount                                                          "+
					"               from b_casebaseinfo b                                                         "+
					"              where substr(b.appdate, 1, 4) = '"+startyear+"'                                         "+
					"                and b.caseid in                                                              "+
					"                    (select distinct p.caseid                                                "+
					"                       from pub_probaseinfo p                                                "+
					"                      where p.protype = '01'                                                 "+
					"                        and p.nodeid = 2                                                     "+
					"                        and p.result = '01'                                                  "+
					"                        and p.caseid not in                                                  "+
					"                            (select distinct caseid from pub_resultbaseinfo))) rr            "+
					"    on 1 = 1                                                                                 ";
		
		List<JSONObject> jsonList = mapDataDao.queryListBySQL(sql.toString());
		for (JSONObject obj : jsonList) {
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("name", obj.getString("name"));
			map.put("value", obj.getString("value"));
			
			probaseinfoList.add(map);
		}
		/*演示数据*/
		probaseinfoList.clear();
		Map<String,Object> map1 = new HashMap<String, Object>();
		map1.put("name", "案件总数");
		map1.put("value", 20);
		Map<String,Object> map2 = new HashMap<String, Object>();
		map2.put("name", "已完成");
		map2.put("value", 0);
		Map<String,Object> map3 = new HashMap<String, Object>();
		map3.put("name", "已受理");
		map3.put("value", 5);
		Map<String,Object> map4 = new HashMap<String, Object>();
		map4.put("name", "未受理");
		map4.put("value", 15);
		probaseinfoList.add(map1);
		probaseinfoList.add(map2);
		probaseinfoList.add(map3);
		probaseinfoList.add(map4);
		/*end*/
		return probaseinfoList;
	}
	
	/**
	 * @Title: queryNodeidByCaseid
	 * @Description: 根据案件ID查询案件当前节点号
	 * @author ybb
	 * @date 2016年9月6日 下午2:22:35
	 * @param caseid
	 * @return
	 * @throws AppException
	 */
	public BigDecimal queryNodeidByCaseid(String caseid) throws AppException{
		
		//节点编号
		BigDecimal nodeid = null;
		
		//根据案件ID查询
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if (casebaseinfo == null) {
			nodeid = BigDecimal.ONE;
		}
		if (casebaseinfo.getNodeid() == null){
			nodeid = BigDecimal.ONE;
		}
		
		//查询该案件是否已完结
			//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_RESULTBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		
		List<?> probaseinfoList = probaseinfoDao.findVoBySql(sql.toString(), Probaseinfo.class);
		if (probaseinfoList != null && !probaseinfoList.isEmpty()) {	//已完结
			nodeid = new BigDecimal(GCC.PRONODEBASEINFO_NODEID_HEAREND);
		} else {
			nodeid = casebaseinfo.getNodeid();
		}
		
		return nodeid;
	}
	
	/**
	 * @Title: queryNodeidByCaseid
	 * @Description: 根据案件ID和流程类型查询案件当前节点号
	 * @author ybb
	 * @date 2016年9月6日 下午3:50:08
	 * @param caseid
	 * @param protype
	 * @return
	 * @throws AppException
	 */
	public BigDecimal queryNodeidByCaseid(String caseid, String protype) throws AppException{
		
		//节点编号
		BigDecimal nodeid = null;
		
		//根据案件ID查询
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if (casebaseinfo == null) {
			nodeid = BigDecimal.ONE;
		}
		if (casebaseinfo.getNodeid() == null){
			nodeid = BigDecimal.ONE;
		}
		
		//查询该案件是否已完结
	    //获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_RESULTBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + protype + "'");
		
		List<?> probaseinfoList = probaseinfoDao.findVoBySql(sql.toString(), Probaseinfo.class);
		if (probaseinfoList != null && !probaseinfoList.isEmpty()) {	//已完结
			
			nodeid = new BigDecimal(GCC.PRONODEBASEINFO_NODEID_HEAREND);
			
		} else {
			
			//查询流程过程表，判断该流程到哪个环节
			sql.setLength(0);
			sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
			sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
			sql.append(" from PUB_PROBASEINFO t ");
			sql.append(" where t.caseid = '" + caseid + "'");
			sql.append(" and t.protype = '" + protype + "'");
			sql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
			
			List<?> probaseinfos = probaseinfoDao.findVoBySql(sql.toString(), Probaseinfo.class);
			if (probaseinfos != null && !probaseinfos.isEmpty()) {	//已到审批环节
				
				nodeid = new BigDecimal(GCC.PRONODEBASEINFO_NODEID_HEAR);
				
			} else {
				nodeid = BigDecimal.ONE;
			}
		}
		
		return nodeid;
	}
	
	/**
	 * @Title: queryProbaseinfosByCaseid
	 * @Description: 根据流程ID与流程类型查询流程信息列表
	 * @author ybb
	 * @date 2016年9月6日 下午5:35:52
	 * @param caseid
	 * @param protype
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	public List<JSONObject> queryProbaseinfosByCaseid(String caseid, String protype) throws AppException{
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'NODENAME' and a.status = 0 and a.code = nvl(t.nodeid,0)) nodeid_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'PUB_PROBASEINFO_OPTTYPE' and a.status = 0 and a.code = t.opttype) opttype_mc ");
		sql.append(" from VIEW_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + protype + "'");
		sql.append(" order by t.starttime ");
		
		//执行查询
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		
		return probaseinfoList;
	}
	
	/**
	 * @Title: queryProbaseinfoForSuspend
	 * @Description: 根据流程ID与流程类型查询流程信息列表（复议中止）
	 * @author ybb
	 * @date 2016年9月20日 上午11:21:56
	 * @param caseid
	 * @param protype
	 * @param opttype
	 * @param nodeid
	 * @return
	 */
	public Probaseinfo queryProbaseinfoForSuspend(String caseid, String protype, String opttype, BigDecimal nodeid) {
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.userid, t.sendunit, t.resultmsg, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'PUB_PROBASEINFO_SUSPEND' and a.status = 0 and a.code = t.reason) reason ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + protype + "'");
		sql.append(" and t.opttype = '" + opttype + "'");
		
		if(nodeid != null){
			sql.append(" and t.nodeid = " + nodeid);
		}
		sql.append(" order by t.endtime desc"); 
		
		List<?> probaseinfoList = probaseinfoDao.findVoBySql(sql.toString(), Probaseinfo.class);
		if(probaseinfoList != null && !probaseinfoList.isEmpty()){
			return (Probaseinfo) probaseinfoList.get(0);
		}
		
		return null;
	}
	
	/**
	 * @Title: queryProbaseinfoForEnd
	 * @Description: 根据流程ID与流程类型查询流程信息列表（复议终止）
	 * @author ybb
	 * @date 2016年9月20日 上午11:35:03
	 * @param caseid
	 * @param protype
	 * @param opttype
	 * @param nodeid
	 * @return
	 */
	public Probaseinfo queryProbaseinfoForEnd(String caseid, String protype, String opttype, BigDecimal nodeid) {
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.userid, t.sendunit, t.resultmsg, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'PUB_PROBASEINFO_END' and a.status = 0 and a.code = t.reason) reason ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + protype + "'");
		sql.append(" and t.opttype = '" + opttype + "'");
		
		if(nodeid != null){
			sql.append(" and t.nodeid = " + nodeid);
		}
		sql.append(" order by t.endtime desc"); 
		
		List<?> probaseinfoList = probaseinfoDao.findVoBySql(sql.toString(), Probaseinfo.class);
		if(probaseinfoList != null && !probaseinfoList.isEmpty()){
			return (Probaseinfo) probaseinfoList.get(0);
		}
		
		return null;
	}
	
	/**
	 * @Title: queryNodeidByRcaseid
	 * @Description: 被复议案件管理-查询流程节点
	 * @author ybb
	 * @date 2016年9月21日 下午1:25:00
	 * @param rcaseid
	 * @return
	 * @throws AppException
	 */
	public BigDecimal queryNodeidByRcaseid(String rcaseid) throws AppException{
		
		//节点编号
		BigDecimal nodeid = null;
		
		//根据案件ID查询
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoDao.get(rcaseid);
		if (rcasebaseinfo == null) {
			nodeid = new BigDecimal(GCC.RCASEBASEINFO_STATE_REQ);
		}
		if (rcasebaseinfo.getNodeid() == null){
			nodeid = new BigDecimal(GCC.RCASEBASEINFO_STATE_REQ);
		}
		
		//查询该案件是否已完结
			//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_RESULTBASEINFO t ");
		sql.append(" where t.caseid = '" + rcaseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
		
		List<?> probaseinfoList = probaseinfoDao.findVoBySql(sql.toString(), Probaseinfo.class);
		if (probaseinfoList != null && !probaseinfoList.isEmpty()) {	//已完结
			nodeid = new BigDecimal(GCC.RCASEBASEINFO_NODEID_END);
		} else {
			nodeid = rcasebaseinfo.getNodeid();
		}
		
		return nodeid;
	}
	
	/**
	 * @Title: queryNodeidByAcaseid
	 * @Description: 应诉案件管理-查询流程节点
	 * @author ybb
	 * @date 2016年9月22日17:42:23
	 * @param rcaseid
	 * @return
	 * @throws AppException
	 */
	public BigDecimal queryNodeidByAcaseid(String acaseid) throws AppException{
		
		//节点编号
		BigDecimal nodeid = null;
		
		//根据案件ID查询
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoDao.get(acaseid);
		if (admlitbaseinfo == null) {
			nodeid = new BigDecimal(GCC.RCASEBASEINFO_STATE_REQ);
		}
		if (admlitbaseinfo.getNodeid() == null){
			nodeid = new BigDecimal(GCC.RCASEBASEINFO_STATE_REQ);
		}
		
		//查询该案件是否已完结
			//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_RESULTBASEINFO t ");
		sql.append(" where t.caseid = '" + acaseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
		
		List<?> probaseinfoList = probaseinfoDao.findVoBySql(sql.toString(), Probaseinfo.class);
		if (probaseinfoList != null && !probaseinfoList.isEmpty()) {	//已完结
			nodeid = new BigDecimal(GCC.ADMLITBASEINFO_NODEID_END);
		} else {
			nodeid = admlitbaseinfo.getNodeid();
		}
		
		return nodeid;
	}
	
	/**
	 * @Title: queryFlowForRcasebaseinfo
	 * @Description: 被复议案件-查询流程列表
	 * @author ybb
	 * @date 2016年9月23日 上午9:49:54
	 * @param caseid
	 * @param protype
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	public List<JSONObject> queryFlowForRcasebaseinfo(String caseid, String protype) throws AppException{
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'RCASEBASEINFO_NODENAME' and a.status = 0 and a.code = nvl(t.nodeid,0)) nodeid_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'PUB_PROBASEINFO_OPTTYPE' and a.status = 0 and a.code = t.opttype) opttype_mc ");
		sql.append(" from VIEW_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + protype + "'");
		sql.append(" order by t.starttime ");
		
		//执行查询
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		
		return probaseinfoList;
	}
	
	/**
	 * @Title: queryFlowForAdmlitbaseinfo
	 * @Description: 应诉案件-查询流程列表
	 * @author ybb
	 * @date 2016年9月23日 上午9:50:53
	 * @param caseid
	 * @param protype
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	public List<JSONObject> queryFlowForAdmlitbaseinfo(String caseid, String protype) throws AppException{
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'ADMLITBASEINFO_NODENAME' and a.status = 0 and a.code = nvl(t.nodeid,0)) nodeid_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'PUB_PROBASEINFO_OPTTYPE' and a.status = 0 and a.code = t.opttype) opttype_mc ");
		sql.append(" from VIEW_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + protype + "'");
		sql.append(" order by t.starttime ");
		
		//执行查询
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		
		return probaseinfoList;
	}
	
	/**
	 * @Title: queryProbaseinfoByProcessid
	 * @Description: 根据流程ID获取流程列表
	 * @author ybb
	 * @date 2017年3月22日 下午6:35:32
	 * @param ccrid
	 * @param protype
	 * @param nodeid
	 * @return
	 */
	public Probaseinfo queryProbaseinfoByProcessid(String ccrid, String protype, BigDecimal nodeid) {
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.processid = '" + ccrid + "'");
		sql.append(" and t.protype = '" + protype + "'");
		
		if(nodeid != null){
			sql.append(" and t.nodeid = " + nodeid);
		}
		sql.append(" order by t.endtime desc"); 
		
		List<?> probaseinfoList = probaseinfoDao.findVoBySql(sql.toString(), Probaseinfo.class);
		if(probaseinfoList != null && !probaseinfoList.isEmpty()){
			return (Probaseinfo) probaseinfoList.get(0);
		}
		
		return null;
	}
}
