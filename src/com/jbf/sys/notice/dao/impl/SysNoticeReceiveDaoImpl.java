package com.jbf.sys.notice.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import com.jbf.common.dao.impl.GenericDao;
import com.jbf.sys.notice.dao.ISysNoticeReceiveDao;
import com.jbf.sys.notice.po.SysNoticeReceive;

/**
 * 留言公告接收人实现类
 * @ClassName: SysNoticeManageDaoImpl 
 * @Description: (留言公告接收人实现类) 
 * @author songxiaojie
 * @date 2015年5月11日 
 */
@Scope("prototype")
@Repository
public class SysNoticeReceiveDaoImpl extends GenericDao<SysNoticeReceive,Long> implements ISysNoticeReceiveDao 
{

	
}
