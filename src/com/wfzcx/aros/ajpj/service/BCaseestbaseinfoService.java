package com.wfzcx.aros.ajpj.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.ajpj.dao.BCaseestbaseinfoDAO;
import com.wfzcx.aros.ajpj.po.BCaseestbaseinfo;
import com.wfzcx.aros.util.GCC;
import com.wfzcx.aros.xzfy.dao.CasebaseinfoDao;
import com.wfzcx.aros.xzfy.dao.ThirdbaseinfoDao;
import com.wfzcx.aros.xzfy.po.Casebaseinfo;
import com.wfzcx.aros.xzfy.po.Thirdbaseinfo;
import com.wfzcx.aros.xzfy.vo.CasebaseinfoVo;
import com.wfzcx.aros.xzfy.vo.ThirdbaseinfoVo;

/**
 * 
 * @author LinXF
 * @date 2016年8月17日 下午3:14:20
 */
@Scope("prototype")
@Service("com.wfzcx.aros.ajpj.service.BCaseestbaseinfoService")
public class BCaseestbaseinfoService {

	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	BCaseestbaseinfoDAO dao;
	@Autowired
	CasebaseinfoDao casebaseinfoDao;
	@Autowired
	ThirdbaseinfoDao thirdDao;

	/**
	 * 
	 * @param map
	 * @return
	 */
	public PaginationSupport queryList(Map<String, Object> param) {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();

		String caseid = (String) param.get("caseid");
		
		sql.append("select t.id,c.caseid,c.csaecode,c.appname,c.defname,t.quatype,t.remark,t.opttime,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='CASEQUATYPE' and a.status=0  and a.code=t.quatype) quatypename");
		sql.append(" from b_caseestbaseinfo t,b_casebaseinfo c");
		sql.append(" where t.caseid=c.caseid and t.caseid='"+caseid+"'");

		String quatype = StringUtil.stringConvert(param.get("quatype"));
		if (quatype != null && !quatype.trim().equals("")) {
			sql.append(" and t.quatype='" + quatype + "'");
		}
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	/**
	 * 根据案件编号获取案件基本信息
	 * @param caseid
	 * @return
	 */
	public Casebaseinfo getCaseBaseinfo(String caseid) {
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		return casebaseinfo;
	}
	
	/**
	 * 根据案件编号获取案件基本信息
	 * @param caseid
	 * @return
	 */
	public CasebaseinfoVo getCaseBaseinfoVo(String caseid) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.*,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_APPTYPE' and a.status=0  and a.code=t.apptype) apptypename,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_IDTYPE' and a.status=0  and a.code=t.idtype) idtypename,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DEFTYPE' and a.status=0  and a.code=t.deftype) deftypename,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ADMINLEVEL' and a.status=0  and a.code=t.admtype) admtypename,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_CASETYPE' and a.status=0  and a.code=t.casetype) casetypename,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.ifcompensation) ifcompensationname");
		sql.append(" from B_CASEBASEINFO t where t.caseid = '"+caseid+"' ");
		
		//Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		List list = casebaseinfoDao.findVoBySql(sql.toString(), CasebaseinfoVo.class);
		if(list != null && list.size() > 0) {
			return (CasebaseinfoVo)list.get(0);
		}
		return null;
	}

	public String save(Map<String, Object> param) throws IllegalAccessException, InvocationTargetException {
		BCaseestbaseinfo bCaseestbaseinfo = new BCaseestbaseinfo();
		BeanUtils.populate(bCaseestbaseinfo, param);
		SysUser user = SecureUtil.getCurrentUser();
		String id = "";
		
		bCaseestbaseinfo.setOperator(user.getUserid().toString());;
		bCaseestbaseinfo.setOpttime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));

		if (bCaseestbaseinfo != null && bCaseestbaseinfo.getId() != null && !bCaseestbaseinfo.getId().trim().equals("")) {
			dao.update(bCaseestbaseinfo);
		} else {
			id = (String) dao.save(bCaseestbaseinfo);
		}

		return id;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public BCaseestbaseinfo getBCaseestbaseinfo(String id) {
		BCaseestbaseinfo bCaseestbaseinfo = dao.get(id);
		return bCaseestbaseinfo;
	}

	public String delete(String id) {
		try {
			BCaseestbaseinfo bCaseestbaseinfo = new BCaseestbaseinfo();
			bCaseestbaseinfo.setId(id);
			dao.delete(bCaseestbaseinfo);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "";
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	public PaginationSupport queryCaseBaseinfoList(Map<String, Object> param) {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		
		//申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		//被申请人
		String defname = StringUtil.stringConvert(param.get("defname"));

		sql.append("select c.caseid,c.csaecode,c.appname,c.defname,c.appdate from b_casebaseinfo c ");
		//状态为2 已处理
		sql.append(" where c.opttype = '").append(GCC.PROBASEINFO_OPTTYPE_PROCESSED).append("'");
		
		if(!"".equals(appname) && !"null".equals(appname)) {
			sql.append(" and c.appname like '%").append(appname).append("%'");
		}
		if(!"".equals(defname) && !"null".equals(defname)) {
			sql.append(" and c.defname like '%").append(defname).append("%'");
		}
		
		System.out.println("----sql: "+sql.toString());
    	return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	
	/**
	 * 根据案件编号获取案件基本信息
	 * @param caseid
	 * @return
	 */
	public ThirdbaseinfoVo getThirdbaseinfoByCaseid(String caseid) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.*,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_APPTYPE' and a.status=0  and a.code=t.thtype) thtypename,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_IDTYPE' and a.status=0  and a.code=t.thidtype) thidtypename");
		sql.append(" from b_thirdbaseinfo t where t.caseid = '"+caseid+"' ");
		
		List list = thirdDao.findVoBySql(sql.toString(), ThirdbaseinfoVo.class);
		if(list != null && list.size() > 0) {
			return (ThirdbaseinfoVo)list.get(0);
		}
		return null;
	}
}
