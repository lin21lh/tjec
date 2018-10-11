package com.wfzcx.app.login.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.common.util.DateUtil;
import com.jbf.sys.user.dao.SysUserDao;
import com.jbf.sys.user.po.SysUser;

@Scope("prototype")
@Service("com.wfzcx.app.login.service.AppLoginService")
public class AppLoginService {

	@Autowired
	SysUserDao userDao;
	
	public Map login(Map<String,Object> param){
		String username = param.get("username").toString();
		String pwd = param.get("pwd").toString();
		Map map = new HashMap<String, Object>();
		List<SysUser> users = (List<SysUser>) userDao.find(
				" from SysUser where usercode=?", username);
		// 判断用户是否存在
		if (users.size() == 0) {
			map.put("rel", false);
			map.put("des", "用户不存在");
			return map;
		}
		SysUser user = users.get(0);
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		// 校验旧密码是否正确
		if (!encoder.isPasswordValid(user.getUserpswd(), pwd, username)) {
			map.put("rel", false);
			map.put("des", "用户名或密码错误");
			return map;
		}
		map.put("rel", true);
		map.put("des", "登陆成功");
		map.put("obj", user);
		return map;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map pwdchange(Map<String,Object> param){
		String username = param.get("username").toString();
		String oldpwd = param.get("oldpwd").toString();
		String newpwd = param.get("newpwd").toString();
		Map map = new HashMap<String, Object>();
		List<SysUser> users = (List<SysUser>) userDao.find(
				" from SysUser where usercode=?", username);
		// 判断用户是否存在
		if (users.size() == 0) {
			map.put("rel", false);
			map.put("des", "用户不存在");
			return map;
		}
		SysUser user = users.get(0);
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		// 校验旧密码是否正确
		if (!encoder.isPasswordValid(user.getUserpswd(), oldpwd, username)) {
			map.put("rel", false);
			map.put("des", "原密码错误");
			return map;
		}
		// 修改为新密码
		String newpassword = encoder.encodePassword(newpwd, username);
		user.setUserpswd(newpassword);
		// 修改人及修改密码时间
		user.setModifyuser(username);
		user.setModifytime(DateUtil.getCurrentDateTime());
		userDao.update(user);
		
		map.put("rel", true);
		map.put("des", "密码修改成功");
		return map;
	}
}
