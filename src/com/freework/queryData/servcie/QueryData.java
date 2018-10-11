package com.freework.queryData.servcie;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class QueryData {

	
	private  static QueryData myobj;


	

	private List<LoadFile> files;
	
	public QueryData(){
		myobj=this;
	}
	
	public QueryData get(){
		return myobj;
		
		
	}
	public void setFiles(List<LoadFile> files){	
		if(this.files==null){
			this.files=files;
		}else{		
			this.files.addAll(files);
		}
		java.util.Collections.sort(this.files, new Comparator<LoadFile>(){
			public int compare(LoadFile o1, LoadFile o2) {
				return o2.getPrefix().length()-o1.getPrefix().length();
			}
		});
		for (LoadFile loadFile : files) {
			System.out.println(loadFile.getPrefix());
		}
	}
	public  QueryConfig getQueryConfig1(String id){
		for (LoadFile file : files) {
			QueryConfig query=file.get(id);
			if(query!=null)
				return query;
		}
		 throw new RuntimeException("id:"+id+"未定义");
	}

	
	public static QueryConfig getQueryConfig(String id){
		return myobj.getQueryConfig1(id);
	}
	
	
	
	public  static Set<String> getParams(String id){
	
		return getQueryConfig(id).getParams();
	}
	public static <T> List<T> queryById(String id,Class<T> clazz,Object ... whereValue){
		
		return myobj.query(id,asMap(whereValue), clazz);

		
	}
	public static <T> List<T> queryById(String id,Object whereValue,Class<T> clazz){
		return myobj.query(id, whereValue, clazz);
	}
	
	public static <K,T> Map<K,T> asMap(Object...values ){
		 Map<K,T> map=new HashMap<K,T>();
		 if(values.length%2==1)
		        throw new IllegalStateException("您的key 与 value 不配对");

		for (int i = 0; i < values.length; i+=2) {
			map.put((K)values[i],(T)values[i+1]);
		}
		return map;
	 
	} 
	public static List<Map> queryById(String id,Object ... whereValue){
		return myobj.query(id, asMap(whereValue));
	}
	public static List<Map> queryById(String id,Object whereValue){
		return myobj.query(id, whereValue);
	}
	private <T> List<T> query(String id,Object whereValue,Class<T> clazz){
		QueryConfig cfg= getQueryConfig1(id);
		QueryService exec=cfg.getExecute();
		return exec.query(cfg, whereValue, clazz);
		
	}
	
	private List<Map> query(String id,Object whereValue){
		 return query(id,whereValue,Map.class);
	}
	
	
}
