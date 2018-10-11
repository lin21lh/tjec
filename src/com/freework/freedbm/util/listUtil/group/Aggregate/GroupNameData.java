package com.freework.freedbm.util.listUtil.group.Aggregate;

import java.util.Map;

import com.freework.freedbm.util.listUtil.group.AggregateCol;

public class GroupNameData implements AggregateData{
	 AggregateCol col;

	public void setCol(AggregateCol col) {
		this.col = col;

	}

	public void setData(Map sourceMap, Map groupMap,String tagertName) {
		
		if(!groupMap.containsKey(tagertName)){
			Object value=groupMap.get(col.sourceName);
				if(value!=null&&!value.equals(""))
					groupMap.put(tagertName,groupMap.get(col.sourceName));
		}
	}


	public String getName() {
		return "groupname";
	}

	public AggregateData newAggregateData(String[] args) {
		// TODO Auto-generated method stub
		return new GroupNameData();
	}

}
