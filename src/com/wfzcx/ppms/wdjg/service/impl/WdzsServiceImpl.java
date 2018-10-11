package com.wfzcx.ppms.wdjg.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;
import com.wfzcx.ppms.discern.dao.ProProjectDao;
import com.wfzcx.ppms.discern.po.ProProject;
import com.wfzcx.ppms.wdjg.dao.ProWdjgDao;
import com.wfzcx.ppms.wdjg.service.WdzsService;

@Scope("prototype")
@Service("com.wfzcx.ppms.wdjg.service.impl.WdzsServiceImpl")
public class WdzsServiceImpl implements WdzsService{

	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProWdjgDao proWdjgDao;
	@Autowired
	ProProjectDao proProjectDao;
	
	@Override
	public PaginationSupport queryProject(Map<String, Object> param)   throws AppException{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select projectid,datatype, pro_name,pro_type,amount, pro_year,");
		sql.append("       pro_trade, pro_perate,pro_return,pro_sendtime,");
		sql.append("       pro_sendtype,pro_sendperson,pro_situation,pro_person,");
		sql.append("       pro_phone,pro_scheme,pro_schemepath,pro_reportpath,");
		sql.append("       pro_conditionpath,pro_article,pro_articlepath,wfid,");
		sql.append("       status,createuser,createtime,updateuser,updatetime,IMPLEMENT_ORGAN,IMPLEMENT_PERSON,IMPLEMENT_PHONE,GOVERNMENT_PATH,cz_result,vfm_pjhj,opinion,");
		sql.append("       (select a.name from SYS_DEPT a where a.status=0  and a.code=t.implement_organ) implement_organ_name,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='APPROVERESULT' and a.status=0  and a.code=t.cz_result) cz_result_name,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROVFMPJHJ' and a.status=0  and a.code=t.VFM_PJHJ) VFM_PJHJ_NAME,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROTYPE' and a.status=0  and a.code=t.pro_type) pro_type_name,");
		sql.append("       (select a.name from SYS_YW_DICCODEITEM a where upper(a.elementcode)='PROTRADE' and a.status=0  and a.code=t.pro_trade) pro_trade_name,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROOPERATE' and a.status=0  and a.code=t.pro_perate) pro_perate_name,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PRORETURN' and a.status=0  and a.code=t.pro_return) pro_return_name,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSENDTYPE' and a.status=0  and a.code=t.pro_sendtype) pro_sendtype_name,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSFXM' and a.status=0  and a.code=t.sfxm) sfxm_name,sfxm,");
		sql.append("(select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.tjxm) tjxm_name,tjxm,");
		sql.append("(select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.sqbt) sqbt_name,sqbt,");
		sql.append(" btje,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='WFSTATUS' and a.status=0  and a.code=t.status) status_name,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROXMDQHJ' and a.status=0  and a.code=t.xmdqhj) xmdqhj_name,");
		sql.append("       (select a.username from sys_user a where  a.userid=t.createuser) createusername,");
		sql.append("       (select a.username from sys_user a where  a.userid=t.updateuser) updateusername ");
		sql.append("  from pro_project t where 1=1 ");
		
		String xmdqhj = StringUtil.stringConvert(param.get("xmdqhj"));
		if(!"".equals(xmdqhj)){
			sql.append(" and t.xmdqhj in ('").append(xmdqhj.replaceAll(",", "','")).append("')");
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
		
		sql.append("  order by t.xmdqhj,t.projectid desc");

		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	public List queryWdzs(String projectid){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.wdjgid,t.xmhj,t.hjfl,t.wdmc,t.glbm,t.glzd,t.bz,t.plsx")
		.append(",(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROXMDQHJ' and a.status=0  and a.code=t.xmhj) xmhj_name")
		.append(",(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROXMHJFL' and a.status=0  and a.code=t.hjfl) hjfl_name")
		.append(" from pro_wdjg t where 1=1")
		.append(" ")
		.append(" order by t.xmhj,t.hjfl,t.plsx ");
		
		List list = proWdjgDao.findMapBySql(sql.toString());
		for(int i=0;i<list.size();i++){
			Map map = (Map)list.get(i);
			String glbm = (String)map.get("glbm");
			String glzd = (String)map.get("glzd");
			String sql_item = "select g." + glzd + " from " + glbm + " g where 1=1 ";
			String bz = (String)map.get("bz");
			if (bz == null || "".equals(bz)){
				sql_item += " and g.projectid=" + projectid;
			} else {
				/*g.pro_name=(select a.pro_name from pro_project a where a.projectid=## )*/
				bz = bz.replace("##", projectid);
				sql_item += " and " + bz;
			}
			List list_item = proWdjgDao.findMapBySql(sql_item);
			if (list_item.size() > 0){
				String itemid = (String)(((Map)list_item.get(0)).get(glzd));
				if (itemid == null || "".equals(itemid)){
					map.put("itemid", "");
				} else {
					map.put("itemid", itemid);
				}
			} else {
				map.put("itemid", "");
			}
		}
		return list;
	}

	public ProProject getProject(String projectid){
		
		return proProjectDao.get(Integer.valueOf(projectid));
	}
}
