/************************************************************
 * 类名：CalculateDatascopeComponentImpl
 *
 * 类别：组件实现类
 * 功能：计算数据权限
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-4-01  CFIT-PM   maqs         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.datascope.component.impl;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jbf.base.datascope.component.CalculateDatascopeComponent;
import com.jbf.base.datascope.component.DatascopeComponent;
import com.jbf.base.datascope.po.SysDatascopeitem;
import com.jbf.base.datascope.po.SysDatascopemain;
import com.jbf.base.datascope.po.SysRoleUserResDscope;
import com.jbf.common.TableNameConst;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.log.service.impl.SysLogApp;
import com.jbf.sys.resource.dao.SysResourceDao;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.role.dao.SysRoleDao;
import com.jbf.sys.role.po.SysRole;
import com.jbf.sys.user.dao.SysUserDao;
import com.jbf.sys.user.dao.SysUserRoleDao;
import com.jbf.sys.user.po.SysUser;


@Scope("prototype")
@Component("base.datascope.component.CalculateDatascopeComponent")
public class CalculateDatascopeComponentImpl implements CalculateDatascopeComponent {

	@Autowired
	SysUserDao userDao;
	@Autowired
	SysRoleDao roleDao;
	@Autowired
	SysResourceDao resourceDao;
	
	@Autowired
	SysUserRoleDao userRoleDao;
	@Autowired
	DatascopeComponent datascopeComponent;
	
    public StringBuffer calculateDataRight(Long resourceid, Long userid, String tableAlias, String tablecode, int scopeType) throws AppException {
    	SysUser user = userDao.get(userid);
    	if (user != null && user.getUsertype().equals(Byte.valueOf("2")))
    		return new StringBuffer("(1=1)");
    	
    	List list = getScopeMainID(resourceid, userid);
    	StringBuffer sb = new StringBuffer();
    	// String elementcode;
    	String datascope;
    	boolean writeLog = false;
    	String opermessage = "";
    	if (list != null && list.size() > 1) {
    		writeLog = true;
    		SysResource resource = resourceDao.get(resourceid);
    		opermessage = "用户：" + user.getUsercode() + "针对同一菜单功能[" + resource.getName() + "]，不同的角色被授予不同的数据权限。对应关系：";
    	}
    	
    	for(Iterator it = list.iterator(); it.hasNext(); andString(sb, datascope)) {
    		SysRoleUserResDscope roleUserResDscope = (SysRoleUserResDscope)it.next();
    		if (writeLog) {
    			SysRole role = roleDao.get(roleUserResDscope.getRoleid());
    			opermessage += "\n 角色为" + role.getRolecode() + "-" + role.getRolename() + "被授予数据权限名称为：" + datascopeComponent.getDatascopemainName(roleUserResDscope.getScopemainid())+ "；";
    		}
            datascope = datascopeComponent.getDynamicSQLExpression(roleUserResDscope.getScopemainid(), tableAlias, tablecode, userid, scopeType);
        }
    	
    	if (writeLog) {
    		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 	
    		SysLogApp.writeLog(request, opermessage, 4);
    	}

        if(sb.length() > 0)
            sb.insert(0, " ( ").append(" ) ");
        return sb;
    }
    
    public String calDataRightByElementcode(Long resourceid, String elementcode, Long userid, String tableAlias, String tablecode, int scopeType) throws AppException {
    	SysUser user = userDao.get(userid);
    	if (user != null && user.getUsertype().equals(Byte.valueOf("2")))
    		return "(1=1)";
    	
    	List list = getScopeMainID(resourceid, userid);
    	String datascope = "";
    	Iterator it = list.iterator();
    	while (it.hasNext()) {
			SysRoleUserResDscope roleUserResDscope = (SysRoleUserResDscope)it.next();
			if (datascope.length() > 0)
				datascope += " or ";
			datascope += datascopeComponent.getConditionByElementcode(roleUserResDscope.getScopemainid(), elementcode, tableAlias, tablecode, userid, scopeType);
		}
    	
        return datascope;
    }
    
    private List getScopeMainID(Long resourceid, Long userid) {
    	StringBuffer query = new StringBuffer();
    	query.append("select t.* from " + TableNameConst.SYS_ROLE_USER_RES_DSCOPE + " t where  (");
    	query.append(" (t.isallmenu=1 and (t.isalluser=1 or  (t.isalluser=0 and t.userid =").append(userid).append(" ))) ");
    	if(resourceid > 0) {
    		query.append(" or ( (t.isalluser=1 or (t.isalluser=0 and t.userid =").append(userid);
    		query.append(" )) and t.isallmenu=0 and t.resourceid =").append(resourceid).append(")");
    	} else {
    		query.append(" or ( (t.isalluser=1 or (t.isalluser=0 and t.userid =").append(userid);
    		query.append(" )) and t.isallmenu=0 )");
    	}
        query.append(" )  and  t.scopemainid>0 and t.roleid in (");
        List<SysRole> roles = userRoleDao.getRolesByUserid(userid);
        for (SysRole role : roles) {
        	query.append(role.getRoleid()).append(",");
        }
        query.append(" 0)");
        
        if (resourceid > 0L)
        	query.append(" and exists (select trum.roleid from " + TableNameConst.SYS_ROLE_USER_RES_DSCOPE + " trum where trum.roleid=t.roleid and trum.resourceid =").append(resourceid).append(")");

        List list = userRoleDao.findVoBySql(query.toString(), SysRoleUserResDscope.class);
        return list;
    }
    
    public boolean isHasDataRight(Long resourceid, Long userid, String elementcode) {
    	
    	List<SysRoleUserResDscope> list = getScopeMainID(resourceid, userid);
    	if (list != null && list.size() > 0) {
    		if (StringUtil.isBlank(elementcode)) {
    			return true;
    		} else {
    			List<SysDatascopeitem> itemList = null;
    			for (SysRoleUserResDscope dsMain : list) {
    				itemList = datascopeComponent.findDatascopeItemList(dsMain.getScopemainid());
    				if (itemList.isEmpty())
    					continue;
    				
    				for (SysDatascopeitem item : itemList) {
    					if (elementcode.equalsIgnoreCase(item.getElementcode()))
    						return true;
    				}
    			}
    			return false;
    		}
    	} else {    		
    		return false;
    	}
    }
    
    public List<SysRoleUserResDscope> findRoleUserResDscopeList(Long resourceid, Long userid) {
    	
    	return getScopeMainID(resourceid, userid);
    }
    
    public StringBuffer andString(StringBuffer sb, String datascope) {
    	
    	if (sb.length() > 0)
    		sb.append(" or ");
    	return sb.append(datascope);
    }
    
    public SysRole getRoleByRoleID(Long roleid) {
    	return roleDao.get(roleid);
    }
 
}
