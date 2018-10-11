package com.freework.queryData.servcie.tree;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;

import com.freework.base.util.unmodifiableMap.UnmodifiableKeyMap;
import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.queryData.dao.BuilerQuery;
import com.freework.queryData.dao.QueryDao;
import com.freework.queryData.servcie.QueryConfig;
import com.freework.queryData.servcie.QueryService;
import com.freework.queryData.servcie.tree.TreeParserFactory.TreeInfo;

public class TreeDataService implements QueryService, TreeRelated<UnmodifiableKeyMap<Object>> {
	
	BornTreeRootNodeService bornTreeRoot=null;
	
	QueryDao dao;
	@Resource()
	public void setBornTreeRoot(BornTreeRootNodeService bornTreeRoot) {
		this.bornTreeRoot = bornTreeRoot;
	}
	public void setDao(QueryDao dao) {
		this.dao = dao;
	}

	@Override
	public Object getParentid(UnmodifiableKeyMap treedata, TreeInfo info) {
		return treedata.getIndex(info.getParentColInt());
	}

	@Override
	public Object getId(UnmodifiableKeyMap treedata, TreeInfo info) {
		return treedata.getIndex(info.getIdColInt());
	}

	@Override
	public UnmodifiableKeyMap getUpNode(TreeInfo treeInfo, Object parentid,QueryConfig cfg) {
		Map map=new HashMap();
		map.put("my_id", parentid);
		map.put("isUp", true);
		
		List<UnmodifiableKeyMap> list=dao.query(map, cfg, UnmodifiableKeyMap.class);
		
		if(list.size()>0){
			UnmodifiableKeyMap tree =list.get(0);
			tree.put("open", true);
			return  tree;

		}
		return null;
	}

	
	
	
	
	@Override
	public <T> List<T> query(QueryConfig cfg, Object whereValue,
			Class<T> clazz) {
		
		if(!Map.class.isAssignableFrom(clazz)){
			return dao.query(whereValue, cfg, clazz);
		}
		String isWhere=null;
		String isOpen=null;
		if(whereValue!=null)
		try {
			 isWhere=BeanUtils.getProperty(whereValue, "isWhere");
			 isOpen=BeanUtils.getProperty(whereValue, "isOpen");
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		} 
		List list=null;
		if(!"1".equals(isWhere)){
			if (whereValue instanceof Map) {
				Map map=(Map)whereValue;
				Object id=map.get("id");
				if(id==null)
					map.put("id", "0");
			}
			
			 list=dao.query(whereValue, cfg, clazz);
			
		}else{
			list=dao.query(whereValue, cfg, clazz);
			 for (Object object : list) {
					((Map)object).put("open",isOpen==null||"1".equals(isOpen)? true:false);
				}
			list=bornTreeRoot.toBornTree(list, (TreeInfo)cfg.getUserData(), this,cfg);

		}
		
		return 	list;

	}

	@Override
	public TableQuery getQueryInfo(QueryConfig cfg, ResultSetMetaData rsmd,
			Class clazz) {
		try {
			return BuilerQuery.getQueryInfo(rsmd, clazz);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	@Override
	public void setChildren(TreeInfo info, UnmodifiableKeyMap<Object> treedata,
			List<UnmodifiableKeyMap<Object>> childerens) {
		treedata.put("children", childerens);
	}
	@Override
	public List<UnmodifiableKeyMap<Object>> getChildren(TreeInfo info,
			UnmodifiableKeyMap<Object> treedata) {
		// TODO Auto-generated method stub
		return (List<UnmodifiableKeyMap<Object>>) treedata.get("children");
	}


}
