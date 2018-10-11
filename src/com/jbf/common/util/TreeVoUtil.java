package com.jbf.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jbf.base.dic.dto.DicTreeVo;
import com.jbf.common.vo.TreeVo;

public class TreeVoUtil {

	/**
	 * 如果没有上级 则不展示
	 * @param list
	 * @param rootid
	 * @param isnoExists
	 * @return
	 */
	public static List<? extends TreeVo> toBornTree(
			List<? extends TreeVo> list, String rootid, boolean isnoExists) {
		List<TreeVo> rootList = new ArrayList<TreeVo>();
		TreeVo treedata = null;
		
		if ((list == null || list.size() == 0) && isnoExists) {
			treedata = new TreeVo("", "<<暂无数据>>", "0");
			rootList.add(treedata);
			return rootList;
		}	
		
		Map<String, List<TreeVo>> branchsMap = new HashMap<String, List<TreeVo>>(
				60, 0.5f);
		for (int i = 0; i < list.size(); i++) {
			treedata = list.get(i);
			if (treedata.getIsLeaf() != null) {
				if (treedata.getIsLeaf())
					treedata.setState("open");
				else
					treedata.setState("closed");
			}

			if (treedata != null
					&& (treedata.getPid() == null || treedata.getPid()
							.toString().equals(rootid))) {
				rootList.add(treedata);
			} else {
				List<TreeVo> branchsList = branchsMap.get(treedata.getPid());
				if (branchsList == null) {
					branchsList = new ArrayList<TreeVo>();
					branchsMap.put(treedata.getPid(), branchsList);
				}
				branchsList.add(treedata);
			}
		}
		for (Iterator<TreeVo> iterator = rootList.iterator(); iterator
				.hasNext();) {
			TreeVo root = iterator.next();
			root.setChildren(branchsMap.get(root.getId()));
			branchsMap.remove(root.getId());
		}
		
		toBornTreeBranch(list, branchsMap);
		return rootList;
	}

	private static void toBornTreeBranch(List<? extends TreeVo> list,
			Map<String, List<TreeVo>> branchsMap) {
		TreeVo treedata = null;
		List<TreeVo> childerens = null;
		
		
		
		for (int i = 0; i < list.size(); i++) {
			treedata = list.get(i);
			childerens = (List<TreeVo>) branchsMap.get(treedata.getId());
			if (childerens != null) {
				treedata.setChildren(childerens);
			}
		}
	}

	/**
	 * 如果没有上级 则展示
	 * @param list
	 * @param rootid
	 * @param isnoExists
	 * @return
	 */
	public static List<DicTreeVo> toBornTree2(List<DicTreeVo> list, String rootid, boolean isnoExists) {
		List<DicTreeVo> rootList = new ArrayList<DicTreeVo>();
		DicTreeVo treedata = null;
		
		if ((list == null || list.size() == 0) && isnoExists) {
			treedata = new DicTreeVo();
			treedata.setId("");
			treedata.setText("<<暂无数据>>");
			treedata.setPid("0");
			rootList.add(treedata);
			return rootList;
		}
		Map<String, List<DicTreeVo>> branchsMap = new HashMap<String, List<DicTreeVo>>(60, 0.5f);
		for (DicTreeVo treeData : list) {
			List<DicTreeVo> list2 = branchsMap.get(treeData.getPid());
			if(list2 == null){
				list2 = new ArrayList<DicTreeVo>();
				branchsMap.put(treeData.getPid(), list2);
			}
			list2.add(treeData);
		}
		for (DicTreeVo treeData : list) {
			  
			List<DicTreeVo> trees=branchsMap.get(treeData.getId());
			  if(trees != null && trees.size() > 0){				  
				  treeData.setIsLeaf(false);
				  treeData.setState("closed");
				  treeData.setChildren(trees);
				  branchsMap.remove(treeData.getId());
			  } else {
				  treeData.setIsLeaf(true);
				  treeData.setState("open");
			  }
			
		}
		
		Collection<List<DicTreeVo>> root=branchsMap.values();
		
		for (List<DicTreeVo> key : root) {
			rootList.addAll(key);
		}
		Collections.sort(rootList, treeCode);
		return rootList;
	}
	
	final static Comparator<DicTreeVo> treeCode=new Comparator<DicTreeVo>(){
		public int compare(DicTreeVo o1, DicTreeVo o2) {
			return o1.getCode().compareTo(o2.getCode());
		}
	};
}
