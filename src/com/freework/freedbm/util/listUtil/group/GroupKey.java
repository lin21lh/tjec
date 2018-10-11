package com.freework.freedbm.util.listUtil.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;




public class GroupKey {

	public String name="";
	public String value="";
	String valueKey="";
	public boolean isData=true;

	
	public GroupKey(String name, String value, String valueKey, boolean isData) {
		super();
		this.name = name;
		this.value = value;
		this.valueKey = valueKey;
		this.isData = isData;
	}

	public GroupKey(String name, String value, String valueKey) {
		super();
		this.name = name;
		this.value = value;
		this.valueKey = valueKey;
	}

	public String getKeyStr(){
		return valueKey;
	}
	public String toString(){
		return getKeyStr();
	}
	
	

	
	
	static void addGroupKey(List<GroupKey> values,String name, String value, StringBuilder keyStr,boolean isdata){
		  if(values.size()!=0)
    		  keyStr.append("_");
    	  keyStr.append(name).append(":").append(value);
    	 values.add(new GroupKey(name,value,keyStr.toString(),isdata));
		
	}
	
	
	public static List<GroupKey> getGroupKey(GroupCol[] cols,Map sourceMap){
		
		String name=null;
		Object groupValue =null;
		int length=cols.length*2;
		List<GroupKey> values=new ArrayList<GroupKey>(length);
		StringBuilder keyStr=new StringBuilder(length*5);
		int colsLength=cols.length-1;
		for (int j = 0; j < cols.length; j++) {
			 name=cols[j].getColName();
			  groupValue =cols[j].getValue(sourceMap);//sourceMap.get(name);
              if(cols[j].getSplitType()==null){
            	  boolean isdata=colsLength!=j?cols[j].isSum():true;
            	  addGroupKey(values,name,groupValue==null?"":groupValue.toString(),keyStr,isdata);
              }else{
            	  String oldcode=null;
            	  String str=groupValue==null?"":groupValue.toString();
            	  int splitType[]=cols[j].getSplitType();
            	  int endIndex=0;
          		int splitTypeLength=splitType.length-1;

            	  for (int i = 0; i < splitType.length; i++) {
                	  boolean isdata=splitTypeLength==i?cols[j].isSum():true;

            		  endIndex+=splitType[i];
            		  if(str.length()<endIndex)
            			break;  
            		  oldcode=str.substring(0, endIndex);
            		  addGroupKey(values,name,oldcode,keyStr,isdata);
            	  }
            	  if(oldcode!=null&&oldcode.length()<str.length()){
            		  addGroupKey(values,name,str,keyStr,false);
            		  
            	  }
 
            	  
              }
		
		
		}
		return values;
		
		
	}
	
}
