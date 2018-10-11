
package com.wfzcx.ppp.xmcg.cgxx.dao;
/**
 * 
 */
 
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppp.xmcg.cgxx.po.TCgCgxx;

@Scope("prototype")
@Repository
public class ProjectCgxxDao extends GenericDao<TCgCgxx, Integer> {
}
