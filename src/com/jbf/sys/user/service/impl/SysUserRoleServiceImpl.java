/************************************************************
 * 类名：SysUserRoleServiceImpl.java
 *
 * 类别：ServiceImpl
 * 功能：用户角色服务实现
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.user.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.TreeVoUtil;
import com.jbf.common.vo.TreeVo;
import com.jbf.sys.role.dao.SysRoleDao;
import com.jbf.sys.role.po.SysRole;
import com.jbf.sys.user.dao.SysUserRoleDao;
import com.jbf.sys.user.po.SysUserRole;
import com.jbf.sys.user.service.SysUserRoleService;

@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {

	@Autowired
	private SysUserRoleDao sysUserRoleDao;

	@Autowired
	private SysRoleDao sysRoleDao;

	@Override
	public List<SysUserRole> getAuthRoles(Long userid) {

		DetachedCriteria dc = DetachedCriteria.forClass(SysUserRole.class).add(
				Property.forName("userid").eq(userid));
		return (List<SysUserRole>) sysUserRoleDao.findByCriteria(dc);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addRolesToUser(Long userid, String roleids) {
		sysUserRoleDao.addRolesToUser(userid, roleids);
	}

	@Override
	public List querySelectedUserByRole(Long roleId, String username) {
		String hql = "select userid,username from SysUser  "
				+ " where userid  in (select userid from SysUserRole where roleid= ?  ) and username like ?  "
				+ " order by username";
		String username_filter = "%";
		if (username != null && username.trim().length() > 0) {
			username_filter = "%" + username.trim() + "%";
		}
		List list = sysUserRoleDao.find(hql, roleId, username_filter);

		List<HashMap> maplist = new ArrayList<HashMap>();

		for (Object o : list) {
			Object[] arr = (Object[]) o;
			HashMap map = new HashMap();
			map.put("id", arr[0]);
			map.put("text", arr[1]);
			maplist.add(map);
		}
		return maplist;

	}

	@Override
	public List queryUnselectedUserByRole(Long roleId, String username) {

		String hql = "select  userid,username from SysUser  "
				+ " where userid not in (select userid from SysUserRole where roleid= ? ) and username like ?"
				+ " order by username";
		String username_filter = "%";
		if (username != null && username.trim().length() > 0) {
			username_filter = "%" + username.trim() + "%";
		}
		List list = sysUserRoleDao.find(hql, roleId, username_filter);

		List<HashMap> maplist = new ArrayList<HashMap>();

		for (Object o : list) {
			Object[] arr = (Object[]) o;
			HashMap map = new HashMap();
			map.put("id", arr[0]);
			map.put("text", arr[1]);
			maplist.add(map);
		}
		return maplist;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void removeUserFromRole(Long roleid, String userids)
			throws Exception {
		if (roleid == null) {
			throw new AppException("param.invalid");
		}
		if (userids == null || userids.trim().length() == 0) {
			throw new AppException("param.invalid");
		}

		// 拒绝不是该角色的创建者修改及删除
		String curentUser = SecureUtil.getCurrentUser().getUsercode();
		SysRole role = sysRoleDao.get(roleid);

		if (!curentUser.equals(role.getCreater())) {
			throw new AppException("permissions.role.createuser.mismatching");
		}

		userids = userids.trim();

		String[] ids = userids.split(",");

		int size = ids.length;
		// 查询数据里面已有的数据
		List<Long> idList = new ArrayList<Long>();

		for (String id : ids) {
			idList.add(Long.parseLong(id));
		}

		DetachedCriteria dc = DetachedCriteria.forClass(SysUserRole.class);
		dc.add(Property.forName("roleid").eq(roleid));
		dc.add(Property.forName("userid").in(idList));
		List<SysUserRole> resultList = (List<SysUserRole>) sysUserRoleDao
				.findByCriteria(dc);

		if (resultList.size() != size) {
			throw new AppException("crud.saveerr");
		}
		sysUserRoleDao.deleteAll(resultList);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addUserToRole(Long roleid, String userids) throws Exception {

		if (roleid == null) {
			throw new AppException("param.invalid");
		}
		if (userids == null || userids.trim().length() == 0) {
			throw new AppException("param.invalid");
		}

		// 拒绝不是该角色的创建者修改及删除
		String curentUser = SecureUtil.getCurrentUser().getUsercode();
		SysRole role = sysRoleDao.get(roleid);

		if (!curentUser.equals(role.getCreater())) {
			throw new AppException("permissions.role.createuser.mismatching");
		}

		userids = userids.trim();

		String[] ids = userids.split(",");

		// 添加到数据库中
		for (String id : ids) {
			Long userid = Long.parseLong(id);
			SysUserRole ur = new SysUserRole();
			ur.setUserid(userid);
			ur.setRoleid(roleid);
			sysUserRoleDao.save(ur);
		}
	}

	@Override
	public List queryRoleByUser(Long userid) {

		List<SysRole> roles = (List<SysRole>) sysRoleDao
				.find(" from SysRole order by roleid");
		List<SysUserRole> userRoles = (List<SysUserRole>) sysUserRoleDao.find(
				" from SysUserRole where userid= ?", userid);
		Set<Long> roleids = new HashSet<Long>();
		for (SysUserRole r : userRoles) {
			roleids.add(r.getRoleid());
		}

		List<TreeVo> treevolist = new ArrayList<TreeVo>();
		for (SysRole r : roles) {
			TreeVo vo = new TreeVo();
			vo.setId("" + r.getRoleid());
			vo.setPid("" + r.getParentroleid());
			vo.setText(r.getRolename());
			vo.setChecked(roleids.contains(r.getRoleid()));
			treevolist.add(vo);
		}
		return TreeVoUtil.toBornTree(treevolist, "0", true);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void editUserRoles(Long userid, String roleids) throws Exception {
		List<SysUserRole> list = (List<SysUserRole>) sysUserRoleDao.find(
				" from SysUserRole where userid= ? ", userid);
		sysUserRoleDao.deleteAll(list);
		sysUserRoleDao.flush();
		String ids[] = roleids.split(",");
		for (String id : ids) {
			Long roleid = Long.parseLong(id);
			SysUserRole sr = new SysUserRole();
			sr.setRoleid(roleid);
			sr.setUserid(userid);
			sysUserRoleDao.save(sr);
		}
	}
}
