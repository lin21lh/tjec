package com.wfzcx.aros.bxzfy.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.bxzfy.po.Rcasebaseinfo;

/**
 * @ClassName: RcasebaseinfoDao
 * @Description: 被复议案件基本信息数据持久化操作类
 * @author ybb
 * @date 2016年9月20日 下午4:20:18
 * @version V1.0
 */
@Scope("prototype")
@Repository
public class RcasebaseinfoDao extends GenericDao<Rcasebaseinfo, String>{

}
