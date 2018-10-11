package com.jbf.sys.role.dao.impl;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.sys.role.dao.SysRoleResourceDao;
import com.jbf.sys.role.po.SysRoleResource;

@Scope("prototype")
@Repository
public class SysRoleResourceDaoImpl extends GenericDao<SysRoleResource, Long>
		implements SysRoleResourceDao {

	// @SuppressWarnings("unchecked")
	// @Override
	// public List<Long> findMenuIdByRole(Long roleid) {
	// String hql = "select menuid from RMRelation  where roleid = ? ";
	// return (List<Long>) this.getHibernateTemplate().find(hql, roleid);
	// }

}
