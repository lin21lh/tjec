package com.jbf.sys.paramCfg.component.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jbf.common.util.StringUtil;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.paramCfg.dao.SysParamCfgDao;
import com.jbf.sys.paramCfg.po.SysParamCfg;
/**
 * 查询系统配置参数实现类
 * @ClassName: ParamCfgComponentImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author MaQingShuang
 * @date 2015年5月13日 下午2:29:04
 */
@Component("sys.paramCfg.component.ParamCfgComponentImpl")
public class ParamCfgComponentImpl implements ParamCfgComponent {
	
	@Autowired
	SysParamCfgDao paramcfgDao;

	@Override
	public String findGeneralParamValue(String scenecode, String paramcode) {
		
		return findParamValue("0", scenecode, paramcode);
	}
	
	@Override
	public String findParamValue(String admivcode, String scenecode, String paramcode) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(SysParamCfg.class);
		if (StringUtil.isBlank(admivcode)) {
			admivcode = "0";
		}
		dc.add(Property.forName("admivcode").eq(Integer.valueOf(admivcode)));
		dc.add(Property.forName("scenecode").eq(scenecode.toUpperCase()));
		dc.add(Property.forName("paramcode").eq(paramcode.toUpperCase()));
		
		List<SysParamCfg> paramCfgList = (List<SysParamCfg>)paramcfgDao.findByCriteria(dc);
		if (paramCfgList.isEmpty()) { //参数编码为空 如果不是查询通用配置，则再查通用配置参数
			if (!"0".equals(admivcode)) {
				dc = DetachedCriteria.forClass(SysParamCfg.class);
				dc.add(Property.forName("admivcode").eq(0));
				dc.add(Property.forName("scenecode").eq(scenecode.toUpperCase()));
				dc.add(Property.forName("paramcode").eq(paramcode.toUpperCase()));
				
				paramCfgList = (List<SysParamCfg>)paramcfgDao.findByCriteria(dc);
			} else {
				return null;
			}
			if (!paramCfgList.isEmpty()) {
				return paramCfgList.get(0).getParamvalue();
			} else {
				return null;
			}
		} else {
			return paramCfgList.get(0).getParamvalue();
		}
	}

}
