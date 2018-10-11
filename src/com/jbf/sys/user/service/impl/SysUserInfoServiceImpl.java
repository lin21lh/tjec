package com.jbf.sys.user.service.impl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.util.DateUtil;
import com.jbf.sys.user.po.SysUser;
import com.jbf.sys.user.service.SysUserInfoService;

/**
 * 个人信息维护service
 * @author songxiaojie
 *
 */
@Scope("prototype")
@Service
public class SysUserInfoServiceImpl implements SysUserInfoService {
	
	@Autowired
	MapDataDaoI mapDataDao;

	@SuppressWarnings("unchecked")
	@Override
	public Map getUserInfo(Map<String, Object> param) {

		Long userId = (Long)param.get("userid");
		int userExtCount = (Integer) param.get("countExt");
		
		StringBuffer sql = new StringBuffer();
		sql.append("select u.userid userid,");
		sql.append("       u.usercode usercode,"); 
		sql.append("       u.username username,");
		sql.append("       u.userpswd userpswd,");
		sql.append("       u.usertype usertype,"); 
		sql.append("       u.orgcode orgcode,");
		sql.append("       (select d.name from sys_dept d where u.orgcode=d.code and u.userid="+userId+") orgname,");
		sql.append("       u.grpcode grpcode,");
		sql.append("       u.status status,"); 
		sql.append("       u.updatetime updatetime,");
		sql.append("       u.isca isca,");
		sql.append("       u.remark remark,"); 
		sql.append("       u.createuser createuser,");
		sql.append("       u.createtime createtime,");
		sql.append("       u.modifyuser modifyuser,");
		sql.append("       u.modifytime modifytime,"); 
		sql.append("       u.overduedate overduedate");
		if(userExtCount > 0)
		{
			sql.append("       ,e.phone phone,");
			sql.append("       e.weixin weixin,"); 
			sql.append("       e.qq qq,");
			sql.append("       e.email email");
		}
		sql.append("  from sys_user u");
		
		if(userExtCount > 0)
		{
			
			sql.append(",sys_userexp e ");
			sql.append("  where u.userid=e.userid");
		}
		else
		{
			sql.append(" where 1=1 ");
		}
	
		sql.append("  and u.userid="+userId);
		System.err.println("getUserInfo--------------"+sql.toString());
		return (Map)mapDataDao.queryListBySQL(sql.toString()).get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getCountUserExtByUserId(Long userid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) count from sys_userexp where userid="+userid);
		System.err.println("getCountUserExtByUserId---------------"+sql.toString());
		Map map = (Map) mapDataDao.queryListBySQL(sql.toString()).get(0);
		
		return Integer.valueOf(map.get("count").toString());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void editPersonalInfo(SysUser sysUser,Map<String, Object> param) {
		//判断是否已经存在用户扩展信息
		Long userid =sysUser.getUserid();
		param.put("userid", userid);
		int countExt = this.getCountUserExtByUserId(sysUser.getUserid());
		//如果没有插入用户扩展信息
		if(countExt == 0)
		{
			
			//插入用户扩展表
			mapDataDao.add(param, "sys_userexp");
		}
		else
		{
			//如果存在，则修改用户扩展表
			mapDataDao.update(param, "sys_userexp");
		}
		
		String nowDat =DateUtil.getCurrentDateTime();
		//修改用户信息表
		StringBuffer sql = new StringBuffer();
		sql.append("update sys_user u set u.username='"+sysUser.getUsername()+"'");
		sql.append("  ,u.remark ='"+sysUser.getRemark()+"'"); 
		sql.append("  ,u.updatetime ='"+nowDat+"'"); 
		sql.append("  where u.userid ="+sysUser.getUserid());
		System.err.println("updateUserInfo---------------"+sql.toString());
		mapDataDao.updateTX(sql.toString());
	}

	
}
