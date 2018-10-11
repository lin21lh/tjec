package com.jbf.sys.notice.dao;

import java.io.IOException;
import java.sql.SQLException;

import com.jbf.common.dao.IGenericDao;
import com.jbf.sys.notice.po.SysNotice;



/**
 * 留言公告管理dao层接口类
 * @ClassName: ISysNoticeManageDao 
 * @Description: (留言公告管理dao层接口类) 
 * @author songxiaojie
 * @date 2015年5月8日 
 */
public interface ISysNoticeDao extends IGenericDao<SysNotice, Long>{

	/**
	 * 获取公告留言的clob内容，转成string字符串
	 * @param noticeId
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public String getContentByNoticeId(Long noticeId)throws SQLException, IOException;
	
}
