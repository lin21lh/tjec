package com.freework.queryData;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class TagsNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
        registerBeanDefinitionParser("sql", new SQLBeanDefinitionParser());  

	}

	

}
