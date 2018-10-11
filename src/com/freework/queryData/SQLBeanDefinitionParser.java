package com.freework.queryData;


import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.freework.queryData.compileSQL.simpleSQL.SimpleCmpleSQL;

public class SQLBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
	 protected Class<SimpleCmpleSQL> getBeanClass(Element element) {  
	        return SimpleCmpleSQL.class;  
	 }  
	@Override
	 protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder){
        String sql=element.getFirstChild().getNodeValue();
        builder.addPropertyValue("sourceSql",sql);  
	}

	
}
