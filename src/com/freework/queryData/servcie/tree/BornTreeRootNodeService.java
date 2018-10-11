/**
 * 
 */
package com.freework.queryData.servcie.tree;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.freework.queryData.servcie.QueryConfig;
import com.freework.queryData.servcie.tree.TreeParserFactory.TreeInfo;
/**
 * @author Administrator
 *
 */
@Service("queryData.servcie.tree.BornTreeRootNodeService")
public class BornTreeRootNodeService {
	
	
	public <T> Map<Object, T>  builderMap(List<T> list
			, TreeInfo info,TreeRelated<T> treeBorn,QueryConfig cfg) {
		Map<Object, T> all=new LinkedHashMap<Object, T>();
		for (T node : list) {
			all.put(treeBorn.getId(node, info), node);
		}
		for (T node : list) {
			this.setAllUpNode(info, node, all, treeBorn,cfg);
		}
		return all;
	
	}
	
	
	
	public <T> List<T> toBornTree(List<T> list
			,TreeInfo info,TreeRelated<T> treeBorn,QueryConfig cfg) {
		Map<Object, T> all=builderMap(list,info,treeBorn,cfg);
		return this.toBornTree(all, info, treeBorn, cfg);
		
		
	}
	
	
	
	public <T> List<T> toBornTree(Map<Object, T> all, 
			TreeInfo info,TreeRelated<T> treeBorn,QueryConfig cfg) {
		Set<Entry<Object, T>> values = all.entrySet();
		List<T> rootList = new LinkedList<T>();
		List<T> childerens = null;
		Object parentid = null;
		for (Entry<Object, T> entry : values) {
			T treedata = entry.getValue();
			parentid = treeBorn.getParentid(treedata, info);
			T uptreedata = all.get(parentid);
			if (uptreedata == null) {
				rootList.add(treedata);
			} else {
				childerens = (List<T>) treeBorn.getChildren(info, uptreedata);
				if (childerens == null) {
					childerens = new LinkedList<T>();
					treeBorn.setChildren(info, uptreedata, childerens);
				}
				childerens.add(treedata);
			}
		}
		return rootList;

	}

	public <T> void setAllUpNode(TreeInfo treeInfo, T node,
			Map<Object, T> all,TreeRelated<T> treeBorn,QueryConfig cfg) {
		Object parentid =  treeBorn.getParentid(node, treeInfo);
		//all.put(treeBorn.getId(node, treeInfo), node);
		while (node != null && parentid != null && !parentid.toString().equals("0")) {
		

			node = all.get(parentid);
			if (node == null) {
				node = treeBorn.getUpNode(treeInfo, parentid,cfg);
				if(node!=null){
					Object id = treeBorn.getId(node, treeInfo);
					all.put(id, node);
				}
			}
			if (node != null)
			parentid = treeBorn.getParentid(node, treeInfo);
		}

	}


}
