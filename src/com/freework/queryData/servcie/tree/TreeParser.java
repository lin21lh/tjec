package com.freework.queryData.servcie.tree;


import javax.annotation.Resource;

import com.freework.base.util.unmodifiableMap.UnmodifiableKeyMap;
import com.freework.freedbm.Cfg;
import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.freedbm.dao.jdbcm.map.dto.MapFieldInfo;
import com.freework.freedbm.dao.jdbcm.map.dto.MapQuery;
import com.freework.queryData.compileSQL.CompileSQLFactory;
import com.freework.queryData.compileSQL.simpleSQL.SimpleCmpleSQL;
import com.freework.queryData.servcie.QueryConfig;
import com.freework.queryData.servcie.tree.TreeParserFactory.TreeInfo;

public class TreeParser extends AbstractTreeParser {
	private String tableName=null;
	private String where=null;
	private String orderBy=null;
	private String otherColStr=null;
	
	

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public void setOtherColStr(String otherColStr) {
		this.otherColStr = otherColStr;
	}
	public void setInfo(TreeInfo info) {
		info.setIdColInt(0);
		info.setParentColInt(3);
		this.info = info;
	}
	
	public void addWhere(StringBuilder sql,String where,String parent,String id){
		sql.append("  where 1=1 [if(isUp!=true&(isWhere!=1)) and (").append(parent).append("={id}").append(" )]");
		if(where!=null&&!where.equals("")){
			sql.append("[if(isUp!=true) and (").append(where).append(")]");
		}
		sql.append("[if(isUp=true) and (").append(id).append("={my_id} ) ]");
	}
	
	
	
	public String createSQL(String tableName,String id,String isParentCol,String[] otherCol,String parent,String name,String where,String orderBy){
		StringBuilder sql=new StringBuilder("select ");
		sql.append(id).append(", ")
		   .append(name).append(", ");
		if(isParentCol==null||isParentCol.equals("")){
			sql.append("( select count(*) from  ").append(tableName).append(" my_tmp where my_tmp.");
			sql.append(parent).append("=t.");
			sql.append(id);
				if(where!=null&&!where.equals("")){
					
					sql.append(" and ").append(where);
				}
			sql.append("  ) as is_parent,");
		}else{
			sql.append(isParentCol).append(" as is_parent, ");
		}
		sql.append(parent);
		if(otherCol!=null){
			for (String col : otherCol) {
				sql.append(" , ").append(col);
			}
		}
		sql.append(" from ").append(tableName).append(" t ");
		addWhere(sql,where,parent,id);
		if(orderBy!=null&&!orderBy.equals("")){
			sql.append(" order by ").append(orderBy);
		}
		//System.out.println(sql);
		System.out.println(sql);
		return sql.toString();
	}
	
	public  TableQuery getMapQueryInfo(String otherCols[]) {
		int otherColSize=otherCols==null?0:otherCols.length;
		JdbcForDTO[] fields=new JdbcForDTO[4+otherColSize];
		fields[0]=new MapFieldInfo(0, "id", info.getIdCol(), Cfg.String, true, false, null, null);
		fields[1]=new MapFieldInfo(1, "name", info.getNameCol(), Cfg.String, true, false, null, null);
		fields[2]=new MapFieldInfo(2, "isParent", "is_parent", Cfg.Integer, true, false, null, null){
			public void setValue(Object obj, Object Value) {
				((UnmodifiableKeyMap)obj).putIndex(2, ((Integer)Value)>0);
			}
		};
		fields[3]=new MapFieldInfo(3, "pid", info.getParentCol(), Cfg.String, true, false, null, null);
		int i=4;
		if(otherCols!=null){
			for (String otherCol : otherCols) {
				fields[i]=new MapFieldInfo(i, otherCol, otherCol, Cfg.String, true, false, null, null);
				i++;
			}
		}
		MapQuery queryInfo = new MapQuery(null, fields);
		queryInfo.setName("children");
		queryInfo.setName("open");
		return queryInfo;
	}
	@Override
	public QueryConfig queryConfig() {
		String otherCol[]=null;
		if(otherColStr!=null&&!otherColStr.equals("")){
			otherCol=otherColStr.split(",");
		}
		String sql=this.createSQL(tableName, info.getIdCol(), info.getIsParentCol(), otherCol, info.getParentCol(), info.getNameCol(), where, orderBy);
		QueryConfig queryConfig=new QueryConfig();
		queryConfig.setSql(sqlFactory.getCompileSQL(sql));
		queryConfig.setSingletonQuery(getMapQueryInfo(otherCol));
		queryConfig.setUserData(info);
		return queryConfig;
	}

	@Override
	protected String getQueryServiceName() {
		// TODO Auto-generated method stub
		return "treeService";
	}



	

}
