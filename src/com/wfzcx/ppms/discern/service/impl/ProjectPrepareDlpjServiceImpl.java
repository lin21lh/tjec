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
import com.wfzcx.ppms.discern.dao.ProDlpjCbDao;
import com.wfzcx.ppms.discern.dao.ProDlpjDao;
import com.wfzcx.ppms.discern.po.ProDlpj;
import com.wfzcx.ppms.discern.po.ProDlpjCb;
import com.wfzcx.ppms.discern.service.ProjectPrepareDlpjService;

@Scope("prototype")
@Service("com.wfzcx.ppms.discern.service.impl.ProjectPrepareDlpjServiceImpl")
public class ProjectPrepareDlpjServiceImpl implements ProjectPrepareDlpjService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProDlpjDao proDlpjDao;
	@Autowired
	ProDlpjCbDao proDlpjCbDao;
	
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
		sql.append("       (select a.name from SYS_DEPT a where a.status=0  and a.code=t.implement_organ) implement_organ_name,");
		sql.append(" case when t2.vfm_Dlpj is null then t1.vfm_Dlpj else t2.vfm_Dlpj end vfm_Dlpj,");
		sql.append("  case when t3.status is null then '未录入' else (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROPJZBSTATUS' and a.status=0  and a.code=t3.status) end cz_result_name,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROVFMPJHJ' and a.status=0  and a.code=t.VFM_PJHJ) VFM_PJHJ_NAME,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROTYPE' and a.status=0  and a.code=t.pro_type) pro_type_name,");
		sql.append("       (select a.name from SYS_YW_DICCODEITEM a where upper(a.elementcode)='PROTRADE' and a.status=0  and a.code=t.pro_trade) pro_trade_name,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROOPERATE' and a.status=0  and a.code=t.pro_perate) pro_perate_name,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PRORETURN' and a.status=0  and a.code=t.pro_return) pro_return_name,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSENDTYPE' and a.status=0  and a.code=t.pro_sendtype) pro_sendtype_name,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSFXM' and a.status=0  and a.code=t.sfxm) sfxm_name,sfxm,");
		sql.append("(select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.tjxm) tjxm_name,tjxm,");
		sql.append("(select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.sqbt) sqbt_name,sqbt,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='JUDGERESULT' and a.status=0  and a.code=t2.qual_Result) qual_Result_name,");
		sql.append(" case when t2.vfm_Dlpj is null then (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t1.VFM_DLPJ) ");
		sql.append(" else (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t2.VFM_DLPJ) end vfm_Dlpj_name,");
		sql.append(" btje,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='WFSTATUS' and a.status=0  and a.code=t.status) status_name,");
		sql.append("       (select a.username from sys_user a where  a.userid=t1.createuser) createusername,");
		sql.append("       (select a.username from sys_user a where  a.userid=t1.updateuser) updateusername, ");
		sql.append(" t2.dxpjid,t2.qual_verifytime,t2.qual_conclusion,t2.qual_df,t2.qual_result,t2.qual_path,t3.dlpjid ");
		sql.append("  from pro_project t,pro_pszb t1,PRO_DXPJ t2 ,PRO_DLPJ T3");
		sql.append("  where t.projectid=t1.projectid and t.projectid=t2.projectid(+) and t.cz_result in('10','20') ");
		sql.append("  and t1.xmhj =t2.xmhj  and t2.dxpjid=t3.dxpjid(+) ");
		sql.append(" and t2.VFM_DLPJ ='1'");//进行VFM定量评价
		sql.append("  and t2.status = '2' ");//VFM定性分析已经提交
		String xmhj = StringUtil.stringConvert(param.get("xmhj"));//项目环节
		if(!"".equals(xmhj)){
			sql.append(" and t1.xmhj = '").append(xmhj).append("'");
			sql.append(" and t3.xmhj(+) = '").append(xmhj).append("'");
		}
		String status = StringUtil.stringConvert(param.get("status"));//状态
		if("1".equals(status)){//待处理
			sql.append(" and (t3.status in ('1','3') or t3.status is null)");
		}else if("2".equals(status)){//已提交
			sql.append(" and t3.status = '2'");
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
		String vfmPjhj = StringUtil.stringConvert(param.get("vfmPjhj"));
		if(!"".equals(vfmPjhj)&&"1".equals(xmhj)){
			sql.append(" and t.vfm_pjhj ='").append(vfmPjhj).append("' ");
		}
		sql.append("  order by t.projectid desc");

		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	/**
	 * 查询定量分析是否新增
	 */
	@Override
	public List queryIsExistDlpj(String dxpjid, String xmhj) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select projectid from PRO_DlPJ where dxpjid= ").append(dxpjid).append(" and xmhj='").append(xmhj).append("' ");
		return mapDataDao.queryListBySQL(sql.toString());
	}
	@Override
	public PaginationSupport queryDsfJg(Map map) {
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		String name = map.get("q")==null?"":map.get("q").toString();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from PRO_DSFJGk t where 1=1 ").append("");
		if(!"".equals(name)){
			sql.append(" and  organ_name like '%").append(name).append("%' ");
		}
		sql.append(" order by dsfjgid ");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor={Exception.class})
	public void dlpjSave(Map<String, Object> param) throws Exception {
		/**
		 * 1、保存主表
		 * 2、保存第三方机构表。
		 */
		String updateFlag = StringUtil.stringConvert(param.get("updateFlag"));//修改标志
		if("".equals(updateFlag)){//新增
			Integer dlpjid = dlpjZbSave(param);
			saveDsfjg(param, dlpjid);
		}else if("1".equals(updateFlag)){//修改
			String dlpjid = StringUtil.stringConvert(param.get("dlpjid"));//
			dlpjZbUpdate(param, Integer.valueOf(dlpjid));
			saveDsfjg(param, Integer.valueOf(dlpjid));
		}
		
	}
	/**
	 * 保存定量分析主表
	 * @Title: dlpjZbSave 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws Exception 设定文件
	 */
	public Integer dlpjZbSave(Map<String, Object> param) throws Exception{
		ProDlpj pd =  new ProDlpj();
		BeanUtils.populate(pd, param);
		SysUser user = SecureUtil.getCurrentUser();
		pd.setStatus(StringUtil.stringConvert(param.get("status")));
		pd.setCreateuser(user.getUserid().toString());//创建人
		pd.setCreatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//创建时间
		return (Integer) proDlpjDao.save(pd);
	}
	/**
	 * 更新定量分析主表
	 * @Title: dlpjZbUpdate 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @throws Exception 设定文件
	 */
	public void dlpjZbUpdate(Map<String, Object> param,Integer dlpjid)throws Exception{
		ProDlpj pd = proDlpjDao.get(dlpjid);
		BeanUtils.populate(pd, param);
		SysUser user = SecureUtil.getCurrentUser();
		pd.setUpdateuser(user.getUserid().toString());//修改人
		pd.setUpdatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//修改时间
		proDlpjDao.update(pd);
	}
	public void saveDsfjg(Map<String, Object> param,Integer dlpjid)throws Exception{
		/**
		 * 1、删除明细
		 * 2、保存明细
		 */
		String thirdOrganGridData = StringUtil.stringConvert(param.get("thirdOrganGridData"));
		String projectid = StringUtil.stringConvert(param.get("projectid"));
		String pszbid = StringUtil.stringConvert(param.get("pszbid"));
		List toList = (List) JSONObject.parse(thirdOrganGridData);
		proDlpjCbDao.deleteBySQL("PRO_DLPJ_CB t", "t.dlpjid = '"+dlpjid+"'");
		for(int i =0;i<toList.size();i++){
			ProDlpjCb pdc = new ProDlpjCb();
			Map map = (Map) toList.get(i);
			BeanUtils.populate(pdc, map);
			pdc.setDsfjgid(Integer.valueOf(StringUtil.stringConvert(map.get("dsfjgid"))));
			pdc.setDlpjid(dlpjid);
			proDlpjCbDao.save(pdc);
		}
	}
	@Override
	public List querythirdOrgan(String dlpjid) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select t.*,(select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.vom_Result)  vom_Result_Name from PRO_DLPJ t ");
		sql.append(" where dlpjid = ").append(dlpjid);
		return 	mapDataDao.queryListBySQLForConvert(sql.toString());
	}
	@Override
	public PaginationSupport thirdOrgQuery(Map map) throws AppException {
		String dxpjid = map.get("dxpjid")==null?"":map.get("dxpjid").toString();
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select t1.*");
		sql.append("  from pro_dlpj t, PRO_DLPJ_CB t1");
		sql.append(" where t.dlpjid = t1.dlpjid");
		sql.append("   and t.dxpjid = ");
		sql.append(dxpjid);
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public void sendDlpj(String projectid, String dlpjid) throws Exception {
		String sqlString ="update PRO_DlPJ set status=2 where dlpjid="+dlpjid;
		mapDataDao.updateTX(sqlString);
	}
	@Override
	public void revokeDlpj(String projectid, String dlpjid) throws Exception {
		String sqlString ="update PRO_DlPJ set status=3 where dlpjid="+dlpjid;
		mapDataDao.updateTX(sqlString);
	}
}
