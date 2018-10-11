/************************************************************
 * 类名：SysUserDaoImpl.java
 *
 * 类别：DaoImpl
 * 功能：用户Dao实现
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.user.dao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.impl.GenericDao;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.dao.SysUserDao;
import com.jbf.sys.user.po.SysUser;

@Scope("prototype")
@Repository("com.jbf.sys.user.dao.impl.SysUserDaoImpl")
public class SysUserDaoImpl extends GenericDao<SysUser, Long> implements
		SysUserDao {

	@Autowired
	MapDataDaoI mapDataDao;
	
	public List<SysResource> getMenusByUser(SysUser user) {

		if (user.getUsertype() == 2) { // 超级用户
			// order by rescource.code
			return (List<SysResource>) super
					.getHibernateTemplate()
					.find(" from SysResource rescource order by parentresid,resorder");
		} else {

			StringBuilder hql = new StringBuilder();
			hql.append(" select rescource from SysRoleResource rm,SysUserRole ur,SysResource rescource ");
			hql.append(" where rm.roleid = ur.roleid");
			hql.append(" and rescource.resourceid = rm.resourceid");
			hql.append(" and ur.userid = ?");
			hql.append(" order by resorder");

			// 去掉重复的菜单
			List<SysResource> relist = (List<SysResource>) super
					.getHibernateTemplate().find(hql.toString(),
							user.getUserid());

			HashSet<Long> ids = new HashSet<Long>();
			for (int i = relist.size() - 1; i >= 0; i--) {
				Long id = relist.get(i).getResourceid();
				if (ids.contains(id)) {
					relist.remove(i);
				} else {
					ids.add(id);
				}
			}

			return relist;

		}
	}
	
	public List<Map> getMenusByUser(SysUser user, Long resourceid) {

		StringBuilder sql = new StringBuilder();
		
		if (user.getUsertype() == 2) { // 超级用户
			sql.append("select distinct RESOURCEID, PARENTRESID, NAME,")
			.append(" WEBPATH, ISLEAF, LEVELNO,")
			.append(" ICONCLS, resorder")
			.append(" from sys_resource")
			.append(" start with resourceid in (").append(resourceid).append(",").append(1).append(")")
			.append(" CONNECT BY parentresid = PRIOR resourceid  ")
			.append(" order by resorder");
		} else {
			sql.append("select distinct rescource.RESOURCEID, rescource.PARENTRESID, rescource.NAME,")
			.append(" rescource.WEBPATH, rescource.ISLEAF, rescource.LEVELNO,")
			.append(" rescource.ICONCLS, rescource.resorder")
			.append(" from sys_role_resource rm, sys_user_role ur, (")
			.append(" select RESOURCEID, PARENTRESID, NAME, resorder, ")
			.append(" WEBPATH, ISLEAF, LEVELNO, ICONCLS")
			.append(" from sys_resource")
			.append(" start with resourceid = ")
			.append(resourceid)
			.append(" CONNECT BY parentresid = PRIOR resourceid) rescource ")
			.append(" where rm.roleid = ur.roleid")
			.append(" and rm.resourceid = rescource.resourceid")
			.append(" and ur.userid = '")
			.append(user.getUserid())
			.append("'")
			.append(" order by resorder");
		}
		
		List<Map> relist = (List<Map>)mapDataDao.queryListBySQL(sql.toString());

		return relist;
	}

}
