package com.jbf.sys.paramCfg.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.log.service.impl.SysLogApp;
import com.jbf.sys.paramCfg.dao.SysParamCfgDao;
import com.jbf.sys.paramCfg.po.SysParamCfg;
import com.jbf.sys.paramCfg.service.SysParamCfgService;

/**
 * 系统配置参数Service实现类
 * @ClassName: SysParamCfgServiceImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author MaQingShuang
 * @date 2015年5月13日 下午2:33:26
 */
@Scope("prototype")
@Service
public class SysParamCfgServiceImpl implements SysParamCfgService {

	@Autowired
	SysParamCfgDao sysParamCfgDao;

	@Override
	public PaginationSupport query(String admivcode, String scenecode, String paramcode, String paramname, String status, Integer pageSize, Integer pageIndex) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(SysParamCfg.class);
		if (StringUtil.isNotBlank(admivcode)) {
			dc.add(Property.forName(admivcode).eq(admivcode));
		}
		if (StringUtil.isNotBlank(scenecode)) {
			dc.add(Property.forName("scenecode").eq(scenecode.toUpperCase()));
		}
		if (StringUtil.isNotBlank(paramcode)) {
			dc.add(Property.forName("paramcode").like(paramcode.toUpperCase(), MatchMode.END));
		}
		if (StringUtil.isNotBlank(paramname)) {
			dc.add(Property.forName("paramname").like(paramname, MatchMode.ANYWHERE));
		}
		if (StringUtil.isNotBlank(status)) {
			dc.add(Property.forName("status").eq(Integer.valueOf(status)));
		}
		return sysParamCfgDao.findByCriteria(dc, pageSize, pageIndex);
	}

	@Override
	public boolean addParam(HttpServletRequest request, SysParamCfg sysParamCfg) {
		// TODO Auto-generated method stub
		
		sysParamCfgDao.save(sysParamCfg);
		StringBuffer opermessage = new StringBuffer("新增配置参数:");
		if (sysParamCfg.getAdmivcode() != 0) {
			opermessage.append("admivcode=").append(sysParamCfg.getAdmivcode()).append(",");
		}
		opermessage.append("scenecode=").append(sysParamCfg.getScenecode()).append(",");
		opermessage.append("paramcode=").append(sysParamCfg.getParamcode()).append(",");
		opermessage.append("paramcode=").append(sysParamCfg.getParamcode()).append(",");
		opermessage.append("status=").append(sysParamCfg.getStatus()).append("。");
		
		SysLogApp.writeLog(opermessage.toString(), 3);
		return true;
	}

	@Override
	public boolean editParam(HttpServletRequest request, SysParamCfg sysParamCfg) {
		// TODO Auto-generated method stub
		
		SysParamCfg dbParamCfg = sysParamCfgDao.get(sysParamCfg.getParamid());
		sysParamCfgDao.update(sysParamCfg);
		StringBuffer opermessage = new StringBuffer("修改配置参数:");
		if (sysParamCfg.getAdmivcode() != dbParamCfg.getAdmivcode()) {
			opermessage.append("admivcode原值为[").append(dbParamCfg.getAdmivcode()).append("],修改为[").append(sysParamCfg.getAdmivcode()).append("]；");
		}
		if (!sysParamCfg.getScenecode().equals(dbParamCfg.getScenecode())) {
			opermessage.append("scenecode原值为[").append(dbParamCfg.getScenecode()).append("],修改为[").append(sysParamCfg.getScenecode()).append("]；");
		}
		if (!sysParamCfg.getParamcode().equals(dbParamCfg.getParamcode())) {
			opermessage.append("paramcode原值为[").append(dbParamCfg.getParamcode()).append("],修改为[").append(sysParamCfg.getParamcode()).append("]；");
		}
		if (!sysParamCfg.getParamname().equals(dbParamCfg.getParamname())) {
			opermessage.append("paramname原值为[").append(dbParamCfg.getParamname()).append("],修改为[").append(sysParamCfg.getParamname()).append("]；");
		}
		if (!sysParamCfg.getParamvalue().equals(dbParamCfg.getParamvalue())) {
			opermessage.append("paramvalue原值为[").append(dbParamCfg.getParamvalue()).append("],修改为[").append(sysParamCfg.getParamvalue()).append("]；");
		}
		if (!sysParamCfg.getParamdesc().equals(dbParamCfg.getParamdesc())) {
			opermessage.append("paramdesc原值为[").append(dbParamCfg.getParamdesc()).append("],修改为[").append(sysParamCfg.getParamdesc()).append("]；");
		}
		if (sysParamCfg.getStatus() != dbParamCfg.getStatus()) {
			opermessage.append("status原值为[").append(dbParamCfg.getStatus()).append("],修改为[").append(sysParamCfg.getStatus()).append("]；");
		}
		
		SysLogApp.writeLog(opermessage.toString(), 3);
		return true;
	}

	@Override
	public boolean deleteParam(HttpServletRequest request, Long paramid) {
		// TODO Auto-generated method stub
		
		SysParamCfg sysParamCfg = sysParamCfgDao.get(paramid);
		SysLogApp.writeLog("删除配置参数：admivcode=" + sysParamCfg.getAdmivcode() + ",scenecode=" 
		+ sysParamCfg.getScenecode() + ",paramcode=" + sysParamCfg.getParamcode() + "。", 3);
		sysParamCfgDao.delete(sysParamCfg);
		return true;
	}
	
	
}
