package com.wfzcx.aros.flow.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.flow.po.Probaseinfo;

/**
 * @ClassName: ProbaseinfoDao
 * @Description: 流程过程表数据持久化操作类
 * @author ybb
 * @date 2016年8月11日 下午3:27:02
 * @version V1.0
 */
@Scope("prototype")
@Repository
public class ProbaseinfoDao extends GenericDao<Probaseinfo, String>{

}
