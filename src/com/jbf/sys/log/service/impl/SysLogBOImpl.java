
package com.jbf.sys.log.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbf.common.security.SecureUtil;
import com.jbf.sys.log.dao.SysLogDao;
import com.jbf.sys.log.service.SysLogBO;
import com.jbf.sys.user.po.SysUser;

@Scope("prototype")
@Component("com.jbf.sys.log.service.impl.SysLogBOImpl")
public class SysLogBOImpl implements SysLogBO {

	@Autowired
	SysLogDao logDao;
	
	@Override
	public void writeLog(HttpServletRequest request, String opermessage, Integer opertype) {
		
		SysUser currentUser = SecureUtil.getCurrentUser();
		
		String usercode = null;
		if (currentUser != null)
			usercode = currentUser.getUsercode();
		
		writeLog(request, opermessage, usercode, opertype);
	}
	
	public void writeLog(HttpServletRequest request, String opermessage, String usercode, Integer opertype) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) 
			ip = request.getHeader("Proxy-Client-IP");
		
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) 
			ip = request.getHeader("WL-Proxy-Client-IP");
		
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) 
			ip = request.getRemoteAddr();
		
		String useragentinfo = request.getHeader("User-Agent");
		
		logDao.writeLog(usercode, ip, opermessage, useragentinfo, opertype);
	}
}
