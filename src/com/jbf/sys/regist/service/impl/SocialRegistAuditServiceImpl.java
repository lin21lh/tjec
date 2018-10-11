package com.jbf.sys.regist.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.dept.dao.SysDeptDao;
import com.jbf.sys.dept.po.SysDept;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.regist.dao.ProSocialRegistDaoI;
import com.jbf.sys.regist.po.ProSocialRegist;
import com.jbf.sys.regist.service.SocialRegistAuditServiceI;
import com.jbf.sys.role.dao.SysRoleDao;
import com.jbf.sys.role.po.SysRole;
import com.jbf.sys.user.dao.SysUserDao;
import com.jbf.sys.user.dao.SysUserRoleDao;
import com.jbf.sys.user.po.SysUser;
import com.jbf.sys.user.po.SysUserRole;
import com.wfzcx.fam.common.MessageComponent;

@Scope("prototype")
@Service("/sys/regist/impl/SocialRegistAuditServiceImpl")
public class SocialRegistAuditServiceImpl implements SocialRegistAuditServiceI {

	@Autowired
	SysUserDao userDao;
	@Autowired
	SysDeptDao sysDeptDao;
	@Autowired
	SysRoleDao sysRoleDao;
	@Autowired
	ParamCfgComponent pcfg;
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	MessageComponent messageComponent;
	@Autowired
	SysUserRoleDao userRoleDao;
	@Autowired
	ProSocialRegistDaoI ProSocialRegistDao;
	
