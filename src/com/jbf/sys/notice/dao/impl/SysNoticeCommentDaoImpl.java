package com.jbf.sys.notice.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import com.jbf.common.dao.impl.GenericDao;
import com.jbf.sys.notice.dao.ISysNoticeCommentDao;
import com.jbf.sys.notice.po.SysNoticeComment;

/**
 * 留言公告评论实现类
 * @ClassName: SysNoticeManageDaoImpl 
 * @Description: (留言公告评论实现类) 
 * @author songxiaojie
 * @date 2015年5月11日 
 */
@Scope("prototype")
@Repository
public class SysNoticeCommentDaoImpl  extends GenericDao<SysNoticeComment, Long> implements ISysNoticeCommentDao {

}
