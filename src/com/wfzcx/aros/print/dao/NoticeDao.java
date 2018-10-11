/**
 * @Description: 通知书打印dao
 * @author 张田田
 * @date 2016-08-26 
 */
package com.wfzcx.aros.print.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.print.po.Noticebaseinfo;

@Scope("prototype")
@Repository
public class NoticeDao extends GenericDao<Noticebaseinfo, String>
{
}
