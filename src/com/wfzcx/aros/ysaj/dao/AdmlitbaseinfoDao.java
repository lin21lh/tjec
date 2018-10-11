package com.wfzcx.aros.ysaj.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.ysaj.po.Admlitbaseinfo;

/**
 * @ClassName: AdmlitbaseinfoDao
 * @Description: 应诉案件基本信息数据持久化操作类
 * @author ybb
 * @date 2016年9月22日 下午1:56:22
 * @version V1.0
 */
@Scope("prototype")
@Repository
public class AdmlitbaseinfoDao extends GenericDao<Admlitbaseinfo, String>{

}
