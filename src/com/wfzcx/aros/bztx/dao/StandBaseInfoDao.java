package com.wfzcx.aros.bztx.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.bztx.po.StandBaseInfo;

/**
 * 
 * @author zhaoxd
 *
 */
@Scope("prototype")
@Repository
public class StandBaseInfoDao extends GenericDao<StandBaseInfo, String>{

}
