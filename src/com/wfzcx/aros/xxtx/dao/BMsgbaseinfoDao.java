package com.wfzcx.aros.xxtx.dao;



import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.xxtx.po.BMsgbaseinfo;

/**
 * @Description: 消息提醒数据持久化操作类
 * @version V1.0
 */
@Scope("prototype")
@Repository
public class BMsgbaseinfoDao extends GenericDao<BMsgbaseinfo, String>{

}
