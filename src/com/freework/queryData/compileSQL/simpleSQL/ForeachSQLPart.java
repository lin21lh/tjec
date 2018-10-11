package com.freework.queryData.compileSQL.simpleSQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import com.freework.base.util.SqlUtil;

public class ForeachSQLPart implements RegisterSQLPart,ISQLPart {

	private String targetSql="";

	String strAdd;
	String paramName;
	List<String> itemNames;
	Pattern regex= null;
	@Override
	public List<String> getParamNames() {
		return Arrays.asList(paramName);
	}

	@Override
	public void addSqlAndParam(Object uvalue, List<Object> params,
			StringBuilder sql) {
		Object	value=SQLPart.getParamValue(paramName,uvalue); 
		
		if(value instanceof String&&regex!=null){
		
			value=Arrays.asList(regex.split((String)value));
		}
		
		
		if (value instanceof Collection) {
			Collection c=(Collection) value;
			boolean first=true;
			if(strAdd!=null){
				for (Object object : c) {
					this.addValue(object, params);
					if(first){
						first=false;
					}else{
						sql.append(strAdd);
					}
					sql.append(targetSql);
				}
			}else{
				for (Object object : c) {
					this.addValue(object, params);
					if(first){
						first=false;
					}else{
						sql.append(',');
					}
					sql.append(targetSql);
				}
				
			}
			
		}
		
		

		
		
	}

	public void addValue(Object object,List<Object> params){
		
		for (String itemName : itemNames) {
			if("@item".equals(itemName)){
				params.add(object);
				
			}else{
				params.add(SQLPart.getParamValue(itemName,object));
			}
		
		}
	}
	
	@Override
	public String getMethodName() {
		return "foreach";
	}
	public static void main(String args[]){
		
	System.out.println(	Arrays.asList(new ForeachSQLPart().getArgs("test,or,','")));
		
	}
	private String[] getArgs(String args){
	 List<String> list= new ArrayList<String>(3);
	 StringBuilder str=new StringBuilder(args.length());
	 boolean start = false;
		for (int i = 0; i < args.length(); i++) {
			char item=args.charAt(i);
			
			if(!start&&item==','){
				list.add(str.toString());
				str=new StringBuilder(args.length()-i);
			}else{
				if(item=='\''){
					start=!start;
				}else{
					str.append(item);
				}
			}			
		}
		if(str.length()!=0)
			list.add(str.toString());
		return list.toArray(new String[list.size()]);
	}
	
	
	public void analyze2(String args, String sql) {
		String[] argsArray=getArgs(args);
		paramName=argsArray[0];
		if(argsArray.length>1){
			strAdd=argsArray[1];
		}
		if(argsArray.length>2){
			regex=Pattern.compile(argsArray[2]);
		}
		List<String> list=SqlUtil.getSqlParameter(sql, PARAM_START_CHAR, PARAM_END_CHAR);
		itemNames=new LinkedList<String>();
		for (String itemName : list) {

			if(itemName.length()>=4){
				if(itemName.substring(0,4).equals("item")){
					targetSql=sql.replace(PARAM_START_CHAR+itemName+PARAM_END_CHAR, "?");
					int index=itemName.indexOf('.');
					if(index!=-1){
						itemNames.add(itemName.substring(index+1));
					}else{
						itemNames.add("@item");
					}
				}
			}
		}
		
	}
	
	
	
	@Override
	public ISQLPart analyze(String args, String sql,SQLPartAnalyze analyze) {
		ForeachSQLPart f=new ForeachSQLPart();
		f.analyze2(args, sql);
		return f;
	}

}
