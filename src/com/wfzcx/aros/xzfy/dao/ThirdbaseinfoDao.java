package com.wfzcx.aros.xzfy.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.xzfy.po.Thirdbaseinfo;

/**
 * @ClassName: ThirdbaseinfoDao
 * @Description: 第三人基本信息数据持久化操作类
 * @author ybb
 * @date 2016年8月12日 上午9:32:28
 * @version V1.0
 */
@Scope("prototype")
@Repository
public class ThirdbaseinfoDao extends GenericDao<Thirdbaseinfo, String>{

}
