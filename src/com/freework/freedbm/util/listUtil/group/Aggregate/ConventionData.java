package com.freework.freedbm.util.listUtil.group.Aggregate;

import java.util.Map;

import com.freework.freedbm.util.listUtil.group.AggregateCol;

public class ConventionData implements AggregateData {
	 AggregateCol col;
		
	public void setCol(AggregateCol col) {
				this.col = col;
	}
	public void setData(Map sourceMap, Map groupMap,String tagertName) {
		groupMap.put(tagertName, col.sourceName);
	}

	
	public String getName() {
		return "convention";
	}
	public AggregateData newAggregateData(String[] args) {
		return new ConventionData();
	}

}
