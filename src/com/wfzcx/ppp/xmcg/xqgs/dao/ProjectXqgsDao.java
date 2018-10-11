
package com.wfzcx.ppp.xmcg.xqgs.dao;
/**
 * 
 */
 
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppp.xmcg.xqgs.po.TCgXqgs;

@Scope("prototype")
@Repository
public class ProjectXqgsDao extends GenericDao<TCgXqgs, Integer> {
}
