package com.jbf.common.security.datasource.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jbf.common.security.datasource.MultiDataSource;
import com.jbf.common.util.WebContextFactoryUtil;

@Component("datasourceService")
public class DataSourceServiceImpl implements DataSourceService {

	public List<Map> getAllDataSource() {
		MultiDataSource datasource = (MultiDataSource)WebContextFactoryUtil.getBean("dataSource");
		Iterator it = datasource.getDataSources().keySet().iterator();
		List list = new ArrayList();
		String key = null;
		Map map = null;
		for (; it.hasNext(); list.add(map)) {
			key = (String)it.next();
			String values[] = key.split(";");
			if (values != null && values.length >=2) {
				map = new HashMap();
				map.put("year", values[0]);
				map.put("displayDSName", values[1]);
			} else {
				//数据源配置错误,格式为[年度;显示数据源名称]
				//throw new AppException();
			}
			map.put("all", key);
		}
		return list;
	}
	
	public boolean isEnabledMultiDataSource() {
		Object object = WebContextFactoryUtil.getBean("dataSource");
		if (object != null && object instanceof MultiDataSource)
			return true;
		else
			return false;
	}

}
