package com.freework.freedbm.util.listUtil.group;

import java.util.Map;

import com.freework.freedbm.util.listUtil.group.Aggregate.AggregateData;
import com.freework.freedbm.util.listUtil.group.Aggregate.GenerateAggregate;



public class AggregateCol {
	public String sourceName="";
	AggregateData aggregate=null;
	TagertParameterName findTagertName=null;	
	public static AggregateCol[] cols2(String... colName){
		AggregateCol[] AggregateCols=new AggregateCol[colName.length];
		for (int i = 0; i < colName.length; i++) {
			AggregateCols[i]=new AggregateCol(colName[i],colName[i]);
		}
		return AggregateCols; 
	}
	public static AggregateCol[] cols(String... colName){
		AggregateCol[] AggregateCols=new AggregateCol[colName.length/2];
		int j=0;
		for (int i = 0; i < colName.length; i+=2) {
			AggregateCols[j]=new AggregateCol(colName[i],colName[i+1]);
			j++;
		}
		return AggregateCols; 
	}
	
	public AggregateCol(String sourceName, String tagertName) {
		super();
		if(sourceName.indexOf(",")!=-1){
			String[] sourceInfo=sourceName.split(",");
			this.sourceName=sourceInfo[1];
			aggregate=GenerateAggregate.generateAggregate(sourceInfo,this);
		}else
		this.sourceName = sourceName;
		
		this.findTagertName=new TagertParameterName(tagertName);
		
	}

	public void setValue(Map sourceMap,Map groupMap){
		try{
			String tagertName=findTagertName.getTagertName(sourceMap);
			if(tagertName==null){
			//	System.out.println(sourceMap);
				return ;
			}
			if(aggregate==null){
				Object value=sourceMap.get(sourceName);
				if(!groupMap.containsKey(tagertName)&&value!=null&&!value.equals("")){
					groupMap.put(tagertName,value);
				}
			}else{
				aggregate.setData(sourceMap, groupMap,tagertName);
			}
		}catch(TagertNameNullParamException e){
			//e.printStackTrace();
		}
		
	}

	

	
		

}
