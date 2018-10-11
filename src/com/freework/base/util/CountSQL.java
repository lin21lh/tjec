package com.freework.base.util;

import java.util.ArrayList;
import java.util.List;

public class CountSQL {
	public static String getCountSql(String sql) {

		String countStr = "";
		if(sql.indexOf("(")!=-1){
			
			countStr = "select count(*) " +countSQL(sql);
		}else{
		
			int sqlFrom = sql.toLowerCase().lastIndexOf("from");
			int sqlOrderby = sql.toLowerCase().lastIndexOf("order by");
			if (sqlOrderby > 0) {
				countStr = "select count(*) " + sql.substring(sqlFrom, sqlOrderby);
			} else {
				countStr = "select count(*) " + sql.substring(sqlFrom);
			}
		}
		return countStr;
	}

	


	private static List<String> getSqlParameter2(String sql, char startStr, char endStr) {
		char[] sqlArray = sql.toCharArray();

		List<String> list = new ArrayList<String>();
		boolean pand = false;
		int j=0;
		StringBuilder parameter = new StringBuilder(10);
		for (int i = 0; i < sqlArray.length; i++) {
			if ( sqlArray[i] == startStr) {
           
				pand = true;
				j++;
			} else if (sqlArray[i] == endStr) {
				j--;

				if(j==0){
				pand = false;
				parameter.append(endStr);
				list.add(parameter.toString());
				parameter = new StringBuilder(10);

				}


			}  if (pand) {
				parameter.append(sqlArray[i]);
			}

		}

		return list;

	}
	private static String countSQL( String sql){
		List<String> list=getSqlParameter2(sql, '(', ')');
		String tmpsql=sql;			
		String where=null;
		for (String string : list) {
		     int indexStr=tmpsql.indexOf(string);
		     String tmp=tmpsql.substring(0, indexStr);
		     if(tmp.toLowerCase().indexOf("from")!=-1){
		    	 where= countSQLWhere(tmpsql);
		    	 break;
		     }
			tmpsql=StringUtils.replace(tmpsql,string, "");
		}
		if(where==null){
	    	 where= countSQLWhere(tmpsql);

		}		
		return where;		
	}
	private static String countSQLWhere(String where){
		int orderIndex=where.toLowerCase().indexOf("order by");
		int fromIndex=where.toLowerCase().indexOf("from");
		if(orderIndex!=-1){
			where=where.substring(fromIndex,orderIndex);
		}else{
			 where= where.substring(fromIndex);
		}
		return where;
	}
	

}
