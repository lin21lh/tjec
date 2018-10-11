package com.freework.queryData.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractCollection;
import java.util.Iterator;

import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
public class ResultCollection extends AbstractCollection<ResultSetMap>{
	ResultIterator iterator;
	public ResultCollection(ResultSet rs,JdbcForDTO[] fields){
		
		iterator=new ResultIterator(rs,fields);
	}
	@Override
	public Iterator<ResultSetMap> iterator() {
		return iterator;
	}

	@Override
	public int size() {
		return 0;
	}
	
	
}


 class ResultIterator implements Iterator<ResultSetMap> {
	ResultSet rs=null;
	ResultSetMap map;
	public ResultIterator(ResultSet rs,JdbcForDTO[] fields){
		this.rs=rs;
		map=new ResultSetMap(fields, rs);
	}
	@Override
	public boolean hasNext() {
		try {
			map.reset();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public ResultSetMap next() {
		return map;
	}

	@Override
	public void remove() {
		
	}

}
