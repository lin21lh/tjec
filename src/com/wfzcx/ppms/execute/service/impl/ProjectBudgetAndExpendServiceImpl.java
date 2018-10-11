package com.wfzcx.ppms.execute.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.dao.util.EasyUISumRowUtil;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.ppms.discern.dao.ProProjectDao;
import com.wfzcx.ppms.execute.service.ProjectBudgetAndExpendService;

/**
 * 项目执行预算和支出service实现类
 * @ClassName: ProjectBudgetAndExpendServiceImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年9月12日 上午8:22:07
 */
@Scope("prototype")
@Service("com.wfzcx.ppms.execute.service.impl.ProjectBudgetAndExpendServiceImpl")
public class ProjectBudgetAndExpendServiceImpl implements ProjectBudgetAndExpendService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProProjectDao proProjectDao;
	@Autowired
	EasyUISumRowUtil easyUISumRowUtil;
	@Override
	public PaginationSupport queryProject(Map<String, Object> param)   throws AppException{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select t.projectid, t.pro_name,t.pro_type,t.amount, t.pro_year,t.xmdqhj,");
		sql.append("           t.pro_trade, t.pro_perate,t.pro_return,t.pro_sendtime,");
		sql.append("           t.pro_sendtype,t.pro_sendperson,t.pro_situation,t.pro_person,");
		sql.append("           t.pro_phone,t.pro_scheme,t.pro_schemepath,t.pro_reportpath,");
		sql.append("           t.pro_conditionpath,t.pro_article,t.pro_articlepath,");
		sql.append("           t.createuser,t.createtime,t.updateuser,t.updatetime,");
		sql.append("           (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROTYPE' and a.status=0  and a.code=t.pro_type) pro_type_name,");
		sql.append("           (select a.name from SYS_YW_DICCODEITEM a where upper(a.elementcode)='PROTRADE' and a.status=0  and a.code=t.pro_trade) pro_trade_name,");
		sql.append("           (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROOPERATE' and a.status=0  and a.code=t.pro_perate) pro_perate_name,");
		sql.append("     (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PRORETURN' and a.status=0  and a.code=t.pro_return) pro_return_name,");
		sql.append("    (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROSENDTYPE' and a.status=0  and a.code=t.pro_sendtype) pro_sendtype_name,");
		sql.append("           (select a.username from sys_user a where  a.userid=t.createuser) createusername,");
		sql.append("           (select a.username from sys_user a where  a.userid=t.updateuser) updateusername");
		sql.append("      from PRO_PROJECT t, PRO_PURCHASE_RESULT t1");
		sql.append("     where t.projectid = t1.projectid  and t1.status='10'");

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
		SysUser user = SecureUtil.getCurrentUser();
		String orgcode = user.getOrgcode();
		//匹配组织机构和创建人
		sql.append(" and t1.orgcode = '").append(orgcode).append("' ");
		sql.append(" and t1.createuser = ").append(user.getUserid());
		sql.append("  order by t.projectid desc");

		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	public PaginationSupport budgetQueryUrl(String projectid, String budgetType) {
		StringBuffer sql = new StringBuffer();
		if("".equals(projectid)){
			sql.append("select * from PRO_BUDGET where 1<>1");
		}else{
			sql.append("select * from PRO_BUDGET where BUDGET_type =");
			sql.append(budgetType);
			sql.append(" and projectid= ");
			sql.append(projectid);
		}
		PaginationSupport p = mapDataDao.queryPageBySQL(sql.toString(), 1, 10000);
		List<Map<String,String>> sumList = new ArrayList<Map<String,String>>();
		Map<String,String> sumMap = new HashMap<String,String>();
		/*sumMap.put("financeName", "本页小计");
		sumMap.put("amount", "AMOUNT");
		sumList.add(sumMap);
		sumMap = new HashMap<String,String>();*/
		sumMap.put("budget_year", "合计");
		sumMap.put("budget_gqtz", "budget_gqtz");
		sumMap.put("budget_yybt", "budget_yybt");
		sumMap.put("budget_fxcd", "budget_fxcd");
		sumMap.put("budget_pttr", "budget_pttr");
		sumMap.put("total", "total");
		sumList.add(sumMap);
		List reList = easyUISumRowUtil.getAllPageSumRow(sumMap,sql.toString());
		p.setSumFooter(reList);
		return p;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void budgetSave(Map<String, Object> param) throws Exception {
		String budgetGrid = StringUtil.stringConvert(param.get("budgetGrid"));
		String budgetType = StringUtil.stringConvert(param.get("budgetType"));
		String where = "projectid = "+param.get("projectid")+" and budget_type='"+budgetType+"' ";
		//先删除原先数据
		proProjectDao.deleteBySQL("PRO_BUDGET", where);
		if(!"".equals(budgetGrid)){
			List list  = JSONArray.parseArray(budgetGrid);
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				map.remove("budgetid");
				map.put("projectid", param.get("projectid"));//放入projectid
				map.put("budget_type", param.get("budgetType"));//放入budget_type
			}
			proProjectDao.addBatchByList(list, "PRO_BUDGET");
		}
	}
	
}
