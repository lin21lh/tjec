package com.wfzcx.aros.ajbj.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.ajbj.po.Casechangereq;

/**
 * @ClassName: CasechangereqDao
 * @Description: 案件变更申请表数据库操作持久类
 * @author ybb
 * @date 2017年3月22日 上午9:29:15
 * @version V1.0
 */
@Scope("prototype")
@Repository
public class CasechangereqDao extends GenericDao<Casechangereq, String>{

}
