package com.freework.freedbm.util.listUtil.group.Aggregate;

import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.freework.freedbm.util.listUtil.group.AggregateCol;

public class MaxData implements AggregateData {
	 AggregateCol col;
		
	public void setCol(AggregateCol col) {
					this.col = col;
	}
	int comparator(Object value1,Object value2){
		Comparable comparable1=null;

		if(value1 instanceof Comparable){
			comparable1=(Comparable)value1;
		}else if(value1==null){
			if(value2!=null)
					return -1;
		}else
			comparable1=value1.toString();


		 if(value2==null){
			if(value1!=null)
				return 1;
		 }
		if(comparable1!=null&&value2!=null){
			if(comparable1.getClass()!=value2.getClass()){
				Converter converter=ConvertUtils.lookup(comparable1.getClass());
				if(converter!=null)
					value2=converter.convert(value2.getClass(), value2);
				else
					value2=null;
				
			}			
			if(value2==null)
				return -1;
			
			int e=comparable1.compareTo(value2);
				return e;
			
		}else{
			
			return 0;
		}
			
	}
	public void setData(Map sourceMap, Map groupMap,String tagertName) {

		Object value1=groupMap.get(tagertName);
		Object value2=sourceMap.get(col.sourceName);
		if(comparator(value1,value2)<0){
			groupMap.put(tagertName, value2);
		}
		
		
	}

	public String getName() {
		return "max";
	}
	public AggregateData newAggregateData(String[] args) {
		return new MaxData();
	}

}
