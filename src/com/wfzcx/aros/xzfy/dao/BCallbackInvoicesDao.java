package com.wfzcx.aros.xzfy.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.xzfy.po.BCallbackInvoicesInfo;

/**
 * @Description: 廉政回访
 * @version V1.0
 */
@Scope("prototype")
@Repository
public class BCallbackInvoicesDao extends GenericDao<BCallbackInvoicesInfo, String>{

}
