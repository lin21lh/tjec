package com.jbf.sys.resource.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.sys.resource.dao.SysResourceOperDao;
import com.jbf.sys.resource.po.SysResourceOper;

@Scope("prototype")
@Repository
public class SysResourceOperDaoImpl extends GenericDao<SysResourceOper, Long>
		implements SysResourceOperDao {

}
