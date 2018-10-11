package com.freework.queryData.servcie;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.util.TotalResult;
import com.freework.queryData.dao.BuilerQuery;
import com.freework.queryData.dao.QueryDao;
import com.freework.queryData.dao.QueryPageDao;

public class DefaultQueryService implements QueryService {
	private QueryDao dao;
	private QueryPageDao pageDao;
	public void setDao(QueryDao dao) {
		this.dao = dao;
	}
	
	
	public void setPageDao(QueryPageDao pageDao) {
		this.pageDao = pageDao;
	}

	public <T> TotalResult<T> queryPage(QueryConfig cfg, Object whereValue,
			Class<T> clazz, int start,  int limit) {
		return pageDao.query(whereValue,cfg,clazz,start,limit);
	}
	
	public <T> TotalResult<T> queryPageForGeneral(QueryConfig cfg, Object whereValue,
			Class<T> clazz, int start,  int limit) {
		return pageDao.queryForGeneral(whereValue,cfg,clazz,start,limit);
	}


	@Override
	public <T> List<T> query(QueryConfig cfg, Object whereValue,
			Class<T> clazz) {
		return dao.query(whereValue,cfg,clazz);
	}

	@Override
	public TableQuery getQueryInfo(QueryConfig cfg, ResultSetMetaData rsmd,
			Class clazz) throws SQLException {
			return BuilerQuery.getQueryInfo(rsmd, clazz);
	}


}
