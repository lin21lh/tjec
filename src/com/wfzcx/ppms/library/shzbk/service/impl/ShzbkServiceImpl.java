package com.wfzcx.ppms.library.shzbk.service.impl;

import java.math.BigDecimal;
import java.util.List;
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
import com.jbf.common.util.StringUtil;
import com.wfzcx.ppms.library.shzbk.dao.ProSocialRegistNewDao;
import com.wfzcx.ppms.library.shzbk.po.ProSocialRegistNew;
import com.wfzcx.ppms.library.shzbk.service.ShzbkService;

/**
 * 社会资本库service实现类
 * @author wang_yliang
 *
 */
@Scope("prototype")
@Service("com.wfzcx.ppms.library.dsfjgk.service.impl.ShzbkServiceImpl")
public class ShzbkServiceImpl implements ShzbkService {

	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProSocialRegistNewDao proSocialRegistNewDao;

	
	@Override
	public PaginationSupport queryShzbk(Map<String, Object> param) throws AppException {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;

		StringBuffer sql = new StringBuffer();
		sql.append("select socialid,usercode,username,userpswd,linkperson,linkphone,")
		.append("orgcode,orgname,iscombo,status,application_time,")
		.append("audit_time,remark,audit_user,category_name,category_code,")
		.append("preferences_code,preferences_name,relevance_user,weixin,")
		.append("decode(relevance_user,null,'否','是') isrelevance,")
		.append("(select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.iscombo) iscombo_name,")
		.append("(select a.orgname from pro_social_regist a where a.socialid=t.relevance_user) relevance_user_name")
		.append(" from pro_social_regist t where 1=1 ");
		String status = StringUtil.stringConvert(param.get("status"));
		if(!"".equals(status)){
			sql.append(" and t.status = '").append(status.trim()).append("'");
		}
		String orgname = StringUtil.stringConvert(param.get("orgname"));
		if(!"".equals(orgname)){
			sql.append(" and t.orgname like '%").append(orgname.trim()).append("%'");
		}
		String linkphone = StringUtil.stringConvert(param.get("linkphone"));
		if(!"".equals(linkphone)){
			sql.append(" and t.linkphone like '%").append(linkphone.trim()).append("%'");
		}
		String isrelevance = StringUtil.stringConvert(param.get("isrelevance"));
		if ("0".equals(isrelevance)){//不关联
			sql.append(" and t.relevance_user is null");
		} else if ("1".equals(isrelevance)){//关联
			sql.append(" and t.relevance_user is not null");
		}
		String categoryCode = StringUtil.stringConvert(param.get("categoryCode"));
		if(!"".equals(categoryCode)){
			sql.append(" and t.category_code = '").append(categoryCode.trim()).append("'");
		}
		String preferencesCode = StringUtil.stringConvert(param.get("preferencesCode"));
		if(!"".equals(preferencesCode)){
			sql.append(" and t.preferences_code in ('").append(preferencesCode.replaceAll(",", "','")).append("')");
		}
		sql.append(" order by t.socialid desc");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);

	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String shzbkAddCommit(Map<String, Object> param) throws Exception{
		//解析页面投资偏好名称
		String preferencesName = (String)param.get("preferencesName");
		if (!"".equals(preferencesName) && preferencesName.indexOf("-")!= -1){
			preferencesName = preferencesName.substring(preferencesName.indexOf("-")+1);
		}
		param.put("preferencesName", preferencesName);
		
		ProSocialRegistNew regist = new ProSocialRegistNew();
		BeanUtils.populate(regist, param);
		Integer bySocialid = null;
		//页面用户关联不选择时置为空
		if ("".equals((String)param.get("relevanceUser"))){
			regist.setRelevanceUser(null);
		} else {
			bySocialid = regist.getRelevanceUser();
		}
		regist.setStatus("4");
		String msg = "";
		try {
			proSocialRegistNewDao.save(regist);
			String sql = "update pro_social_regist set relevance_user=" + regist.getSocialid() + " where socialid=" + bySocialid;
			proSocialRegistNewDao.updateBySql(sql);
			msg = "success";
		} catch (Exception e) {
			msg = "fail";
			throw new Exception();
		}
		return msg;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String shzbkEditCommit(Map<String, Object> param) throws Exception {
		ProSocialRegistNew regist = proSocialRegistNewDao.get(Integer.parseInt(StringUtil.stringConvert(param.get("socialid"))));
		Integer oRe = regist.getRelevanceUser();
		
		//解析页面投资偏好名称
		String preferencesName = (String)param.get("preferencesName");
		if (!"".equals(preferencesName) && preferencesName.indexOf("-")!= -1){
			preferencesName = preferencesName.substring(preferencesName.indexOf("-")+1);
		}
		param.put("preferencesName", preferencesName);
		BeanUtils.populate(regist, param);
		
		Integer nRe = null;
		//页面为空时置为null
		if ("".equals((String)param.get("relevanceUser"))){
			regist.setRelevanceUser(null);
		} else {
			nRe = regist.getRelevanceUser();
		}
		String msg = "";
		try {
			proSocialRegistNewDao.update(regist);
			//更新旧关联
			if (oRe != null){
				String sql1 = "update pro_social_regist set relevance_user='' where socialid=" + oRe;
				proSocialRegistNewDao.updateBySql(sql1);
			}
			//添加新关联
			if (nRe != null){
				String sql2 = "update pro_social_regist set relevance_user=" + regist.getSocialid() + " where socialid=" + nRe;
				proSocialRegistNewDao.updateBySql(sql2);
			}
			
			msg = "success";
		} catch (Exception e) {
			msg = "fail";
			throw new Exception();
		}
		return msg;
	}
	
	/**
	 *  根据主键socialid删除社会资本
	 * @param socialid
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void shzbkDelete(String socialid){
		ProSocialRegistNew regist = proSocialRegistNewDao.get(Integer.parseInt(StringUtil.stringConvert(socialid)));
		Integer oRe = regist.getRelevanceUser();
		String where = "socialid = " + socialid;
		proSocialRegistNewDao.deleteBySQL("PRO_SOCIAL_REGIST", where);
		//更新旧关联
		if (oRe != null){
			String sql1 = "update pro_social_regist set relevance_user='' where socialid=" + oRe;
			proSocialRegistNewDao.updateBySql(sql1);
		}
	}
	
	@Override
	public PaginationSupport queryShzbkRelevance(Map<String, Object> param) throws AppException {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;

		StringBuffer sql = new StringBuffer();
		sql.append("select socialid,usercode,username,userpswd,linkperson,linkphone,")
		.append("orgcode,orgname,iscombo,status,application_time,")
		.append("audit_time,remark,audit_user,category_name,category_code,")
		.append("preferences_code,preferences_name,relevance_user,weixin,")
		.append("(select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.iscombo) iscombo_name,")
		.append("(select a.orgname from pro_social_regist a where a.socialid=t.relevance_user) relevance_user_name")
		.append(" from pro_social_regist t where 1=1 ");
		String orgname = StringUtil.stringConvert(param.get("orgname"));
		if(!"".equals(orgname)){
			sql.append(" and t.orgname like '%").append(orgname.trim()).append("%'");
		}
		String linkperson = StringUtil.stringConvert(param.get("linkperson"));
		if(!"".equals(linkperson)){
			sql.append(" and t.linkperson like '%").append(linkperson.trim()).append("%'");
		}
		String socialid = StringUtil.stringConvert(param.get("socialid"));
		if(!"".equals(socialid)){
			sql.append(" and t.socialid != '" + socialid + "'");
		}
		String status = StringUtil.stringConvert(param.get("status"));
		sql.append(" and t.status = '" + status + "'");
		sql.append(" and t.relevance_user is null");
		sql.append(" order by t.socialid desc");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);

	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String shzbkRelevanceCommit(Map<String, Object> param) throws Exception{
		
		String msg = "";
		String sid1 = (String)param.get("sid1");
		String sid2 = (String)param.get("sid2");
		String sid3 = (String)param.get("sid3");//原被关联用户
		String sql1 = "update pro_social_regist set relevance_user=" + sid2 + " where socialid=" + sid1;
		String sql2 = "update pro_social_regist set relevance_user=" + sid1 + " where socialid=" + sid2;
		String sql3 = "update pro_social_regist set relevance_user=null where socialid=" + sid3;
		try {
			proSocialRegistNewDao.updateBySql(sql1);
			proSocialRegistNewDao.updateBySql(sql2);
			if (!"".equals(sid3)){
				proSocialRegistNewDao.updateBySql(sql3);
			}
			msg = "success";
		} catch (Exception e) {
			msg = "fail";
			throw new Exception();
		}
		return msg;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String shzbkCheck(Map<String, Object> param) throws Exception{
		String socialid = StringUtil.stringConvert(param.get("socialid"));;
		String orgname = StringUtil.stringConvert(param.get("orgname"));
		String flag = StringUtil.stringConvert(param.get("flag"));
		String msg = "success";//默认成功，不重名
		Map map = null;
		if ("add".equals(flag)){//新增页面
			String sql = "select count(1) c from pro_social_regist where status='4' and orgname='"+orgname.trim()+"'";
			List list = proSocialRegistNewDao.findMapBySql(sql);
			map =  (Map) list.get(0);
			BigDecimal num =(BigDecimal)map.get("c");
			if (num.intValue()> 0){
				msg = "fail";
			} 
			
		} else {//修改页面
			String sql = "select socialid from pro_social_regist where status='4' and orgname='"+orgname.trim()+"'";
			List list = proSocialRegistNewDao.findMapBySql(sql);
			for(int i=0;i<list.size();i++){
				map =  (Map) list.get(i);
				BigDecimal sid =(BigDecimal)map.get("socialid");
				
				if (!(sid.toString().equals(socialid))){
					msg = "fail";
					break;
				}
			}
			
		}
		return msg;
	}
}
