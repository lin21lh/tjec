package com.freework.freedbm.util.listUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortList {


	
	public enum ItemType{
		MAP,MAP_DESC,DTO,DTO_DESC,OTHER
	}
	
	public static Comparator getComparator(ItemType type, String... fieldNames){
		
		switch (type){
			case MAP:
				return new MapComparator(fieldNames);
			case MAP_DESC:
				return new MapComparator(true,fieldNames);
			case DTO:
				return new DTOComparator(fieldNames);
			case DTO_DESC:
				return new DTOComparator(true,fieldNames);
		}
				return null;
			
	}
	
	public static  void sort(List list,ItemType itemtype, String... sortFieldName){
		if(ItemType.OTHER==itemtype||sortFieldName==null||sortFieldName.length<=0)
			Collections.sort(list);
		else{
		Comparator comparator=getComparator(itemtype,sortFieldName);
		if(comparator!=null)
			Collections.sort(list,comparator);
		}
	
	}
	
	
	
}
