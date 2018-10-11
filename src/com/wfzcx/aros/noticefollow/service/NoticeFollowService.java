/**
 * @Description: 通知书跟踪service
 * @author 张田田
 * @date 2016-08-26 
 */
package com.wfzcx.aros.noticefollow.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.util.StringUtil;
import com.wfzcx.aros.noticefollow.dao.NoticeFollowDao;
import com.wfzcx.aros.noticefollow.po.Noticemanagebaseinfo;

@Scope("prototype")
@Service("com.wfzcx.aros.noticefollow.service.NoticeFollowService")
public class NoticeFollowService
{
	@Autowired
	private NoticeFollowDao noticeFollowDao;
	@Autowired
	private MapDataDaoI mapDataDao;
	
	public PaginationSupport queryNotice(Map<String, Object> param)
	{
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select c.intro, c.noticeid, c.doctype, c.doctypename, d.id, d.orgperson, d.deliverydate from")
			.append(" (select t.intro, b.noticeid, b.doctype, ")
			.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='DOCTYPE' and a.status=0 and a.code = b.doctype) doctypename ")
			.append(" from B_CASEBASEINFO t, B_NOTICEBASEINFO b where t.caseid = b.caseid) c, b_noticemanagebaseinfo d")
			.append(" where d.noticeid (+)= c.noticeid");
		
		// 通知书类型
		String doctype = StringUtil.stringConvert(param.get("doctype"));
		if (StringUtils.isNotBlank(doctype))
		{
			sql.append(" and c.doctype = '").append(doctype).append("'");
		}
		
		// 案件描述
		String intro = StringUtil.stringConvert(param.get("intro"));
		if (StringUtils.isNotBlank(intro))
		{
			sql.append(" and c.intro like '%").append(intro).append("%'");
		}
		
		// 受送达人
		String orgperson = StringUtil.stringConvert(param.get("orgperson"));
		if (StringUtils.isNotBlank(orgperson))
		{
			sql.append(" and d.orgperson like '%").append(orgperson).append("%'");
		}
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String save(Noticemanagebaseinfo info)
	{
		return (String)noticeFollowDao.save(info);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void update(Noticemanagebaseinfo info)
	{
		noticeFollowDao.update(info);
	}
	
	public Noticemanagebaseinfo queryNoticeInfo(String id)
	{
		return noticeFollowDao.get(id);
	}
}
