package com.wfzcx.aros.sqbl.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.aros.sqbl.dao.ApplyRecordDao;
import com.wfzcx.aros.sqbl.po.ApplyRecordInfo;

/**
 * 标准体系service
 * @author zhaoxd
 *
 */
@Scope("prototype")
@Service("com.wfzcx.aros.sqbl.service.ApplyRecordInfoService")
public class ApplyRecordInfoService {
	
	@Autowired
	private MapDataDaoI mapDataDao;
	@Autowired
	private ApplyRecordDao dao;
	
	/**
	 * 查询列表
	 * @param param
	 * @return
	 */
	public PaginationSupport query(Map<String, Object> param) {
		String appname = (String) param.get("appname");
		String inquirer = (String) param.get("inquirer");
		String starttime = (String) param.get("starttime");
		String endtime = (String) param.get("endtime");
		String issend = (String) param.get("issend");
		String iscase = (String) param.get("iscase");
		// b_standbaseinfo
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("select t.arid,t.caseid,t.reqtime, t.place,t.inquirer,t.noter,t.workunits, t.appname,t.sex,t.age,t.address, t.phone,   t.issue,   t.answer,  t.appdate, t.opttype, t.issolve, t.remark,  t.userid,  t.operator,t.optdate ");
		sql.append(" , (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SEX' and a.status=0  and a.code=t.sex) sexname ");
		sql.append(" , (select a.csaecode from b_casebaseinfo a where a.caseid=t.caseid) csaecode ");
		sql.append(" , (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ISORNOT' and a.status=0  and a.code=t.issolve) issolvename ");
		sql.append(" from B_APPLYRECORD t ");
		sql.append(" where 1=1 ");
		if(!StringUtils.isEmpty(appname)){
			sql.append(" and appname like '%").append(appname).append("%'");
		}
		if(!StringUtils.isEmpty(inquirer)){
			sql.append(" and inquirer like '%").append(inquirer).append("%'");
		}
		if(!StringUtils.isEmpty(starttime)){
			sql.append(" and appdate >= '").append(starttime).append("'");
		}
		if(!StringUtils.isEmpty(endtime)){
			sql.append(" and appdate <= '").append(endtime).append("'");
		}
		if(!StringUtils.isEmpty(issend)){
			// 否
			if("0".equals(issend)){
				sql.append(" and opttype <> '2' ");
			}// 是
			if("1".equals(issend)){
				sql.append(" and opttype = '2' ");
			}
		}
		if(!StringUtils.isEmpty(iscase)){
			// 否
			if("0".equals(iscase)){
				sql.append(" and caseid is null or caseid = '' ");
			}
			// 是
			if("1".equals(iscase)){
				sql.append(" and caseid is not null ");
			}
		}
		sql.append(" order by t.appdate desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
		
	}

	/**
	 * 保存信息
	 * @param param
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public ResultMsg save(Map<String, Object> param) throws Exception{
		ResultMsg result = new ResultMsg(true, "");
		Set<String> removeSet = new HashSet<String>();
		Set<String> keys = param.keySet();
		for (String key : keys) {
			if(StringUtils.isEmpty((String)param.get(key))){
				removeSet.add(key);
			}
		}
		for (String string : removeSet) {
			param.remove(string);
		}
		ApplyRecordInfo info = new ApplyRecordInfo();
		String arid = (String) param.get("arid");
		String sendflag = (String) param.get("sendflag");
		if( StringUtils.isEmpty(arid)){
			BeanUtils.populate(info, param);
			info.setOpttype("1");
			if("1".equals(sendflag)){
				info.setOpttype("2");
			}
			arid = (String) dao.save(info);
			result.setTitle("新增成功");
			
		}else{
			info = dao.get(arid);
			BeanUtils.populate(info, param);
			if("1".equals(sendflag)){
				info.setOpttype("2");
			}
			dao.update(info);
			result.setTitle("修改成功");
		}
		if("1".equals(sendflag)){
			result.setTitle("发送成功");
		}
		return result;
	}

	/**
	 * 
	 * @param param
	 */
	public void delete(Map<String, Object> param) {
		String id = (String) param.get("id");
		dao.delete(id);
	}

	public Map<String,String> detail(Map<String, Object> param) {
		//获取StringBuffer对象，用来拼接sql语句
		Map<String,String> map = new HashMap<String, String>();
		String caseid = (String) param.get("caseid");
		StringBuffer sql = new StringBuffer();
		sql.append("select t.arid,t.caseid,t.reqtime, t.place,t.inquirer,t.noter,t.workunits, t.appname,t.sex,t.age,t.address, t.phone, t.issue,   t.answer,  t.appdate, t.opttype, t.issolve, t.remark,  t.userid,  t.operator,t.optdate ");
		sql.append(" , (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SEX' and a.status=0  and a.code=t.sex) sexname ");
		sql.append(" , (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ISORNOT' and a.status=0  and a.code=t.issolve) issolvename ");
		sql.append(" from B_APPLYRECORD t ");
		String id = (String) param.get("id");
		sql.append(" where arid='").append(id).append("'");
		List<Map<String,String>> result = mapDataDao.queryListBySQL(sql.toString());
		if(!CollectionUtils.isEmpty(result)){
			map = result.get(0);
		}
		return map;
	}
	
	public Map<String,String> applyDetailByCase(Map<String, Object> param) {
		//获取StringBuffer对象，用来拼接sql语句
		Map<String,String> map = new HashMap<String, String>();
		String caseid = (String) param.get("caseid");
		StringBuffer sql = new StringBuffer();
		sql.append("select t.arid,t.caseid,t.reqtime, t.place,t.inquirer,t.noter,t.workunits, t.appname,t.sex,t.age,t.address, t.phone, t.issue,   t.answer,  t.appdate, t.opttype, t.issolve, t.remark,  t.userid,  t.operator,t.optdate ");
		sql.append(" , (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SEX' and a.status=0  and a.code=t.sex) sexname ");
		sql.append(" , (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ISORNOT' and a.status=0  and a.code=t.issolve) issolvename ");
		sql.append(" from B_APPLYRECORD t ");
		sql.append(" where caseid='").append(caseid).append("'");
		List<Map<String,String>> result = mapDataDao.queryListBySQL(sql.toString());
		if(!CollectionUtils.isEmpty(result)){
			map = result.get(0);
		}
		return map;
	}

	/**
	 * 按照关联案件查询笔录列表
	 * @param param
	 * @return
	 */
	public PaginationSupport queryListByCase(Map<String, Object> param) {
		String appname = (String) param.get("appname");
		String inquirer = (String) param.get("inquirer");
		String starttime = (String) param.get("starttime");
		String endtime = (String) param.get("endtime");
		// b_standbaseinfo
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("select t.arid,t.caseid,t.reqtime, t.place,t.inquirer,t.noter,t.workunits, t.appname,t.sex,t.age,t.address, t.phone,   t.issue,   t.answer,  t.appdate, t.opttype, t.issolve, t.remark,  t.userid,  t.operator,t.optdate ");
		sql.append(" , (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SEX' and a.status=0  and a.code=t.sex) sexname ");
		sql.append(" , (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ISORNOT' and a.status=0  and a.code=t.issolve) issolvename ");
		sql.append(" from B_APPLYRECORD t ");
		sql.append(" where 1=1 ");
		sql.append(" and ISSOLVE = '0' ");
		sql.append(" and opttype = '2' ");
		sql.append(" and t.caseid is null order by t.appdate desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	/**
	 * 通过caseid查询arid
	 * @param caseid
	 * @return
	 */
	public String getAridByCaseId(String caseid) {
		String result = "";
	    ApplyRecordInfo info = new ApplyRecordInfo();
	    info.setCaseid(caseid);
	    List<ApplyRecordInfo> list =  dao.findByExample(info);
	    if(!CollectionUtils.isEmpty(list)){
	    	result = list.get(0).getArid();
	    }
		return result;
	}

}
