package com.freework.queryData.compileSQL.simpleSQL;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freework.queryData.compileSQL.CompileSQL;
import com.freework.queryData.compileSQL.SqlAndParam;

public class SimpleCmpleSQL implements CompileSQL {

	private String sourceSql="";
	private List<ISQLPart> sqlParts=null;
	private SQLPartAnalyze analyze;
	
	public void setAnalyze(SQLPartAnalyze analyze) {
		this.analyze = analyze;
	}
	public List<ISQLPart> getSqlParts() {
		return new ArrayList<ISQLPart>(sqlParts);
	}
	public SimpleCmpleSQL(){}
	public SimpleCmpleSQL(String sourceSql,SQLPartAnalyze analyze){
		this.analyze=analyze;
		this.setSourceSql(sourceSql);
	}
	public void setSourceSql(String sourceSql) {
		this.sourceSql = sourceSql;
		analyze(sourceSql);
		
	}
	private void analyze(String sourceSql){
		//System.out.println(sourceSql);
		if(analyze==null)
		   analyze=new SQLPartAnalyze();
		sqlParts=analyze.analyze(sourceSql);
		
	}
	@Override
	public Set<String> getParamNames() {
		HashSet<String> set=new HashSet<String>();
		for (ISQLPart sqlPart : sqlParts) {
			if(sqlPart.getParamNames()!=null)
				set.addAll(sqlPart.getParamNames());
		}
		
		return set;
	}

	@Override
	public String getSourceSql() {
		return sourceSql;
	}

	@Override
	public SqlAndParam getSQL(Object param) {
		
		List<Object> list=new LinkedList<Object>();
		//String targetSql=sourceSql;
		StringBuilder targetSql=new StringBuilder(sqlParts.size()*16);
		for (ISQLPart sqlPart : sqlParts) {
			sqlPart.addSqlAndParam(param, list, targetSql);
		}

		return new SqlAndParam(targetSql.toString(),list);
	}

public  static void main(String ra[]){
		
		 String sql="select   e.*,u.name as districtName,su.username as suusername \n"+
				  " from SYS_ENGI e  \n"+
				  " left join SYS_UNIT u on e.district=u.code  \n"+
				 "  left join SYS_USER su on e.verify=su.usercode  \n"+
				 "  where 1=1 \n"+
				 "  [and e.name like '%'||{name}||'%' ] \n"+
				  " [and e.startmark like '%'||{startmark}||'%'] \n"+
				"and u.district like  {sessionUser.district}||'%' \n"+
				 " order by e.code asc ";
			SimpleCmpleSQL cmple=	new SimpleCmpleSQL(sql,null);
			
			Map map= new HashMap() ;
		        map.put("name", false);
		        map.put("startmark", 1);
		        SqlAndParam sp=   cmple.getSQL(map);
		     
		
	}

}
