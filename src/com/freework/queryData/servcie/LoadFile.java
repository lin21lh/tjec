package com.freework.queryData.servcie;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

public class LoadFile implements InitializingBean {
	private IQueryConfigParser parser;
	private Resource configLocation;
	private QueryService defaultQueryService = null;
	private boolean isDebug = false;
	private String prefix = "";
	private Map<String, QueryConfig> map;
	private long lastModified = 0;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public boolean isPrefix(String key) {
		if (key == null || key.length() == 0)
			return false;
		
			return key.startsWith(prefix + ".");

	}

	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public void setDefaultQueryService(QueryService defaultQueryService) {
		this.defaultQueryService = defaultQueryService;
	}

	public void setParser(IQueryConfigParser parser) {
		this.parser = parser;
	}

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	public void load(File file) throws IOException {
		map = parser.loadExceSQL(file);
		Collection<QueryConfig> values = map.values();
		for (QueryConfig queryConfig : values) {
			if (queryConfig.getExecute() == null)
				queryConfig.setExecute(defaultQueryService);
		}
	}
	
	private void debugLoad(){
		if (isDebug) {
			try {
				File file = configLocation.getFile();
				if (lastModified < file.lastModified()) {
					load(file);
					lastModified=file.lastModified();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public QueryConfig get(String id) {
		debugLoad();
		if (prefix.length() == 0) {
			return map.get(id);
		} else {
			if (isPrefix(id)) {
				return map.get(id.substring(prefix.length()+1));
			}else{
				return null;
			}
		}
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		if(configLocation==null)
			return;
		File file = configLocation.getFile();
		if (isDebug) {
			lastModified = file.lastModified();
		}
		load(file);

	}

}
