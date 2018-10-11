package com.freework.queryData.servcie.tree;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.freework.base.util.unmodifiableMap.UnmodifiableKeyMap;
import com.freework.queryData.servcie.QueryConfig;
import com.freework.queryData.servcie.tree.TreeParserFactory.TreeInfo;

public class TreeCheckDataService extends TreeDataService{

	public <T> List<T> query(QueryConfig cfg, Object whereValues,
			Class<T> clazz) {
		Map whereValue=(Map) whereValues;
	
		String isWhere=null;
		
		if(whereValue!=null){
			isWhere=(String) whereValue.get("isWhere");
		}
		Object	 id=whereValue.get("id");
		if(id==null){
			id="0";
			whereValue.put("id", id);
		}
		
		List list=null;
		if(!"1".equals(isWhere)&&!id.equals("0")){
			 list=dao.query(whereValue, cfg, clazz);
		}else{
			 list=dao.query(whereValue, cfg, clazz);
			 if(list.size()==0){
				 whereValue.put("isRoot", true);
				 list=dao.query(whereValue, cfg, clazz);
				 return list;
			 }
				 
			 
			 
			 Map<Object, UnmodifiableKeyMap<Object>> map= this.builderMap(list, whereValue, (TreeInfo)cfg.getUserData(), cfg);


			 if("1".equals(isWhere)){ 
				for (Object object : list) {
						((Map)object).put("open", true);
				}
			}
			list=bornTreeRoot.toBornTree(map, (TreeInfo)cfg.getUserData(), this,cfg);

		}
		
		return 	list;

	}
	public void setAllUpNode(TreeInfo treeInfo, UnmodifiableKeyMap<Object> node,
			Map<Object, UnmodifiableKeyMap<Object>> all,QueryConfig cfg,HashSet parentIds) {
		Object parentid = getParentid(node, treeInfo);
		while (node != null && parentid != null && !parentid.toString().equals("0")) {
			node = all.get(parentid);
			if (node == null) {
				node =getUpNode(treeInfo, parentid,cfg);
				if(node!=null){
					Object id =getId(node, treeInfo);
					parentIds.add(getParentid(node, treeInfo));
					all.put(id, node);
				}
			}
			if (node != null)
			parentid =getParentid(node, treeInfo);
		}

	}

	public Map<Object, UnmodifiableKeyMap<Object>>  builderMap(List<UnmodifiableKeyMap<Object>> list,Map whereValue
			, TreeInfo info,QueryConfig cfg) {
		Map<Object, UnmodifiableKeyMap<Object>> all=new LinkedHashMap<Object, UnmodifiableKeyMap<Object>>();
		HashSet parentIds=new HashSet();
		List itemIds=new LinkedList();
		
		for (UnmodifiableKeyMap<Object> node : list) {
			node.put("checked", true);
			all.put(getId(node, info), node);
			itemIds.add(getId(node, info));
		}
		for (UnmodifiableKeyMap<Object> node : list) {
			setAllUpNode(info, node, all,cfg,parentIds);
			parentIds.add(getParentid(node, info));
		}
	
		
		if(itemIds.size()!=0&&parentIds.size()!=0){
			whereValue.put("isParents", true);
			whereValue.put("parentIds", parentIds);
			whereValue.put("itemIds", itemIds);
			List<Map> list2=dao.query(whereValue, cfg, Map.class);
			for (Map node : list2) {
				all.put(getId((UnmodifiableKeyMap) node, info),(UnmodifiableKeyMap) node);
			}
		}
		return all;
	
	}
	
	
}
