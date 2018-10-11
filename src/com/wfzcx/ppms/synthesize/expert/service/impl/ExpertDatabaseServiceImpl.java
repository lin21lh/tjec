package com.wfzcx.ppms.synthesize.expert.service.impl;

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
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.ppms.synthesize.expert.dao.ProAssociationDaoI;
import com.wfzcx.ppms.synthesize.expert.dao.ProAvoidUnitDaoI;
import com.wfzcx.ppms.synthesize.expert.dao.ProExpertDaoI;
import com.wfzcx.ppms.synthesize.expert.dao.ProExpertWorkedDaoI;
import com.wfzcx.ppms.synthesize.expert.dao.ProQualificationDaoI;
import com.wfzcx.ppms.synthesize.expert.po.ProAssociation;
import com.wfzcx.ppms.synthesize.expert.po.ProAvoidUnit;
import com.wfzcx.ppms.synthesize.expert.po.ProExpert;
import com.wfzcx.ppms.synthesize.expert.po.ProExpertWorked;
import com.wfzcx.ppms.synthesize.expert.po.ProQualification;
import com.wfzcx.ppms.synthesize.expert.service.ExpertDatabaseServiceI;

@Scope("prototype")
@Service("/synthesize/expert/service/impl/ExpertDatabaseServiceImpl")
public class ExpertDatabaseServiceImpl implements ExpertDatabaseServiceI {

	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProQualificationDaoI proQualificationDao;
	@Autowired
	ProExpertDaoI proExpertDao;
	@Autowired
	ProExpertWorkedDaoI proExpertWorkedDao;
	@Autowired
	ProAvoidUnitDaoI proAvoidUnitDao;
	@Autowired
	ProAssociationDaoI proAssociationDao;
	
