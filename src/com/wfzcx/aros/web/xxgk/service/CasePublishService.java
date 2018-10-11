/**
 * @Description: 信息公开service
 * @author 张田田
 * @date 2016-09-8 
 */
package com.wfzcx.aros.web.xxgk.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.util.StringUtil;

@Service("com.wfzcx.aros.web.xxgk.service.CasePublishService")
public class CasePublishService
{
	@Autowired
	private MapDataDaoI mapDataDao;
	
	public PaginationSupport query(Map<String, Object> param)
	{
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.caseid, ");
		sql.append(" t.appname || '对' || t.defname || '的行政复议-' ||");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'DOCTYPE' and a.status = 0 and a.code = b.doctype) title,");
		sql.append(" b.doctype,  b.opttime, b.noticeid");
		sql.append(" from B_CASEBASEINFO t, b_noticebaseinfo b");
		sql.append(" where t.caseid = b.caseid");
		
		// 申请人
		String appname = (String)param.get("appname");
		if (StringUtils.isNotBlank(appname))
		{
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		// 被申请人
		String defname = (String)param.get("defname");
		if (StringUtils.isNotBlank(defname))
		{
			sql.append(" and t.defname like '%").append(defname).append("%'");
		}
		
		// 通知书类型
		String doctype = (String)param.get("doctype");
		if (StringUtils.isNotBlank(doctype))
		{
			sql.append(" and b.doctype = '").append(doctype).append("'");
		}
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
}
