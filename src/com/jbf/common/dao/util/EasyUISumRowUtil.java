package com.jbf.common.dao.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jbf.common.dao.MapDataDaoI;

@Component
public class EasyUISumRowUtil {

	@Autowired
	MapDataDaoI mapDataDao;
	
	/**
	 * 获取总的合计行跟当前页的合计行
	 * @Title: getSumRow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param list（list中第一个为当前页合计的参数，第二个为总合计的参数）
	 * @param @param sql
	 * @param @param pageSize
	 * @param @param pageIndex
	 * @param @return 设定文件 
	 * @return List 返回类型 
	 * @throws
	 */
	public List getSumRow(List<Map<String,String>> list,String sql,Integer pageSize,Integer pageIndex){
		List reList = new ArrayList();
		List<Map<String,String>> midList = new ArrayList<Map<String,String>>();
		List<String> cols = mapDataDao.querySqlColumns(sql, null);
		int start = (pageIndex - 1) * pageSize;
		String sumSql = "";
		if(list!=null && list.size()>=1){
			for(int i=0;i<list.size();i++){
				Map<String,String> map = list.get(i);
				Set<String> set = map.keySet(); 
				int count=1;
				if(i==0){
					sumSql = " select ";
					for (String s:set) {
						if(cols.contains(map.get(s).toUpperCase())){
							if(count==set.size()){
								sumSql+=" sum("+map.get(s)+") "+s+" ";
							}else{
								sumSql+=" sum("+map.get(s)+") "+s+", ";
							}
						}else{
							if(count==set.size()){
								sumSql+=" '"+map.get(s)+"' "+s+" ";
							}else{
								sumSql+=" '"+map.get(s)+"' "+s+", ";
							}
							
						}
						count++;
					}
					sumSql+=" from (SELECT * FROM (SELECT ROWNUM R,T_td.* FROM ("+sql+") T_td) F WHERE F.R > "+start+" AND F.R <= "+(start+pageSize)+")";
				}else{
					sumSql = "select ";
					for (String s:set) {
						if(cols.contains(map.get(s).toUpperCase())){
							if(count==set.size()){
								sumSql+=" sum("+map.get(s)+") "+s+" ";
							}else{
								sumSql+=" sum("+map.get(s)+") "+s+", ";
							}
						}else{
							if(count==set.size()){
								sumSql+=" '"+map.get(s)+"' "+s+" ";
							}else{
								sumSql+=" '"+map.get(s)+"' "+s+", ";
							}
							
						}
						count++;
					}
					sumSql+=" from ("+sql+")";
				}
				
				List sqlList = mapDataDao.queryListBySQL(sumSql);
				midList.add(i,(Map<String,String>)sqlList.get(0));
			}
		}
		
		//将查询出来的list遍历获取map将map的键值设为给定值
		for(int i=0;i<list.size();i++){
			
			Set<String> FiSet = list.get(i).keySet();
			Set<String> midSet = midList.get(i).keySet();
			Map<String,Object> map = new HashMap<String,Object>();
			for(String s:FiSet ){
				for(String ms:midSet){
					if(ms.compareToIgnoreCase(s)==0){
						map.put(s, midList.get(i).get(ms));
					}
				}
				
			}
			reList.add(map);
		}
		return reList;
	}
	/**
	 * 获取总合计
	 * @Title: getAllPageSumRow 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param map
	 * @param @param sql
	 * @param @return 设定文件 
	 * @return List 返回类型 
	 * @throws
	 */
	public List getAllPageSumRow(Map<String,String> map,String sql){
		
		List reList = new ArrayList();
		List<String> cols = mapDataDao.querySqlColumns(sql, null);
		Set<String> set = map.keySet(); 
		int count=1;
		String sumSql = "select ";
		for (String s:set) {
			if(cols.contains(map.get(s).toUpperCase())){
				if(count==set.size()){
					sumSql+=" sum("+map.get(s)+") "+s+" ";
				}else{
					sumSql+=" sum("+map.get(s)+") "+s+", ";
				}
			}else{
				if(count==set.size()){
					sumSql+=" '"+map.get(s)+"' "+s+" ";
				}else{
					sumSql+=" '"+map.get(s)+"' "+s+", ";
				}
				
			}
			count++;
		}
		sumSql+=" from ("+sql+")";
		List sqlList = mapDataDao.queryListBySQL(sumSql);
		Map<String,String> reMap = (Map<String,String>)sqlList.get(0);
		
		//将查询结果的键值跟传过来的键值对应
		Set<String> midSet = reMap.keySet();
		Map<String,Object> midMap = new HashMap<String,Object>();
		for(String s:set ){
			for(String ms:midSet){
				if(ms.compareToIgnoreCase(s)==0){
					midMap.put(s, reMap.get(ms));
				}
			}
			
		}
		reList.add(midMap);
		return reList;
	}
	public List getCurrentPageSumRow(List<Map<String,String>> list,String sql,Integer pageSize,Integer pageIndex){
		return this.getSumRow(list, sql, pageSize, pageIndex);
	}
}
