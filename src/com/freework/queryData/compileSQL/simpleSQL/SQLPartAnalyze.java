package com.freework.queryData.compileSQL.simpleSQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.freework.base.util.SqlUtil;
import com.freework.base.util.StringUtils;

public class SQLPartAnalyze {

	private List<RegisterSQLPart> registerSQLList=new ArrayList<RegisterSQLPart>(Arrays.asList(
			new IFRegisterSQLPart()
			,new ForeachSQLPart()
			,new StringSQL()
			,new DicSQLPart()
			,new SQLEscape()));
	
	public void setRegisterSQLList(List<RegisterSQLPart> registerSQLList) {
		this.registerSQLList.addAll(registerSQLList);
	}
	
	public static final char START_CHAR='[';
	public static final char END_CHAR=']';
	public   String[] getAnalyze(String str,String name) {
		
		int namelength=name.length()+1;
		
		if(str.length()<=namelength+1){
			return null;
		}
		String start=str.substring(0, namelength);
		if(!start.equals(name+"(")){
			return null;
		}
		int j=1;
		for (int i =namelength; i < str.length(); i++) {
			
			if ( str.charAt(i) == '(') {
				j++;
			} else if (str.charAt(i) == ')') {
				j--;
				if(j==0){
					return new String[]{str.substring(namelength,i),str.substring(i+1)};
				}
			}
		}
		return null;

	}
	
	public ISQLPart getSQLPart(String sql){
		for (RegisterSQLPart register : registerSQLList) {
			String[] arrayStr=getAnalyze(sql,register.getMethodName());
			if(arrayStr!=null){
				return register.analyze(arrayStr[0], arrayStr[1],this);
			}

		}
		return new NotBlankSQLPart(sql);
	}
	
	
	public List<String>  getSQLPartStr(String sourceSql){
		List<String> sqlPartStr=SqlUtil.getSqlParameter2(sourceSql,START_CHAR, END_CHAR);
		return sqlPartStr;
	}
	
	
	public  List<ISQLPart> analyze(String sourceSql){
		return this.analyze(sourceSql, getSQLPartStr(sourceSql));
	}
	
	public List<ISQLPart> analyze(String sourceSql,List<String> sqlPartStr){
		//System.out.println(sourceSql);
		List<ISQLPart> sqlParts=new LinkedList<ISQLPart>();
		if(sqlPartStr.size()==0){
			sqlParts.add(new SQLPart(sourceSql));
			return sqlParts;
		}
		
		int index=0;
		int end=0;

		for (String sql : sqlPartStr) {
			end=sourceSql.indexOf(START_CHAR+sql+END_CHAR,index);
				
				if(index!=end){
					String str=	sourceSql.substring(index,end);
					//if(!StringUtils.replaceEnter(str, "").trim().equals(""))
						sqlParts.add(new SQLPart(str));
				}
				index=end+sql.length()+2;
				
				
				sqlParts.add(getSQLPart(sql));
		}
		if(sourceSql.length()>index){
			String str=	sourceSql.substring(index);
			
			if(!StringUtils.replaceEnter(str, "").trim().equals(""))
				sqlParts.add(new SQLPart(str));
		}
		return sqlParts;
		
	}
	
	
	
	
}
