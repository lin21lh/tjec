package com.wfzcx.aros.tjfx.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.FormatUtil;
import com.jbf.common.util.StringUtil;


@Scope("prototype")
@Service("com.wfzcx.aros.tjfx.service.CaseAnalysisService")
public class CaseAnalysisService {
	@Autowired
	MapDataDaoI mapDataDao;

	/**
	 * 
	 * @param param
	 * @return
	 */
	public PaginationSupport query(Map<String, Object> param) {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		String sql ="select 100 as appamount,80 as receiveamount,60 as finishamount,20 as result01,500 as result05,40 as comamount , 60 as amount from dual";
				

		System.out.println("sql="+sql);
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	public List<Map> queryList(String anaType,Map<String, Object> param) {
		List<Map> list = null;
		if (anaType != null && !anaType.trim().equals("")) {
			if (anaType.trim().equals("1")) {
				list = queryAdminList(param);
			} else if (anaType.trim().equals("2")) {
				list = queryRegionList(param);
			} else if (anaType.trim().equals("3")) {
				list = queryDefList(param);
			} else if (anaType.trim().equals("4")) {
				list = queryCaseList(param);
			} else if (anaType.trim().equals("5")) {
				list = queryResList(param);
			} else if (anaType.trim().equals("6")) {
				list = queryTrialList(param);
			} else if (anaType.trim().equals("7")) {
				list = queryStateList(param);
			} else if (anaType.trim().equals("8")) {
				list = queryYearList(param);
			} else if (anaType.trim().equals("9")) {
				list = queryAdmLitList(param);
			} else if (anaType.trim().equals("10")) {
				list = queryGreatList(param);
			} else if (anaType.trim().equals("11")) {
				list = querySpeList(param);
			}
			
		}
		return list;
	}
	
	/**
	 * 按照行政管理类型统计
	 * @return
	 */
	public List<Map> queryAdminList(Map<String, Object> param) {
		// 获取系统当前日期
		String currentDate = FormatUtil.stringDate2().substring(0, 4);
		String startyear = currentDate;
		String qStartYear = StringUtil.stringConvert(param.get("startyear"));
		if (qStartYear != null && !qStartYear.trim().equals("")) {
			startyear = qStartYear;
		}
		
		String sql = "select (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ADMINLEVEL' and a.status=0  and a.code=bc.admtype) admtype,"+
					"       bc.bcount tcount,                                                                                                               "+
					"       nvl(br.rcount,0) rcount,                                                                                                        "+
					"       nvl(pr.pcount, 0) fcount,                                                                                                       "+
					"       def.C01,def.C02,def.C03,def.C04,def.C05,def.C06,def.C07,def.C08,                                                                "+
					"       cas.D01,cas.D02,cas.D03,cas.D04,cas.D05,cas.D06,cas.D07,cas.D08,cas.D09,cas.D10,                                                "+
					"       res.E01,res.E02,res.E03,res.E04,res.E05,                                                                                        "+
					"       nvl(amo.casesum,0) casesum,nvl(amo.amount,0) amount                                                                             "+
					"  from (select b.admtype admtype, count(*) bcount                                                                                      "+
					"          from b_casebaseinfo b                                                                                                        "+
					"          where substr(b.appdate,1,4)='"+startyear+"'                                                                                           "+
					"         group by b.admtype                                                                                                            "+
					"         order by b.admtype) bc                                                                                                        "+
					"  left join (select b.admtype admtype, count(*) pcount                                                                                 "+
					"               from b_casebaseinfo b                                                                                                   "+
					"              where b.caseid in (select distinct p.caseid                                                                              "+
					"                                   from pub_resultbaseinfo p                                                                           "+
					"                                  where p.protype = '01')                                                                              "+
					"              group by b.admtype                                                                                                       "+
					"              order by b.admtype) pr                                                                                                   "+
					"    on bc.admtype = pr.admtype                                                                                                         "+
					"  left join (select b.admtype admtype, count(*) rcount                                                                                 "+
					"               from b_casebaseinfo b                                                                                                   "+
					"              where b.caseid in                                                                                                        "+
					"                    (select distinct p.caseid                                                                                          "+
					"                       from pub_probaseinfo p                                                                                          "+
					"                      where p.protype = '01'                                                                                           "+
					"                        and p.nodeid = 2                                                                                               "+
					"                        and p.result = '01'                                                                                            "+
					"                        and p.caseid not in                                                                                            "+
					"                            (select distinct caseid from pub_resultbaseinfo))                                                          "+
					"              group by b.admtype                                                                                                       "+
					"                                                                                                                                       "+
					"             ) br                                                                                                                      "+
					"    on bc.admtype = br.admtype                                                                                                         "+
					"                                                                                                                                       "+
					"  left join (select *                                                                                                                  "+
					"               from (select da.admtype,                                                                                                "+
					"                            da.deftype,                                                                                                "+
					"                            (nvl(bc.bcount, 0) - 0) defcount                                                                           "+
					"                       from (select at.admtype, dt.deftype, 0                                                                          "+
					"                               from (select a.code deftype                                                                             "+
					"                                       from SYS_YW_DICENUMITEM a                                                                       "+
					"                                      where upper(a.elementcode) =                                                                     "+
					"                                            'B_CASEBASEINFO_DEFTYPE'                                                                   "+
					"                                        and a.status = 0) dt,                                                                          "+
					"                                    (select a.code admtype                                                                             "+
					"                                       from SYS_YW_DICENUMITEM a                                                                       "+
					"                                      where upper(a.elementcode) = 'ADMINLEVEL'                                                        "+
					"                                        and a.status = 0                                                                               "+
					"                                        and a.code in                                                                                  "+
					"                                            (select distinct admtype                                                                   "+
					"                                               from b_casebaseinfo)) at) da                                                            "+
					"                       left join (select b.admtype, b.deftype, count(*) bcount                                                         "+
					"                                   from b_casebaseinfo b                                                                               "+
					"                                  group by b.admtype, b.deftype) bc                                                                    "+
					"                         on da.admtype = bc.admtype                                                                                    "+
					"                        and da.deftype = bc.deftype) pivot(sum(defcount) for deftype in('01' as C01,                                   "+
					"                                                                                        '02' as C02,                                   "+
					"                                                                                        '03' as C03,                                   "+
					"                                                                                        '04' as C04,                                   "+
					"                                                                                        '05' as C05,                                   "+
					"                                                                                        '06' as C06,                                   "+
					"                                                                                        '07' as C07,                                   "+
					"                                                                                        '08' as C08))                                  "+
					"                                                                                                                                       "+
					"             ) def                                                                                                                     "+
					"    on bc.admtype = def.admtype                                                                                                        "+
					"  left join (select *                                                                                                                  "+
					"               from (select da.admtype,                                                                                                "+
					"                            da.casetype,                                                                                               "+
					"                            (nvl(bc.bcount, 0) - 0) defcount                                                                           "+
					"                       from (select at.admtype, dt.casetype, 0                                                                         "+
					"                               from (select a.code casetype                                                                            "+
					"                                       from SYS_YW_DICENUMITEM a                                                                       "+
					"                                      where upper(a.elementcode) =                                                                     "+
					"                                            'B_CASEBASEINFO_CASETYPE'                                                                  "+
					"                                        and a.status = 0) dt,                                                                          "+
					"                                    (select a.code admtype                                                                             "+
					"                                       from SYS_YW_DICENUMITEM a                                                                       "+
					"                                      where upper(a.elementcode) = 'ADMINLEVEL'                                                        "+
					"                                        and a.status = 0                                                                               "+
					"                                        and a.code in                                                                                  "+
					"                                            (select distinct admtype                                                                   "+
					"                                               from b_casebaseinfo)) at) da                                                            "+
					"                       left join (select b.admtype,                                                                                    "+
					"                                        b.casetype,                                                                                    "+
					"                                        count(*) bcount                                                                                "+
					"                                   from b_casebaseinfo b                                                                               "+
					"                                  group by b.admtype, b.casetype) bc                                                                   "+
					"                         on da.admtype = bc.admtype                                                                                    "+
					"                        and da.casetype = bc.casetype) pivot(sum(defcount) for casetype in('01' as D01,                                "+
					"                                                                                           '02' as D02,                                "+
					"                                                                                           '03' as D03,                                "+
					"                                                                                           '04' as D04,                                "+
					"                                                                                           '05' as D05,                                "+
					"                                                                                           '06' as D06,                                "+
					"                                                                                           '07' as D07,                                "+
					"                                                                                           '08' as D08,                                "+
					"                                                                                           '09' as D09,                                "+
					"                                                                                           '10' as D10))                               "+
					"                                                                                                                                       "+
					"             ) cas                                                                                                                     "+
					"    on bc.admtype = cas.admtype                                                                                                        "+
					"  left join (select *                                                                                                                  "+
					"               from (select da.admtype,                                                                                                "+
					"                            da.result,                                                                                                 "+
					"                            (nvl(bc.pcount, 0) - 0) rescount                                                                           "+
					"                       from (select at.admtype, dt.result, 0                                                                           "+
					"                               from (select a.code result                                                                              "+
					"                                       from SYS_YW_DICENUMITEM a                                                                       "+
					"                                      where upper(a.elementcode) = 'HEAR'                                                              "+
					"                                        and a.status = 0) dt,                                                                          "+
					"                                    (select a.code admtype                                                                             "+
					"                                       from SYS_YW_DICENUMITEM a                                                                       "+
					"                                      where upper(a.elementcode) = 'ADMINLEVEL'                                                        "+
					"                                        and a.status = 0                                                                               "+
					"                                        and a.code in                                                                                  "+
					"                                            (select distinct admtype                                                                   "+
					"                                               from b_casebaseinfo)) at) da                                                            "+
					"                       left join (select b.admtype, p.result, count(*) pcount                                                          "+
					"                                   from pub_resultbaseinfo p,                                                                          "+
					"                                        b_casebaseinfo     b                                                                           "+
					"                                  where p.caseid = b.caseid                                                                            "+
					"                                    and p.protype = '01'                                                                               "+
					"                                  group by b.admtype, p.result) bc                                                                     "+
					"                         on da.admtype = bc.admtype                                                                                    "+
					"                        and da.result = bc.result) pivot(sum(rescount) for result in('01' as E01,                                      "+
					"                                                                                     '02' as E02,                                      "+
					"                                                                                     '03' as E03,                                      "+
					"                                                                                     '04' as E04,                                      "+
					"                                                                                     '05' as E05))                                     "+
					"                                                                                                                                       "+
					"             ) res                                                                                                                     "+
					"    on bc.admtype = res.admtype                                                                                                        "+
					"  left join (select b.admtype, count(*) casesum, sum(b.amount) amount                                                                  "+
					"               from b_casebaseinfo b                                                                                                   "+
					"              where b.ifcompensation = '1'                                                                                             "+
					"              group by b.admtype) amo                                                                                                  "+
					"    on bc.admtype = amo.admtype                                                                                                        "+
					" order by bc.admtype                                                                                                                   ";
	return mapDataDao.queryListBySQL(sql);
	}
	
	/**
	 * 按照行政区划统计
	 * @return
	 */
	public List<Map> queryRegionList(Map<String, Object> param) {
		// 获取系统当前日期
		String currentDate = FormatUtil.stringDate2().substring(0, 4);
		String startyear = currentDate;
		String qStartYear = StringUtil.stringConvert(param.get("startyear"));
		if (qStartYear != null && !qStartYear.trim().equals("")) {
			startyear = qStartYear;
		}
		
		String sql = "select (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SYS_AREA' and a.status=0  and a.code=bc.region) region,"+
					"       bc.bcount tcount,                                                                                                           "+
					"       nvl(br.rcount, 0) rcount,                                                                                                   "+
					"       nvl(pr.pcount, 0) fcount,                                                                                                   "+
					"       def.C01,def.C02,def.C03,def.C04,def.C05,def.C06,def.C07,def.C08,                                                            "+
					"       cas.D01,cas.D02,cas.D03,cas.D04,cas.D05,cas.D06,cas.D07,cas.D08,cas.D09,cas.D10,                                            "+
					"       res.E01,res.E02,res.E03,res.E04,res.E05,                                                                                    "+
					"       nvl(amo.casesum, 0) casesum,nvl(amo.amount, 0) amount                                                                       "+
					"  from (select b.region region, count(*) bcount                                                                                    "+
					"          from b_casebaseinfo b                                                                                                    "+
					"         where substr(b.appdate, 1, 4) = '"+startyear+"'                                                                                    "+
					"           and b.region is not null                                                                                                "+
					"         group by b.region                                                                                                         "+
					"         order by b.region) bc                                                                                                     "+
					"  left join (select b.region region, count(*) pcount                                                                               "+
					"               from b_casebaseinfo b                                                                                               "+
					"              where b.caseid in (select distinct p.caseid                                                                          "+
					"                                   from pub_resultbaseinfo p                                                                       "+
					"                                  where p.protype = '01')                                                                          "+
					"                and b.region is not null                                                                                           "+
					"              group by b.region                                                                                                    "+
					"              order by b.region) pr                                                                                                "+
					"    on bc.region = pr.region                                                                                                       "+
					"                                                                                                                                   "+
					"  left join (select b.region region, count(*) rcount                                                                               "+
					"               from b_casebaseinfo b                                                                                               "+
					"              where b.region is not null                                                                                           "+
					"                and b.caseid in                                                                                                    "+
					"                    (select distinct p.caseid                                                                                      "+
					"                       from pub_probaseinfo p                                                                                      "+
					"                      where p.protype = '01'                                                                                       "+
					"                        and p.nodeid = 2                                                                                           "+
					"                        and p.result = '01'                                                                                        "+
					"                        and p.caseid not in                                                                                        "+
					"                            (select distinct caseid from pub_resultbaseinfo))                                                      "+
					"              group by b.region) br                                                                                                "+
					"    on bc.region = br.region                                                                                                       "+
					"                                                                                                                                   "+
					"  left join (select *                                                                                                              "+
					"               from (select da.region,                                                                                             "+
					"                            da.deftype,                                                                                            "+
					"                            (nvl(bc.bcount, 0) - 0) defcount                                                                       "+
					"                       from (select at.region, dt.deftype, 0                                                                       "+
					"                               from (select a.code deftype                                                                         "+
					"                                       from SYS_YW_DICENUMITEM a                                                                   "+
					"                                      where upper(a.elementcode) =                                                                 "+
					"                                            'B_CASEBASEINFO_DEFTYPE'                                                               "+
					"                                        and a.status = 0) dt,                                                                      "+
					"                                    (select a.code region                                                                          "+
					"                                       from SYS_YW_DICENUMITEM a                                                                   "+
					"                                      where upper(a.elementcode) = 'SYS_AREA'                                                      "+
					"                                        and a.status = 0                                                                           "+
					"                                        and a.code in                                                                              "+
					"                                            (select distinct region                                                                "+
					"                                               from b_casebaseinfo                                                                 "+
					"                                              where region is not null)) at) da                                                    "+
					"                       left join (select b.region, b.deftype, count(*) bcount                                                      "+
					"                                   from b_casebaseinfo b                                                                           "+
					"                                  where b.region is not null                                                                       "+
					"                                  group by b.region, b.deftype) bc                                                                 "+
					"                         on da.region = bc.region                                                                                  "+
					"                        and da.deftype = bc.deftype) pivot(sum(defcount) for deftype in('01' as C01,                               "+
					"                                                                                        '02' as C02,                               "+
					"                                                                                        '03' as C03,                               "+
					"                                                                                        '04' as C04,                               "+
					"                                                                                        '05' as C05,                               "+
					"                                                                                        '06' as C06,                               "+
					"                                                                                        '07' as C07,                               "+
					"                                                                                        '08' as C08))                              "+
					"                                                                                                                                   "+
					"             ) def                                                                                                                 "+
					"    on bc.region = def.region                                                                                                      "+
					"  left join (select *                                                                                                              "+
					"               from (select da.region,                                                                                             "+
					"                            da.casetype,                                                                                           "+
					"                            (nvl(bc.bcount, 0) - 0) defcount                                                                       "+
					"                       from (select at.region, dt.casetype, 0                                                                      "+
					"                               from (select a.code casetype                                                                        "+
					"                                       from SYS_YW_DICENUMITEM a                                                                   "+
					"                                      where upper(a.elementcode) =                                                                 "+
					"                                            'B_CASEBASEINFO_CASETYPE'                                                              "+
					"                                        and a.status = 0) dt,                                                                      "+
					"                                    (select a.code region                                                                          "+
					"                                       from SYS_YW_DICENUMITEM a                                                                   "+
					"                                      where upper(a.elementcode) = 'SYS_AREA'                                                      "+
					"                                        and a.status = 0                                                                           "+
					"                                        and a.code in                                                                              "+
					"                                            (select distinct region                                                                "+
					"                                               from b_casebaseinfo                                                                 "+
					"                                              where region is not null)) at) da                                                    "+
					"                       left join (select b.region, b.casetype, count(*) bcount                                                     "+
					"                                   from b_casebaseinfo b                                                                           "+
					"                                  where b.region is not null                                                                       "+
					"                                  group by b.region, b.casetype) bc                                                                "+
					"                         on da.region = bc.region                                                                                  "+
					"                        and da.casetype = bc.casetype) pivot(sum(defcount) for casetype in('01' as D01,                            "+
					"                                                                                           '02' as D02,                            "+
					"                                                                                           '03' as D03,                            "+
					"                                                                                           '04' as D04,                            "+
					"                                                                                           '05' as D05,                            "+
					"                                                                                           '06' as D06,                            "+
					"                                                                                           '07' as D07,                            "+
					"                                                                                           '08' as D08,                            "+
					"                                                                                           '09' as D09,                            "+
					"                                                                                           '10' as D10))                           "+
					"                                                                                                                                   "+
					"             ) cas                                                                                                                 "+
					"    on bc.region = cas.region                                                                                                      "+
					"  left join (select *                                                                                                              "+
					"               from (select da.region,                                                                                             "+
					"                            da.result,                                                                                             "+
					"                            (nvl(bc.pcount, 0) - 0) rescount                                                                       "+
					"                       from (select at.region, dt.result, 0                                                                        "+
					"                               from (select a.code result                                                                          "+
					"                                       from SYS_YW_DICENUMITEM a                                                                   "+
					"                                      where upper(a.elementcode) = 'HEAR'                                                          "+
					"                                        and a.status = 0) dt,                                                                      "+
					"                                    (select a.code region                                                                          "+
					"                                       from SYS_YW_DICENUMITEM a                                                                   "+
					"                                      where upper(a.elementcode) = 'SYS_AREA'                                                      "+
					"                                        and a.status = 0                                                                           "+
					"                                        and a.code in                                                                              "+
					"                                            (select distinct region                                                                "+
					"                                               from b_casebaseinfo                                                                 "+
					"                                              where region is not null)) at) da                                                    "+
					"                       left join (select b.region, p.result, count(*) pcount                                                       "+
					"                                   from pub_resultbaseinfo p,                                                                      "+
					"                                        b_casebaseinfo     b                                                                       "+
					"                                  where p.caseid = b.caseid                                                                        "+
					"                                    and p.protype = '01'                                                                           "+
					"                                    and b.region is not null                                                                       "+
					"                                  group by b.region, p.result) bc                                                                  "+
					"                         on da.region = bc.region                                                                                  "+
					"                        and da.result = bc.result) pivot(sum(rescount) for result in('01' as E01,                                  "+
					"                                                                                     '02' as E02,                                  "+
					"                                                                                     '03' as E03,                                  "+
					"                                                                                     '04' as E04,                                  "+
					"                                                                                     '05' as E05))                                 "+
					"                                                                                                                                   "+
					"             ) res                                                                                                                 "+
					"    on bc.region = res.region                                                                                                      "+
					"  left join (select b.region, count(*) casesum, sum(b.amount) amount                                                               "+
					"               from b_casebaseinfo b                                                                                               "+
					"              where b.ifcompensation = '1'                                                                                         "+
					"                and b.region is not null                                                                                           "+
					"              group by b.region) amo                                                                                               "+
					"    on bc.region = amo.region                                                                                                      "+
					" order by bc.region                                                                                                                ";
		return mapDataDao.queryListBySQL(sql);
	}
	
	/**
	 * 按照被申请人类型统计
	 * @return
	 */
	public List<Map> queryDefList(Map<String, Object> param) {
		// 获取系统当前日期
		String currentDate = FormatUtil.stringDate2().substring(0, 4);
		String startyear = currentDate;
		String qStartYear = StringUtil.stringConvert(param.get("startyear"));
		if (qStartYear != null && !qStartYear.trim().equals("")) {
			startyear = qStartYear;
		}
		
		String sql = "select (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DEFTYPE' and a.status=0  and a.code=bc.deftype) deftype,"+
					"       bc.bcount tcount,                                                                                                                           "+
					"       nvl(br.rcount,0) rcount,                                                                                                                    "+
					"       nvl(pr.pcount, 0) fcount,                                                                                                                   "+
					"       cas.D01,cas.D02,cas.D03,cas.D04,cas.D05,cas.D06,cas.D07,cas.D08,cas.D09,cas.D10,                                                            "+
					"       res.E01,res.E02,res.E03,res.E04,res.E05,                                                                                                    "+
					"       nvl(amo.casesum,0) casesum,nvl(amo.amount,0) amount                                                                                         "+
					"  from (select b.deftype deftype, count(*) bcount                                                                                                  "+
					"          from b_casebaseinfo b                                                                                                                    "+
					"          where substr(b.appdate, 1, 4) = '"+startyear+"'                                                                                                   "+
					"         group by b.deftype                                                                                                                        "+
					"         order by b.deftype) bc                                                                                                                    "+
					"  left join (select b.deftype deftype, count(*) pcount                                                                                             "+
					"               from b_casebaseinfo b                                                                                                               "+
					"              where b.caseid in (select distinct p.caseid                                                                                          "+
					"                                   from pub_resultbaseinfo p                                                                                       "+
					"                                  where p.protype = '01')                                                                                          "+
					"              group by b.deftype                                                                                                                   "+
					"              order by b.deftype) pr                                                                                                               "+
					"    on bc.deftype = pr.deftype                                                                                                                     "+
					"                                                                                                                                                   "+
					"  left join (select b.deftype deftype, count(*) rcount                                                                                             "+
					"               from b_casebaseinfo b                                                                                                               "+
					"              where b.caseid in                                                                                                                    "+
					"                    (select distinct p.caseid                                                                                                      "+
					"                       from pub_probaseinfo p                                                                                                      "+
					"                      where p.protype = '01'                                                                                                       "+
					"                        and p.nodeid = 2                                                                                                           "+
					"                        and p.result = '01'                                                                                                        "+
					"                        and p.caseid not in                                                                                                        "+
					"                            (select distinct caseid from pub_resultbaseinfo))                                                                      "+
					"              group by b.deftype                                                                                                                   "+
					"                                                                                                                                                   "+
					"             ) br                                                                                                                                  "+
					"    on bc.deftype = br.deftype                                                                                                                     "+
					"                                                                                                                                                   "+
					"  left join (select *                                                                                                                              "+
					"               from (select da.deftype,                                                                                                            "+
					"                            da.casetype,                                                                                                           "+
					"                            (nvl(bc.bcount, 0) - 0) defcount                                                                                       "+
					"                       from (select at.deftype, dt.casetype, 0                                                                                     "+
					"                               from (select a.code casetype                                                                                        "+
					"                                       from SYS_YW_DICENUMITEM a                                                                                   "+
					"                                      where upper(a.elementcode) =                                                                                 "+
					"                                            'B_CASEBASEINFO_CASETYPE'                                                                              "+
					"                                        and a.status = 0) dt,                                                                                      "+
					"                                    (select a.code deftype                                                                                         "+
					"                                       from SYS_YW_DICENUMITEM a                                                                                   "+
					"                                      where upper(a.elementcode) = 'B_CASEBASEINFO_DEFTYPE'                                                        "+
					"                                        and a.status = 0                                                                                           "+
					"                                        and a.code in                                                                                              "+
					"                                            (select distinct deftype                                                                               "+
					"                                               from b_casebaseinfo)) at) da                                                                        "+
					"                       left join (select b.deftype,                                                                                                "+
					"                                        b.casetype,                                                                                                "+
					"                                        count(*) bcount                                                                                            "+
					"                                   from b_casebaseinfo b                                                                                           "+
					"                                  group by b.deftype, b.casetype) bc                                                                               "+
					"                         on da.deftype = bc.deftype                                                                                                "+
					"                        and da.casetype = bc.casetype) pivot(sum(defcount) for casetype in('01' as D01,                                            "+
					"                                                                                           '02' as D02,                                            "+
					"                                                                                           '03' as D03,                                            "+
					"                                                                                           '04' as D04,                                            "+
					"                                                                                           '05' as D05,                                            "+
					"                                                                                           '06' as D06,                                            "+
					"                                                                                           '07' as D07,                                            "+
					"                                                                                           '08' as D08,                                            "+
					"                                                                                           '09' as D09,                                            "+
					"                                                                                           '10' as D10))                                           "+
					"                                                                                                                                                   "+
					"             ) cas                                                                                                                                 "+
					"    on bc.deftype = cas.deftype                                                                                                                    "+
					"  left join (select *                                                                                                                              "+
					"               from (select da.deftype,                                                                                                            "+
					"                            da.result,                                                                                                             "+
					"                            (nvl(bc.pcount, 0) - 0) rescount                                                                                       "+
					"                       from (select at.deftype, dt.result, 0                                                                                       "+
					"                               from (select a.code result                                                                                          "+
					"                                       from SYS_YW_DICENUMITEM a                                                                                   "+
					"                                      where upper(a.elementcode) = 'HEAR'                                                                          "+
					"                                        and a.status = 0) dt,                                                                                      "+
					"                                    (select a.code deftype                                                                                         "+
					"                                       from SYS_YW_DICENUMITEM a                                                                                   "+
					"                                      where upper(a.elementcode) = 'B_CASEBASEINFO_DEFTYPE'                                                        "+
					"                                        and a.status = 0                                                                                           "+
					"                                        and a.code in                                                                                              "+
					"                                            (select distinct deftype                                                                               "+
					"                                               from b_casebaseinfo)) at) da                                                                        "+
					"                       left join (select b.deftype, p.result, count(*) pcount                                                                      "+
					"                                   from pub_resultbaseinfo p,                                                                                      "+
					"                                        b_casebaseinfo     b                                                                                       "+
					"                                  where p.caseid = b.caseid                                                                                        "+
					"                                    and p.protype = '01'                                                                                           "+
					"                                  group by b.deftype, p.result) bc                                                                                 "+
					"                         on da.deftype = bc.deftype                                                                                                "+
					"                        and da.result = bc.result) pivot(sum(rescount) for result in('01' as E01,                                                  "+
					"                                                                                     '02' as E02,                                                  "+
					"                                                                                     '03' as E03,                                                  "+
					"                                                                                     '04' as E04,                                                  "+
					"                                                                                     '05' as E05))                                                 "+
					"                                                                                                                                                   "+
					"             ) res                                                                                                                                 "+
					"    on bc.deftype = res.deftype                                                                                                                    "+
					"  left join (select b.deftype, count(*) casesum, sum(b.amount) amount                                                                              "+
					"               from b_casebaseinfo b                                                                                                               "+
					"              where b.ifcompensation = '1'                                                                                                         "+
					"              group by b.deftype) amo                                                                                                              "+
					"    on bc.deftype = amo.deftype                                                                                                                    "+
					" order by bc.deftype                                                                                                                               ";
		return mapDataDao.queryListBySQL(sql);
	}
	
	/**
	 * 按照申请事项类型统计
	 * @return
	 */
	public List<Map> queryCaseList(Map<String, Object> param) {
		// 获取系统当前日期
		String currentDate = FormatUtil.stringDate2().substring(0, 4);
		String startyear = currentDate;
		String qStartYear = StringUtil.stringConvert(param.get("startyear"));
		if (qStartYear != null && !qStartYear.trim().equals("")) {
			startyear = qStartYear;
		}
		
		String sql ="select (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_CASETYPE' and a.status=0  and a.code=bc.casetype) casetype,"+
					"       bc.bcount tcount,                                                                                                                              "+
					"       nvl(br.rcount,0) rcount,                                                                                                                       "+
					"       nvl(pr.pcount, 0) fcount,                                                                                                                      "+
					"       def.C01,def.C02,def.C03,def.C04,def.C05,def.C06,def.C07,def.C08,                                                                               "+
					"       res.E01,res.E02,res.E03,res.E04,res.E05,                                                                                                       "+
					"       nvl(amo.casesum,0) casesum,nvl(amo.amount,0) amount                                                                                            "+
					"  from (select b.casetype casetype, count(*) bcount                                                                                                   "+
					"          from b_casebaseinfo b                                                                                                                       "+
					"          where substr(b.appdate,1,4)='"+startyear+"'                                                                                                          "+
					"         group by b.casetype                                                                                                                          "+
					"         order by b.casetype) bc                                                                                                                      "+
					"  left join (select b.casetype casetype, count(*) pcount                                                                                              "+
					"               from b_casebaseinfo b                                                                                                                  "+
					"              where b.caseid in (select distinct p.caseid                                                                                             "+
					"                                   from pub_resultbaseinfo p                                                                                          "+
					"                                  where p.protype = '01')                                                                                             "+
					"              group by b.casetype                                                                                                                     "+
					"              order by b.casetype) pr                                                                                                                 "+
					"    on bc.casetype = pr.casetype                                                                                                                      "+
					"                                                                                                                                                      "+
					"  left join (select b.casetype casetype, count(*) rcount                                                                                              "+
					"               from b_casebaseinfo b                                                                                                                  "+
					"              where b.caseid in                                                                                                                       "+
					"                    (select distinct p.caseid                                                                                                         "+
					"                       from pub_probaseinfo p                                                                                                         "+
					"                      where p.protype = '01'                                                                                                          "+
					"                        and p.nodeid = 2                                                                                                              "+
					"                        and p.result = '01'                                                                                                           "+
					"                        and p.caseid not in                                                                                                           "+
					"                            (select distinct caseid from pub_resultbaseinfo))                                                                         "+
					"              group by b.casetype                                                                                                                     "+
					"                                                                                                                                                      "+
					"             ) br                                                                                                                                     "+
					"    on bc.casetype = br.casetype                                                                                                                      "+
					"                                                                                                                                                      "+
					"  left join (select *                                                                                                                                 "+
					"               from (select da.casetype,                                                                                                              "+
					"                            da.deftype,                                                                                                               "+
					"                            (nvl(bc.bcount, 0) - 0) defcount                                                                                          "+
					"                       from (select at.casetype, dt.deftype, 0                                                                                        "+
					"                               from (select a.code deftype                                                                                            "+
					"                                       from SYS_YW_DICENUMITEM a                                                                                      "+
					"                                      where upper(a.elementcode) =                                                                                    "+
					"                                            'B_CASEBASEINFO_DEFTYPE'                                                                                  "+
					"                                        and a.status = 0) dt,                                                                                         "+
					"                                    (select a.code casetype                                                                                           "+
					"                                       from SYS_YW_DICENUMITEM a                                                                                      "+
					"                                      where upper(a.elementcode) = 'B_CASEBASEINFO_CASETYPE'                                                          "+
					"                                        and a.status = 0                                                                                              "+
					"                                        and a.code in                                                                                                 "+
					"                                            (select distinct casetype                                                                                 "+
					"                                               from b_casebaseinfo)) at) da                                                                           "+
					"                       left join (select b.casetype, b.deftype, count(*) bcount                                                                       "+
					"                                   from b_casebaseinfo b                                                                                              "+
					"                                  group by b.casetype, b.deftype) bc                                                                                  "+
					"                         on da.casetype = bc.casetype                                                                                                 "+
					"                        and da.deftype = bc.deftype) pivot(sum(defcount) for deftype in('01' as C01,                                                  "+
					"                                                                                        '02' as C02,                                                  "+
					"                                                                                        '03' as C03,                                                  "+
					"                                                                                        '04' as C04,                                                  "+
					"                                                                                        '05' as C05,                                                  "+
					"                                                                                        '06' as C06,                                                  "+
					"                                                                                        '07' as C07,                                                  "+
					"                                                                                        '08' as C08))                                                 "+
					"                                                                                                                                                      "+
					"             ) def                                                                                                                                    "+
					"    on bc.casetype = def.casetype                                                                                                                     "+
					"   left join (select *                                                                                                                                "+
					"               from (select da.casetype,                                                                                                              "+
					"                            da.result,                                                                                                                "+
					"                            (nvl(bc.pcount, 0) - 0) rescount                                                                                          "+
					"                       from (select at.casetype, dt.result, 0                                                                                         "+
					"                               from (select a.code result                                                                                             "+
					"                                       from SYS_YW_DICENUMITEM a                                                                                      "+
					"                                      where upper(a.elementcode) = 'HEAR'                                                                             "+
					"                                        and a.status = 0) dt,                                                                                         "+
					"                                    (select a.code casetype                                                                                           "+
					"                                       from SYS_YW_DICENUMITEM a                                                                                      "+
					"                                      where upper(a.elementcode) = 'B_CASEBASEINFO_CASETYPE'                                                          "+
					"                                        and a.status = 0                                                                                              "+
					"                                        and a.code in                                                                                                 "+
					"                                            (select distinct casetype                                                                                 "+
					"                                               from b_casebaseinfo)) at) da                                                                           "+
					"                       left join (select b.casetype, p.result, count(*) pcount                                                                        "+
					"                                   from pub_resultbaseinfo p,                                                                                         "+
					"                                        b_casebaseinfo     b                                                                                          "+
					"                                  where p.caseid = b.caseid                                                                                           "+
					"                                    and p.protype = '01'                                                                                              "+
					"                                  group by b.casetype, p.result) bc                                                                                   "+
					"                         on da.casetype = bc.casetype                                                                                                 "+
					"                        and da.result = bc.result) pivot(sum(rescount) for result in('01' as E01,                                                     "+
					"                                                                                     '02' as E02,                                                     "+
					"                                                                                     '03' as E03,                                                     "+
					"                                                                                     '04' as E04,                                                     "+
					"                                                                                     '05' as E05))                                                    "+
					"                                                                                                                                                      "+
					"             ) res                                                                                                                                    "+
					"    on bc.casetype = res.casetype                                                                                                                     "+
					"  left join (select b.casetype, count(*) casesum, sum(b.amount) amount                                                                                "+
					"               from b_casebaseinfo b                                                                                                                  "+
					"              where b.ifcompensation = '1'                                                                                                            "+
					"              group by b.casetype) amo                                                                                                                "+
					"    on bc.casetype = amo.casetype                                                                                                                     "+
					" order by bc.casetype                                                                                                                                 ";
		return mapDataDao.queryListBySQL(sql);
	}
	
	/**
	 * 按照审议结果统计
	 * @return
	 */
	public List<Map> queryResList(Map<String, Object> param) {
		// 获取系统当前日期
		String currentDate = FormatUtil.stringDate2().substring(0, 4);
		String startyear = currentDate;
		String qStartYear = StringUtil.stringConvert(param.get("startyear"));
		if (qStartYear != null && !qStartYear.trim().equals("")) {
			startyear = qStartYear;
		}
		
		String sql ="select (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='HEAR' and a.status=0  and a.code=pc.result) result, "+
					"       nvl(pc.pcount, 0) pcount,                                                                                                "+
					"       def.C01,def.C02,def.C03,def.C04,def.C05,def.C06,def.C07,def.C08,                                                         "+
					"       cas.D01,cas.D02,cas.D03,cas.D04,cas.D05,cas.D06,cas.D07,cas.D08,cas.D09,cas.D10,                                         "+
					"       nvl(amo.casesum, 0) casesum,nvl(amo.amount, 0) amount                                                                    "+
					"  from (select p.result result, count(*) pcount                                                                                 "+
					"          from pub_resultbaseinfo p, b_casebaseinfo b                                                                           "+
					"         where p.caseid = b.caseid                                                                                              "+
					"           and substr(b.appdate, 1, 4) = '"+startyear+"'                                                                                 "+
					"           and p.protype = '01'                                                                                                 "+
					"         group by p.result                                                                                                      "+
					"         order by p.result) pc                                                                                                  "+
					"  left join (select *                                                                                                           "+
					"               from (select da.result,                                                                                          "+
					"                            da.deftype,                                                                                         "+
					"                            (nvl(pc.pcount, 0) - 0) defcount                                                                    "+
					"                       from (select at.result, dt.deftype, 0                                                                    "+
					"                               from (select a.code deftype                                                                      "+
					"                                       from SYS_YW_DICENUMITEM a                                                                "+
					"                                      where upper(a.elementcode) =                                                              "+
					"                                            'B_CASEBASEINFO_DEFTYPE'                                                            "+
					"                                        and a.status = 0) dt,                                                                   "+
					"                                    (select a.code result                                                                       "+
					"                                       from SYS_YW_DICENUMITEM a                                                                "+
					"                                      where upper(a.elementcode) = 'HEAR'                                                       "+
					"                                        and a.status = 0                                                                        "+
					"                                        and a.code in                                                                           "+
					"                                            (select distinct result                                                             "+
					"                                               from pub_resultbaseinfo                                                          "+
					"                                              where protype = '01')) at) da                                                     "+
					"                       left join (select p.result, b.deftype, count(*) pcount                                                   "+
					"                                   from b_casebaseinfo     b,                                                                   "+
					"                                        pub_resultbaseinfo p                                                                    "+
					"                                  where b.caseid = p.caseid                                                                     "+
					"                                    and p.protype = '01'                                                                        "+
					"                                  group by p.result, b.deftype) pc                                                              "+
					"                         on da.result = pc.result                                                                               "+
					"                        and da.deftype = pc.deftype) pivot(sum(defcount) for deftype in('01' as C01,                            "+
					"                                                                                        '02' as C02,                            "+
					"                                                                                        '03' as C03,                            "+
					"                                                                                        '04' as C04,                            "+
					"                                                                                        '05' as C05,                            "+
					"                                                                                        '06' as C06,                            "+
					"                                                                                        '07' as C07,                            "+
					"                                                                                        '08' as C08))                           "+
					"                                                                                                                                "+
					"             ) def                                                                                                              "+
					"    on pc.result = def.result                                                                                                   "+
					"  left join (select *                                                                                                           "+
					"               from (select da.result,                                                                                          "+
					"                            da.casetype,                                                                                        "+
					"                            (nvl(pc.pcount, 0) - 0) defcount                                                                    "+
					"                       from (select at.result, dt.casetype, 0                                                                   "+
					"                               from (select a.code casetype                                                                     "+
					"                                       from SYS_YW_DICENUMITEM a                                                                "+
					"                                      where upper(a.elementcode) =                                                              "+
					"                                            'B_CASEBASEINFO_CASETYPE'                                                           "+
					"                                        and a.status = 0) dt,                                                                   "+
					"                                    (select a.code result                                                                       "+
					"                                       from SYS_YW_DICENUMITEM a                                                                "+
					"                                      where upper(a.elementcode) = 'HEAR'                                                       "+
					"                                        and a.status = 0                                                                        "+
					"                                        and a.code in                                                                           "+
					"                                            (select distinct result                                                             "+
					"                                               from pub_resultbaseinfo                                                          "+
					"                                              where protype = '01')) at) da                                                     "+
					"                       left join (select p.result, b.casetype, count(*) pcount                                                  "+
					"                                   from b_casebaseinfo     b,                                                                   "+
					"                                        pub_resultbaseinfo p                                                                    "+
					"                                  where b.caseid = p.caseid                                                                     "+
					"                                    and p.protype = '01'                                                                        "+
					"                                  group by p.result, b.casetype) pc                                                             "+
					"                         on da.result = pc.result                                                                               "+
					"                        and da.casetype = pc.casetype) pivot(sum(defcount) for casetype in('01' as D01,                         "+
					"                                                                                           '02' as D02,                         "+
					"                                                                                           '03' as D03,                         "+
					"                                                                                           '04' as D04,                         "+
					"                                                                                           '05' as D05,                         "+
					"                                                                                           '06' as D06,                         "+
					"                                                                                           '07' as D07,                         "+
					"                                                                                           '08' as D08,                         "+
					"                                                                                           '09' as D09,                         "+
					"                                                                                           '10' as D10))                        "+
					"                                                                                                                                "+
					"             ) cas                                                                                                              "+
					"    on pc.result = cas.result                                                                                                   "+
					"  left join (select p.result, count(*) casesum, sum(b.amount) amount                                                            "+
					"               from b_casebaseinfo b, pub_resultbaseinfo p                                                                      "+
					"              where b.ifcompensation = '1'                                                                                      "+
					"                and b.caseid = p.caseid                                                                                         "+
					"                and p.protype = '01'                                                                                            "+
					"              group by p.result) amo                                                                                            "+
					"    on pc.result = amo.result                                                                                                   "+
					" order by pc.result                                                                                                             ";
		return mapDataDao.queryListBySQL(sql);
	}
	
	/**
	 * 按照审理方式统计
	 * @return
	 */
	public List<Map> queryTrialList(Map<String, Object> param) {
		// 获取系统当前日期
		String currentDate = FormatUtil.stringDate2().substring(0, 4);
		String startyear = currentDate;
		String qStartYear = StringUtil.stringConvert(param.get("startyear"));
		if (qStartYear != null && !qStartYear.trim().equals("")) {
			startyear = qStartYear;
		}
		
		String sql ="select (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='TRIALTYPE' and a.status=0  and a.code=bc.trialtype) trialtype,"+
					"       bc.bcount tcount,                                                                                                                  "+
					"       nvl(br.rcount,0) rcount,                                                                                                           "+
					"       nvl(pr.pcount, 0) fcount,                                                                                                          "+
					"       def.C01,def.C02,def.C03,def.C04,def.C05,def.C06,def.C07,def.C08,                                                                   "+
					"       cas.D01,cas.D02,cas.D03,cas.D04,cas.D05,cas.D06,cas.D07,cas.D08,cas.D09,cas.D10,                                                   "+
					"       res.E01,res.E02,res.E03,res.E04,res.E05,                                                                                           "+
					"       nvl(amo.casesum,0) casesum,nvl(amo.amount,0) amount                                                                                "+
					"  from (select t.trialtype trialtype, count(*) bcount                                                                                     "+
					"          from b_trialbaseinfo t, b_casebaseinfo b                                                                                        "+
					"         where substr(b.appdate, 1, 4) = '"+startyear+"'                                                                                           "+
					"           and t.caseid = b.caseid                                                                                                        "+
					"           and t.trialtype is not null                                                                                                    "+
					"         group by t.trialtype                                                                                                             "+
					"         order by t.trialtype) bc                                                                                                         "+
					"  left join (select t.trialtype trialtype, count(*) pcount                                                                                "+
					"               from b_trialbaseinfo t                                                                                                     "+
					"              where t.caseid in (select distinct p.caseid                                                                                 "+
					"                                   from pub_resultbaseinfo p                                                                              "+
					"                                  where p.protype = '01')                                                                                 "+
					"              group by t.trialtype                                                                                                        "+
					"              order by t.trialtype) pr                                                                                                    "+
					"    on bc.trialtype = pr.trialtype                                                                                                        "+
					"  left join (select t.trialtype trialtype, count(*) rcount                                                                                "+
					"               from b_trialbaseinfo t                                                                                                     "+
					"              where t.caseid in                                                                                                           "+
					"                    (select distinct p.caseid                                                                                             "+
					"                       from pub_probaseinfo p                                                                                             "+
					"                      where p.protype = '01'                                                                                              "+
					"                        and p.nodeid = 2                                                                                                  "+
					"                        and p.result = '01'                                                                                               "+
					"                        and p.caseid not in                                                                                               "+
					"                            (select distinct caseid from pub_resultbaseinfo))                                                             "+
					"              group by t.trialtype                                                                                                        "+
					"                                                                                                                                          "+
					"             ) br                                                                                                                         "+
					"    on bc.trialtype = br.trialtype                                                                                                        "+
					"                                                                                                                                          "+
					"  left join (select *                                                                                                                     "+
					"               from (select da.trialtype,                                                                                                 "+
					"                            da.deftype,                                                                                                   "+
					"                            (nvl(bc.bcount, 0) - 0) defcount                                                                              "+
					"                       from (select at.trialtype, dt.deftype, 0                                                                           "+
					"                               from (select a.code deftype                                                                                "+
					"                                       from SYS_YW_DICENUMITEM a                                                                          "+
					"                                      where upper(a.elementcode) =                                                                        "+
					"                                            'B_CASEBASEINFO_DEFTYPE'                                                                      "+
					"                                        and a.status = 0) dt,                                                                             "+
					"                                    (select a.code trialtype                                                                              "+
					"                                       from SYS_YW_DICENUMITEM a                                                                          "+
					"                                      where upper(a.elementcode) = 'TRIALTYPE'                                                            "+
					"                                        and a.status = 0                                                                                  "+
					"                                        and a.code in                                                                                     "+
					"                                            (select distinct trialtype                                                                    "+
					"                                               from b_trialbaseinfo)) at) da                                                              "+
					"                       left join (select t.trialtype,                                                                                     "+
					"                                        b.deftype,                                                                                        "+
					"                                        count(*) bcount                                                                                   "+
					"                                   from b_casebaseinfo b, b_trialbaseinfo t                                                               "+
					"                                  where b.caseid = t.caseid                                                                               "+
					"                                  group by t.trialtype, b.deftype) bc                                                                     "+
					"                         on da.trialtype = bc.trialtype                                                                                   "+
					"                        and da.deftype = bc.deftype) pivot(sum(defcount) for deftype in('01' as C01,                                      "+
					"                                                                                        '02' as C02,                                      "+
					"                                                                                        '03' as C03,                                      "+
					"                                                                                        '04' as C04,                                      "+
					"                                                                                        '05' as C05,                                      "+
					"                                                                                        '06' as C06,                                      "+
					"                                                                                        '07' as C07,                                      "+
					"                                                                                        '08' as C08))                                     "+
					"                                                                                                                                          "+
					"             ) def                                                                                                                        "+
					"    on bc.trialtype = def.trialtype                                                                                                       "+
					"  left join (select *                                                                                                                     "+
					"               from (select da.trialtype,                                                                                                 "+
					"                            da.casetype,                                                                                                  "+
					"                            (nvl(bc.bcount, 0) - 0) defcount                                                                              "+
					"                       from (select at.trialtype, dt.casetype, 0                                                                          "+
					"                               from (select a.code casetype                                                                               "+
					"                                       from SYS_YW_DICENUMITEM a                                                                          "+
					"                                      where upper(a.elementcode) =                                                                        "+
					"                                            'B_CASEBASEINFO_CASETYPE'                                                                     "+
					"                                        and a.status = 0) dt,                                                                             "+
					"                                    (select a.code trialtype                                                                              "+
					"                                       from SYS_YW_DICENUMITEM a                                                                          "+
					"                                      where upper(a.elementcode) = 'TRIALTYPE'                                                            "+
					"                                        and a.status = 0                                                                                  "+
					"                                        and a.code in                                                                                     "+
					"                                            (select distinct trialtype                                                                    "+
					"                                               from b_trialbaseinfo)) at) da                                                              "+
					"                       left join (select t.trialtype,                                                                                     "+
					"                                        b.casetype,                                                                                       "+
					"                                        count(*) bcount                                                                                   "+
					"                                   from b_casebaseinfo b, b_trialbaseinfo t                                                               "+
					"                                  where b.caseid = t.caseid                                                                               "+
					"                                  group by t.trialtype, b.casetype) bc                                                                    "+
					"                         on da.trialtype = bc.trialtype                                                                                   "+
					"                        and da.casetype = bc.casetype) pivot(sum(defcount) for casetype in('01' as D01,                                   "+
					"                                                                                           '02' as D02,                                   "+
					"                                                                                           '03' as D03,                                   "+
					"                                                                                           '04' as D04,                                   "+
					"                                                                                           '05' as D05,                                   "+
					"                                                                                           '06' as D06,                                   "+
					"                                                                                           '07' as D07,                                   "+
					"                                                                                           '08' as D08,                                   "+
					"                                                                                           '09' as D09,                                   "+
					"                                                                                           '10' as D10))                                  "+
					"                                                                                                                                          "+
					"             ) cas                                                                                                                        "+
					"    on bc.trialtype = cas.trialtype                                                                                                       "+
					"  left join (select *                                                                                                                     "+
					"               from (select da.trialtype,                                                                                                 "+
					"                            da.result,                                                                                                    "+
					"                            (nvl(bc.pcount, 0) - 0) rescount                                                                              "+
					"                       from (select at.trialtype, dt.result, 0                                                                            "+
					"                               from (select a.code result                                                                                 "+
					"                                       from SYS_YW_DICENUMITEM a                                                                          "+
					"                                      where upper(a.elementcode) = 'HEAR'                                                                 "+
					"                                        and a.status = 0) dt,                                                                             "+
					"                                    (select a.code trialtype                                                                              "+
					"                                       from SYS_YW_DICENUMITEM a                                                                          "+
					"                                      where upper(a.elementcode) = 'TRIALTYPE'                                                            "+
					"                                        and a.status = 0                                                                                  "+
					"                                        and a.code in                                                                                     "+
					"                                            (select distinct trialtype                                                                    "+
					"                                               from b_trialbaseinfo)) at) da                                                              "+
					"                       left join (select t.trialtype,                                                                                     "+
					"                                        p.result,                                                                                         "+
					"                                        count(*) pcount                                                                                   "+
					"                                   from pub_resultbaseinfo p,                                                                             "+
					"                                        b_casebaseinfo     b,                                                                             "+
					"                                        b_trialbaseinfo    t                                                                              "+
					"                                  where p.caseid = b.caseid                                                                               "+
					"                                    and b.caseid = t.caseid                                                                               "+
					"                                    and p.protype = '01'                                                                                  "+
					"                                  group by t.trialtype, p.result) bc                                                                      "+
					"                         on da.trialtype = bc.trialtype                                                                                   "+
					"                        and da.result = bc.result) pivot(sum(rescount) for result in('01' as E01,                                         "+
					"                                                                                     '02' as E02,                                         "+
					"                                                                                     '03' as E03,                                         "+
					"                                                                                     '04' as E04,                                         "+
					"                                                                                     '05' as E05))                                        "+
					"                                                                                                                                          "+
					"             ) res                                                                                                                        "+
					"    on bc.trialtype = res.trialtype                                                                                                       "+
					"  left join (select t.trialtype, count(*) casesum, sum(b.amount) amount                                                                   "+
					"               from b_casebaseinfo b, b_trialbaseinfo t                                                                                   "+
					"              where b.caseid = t.caseid                                                                                                   "+
					"                and b.ifcompensation = '1'                                                                                                "+
					"              group by t.trialtype) amo                                                                                                   "+
					"    on bc.trialtype = amo.trialtype                                                                                                       "+
					" order by bc.trialtype                                                                                                                    ";
		return mapDataDao.queryListBySQL(sql);
	}
	/**
	 * 按照案件状态统计
	 * @return
	 */
	public List<Map> queryStateList(Map<String, Object> param) {
		// 获取系统当前日期
		String currentDate = FormatUtil.stringDate2().substring(0, 4);
		String startyear = currentDate;
		String qStartYear = StringUtil.stringConvert(param.get("startyear"));
		if (qStartYear != null && !qStartYear.trim().equals("")) {
			startyear = qStartYear;
		}

		
		String sql ="select (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='CASESTATE' and a.status=0  and a.code=bc.state) state, "+
					"       bc.bcount tcount,                                                                                                           "+
					"       nvl(br.rcount,0) rcount,                                                                                                    "+
					"       nvl(pr.pcount, 0) fcount,                                                                                                   "+
					"       def.C01,def.C02,def.C03,def.C04,def.C05,def.C06,def.C07,def.C08,                                                            "+
					"       cas.D01,cas.D02,cas.D03,cas.D04,cas.D05,cas.D06,cas.D07,cas.D08,cas.D09,cas.D10,                                            "+
					"       res.E01,res.E02,res.E03,res.E04,res.E05,                                                                                    "+
					"       nvl(amo.casesum,0) casesum,nvl(amo.amount,0) amount                                                                         "+
					"  from (select b.state state, count(*) bcount                                                                                      "+
					"          from b_casebaseinfo b                                                                                                    "+
					"         where substr(b.appdate, 1, 4) = '"+startyear+"'                                                                                    "+
					"           and b.state is not null                                                                                                 "+
					"         group by b.state                                                                                                          "+
					"         order by b.state) bc                                                                                                      "+
					"  left join (select b.state state, count(*) pcount                                                                                 "+
					"               from b_casebaseinfo b                                                                                               "+
					"              where b.caseid in (select distinct p.caseid                                                                          "+
					"                                   from pub_resultbaseinfo p                                                                       "+
					"                                  where p.protype = '01')                                                                          "+
					"              group by b.state                                                                                                     "+
					"              order by b.state) pr                                                                                                 "+
					"    on bc.state = pr.state                                                                                                         "+
					"  left join (select b.state state, count(*) rcount                                                                                 "+
					"               from b_casebaseinfo b                                                                                               "+
					"              where b.caseid in                                                                                                    "+
					"                    (select distinct p.caseid                                                                                      "+
					"                       from pub_probaseinfo p                                                                                      "+
					"                      where p.protype = '01'                                                                                       "+
					"                        and p.nodeid = 2                                                                                           "+
					"                        and p.result = '01'                                                                                        "+
					"                        and p.caseid not in                                                                                        "+
					"                            (select distinct caseid from pub_resultbaseinfo))                                                      "+
					"              group by b.state                                                                                                     "+
					"                                                                                                                                   "+
					"             ) br                                                                                                                  "+
					"    on bc.state = br.state                                                                                                         "+
					"                                                                                                                                   "+
					"  left join (select *                                                                                                              "+
					"               from (select da.state,                                                                                              "+
					"                            da.deftype,                                                                                            "+
					"                            (nvl(bc.bcount, 0) - 0) defcount                                                                       "+
					"                       from (select at.state, dt.deftype, 0                                                                        "+
					"                               from (select a.code deftype                                                                         "+
					"                                       from SYS_YW_DICENUMITEM a                                                                   "+
					"                                      where upper(a.elementcode) =                                                                 "+
					"                                            'B_CASEBASEINFO_DEFTYPE'                                                               "+
					"                                        and a.status = 0) dt,                                                                      "+
					"                                    (select a.code state                                                                           "+
					"                                       from SYS_YW_DICENUMITEM a                                                                   "+
					"                                      where upper(a.elementcode) = 'CASESTATE'                                                     "+
					"                                        and a.status = 0                                                                           "+
					"                                        and a.code in                                                                              "+
					"                                            (select distinct state                                                                 "+
					"                                               from b_casebaseinfo)) at) da                                                        "+
					"                       left join (select b.state, b.deftype, count(*) bcount                                                       "+
					"                                   from b_casebaseinfo b                                                                           "+
					"                                  group by b.state, b.deftype) bc                                                                  "+
					"                         on da.state = bc.state                                                                                    "+
					"                        and da.deftype = bc.deftype) pivot(sum(defcount) for deftype in('01' as C01,                               "+
					"                                                                                        '02' as C02,                               "+
					"                                                                                        '03' as C03,                               "+
					"                                                                                        '04' as C04,                               "+
					"                                                                                        '05' as C05,                               "+
					"                                                                                        '06' as C06,                               "+
					"                                                                                        '07' as C07,                               "+
					"                                                                                        '08' as C08))                              "+
					"                                                                                                                                   "+
					"             ) def                                                                                                                 "+
					"    on bc.state = def.state                                                                                                        "+
					"  left join (select *                                                                                                              "+
					"               from (select da.state,                                                                                              "+
					"                            da.casetype,                                                                                           "+
					"                            (nvl(bc.bcount, 0) - 0) defcount                                                                       "+
					"                       from (select at.state, dt.casetype, 0                                                                       "+
					"                               from (select a.code casetype                                                                        "+
					"                                       from SYS_YW_DICENUMITEM a                                                                   "+
					"                                      where upper(a.elementcode) =                                                                 "+
					"                                            'B_CASEBASEINFO_CASETYPE'                                                              "+
					"                                        and a.status = 0) dt,                                                                      "+
					"                                    (select a.code state                                                                           "+
					"                                       from SYS_YW_DICENUMITEM a                                                                   "+
					"                                      where upper(a.elementcode) = 'CASESTATE'                                                     "+
					"                                        and a.status = 0                                                                           "+
					"                                        and a.code in                                                                              "+
					"                                            (select distinct state                                                                 "+
					"                                               from b_casebaseinfo)) at) da                                                        "+
					"                       left join (select b.state, b.casetype, count(*) bcount                                                      "+
					"                                   from b_casebaseinfo b                                                                           "+
					"                                  group by b.state, b.casetype) bc                                                                 "+
					"                         on da.state = bc.state                                                                                    "+
					"                        and da.casetype = bc.casetype) pivot(sum(defcount) for casetype in('01' as D01,                            "+
					"                                                                                           '02' as D02,                            "+
					"                                                                                           '03' as D03,                            "+
					"                                                                                           '04' as D04,                            "+
					"                                                                                           '05' as D05,                            "+
					"                                                                                           '06' as D06,                            "+
					"                                                                                           '07' as D07,                            "+
					"                                                                                           '08' as D08,                            "+
					"                                                                                           '09' as D09,                            "+
					"                                                                                           '10' as D10))                           "+
					"                                                                                                                                   "+
					"             ) cas                                                                                                                 "+
					"    on bc.state = cas.state                                                                                                        "+
					"  left join (select *                                                                                                              "+
					"               from (select da.state,                                                                                              "+
					"                            da.result,                                                                                             "+
					"                            (nvl(bc.pcount, 0) - 0) rescount                                                                       "+
					"                       from (select at.state, dt.result, 0                                                                         "+
					"                               from (select a.code result                                                                          "+
					"                                       from SYS_YW_DICENUMITEM a                                                                   "+
					"                                      where upper(a.elementcode) = 'HEAR'                                                          "+
					"                                        and a.status = 0) dt,                                                                      "+
					"                                    (select a.code state                                                                           "+
					"                                       from SYS_YW_DICENUMITEM a                                                                   "+
					"                                      where upper(a.elementcode) = 'CASESTATE'                                                     "+
					"                                        and a.status = 0                                                                           "+
					"                                        and a.code in                                                                              "+
					"                                            (select distinct state                                                                 "+
					"                                               from b_casebaseinfo)) at) da                                                        "+
					"                       left join (select b.state, p.result, count(*) pcount                                                        "+
					"                                   from pub_resultbaseinfo p,                                                                      "+
					"                                        b_casebaseinfo     b                                                                       "+
					"                                  where p.caseid = b.caseid                                                                        "+
					"                                    and p.protype = '01'                                                                           "+
					"                                  group by b.state, p.result) bc                                                                   "+
					"                         on da.state = bc.state                                                                                    "+
					"                        and da.result = bc.result) pivot(sum(rescount) for result in('01' as E01,                                  "+
					"                                                                                     '02' as E02,                                  "+
					"                                                                                     '03' as E03,                                  "+
					"                                                                                     '04' as E04,                                  "+
					"                                                                                     '05' as E05))                                 "+
					"                                                                                                                                   "+
					"             ) res                                                                                                                 "+
					"    on bc.state = res.state                                                                                                        "+
					"  left join (select b.state, count(*) casesum, sum(b.amount) amount                                                                "+
					"               from b_casebaseinfo b                                                                                               "+
					"              where b.ifcompensation = '1'                                                                                         "+
					"              group by b.state) amo                                                                                                "+
					"    on bc.state = amo.state                                                                                                        "+
					" order by bc.state                                                                                                                 ";
		return mapDataDao.queryListBySQL(sql);
	}
	
	/**
	 * 按照年度统计
	 * @return
	 */
	public List<Map> queryYearList(Map<String, Object> param) {
		// 获取系统当前日期
		String currentDate = FormatUtil.stringDate2().substring(0, 4);
		String startyear = currentDate;
		String endyear = "2099-12-31";

		String qStartYear = StringUtil.stringConvert(param.get("startyear"));
		if (qStartYear != null && !qStartYear.trim().equals("")) {
			startyear = qStartYear;
		}
		String qEndYear = StringUtil.stringConvert(param.get("endyear"));
		if (qEndYear != null && !qEndYear.trim().equals("")) {
			endyear = qEndYear;
		}
		
		String sql ="select bc.appyear,                                                                                              "+
					"       bc.bcount tcount,                                                                                        "+
					"       nvl(br.rcount, 0) rcount,                                                                                "+
					"       nvl(pr.pcount, 0) fcount,                                                                                "+
					"       def.C01,def.C02,def.C03,def.C04,def.C05,def.C06,def.C07,def.C08,                                         "+
					"       cas.D01,cas.D02,cas.D03,cas.D04,cas.D05,cas.D06,cas.D07,cas.D08,cas.D09,cas.D10,                         "+
					"       res.E01,res.E02,res.E03,res.E04,res.E05,                                                                 "+
					"       nvl(amo.casesum, 0) casesum,nvl(amo.amount, 0) amount                                                    "+
					"  from (select substr(b.appdate, 1, 4) appyear, count(*) bcount                                                 "+
					"          from b_casebaseinfo b                                                                                 "+
					"         where substr(b.appdate, 1, 4) >= '"+startyear+"'                                                                "+
					"           and substr(b.appdate, 1, 4) <= '"+endyear+"'                                                                "+
					"         group by substr(b.appdate, 1, 4)                                                                       "+
					"         order by substr(b.appdate, 1, 4)) bc                                                                   "+
					"  left join (select substr(b.appdate, 1, 4) appyear, count(*) pcount                                            "+
					"               from b_casebaseinfo b                                                                            "+
					"              where substr(b.appdate, 1, 4) >= '"+startyear+"'                                                           "+
					"                and substr(b.appdate, 1, 4) <= '"+endyear+"'                                                           "+
					"                and b.caseid in (select distinct p.caseid                                                       "+
					"                                   from pub_resultbaseinfo p                                                    "+
					"                                  where p.protype = '01')                                                       "+
					"              group by substr(b.appdate, 1, 4)                                                                  "+
					"              order by substr(b.appdate, 1, 4)) pr                                                              "+
					"    on bc.appyear = pr.appyear                                                                                  "+
					"  left join (select substr(b.appdate, 1, 4) appyear, count(*) rcount                                            "+
					"               from b_casebaseinfo b                                                                            "+
					"              where substr(b.appdate, 1, 4) >= '"+startyear+"'                                                           "+
					"                and substr(b.appdate, 1, 4) <= '"+endyear+"'                                                           "+
					"                and b.caseid in                                                                                 "+
					"                    (select distinct p.caseid                                                                   "+
					"                       from pub_probaseinfo p                                                                   "+
					"                      where p.protype = '01'                                                                    "+
					"                        and p.nodeid = 2                                                                        "+
					"                        and p.result = '01'                                                                     "+
					"                        and p.caseid not in                                                                     "+
					"                            (select distinct caseid from pub_resultbaseinfo))                                   "+
					"              group by substr(b.appdate, 1, 4)                                                                  "+
					"                                                                                                                "+
					"             ) br                                                                                               "+
					"    on bc.appyear = br.appyear                                                                                  "+
					"                                                                                                                "+
					"  left join (select *                                                                                           "+
					"               from (select da.appyear,                                                                         "+
					"                            da.deftype,                                                                         "+
					"                            (nvl(bc.bcount, 0) - 0) defcount                                                    "+
					"                       from (select at.appyear, dt.deftype, 0                                                   "+
					"                               from (select a.code deftype                                                      "+
					"                                       from SYS_YW_DICENUMITEM a                                                "+
					"                                      where upper(a.elementcode) =                                              "+
					"                                            'B_CASEBASEINFO_DEFTYPE'                                            "+
					"                                        and a.status = 0) dt,                                                   "+
					"                                    (select distinct substr(b.appdate, 1, 4) appyear                            "+
					"                                       from b_casebaseinfo b                                                    "+
					"                                      where substr(b.appdate, 1, 4) >= '"+startyear+"'                                   "+
					"                                        and substr(b.appdate, 1, 4) <= '"+endyear+"') at) da                           "+
					"                       left join (select b.appyear, b.deftype, count(*) bcount                                  "+
					"                                   from (select caseid,                                                         "+
					"                                                substr(appdate, 1, 4) appyear,                                  "+
					"                                                deftype                                                         "+
					"                                           from b_casebaseinfo                                                  "+
					"                                          where substr(appdate, 1, 4) >=                                        "+
					"                                                '"+startyear+"'                                                          "+
					"                                            and substr(appdate, 1, 4) <=                                        "+
					"                                                '"+endyear+"') b                                                       "+
					"                                  group by b.appyear, b.deftype) bc                                             "+
					"                         on da.appyear = bc.appyear                                                             "+
					"                        and da.deftype = bc.deftype) pivot(sum(defcount) for deftype in('01' as C01,            "+
					"                                                                                        '02' as C02,            "+
					"                                                                                        '03' as C03,            "+
					"                                                                                        '04' as C04,            "+
					"                                                                                        '05' as C05,            "+
					"                                                                                        '06' as C06,            "+
					"                                                                                        '07' as C07,            "+
					"                                                                                        '08' as C08))           "+
					"                                                                                                                "+
					"             ) def                                                                                              "+
					"    on bc.appyear = def.appyear                                                                                 "+
					"  left join (select *                                                                                           "+
					"               from (select da.appyear,                                                                         "+
					"                            da.casetype,                                                                        "+
					"                            (nvl(bc.bcount, 0) - 0) defcount                                                    "+
					"                       from (select at.appyear, dt.casetype, 0                                                  "+
					"                               from (select a.code casetype                                                     "+
					"                                       from SYS_YW_DICENUMITEM a                                                "+
					"                                      where upper(a.elementcode) =                                              "+
					"                                            'B_CASEBASEINFO_CASETYPE'                                           "+
					"                                        and a.status = 0) dt,                                                   "+
					"                                    (select distinct substr(b.appdate, 1, 4) appyear                            "+
					"                                       from b_casebaseinfo b                                                    "+
					"                                      where substr(b.appdate, 1, 4) >= '"+startyear+"'                                   "+
					"                                        and substr(b.appdate, 1, 4) <= '"+endyear+"') at) da                           "+
					"                       left join (select b.appyear,                                                             "+
					"                                        b.casetype,                                                             "+
					"                                        count(*) bcount                                                         "+
					"                                   from (select caseid,                                                         "+
					"                                                substr(appdate, 1, 4) appyear,                                  "+
					"                                                casetype                                                        "+
					"                                           from b_casebaseinfo                                                  "+
					"                                          where substr(appdate, 1, 4) >=                                        "+
					"                                                '"+startyear+"'                                                          "+
					"                                            and substr(appdate, 1, 4) <=                                        "+
					"                                                '"+endyear+"') b                                                       "+
					"                                  group by b.appyear, b.casetype) bc                                            "+
					"                         on da.appyear = bc.appyear                                                             "+
					"                        and da.casetype = bc.casetype) pivot(sum(defcount) for casetype in('01' as D01,         "+
					"                                                                                           '02' as D02,         "+
					"                                                                                           '03' as D03,         "+
					"                                                                                           '04' as D04,         "+
					"                                                                                           '05' as D05,         "+
					"                                                                                           '06' as D06,         "+
					"                                                                                           '07' as D07,         "+
					"                                                                                           '08' as D08,         "+
					"                                                                                           '09' as D09,         "+
					"                                                                                           '10' as D10))        "+
					"                                                                                                                "+
					"             ) cas                                                                                              "+
					"    on bc.appyear = cas.appyear                                                                                 "+
					"  left join (select *                                                                                           "+
					"               from (select da.appyear,                                                                         "+
					"                            da.result,                                                                          "+
					"                            (nvl(bc.pcount, 0) - 0) rescount                                                    "+
					"                       from (select at.appyear, dt.result, 0                                                    "+
					"                               from (select a.code result                                                       "+
					"                                       from SYS_YW_DICENUMITEM a                                                "+
					"                                      where upper(a.elementcode) = 'HEAR'                                       "+
					"                                        and a.status = 0) dt,                                                   "+
					"                                    (select distinct substr(b.appdate, 1, 4) appyear                            "+
					"                                       from b_casebaseinfo b                                                    "+
					"                                      where substr(b.appdate, 1, 4) >= '"+startyear+"'                                   "+
					"                                        and substr(b.appdate, 1, 4) <= '"+endyear+"') at) da                           "+
					"                       left join (select b.appyear, p.result, count(*) pcount                                   "+
					"                                   from pub_resultbaseinfo p,                                                   "+
					"                                        (select b.caseid caseid,                                                "+
					"                                                substr(b.appdate, 1, 4) appyear                                 "+
					"                                           from b_casebaseinfo b                                                "+
					"                                          where substr(b.appdate, 1, 4) >=                                      "+
					"                                                '"+startyear+"'                                                          "+
					"                                            and substr(b.appdate, 1, 4) <=                                      "+
					"                                                '"+endyear+"') b                                                       "+
					"                                  where p.caseid = b.caseid                                                     "+
					"                                    and p.protype = '01'                                                        "+
					"                                  group by b.appyear, p.result) bc                                              "+
					"                         on da.appyear = bc.appyear                                                             "+
					"                        and da.result = bc.result) pivot(sum(rescount) for result in('01' as E01,               "+
					"                                                                                     '02' as E02,               "+
					"                                                                                     '03' as E03,               "+
					"                                                                                     '04' as E04,               "+
					"                                                                                     '05' as E05))              "+
					"                                                                                                                "+
					"             ) res                                                                                              "+
					"    on bc.appyear = res.appyear                                                                                 "+
					"  left join (select substr(b.appdate, 1, 4) appyear,                                                            "+
					"                    count(*) casesum,                                                                           "+
					"                    sum(b.amount) amount                                                                        "+
					"               from b_casebaseinfo b                                                                            "+
					"              where b.ifcompensation = '1'                                                                      "+
					"                and substr(b.appdate, 1, 4) >= '"+startyear+"'                                                           "+
					"                and substr(b.appdate, 1, 4) <= '"+endyear+"'                                                           "+
					"              group by substr(b.appdate, 1, 4)                                                                  "+
					"              order by substr(b.appdate, 1, 4)) amo                                                             "+
					"    on bc.appyear = amo.appyear                                                                                 "+
					" order by bc.appyear                                                                                            ";
		return mapDataDao.queryListBySQL(sql);
	}
	
	/**
	 * 按照行政应诉裁判结果统计
	 * @return
	 */
	public List<Map> queryAdmLitList(Map<String, Object> param) {
		// 获取系统当前日期
		String currentDate = FormatUtil.stringDate2().substring(0, 4);
		String startyear = currentDate;

		String qStartYear = StringUtil.stringConvert(param.get("startyear"));
		if (qStartYear != null && !qStartYear.trim().equals("")) {
			startyear = qStartYear;
		}		
		
		String sql ="select (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ADMLITRESULT' and a.status=0  and a.code=pp.result) result, "+
					"       nvl(pp.pcount,0) pcount,                                                                                                         "+
					"       nvl(ss.s01,0) s01,nvl(ss.s02,0) s02,nvl(ss.s03,0) s03,                                                                                                            "+
					"       nvl(rr.r01,0) r01,nvl(rr.r02,0) r02,nvl(rr.r03,0) r03,nvl(rr.r04,0) r04,                                                                                                     "+
					"       nvl(tt.t01,0) t01,nvl(tt.t02,0) t02,                                                                                                                   "+
					"       (nvl(pp.pcount,0)-nvl(icount,0)) i01, nvl(icount,0) i02                                                                          "+
					"  from (select p.result, count(*) pcount                                                                                                "+
					"          from pub_resultbaseinfo p, b_admlitbaseinfo a                                                                                 "+
					"         where p.caseid = a.acaseid                                                                                                     "+
					"           and substr(a.rectime, 1, 4) = '"+startyear+"'                                                                                         "+
					"           and p.protype = '10'                                                                                                         "+
					"         group by p.result) pp                                                                                                          "+
					"  left join (select *                                                                                                                   "+
					"               from ((select p.result, a.stage, count(*) scount                                                                         "+
					"                        from pub_resultbaseinfo p, b_admlitbaseinfo a                                                                   "+
					"                       where p.caseid = a.acaseid                                                                                       "+
					"                         and p.protype = '10'                                                                                           "+
					"                       group by p.result, a.stage)                                                                                      "+
					"                     pivot(sum(scount) for                                                                                              "+
					"                           stage in ('01' as s01, '02' as s02, '03' as s03)))) ss                                                       "+
					"    on pp.result = ss.result                                                                                                            "+
					"  left join (select *                                                                                                                   "+
					"               from ((select p.result, a.rectype, count(*) rcount                                                                       "+
					"                        from pub_resultbaseinfo p, b_admlitbaseinfo a                                                                   "+
					"                       where p.caseid = a.acaseid                                                                                       "+
					"                         and p.protype = '10'                                                                                           "+
					"                       group by p.result, a.rectype)                                                                                    "+
					"                     pivot(sum(rcount) for                                                                                              "+
					"                           rectype in ('01' as r01,                                                                                     "+
					"                                       '02' as r02,                                                                                     "+
					"                                       '03' as r03,                                                                                     "+
					"                                       '04' as r04)))) rr                                                                               "+
					"    on pp.result = rr.result                                                                                                            "+
					"  left join (select *                                                                                                                   "+
					"               from ((select p.result, a.partytype, count(*) tcount                                                                     "+
					"                        from pub_resultbaseinfo p, b_admlitbaseinfo a                                                                   "+
					"                       where p.caseid = a.acaseid                                                                                       "+
					"                         and p.protype = '10'                                                                                           "+
					"                       group by p.result, a.partytype)                                                                                  "+
					"                     pivot(sum(tcount) for                                                                                              "+
					"                           partytype in ('01' as t01, '02' as t02)))) tt                                                                "+
					"    on pp.result = tt.result                                                                                                            "+
					"                                                                                                                                        "+
					"  left join (select p.result, count(*) icount                                                                                           "+
					"               from pub_resultbaseinfo p, b_admlitbaseinfo a                                                                            "+
					"              where p.caseid = a.acaseid                                                                                                "+
					"                and p.protype = '10'                                                                                                    "+
					"                and a.ifappeal = '1'                                                                                                    "+
					"              group by p.result) ii                                                                                                     "+
					"    on pp.result = ii.result                                                                                                            "+
					" order by pp.result                                                                                                                     ";
		return mapDataDao.queryListBySQL(sql);
	}
	
	/**
	 * 按照是否重大案件备案统计
	 * @return
	 */
	public List<Map> queryGreatList(Map<String, Object> param) {
		// 获取系统当前日期
		String currentDate = FormatUtil.stringDate2().substring(0, 4);
		String startyear = currentDate;

		String qStartYear = StringUtil.stringConvert(param.get("startyear"));
		if (qStartYear != null && !qStartYear.trim().equals("")) {
			startyear = qStartYear;
		}		
		
		String sql ="select (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ISORNOT' and a.status=0  and a.code=bc.isgreat) isgreat, "+
					"       bc.bcount tcount,                                                                                                             "+
					"       nvl(br.rcount,0) rcount,                                                                                                      "+
					"       nvl(pr.pcount, 0) fcount,                                                                                                     "+
					"       def.C01,def.C02,def.C03,def.C04,def.C05,def.C06,def.C07,def.C08,                                                              "+
					"       cas.D01,cas.D02,cas.D03,cas.D04,cas.D05,cas.D06,cas.D07,cas.D08,cas.D09,cas.D10,                                              "+
					"       res.E01,res.E02,res.E03,res.E04,res.E05,                                                                                      "+
					"       nvl(amo.casesum,0) casesum,nvl(amo.amount,0) amount                                                                           "+
					"  from (select b.isgreat isgreat, count(*) bcount                                                                                    "+
					"          from b_casebaseinfo b                                                                                                      "+
					"          where substr(b.appdate,1,4)='"+startyear+"'                                                                                         "+
					"         group by b.isgreat                                                                                                          "+
					"         order by b.isgreat) bc                                                                                                      "+
					"  left join (select b.isgreat isgreat, count(*) pcount                                                                               "+
					"               from b_casebaseinfo b                                                                                                 "+
					"              where b.caseid in (select distinct p.caseid                                                                            "+
					"                                   from pub_resultbaseinfo p                                                                         "+
					"                                  where p.protype = '01')                                                                            "+
					"              group by b.isgreat                                                                                                     "+
					"              order by b.isgreat) pr                                                                                                 "+
					"    on bc.isgreat = pr.isgreat                                                                                                       "+
					"  left join (select b.isgreat isgreat, count(*) rcount                                                                               "+
					"               from b_casebaseinfo b                                                                                                 "+
					"              where b.caseid in                                                                                                      "+
					"                    (select distinct p.caseid                                                                                        "+
					"                       from pub_probaseinfo p                                                                                        "+
					"                      where p.protype = '01'                                                                                         "+
					"                        and p.nodeid = 2                                                                                             "+
					"                        and p.result = '01'                                                                                          "+
					"                        and p.caseid not in                                                                                          "+
					"                            (select distinct caseid from pub_resultbaseinfo))                                                        "+
					"              group by b.isgreat                                                                                                     "+
					"                                                                                                                                     "+
					"             ) br                                                                                                                    "+
					"    on bc.isgreat = br.isgreat                                                                                                       "+
					"                                                                                                                                     "+
					"  left join (select *                                                                                                                "+
					"               from (select da.isgreat,                                                                                              "+
					"                            da.deftype,                                                                                              "+
					"                            (nvl(bc.bcount, 0) - 0) defcount                                                                         "+
					"                       from (select at.isgreat, dt.deftype, 0                                                                        "+
					"                               from (select a.code deftype                                                                           "+
					"                                       from SYS_YW_DICENUMITEM a                                                                     "+
					"                                      where upper(a.elementcode) =                                                                   "+
					"                                            'B_CASEBASEINFO_DEFTYPE'                                                                 "+
					"                                        and a.status = 0) dt,                                                                        "+
					"                                    (select a.code isgreat                                                                           "+
					"                                       from SYS_YW_DICENUMITEM a                                                                     "+
					"                                      where upper(a.elementcode) = 'ISORNOT'                                                         "+
					"                                        and a.status = 0                                                                             "+
					"                                        and a.code in                                                                                "+
					"                                            (select distinct isgreat                                                                 "+
					"                                               from b_casebaseinfo)) at) da                                                          "+
					"                       left join (select b.isgreat, b.deftype, count(*) bcount                                                       "+
					"                                   from b_casebaseinfo b                                                                             "+
					"                                  group by b.isgreat, b.deftype) bc                                                                  "+
					"                         on da.isgreat = bc.isgreat                                                                                  "+
					"                        and da.deftype = bc.deftype) pivot(sum(defcount) for deftype in('01' as C01,                                 "+
					"                                                                                        '02' as C02,                                 "+
					"                                                                                        '03' as C03,                                 "+
					"                                                                                        '04' as C04,                                 "+
					"                                                                                        '05' as C05,                                 "+
					"                                                                                        '06' as C06,                                 "+
					"                                                                                        '07' as C07,                                 "+
					"                                                                                        '08' as C08))                                "+
					"                                                                                                                                     "+
					"             ) def                                                                                                                   "+
					"    on bc.isgreat = def.isgreat                                                                                                      "+
					"  left join (select *                                                                                                                "+
					"               from (select da.isgreat,                                                                                              "+
					"                            da.casetype,                                                                                             "+
					"                            (nvl(bc.bcount, 0) - 0) defcount                                                                         "+
					"                       from (select at.isgreat, dt.casetype, 0                                                                       "+
					"                               from (select a.code casetype                                                                          "+
					"                                       from SYS_YW_DICENUMITEM a                                                                     "+
					"                                      where upper(a.elementcode) =                                                                   "+
					"                                            'B_CASEBASEINFO_CASETYPE'                                                                "+
					"                                        and a.status = 0) dt,                                                                        "+
					"                                    (select a.code isgreat                                                                           "+
					"                                       from SYS_YW_DICENUMITEM a                                                                     "+
					"                                      where upper(a.elementcode) = 'ISORNOT'                                                         "+
					"                                        and a.status = 0                                                                             "+
					"                                        and a.code in                                                                                "+
					"                                            (select distinct isgreat                                                                 "+
					"                                               from b_casebaseinfo)) at) da                                                          "+
					"                       left join (select b.isgreat,                                                                                  "+
					"                                        b.casetype,                                                                                  "+
					"                                        count(*) bcount                                                                              "+
					"                                   from b_casebaseinfo b                                                                             "+
					"                                  group by b.isgreat, b.casetype) bc                                                                 "+
					"                         on da.isgreat = bc.isgreat                                                                                  "+
					"                        and da.casetype = bc.casetype) pivot(sum(defcount) for casetype in('01' as D01,                              "+
					"                                                                                           '02' as D02,                              "+
					"                                                                                           '03' as D03,                              "+
					"                                                                                           '04' as D04,                              "+
					"                                                                                           '05' as D05,                              "+
					"                                                                                           '06' as D06,                              "+
					"                                                                                           '07' as D07,                              "+
					"                                                                                           '08' as D08,                              "+
					"                                                                                           '09' as D09,                              "+
					"                                                                                           '10' as D10))                             "+
					"                                                                                                                                     "+
					"             ) cas                                                                                                                   "+
					"    on bc.isgreat = cas.isgreat                                                                                                      "+
					"  left join (select *                                                                                                                "+
					"               from (select da.isgreat,                                                                                              "+
					"                            da.result,                                                                                               "+
					"                            (nvl(bc.pcount, 0) - 0) rescount                                                                         "+
					"                       from (select at.isgreat, dt.result, 0                                                                         "+
					"                               from (select a.code result                                                                            "+
					"                                       from SYS_YW_DICENUMITEM a                                                                     "+
					"                                      where upper(a.elementcode) = 'HEAR'                                                            "+
					"                                        and a.status = 0) dt,                                                                        "+
					"                                    (select a.code isgreat                                                                           "+
					"                                       from SYS_YW_DICENUMITEM a                                                                     "+
					"                                      where upper(a.elementcode) = 'ISORNOT'                                                         "+
					"                                        and a.status = 0                                                                             "+
					"                                        and a.code in                                                                                "+
					"                                            (select distinct isgreat                                                                 "+
					"                                               from b_casebaseinfo)) at) da                                                          "+
					"                       left join (select b.isgreat, p.result, count(*) pcount                                                        "+
					"                                   from pub_resultbaseinfo p,                                                                        "+
					"                                        b_casebaseinfo     b                                                                         "+
					"                                  where p.caseid = b.caseid                                                                          "+
					"                                    and p.protype = '01'                                                                             "+
					"                                  group by b.isgreat, p.result) bc                                                                   "+
					"                         on da.isgreat = bc.isgreat                                                                                  "+
					"                        and da.result = bc.result) pivot(sum(rescount) for result in('01' as E01,                                    "+
					"                                                                                     '02' as E02,                                    "+
					"                                                                                     '03' as E03,                                    "+
					"                                                                                     '04' as E04,                                    "+
					"                                                                                     '05' as E05))                                   "+
					"                                                                                                                                     "+
					"             ) res                                                                                                                   "+
					"    on bc.isgreat = res.isgreat                                                                                                      "+
					"  left join (select b.isgreat, count(*) casesum, sum(b.amount) amount                                                                "+
					"               from b_casebaseinfo b                                                                                                 "+
					"              where b.ifcompensation = '1'                                                                                           "+
					"              group by b.isgreat) amo                                                                                                "+
					"    on bc.isgreat = amo.isgreat                                                                                                      "+
					" order by bc.isgreat desc                                                                                                            ";
		return mapDataDao.queryListBySQL(sql);
	}
	
	/**
	 * 按照委员参与案件统计
	 * @return
	 */
	public List<Map> querySpeList(Map<String, Object> param) {
		// 获取系统当前日期
		String currentDate = FormatUtil.stringDate2().substring(0, 4);
		String startyear = currentDate;

		String qStartYear = StringUtil.stringConvert(param.get("startyear"));
		if (qStartYear != null && !qStartYear.trim().equals("")) {
			startyear = qStartYear;
		}		
		
		String sql ="select (select spename from b_specialistbaseinfo where speid=tt.speid) speid,                                                                                                               "+
					"       ccount,tcount,                                                                                                                                                                       "+
					"       nvl(def.c01,0) c01,nvl(def.c02,0) c02,nvl(def.c03,0) c03,nvl(def.c04,0) c04,nvl(def.c05,0) c05,nvl(def.c06,0) c06,nvl(def.c07,0) c07,nvl(def.c08,0) c08,                             "+
					"       nvl(dd.d01,0) d01,nvl(dd.d02,0) d02,nvl(dd.d03,0) d03,nvl(dd.d04,0) d04,nvl(dd.d05,0) d05,nvl(dd.d06,0) d06,nvl(dd.d07,0) d07,nvl(dd.d08,0) d08,nvl(dd.d09,0) d09,nvl(dd.d10,0) d10, "+
					"       nvl(ii.icount,0) icount,nvl(ii.iamount,0) iamount                                                                                                                                    "+
					"  from (select speid, count(distinct caseid) ccount, count(groupid) tcount                                                                                                                  "+
					"          from (select c.caseid, rr.groupid, rr.speid                                                                                                                                       "+
					"                  from b_casesperelabaseinfo c,                                                                                                                                             "+
					"                       b_casebaseinfo b,                                                                                                                                                    "+
					"                       (select distinct groupid, speid                                                                                                                                      "+
					"                          from b_spesugbaseinfo r                                                                                                                                           "+
					"                         where (r.remark is not null or trim(remark) != '')) rr                                                                                                             "+
					"                 where c.groupid = rr.groupid                                                                                                                                               "+
					"                   and c.caseid = b.caseid                                                                                                                                                  "+
					"                   and substr(b.appdate,1,4) = '"+startyear+"')                                                                                                                                      "+
					"         group by speid) tt                                                                                                                                                                 "+
					"                                                                                                                                                                                            "+
					"  left join (select speid, deftype, count(*) defcount                                                                                                                                       "+
					"               from (select distinct c.caseid, rr.speid, b.deftype                                                                                                                          "+
					"                       from b_casesperelabaseinfo c,                                                                                                                                        "+
					"                            b_casebaseinfo b,                                                                                                                                               "+
					"                            (select distinct groupid, speid                                                                                                                                 "+
					"                               from b_spesugbaseinfo r                                                                                                                                      "+
					"                              where (r.remark is not null or                                                                                                                                "+
					"                                    trim(remark) != '')) rr                                                                                                                                 "+
					"                      where c.caseid = b.caseid                                                                                                                                             "+
					"                        and c.groupid = rr.groupid)                                                                                                                                         "+
					"              group by speid, deftype) pivot(sum(defcount)                                                                                                                                  "+
					"   for deftype in('01' as c01,                                                                                                                                                              "+
					"                  '02' as c02,                                                                                                                                                              "+
					"                  '03' as c03,                                                                                                                                                              "+
					"                  '04' as c04,                                                                                                                                                              "+
					"                  '05' as c05,                                                                                                                                                              "+
					"                  '06' as c06,                                                                                                                                                              "+
					"                  '07' as c07,                                                                                                                                                              "+
					"                  '08' as c08)) def                                                                                                                                                         "+
					"    on tt.speid = def.speid                                                                                                                                                                 "+
					"  left join (select speid, casetype, count(*) cascount                                                                                                                                      "+
					"               from (select distinct c.caseid, rr.speid, b.casetype                                                                                                                         "+
					"                       from b_casesperelabaseinfo c,                                                                                                                                        "+
					"                            b_casebaseinfo b,                                                                                                                                               "+
					"                            (select distinct groupid, speid                                                                                                                                 "+
					"                               from b_spesugbaseinfo r                                                                                                                                      "+
					"                              where (r.remark is not null or                                                                                                                                "+
					"                                    trim(remark) != '')) rr                                                                                                                                 "+
					"                      where c.caseid = b.caseid                                                                                                                                             "+
					"                        and c.groupid = rr.groupid)                                                                                                                                         "+
					"              group by speid, casetype) pivot(sum(cascount)                                                                                                                                 "+
					"   for casetype in('01' as d01,                                                                                                                                                             "+
					"                   '02' as d02,                                                                                                                                                             "+
					"                   '03' as d03,                                                                                                                                                             "+
					"                   '04' as d04,                                                                                                                                                             "+
					"                   '05' as d05,                                                                                                                                                             "+
					"                   '06' as d06,                                                                                                                                                             "+
					"                   '07' as d07,                                                                                                                                                             "+
					"                   '08' as d08,                                                                                                                                                             "+
					"                   '09' as d09,                                                                                                                                                             "+
					"                   '10' as d10)) dd                                                                                                                                                         "+
					"    on tt.speid = dd.speid                                                                                                                                                                  "+
					"  left join (select speid, count(ifcompensation) icount, sum(amount) iamount                                                                                                                "+
					"               from (select distinct c.caseid,                                                                                                                                              "+
					"                                     rr.speid,                                                                                                                                              "+
					"                                     b.ifcompensation,                                                                                                                                      "+
					"                                     b.amount                                                                                                                                               "+
					"                       from b_casesperelabaseinfo c,                                                                                                                                        "+
					"                            b_casebaseinfo b,                                                                                                                                               "+
					"                            (select distinct groupid, speid                                                                                                                                 "+
					"                               from b_spesugbaseinfo r                                                                                                                                      "+
					"                              where (r.remark is not null or                                                                                                                                "+
					"                                    trim(remark) != '')) rr                                                                                                                                 "+
					"                      where c.caseid = b.caseid                                                                                                                                             "+
					"                        and b.ifcompensation = '1'                                                                                                                                          "+
					"                        and c.groupid = rr.groupid)                                                                                                                                         "+
					"              group by speid, ifcompensation) ii                                                                                                                                            "+
					"    on tt.speid = ii.speid                                                                                                                                                                  ";
		return mapDataDao.queryListBySQL(sql);
	}
	
	/**
	 * @Title: queryCaseDefQSAnalysis
	 * @Description:图形统计：被申请人_趋势占比统计
	 * @author LXF
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<Map<String, Object>> queryCaseDefQSAnalysis(Map<String, Object> param) throws AppException{
		// 获取系统当前日期
		String currentDate = FormatUtil.stringDate2();
		
		String sql="";
		
		//查询条件赋默认值
		//获取开始年
		String startyear=currentDate.substring(0, 4);
		//获取结束年
		String endyear=currentDate.substring(0, 4);
		//获取开始月
		String startmonth="01";
		//获取结束月
		String endmonth=currentDate.substring(5, 7);
		
		//获取统计周期
	    String anaType = StringUtil.stringConvert(param.get("anafrequency"));		
		//获取被申请人类型
		String deftype=StringUtil.stringConvert(param.get("deftype"));		
		//获取开始年
		startyear=StringUtil.stringConvert(param.get("startyear"));
		//获取结束年
		endyear=StringUtil.stringConvert(param.get("endyear"));		
		//获取开始月
		startmonth=StringUtil.stringConvert(param.get("startmonth"));
		//获取结束月
		endmonth=StringUtil.stringConvert(param.get("endmonth"));
		
		if (!StringUtils.isBlank(anaType)) {
			if ("01".equals(anaType.trim())) {
				//按月统计
				if (!StringUtils.isBlank(deftype)
						&& !StringUtils.isBlank(startyear)
						&& !StringUtils.isBlank(endyear)
						&& !StringUtils.isBlank(startmonth)
						&& !StringUtils.isBlank(endmonth)) {
					
					String startYM = startyear + "-" + startmonth;
					String endYM = endyear + "-" + endmonth;
					
					sql="select ym.ymdate name, nvl(acount,0) value                                  "+
						"  from (select ymyear || '-' || ymmonth as ymdate                        "+
						"          from (select trim(a.code) ymyear                               "+
						"                  from SYS_YW_DICENUMITEM a                              "+
						"                 where upper(a.elementcode) = 'ANALYSISYEAR'             "+
						"                   and a.status = 0                                      "+
						"                   and a.code >= '"+startyear+"'                         "+
						"                   and a.code <= '"+endyear+"'),                                "+
						"               (select trim(a.code) ymmonth                              "+
						"                  from SYS_YW_DICENUMITEM a                              "+
						"                 where upper(a.elementcode) = 'ANALYSISMONTH'            "+
						"                   and a.status = 0                                      "+
						"                   and a.code >= '"+startmonth+"'                                    "+
						"                   and a.code <= '"+endmonth+"')) ym                               "+
						"  left join (                                                            "+
						"                                                                         "+
						"             select substr(b.appdate, 1, 7) appdate, count(*) acount     "+
						"               from b_casebaseinfo b                                     "+
						"              where b.deftype = '"+deftype+"'                                     "+
						"                and substr(b.appdate, 1, 7) >= '"+startYM+"'                 "+
						"                and substr(b.appdate, 1, 7) <= '"+endYM+"'                 "+
						"              group by substr(b.appdate, 1, 7)) bb                       "+
						"    on ym.ymdate = bb.appdate                                            "+
						"    where ym.ymdate  >= '"+startYM+"' and  ym.ymdate<='"+endYM+"'              "+
						" order by ym.ymdate                                                      ";
				}
			} else if ("02".equals(anaType.trim())) {
				//按年统计
				if (!StringUtils.isBlank(deftype)
						&& !StringUtils.isBlank(startyear)
						&& !StringUtils.isBlank(endyear)) {
					sql="select yw.code name, nvl(bb.ycount, 0) value                               "+
						"  from (select a.code, count(*)                                        "+
						"          from SYS_YW_DICENUMITEM a                                    "+
						"         where upper(a.elementcode) = 'ANALYSISYEAR'                   "+
						"           and a.status = 0                                            "+
						"           and a.code >= '"+startyear+"'                                        "+
						"           and a.code <= '"+endyear+"'                                        "+
						"         group by a.code) yw                                           "+
						"  left join (select substr(b.appdate, 1, 4) appyear, count(*) ycount   "+
						"               from b_casebaseinfo b                                   "+
						"              where b.deftype = '"+deftype+"'                                   "+
						"                and substr(b.appdate, 1, 4) >= '"+startyear+"'                  "+
						"                and substr(b.appdate, 1, 4) <= '"+endyear+"'                  "+
						"              group by substr(b.appdate, 1, 4)) bb                     "+
						"    on yw.code = bb.appyear                                            ";
				}
			}
		}
		
		
		//获取List，用来返回结果
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		
		
		
		List<JSONObject> jsonList = mapDataDao.queryListBySQL(sql.toString());
		for (JSONObject obj : jsonList) {
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("name", obj.getString("name"));
			map.put("value", obj.getString("value"));
			
			dataList.add(map);
		}
		
		return dataList;
	}
	
	/**
	 * @Title: queryCaseTypeQSAnalysis
	 * @Description:图形统计：申请事项类型_趋势占比统计
	 * @author LXF
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<Map<String, Object>> queryCaseTypeQSAnalysis(Map<String, Object> param) throws AppException{
		// 获取系统当前日期
		String currentDate = FormatUtil.stringDate2();
		
		String sql="";
		
		//查询条件赋默认值
		//获取开始年
		String startyear=currentDate.substring(0, 4);
		//获取结束年
		String endyear=currentDate.substring(0, 4);
		//获取开始月
		String startmonth="01";
		//获取结束月
		String endmonth=currentDate.substring(5, 7);
		
		//获取统计周期
	    String anaType = StringUtil.stringConvert(param.get("anafrequency"));		
		//获取被申请人类型
		String casetype=StringUtil.stringConvert(param.get("casetype"));		
		//获取开始年
		startyear=StringUtil.stringConvert(param.get("startyear"));
		//获取结束年
		endyear=StringUtil.stringConvert(param.get("endyear"));		
		//获取开始月
		startmonth=StringUtil.stringConvert(param.get("startmonth"));
		//获取结束月
		endmonth=StringUtil.stringConvert(param.get("endmonth"));
		
		if (!StringUtils.isBlank(anaType)) {
			if ("01".equals(anaType.trim())) {
				//按月统计
				if (!StringUtils.isBlank(casetype)
						&& !StringUtils.isBlank(startyear)
						&& !StringUtils.isBlank(endyear)
						&& !StringUtils.isBlank(startmonth)
						&& !StringUtils.isBlank(endmonth)) {
					
					String startYM = startyear + "-" + startmonth;
					String endYM = endyear + "-" + endmonth;
					
					sql="select ym.ymdate name, nvl(acount,0) value                                  "+
						"  from (select ymyear || '-' || ymmonth as ymdate                        "+
						"          from (select trim(a.code) ymyear                               "+
						"                  from SYS_YW_DICENUMITEM a                              "+
						"                 where upper(a.elementcode) = 'ANALYSISYEAR'             "+
						"                   and a.status = 0                                      "+
						"                   and a.code >= '"+startyear+"'                         "+
						"                   and a.code <= '"+endyear+"'),                                "+
						"               (select trim(a.code) ymmonth                              "+
						"                  from SYS_YW_DICENUMITEM a                              "+
						"                 where upper(a.elementcode) = 'ANALYSISMONTH'            "+
						"                   and a.status = 0                                      "+
						"                   and a.code >= '"+startmonth+"'                                    "+
						"                   and a.code <= '"+endmonth+"')) ym                               "+
						"  left join (                                                            "+
						"                                                                         "+
						"             select substr(b.appdate, 1, 7) appdate, count(*) acount     "+
						"               from b_casebaseinfo b                                     "+
						"              where b.casetype = '"+casetype+"'                                     "+
						"                and substr(b.appdate, 1, 7) >= '"+startYM+"'                 "+
						"                and substr(b.appdate, 1, 7) <= '"+endYM+"'                 "+
						"              group by substr(b.appdate, 1, 7)) bb                       "+
						"    on ym.ymdate = bb.appdate                                            "+
						"    where ym.ymdate  >= '"+startYM+"' and  ym.ymdate<='"+endYM+"'              "+
						" order by ym.ymdate                                                      ";
				}
			} else if ("02".equals(anaType.trim())) {
				//按年统计
				if (!StringUtils.isBlank(casetype)
						&& !StringUtils.isBlank(startyear)
						&& !StringUtils.isBlank(endyear)) {
					sql="select yw.code name, nvl(bb.ycount, 0) value                               "+
						"  from (select a.code, count(*)                                        "+
						"          from SYS_YW_DICENUMITEM a                                    "+
						"         where upper(a.elementcode) = 'ANALYSISYEAR'                   "+
						"           and a.status = 0                                            "+
						"           and a.code >= '"+startyear+"'                                        "+
						"           and a.code <= '"+endyear+"'                                        "+
						"         group by a.code) yw                                           "+
						"  left join (select substr(b.appdate, 1, 4) appyear, count(*) ycount   "+
						"               from b_casebaseinfo b                                   "+
						"              where b.casetype = '"+casetype+"'                                   "+
						"                and substr(b.appdate, 1, 4) >= '"+startyear+"'                  "+
						"                and substr(b.appdate, 1, 4) <= '"+endyear+"'                  "+
						"              group by substr(b.appdate, 1, 4)) bb                     "+
						"    on yw.code = bb.appyear                                            ";
				}
			}
		}
		
		
		//获取List，用来返回结果
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		
		
		
		List<JSONObject> jsonList = mapDataDao.queryListBySQL(sql.toString());
		for (JSONObject obj : jsonList) {
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("name", obj.getString("name"));
			map.put("value", obj.getString("value"));
			
			dataList.add(map);
		}
		
		return dataList;
	}
	
	/**
	 * @Title: queryCaseSumQSAnalysis
	 * @Description:图形统计：案件处理状态_趋势占比统计
	 * @author LXF
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List queryCaseSumQSAnalysis(Map<String, Object> param) throws AppException{
		// 获取系统当前日期
		String currentDate = FormatUtil.stringDate2();
		
		String sql="";
		
		//查询条件赋默认值
		//获取开始年
		String startyear=currentDate.substring(0, 4);
		//获取结束年
		String endyear=currentDate.substring(0, 4);
		//获取开始月
		String startmonth="01";
		//获取结束月
		String endmonth=currentDate.substring(5, 7);
		
		//获取统计周期
	    String anaType = StringUtil.stringConvert(param.get("anafrequency"));
		//获取开始年
		startyear=StringUtil.stringConvert(param.get("startyear"));
		//获取结束年
		endyear=StringUtil.stringConvert(param.get("endyear"));		
		//获取开始月
		startmonth=StringUtil.stringConvert(param.get("startmonth"));
		//获取结束月
		endmonth=StringUtil.stringConvert(param.get("endmonth"));
		
		if (!StringUtils.isBlank(anaType)) {
			if ("01".equals(anaType.trim())) {
				//按月统计
				if (!StringUtils.isBlank(startyear)
						&& !StringUtils.isBlank(endyear)
						&& !StringUtils.isBlank(startmonth)
						&& !StringUtils.isBlank(endmonth)) {
					
					String startYM = startyear + "-" + startmonth;
					String endYM = endyear + "-" + endmonth;
					
					sql="select ym.ymdate name,                                                               "+   
						"       nvl(ccc.bcount, 0) bcount,                                             "+
						"       nvl(ccc.pcount, 0) pcount,                                           "+
						"       nvl(ccc.rcount, 0) rcount                                            "+
						"  from (select ymyear || '-' || ymmonth as ymdate                               "+
						"          from (select trim(a.code) ymyear                                      "+
						"                  from SYS_YW_DICENUMITEM a                                     "+
						"                 where upper(a.elementcode) = 'ANALYSISYEAR'                    "+
						"                   and a.status = 0                                             "+
						"                   and a.code >= '"+startyear+"'                                         "+
						"                   and a.code <= '"+endyear+"'),                                       "+
						"               (select trim(a.code) ymmonth                                     "+
						"                  from SYS_YW_DICENUMITEM a                                     "+
						"                 where upper(a.elementcode) = 'ANALYSISMONTH'                   "+
						"                   and a.status = 0                                             "+
						"                   and a.code >= '"+startmonth+"'                                           "+
						"                   and a.code <= '"+endmonth+"')) ym                                      "+
						"  left join (select bb.bdate,                                                   "+
						"                    nvl(bb.bcount, 0) bcount,                                   "+
						"                    nvl(pp.pcount, 0) pcount,                                   "+
						"                    nvl(rr.rcount, 0) rcount                                    "+
						"               from (select substr(b.appdate, 1, 7) bdate, count(*) bcount      "+
						"                       from b_casebaseinfo b                                    "+
						"                      where substr(b.appdate, 1, 7) >= '"+startYM+"'                "+
						"                        and substr(b.appdate, 1, 7) <= '"+endYM+"'                "+
						"                      group by substr(b.appdate, 1, 7)) bb                      "+
						"               left join (select substr(b.appdate, 1, 7) pdate,                 "+
						"                                count(*) pcount                                 "+
						"                           from b_casebaseinfo b                                "+
						"                          where substr(b.appdate, 1, 7) >= '"+startYM+"'            "+
						"                            and substr(b.appdate, 1, 7) <= '"+endYM+"'            "+
						"                            and b.caseid in                                     "+
						"                                (select distinct p.caseid                       "+
						"                                   from pub_resultbaseinfo p                    "+
						"                                  where p.protype = '01')                       "+
						"                          group by substr(b.appdate, 1, 7)) pp                  "+
						"                 on bb.bdate = pp.pdate                                         "+
						"               left join (select substr(b.appdate, 1, 7) rdate,                 "+
						"                                count(*) rcount                                 "+
						"                           from b_casebaseinfo b                                "+
						"                          where substr(b.appdate, 1, 7) >= '"+startYM+"'            "+
						"                            and substr(b.appdate, 1, 7) <= '"+endYM+"'            "+
						"                            and b.caseid in                                     "+
						"                                (select distinct p.caseid                       "+
						"                                   from pub_probaseinfo p                       "+
						"                                  where p.protype = '01'                        "+
						"                                    and p.nodeid = 2                            "+
						"                                    and p.result = '01'                         "+
						"                                    and p.caseid not in                         "+
						"                                        (select distinct caseid                 "+
						"                                           from pub_resultbaseinfo))            "+
						"                          group by substr(b.appdate, 1, 7)) rr                  "+
						"                 on bb.bdate = rr.rdate) ccc                                    "+
						"    on ym.ymdate = ccc.bdate                                                    "+
						" order by ym.ymdate                                                             ";
				}
			} else if ("02".equals(anaType.trim())) {
				//按年统计
				if (!StringUtils.isBlank(startyear)
						&& !StringUtils.isBlank(endyear)) {
					sql="select yw.code name,                                                              "+
						"       nvl(ccc.bcount, 0) bcount,                                          "+
						"       nvl(ccc.pcount, 0) pcount,                                        "+
						"       nvl(ccc.rcount, 0) rcount                                         "+
						"  from (select a.code, count(*)                                              "+
						"          from SYS_YW_DICENUMITEM a                                          "+
						"         where upper(a.elementcode) = 'ANALYSISYEAR'                         "+
						"           and a.status = 0                                                  "+
						"           and a.code >= '"+startyear+"'                                              "+
						"           and a.code <= '"+endyear+"'                                              "+
						"         group by a.code) yw                                                 "+
						"  left join (                                                                "+
						"             select bb.byear,                                                "+
						"                     nvl(bb.bcount, 0) bcount,                               "+
						"                     nvl(pp.pcount, 0) pcount,                               "+
						"                     nvl(rr.rcount, 0) rcount                                "+
						"               from (select substr(b.appdate, 1, 4) byear, count(*) bcount   "+
						"                        from b_casebaseinfo b                                "+
						"                       where substr(b.appdate, 1, 4) >= '"+startyear+"'               "+
						"                         and substr(b.appdate, 1, 4) <= '"+endyear+"'               "+
						"                       group by substr(b.appdate, 1, 4)) bb                  "+
						"               left join (select substr(b.appdate, 1, 4) pyear,              "+
						"                                 count(*) pcount                             "+
						"                            from b_casebaseinfo b                            "+
						"                           where substr(b.appdate, 1, 4) >= '"+startyear+"'           "+
						"                             and substr(b.appdate, 1, 4) <= '"+endyear+"'           "+
						"                             and b.caseid in                                 "+
						"                                 (select distinct p.caseid                   "+
						"                                    from pub_resultbaseinfo p                "+
						"                                   where p.protype = '01')                   "+
						"                           group by substr(b.appdate, 1, 4)) pp              "+
						"                 on bb.byear = pp.pyear                                      "+
						"               left join (select substr(b.appdate, 1, 4) ryear,              "+
						"                                 count(*) rcount                             "+
						"                            from b_casebaseinfo b                            "+
						"                           where substr(b.appdate, 1, 4) >= '"+startyear+"'           "+
						"                             and substr(b.appdate, 1, 4) <= '"+endyear+"'           "+
						"                             and b.caseid in                                 "+
						"                                 (select distinct p.caseid                   "+
						"                                    from pub_probaseinfo p                   "+
						"                                   where p.protype = '01'                    "+
						"                                     and p.nodeid = 2                        "+
						"                                     and p.result = '01'                     "+
						"                                     and p.caseid not in                     "+
						"                                         (select distinct caseid             "+
						"                                            from pub_resultbaseinfo))        "+
						"                           group by substr(b.appdate, 1, 4)) rr              "+
						"                 on bb.byear = rr.ryear) ccc                                 "+
						"    on yw.code = ccc.byear                                                   "+
						" order by yw.code                                                            ";
				}
			}
		}
		
		
		// 获取List，用来返回结果
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

		List tList = new ArrayList();
		List<Map<String, Object>> bList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> pList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> rList = new ArrayList<Map<String, Object>>();

		List<JSONObject> jsonList = mapDataDao.queryListBySQL(sql.toString());
		for (JSONObject obj : jsonList) {
			Map<String, Object> bMap = new HashMap<String, Object>();
			Map<String, Object> pMap = new HashMap<String, Object>();
			Map<String, Object> rMap = new HashMap<String, Object>();

			String code = obj.getString("name");

			bMap.put("name", code);
			bMap.put("value", obj.getString("bcount"));
			bList.add(bMap);

			pMap.put("name", code);
			pMap.put("value", obj.getString("pcount"));
			pList.add(pMap);

			rMap.put("name", code);
			rMap.put("value", obj.getString("rcount"));
			rList.add(rMap);
		}

		tList.add(bList);
		tList.add(pList);
		tList.add(rList);
		
		return tList;
	}	

	/**
	 * @Title: queryCaseViewList
	 * @Description: 案件查询：grid列表页面查询
	 * @author zhaoxingdi
	 * @date 2016年9月27日10:21:58
	 * @param request
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryCaseListForView(Map<String, Object> param) {
		
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
		xzfySql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0 and a.code = t.ifcompensation) ifcompensation_mc ");
		xzfySql.append("from B_CASEBASEINFO t where 1=1 ");
		
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
	
	/**
	 * @Title: queryCaseStateCount
	 * @Description: 全市案件办理结果统计
	 * @author ybb
	 * @date 2016年11月18日 下午3:00:25
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryCaseStateCount(Map<String,Object> param){
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		//此处填充数据
			//查询状态和案件占比
		sql.append("select t1.name, nvl(t2.percent,0) percent");
		sql.append(" from (select code, name from SYS_YW_DICENUMITEM where upper(elementcode) = 'CASESTATE') t1 ");
		sql.append(" left join (select a.state, a.sucessnum, round((sucessnum/allnum)*100,2) percent ");
		sql.append(" from (select state, count(caseid) sucessnum from B_CASEBASEINFO group by state) a, ");
		sql.append(" (select count(caseid) allnum from B_CASEBASEINFO) b) t2 on t1.code = t2.state ");
		
		List<JSONObject> dicenumitemList = mapDataDao.queryListBySQL(sql.toString());
		if (null == dicenumitemList || dicenumitemList.isEmpty()) {
			return null;
		}
		
		//填充数据
		List<Object> data2_list = new ArrayList<Object>();
		for(JSONObject obj : dicenumitemList){
			
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("name", obj.getString("name"));
			map.put("value", obj.getString("percent"));
			
			data2_list.add(map);
		}
		
		Map<String, Object> series_map = new HashMap<String, Object>();
		series_map.put("data", data2_list);
		
		List<Object> series_list = new ArrayList<Object>();
		series_list.add(series_map);
		
		Map<String,Object> optionMap = new HashMap<String, Object>();
		optionMap.put("series", series_map);
	
		return optionMap;
	}
}
