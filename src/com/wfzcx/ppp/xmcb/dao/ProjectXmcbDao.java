
package com.wfzcx.ppp.xmcb.dao;
/**
 * 
 */
 
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppp.xmcb.po.TXmxx;

@Scope("prototype")
@Repository
public class ProjectXmcbDao extends GenericDao<TXmxx, Integer> {
}
