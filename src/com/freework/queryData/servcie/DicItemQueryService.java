package com.freework.queryData.servcie;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.freework.freedbm.cfg.tablecfg.TableQuery;

public class DicItemQueryService implements QueryService{
	List<Map> list=null;
	
	public DicItemQueryService(List<Map> list){
		this.list = list;
		
	}
	public void setList(List<Map> list) {
		this.list = list;
	}
	@Override
	public <T> List<T> query(QueryConfig cfg, Object whereValue, Class<T> clazz) {
		return (List<T>) list;
	}

	@Override
	public TableQuery getQueryInfo(QueryConfig cfg, ResultSetMetaData rsmd,
			Class clazz) throws SQLException {
		return null;
	}



}
