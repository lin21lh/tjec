package com.wfzcx.aros.gzgl.service;



import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.wfzcx.aros.gzgl.dao.BRulebaseinfoDao;
import com.wfzcx.aros.gzgl.po.BRulebaseinfo;

@Scope("prototype")
@Service("com.wfzcx.aros.gzgl.service.BRulebaseinfoService")
public class BRulebaseinfoService {
	
	@Autowired
	MapDataDaoI mapDataDao;
	
	@Autowired
	BRulebaseinfoDao bRulebaseinfoDao;
	/**
	 * 分页查询规则信息列表
	 * @param param
	 * @return
	 */
	public PaginationSupport queryBRulebaseinfoList(Map<String, Object> param) {
		String status = (String) param.get("status");
		String ruelname = (String) param.get("selectruelname");
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ruleid,rulename,tabcode,tabname,fieldcode,fieldname,"); 
		sql.append(" (select name FROM SYS_YW_DICENUMITEM t WHERE t.elementcode='RULE_STATUAS' and t.status=0 AND t.code = u.status)statusname, status, frequency,limit,wherestr,operator,opttime FROM B_RULEBASEINFO u WHERE 1=1 ");
		if(StringUtils.isNotEmpty(status)){
			sql.append(" AND STATUS='");
			sql.append(status);
			sql.append("'");
		}
		if(StringUtils.isNotEmpty(ruelname)){
			sql.append(" AND rulename like '%");
			sql.append(ruelname);
			sql.append("%'");
		}
    	return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	/**
	 * 保存新增的规则
	 * @param param
	 */
	public void saveRuleInfo(Map<String, Object> param) throws Exception{
		String ruleid    =  (String)param.get("ruleid");
		String rulename  = (String)param.get("rulename");
		String tabcode   = (String)param.get("tabcode");
		String tabname   = (String)param.get("tabname");
		String fieldcode = (String)param.get("fieldcode");
		String fieldname = (String)param.get("fieldname");
		String status    = (String)param.get("status");
		String frequency = (String)param.get("frequency");
		String limit     = (String)param.get("limit");
		String wherestr  = (String)param.get("wherestr");
		String operator  = SecureUtil.getCurrentUser().getUsername();
		String opttime   = DateUtil.dateToString(Calendar.getInstance().getTime(), DateUtil.DATE_FORMAT);
		
		BRulebaseinfo bean = new BRulebaseinfo();
		bean.setRulename(rulename);
		bean.setFieldcode(fieldcode);
		bean.setTabcode(tabcode);
		bean.setTabname(tabname);
		bean.setFieldname(fieldname);
		bean.setStatus(status);
		bean.setFrequency(frequency);
		bean.setLimit(new BigDecimal(limit));
		bean.setWherestr(wherestr);
		bean.setOperator(operator);
		bean.setOpttime(opttime);
		if(StringUtils.isEmpty(ruleid)){
			bean.setStatus("0");
			bRulebaseinfoDao.save(bean);
		}else{
			bean.setRuleid(ruleid);
			bRulebaseinfoDao.update(bean);
		}
	}

	/**
	 * 删除
	 * @param param
	 */
	public void delete(Map<String, Object> param) throws Exception{
		String ruleid = (String)param.get("ruleid");
		bRulebaseinfoDao.delete(ruleid);
	}

	/**
	 * 发布规则
	 * @param param
	 */
	public void redo(Map<String, Object> param) {
		String ruleid = (String)param.get("ruleid");
		String sql = "update b_rulebaseinfo set status='1' where ruleid='" + ruleid + "'";
		mapDataDao.updateTX(sql);
	}
	
}
