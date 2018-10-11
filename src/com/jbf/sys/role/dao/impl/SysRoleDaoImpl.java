package com.jbf.sys.role.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.sys.role.dao.SysRoleDao;
import com.jbf.sys.role.po.SysRole;

@Scope("prototype")
@Repository
public class SysRoleDaoImpl extends GenericDao<SysRole, Long> implements
		SysRoleDao {

}
