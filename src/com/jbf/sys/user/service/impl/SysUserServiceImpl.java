/************************************************************
 * 类名：SysUserServiceImpl.java
 *
 * 类别：ServiceImpl
 * 功能：用户服务实现
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.user.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.common.TableNameConst;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.util.TreeVoUtil;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.resource.vo.ResourceTreeVo;
import com.jbf.sys.user.dao.SysUserDao;
import com.jbf.sys.user.dao.SysUserRoleDao;
import com.jbf.sys.user.po.SysUser;
import com.jbf.sys.user.po.SysUserRole;
import com.jbf.sys.user.service.SysUserService;

@Scope("prototype")
@Service("sys.user.service.impl.SysUserServiceImpl")
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	SysUserDao userDao;

	@Autowired
	SysUserRoleDao userRoleDao;

	public void setUserDao(SysUserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Long add(SysUser user) throws Exception {
		// 将密码加密后存储

		String uname = user.getUsercode();
		String pswd = user.getUserpswd();
		Md5PasswordEncoder enc = new Md5PasswordEncoder();
		String newpswd = enc.encodePassword(pswd, uname);
		user.setUserpswd(newpswd);
		user.setCreatetime(DateUtil.getCurrentDateTime());

		// 创建人及创建时间、修改人及修改密码时间
		String usercode = SecureUtil.getCurrentUser().getUsercode();
		user.setCreateuser(usercode);
		user.setCreatetime(DateUtil.getCurrentDateTime());
		user.setModifyuser(usercode);
		user.setModifytime(DateUtil.getCurrentDateTime());
		return (Long)userDao.save(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void edit(SysUser user) throws Exception {
		// 前台修改时不含密码参数，防止密码丢失
		String hql = " select  userpswd from SysUser where userid= ?";
		List list = userDao.find(hql, user.getUserid());
		if (list.size() < 1) {
			throw new AppException("crud.obj.not.found");
		}
		user.setUserpswd((String) list.get(0));
		user.setUpdatetime(DateUtil.getCurrentDateTime());
		userDao.update(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(Long userid) throws Exception {
		List<SysUserRole> list = (List<SysUserRole>) userRoleDao.find(
				" from SysUserRole where userid= ?", userid);
		userRoleDao.deleteAll(list);
		userDao.delete(userDao.get(userid));
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaginationSupport query(Map<String, Object> param) {
		
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer
				.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer
				.valueOf(param.get("page").toString()) : 1;

		DetachedCriteria dc = DetachedCriteria.forClass(SysUser.class);

		if (param.get("usercode") != null
				&& param.get("usercode").toString().trim().length() > 0) {
			dc.add(Property.forName("usercode").like(
					param.get("usercode").toString(), MatchMode.START));
		}

		if (param.get("username") != null
				&& param.get("username").toString().trim().length() > 0) {
			dc.add(Property.forName("username").like(
					param.get("username").toString(), MatchMode.ANYWHERE));
		}

		// 非超级管理用户只能查看自己当前创建的用户
		SysUser cUser = SecureUtil.getCurrentUser();
		if (cUser.getUsertype() != 2) {
			dc.add(Restrictions.eq("createuser", cUser.getUsername()));
		}

		Object sortobj = param.get("sort");
		if (sortobj != null && StringUtil.isNotBlank(String.valueOf(sortobj))) {
			String sorts = String.valueOf(param.get("sort"));
			String orders = String.valueOf(param.get("order"));
			for (int i = 0; i < sorts.split(",").length; i++) {
				String sort = sorts.split(",")[i];
				String order = orders.split(",")[i];
				if ("asc".equals(order)) {
					dc.addOrder(Order.asc(sort));
				} else {
					dc.addOrder(Order.desc(sort));
				}
			}
		}

		return userDao.findByCriteria(dc, pageSize, pageIndex);

	}

	public SysUser getUserByUsercode(String usercode) {

		DetachedCriteria dc = DetachedCriteria.forClass(SysUser.class);
		dc.add(Property.forName("usercode").eq(usercode));
		List userList = userDao.findByCriteria(dc);

		return (userList != null && userList.size() > 0) ? (SysUser) userList
				.get(0) : null;
	}

	public List getResourceTree(SysUser user) throws Exception {

		List<SysResource> resources = userDao.getMenusByUser(user);

		ResourceTreeVo vo = null;
		List<ResourceTreeVo> trees = new ArrayList<ResourceTreeVo>(
				resources.size());
		for (SysResource resource : resources) {
			vo = new ResourceTreeVo();
			vo.setId(resource.getResourceid().toString());
			vo.setPid(resource.getParentresid().toString());
			vo.setText(resource.getName());
			if (null == resource.getIsleaf()) {
				vo.setIsLeaf(false);
			} else
				vo.setIsLeaf(resource.getIsleaf().equals(Byte.valueOf("1")) ? true
						: false);
			if (vo.getIsLeaf()) {

				vo.setIconCls(null == resource.getIconCls() ? "icon-node"
						: resource.getIconCls());
			} else {
				vo.setIconCls(null == resource.getIconCls() ? "icon-folder"
						: resource.getIconCls());
			}
			vo.setLevelno(vo.getLevelno());
			vo.setChecked(false);
			vo.setWebpath(resource.getWebpath());
			trees.add(vo);
		}

		trees = (List<ResourceTreeVo>) TreeVoUtil.toBornTree(trees, "0", true);
		return trees;
	}
	
	public List getResourceTree(SysUser user, Long resourceid) throws Exception {

		List<Map> resources = userDao.getMenusByUser(user, resourceid);

		ResourceTreeVo vo = null;
		List<ResourceTreeVo> trees = new ArrayList<ResourceTreeVo>(
				resources.size());
		for (Map<String, Object> resource : resources) {
			vo = new ResourceTreeVo();
			vo.setId(resource.get("resourceid").toString());
			vo.setPid(resource.get("parentresid").toString());
			vo.setText(resource.get("name").toString());
			if (null == resource.get("isleaf")) {
				vo.setIsLeaf(false);
			} else
				vo.setIsLeaf(resource.get("isleaf").equals(Byte.valueOf("1")) ? true : false);
			if (vo.getIsLeaf()) {
				vo.setIconCls(null == resource.get("iconcls") ? "icon-node" : resource.get("iconcls").toString());
			} else {
				vo.setIconCls(null == resource.get("iconcls") ? "icon-folder" : resource.get("iconcls").toString());
			}
			vo.setLevelno(vo.getLevelno());
			vo.setChecked(false);
			vo.setWebpath(null == resource.get("webpath") ? "" : resource.get("webpath").toString());
			trees.add(vo);
		}

		trees = (List<ResourceTreeVo>) TreeVoUtil.toBornTree(trees, "0", true);
		return trees;
	}

	public List getResourceTree() throws Exception {

		List resources = userDao.queryBySQL("Sys_Resource", "levelno = '1' order by parentresid,resorder ");

		return resources;
	}
	
	@Override
	public void addUserToRole(Long roleid, String userids) throws Exception {
		if (roleid == null) {
			throw new AppException("param.invalid");
		}
		if (userids == null || userids.trim().length() == 0) {
			throw new AppException("param.invalid");
		}

		userids = userids.trim();

		String[] ids = userids.split(",");

		// 添加到数据库中
		for (String id : ids) {
			Long userid = Long.parseLong(id);
			SysUserRole ur = new SysUserRole();
			ur.setUserid(userid);
			ur.setRoleid(roleid);
			userRoleDao.save(ur);
		}

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

		userids = userids.trim();

		String[] ids = userids.split(",");

		int size = ids.length;
		// 查询数据里面已有的数据
		List<Long> idList = new ArrayList<Long>();

		for (String id : ids) {
			idList.add(Long.parseLong(id));
		}

		DetachedCriteria dc = DetachedCriteria.forClass(SysUserRole.class).add(
				Property.forName("roleid").eq(roleid));
		dc.add(Property.forName("userid").in(idList));
		List<SysUserRole> resultList = (List<SysUserRole>) userRoleDao
				.findByCriteria(dc);

		if (resultList.size() != size) {
			System.out.println("数据库数据与请求的数据不一致，操作失败");
			throw new AppException("crud.delerr");
		}

		userRoleDao.deleteAll(resultList);

	}

	@Override
	public List getUserByRoleID(Long roleid) {
		return userRoleDao.getUserByRoleID(roleid);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void editPassword(String username, String pswdold, String pswdnew)
			throws Exception {
		// 判断前台页面上用户与session用户是否一致，防止多开窗口多用户登陆问题
		String cuser = SecureUtil.getCurrentUser().getUsercode();
		if (!cuser.equals(username)) {
			throw new AppException("user.password.err.usercode.differ",
					new String[] { username, cuser });
		}
		List<SysUser> users = (List<SysUser>) userDao.find(
				" from SysUser where usercode=?", username);
		// 判断用户是否存在
		if (users.size() == 0) {
			throw new AppException("user.login.user.not.exists");
		}
		SysUser user = users.get(0);

		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		// 校验旧密码是否正确
		if (!encoder.isPasswordValid(user.getUserpswd(), pswdold, username)) {
			throw new AppException(
					"user.password.err.originalpassword.incorrect");
		}
		// 修改为新密码
		String newpassword = encoder.encodePassword(pswdnew, username);
		user.setUserpswd(newpassword);

		// 修改人及修改密码时间
		user.setModifyuser(username);
		user.setModifytime(DateUtil.getCurrentDateTime());

		userDao.update(user);
	}

	@Override
	public void resetUserPasw(Long userid, String pasw) throws Exception {

		SysUser user = userDao.get(userid);
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		user.setUserpswd(encoder.encodePassword(pasw, user.getUsercode()));

		// 修改人及修改密码时间
		user.setModifyuser(SecureUtil.getCurrentUser().getUsercode());
		user.setModifytime(DateUtil.getCurrentDateTime());

		userDao.update(user);
	}
	
	@Override
	public List<SysUser> findUserListByRoleid(Long roleid) {
		List<SysUser> userList = (List<SysUser>)userDao.find("from SysUser where userid in(select userid from SysUserRole where roleid=?)", roleid);
		return userList;
	}

	@Override
	public void resetAllUserPasw(String pasw) throws Exception {
		// TODO Auto-generated method stub
		List<SysUser> userList = userDao.list();
		for (SysUser user : userList) {
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			user.setUserpswd(encoder.encodePassword(pasw, user.getUsercode()));

			// 修改人及修改密码时间
			user.setModifyuser(SecureUtil.getCurrentUser().getUsercode());
			user.setModifytime(DateUtil.getCurrentDateTime());

			userDao.update(user);
		}

	}

	@Override
	public List<SysUser> findAllUserList() {
		// TODO Auto-generated method stub
		return (List<SysUser>)userDao.find("from SysUser ");
	}

	/**
	 * 查询系统预设字典值
	 * @Title: getNameByCode 
	 * @param 字典名称编号，字典项编号
	 * @return Sting 返回类型 
	 * @author ztt
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String getNameByCode(String elementcode, String code) {
		String table = TableNameConst.SYS_DICENUMITEM;
		StringBuilder where = new StringBuilder();
		where.append(" upper(elementcode) ='").append(elementcode)
		     .append("' and status = 0 and code ='").append(code).append("'");
		String name = "";
		if (null != code)
		{
			List<Map<String, Object>> probaseinfoList = userDao.queryBySQL(table, where.toString());
			if (null != probaseinfoList && !probaseinfoList.isEmpty()) {
				name = (String)probaseinfoList.get(0).get("name");
			} 
		}
		return name;
	}
}
