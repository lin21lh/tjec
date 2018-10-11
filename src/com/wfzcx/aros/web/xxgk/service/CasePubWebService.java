package com.wfzcx.aros.web.xxgk.service;

import java.sql.Clob;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.util.StringUtil;
import com.wfzcx.aros.util.UtilTool;

@Scope("prototype")
@Service("com.wfzcx.aros.web.xxgk.service.CasePubWebService")
public class CasePubWebService {
	
	@Autowired
	MapDataDaoI mapDataDao;
	
	/**
	 * 信息公开list
	 * @param param
	 * @return
	 */
	public PaginationSupport queryList(Map<String, Object> param)
	{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.conid, t.title, t.opttime, t.operator")
			.append(" from b_contentbaseinfo t")
			.append(" where t.status = '02'")
			.append(" and t.contype = '06'");// contype 01:新闻,02：法律法规,03：典型案例,04：委员会审议制度,05：委员会工作守则,06:信息公开
		
		// 发布日期
		//开始时间
		String starttime = StringUtil.stringConvert(param.get("starttime"));
		if (StringUtil.isNotBlank(starttime))
		{
			sql.append(" and substr(t.opttime,0,10) >= '").append(starttime).append("'");
		}
		
		//结束时间
		String endtime = StringUtil.stringConvert(param.get("endtime"));
		if (StringUtil.isNotBlank(endtime))
		{
			sql.append(" and substr(t.opttime,0,10) <= '").append(endtime).append("'");
		}
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	/**
	 * 获取clob字段内容
	 */
	@SuppressWarnings("unchecked")
	public String getClobContentVal(String conid)
	{
		// 查询通知书内容
		StringBuffer sql = new StringBuffer();
		sql.append("select content from b_contentbaseinfo where conid='").append(conid).append("'");
		
		String xml = "";
		List<Map<String, Object>> list = mapDataDao.queryListBySQL(sql.toString());
		if (null != list && !CollectionUtils.isEmpty(list))
		{
			xml = UtilTool.clobToString((Clob)list.get(0).get("content"));
		}
		return xml;
	}
}
