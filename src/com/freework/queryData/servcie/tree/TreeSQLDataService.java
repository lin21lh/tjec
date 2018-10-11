package com.freework.queryData.servcie.tree;

import java.sql.ResultSetMetaData;

import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.freedbm.dao.jdbcm.map.dto.MapQuery;
import com.freework.queryData.servcie.QueryConfig;
import com.freework.queryData.servcie.tree.TreeParserFactory.TreeInfo;

public class TreeSQLDataService extends TreeDataService {
	@Override
	public TableQuery getQueryInfo(QueryConfig cfg, ResultSetMetaData rsmd,
			Class clazz) {
	
			TableQuery query=super.getQueryInfo(cfg, rsmd, clazz);
			TreeInfo treeInfo =(TreeInfo) cfg.getUserData();
			JdbcForDTO[] fields=query.getFields();
			int j=0;
			for (int i = 0; i < fields.length; i++) {
				if(treeInfo.getIdCol().equals(fields[i].getName())){
					treeInfo.setIdColInt(i);
					j++;
				}else if(treeInfo.getParentCol().equals(fields[i].getName())){
					treeInfo.setParentColInt(i);
					j++;
				}
				if(j==2){
					break;
				}
			}
			if(query instanceof MapQuery ){
				((MapQuery)query).setName("children");
			}
			
			return query;
	
	}
}
