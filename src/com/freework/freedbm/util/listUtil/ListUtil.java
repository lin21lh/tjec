package com.freework.freedbm.util.listUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.freework.freedbm.util.listUtil.SortList.ItemType;
import com.freework.freedbm.util.listUtil.group.AggregateCol;
import com.freework.freedbm.util.listUtil.group.GroupCol;
import com.freework.freedbm.util.listUtil.group.GroupKey;

public class ListUtil {
	public static void main(String args[]){
			List<Map> list=new ArrayList<Map>(); 
			for (int deptId = 0; deptId <5; deptId++) {
				for (int departmentId = 0; departmentId <10; departmentId++) {
						//for (int i = 0; i < 10000; i++) {
							Map map=new HashMap();
							map.put("deptId",deptId);
							map.put("departmentId",departmentId);
							map.put("salary1",10);
							map.put("salary2", 20);
							map.put("name", "уе");
							list.add(map);
					//}
				}
			}
			GroupCol[] groupCols={GroupCol.col("deptId"),GroupCol.col("departmentId")};
			AggregateCol aggregateCols[]={
				 new AggregateCol("sum,salary1","salary1"),
				 new AggregateCol("sum,salary1+salary2","test"),
				 new AggregateCol("decodeSum,deptId=0,salary1+salary2,0","test2"),
				 new AggregateCol("count,*","countee_{departmentId}")
			 };

			long starttime=System.currentTimeMillis();
			Collection list2= group(list,groupCols,aggregateCols);
			System.out.println(System.currentTimeMillis()-starttime);
			
			starttime=System.currentTimeMillis();
			 list2= group(list,groupCols,aggregateCols);
			System.out.println(System.currentTimeMillis()-starttime);
			
			starttime=System.currentTimeMillis();
			 list2= group(list,groupCols,aggregateCols);
			System.out.println(System.currentTimeMillis()-starttime);
			System.out.println( JSON.toJSONString(list2));
		
	}
	
	
	
	
	public static Map mergerMap(int size, List<GroupKey> keys){
		Map mergerMap=new HashMap();
		for (int j = 0; j <=size; j++) {
			mergerMap.put(keys.get(j).name,keys.get(j).value);
		}
		return mergerMap;
	}
	
	
	
	public static  void sort(List list,ItemType itemtype, String... sortFieldName){
		SortList.sort(list, itemtype, sortFieldName);
	}
	
	
	
	
	
	public static Collection group(List<Map> list,GroupCol[] groupCols,AggregateCol[] aggregateCol){
		SortedMap<String, Map> groupMap = new TreeMap<String, Map>();
		for (Map map : list) {
				 List<GroupKey> keyList=GroupKey.getGroupKey(groupCols, map);
		int i=0;
			for (GroupKey groupKey2 : keyList) {
					//String key=keystr.toString();
					if(groupKey2.isData){
						String keystr=groupKey2.toString();
						Map mergerMap=groupMap.get(keystr);
						if(mergerMap==null){
							mergerMap=mergerMap(i,keyList);
							mergerMap.put("gourp_sys_level", i);
							groupMap.put(keystr, mergerMap);
						}
						for (int j = 0; j < aggregateCol.length; j++) {
							aggregateCol[j].setValue(map, mergerMap);
						}
					}
					i++;
			}
			
		}
		return groupMap.values();
		
	}
}
