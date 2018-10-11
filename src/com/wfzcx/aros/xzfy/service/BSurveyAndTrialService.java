package com.wfzcx.aros.xzfy.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.xzfy.dao.BSurveyrecordDao;
import com.wfzcx.aros.xzfy.dao.BTrialbaseinfoDao;
import com.wfzcx.aros.xzfy.po.BSurveyrecord;
import com.wfzcx.aros.xzfy.po.BTrialbaseinfo;
import com.wfzcx.aros.xzys.dao.BRespbaseinfoDAO;

/**
 * 行政复议案件调查笔录和庭审笔录service
 * @author wzk
 * @date 2016年8月11日 14:22:14
 *
 */
@Scope("prototype")
@Service("com.wfzcx.aros.xzfy.service.BSurveyAndTrialService")
public class BSurveyAndTrialService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	BSurveyrecordDao surveyDao;
	@Autowired
	BTrialbaseinfoDao trialDao;	
	@Autowired
	ParamCfgComponent pcfg;
	@Autowired
	BRespbaseinfoDAO respdao;

	
	/**
	 * 分页查询
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport querySurveyGrid(Map<String, Object> param)   throws AppException{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		String caseid = StringUtil.stringConvert(param.get("caseid"));

		sql.append(" select srid, caseid, investtime,place, invester, recorder, efinvester,");
		sql.append(" sex, phone, item, obligrights,remark, lasttime");
		
		//查询采购结果审核通过的项目
		sql.append(" from B_SURVEYRECORD t ");
		sql.append(" where t.caseid = '"+caseid+"' ");

		sql.append("  order by t.investtime ");
		System.out.println("sql--"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	/**
	 * 分页查询
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryTrialGrid(Map<String, Object> param)   throws AppException{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		String reviewtype = StringUtil.stringConvert(param.get("reviewtype"));
		
		sql.append(" select trialid, caseid, trialtype, htype, moderator, trialtime, trialplace,");
		sql.append(" recorder, joiner, matters, contents, operator, opttime,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='TRIALTYPE' and a.status=0 and a.code = t.trialtype) trialtypename");
		sql.append(" from B_TRIALBASEINFO t");
		sql.append(" where t.caseid = '").append(caseid).append("' and trialtype='").append(reviewtype).append("' ");
		sql.append(" order by t.opttime ");
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	/**
	 * 保存调查笔录信息
	 * @Title: saveSurvey 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public  String saveSurvey(Map<String, Object> param) throws Exception{
		BSurveyrecord data = new BSurveyrecord();
		//取值
		BeanUtils.populate(data, param);
				
		data.setLasttime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//修改时间
		String id = StringUtil.stringConvert(param.get("srid"));
		if("".equals(id)) {
			//新增
			surveyDao.save(data);
		}else {
			surveyDao.update(data);
		}
		return "";
	}
	
	/**
	 * 保存审理记录信息
	 * @Title: saveTrial 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws Exception 设定文件
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public  String saveTrial(Map<String, Object> param) throws Exception
	{
		BTrialbaseinfo data = new BTrialbaseinfo();
		//取值
		BeanUtils.populate(data, param);
		//操作时间
		data.setOpttime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		//获取系统当前登录用户信息，并将用户信息转到到行政复议申请PO中
		SysUser user = SecureUtil.getCurrentUser();
		data.setOperator(user.getUsername());
		String id = StringUtil.stringConvert(param.get("trialid"));
		if ("".equals(id))
		{
			id = (String)trialDao.save(data);
			//判断是否上传了附件
			//上传，将案件ID更新至附件表的keyid中
			//未上传，不做处理
			String fjkeyid = (String) param.get("caseid");
			String trialtype = (String) param.get("trialtype");
			String fileSql = "select itemid from SYS_FILEMANAGE where keyid = '" + fjkeyid + "' and elementcode='XZFY_TS_" + trialtype+  "'" ;
			List<JSONObject> fileList = mapDataDao.queryListBySQL(fileSql);
			if (fileList != null && !fileList.isEmpty())
			{
				String updFjKeySql = "update sys_filemanage t set t.keyid = '" + id + "' where t.keyid = '" + fjkeyid + "'  and elementcode='XZFY_TS_" + trialtype+  "'" ;
				respdao.updateBySql(updFjKeySql);
			}
			
		}
		else
		{
			trialDao.update(data);
		}

		return "";
	}
	
	
	/**
	 * 按照id删除调查笔录
	 * @Title: deleteSurveyById 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param id
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String deleteSurveyById(String id) throws Exception {
		if("".equals(id)) {
			throw new Exception("参数id的值未获取到！");
		}
		surveyDao.delete(id);

		return "";
	}
	
	/**
	 * 按照id删除庭审笔录
	 * @Title: deleteTrialById 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param id
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String deleteTrialById(String id) throws Exception {
		if("".equals(id)) {
			throw new Exception("参数id的值未获取到！");
		}
		trialDao.delete(id);

		return "";
	}
	
}
