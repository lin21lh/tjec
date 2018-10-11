package com.wfzcx.aros.web.dxal.service;

import java.sql.Clob;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.util.StringUtil;
import com.wfzcx.aros.util.UtilTool;

@Scope("prototype")
@Service("com.wfzcx.aros.web.dxal.service.DxalWebService")
public class DxalWebService
{
	@Autowired
	MapDataDaoI mapDataDao;
	
	/**
	 * 查询典型案例list
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
			.append(" and t.contype = '03'");// contype 01:新闻，02：法律法规，03：典型案例，04：委员会审议制度， 05：委员会工作守则
		
		// 标题
		String title = (String)param.get("title");
		if (StringUtils.isNotBlank(title))
		{
			sql.append(" and t.title like '%").append(title).append("%'");
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
