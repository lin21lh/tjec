package com.wfzcx.aros.xzfy.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.xzfy.po.BSurveyrecord;

/**
 * @ClassName: CasebaseinfoDao
 * @Description: 行政复议案件调查笔录数据持久化操作类
 * @author wangzk
 * @date 2016年8月12日 上午9:30:51
 * @version V1.0
 */
@Scope("prototype")
@Repository
public class BSurveyrecordDao extends GenericDao<BSurveyrecord, String>{

}
