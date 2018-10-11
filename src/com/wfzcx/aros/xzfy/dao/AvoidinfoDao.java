package com.wfzcx.aros.xzfy.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.xzfy.po.Avoidinfo;

/**
 * @ClassName: AvoidinfoDao
 * @Description: 案件回避信息数据库持久化操作类
 * @author ybb
 * @date 2016年8月29日 上午9:50:56
 * @version V1.0
 */
@Scope("prototype")
@Repository
public class AvoidinfoDao extends GenericDao<Avoidinfo, String>{

}
