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
import com.wfzcx.ppms.discern.dao.ProDxpjDao;
import com.wfzcx.ppms.discern.dao.ProDxpjZbjgDao;
import com.wfzcx.ppms.discern.po.ProDxpj;
import com.wfzcx.ppms.discern.po.ProDxpjZbjg;
import com.wfzcx.ppms.discern.service.ProjectPrepareDxpjService;

@Scope("prototype")
@Service("com.wfzcx.ppms.discern.service.impl.ProjectPrepareDxpjServiceImpl")
public class ProjectPrepareDxpjServiceImpl implements ProjectPrepareDxpjService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProDxpjDao proDxpjDao;
	@Autowired
	ProDxpjZbjgDao proDxpjZbjgDao;
	
	public PaginationSupport queryProject(Map<String, Object> param)throws AppException {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select t.projectid,t.datatype, pro_name,pro_type,amount, pro_year,");
		sql.append("       pro_trade, pro_perate,pro_return,pro_sendtime,");
		sql.append("       pro_sendtype,pro_sendperson,pro_situation,pro_person,");
		sql.append("       pro_phone,pro_scheme,pro_schemepath,pro_reportpath,");
		sql.append("       pro_conditionpath,pro_article,pro_articlepath,wfid,t1.pszbid,");
		sql.append("       t1.status,t2.createuser,t2.createtime,t2.updateuser,t2.updatetime,IMPLEMENT_ORGAN,IMPLEMENT_PERSON,IMPLEMENT_PHONE,GOVERNMENT_PATH,cz_result,vfm_pjhj,opinion,");
		sql.append("       (select a.name from SYS_DEPT a where a.status=0  and a.code=t.implement_organ) implement_organ_name,");
		sql.append(" case when t2.vfm_Dlpj is null then t1.vfm_Dlpj else t2.vfm_Dlpj end vfm_Dlpj,");
		sql.append("  case when t2.status is null then '未录入' else (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROPJZBSTATUS' and a.status=0  and a.code=t2.status) end cz_result_name,");
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
		sql.append("       (select a.username from sys_user a where  a.userid=t2.createuser) createusername,");
		sql.append("       (select a.username from sys_user a where  a.userid=t2.updateuser) updateusername, ");
		sql.append(" t2.dxpjid,t2.qual_verifytime,t2.qual_conclusion,t2.qual_df,t2.qual_result,t2.qual_path ");
		sql.append("  from pro_project t,pro_pszb t1,PRO_DXPJ t2 where t.projectid=t1.projectid and t.projectid=t2.projectid(+) and t.cz_result in('10','20') ");
		sql.append("  and t1.status = '2' ");//项目准备已经提交
		String xmhj = StringUtil.stringConvert(param.get("xmhj"));//项目环节
		String status = StringUtil.stringConvert(param.get("status"));//状态
		if("1".equals(status)){//待处理
			sql.append(" and (t2.status in ('1','3') or t2.status is null)");
		}else if("2".equals(status)){//已提交
			sql.append(" and t2.status = '2'");
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
			sql.append(" and t1.xmhj = '").append(xmhj).append("'");
			sql.append(" and t2.xmhj(+) = '").append(xmhj).append("'");
		}
		if("1".equals(xmhj)){
			String vfmPjhj = StringUtil.stringConvert(param.get("vfmPjhj"));
			if(!"".equals(vfmPjhj)){
				sql.append(" and t.vfm_pjhj ='").append(vfmPjhj).append("' ");
			}
		}
		sql.append("  order by t.projectid desc");

		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	@Override
	public List queryIsExistDxpj(String projectid,String xmhj) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select projectid from PRO_DXPJ where projectid= ").append(projectid).append(" and xmhj='").append(xmhj).append("' ");
		return mapDataDao.queryListBySQL(sql.toString());
	}

	@Override
	public String getZbdfGridColumns(String pszbid) throws Exception {
		/**
		 * 1.查询所有专家
		 */
		List zjList = getZjList(pszbid);
		String returnString = "";
		//固定列
		returnString += "[[{ field: 'zbmc', title: '指标名称', width: 200,halign : 'center'},";
		returnString += "{ field: 'qz', title: '权值', width: 80,halign : 'center',align:'right'},";
		returnString += "{ field: 'zbid', title: 'zbid', width: 80,halign : 'center',hidden:'true'},";
		for (int i = 0; i < zjList.size(); i++) {
			Map map = (Map) zjList.get(i);
			returnString += "{ field: '"+map.get("qualexpertid")+"', title: '"+map.get("expert_name")+"',width: 120,halign : 'center',align:'right',";
			returnString += " editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入评分',min:0,precision:2,max:100} }}";
			if(i!=zjList.size()-1){
				returnString += ",";
			}
		}
		returnString += "]]";
		return returnString;
	}
	/**
	 * 查询专家列表
	 * @Title: getZjList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param pszbid
	 * @return 设定文件
	 */
	public List getZjList(String pszbid){
		String zjSql = " select  t.qualexpertid,t.expert_name from PRO_QUALITATIVE_EXPERT t where t.pszbid='"+pszbid+"' order by t.qualexpertid ";
		return mapDataDao.queryListBySQL(zjSql.toString());
	}
	@Override
	public PaginationSupport queryZbList(Map map) throws AppException {
		String pszbid = map.get("pszbid")==null?"":map.get("pszbid").toString();
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select   t.zbid, t1.zbmc, t.qz,t2.*");
		sql.append("  from pro_pszb_zb t,");
		sql.append("       PRO_ZBK t1,");
		sql.append("       (select a.zbid zbida,");
		//获取相应的专家。
		List  zjList = getZjList(pszbid);
		for (int i = 0; i < zjList.size(); i++) {//将行转换为列
			Map zjMap = (Map) zjList.get(i);
			String qualexpertid = StringUtil.stringConvert(zjMap.get("qualexpertid"));
			if(i!=zjList.size()-1){
				sql.append("  max(decode(a.qualexpertid,").append(qualexpertid).append(", a.df)) \"").append(qualexpertid).append("\",");
			}else {
				sql.append("  max(decode(a.qualexpertid,").append(qualexpertid).append(", a.df)) \"").append(qualexpertid).append("\"");
			}
		}
		sql.append("from PRO_DXPJ_ZBJG a");
		sql.append("          where 1=1 ");
		if("".equals(pszbid)){
			sql.append(" and 1<>1" );
		}else{
			sql.append(" and a.pszbid = "+pszbid );
		}
		sql.append("         group by a.zbid) t2");
		sql.append(" where t.zbkid = t1.zbkid");
		sql.append("   and t.zbid = t2.zbida(+)");
		if("".equals(pszbid)){
			sql.append(" and 1<>1" );
		}else{
			sql.append(" and T.pszbid = "+pszbid );
		}
		sql.append(" order by t.zbid ");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor={Exception.class})
	public void zbdfSave(Map<String, Object> param) throws Exception {
		/**
		 * 1、保存主表PRO_DXPJ
		 * 2、保存得分表PRO_DXPJ_ZBJG
		 */
		String updateFlag = StringUtil.stringConvert(param.get("updateFlag"));//修改标志
		String dxpjid = StringUtil.stringConvert(param.get("dxpjid"));//
		if("".equals(updateFlag)){//新增
			Integer dxpjidNew =	saveZbdf(param);
			saveZbdfMx(param, dxpjidNew);
		}else if("1".equals(updateFlag)){//修改
			updateZdbf(param, Integer.valueOf(dxpjid));
			saveZbdfMx(param, Integer.valueOf(dxpjid));
		}
	}
	/**
	 * 定性评价主表
	 * @Title: saveZbdf 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws Exception 设定文件
	 */
	public Integer saveZbdf(Map<String, Object> param) throws Exception{
		ProDxpj pd = new ProDxpj();
		BeanUtils.populate(pd, param);
		SysUser user = SecureUtil.getCurrentUser();
		pd.setStatus(StringUtil.stringConvert(param.get("status")));
		pd.setCreateuser(user.getUserid().toString());//创建人
		pd.setCreatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//创建时间
		return (Integer) proDxpjDao.save(pd);
	}
	/**
	 * 更新定性评价主表
	 * @Title: updateZdbf 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @param dxpjid
	 * @throws Exception 设定文件
	 */
	public void updateZdbf(Map<String, Object> param,Integer dxpjid)throws Exception{
		ProDxpj pd = proDxpjDao.get(dxpjid);
		pd.setQualConclusion(StringUtil.stringConvert(param.get("qualConclusion")));
		pd.setQualDf(StringUtil.stringConvert(param.get("df")));
		pd.setQualPath(StringUtil.stringConvert(param.get("qualPath")));
		pd.setQualResult(StringUtil.stringConvert(param.get("qualResult")));
		pd.setQualVerifytime(StringUtil.stringConvert(param.get("qualVerifytime")));
		pd.setStatus(StringUtil.stringConvert(param.get("status")));
		pd.setVfmDlpj(StringUtil.stringConvert(param.get("vfmDlpj")));
		SysUser user = SecureUtil.getCurrentUser();
		pd.setUpdateuser(user.getUserid().toString());//修改人
		pd.setUpdatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//修改时间
		proDxpjDao.update(pd);
	}
	/**
	 * 保存得分明细
	 * @Title: saveZbdfMx 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @param dxpjid
	 * @throws Exception 设定文件
	 */
	public void saveZbdfMx(Map<String, Object> param,Integer dxpjid)throws Exception{
		/**
		 * 1、获取相关指标
		 * 2、删除指标得分
		 * 3、将前录入的数据保存至数据库
		 */
		String zbdfGridData = StringUtil.stringConvert(param.get("zbdfGridData"));
		String projectid = StringUtil.stringConvert(param.get("projectid"));
		String pszbid = StringUtil.stringConvert(param.get("pszbid"));
		List zbdfGridList = (List) JSONObject.parse(zbdfGridData);
		proDxpjZbjgDao.deleteBySQL("PRO_DXPJ_ZBJG t", "t.dxpjid = '"+dxpjid+"'");
		List zjList = getZjList(pszbid);
		for(int i =0;i<zbdfGridList.size();i++){
			for (int j = 0; j < zjList.size(); j++) {
				ProDxpjZbjg p = new ProDxpjZbjg();
				Map zbdfMap = (Map)zbdfGridList.get(i);
				Map map = (Map) zjList.get(j);
				p.setZbid(Integer.valueOf(StringUtil.stringConvert(zbdfMap.get("zbid"))));
				p.setQualexpertid(Integer.valueOf(StringUtil.stringConvert(map.get("qualexpertid"))));
				p.setPszbid(Integer.valueOf(pszbid));
				p.setDf(Double.valueOf(StringUtil.stringConvert(zbdfMap.get(String.valueOf(map.get("qualexpertid"))))));
				p.setDxpjid(dxpjid);
				proDxpjZbjgDao.save(p);
			}
		}
	}
	@Override
	public void sendDxpj(String projectid, String dxpjid) throws Exception {
		String sqlString ="update PRO_DXPJ set status=2 where dxpjid="+dxpjid;
		mapDataDao.updateTX(sqlString);
	}
	@Override
	public void revokeDxpj(String projectid, String dxpjid) throws Exception {
		String sqlString ="update PRO_DXPJ set status=3 where dxpjid="+dxpjid;
		mapDataDao.updateTX(sqlString);
	}
}
