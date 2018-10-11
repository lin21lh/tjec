package com.wfzcx.ppms.prepare.service.impl;

import java.lang.reflect.InvocationTargetException;
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
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.ppms.prepare.dao.ProDevelopDaoI;
import com.wfzcx.ppms.prepare.po.ProDevelop;
import com.wfzcx.ppms.prepare.po.ProSolution;
import com.wfzcx.ppms.prepare.service.DevelopmentPlanServiceI;

@Scope("prototype")
@Service("/prepare/service/impl/DevelopmentPlanServiceImpl")
public class DevelopmentPlanServiceImpl implements DevelopmentPlanServiceI{
	
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProDevelopDaoI proDevelopDao;
	
	/**
	 * 查询项目计划
	 * @Title: qryDevPlan 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param param
	 * @param @return 设定文件 
	 * @return PaginationSupport 返回类型 
	 * @throws
	 */
	public PaginationSupport qryDevPlan(Map<String, Object> param){
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		String dealStatus = param.get("dealStatus")==null?"" :param.get("dealStatus").toString();
		String proName = param.get("proName")==null?"" :param.get("proName").toString();
		String proTrade = param.get("proTrade")==null?"" :param.get("proTrade").toString();
		String proPerate = param.get("proPerate")==null?"" :param.get("proPerate").toString();
		String proReturn = param.get("proReturn")==null?"" :param.get("proReturn").toString();
		String proSendtype = param.get("proSendtype")==null?"" :param.get("proSendtype").toString();
		String proType = param.get("proType")==null?"" :param.get("proType").toString();
		String proPerson = param.get("proPerson")==null?"" :param.get("proPerson").toString();
		
		StringBuffer sql = new StringBuffer();
		SysUser user = SecureUtil.getCurrentUser();
		sql.append("select t.projectid,");
		sql.append("       t.pro_name,");
		sql.append("       t.pro_type,");
		sql.append("       f.deveid,");
		sql.append("       f.deve_year,");
		sql.append("       f.implement_organ,");
		sql.append("       f.implement_phone,");
		sql.append("       f.implement_person,");
		sql.append("       f.purchase_type,");
		sql.append("       f.remark,");
		sql.append("       f.createuser,");
		sql.append("       (select a.username from sys_user a where  a.userid=f.createuser) createusername,");
		sql.append("       (select a.username from sys_user a where  a.userid=f.updateuser) updateusername,");
		sql.append("       f.createtime,");
		sql.append("       f.updateuser,");
		sql.append("       f.updatetime,");
		sql.append("       t.pro_year,");
		sql.append("       t.amount,");
		sql.append("       t.pro_trade,");
		sql.append("       t.pro_perate,");
		sql.append("       t.pro_return,");
		sql.append("       t.pro_sendtime,");
		sql.append("       t.pro_sendtype,");
		sql.append("       t.pro_sendperson,");
		sql.append("       t.pro_situation,");
		sql.append("       t.pro_person,");
		sql.append("       t.pro_phone,");
		sql.append("       t.pro_scheme,");
		sql.append("       f.dev_path,");
		sql.append("       (select a.name from SYS_DEPT a where a.status=0  and a.code=f.implement_organ) implement_organ_name,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROTYPE' and a.status=0  and a.code=t.pro_type) pro_type_name,");
		sql.append("       (select a.name from SYS_YW_DICCODEITEM a where upper(a.elementcode)='PROTRADE' and a.status=0  and a.code=t.pro_trade) pro_trade_name,");
		sql.append("       (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROOPERATE' and a.status=0  and a.code=t.pro_perate) pro_perate_name,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PRORETURN' and a.status=0  and a.code=t.pro_return) pro_return_name,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PURCHASE' and a.status=0  and a.code=f.purchase_type) purchase_type_name,");
		sql.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSENDTYPE' and a.status=0  and a.code=t.pro_sendtype) pro_sendtype_name");
		sql.append("  FROM PRO_PROJECT T,PRO_DEVELOP f");
		sql.append("    WHERE   T.STATUS='10' and t.projectid=f.projectid(+) ");
		sql.append("    AND t.CZ_RESULT IN ('10','20') ");
		sql.append("  and ((t.vfm_pjhj =1 and exists (select 1 from pro_czcsnl l where t.projectid=l.projectid and l.xmhj=1 and l.status=2))or t.vfm_pjhj=2) ");
		sql.append("  and not exists (select * from pro_czcsnl c where c.projectid=t.projectid and c.fc_result='0' and c.status='2')");
		if(!"".equals(dealStatus)){
			if("1".equals(dealStatus)){
				sql.append("  and not exists (select 1 from pro_develop h  where h.projectid = t.projectid  )");
			}else if("2".equals(dealStatus)){
				sql.append("  and exists (select 1 from pro_develop h  where h.projectid = t.projectid  ) ");
			}
		}
		if(!"".equals(proName)){
			sql.append(" and t.pro_name like '%").append(proName.trim()).append("%'");
		}
		if(!"".equals(proPerson)){
			sql.append(" and t.pro_person like '%").append(proPerson.trim()).append("%'");
		}
		if(!"".equals(proTrade)){
			sql.append(" and t.pro_trade in ('").append(proTrade.replaceAll(",", "','")).append("')");
		}
		if(!"".equals(proPerate)){
			sql.append(" and t.pro_perate in ('").append(proPerate.replaceAll(",", "','")).append("')");
		}
		if(!"".equals(proReturn)){
			sql.append(" and t.pro_return in ('").append(proReturn.replaceAll(",", "','")).append("')");
		}
		if(!"".equals(proSendtype)){
			sql.append(" and t.pro_sendtype in ('").append(proSendtype.replaceAll(",", "','")).append("')");
		}
		if(!"".equals(proType)){
			sql.append(" and t.pro_type in ('").append(proType.replaceAll(",", "','")).append("')");
		}
		sql.append("  order by t.PROJECTID desc ");
		System.out.println("【开发计划打印sql】"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveDevPlan(Map<String, Object> map) throws AppException, IllegalAccessException, InvocationTargetException {
		// TODO Auto-generated method stub
		String optFlag = map.get("optFlag")==null?"":map.get("optFlag").toString();
		String projectid = map.get("projectid")==null?"":map.get("projectid").toString();
		String deveid = map.get("deveid")==null?"":map.get("deveid").toString();
		ProDevelop dev = new ProDevelop();
		BeanUtils.populate(dev, map);
		SysUser user = SecureUtil.getCurrentUser();
		if("add".equals(optFlag)){
			dev.setCreateuser(user.getUserid().toString());
			dev.setCreatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			proDevelopDao.save(dev);
		}else if("edit".equals(optFlag)){
			if("".equals(deveid)){
				throw new AppException("信息主键没找到！");
			}
			dev.setUpdateuser(user.getUserid().toString());
			dev.setUpdatetime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			proDevelopDao.update(dev);
		}
		//add by 辛鹏 2016年3月22日19:45:26
		//增加修改项目环节
		String sql = "update PRO_PROJECT set xmdqhj ='2' where projectid="+projectid;
		proDevelopDao.updateBySql(sql);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delDevPlan(Map<String, Object> map) throws AppException {
		// TODO Auto-generated method stub
		String deveids = map.get("deveids")==null?"":map.get("deveids").toString();
		
		if("".equals(deveids)){
			throw new AppException("信息主键没找到！");
		}
		String[] devs = deveids.split(","); 
		for(int i=0;i<devs.length;i++){
			int id = Integer.parseInt(devs[i]);
			proDevelopDao.delete(id);
		}
	}

	@Override
	public boolean qryImlplanIsAudit(Map<String, Object> map) throws AppException {
		String deveid = map.get("deveid")==null?"":map.get("deveid").toString();
		boolean flag = true;
		if("".equals(deveid)){
			flag = false;
		}else{
			ProDevelop pd = proDevelopDao.get(Integer.parseInt(deveid));
			String sql = "select * from PRO_SOLUTION t where t.projectid = '"+pd.getProjectid()+"'";
			List<ProSolution> pros = (List<ProSolution>)proDevelopDao.findVoBySql(sql, ProSolution.class);
			if(pros.size()>1){
				throw new AppException("一个项目计划查询出多条实施方案！");
			}else{
				if(pros.size()!=0){
					String status = pros.get(0).getStatus();
					if(!"10".equals(status)){
						flag = false;
					}
				}else{
					flag = false;
				}
			}
		}
		return flag;
	}
}
