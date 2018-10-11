package com.wfzcx.aros.gzgl.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.gzgl.po.BRulebaseinfo;

@Scope("prototype")
@Repository
public class BRulebaseinfoDao extends GenericDao<BRulebaseinfo, String>
{
}
