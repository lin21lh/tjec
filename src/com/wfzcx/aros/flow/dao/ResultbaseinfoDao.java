package com.wfzcx.aros.flow.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.flow.po.Resultbaseinfo;

/**
 * @ClassName: ResultbaseinfoDao
 * @Description: 流程结果表数据持久化操作类
 * @author ybb
 * @date 2016年8月17日 下午5:52:26
 * @version V1.0
 */
@Scope("prototype")
@Repository
public class ResultbaseinfoDao extends GenericDao<Resultbaseinfo, String>{

}
