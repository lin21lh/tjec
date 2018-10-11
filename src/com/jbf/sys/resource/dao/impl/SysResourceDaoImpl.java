package com.jbf.sys.resource.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.sys.resource.dao.SysResourceDao;
import com.jbf.sys.resource.po.SysResource;

@Scope("prototype")
@Repository
public class SysResourceDaoImpl extends GenericDao<SysResource, Long> implements
		SysResourceDao {
}
