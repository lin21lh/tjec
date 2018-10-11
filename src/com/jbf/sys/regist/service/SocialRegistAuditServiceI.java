package com.jbf.sys.regist.service;

import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.sys.user.po.SysUser;

public interface SocialRegistAuditServiceI {
	
	public PaginationSupport qrySocAud(Map map);
	public void approveSocAud(Map map) throws Exception;
	public void refuseSocAud(Map map) throws Exception;
	public void delSocAud(Map map) throws Exception;

}
