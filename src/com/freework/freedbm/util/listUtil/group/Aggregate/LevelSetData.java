package com.freework.freedbm.util.listUtil.group.Aggregate;

import java.util.Map;

import com.freework.freedbm.util.listUtil.group.AggregateCol;

public class LevelSetData  implements AggregateData{

	int level=0;
	AggregateCol col;
	public void setCol(AggregateCol col) {
		this.col=col;
	}

	public void setData(Map sourceMap, Map groupMap,String tagertName) {
		Object value=sourceMap.get(col.sourceName);
		Integer level=(Integer) groupMap.get("gourp_sys_level");
		if(!groupMap.containsKey(tagertName)&&value!=null&&!value.equals("")&&level==this.level){
			groupMap.put(tagertName,value);
		}
	}


	
	public AggregateData newAggregateData(String[] args) {
		LevelSetData myobj=new LevelSetData();
		if(args!=null&&args.length>0)
			myobj.level=Integer.parseInt(args[0]);
		return myobj;
	}
	public String getName() {
		return "levelSet";
	}

}
