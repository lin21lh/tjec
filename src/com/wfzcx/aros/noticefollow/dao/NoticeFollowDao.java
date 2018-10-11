/**
 * @Description: 通知书跟踪dao
 * @author 张田田
 * @date 2016-08-26 
 */
package com.wfzcx.aros.noticefollow.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.noticefollow.po.Noticemanagebaseinfo;

@Scope("prototype")
@Repository
public class NoticeFollowDao extends GenericDao<Noticemanagebaseinfo, String> {

}
