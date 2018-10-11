package com.freework.base.cache;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import com.freework.base.util.FileUtil;
public class LoaderCacheResource   implements  FactoryBean<ByteArrayResource>,  InitializingBean{
	
	private ResourcePatternResolver resourceLoader;
	private String path;
	private ByteArrayResource resource;
	@Autowired
	public void setResourceLoader(ResourcePatternResolver resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Resource[] resource=resourceLoader.getResources(path);
		StringBuilder strBuf=new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><ehcache>");
		for (Resource resource2 : resource) {
			String str=FileUtil.readTxtFile(resource2.getInputStream(), "UTF-8");
			int startIndex=str.indexOf("<ehcache>")+9;
			int endIndex=str.lastIndexOf("</ehcache>");
			strBuf.append(str.substring(startIndex, endIndex));
		}
		strBuf.append("</ehcache>");
		this.resource=new ByteArrayResource(strBuf.toString().getBytes( "UTF-8"));
	}

	@Override
	public ByteArrayResource getObject() throws Exception {
		return resource;
	}

	@Override
	public Class<?> getObjectType() {
		return ByteArrayResource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
	

}
