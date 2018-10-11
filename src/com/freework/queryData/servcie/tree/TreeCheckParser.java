package com.freework.queryData.servcie.tree;

import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.jdbcm.map.dto.MapQuery;

public class TreeCheckParser extends TreeParser {

	private String checkTable =null;
	private String checkKey =null;
	private String checkWhere =null;

	public void setCheckTable(String checkTable) {
		this.checkTable = checkTable;
	}

	public void setCheckKey(String checkKey) {
		this.checkKey = checkKey;
	}

	public void setCheckWhere(String checkWhere) {
		this.checkWhere = checkWhere;
	}
	@Override
	protected String getQueryServiceName() {
		return "treeCheckService";
	}
	public  TableQuery getMapQueryInfo(String otherCols[]) {
		MapQuery queryInfo =(MapQuery) super.getMapQueryInfo(otherCols);
		queryInfo.setName("checked");
	return queryInfo;
	}
	public void addWhere(StringBuilder sql,String where,String parent,String id){
		sql.append("  where 1=1" +
				  " [if(isParents!=true&isUp!=true&isWhere!=1&id!='0') and (")
		.append(parent).append("={id}").append(" )]");
		
		sql.append("[if(isParents!=true&isUp!=true&id='0') and ")
		.append(id).append(" in ( select ").append(checkKey).append(" from ")
		.append(checkTable).append(" where ").append(checkWhere).append(") ]");
		
		if(where!=null&&!where.equals("")){
			sql.append("[if(isUp!=true) and (").append(where).append(")]");
		}
		sql.append("[if(isRoot=true) or (").append(parent).append("=0 ) ]");
		sql.append("[if(isUp=true) and (").append(id).append("={my_id} ) ]");
		sql.append("[if(isParents=true&isUp!=true) and (([foreach(parentIds,or)  ").append(parent).append("={item}])  and  ([foreach(itemIds,and)  ").append(id).append("<>{item}]) ) ]");
	
	}

}
