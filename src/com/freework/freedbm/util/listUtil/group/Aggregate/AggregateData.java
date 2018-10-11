package com.freework.freedbm.util.listUtil.group.Aggregate;

import java.util.Map;

import com.freework.freedbm.util.listUtil.group.AggregateCol;

public interface AggregateData {

	 public AggregateData newAggregateData(String[] args);
	public void setCol(AggregateCol col);

	public void setData(Map sourceMap,Map groupMap,String tagertName);
	public String getName();
	
}
