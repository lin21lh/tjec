package com.freework.freedbm.util.listUtil.group.Aggregate;

import java.util.Map;

import com.freework.freedbm.util.listUtil.group.AggregateCol;

public class AppendData implements AggregateData{

	String regex=",";
	 AggregateCol col;
		
	public void setCol(AggregateCol col) {
			this.col = col;
		}
	public void setData(Map sourceMap, Map groupMap,String tagertName) {

		StringBuilder value=(StringBuilder)groupMap.get(tagertName);
		if(value!=null)
			value.append(regex);
		else{
			value=new StringBuilder();
			groupMap.put(tagertName, value);
		}
		value.append(sourceMap.get(col.sourceName));

	}
	public void setInfo(String[] infos) {
		if(infos!=null&&infos.length>0)
			regex=infos[0];
	}
	public String getName() {
		return "append";
	}

	public AggregateData newAggregateData(String[] args) {
		AppendData myobj=new AppendData();
		if(args!=null&&args.length>0)
			myobj.regex=args[0];
		return myobj;
	}

}
