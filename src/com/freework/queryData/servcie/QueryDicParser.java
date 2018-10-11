package com.freework.queryData.servcie;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Attribute;
import org.dom4j.Element;

import com.freework.base.util.XMLUtil;
import com.freework.base.util.unmodifiableMap.KeysIndex;
import com.freework.base.util.unmodifiableMap.UnmodifiableKeyMap;
import com.freework.freedbm.Cfg;
import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.jdbcm.map.dto.MapFieldInfo;
import com.freework.freedbm.dao.jdbcm.map.dto.MapQuery;
import com.freework.queryData.compileSQL.CompileSQLFactory;
import com.freework.queryData.compileSQL.simpleSQL.SimpleCmpleSQL;


public class QueryDicParser implements IQueryConfigParser {
	
	private CompileSQLFactory sqlFactory;
	@Resource(name="freework.queryData.sqlFactory")
	public void setSqlFactory(CompileSQLFactory sqlFactory) {
		this.sqlFactory = sqlFactory;
	}

	public String getValue(Element element,String name){
		Attribute attribute=element.attribute(name);
		if(attribute!=null){
			return attribute.getStringValue();
		}else{
			Element e=element.element(name);
			if(e==null)
				return "";
			else
				return e.getText();
		}
		
	}
	public  TableQuery getMapQueryInfo(String id,String code,String name) {
		MapFieldInfo[] field ={
		new MapFieldInfo(0,"id", id,Cfg.String, true, false, null, ""),
		new MapFieldInfo(1,"code",code,Cfg.String, true, false, null, ""),
		new MapFieldInfo(2,"name", name,Cfg.String, true, false, null, "")
		};
		MapQuery queryInfo = new MapQuery(null, field);
		return queryInfo;
	}
	private  QueryConfig loadItems(String[] cols,List<Element> items){
		
		List<Map> list=new ArrayList<Map>(items.size());
		KeysIndex keys=new KeysIndex(cols);
		for (Element element : items) {
			UnmodifiableKeyMap<String> map=new UnmodifiableKeyMap<String>(keys);
			for (int i = 0; i < cols.length; i++) {
				map.putIndex(i, this.getValue(element, cols[i]));
			}
			list.add(map);
		}
		
		QueryConfig cfg = new QueryConfig();
		cfg.setExecute(new DicItemQueryService(list));
		return cfg;
		
		
		
	}
	
	
	@Override
	public Map<String, QueryConfig> loadExceSQL(File file) throws IOException {
		Element root = XMLUtil.readDocumentFromFile(file, "UTF-8").getRootElement();
		List<Element> elements = root.elements();
		Map<String, QueryConfig> map = new HashMap<String, QueryConfig>();
		for (Element element2 : elements) {
			
			
			List<Element> items=element2.elements("item");
			String cols=this.getValue(element2, "cols");
			String id = element2.attributeValue("id");

			if(items.size()!=0&&cols!=null){
				map.put(id, loadItems(cols.split(","),items));
			}else{
			
			
			
				String sql=getValue(element2,"sql");
				String where=getValue(element2,"where");
				String idCol=getValue(element2,"key");
				String code=getValue(element2,"code");
				String name=getValue(element2,"name");
				String tableName=null;
				if(sql!=null&&!sql.equals("")){
					tableName="("+sql+") dic";
				}else{
					tableName=getValue(element2,"table");
					
				}
				StringBuilder	sqlBuf=new StringBuilder().append("select ")
						.append(idCol).append(",")
						.append(code).append(",")
						.append(name)
						.append(" from ").append(tableName);
				if(where!=null&&!where.equals(""))
						sqlBuf.append(" where ").append(where).toString();
				QueryConfig cfg = new QueryConfig();
				cfg.setSingletonQuery(getMapQueryInfo(idCol,code,name));
				cfg.setSql(sqlFactory.getCompileSQL(sqlBuf.toString()));
				cfg.setUserData(tableName);
				map.put(id, cfg);
			}
			
		}
		return map;

	}

}
