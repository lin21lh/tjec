package com.freework.queryData.servcie.tree;

import java.util.List;

import com.freework.queryData.servcie.QueryConfig;
import com.freework.queryData.servcie.tree.TreeParserFactory.TreeInfo;

public interface TreeRelated<T> {
	public Object getParentid(T treedata,TreeInfo info);
	public Object getId(T treedata,TreeInfo info);
	public T getUpNode(TreeInfo treeInfo, Object parentid,QueryConfig cfg) ;
	public void setChildren(TreeInfo info,T treedata,List<T> childerens);
	public List<T> getChildren(TreeInfo info,T treedata);

	
}
