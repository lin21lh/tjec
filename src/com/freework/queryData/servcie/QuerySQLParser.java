package com.freework.queryData.servcie;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Element;

import com.freework.base.util.XMLUtil;
import com.freework.queryData.compileSQL.CompileSQLFactory;
import com.freework.queryData.compileSQL.simpleSQL.SimpleCmpleSQL;

public class QuerySQLParser implements IQueryConfigParser {
	private CompileSQLFactory sqlFactory;
	@Resource
	public void setSqlFactory(CompileSQLFactory sqlFactory) {
		this.sqlFactory = sqlFactory;
	}
	@Override
	public Map<String, QueryConfig> loadExceSQL(File file) throws IOException {
		Element root = XMLUtil.readDocumentFromFile(file, "UTF-8").getRootElement();
		List<Element> elements = root.elements();
		Map<String, QueryConfig> map = new HashMap<String, QueryConfig>();
		for (Element element2 : elements) {
			String sql = element2.getText();
			String id = element2.attributeValue("id");
			QueryConfig cfg = new QueryConfig();
			cfg.setSql(sqlFactory.getCompileSQL(sql));
			map.put(id, cfg);
		}
		return map;

	}

}
