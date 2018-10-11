package com.wfzcx.aros.flow.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.flow.po.Pronodebaseinfo;

/**
 * @ClassName: PronodebaseinfoDao
 * @Description: 流程节点配置信息数据持久化操作类
 * @author ybb
 * @date 2016年8月11日 下午3:23:10
 * @version V1.0
 */
@Scope("prototype")
@Repository
public class PronodebaseinfoDao extends GenericDao<Pronodebaseinfo, String>{

}
