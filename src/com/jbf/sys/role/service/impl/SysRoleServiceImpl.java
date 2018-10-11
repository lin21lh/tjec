package com.jbf.sys.role.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.TreeVoUtil;
import com.jbf.sys.role.dao.SysRoleDao;
import com.jbf.sys.role.dao.SysRoleResourceDao;
import com.jbf.sys.role.dao.SysRoleResourceOperDao;
import com.jbf.sys.role.po.SysRole;
import com.jbf.sys.role.po.SysRoleResource;
import com.jbf.sys.role.po.SysRoleResourceOper;
import com.jbf.sys.role.service.SysRoleService;
import com.jbf.sys.role.vo.RoleTreeVo;
import com.jbf.sys.user.dao.SysUserRoleDao;
import com.jbf.sys.user.po.SysUser;
import com.jbf.sys.user.po.SysUserRole;

@Scope("prototype")
@Service
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	SysRoleDao sysRoleDao;

	@Autowired
	SysUserRoleDao sysUserRoleDao;

	@Autowired
	SysRoleResourceDao sysRoleResourceDao;

	@Autowired
	SysRoleResourceOperDao sysRoleResourceOperDao;

	@Override
	public List<Integer> queryRoleIdsByUserID(Integer userID) {
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public SysRole get(Long id) {

		return sysRoleDao.get(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(SysRole role) throws Exception {
		String creater = SecureUtil.getCurrentUser().getUsercode();
		if (role.getStatus() == null) {
			role.setStatus(0);
		}
		
		if (role.getRoleid() == null) {
			if (role.getParentroleid() != null) {
				SysRole parentRole = sysRoleDao.get(role.getParentroleid());
				role.setLevelno(parentRole.getLevelno() + 1);
				role.setIsleaf(1);
				if (parentRole.getIsleaf() == 1) {
					parentRole.setIsleaf(0);
					sysRoleDao.update(parentRole);
				}
			}
			// 新建
			role.setCreater(creater);
		} else {
			// 修改
			// 拒绝不是该角色的创建者修改及删除
			if (!creater.equals(role.getCreater())) {
				throw new AppException(
						"permissions.role.createuser.mismatching");
			}
		}
		sysRoleDao.saveOrUpdate(role);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(Long id) throws Exception {
		String creater = SecureUtil.getCurrentUser().getUsercode();

		// 拒绝不是该角色的创建者修改及删除
		SysRole role = sysRoleDao.get(id);
		if (!creater.equals(role.getCreater())) {
			throw new AppException("permissions.role.createuser.mismatching");
		}
		
		if (role.getParentroleid() != null) { //判断是否修改符角色的是否叶子节点属性
			
			DetachedCriteria dc = DetachedCriteria.forClass(SysRole.class);
			dc.add(Property.forName("parentroleid").eq(role.getParentroleid()));
			dc.add(Property.forName("roleid").ne(role.getRoleid()));
			
			List<SysRole> list = (List<SysRole>)sysRoleDao.findByCriteria(dc);
			if (list.isEmpty()) {
				SysRole parentRole = sysRoleDao.get(role.getParentroleid());
				parentRole.setIsleaf(1);
				sysRoleDao.update(parentRole);
			}
		}
		sysRoleDao.delete(role);

		// 删除roleuser
		List<SysUserRole> sur = (List<SysUserRole>) sysUserRoleDao.find(
				" from SysUserRole where roleid= ?", id);
		sysUserRoleDao.deleteAll(sur);

		// 删除roleresource
		List<SysRoleResource> srr = (List<SysRoleResource>) sysRoleResourceDao
				.find(" from SysRoleResource where roleid= ?", id);
		sysRoleResourceDao.deleteAll(srr);
		// 删除 roleresourceoper

		List<SysRoleResourceOper> srro = (List<SysRoleResourceOper>) sysRoleResourceOperDao
				.find(" from SysRoleResourceOper where roleid= ?", id);
		sysRoleResourceOperDao.deleteAll(srro);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List query() {
		List<SysRole> rolelist = (List<SysRole>) sysRoleDao
				.find(" from SysRole");

		List<RoleTreeVo> list = new ArrayList<RoleTreeVo>();
		for (SysRole role : rolelist) {
			RoleTreeVo vo = new RoleTreeVo();
			vo.setId("" + role.getRoleid());
			vo.setPid("" + role.getParentroleid());
			vo.setText(role.getRolename());
			vo.setRolecode(role.getRolecode());
			vo.setStatus(role.getStatus());
			list.add(vo);
		}
		return TreeVoUtil.toBornTree(list, "0", true);
	}

	public List querySelectedUsers(Long roleid){
	    /*DetachedCriteria dc = DetachedCriteria.forClass(SysUserRole.class);
	    List<Long> uids = new ArrayList<Long>();
	    if(roleid != null){
    	    dc.add(Property.forName("roleid").eq(roleid));
    	    List<SysUserRole> surs = (List<SysUserRole>)sysUserRoleDao.findByCriteria(dc);
    	    
    	    for (SysUserRole sur : surs) {
    	        uids.add(sur.getUserid());
            }
	    }
	    
	    if(uids.isEmpty()){
	        return new ArrayList();
	    }
	    
	    dc = DetachedCriteria.forClass(SysUser.class);
	    if(roleid != null){
	        dc.add(Property.forName("userid").in(uids));
	    }
	    return sysUserRoleDao.findByCriteria(dc);*/
		
		DetachedCriteria dcUserRole = DetachedCriteria.forClass(SysUserRole.class,"role");
		 
		 DetachedCriteria dcUser = DetachedCriteria.forClass(SysUser.class,"user");
		 dcUserRole.add(Restrictions.eq("roleid", roleid));
		 dcUserRole.add(Property.forName("role.userid").eqProperty("user.userid"));
		 dcUser.add(Subqueries.exists(dcUserRole.setProjection(Projections.property("role.userid"))));
		 return sysUserRoleDao.findByCriteria(dcUser);
	}
	
	public List queryUnselectUsers(Long roleid){
		/*写了什么啊！！！
	    DetachedCriteria dc = DetachedCriteria.forClass(SysUserRole.class);
	    List<Long> uids = new ArrayList<Long>();
	    if(roleid != null){
	        dc.add(Property.forName("roleid").eq(roleid));
	        List<SysUserRole> surs = (List<SysUserRole>)sysUserRoleDao.findByCriteria(dc);
	        
	        for (SysUserRole sur : surs) {
	            uids.add(sur.getUserid());
	        }
	    }
	    
	    dc = DetachedCriteria.forClass(SysUser.class);
	    if(roleid != null && !uids.isEmpty()){
	        dc.add(Restrictions.not(Restrictions.in("userid", uids)));
	    }
	    return sysUserRoleDao.findByCriteria(dc);
	*/
		 DetachedCriteria dcUserRole = DetachedCriteria.forClass(SysUserRole.class,"role");
		 
		 DetachedCriteria dcUser = DetachedCriteria.forClass(SysUser.class,"user");
		 dcUserRole.add(Restrictions.eq("roleid", roleid));
		 dcUserRole.add(Property.forName("role.userid").eqProperty("user.userid"));
		 dcUser.add(Subqueries.notExists(dcUserRole.setProjection(Projections.property("role.userid"))));
		 
		return sysUserRoleDao.findByCriteria(dcUser);
	}
}
