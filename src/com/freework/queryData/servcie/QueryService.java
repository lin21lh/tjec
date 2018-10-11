package com.freework.queryData.servcie;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import com.freework.freedbm.cfg.tablecfg.TableQuery;

public interface QueryService {
	public <T> List<T> query(QueryConfig cfg,Object whereValue,Class<T> clazz);
	public TableQuery getQueryInfo(QueryConfig cfg,ResultSetMetaData rsmd,Class clazz)throws SQLException;
	
}
