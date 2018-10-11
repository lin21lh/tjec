package com.freework.freedbm.util.listUtil;
import java.util.Map;
public   class MapComparator extends MyComparator<Map> {

	
	public MapComparator(boolean isDESC, String... fieldNames) {
		super(isDESC, fieldNames);
	}

	public MapComparator(String... fieldNames) {
		super(fieldNames);
	}

	public Object getValue(String fieldName,Map dto){
		return dto.get(fieldName);
	}
	


}
