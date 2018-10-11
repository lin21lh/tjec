package com.wfzcx.aros.ajgz.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.ajgz.dao.BCasetracebaseinfoDAO;
import com.wfzcx.aros.ajgz.po.BCasetracebaseinfo;
import com.wfzcx.aros.util.GCC;
import com.wfzcx.aros.xzfy.dao.CasebaseinfoDao;
import com.wfzcx.aros.xzfy.dao.ThirdbaseinfoDao;
import com.wfzcx.aros.xzfy.po.Casebaseinfo;
import com.wfzcx.aros.xzfy.vo.CasebaseinfoVo;
import com.wfzcx.aros.xzfy.vo.ThirdbaseinfoVo;

@Scope("prototype")
@Service("com.wfzcx.aros.ajgz.service.BCasetracebaseinfoService")
public class BCasetracebaseinfoService {

	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	BCasetracebaseinfoDAO dao;
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
		
		sql.append("select t.id,c.caseid,c.csaecode, c.appname,c.defname,t.exectype,t.remark,t.opttime,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='BZXRZXQK' and a.status=0  and a.code=t.exectype) exectypename");
		sql.append(" from b_casetracebaseinfo t,b_casebaseinfo c");
		sql.append(" where t.caseid=c.caseid and t.caseid='"+caseid+"'");

		String exectype = StringUtil.stringConvert(param.get("exectype"));
		if (exectype != null && !exectype.trim().equals("")) {
			sql.append(" and t.exectype='" + exectype + "'");
		}
		sql.append(" order by t.opttime ");
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
		BCasetracebaseinfo bCasetracebaseinfo = new BCasetracebaseinfo();
		BeanUtils.populate(bCasetracebaseinfo, param);
		SysUser user = SecureUtil.getCurrentUser();
		String id = "";
		
		bCasetracebaseinfo.setOperator(user.getUserid().toString());;
		bCasetracebaseinfo.setOpttime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));

		if (bCasetracebaseinfo != null && bCasetracebaseinfo.getId() != null && !bCasetracebaseinfo.getId().trim().equals("")) {
			dao.update(bCasetracebaseinfo);
		} else {
			id = (String) dao.save(bCasetracebaseinfo);
			//判断是否上传了附件
			//上传，将案件ID更新至附件表的keyid中
			//未上传，不做处理
			String fjkeyid = StringUtil.stringConvert(param.get("fjkeyid"));
			String fileSql = "select itemid from SYS_FILEMANAGE where keyid = '" + fjkeyid + "'";
			List<JSONObject> fileList = mapDataDao.queryListBySQL(fileSql);
			if (fileList != null && !fileList.isEmpty())
			{
				String updFjKeySql = "update sys_filemanage t set t.keyid = '" + id + "' where t.keyid = '" + fjkeyid + "'";
				dao.updateBySql(updFjKeySql);
			}
		}

		return id;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public BCasetracebaseinfo getBCasetracebaseinfo(String id) {
		BCasetracebaseinfo bCasetracebaseinfo = dao.get(id);
		return bCasetracebaseinfo;
	}

	public String delete(String id) {
		try {
			BCasetracebaseinfo bCasetracebaseinfo = new BCasetracebaseinfo();
			bCasetracebaseinfo.setId(id);
			dao.delete(bCasetracebaseinfo);
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
		// 案件编码
		String csaecode = StringUtil.stringConvert(param.get("csaecode")); 
		// 申请复议类型
		String casetype = StringUtil.stringConvert(param.get("casetype"));
		//申请人名称
		String appname = StringUtil.stringConvert(param.get("appname"));
		//被申请人名称
		String defname = StringUtil.stringConvert(param.get("defname"));
		

		sql.append("select c.caseid,c.csaecode,c.appname,c.defname,c.appdate, (SELECT NAME FROM SYS_YW_DICENUMITEM WHERE elementcode='B_CASEBASEINFO_CASETYPE' and code=c.casetype) as casetype from b_casebaseinfo c, pub_resultbaseinfo t ");
		//状态为已处理
		// sql.append(" where c.opttype = '").append(GCC.PROBASEINFO_OPTTYPE_PROCESSED).append("'");
		sql.append(" where c.caseid = t.caseid and t.protype='01' and t.opttype='3' ");
		
		if(!"".equals(appname) && !"null".equals(appname)) {
			sql.append(" and c.appname like '%").append(appname).append("%'");
		}
		if(!"".equals(csaecode) && !"null".equals(csaecode)) {
			sql.append(" and c.csaecode like '%").append(csaecode).append("%'");
		}
		if(!"".equals(casetype) && !"null".equals(casetype)) {
			sql.append(" and c.casetype = '").append(casetype).append("'");
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
