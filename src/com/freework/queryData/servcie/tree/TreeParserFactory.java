package com.freework.queryData.servcie.tree;

import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Attribute;
import org.dom4j.Element;

import com.freework.queryData.compileSQL.CompileSQLFactory;
import com.freework.queryData.servcie.QueryConfig;
import com.freework.queryData.servcie.QueryService;

public class TreeParserFactory {
	Map<String,QueryService> treeServices;
	private CompileSQLFactory sqlFactory;

	@Resource(name="freework.queryData.sqlFactory")
	public void setSqlFactory(CompileSQLFactory sqlFactory) {
		this.sqlFactory = sqlFactory;
	}

	
	public void setTreeServices(Map<String,QueryService> treeServices) {
		this.treeServices = treeServices;
	}

	public String getValue(Element element, String name) {
		Attribute attribute = element.attribute(name);
		if (attribute != null) {
			return attribute.getStringValue();
		} else {
			Element e = element.element(name);
			if (e == null)
				return null;
			else
				return e.getText();
		}

	}

	public  QueryConfig queryConfig(Element element){
		AbstractTreeParser parser=	getTreeParser(element);
		if(parser!=null){
			QueryConfig query=parser.getConfig(treeServices);
			return query;
		}else{
			throw new RuntimeException("not element:"+element.toString());
		}
	}

	
	
	public AbstractTreeParser getTreeParser(Element element) {

		String idCol = getValue(element, "key");
		String nameCol = getValue(element, "name");
		String parentCol = getValue(element, "parent");
		String isParentCol = getValue(element, "isParent");
		TreeInfo treeInfo = new TreeInfo(idCol, nameCol, parentCol, isParentCol);

		String sql = getValue(element, "sql");
		String table = getValue(element, "table");
		AbstractTreeParser treeParse=null;
		if(sql!=null){
			
			
			 treeParse=new TreeParserSQL();
			 treeParse.setSqlFactory(sqlFactory);
			 treeParse.setInfo(treeInfo);
			 treeParse.setSQL(sql);

		} else if(table!=null){
			String otherColStr = getValue(element, "otherCol");
			String where = getValue(element, "where");
			String orderBy = getValue(element, "orderBy");
			String checkTable = getValue(element, "checkTable");
			
			TreeParser	treeParseTmp;
			if(checkTable!=null){
				treeParseTmp=new TreeCheckParser();
				treeParseTmp.setSqlFactory(sqlFactory);

				String checkKey = getValue(element, "checkKey");
				String checkWhere = getValue(element, "checkWhere");
				((TreeCheckParser)treeParseTmp).setCheckTable(checkTable);
				((TreeCheckParser)treeParseTmp).setCheckKey(checkKey);
				((TreeCheckParser)treeParseTmp).setCheckWhere(checkWhere);

			}else{
				treeParseTmp=new TreeParser();
				treeParseTmp.setSqlFactory(sqlFactory);
			}
			treeParseTmp.setInfo(treeInfo);
			treeParseTmp.setOrderBy(orderBy);
			treeParseTmp.setOtherColStr(otherColStr);
			treeParseTmp.setTableName(table);
			treeParseTmp.setWhere(where);
			treeParse=treeParseTmp;
		}
		
		
		return treeParse;

	}
	public class TreeInfo{
		private String idCol="";
		private String nameCol="";
		private String parentCol="";
		private String isParentCol="";

		private int idColInt;
		private int parentColInt;
		
		
		public TreeInfo(String idCol, String nameCol, String parentCol,
				String isParentCol) {
			this.idCol = idCol;
			this.nameCol = nameCol;
			this.parentCol = parentCol;
			this.isParentCol = isParentCol;
		}
		public String getIdCol() {
			return idCol;
		}
		public void setIdCol(String idCol) {
			this.idCol = idCol;
		}
		public String getNameCol() {
			return nameCol;
		}
		public void setNameCol(String nameCol) {
			this.nameCol = nameCol;
		}
		public String getParentCol() {
			return parentCol;
		}
		public void setParentCol(String parentCol) {
			this.parentCol = parentCol;
		}
		public String getIsParentCol() {
			return isParentCol;
		}
		public void setIsParentCol(String isParentCol) {
			this.isParentCol = isParentCol;
		}
		public int getIdColInt() {
			return idColInt;
		}
		public void setIdColInt(int idColInt) {
			this.idColInt = idColInt;
		}
		
		public int getParentColInt() {
			return parentColInt;
		}
		public void setParentColInt(int parentColInt) {
			this.parentColInt = parentColInt;
		}
	
		
	}
}
