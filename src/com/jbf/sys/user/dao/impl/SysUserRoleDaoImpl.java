/************************************************************
 * 类名：SysUserRoleDaoImpl.java
 *
 * 类别：DaoImpl
 * 功能：用户角色Dao实现 
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.user.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.TableNameConst;
import com.jbf.common.dao.impl.GenericDao;
import com.jbf.sys.role.po.SysRole;
import com.jbf.sys.user.dao.SysUserRoleDao;
import com.jbf.sys.user.po.SysUserRole;

@Scope("prototype")
@Repository("sys.user.dao.sysUserRoleDao")
public class SysUserRoleDaoImpl extends GenericDao<SysUserRole, Long> implements
		SysUserRoleDao {

	public void addRolesToUser(Long userid, String roleids) {

		DetachedCriteria dc = DetachedCriteria.forClass(SysUserRole.class).add(
				Property.forName("userid").eq(userid));
		List<SysUserRole> urList = (List<SysUserRole>) findByCriteria(dc);
		deleteAll(urList);

		if (roleids == null || roleids.isEmpty()) {
			return;
		}

		String[] roleidss = roleids.split(",");
		for (String roleid : roleidss) {
			SysUserRole sur = new SysUserRole();
			sur.setUserid(userid);
			sur.setRoleid(Long.valueOf(roleid));

			this.save(sur);
		}

	}

	public List getUserByRoleID(Long roleid) {
		String sql = "select userid, usercode, username from  "
				+ TableNameConst.SYS_USER
				+ " where userid in(select userid from "
				+ TableNameConst.SYS_USER_ROLE + " where roleid =" + roleid
				+ " )";
		return findMapBySql(sql);
	}

	public List<SysRole> getRolesByUserid(Long userid) {
		String sql = "select * from " + TableNameConst.SYS_ROLE
				+ " where roleid in(select distinct roleid from "
				+ TableNameConst.SYS_USER_ROLE + " where userid =" + userid
				+ " )";

		return (List<SysRole>) findVoBySql(sql, SysRole.class);
	}

	@Override
	public List getUsernameByRoleID(Long roleid) {
		String hql = " select user.usercode from SysUserRole ur , SysUser user where ur.userid= user.userid and ur.roleid= ?";
		return this.getHibernateTemplate().find(hql, roleid);
	}

}