	@Override
	public PaginationSupport qrySocAud(Map map) {
		// TODO Auto-generated method stub
		
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		
		String status = map.get("status")==null?"":map.get("status").toString();
		String usercode = map.get("usercode")==null?"":map.get("usercode").toString();
		String username = map.get("username")==null?"":map.get("username").toString();
		String orgcode = map.get("orgcode")==null?"":map.get("orgcode").toString();
		String orgname = map.get("orgname")==null?"":map.get("orgname").toString();
		String linkperson = map.get("linkperson")==null?"":map.get("linkperson").toString();
		String linkphone = map.get("linkphone")==null?"":map.get("linkphone").toString();
		
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT T.SOCIALID,");
		sql.append("       T.USERCODE,");
		sql.append("       T.USERNAME,");
		sql.append("       T.USERPSWD,");
		sql.append("       T.LINKPERSON,");
		sql.append("       T.LINKPHONE,");
		sql.append("       T.ORGCODE,");
		sql.append("       T.ORGNAME,");
		sql.append("       T.ISCOMBO,");
		sql.append("       T.STATUS,");
		sql.append("       t.preferences_code,");
		sql.append("       t.preferences_name,");
		sql.append("       T.CATEGORY_NAME,");
		sql.append("       T.CATEGORY_CODE,");
		sql.append("       T.APPLICATION_TIME,");
		sql.append("       T.AUDIT_TIME,");
		sql.append("       T.REMARK,");
		sql.append("       T.AUDIT_USER,");
		sql.append("       (SELECT A.USERNAME FROM SYS_USER A WHERE A.USERID = T.AUDIT_USER) AUDIT_USER_NAME");
		sql.append("  FROM PRO_SOCIAL_REGIST T");
		sql.append("  where 1=1 ");

		if(!"".equals(status)){
			sql.append("  and t.status = '"+status+"'");
		}
		if(!"".equals(usercode)){
			sql.append("  and t.usercode like '%"+usercode+"%'");
		}
		if(!"".equals(username)){
			sql.append("  and t.username like '%"+username+"%'");
		}
		if(!"".equals(orgcode)){
			sql.append("  and t.orgcode like '%"+orgcode+"%'");
		}
		if(!"".equals(orgname)){
			sql.append("  and t.orgname like '%"+orgname+"%'");
		}
		if(!"".equals(linkperson)){
			sql.append("  and t.linkperson like '%"+linkperson+"%'");
		}
		if(!"".equals(linkphone)){
			sql.append("  and t.linkphone like '%"+linkphone+"%'");
		}
		sql.append("  order by T.APPLICATION_TIME desc");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void approveSocAud(Map map) throws Exception {
		// TODO Auto-generated method stub
		String socialids = map.get("socialid")==null?"":map.get("socialid").toString();
		if(!"".equals(socialids)){
			String[] socialidss = socialids.split(",");
			for(int i=0;i<socialidss.length;i++){
				SysUser sysUser = new SysUser();
				ProSocialRegist t = ProSocialRegistDao.get(Integer.parseInt(socialidss[i]));
				
				/*保存机构表*/
				SysDept sysDept = new SysDept();
				
				sysDept.setName(t.getOrgname());
				sysDept.setWholename(t.getOrgname());
				sysDept.setShortname(t.getOrgname());
				sysDept.setIsbncode(t.getOrgcode());
				sysDept.setLevelno(1);
				sysDept.setIsleaf(1);
				sysDept.setStartdate(DateUtil.getCurrentDateTime());
				sysDept.setStatus(0);
				sysDept.setAgencycat((long)3);
				List<?> reList = new ArrayList();
				List<Map> list = null;
				do{
					list = (List<Map>)sysDeptDao.findMapBySql("select seq_regist_code.nextval seqcode from dual");
					reList = sysDeptDao.findMapBySql("select * from sys_dept t where t.code = '"+list.get(0).get("seqcode").toString()+"'");
				}while(reList.size()>0);
				sysDept.setCode(list.get(0).get("seqcode").toString());
				sysDeptDao.save(sysDept);
				
				/*保存系统用户*/
				sysUser.setUsercode(t.getUsercode());
				sysUser.setUsername(t.getUsername());
				Md5PasswordEncoder enc = new Md5PasswordEncoder();
				String newpswd = enc.encodePassword(t.getUserpswd(), t.getUsercode());
				sysUser.setUserpswd(newpswd);
				sysUser.setCreatetime(DateUtil.getCurrentDateTime());
				String userid = String.valueOf(SecureUtil.getCurrentUser().getUserid());
				sysUser.setCreateuser(userid);
				sysUser.setUsertype((byte)0);
				sysUser.setOrgcode(sysDept.getCode());
				sysUser.setOrgname(t.getOrgname());
				sysUser.setStatus((byte)0);
				sysUser.setIsca((byte)0);
				userDao.save(sysUser);
				
				//赋给默认的用户角色
				String roleCode = pcfg.findGeneralParamValue("SYSTEM", "REGIST");
				if("".equals(roleCode) || roleCode==null){
					throw new AppException("配置表没有配置社会资本注册的默认角色code！");
				}
				SysRole sysRole = new SysRole();
				sysRole.setRolecode(roleCode);
				List<SysRole> roleList = sysRoleDao.findByExample(sysRole);
				if(roleList.size()!=1){
					throw new AppException("角色表中的社会资本注册对应角色配置不正确！");
				}
				SysUserRole ur = new SysUserRole();
				ur.setUserid(sysUser.getUserid());
				ur.setRoleid(roleList.get(0).getRoleid());
				userRoleDao.save(ur);
				
				/*修改状态*/
				t.setStatus("1");
				t.setAuditTime(DateUtil.getCurrentDateTime());
				t.setAuditUser(userid);
				ProSocialRegistDao.update(t);
				String messageContent = pcfg.findGeneralParamValue("SYSTEM", "MESSAGECONTENT");
				String newContent = messageContent.replaceAll("#usercode#",t.getUsercode() ).replaceAll("#userpsw#", t.getUserpswd());
				/*发送消息*/
				messageComponent.saveMessage(t.getSocialid(), t.getLinkphone(), newContent,"");
				
			}
		}
		
	}

	@Override
	public void refuseSocAud(Map map) throws Exception {
		// TODO Auto-generated method stub
		String socialids = map.get("socialid")==null?"":map.get("socialid").toString();
		String userid = String.valueOf(SecureUtil.getCurrentUser().getUserid());
		if(!"".equals(socialids)){
			String[] socialidss = socialids.split(",");
			for(int i=0;i<socialidss.length;i++){
				ProSocialRegist t = ProSocialRegistDao.get(Integer.parseInt(socialidss[i]));
				t.setStatus("2");
				t.setAuditTime(DateUtil.getCurrentDateTime());
				t.setAuditUser(userid);
				ProSocialRegistDao.update(t);
				/*发送消息*/
				messageComponent.saveMessage(t.getSocialid(), t.getLinkphone(), "您好，您在PPP项目管理系统中注册的社会资本账户未通过审核！","");
			}
		}
	}

	@Override
	public void delSocAud(Map map) throws Exception {
		// TODO Auto-generated method stub
		String socialids = map.get("socialids")==null?"":map.get("socialids").toString();
		String userid = String.valueOf(SecureUtil.getCurrentUser().getUserid());
		if(!"".equals(socialids)){
			String[] socialidss = socialids.split(",");
			for(int i=0;i<socialidss.length;i++){
				ProSocialRegist t = ProSocialRegistDao.get(Integer.parseInt(socialidss[i]));
				t.setStatus("3");
				t.setAuditTime(DateUtil.getCurrentDateTime());
				t.setAuditUser(userid);
				ProSocialRegistDao.update(t);
			}
		}
	}
	
}
