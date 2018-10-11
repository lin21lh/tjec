package com.jbf.sys.sortfieldset.service.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.role.po.SysRole;
import com.jbf.sys.sortfieldset.dao.SysSortFieldSetDao;
import com.jbf.sys.sortfieldset.po.SysSortFieldSet;
import com.jbf.sys.sortfieldset.service.SysSortFieldSetService;
import com.jbf.sys.user.dao.SysUserRoleDao;
import com.jbf.sys.user.po.SysUser;

@Scope("prototype")
@Service
public class SysSortFieldSetServiceImpl implements SysSortFieldSetService {

	@Autowired
	SysSortFieldSetDao sortFieldSetDao;
	
	@Autowired
	SysUserRoleDao userRoleDao;
	
	@Override
	public PaginationSupport query(Map<String, Object> paramMap, Integer pageSize, Integer pageIndex) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysSortFieldSet.class);
		String sortname = (String)paramMap.get("sortname");
		if (StringUtil.isNotBlank(sortname))
			dc.add(Property.forName("sortname").like(sortname, MatchMode.ANYWHERE));
		
		return sortFieldSetDao.findByCriteria(dc, pageSize, pageIndex);
	}
	
	@Override
	public void add(SysSortFieldSet sortFieldSet) {
		// TODO Auto-generated method stub
		sortFieldSetDao.save(sortFieldSet);
	}


	@Override
	public void edit(SysSortFieldSet sortFieldSet) {
		// TODO Auto-generated method stub
		sortFieldSetDao.update(sortFieldSet);
	}


	@Override
	public void delete(Long sortid) {
		// TODO Auto-generated method stub
		sortFieldSetDao.delete(sortid);
	}

	@Override
	public List<SysSortFieldSet> queryListByApp(Long menuid) {
		
		SysUser user = SecureUtil.getCurrentUser();
		
		List<SysRole> roleList = userRoleDao.getRolesByUserid(user.getUserid());
		String roleids = "";
		if (!roleList.isEmpty()) {
			for(SysRole role : roleList) {
				if (roleids.length() > 0)
					roleids += ",";
				roleids += role.getRoleid().toString();
			}
			
		}
 		String hql = " * from SysSortFieldSet where ((resourceid=? and roleid is null and userid is null) or";
		if (roleids.length() > 0) {
			hql += " (resourceid=? and roleid in(?) and userid is null) or ";
		}
		
		hql += "(resourceid=? and userid=?)) and status=0 order by userid, roleid, orderno";
 		
		List<SysSortFieldSet> sfsList = null;
 		if (roleList.isEmpty()) {
 			sfsList = (List<SysSortFieldSet>) sortFieldSetDao.find(hql, menuid, menuid, menuid, user.getUserid());
 		} else {
 			
 			sfsList = (List<SysSortFieldSet>) sortFieldSetDao.find(hql, menuid, menuid, roleids, menuid, user.getUserid());
 		}
 		return sfsList;
	}
}
