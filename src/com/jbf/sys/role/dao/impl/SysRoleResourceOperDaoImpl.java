package com.jbf.sys.role.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.sys.role.dao.SysRoleResourceOperDao;
import com.jbf.sys.role.po.SysRoleResourceOper;

@Scope("prototype")
@Repository
public class SysRoleResourceOperDaoImpl extends
		GenericDao<SysRoleResourceOper, Long> implements SysRoleResourceOperDao {

}
