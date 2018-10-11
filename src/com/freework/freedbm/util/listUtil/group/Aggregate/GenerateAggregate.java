package com.freework.freedbm.util.listUtil.group.Aggregate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.freework.base.util.LoadDirClass;
import com.freework.freedbm.util.listUtil.group.AggregateCol;

public class GenerateAggregate {

	static	Map<String,AggregateData> methodMap=new HashMap<String,AggregateData>();
	static{
			
			LoadDirClass ldc=new LoadDirClass( AggregateData.class);
			Collection<AggregateData> aggregateDatas= ldc.getObjects();
			for (AggregateData aggregateData : aggregateDatas) {
				methodMap.put(aggregateData.getName(), aggregateData);
			}
		
	}
	public static AggregateData generateAggregate(String[] sourceInfo,AggregateCol col ){
		AggregateData	aggregateDate=methodMap.get(sourceInfo[0]);
		
		
		if(aggregateDate==null)
			return null;
		if(sourceInfo.length>2){
			String [] infos=new String[sourceInfo.length-2];
			System.arraycopy(sourceInfo, 2, infos, 0, infos.length);
			aggregateDate=aggregateDate.newAggregateData(infos);
			aggregateDate.setCol(col);

		}else
		{
			aggregateDate=aggregateDate.newAggregateData(null);
			aggregateDate.setCol(col);
		}
		return aggregateDate;
	}
}