	@Override
	public PaginationSupport qryExpert(Map map) {
		// TODO Auto-generated method stub
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		String name = map.get("name")==null?"":map.get("name").toString();
		String highestDegree = map.get("highestDegree")==null?"":map.get("highestDegree").toString();
		String bidMajor = map.get("bidMajor")==null?"":map.get("bidMajor").toString();
		String professionalTitle = map.get("professionalTitle")==null?"":map.get("professionalTitle").toString();
		String qualification = map.get("qualification")==null?"":map.get("qualification").toString();
		String highestOffering = map.get("highestOffering")==null?"":map.get("highestOffering").toString();
		String industry = map.get("industry")==null?"":map.get("industry").toString();
		String expertType = map.get("expertType")==null?"":map.get("expertType").toString();
		
		SysUser user = SecureUtil.getCurrentUser();
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT T.EXPERTID,");
		sql.append("       T.CODE,");
		sql.append("       T.NAME,");
		sql.append("       T.SEX,");
		sql.append("       T.BIRTHDAY,");
		sql.append("       T.IDTYPE,");
		sql.append("       T.IDCARD,");
		sql.append("       T.REGION,");
		sql.append("       T.POLITICS_STATUS,");
		sql.append("       T.IS_EMERGENCY,");
		sql.append("       T.EXPERT_TYPE,");
		sql.append("       T.HIGHEST_DEGREE,");
		sql.append("       T.MAJOR,");
		sql.append("       T.GRADUATE_SCHOOL,");
		sql.append("       T.WORKING_CONDITION,");
		sql.append("       T.MAJOR_TYPE,");
		sql.append("       T.MAJOR_YEAR,");
		sql.append("       T.DUTY,");
		sql.append("       T.PROFESSIONAL_TITLE,");
		sql.append("       T.TITLE_NUMBER,");
		sql.append("       T.UNIT_NAME,");
		sql.append("       T.UNIT_ADDRESS,");
		sql.append("       T.AVOID_UNIT,");
		sql.append("       T.PHONE_NUMBER,");
		sql.append("       T.WECHAT,");
		sql.append("       T.QQ,");
		sql.append("       T.EMAIL,");
		sql.append("       T.HOME_TELEPHONE,");
		sql.append("       T.HOME_ADDRESS,");
		sql.append("       T.HOME_POSTCODE,");
		sql.append("       T.PHOTO,");
		sql.append("       T.BID_MAJOR,t.is_Use,");
		sql.append("       (SELECT WMSYS.WM_CONCAT(L.FOREIGNKEY)");
		sql.append("          FROM PRO_ASSOCIATION L");
		sql.append("         WHERE L.MAJORKEY = T.EXPERTID");
		sql.append("           AND L.ELEMENTCODE = 'BIDMAJOR') BID_MAJOR_CODE,");
		sql.append("       (SELECT WMSYS.WM_CONCAT(A.CODE || '-' ||A.NAME)");
		sql.append("          FROM SYS_YW_DICCODEITEM A, PRO_ASSOCIATION L");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'BIDMAJOR'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = L.FOREIGNKEY");
		sql.append("           AND L.MAJORKEY = T.EXPERTID");
		sql.append("           AND L.ELEMENTCODE = 'BIDMAJOR') BID_MAJOR_NAME,");
		sql.append("       T.BID_AREA,");
		sql.append("       (SELECT WMSYS.WM_CONCAT(L.FOREIGNKEY)");
		sql.append("          FROM PRO_ASSOCIATION L");
		sql.append("         WHERE L.MAJORKEY = T.EXPERTID");
		sql.append("           AND L.ELEMENTCODE = 'BIDAREA') BID_AREA_CODE,");
		sql.append("       (SELECT WMSYS.WM_CONCAT(A.CODE || '-' ||A.NAME)");
		sql.append("          FROM SYS_YW_DICCODEITEM A, PRO_ASSOCIATION L");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'BIDAREA'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = L.FOREIGNKEY");
		sql.append("           AND L.MAJORKEY = T.EXPERTID");
		sql.append("           AND L.ELEMENTCODE = 'BIDAREA') BID_AREA_NAME,");
		sql.append("       T.QUALIFICATION,");
		sql.append("       T.CREATE_USER,");
		sql.append("       T.CREATE_TIME,");
		sql.append("       T.UPDATE_USER,");
		sql.append("       T.UPDATE_TIME,");
		sql.append("       T.IS_TRAIN,");
		sql.append("       T.RESEARCH,");
		sql.append("       T.HAS_BID_PROJECT,");
		sql.append("       T.INDUSTRY,");
		sql.append("       T.HIGHEST_OFFERING,");
		sql.append("       T.NATION,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'SEX'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.SEX) SEX_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'IDTYPE'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.IDTYPE) IDTYPE_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'POLITICSSTATUS'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.POLITICS_STATUS) POLITICS_STATUS_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'ISEMERGENCY'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.IS_EMERGENCY) IS_EMERGENCY_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'EXPERTTYPE'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.EXPERT_TYPE) EXPERT_TYPE_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'HIGHESTDEGREE'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.HIGHEST_DEGREE) HIGHEST_DEGREE_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'PROFESSIONALTITLE'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.PROFESSIONAL_TITLE) PROFESSIONAL_TITLE_NAME,");
		sql.append("       (SELECT WMSYS.WM_CONCAT(A.NAME)");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'QUALIFICATION'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE IN (SELECT F.ENUM_CODE");
		sql.append("                            FROM PRO_QUALIFICATION F");
		sql.append("                           WHERE F.EXPERTID = T.EXPERTID)) QUALIFICATION_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'ISORNOT'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.IS_TRAIN) IS_TRAIN_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'ISORNOT'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.IS_use) IS_use_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'HIGHESTOFFERING'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.HIGHEST_OFFERING) HIGHEST_OFFERING_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'MAJORTYPE'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.MAJOR_TYPE) MAJOR_TYPE_NAME,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'CATEGORY'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.INDUSTRY) INDUSTRY_NAME,");
		sql.append("       (SELECT A.USERNAME FROM SYS_USER A WHERE A.USERID = T.CREATE_USER) CREATE_USER_NAME,");
		sql.append("       (SELECT A.USERNAME FROM SYS_USER A WHERE A.USERID = T.UPDATE_USER) UPDATE_USER_NAME");
		sql.append("  FROM PRO_EXPERT T");
		sql.append("  WHERE 1=1 ");
		
		if(!"".equals(name)){
			sql.append(" and t.name like '%").append(name.trim()).append("%'");
		}
		if(!"".equals(highestDegree)){
			sql.append(" and t.highest_degree in ('").append(highestDegree.replaceAll(",", "','")).append("')");
		}
		if(!"".equals(bidMajor)){
			sql.append(" and exists (select tt.associationid from pro_association tt where tt.elementcode='BIDMAJOR' and tt.majorkey = t.expertid and tt.FOREIGNKEY in '"+bidMajor.replaceAll(",", "','")+"')");
		}
		if(!"".equals(professionalTitle)){
			sql.append(" and t.professional_title in ('").append(professionalTitle.replaceAll(",", "','")).append("')");
		}
		if(!"".equals(qualification)){
			sql.append(" and exists (select tf.QUALIFICATIONID from pro_qualification tf where tf.EXPERTID = t.EXPERTID and tf.ENUM_CODE in '"+qualification.replaceAll(",", "','")+"')");
		}
		if(!"".equals(highestOffering)){
			sql.append(" and t.highest_offering in ('").append(highestOffering.replaceAll(",", "','")).append("')");
		}
		if(!"".equals(industry)){
			sql.append(" and t.industry in ('").append(industry.replaceAll(",", "','")).append("')");
		}
		if(!"".equals(expertType)){
			sql.append(" and t.expert_type in ('").append(expertType.replaceAll(",", "','")).append("')");
		}
		sql.append(" ORDER BY T.EXPERTID");

		System.out.println("【专家库查询打印sql：】"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	@Override
	public PaginationSupport qryExpertWorked(Map map) {
		// TODO Auto-generated method stub
		
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		String expertid = map.get("expertid")==null?"":map.get("expertid").toString();
		SysUser user = SecureUtil.getCurrentUser();
		StringBuffer sql = new StringBuffer();
		if("".equals(expertid)){
			expertid="-1";
		}
		sql.append("select t.workedid,");
		sql.append("       t.name,");
		sql.append("       t.time,");
		sql.append("       t.duty,");
		sql.append("       t.certifier,");
		sql.append("       t.certifier_phone,");
		sql.append("       t.expertid");
		sql.append("  from pro_expert_worked t");
		sql.append(" where t.expertid ="+expertid);
		
		System.out.println("【工作履历查询打印sql：】"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	@Override
	public PaginationSupport qryQualification(Map map) {
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		String expertid = map.get("expertid")==null?"":map.get("expertid").toString();
		SysUser user = SecureUtil.getCurrentUser();
		StringBuffer sql = new StringBuffer();

		if("".equals(expertid)){
			expertid="-1";
		}
		sql.append("SELECT T.QUALIFICATIONID,");
		sql.append("       T.ENUM_CODE,");
		sql.append("       (SELECT A.NAME");
		sql.append("          FROM SYS_YW_DICENUMITEM A");
		sql.append("         WHERE UPPER(A.ELEMENTCODE) = 'QUALIFICATION'");
		sql.append("           AND A.STATUS = 0");
		sql.append("           AND A.CODE = T.ENUM_CODE) ENUM_CODE_NAME,");
		sql.append("       T.FILEPATH,");
		sql.append("       T.UNIT,");
		sql.append("       T.qualification_code,");
		sql.append("       T.START_TIME,");
		sql.append("       T.END_TIME,");
		sql.append("       T.EXPERTID");
		sql.append("  FROM PRO_QUALIFICATION T");
		sql.append(" where t.expertid ="+expertid);
		
		System.out.println("【执业资格查询打印sql：】"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	@Override
	public PaginationSupport qryAvoidUnitGrid(Map map) {
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		String expertid = map.get("expertid")==null?"":map.get("expertid").toString();
		SysUser user = SecureUtil.getCurrentUser();
		StringBuffer sql = new StringBuffer();
		if("".equals(expertid)){
			expertid="-1";
		}
		sql.append("SELECT F.UNITID, F.EXPERTID, F.NAME, F.IS_WORK, F.AVOID_TIME, F.REMARK FROM PRO_AVOID_UNIT F");
		sql.append(" where f.expertid ="+expertid);
		
		System.out.println("【回避单位查询打印sql：】"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveExpert(Map map) throws Exception {
		// TODO Auto-generated method stub
		String optFlag = map.get("optFlag")==null?"":map.get("optFlag").toString();
		String expertid = map.get("expertid")==null?"":map.get("expertid").toString();
		String menuid = map.get("menuid")==null?"":map.get("menuid").toString();
		String activityId = map.get("activityId")==null?"":map.get("activityId").toString();
		String sendFlag = map.get("sendFlag")==null?"":map.get("sendFlag").toString();
		String subStrWork = map.get("subStr_work")==null?"":map.get("subStr_work").toString();
		String subStrQual = map.get("subStr_qual")==null?"":map.get("subStr_qual").toString();
		String subStrUnit = map.get("subStr_unit")==null?"":map.get("subStr_unit").toString();
		String bidMajorCode = map.get("bidMajorCode")==null?"":map.get("bidMajorCode").toString();
		String bidAreaCode = map.get("bidAreaCode")==null?"":map.get("bidAreaCode").toString();
		String wfid = "";
		ProExpert pe = new ProExpert();
		BeanUtils.populate(pe, map);
		SysUser user = SecureUtil.getCurrentUser();
		if("add".equals(optFlag)){
			pe.setCreateUser(user.getUserid().toString());
			pe.setCreateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			proExpertDao.save(pe);
			expertid = pe.getExpertid().toString();
		}else if("edit".equals(optFlag)){
			if("".equals(expertid)){
				throw new AppException("信息主键没找到！");
			}
			pe.setUpdateUser(user.getUserid().toString());
			pe.setUpdateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			proExpertDao.update(pe);
		}
		
		//保存投标专业
		String[] bidMajorCodes = bidMajorCode.split(",");
		if("edit".equals(optFlag)){
			proAssociationDao.deleteBySQL("pro_association t", "t.majorkey = '"+expertid+"' and t.elementcode='BIDMAJOR'" );
		}
		for(int i =0;i<bidMajorCodes.length;i++){
			ProAssociation p  = new ProAssociation();
			p.setElementcode("BIDMAJOR");
			p.setForeignkey(bidMajorCodes[i]);
			p.setMajorkey(expertid);
			proAssociationDao.save(p);
		}
		//保存投标区域
		String[] bidAreaCodes = bidAreaCode.split(",");
		if("edit".equals(optFlag)){
			proAssociationDao.deleteBySQL("pro_association t", "t.majorkey = '"+expertid+"' and t.elementcode='BIDAREA'" );
		}
		for(int i =0;i<bidAreaCodes.length;i++){
			ProAssociation p  = new ProAssociation();
			p.setElementcode("BIDAREA");
			p.setForeignkey(bidAreaCodes[i]);
			p.setMajorkey(expertid);
			proAssociationDao.save(p);
		}
		
		//保存工作情况
		List listWork = (List) JSONObject.parse(subStrWork);
		if("edit".equals(optFlag)){
			proExpertWorkedDao.deleteBySQL("pro_expert_worked t", "t.expertid = "+expertid+"");
		}
		for(int i =0;i<listWork.size();i++){
			ProExpertWorked p = new ProExpertWorked();
			BeanUtils.populate(p, (Map)listWork.get(i));
			p.setExpertid(Integer.parseInt(expertid));
			proExpertWorkedDao.save(p);
		}
		
		//保存执业资格
		List listQual = (List) JSONObject.parse(subStrQual);
		if("edit".equals(optFlag)){
			proQualificationDao.deleteBySQL("pro_qualification t", "t.expertid = "+expertid+"");
		}
		for(int i =0;i<listQual.size();i++){
			ProQualification p = new ProQualification();
			BeanUtils.populate(p, (Map)listQual.get(i));
			p.setExpertid(Integer.parseInt(expertid));
			proQualificationDao.save(p);
		}
		
		//保存回避单位
		List listUnit = (List) JSONObject.parse(subStrUnit);
		if("edit".equals(optFlag)){
			proAvoidUnitDao.deleteBySQL("pro_avoid_unit t", "t.expertid = "+expertid+"");
		}
		for(int i =0;i<listUnit.size();i++){
			ProAvoidUnit p = new ProAvoidUnit();
			BeanUtils.populate(p, (Map)listUnit.get(i));
			p.setExpertid(Integer.parseInt(expertid));
			proAvoidUnitDao.save(p);
		}
	}

	@Override
	public void delExpert(Map map) throws Exception {
		// TODO Auto-generated method stub
		String expertids = map.get("expertids")==null?"":map.get("expertids").toString();
		
		if(!"".equals(expertids)){
			mapDataDao.delete("pro_association tt", "tt.MAJORKEY in ("+expertids.replaceAll(",", "','")+") and tt.ELEMENTCODE = 'BIDMAJOR'");//评标专业
			mapDataDao.delete("pro_association tt", "tf.MAJORKEY in ("+expertids.replaceAll(",", "','")+") and tf.ELEMENTCODE = 'BIDAREA'");//评标区域
			mapDataDao.delete("pro_qualification t", "t.expertid in ("+expertids+")");//执业资格
			mapDataDao.delete("pro_avoid_unit f", "f.expertid in ("+expertids+")");//回避单位
			mapDataDao.delete("pro_expert_worked l ", "l.expertid in ("+expertids+")");//工作简历
			mapDataDao.delete("pro_expert k", "k.expertid in ("+expertids+")");//专家信息
		}else{
			throw new Exception("删除异常！");
		}
		
		
	}

}
