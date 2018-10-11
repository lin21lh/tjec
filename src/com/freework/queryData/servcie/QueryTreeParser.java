package com.freework.queryData.servcie;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.freework.base.util.XMLUtil;
import com.freework.queryData.servcie.tree.TreeParserFactory;

public class QueryTreeParser implements IQueryConfigParser {
	TreeParserFactory factory=null;
	
	
	public void setFactory(TreeParserFactory factory) {
		this.factory = factory;
	}


	@Override
	public Map<String, QueryConfig> loadExceSQL(File file) throws IOException {
		Element root = XMLUtil.readDocumentFromFile(file, "UTF-8").getRootElement();
		List<Element> elements = root.elements();
		
		Map<String, QueryConfig> map = new HashMap<String, QueryConfig>();
		for (Element element : elements) {
			String id=element.attributeValue("id");
				map.put(id, factory.queryConfig(element));
				
		}
		
		
		
		
		return map;
	}

	

}
