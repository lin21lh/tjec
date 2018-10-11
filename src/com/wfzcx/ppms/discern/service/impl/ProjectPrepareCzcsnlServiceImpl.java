package com.wfzcx.ppms.discern.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.ppms.discern.dao.ProCzcsnlDao;
import com.wfzcx.ppms.discern.po.ProCzcsnl;
import com.wfzcx.ppms.discern.service.ProjectPrepareCzcsnlService;

@Scope("prototype")
@Service("com.wfzcx.ppms.discern.service.impl.ProjectPrepareCzcsnlServiceImpl")
public class ProjectPrepareCzcsnlServiceImpl implements ProjectPrepareCzcsnlService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProCzcsnlDao proCzcsnlDao;
	public PaginationSupport queryProject(Map<String, Object> param)throws AppException {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select t.projectid,t.datatype, pro_name,pro_type,amount, pro_year,");
		sql.append("       pro_trade, pro_perate,pro_return,pro_sendtime,");
		sql.append("       pro_sendtype,pro_sendperson,pro_situation,pro_person,");
		sql.append("       pro_phone,pro_scheme,pro_schemepath,pro_reportpath,");
		sql.append("       pro_conditionpath,pro_article,pro_articlepath,wfid,t1.pszbid,");
		sql.append("       t1.status,t4.createuser,t4.createtime,t4.updateuser,t4.updatetime,IMPLEMENT_ORGAN,IMPLEMENT_PERSON,IMPLEMENT_PHONE,GOVERNMENT_PATH,cz_result,vfm_pjhj,opinion,");
		sql.append("       (select a.name from SYS_DEPT a where a.status=0  and a.code=t.implement_organ) implement_organ_name,");
		sql.append(" case when t2.vfm_Dlpj is null then t1.vfm_Dlpj else t2.vfm_Dlpj end vfm_Dlpj,");
		sql.append("  case when t4.status is null then '未录入' else (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROPJZBSTATUS' and a.status=0  and a.code=t4.status) end cz_result_name,");
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
		sql.append("       (select a.username from sys_user a where  a.userid=t4.createuser) createusername,");
		sql.append("       (select a.username from sys_user a where  a.userid=t4.updateuser) updateusername, ");
		sql.append(" t2.dxpjid,t2.qual_verifytime,t2.qual_conclusion,t2.qual_df,t2.qual_result,t2.qual_path,t3.dlpjid,t4.czcsnlid ");
		sql.append("  from pro_project t,pro_pszb t1,PRO_DXPJ t2 ,PRO_DLPJ T3,PRO_CZCSNL t4");
		sql.append("  where t.projectid=t1.projectid and t.projectid=t2.projectid  and t2.dxpjid=t4.dxpjid(+) and t.cz_result in('10','20') ");
		sql.append("  and t1.xmhj =t2.xmhj  and t2.dxpjid=t3.dxpjid(+)  ");
		sql.append("  and t2.status = '2' ");//VFM定性分析已经提交
		sql.append("    and ((t2.vfm_dlpj ='1' and t3.status='2' and  t2.dxpjid = t3.dxpjid  )");
		sql.append("  		    or  (t2.vfm_dlpj='0'   )) ");
		String xmhj = StringUtil.stringConvert(param.get("xmhj"));//项目环节
		if(!"".equals(xmhj)){
			sql.append(" and t1.xmhj = '").append(xmhj).append("'");
			sql.append(" and t4.xmhj(+) = '").append(xmhj).append("'");
		}
		String status = StringUtil.stringConvert(param.get("status"));//状态
		if("1".equals(status)){//待处理
			sql.append(" and (t4.status in ('1','3') or t4.status is null)");
		}else if("2".equals(status)){//已提交
			sql.append(" and t4.status = '2'");
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
	@Override
	public List queryIsExistCzcsnl(String dxpjid, String xmhj, String projectid)throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select projectid from PRO_CZCSNL where projectid= ").append(projectid).append(" and xmhj='").append(xmhj).append("' ");
		return mapDataDao.queryListBySQL(sql.toString());
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor={Exception.class})
	public void czcsnlSave(Map<String, Object> param) throws Exception {
		/**
		 * 1、保存主表
		 * 2、保存财政预算信息
		 */
		String updateFlag = StringUtil.stringConvert(param.get("updateFlag"));//修改标志
		if("".equals(updateFlag)){//新增
			Integer czcsnlid = czcsnlZbSave(param);
			saveFinanceData(param, czcsnlid,"1");
		}else if("1".equals(updateFlag)){//修改
			String czcsnlid = StringUtil.stringConvert(param.get("czcsnlid"));//
			czcsnlZbUpdate(param, Integer.valueOf(czcsnlid));
			saveFinanceData(param, Integer.valueOf(czcsnlid),"1");//1预算支出
		}
	}
	/**
	 * 财政承受能力主表保存
	 * @Title: czcsnlZbSave 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @return
	 * @throws Exception 设定文件
	 */
	public Integer czcsnlZbSave(Map<String, Object> param)throws Exception{
		ProCzcsnl pc =  new ProCzcsnl();
		BeanUtils.populate(pc, param);
		SysUser user = SecureUtil.getCurrentUser();
		pc.setStatus(StringUtil.stringConvert(param.get("status")));
		pc.setCreateuser(user.getUserid().toString());//创建人
		pc.setCreatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//创建时间
		return (Integer) proCzcsnlDao.save(pc);
	}
	/**
	 * 财政承受能力主表修改
	 * @Title: czcsnlZbUpdate 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @param dlpjid
	 * @throws Exception 设定文件
	 */
	public void czcsnlZbUpdate(Map<String, Object> param,Integer dlpjid)throws Exception{
		ProCzcsnl pd = proCzcsnlDao.get(dlpjid);
		BeanUtils.populate(pd, param);
		SysUser user = SecureUtil.getCurrentUser();
		pd.setUpdateuser(user.getUserid().toString());//修改人
		pd.setUpdatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));//修改时间
		proCzcsnlDao.update(pd);
	}
	/**
	 * 保存财政预算支出数据
	 * @Title: saveFinanceData 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @param czcsnlid
	 * @param budgetType 设定文件
	 */
	public void saveFinanceData(Map<String, Object> param,Integer czcsnlid ,String budgetType){
		String financeGridList =StringUtil.stringConvert(param.get("financeGridData"));
		List list  = JSONArray.parseArray(financeGridList);
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			map.remove("budgetid");
			map.put("projectid", param.get("projectid"));//放入projectid
			map.put("budget_type", budgetType);//放入支出类型 1：预算支出，2 ：实际支出
			map.put("xmhj", param.get("xmhj"));//项目环节
		}
		String where = "budget_type=1 and projectid = "+param.get("projectid");
		//先删除原先数据
		proCzcsnlDao.deleteBySQL("PRO_BUDGET", where);
		//再保存页面数据
		proCzcsnlDao.addBatchByList(list, "PRO_BUDGET");
	}
	@Override
	public List queryCzcsnlForm(String czcsnlid) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select t.*,(select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.fc_result)  FC_RESULT_name from PRO_CZCSNL t ");
		sql.append(" where czcsnlid = ").append(czcsnlid);
		return 	mapDataDao.queryListBySQLForConvert(sql.toString());
	}
	@Override
	public PaginationSupport financeQuery(Map map) throws AppException {
		String projectid = map.get("projectid")==null?"":map.get("projectid").toString();
		String budgetType = map.get("budgetType")==null?"":map.get("budgetType").toString();
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select * from PRO_BUDGET t where t.projectid=").append(projectid);
		sql.append("and t.budget_type='").append(budgetType).append("' ");
		return mapDataDao.queryPageBySQL(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public void sendCzcsnl(String projectid, String czcsnlid) throws Exception {
		String sqlString ="update PRO_CZCSNL set status=2 where czcsnlid="+czcsnlid;
		mapDataDao.updateTX(sqlString);
	}
	@Override
	public void revokeCzcsnl(String projectid, String czcsnlid)throws Exception {
		String sqlString ="update PRO_CZCSNL set status=3 where czcsnlid="+czcsnlid;
		mapDataDao.updateTX(sqlString);
	}
}
