package com.wfzcx.aros.xxtx.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.gzgl.dao.BRulebaseinfoDao;
import com.wfzcx.aros.gzgl.po.BRulebaseinfo;
import com.wfzcx.aros.util.GCC;
import com.wfzcx.aros.xxtx.dao.BMsgbaseinfoDao;
import com.wfzcx.aros.xxtx.po.BMsgbaseinfo;

/**
 * @Description: 消息提醒
 * @version V1.0
 */
@Scope("prototype")
@Service("com.wfzcx.aros.xxtx.service.BMsgbaseinfoService")
public class BMsgbaseinfoService {
	
	// 日志
	Logger logger = Logger.getLogger(BMsgbaseinfoService.class);

	@Autowired
	private MapDataDaoI mapDataDao;
	@Autowired
	BMsgbaseinfoDao bMsgbaseinfoDao;
	@Autowired
	BRulebaseinfoDao bRulebaseinfoDao;
	
	
	//.......................................................定时任务service_begin
	/**
	 * 逾期案件写入消息（通过配置规则判断逾期）（定时任务调用）
	 * @param param
	 */
	public void overdueCase(Map<String, Object> param){
		BRulebaseinfo ruleBean = new BRulebaseinfo();
		ruleBean.setStatus(GCC.RULE_STATUS_PUBLISH);
		List<BRulebaseinfo> ruleList = bRulebaseinfoDao.findByExample(ruleBean);
		for (BRulebaseinfo bRulebaseinfo : ruleList) {
			String rluename = bRulebaseinfo.getRulename();
			String tabcode = bRulebaseinfo.getTabcode();
			String fieldcode = bRulebaseinfo.getFieldcode();
			String whereStr = bRulebaseinfo.getWherestr();
			String fre = bRulebaseinfo.getFrequency();
			int limit = bRulebaseinfo.getLimit().intValue();
			String dateComStr = "(ROUND(TO_NUMBER(SYSDATE-to_date(" + fieldcode + ",'yyyy-MM-dd'))) - " + limit + ")"; 
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT t.caseid, t.csaecode, t.appname, t.userid, n.roleid, n.proname, n.nodename, ");
			sql.append(dateComStr);
			sql.append(" AS overdue ");
			if ("B_CASEBASEINFO".equalsIgnoreCase(tabcode)){
				sql.append("FROM B_CASEBASEINFO t , pub_pronodebaseinfo n WHERE 1=1 AND");
			}else {
			    sql.append("FROM B_CASEBASEINFO t, pub_pronodebaseinfo n,");
				sql.append(tabcode);
				sql.append(" u ");
				sql.append("WHERE u.caseid= t.caseid AND ");
			}
			sql.append(dateComStr);
			sql.append(" >= 1");
			sql.append(" AND t.protype = n.protype and t.nodeid=n.nodeid ");
			if(StringUtils.isNotEmpty(whereStr)){
				sql.append(" AND ");
				sql.append(whereStr);
			}
			List<Map<String, Object>> caseList = new ArrayList<Map<String,Object>>();
			try {
				caseList = mapDataDao.queryListBySQL(sql.toString());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("BMsgbaseinfoService.overdueCase() query error rluename=" + rluename + "sql=" + sql.toString());
			}
			for (Map<String, Object> map : caseList) {
				try {
					String caseid = (String) map.get("caseid");
					String casecode = (String) map.get("csaecode");
					BigDecimal roleid = (BigDecimal) map.get("roleid");
					if(StringUtils.isEmpty(casecode)){
						casecode = (String) map.get("appname") + "申请的案件";
					}
					BigDecimal overdue = (BigDecimal) map.get("overdue");
					String proname = (String) map.get("proname");
					String nodename = (String) map.get("nodename");
					String msgcontent = casecode + "-"+ rluename + "-已逾期" + overdue.intValue() + "天！请及时处理。";
					String selectSql = "SELECT id, ifread FROM B_MSGBASEINFO WHERE (ifread is null or ifread <> '1') AND CASEID='" + caseid + "' AND MSGTYPE = '" + GCC.MESSAGETYPE_OVERDUE + "' AND MSGCONTENT LIKE '%-"+ rluename + "-%'";
					List<Map<String, Object>> queryList = mapDataDao.queryListBySQL(selectSql);
					if(CollectionUtils.isEmpty(queryList)){
						BMsgbaseinfo msgBean = new BMsgbaseinfo();
						msgBean.setMsgcontent(msgcontent);
						msgBean.setCaseid(caseid);
						if(null != roleid){
							msgBean.setSender(String.valueOf(roleid.longValue()));
						}
						msgBean.setSendfre(fre);
						msgBean.setMsgtype(GCC.MESSAGETYPE_OVERDUE);
						msgBean.setGnetime(DateUtil.dateToString(Calendar.getInstance().getTime(), DateUtil.DATE_FORMAT));
						bMsgbaseinfoDao.save(msgBean);
					}else{
						Map<String, Object> msgMap = queryList.get(0);
						String updateSql =  "UPDATE B_MSGBASEINFO SET SENDER="+ roleid.intValue() + ", MSGCONTENT='" + msgcontent + "', gnetime='" 
								+ DateUtil.dateToString(Calendar.getInstance().getTime(), DateUtil.DATE_FORMAT) +"' WHERE id='" + msgMap.get("id") + "'"; 
						bMsgbaseinfoDao.updateBySql(updateSql);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("BMsgbaseinfoService.overdueCase() insert error map="+ map.toString());
				}
			}
		}
	}
	
	/**
	 * 流程消息
	 * @param param
	 */
	public void procedureMsg(Map<String, String> param){}
	
	/**
	 * 意见反馈消息
	 * @param param
	 */
	public void feedbackMsg(Map<String, String> param){}
	
	/**
	 * 网上答疑消息
	 * @param param
	 */
	public void networkquestionMsg(Map<String, String> param){}
	
	//.......................................................定时任务service_end
	/**
	 * 登陆页面消息提醒
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> loginPageQueryMsg(Map<String, Object> param) {
		StringBuffer sql = new StringBuffer();
		SysUser user = SecureUtil.getCurrentUser();
		Long userid  = user.getUserid();
		sql.append("SElECT id, caseid, msgcontent, gnetime,(select name FROM SYS_YW_DICENUMITEM t WHERE t.elementcode='MESSAGETYPE' and t.status=0 AND t.code = msgtype) msgtype FROM B_MSGBASEINFO WHERE (ifread is null or ifread <> '1') AND SENDER in ");
		sql.append("(SELECT ROLEID FROM SYS_USER_ROLE WHERE USERID='");
		sql.append(userid);
		sql.append("') ORDER BY CASEID");
		return mapDataDao.queryListBySQL(sql.toString());
	}
	/**
	 * 删除
	 * @param param
	 * @return
	 */
	public void delete(Map<String, Object> param) throws Exception {
		String id = (String) param.get("id");
		bMsgbaseinfoDao.delete(id);
	}

	/**
	 * 设置消息为已读状态
	 * @param param
	 * @return 
	 */
	public void modifyReadStatus(Map<String, Object> param) throws Exception {
		String id = (String) param.get("id");
		StringBuffer updateSql = new StringBuffer();
		updateSql.append("UPDATE B_MSGBASEINFO SET ifread='1', acptime='");
		updateSql.append(DateUtil.dateToString(Calendar.getInstance().getTime(), DateUtil.DATE_FORMAT));
		updateSql.append("' WHERE id='");
		updateSql.append(id);
		updateSql.append("'");
		bMsgbaseinfoDao.updateBySql(updateSql.toString());
	}

	
	
}