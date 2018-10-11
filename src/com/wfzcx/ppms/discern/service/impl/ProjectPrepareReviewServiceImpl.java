package com.wfzcx.ppms.discern.service.impl;

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
import com.sun.star.sdbc.SQLException;
import com.wfzcx.ppms.discern.dao.PrepareReviewZbDao;
import com.wfzcx.ppms.discern.dao.ProProjectDao;
import com.wfzcx.ppms.discern.dao.ProjectPrepareReviewDao;
import com.wfzcx.ppms.discern.po.ProPszb;
import com.wfzcx.ppms.discern.po.ProPszbZb;
import com.wfzcx.ppms.discern.service.ProjectPrepareReviewService;
import com.wfzcx.ppms.prepare.dao.ProQualitativeExpertDaoI;
import com.wfzcx.ppms.prepare.po.ProQualitativeExpert;
import com.wfzcx.ppms.synthesize.expert.dao.ProExpertDaoI;

/**
 * 评审准备实现类
 * @ClassName: ProjectPrepareReviewServiceImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2016年3月7日 下午3:26:47
 */
@Scope("prototype")
@Service("com.wfzcx.ppms.discern.service.impl.ProjectPrepareReviewServiceImpl")
public class ProjectPrepareReviewServiceImpl implements ProjectPrepareReviewService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProjectPrepareReviewDao dao;
	@Autowired
	ProProjectDao projectDao;
	@Autowired
	ProjectPrepareReviewDao pprDao;
	@Autowired
	ProExpertDaoI peDao;
	@Autowired
	PrepareReviewZbDao pprzDao;
	@Autowired
	ProQualitativeExpertDaoI proQualitativeExpertDao;//定性分析专家
	@Override
	public PaginationSupport queryProject(Map<String, Object> param)throws AppException {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select t.projectid,t.datatype, pro_name,pro_type,amount, pro_year,");
		sql.append("       pro_trade, pro_perate,pro_return,pro_sendtime,");
		sql.append("       pro_sendtype,pro_sendperson,pro_situation,pro_person,");
		sql.append("       pro_phone,pro_scheme,pro_schemepath,pro_reportpath,");
		sql.append("       pro_conditionpath,pro_article,pro_articlepath,wfid,t1.pszbid,");
		sql.append("       t1.status,t1.createuser,t1.createtime,t1.updateuser,t1.updatetime,IMPLEMENT_ORGAN,IMPLEMENT_PERSON,IMPLEMENT_PHONE,GOVERNMENT_PATH,cz_result,vfm_pjhj,opinion,");
		sql.append("       (select a.name from SYS_DEPT a where a.status=0  and a.code=t.implement_organ) implement_organ_name,t1.vfm_Dlpj,");
		sql.append("  case when t1.status is null then '未录入' else (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROPJZBSTATUS' and a.status=0  and a.code=t1.status) end cz_result_name,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROVFMPJHJ' and a.status=0  and a.code=t.VFM_PJHJ) VFM_PJHJ_NAME,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROTYPE' and a.status=0  and a.code=t.pro_type) pro_type_name,");
		sql.append("       (select a.name from SYS_YW_DICCODEITEM a where upper(a.elementcode)='PROTRADE' and a.status=0  and a.code=t.pro_trade) pro_trade_name,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROOPERATE' and a.status=0  and a.code=t.pro_perate) pro_perate_name,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PRORETURN' and a.status=0  and a.code=t.pro_return) pro_return_name,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSENDTYPE' and a.status=0  and a.code=t.pro_sendtype) pro_sendtype_name,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSFXM' and a.status=0  and a.code=t.sfxm) sfxm_name,sfxm,");
		sql.append("(select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.tjxm) tjxm_name,tjxm,");
		sql.append("(select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.sqbt) sqbt_name,sqbt,");
		sql.append("(select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t1.VFM_DLPJ) vfm_Dlpj_name,");
		sql.append(" btje,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='WFSTATUS' and a.status=0  and a.code=t.status) status_name,");
		sql.append("       (select a.username from sys_user a where  a.userid=t1.createuser) createusername,");
		sql.append("       (select a.username from sys_user a where  a.userid=t1.updateuser) updateusername ");
		sql.append("  from pro_project t,pro_pszb t1 where t.projectid=t1.projectid(+) and t.cz_result in('10','20') ");
		String xmhj = StringUtil.stringConvert(param.get("xmhj"));//项目环节
		String status = StringUtil.stringConvert(param.get("status"));//状态
		if("1".equals(status)){//待处理
			sql.append(" and (t1.status in ('1','3') or t1.status is null)");
		}else if("2".equals(status)){//已提交
			sql.append(" and t1.status = '2'");
		}
		String proName = StringUtil.stringConvert(param.get("proName"));
		if(!"".equals(proName)){
			sql.append(" and t.pro_name like '%").append(proName.trim()).append("%'");
		}
		String proPerson = StringUtil.stringConvert(param.get("proPerson"));
		if(!"".equals(proPerson)){
			sql.append(" and t.pro_person like '%").append(proPerson.trim()).append("%'");
		}
		String proTrade = StringUtil.stringConvert(param.get("proTrade"));
		if(!"".equals(proTrade)){
			sql.append(" and t.pro_trade in ('").append(proTrade.replaceAll(",", "','")).append("')");
		}
		String proPerate = StringUtil.stringConvert(param.get("proPerate"));
		if(!"".equals(proPerate)){
			sql.append(" and t.pro_perate in ('").append(proPerate.replaceAll(",", "','")).append("')");
		}
		String proReturn = StringUtil.stringConvert(param.get("proReturn"));
		if(!"".equals(proReturn)){
			sql.append(" and t.pro_return in ('").append(proReturn.replaceAll(",", "','")).append("')");
		}
		String proSendtype = StringUtil.stringConvert(param.get("proSendtype"));
		if(!"".equals(proSendtype)){
			sql.append(" and t.pro_sendtype in ('").append(proSendtype.replaceAll(",", "','")).append("')");
		}
		String proType = StringUtil.stringConvert(param.get("proType"));
		if(!"".equals(proType)){
			sql.append(" and t.pro_type in ('").append(proType.replaceAll(",", "','")).append("')");
		}
		if(!"".equals(xmhj)){
			sql.append(" and t1.xmhj(+) = '").append(xmhj).append("'");
		}
		String vfmPjhj = StringUtil.stringConvert(param.get("vfmPjhj"));
		if("2".equals(xmhj)){//项目准备环节
			//由2部分数据组成，1项目审批中vfm验证在项目准备环节的数据pro_project 中vfm_Pjhj=2 ，2 项目识别做vfm验证，并且财政承受能力通过的数据
			sql.append(" and ((t.vfm_pjhj =2) or( t.vfm_pjhj =1 and exists(select 1 from PRO_CZCSNL l where t.projectid=l.projectid and l.XMHJ =1 and l.STATUS=2 and l.FC_RESULT =1 )))");
			//必须项目实施方案已经通过审批
			sql.append(" and (exists(select 1 from PRO_SOLUTION n where t.projectid = n.projectid and n.status ='10'))");
		}else {
			if(!"".equals(vfmPjhj)){
				sql.append(" and t.vfm_pjhj ='").append(vfmPjhj).append("' ");
			}
		}
		sql.append("  order by t.projectid desc");

		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public PaginationSupport qualExpertGrid(Map map) throws AppException {
		String projectid = map.get("projectid")==null?"":map.get("projectid").toString();
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T.QUALEXPERTID,");
        sql.append("       T.EXPERT_NAME,");
        sql.append("       T.EXPERT_ID,");
        sql.append("       T.EXPERT_TYPE,");
        sql.append("       T.EXPERT_PHONE,");
        sql.append("       T.BIDMAJOR,");
        sql.append("       T.RESPONSIBLE_AREA,");
        sql.append("       T.REMARK,");
        sql.append("       T.PROJECTID,t.xmhj");
        sql.append("  FROM PRO_QUALITATIVE_EXPERT T");
        sql.append("  where 1=1 ");
        String xmhj  = StringUtil.stringConvert(map.get("xmhj"));
		if("".equals(projectid)){
			sql.append(" and 1<>1" );
		}else{
			sql.append(" and T.PROJECTID = "+projectid );
		}
		if(!"".equals(xmhj)){
			sql.append(" and T.xmhj = '").append(xmhj).append("' ");
		}
		System.out.println("【评审准备-定性分析专家sql：】"+sql.toString());
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public PaginationSupport queryPjzbTable(Map map) throws AppException {
		String pszbid = map.get("pszbid")==null?"":map.get("pszbid").toString();
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.zbkid,t.qz,t1.zbmc,t1.zbms, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROZBLB' and a.status=0  and a.code=t1.zblb) zblb ");		
		sql.append("  from pro_pszb_zb t,PRO_ZBK t1 where t.zbkid=t1.zbkid ");
		if("".equals(pszbid)){
			sql.append(" and 1<>1" );
		}else{
			sql.append(" and T.pszbid = "+pszbid );
		}
		System.out.println("【评价指标sql：】"+sql.toString());
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public PaginationSupport qryExpertByQ(Map map) {
		// TODO Auto-generated method stub
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		String name = map.get("q")==null?"":map.get("q").toString();
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
		sql.append("       T.BID_MAJOR,");
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
		sql.append("  WHERE 1=1 and T.is_use = 1");
		if(!"".equals(name)){
			sql.append(" and t.name like '%").append(name.trim()).append("%'");
		}
		sql.append(" ORDER BY T.EXPERTID");

		System.out.println("【评审准备-过滤专家库打印sql：】"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public List queryPszbList(Map<String, Object> param) throws AppException {
		String zbkid = StringUtil.stringConvert(param.get("zbkid"));
		StringBuffer sql = new StringBuffer();
		sql.append("select t.zbkid,t.zbmc,t.zbms,  ");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROZBLB' and a.status=0  and a.code=t.zblb) zblb ");
		sql.append(" from PRO_ZBK t where SFYX =1 ");
	    //获取查询条件
	    String zbmc =StringUtil.stringConvert(param.get("zbmc"));
		if(StringUtil.isNotBlank(zbmc)){
			sql.append(" and t.zbmc like '%").append(zbmc).append("%'");
		}
		if(!"".equals(zbkid)){
			sql.append(" and t.zbkid not in(").append(zbkid).append(") ");
		}
		sql.append(" order by t.zbmc");
	 return mapDataDao.queryListBySQL(sql.toString());
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor={Exception.class, RuntimeException.class,SQLException.class})
	public void pjzbSave(Map<String, Object> param) throws Exception {
		/**
		 * 1保存主表信息
		 * 2、保存专家信息
		 * 3、保存评审指标信息
		 */
		String projectid = StringUtil.stringConvert(param.get("projectid"));
		String updateFlag = StringUtil.stringConvert(param.get("updateFlag"));//修改标志
		if("".equals(updateFlag)){
			Integer pszbid = savePjzb(param);
			savaOrUpdateZj(param,pszbid);
			savaOrUpdateZb(param, pszbid);
		}else {
			String pszbid = StringUtil.stringConvert(param.get("pszbid"));//修改标志
			updatePjzb(param, Integer.valueOf(pszbid));
			savaOrUpdateZj(param,Integer.valueOf(pszbid));
			savaOrUpdateZb(param, Integer.valueOf(pszbid));
		}
	}
	/**
	 * 评审主表保存
	 * @Title: savePjzb 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @throws Exception 设定文件
	 */
	public Integer savePjzb(Map<String, Object> param) throws Exception{
		ProPszb ppProPszb  = new ProPszb();
		BeanUtils.populate(ppProPszb, param);
		SysUser user = SecureUtil.getCurrentUser();
		ppProPszb.setStatus(StringUtil.stringConvert(param.get("status")));
		ppProPszb.setCreateuser(user.getUserid().toString());//创建人
		ppProPszb.setCreatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//创建时间
		return (Integer) pprzDao.save(ppProPszb);
	}
	/**
	 * 修改保存
	 * @Title: updatePjzb 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @param pszbid
	 * @throws Exception 设定文件
	 */
	public void updatePjzb(Map<String, Object> param,Integer pszbid) throws Exception{
		ProPszb ppProPszb  = pprzDao.get(pszbid);
		SysUser user = SecureUtil.getCurrentUser();
		ppProPszb.setStatus(StringUtil.stringConvert(param.get("status")));
		ppProPszb.setVfmDlpj(StringUtil.stringConvert(param.get("vfmDlpj")));
		ppProPszb.setUpdateuser(user.getUserid().toString());//修改人
		ppProPszb.setUpdatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//修改时间
	    pprzDao.update(ppProPszb);
	}
	/**
	 * 定性评审专家
	 * @Title: savaZj
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @throws Exception 设定文件
	 */
	public void savaOrUpdateZj(Map<String, Object> param,Integer pszbid)throws Exception{
		String qualExpertData = StringUtil.stringConvert(param.get("qualExpertData"));
		String xmhj = StringUtil.stringConvert(param.get("xmhj"));
		String projectid = StringUtil.stringConvert(param.get("projectid"));
		List qualExpertList = (List) JSONObject.parse(qualExpertData);
		proQualitativeExpertDao.deleteBySQL("PRO_QUALITATIVE_EXPERT t", "t.pszbid = '"+pszbid+"'");
		for(int i =0;i<qualExpertList.size();i++){
			ProQualitativeExpert p = new ProQualitativeExpert();
			BeanUtils.populate(p, (Map)qualExpertList.get(i));
			p.setProjectid(Integer.parseInt(projectid));
			p.setPszbid(pszbid);
			p.setXmhj(xmhj);
			proQualitativeExpertDao.save(p);
		}
	}
	/**
	 * 评审指标
	 * @Title: savaOrUpdateZb 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @param pszbid
	 * @throws Exception 设定文件
	 */
	public void savaOrUpdateZb(Map<String, Object> param,Integer pszbid)throws Exception{
		String pjzbTableData = StringUtil.stringConvert(param.get("pjzbTableData"));
		String xmhj = StringUtil.stringConvert(param.get("xmhj"));
		List pjzbTableList = (List) JSONObject.parse(pjzbTableData);
		proQualitativeExpertDao.deleteBySQL("PRO_PSZB_ZB t", "t.pszbid = '"+pszbid+"' ");
		for(int i =0;i<pjzbTableList.size();i++){
			ProPszbZb p = new ProPszbZb();
			BeanUtils.populate(p, (Map)pjzbTableList.get(i));
			p.setPszbid(pszbid);
			pprDao.save(p);
		}
	
	}
	@Override
	public List queryIsExistPszb(String projectid,String xmhj) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select projectid from pro_pszb where projectid= ").append(projectid).append(" and xmhj='").append(xmhj).append("' ");
		return mapDataDao.queryListBySQL(sql.toString());
	}
	@Override
	public void revokePszb(String projectid, String pszbid) throws Exception {
		String sqlString ="update pro_pszb set status=3 where pszbid="+pszbid;
		mapDataDao.updateTX(sqlString);
	}
	@Override
	public void sendPszb(String projectid, String pszbid) throws Exception {
		String sqlString ="update pro_pszb set status=2 where pszbid="+pszbid;
		mapDataDao.updateTX(sqlString);
	}
	
	public String recallYN(Map<String, Object> param) throws Exception{
		String xmhj = (String)param.get("xmhj");
		String dqjd = (String)param.get("dqjd");
		String projectid = (String)param.get("projectid");
		String sql = "";
		String re = "1";//1代表可返回
		if ("ps".equals(dqjd)){//评审准备
			sql = "select count(1) c from pro_dxpj where status='2' and xmhj ='"+xmhj+"' and projectid=" + projectid;
		} else if ("dx".equals(dqjd)){//定性评价
			sql = "select count(1) c from pro_dlpj where status='2' and xmhj ='"+xmhj+"' and projectid=" + projectid;
		} else if ("dl".equals(dqjd)){//定量评价
			sql = "select count(1) c from pro_czcsnl where status='2' and xmhj ='"+xmhj+"' and projectid=" + projectid;
		} else if ("cz".equals(dqjd)){//财政承受能力
			if ("0".equals(xmhj)){//项目识别
				sql = "select count(1) c from pro_develop where projectid=" + projectid;
			} else if ("1".equals(xmhj)){//项目准备
				sql = "select count(1) c from pro_advance_result where projectid=" + projectid;
			}
		}
		List list = mapDataDao.queryListBySQL(sql);
		int num = Integer.valueOf(((Map)list.get(0)).get("c").toString());
		if (num > 0){
			re = "0";
		}
		return re; 
	}
	
}
