package com.jbf.sys.role.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.TreeVoUtil;
import com.jbf.common.vo.TreeVo;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.resource.po.SysResourceOper;
import com.jbf.sys.resource.vo.ResourceTreeVo;
import com.jbf.sys.role.dao.SysRoleDao;
import com.jbf.sys.role.dao.SysRoleResourceDao;
import com.jbf.sys.role.dao.SysRoleResourceOperDao;
import com.jbf.sys.role.po.SysRole;
import com.jbf.sys.role.po.SysRoleResource;
import com.jbf.sys.role.po.SysRoleResourceOper;
import com.jbf.sys.role.service.SysRoleResourceService;
import com.jbf.sys.role.vo.RoleTreeVo;

@Scope("prototype")
@Service
public class SysRoleResourceServiceImpl implements SysRoleResourceService {

	@Autowired
	SysRoleResourceDao sysRoleResourceDao;
	@Autowired
	SysRoleDao sysRoleDao;
	@Autowired
	SysRoleResourceOperDao sysRoleResourceOperDao;

	@Override
	public List query(Long roleid) {
		// 查询角色的父角色拥有的菜单
		SysRole role = sysRoleDao.get(roleid);
		Long parentRoleId = role.getParentroleid();

		String reshql = "select res from SysRoleResource  rr ,SysResource res  where res.resourceid=rr.resourceid and rr.roleid=? order by parentresid,resorder";

		List<SysResource> pres = null;

		if (parentRoleId == 0l) { // 如果是根角色
			pres = (List<SysResource>) sysRoleResourceDao
					.find(" from SysResource  order by parentresid,resorder");
		} else {
			pres = (List<SysResource>) sysRoleResourceDao.find(reshql,
					parentRoleId);
		}

		// 查询本角色拥有的菜单

		List<SysResource> selfRes = (List<SysResource>) sysRoleResourceDao
				.find(reshql, roleid);

		Set<Long> set = new HashSet<Long>();
		for (SysResource res : selfRes) {
			set.add(res.getResourceid());
		}

		// 组建树
		List<ResourceTreeVo> treelist = new ArrayList<ResourceTreeVo>();
		for (SysResource res : pres) {
			ResourceTreeVo vo = new ResourceTreeVo();
			vo.setId("" + res.getResourceid());
			vo.setPid("" + res.getParentresid());
			vo.setText(res.getName());
			vo.setChecked(set.contains(res.getResourceid()));
			treelist.add(vo);
		}
		return TreeVoUtil.toBornTree(treelist, "0", true);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveResourceToRole(Long roleId, String resourceIds)
			throws Exception {

		String curentUser = SecureUtil.getCurrentUser().getUsercode();
		SysRole role = sysRoleDao.get(roleId);
		// 拒绝不是该角色的创建者修改及删除
		if (!curentUser.equals(role.getCreater())) {
			throw new AppException("permissions.role.createuser.mismatching");
		}

		List<SysRoleResource> list = (List<SysRoleResource>) sysRoleResourceOperDao
				.find(" from SysRoleResource where roleid=?", roleId);

		for (SysRoleResource o : list) {
			sysRoleResourceDao.delete(o);
		}
//		sysRoleResourceDao.deleteAll(list);
		System.out.println("size:" + list.size());

		String[] resids = resourceIds.split(",");
		for (String s : resids) {

			if (s != null && s.trim().length() > 0) {
				SysRoleResource res = new SysRoleResource();
				res.setResourceid(Long.parseLong(s));
				res.setRoleid(roleId);
				sysRoleResourceDao.save(res);
			}
		}
	}

	@Override
	public void editRoleResourceOper(Long roleId, Long resourceId,
			String operIds) throws Exception {
		
		// 拒绝不是该角色的创建者修改及删除
		String curentUser = SecureUtil.getCurrentUser().getUsercode();
		SysRole role = sysRoleDao.get(roleId);
		
		if (!curentUser.equals(role.getCreater())) {
			throw new AppException("permissions.role.createuser.mismatching");
		}
		
		List<SysRoleResourceOper> rrop = (List<SysRoleResourceOper>) sysRoleResourceOperDao
				.find(" from SysRoleResourceOper where roleid= ? and resourceid= ?",
						roleId, resourceId);
		sysRoleResourceOperDao.deleteAll(rrop);
		if (operIds == null || operIds.trim().length() == 0) {
			return;
		}
		String ids[] = operIds.split(",");
		for (String id : ids) {
			SysRoleResourceOper o = new SysRoleResourceOper();
			Long l = null;
			try {
				l = Long.parseLong(id);
			} catch (Exception e) {
				e.printStackTrace();
				throw new AppException("param.invalid");
			}
			o.setResoperid(l);
			o.setResourceid(resourceId);
			o.setRoleid(roleId);
			sysRoleResourceOperDao.save(o);
		}

	}

	@Override
	public List queryRoleResourceOper(Long roleId, Long resourceId) {

		// 查询拥有的操作
		List<SysRoleResourceOper> rrop = (List<SysRoleResourceOper>) sysRoleResourceOperDao
				.find(" from SysRoleResourceOper where roleid= ? and resourceid= ?",
						roleId, resourceId);

		Set<Long> set = new HashSet<Long>();
		for (SysRoleResourceOper o : rrop) {
			set.add(o.getResoperid());
		}
		List<SysResourceOper> operList = (List<SysResourceOper>) sysRoleResourceOperDao
				.find(" from SysResourceOper where resourceid=  ? order by code",
						resourceId);
		List<ResourceTreeVo> volist = new ArrayList<ResourceTreeVo>();
		for (SysResourceOper op : operList) {
			ResourceTreeVo vo = new ResourceTreeVo();
			vo.setId("" + op.getId());
			if(op.getPosition()!=null){
			vo.setText(op.getName()+"&nbsp;&nbsp;-&nbsp;&nbsp;"+op.getPosition());
			}else{
				vo.setText(op.getName());
			}
			vo.setPid("0");
			vo.setCode(op.getCode());
			vo.setChecked(set.contains(op.getId()));
			volist.add(vo);
		}

		return TreeVoUtil.toBornTree(volist, "0", true);
	}
	
	public List findRoleListByResource(Long resourceid) {
		List<SysRole> roleList = (List<SysRole>) sysRoleDao.find(" from SysRole where roleid in(select roleid from SysRoleResource where resourceid=?)", resourceid);
		
		List<RoleTreeVo> list = new ArrayList<RoleTreeVo>();
		for (SysRole role : roleList) {
			RoleTreeVo vo = new RoleTreeVo();
			vo.setId("" + role.getRoleid());
			vo.setPid("" + role.getParentroleid());
			vo.setText(role.getRolecode() + "-" + role.getRolename());
			vo.setLevelno(role.getLevelno());
			vo.setIsLeaf(role.getIsleaf() == 1);
			vo.setRolecode(role.getRolecode());
			vo.setStatus(role.getStatus());
			list.add(vo);
		}
		return TreeVoUtil.toBornTree(list, "0", true);
	}
}
