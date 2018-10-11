package com.freework.freedbm.util.listUtil.group.Aggregate;

import java.util.Map;

import com.freework.freedbm.util.listUtil.group.AggregateCol;

public class CountData implements AggregateData {

	@Override
	public AggregateData newAggregateData(String[] args) {
		return new CountData();
	}

	@Override
	public void setCol(AggregateCol col) {

	}

	@Override
	public void setData(Map sourceMap, Map groupMap, String tagertName) {
		Integer count=(Integer)groupMap.get(tagertName);
		if(count!=null){
			count++;
			groupMap.put(tagertName, count);
		}else{
			groupMap.put(tagertName, 1);
		}
	}
	@Override
	public String getName() {
		return "count";
	}

	
}
