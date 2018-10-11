package com.wfzcx.ppms.indexPage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.util.StringUtil;
import com.wfzcx.ppms.indexPage.service.IndexPageService;

@Scope("prototype")
@Service("com.wfzcx.ppms.indexPage.service.impl.IndexPageServiceImpl")
public class IndexPageServiceImpl implements IndexPageService {

	@Autowired
	MapDataDaoI mapDataDao;
	
	
	/**
	 * 图表1的查询
	 * @param param
	 * @return
	 */
	public Map<String,Object> myChart1Query(Map<String,Object> param){
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*, nvl(b.sl, 0) sl,nvl(b.amount,0) amount")
		.append(" from (select s.code, s.name return_name")
		.append(" from sys_yw_dicenumitem s")
		.append(" where upper(s.elementcode) = 'PRORETURN'")
		.append(" and s.status = 0) a,")
		.append(" (select pro_return code,")
		.append(" count(1) sl ,sum(amount) amount from pro_project p where substr(pro_sendtime, 0, 4) = '"+param.get("year")+"' group by pro_return) b")
		.append(" where a.code = b.code(+) order by a.code");
		
		List list = mapDataDao.queryListBySQL(sql.toString());
		List data1 = new ArrayList<Map>();
		List data2 = new ArrayList<Map>();
	
		for (int i=0;i<list.size();i++){
			Map map1 = new HashMap<String, Object>();
			Map map2 = new HashMap<String, Object>();
			map1.put("name", ((Map)list.get(i)).get("return_name"));
			map1.put("value", ((Map)list.get(i)).get("sl"));
			
			map2.put("name", ((Map)list.get(i)).get("return_name"));
			map2.put("value", ((Map)list.get(i)).get("amount"));
			
			data1.add(map1);
			data2.add(map2);
		}
		Map reMap = new HashMap<String,Object>();
		reMap.put("data1", data1);
		reMap.put("data2", data2);
		return reMap;
	}
	
	/**
	 * 图表2查询
	 * @param param
	 * @return
	 */
	public Map<String,Object> myChart2Query(Map<String,Object> param){
		Map reMap = new HashMap<String,Object>();//返回页面的map
		
		StringBuffer fruit1_sql = new StringBuffer("select s.code,s.name from sys_yw_dicenumitem s where upper(s.elementcode) = 'PROXMDQHJ' and s.status = 0 order by s.code");
		List<Map<String,String>> fruit1 = (List<Map<String,String>>)mapDataDao.queryListBySQL(fruit1_sql.toString());
		String[] datax_str = new String[fruit1.size()];
		String[] datax_value = new String[fruit1.size()];
		for(int i=0;i<datax_value.length;i++){
			datax_str[i] = fruit1.get(i).get("code");
			datax_value[i] = fruit1.get(i).get("name");
		}
		Map xaxis_value = new HashMap<String, Object>();
		xaxis_value.put("data", datax_value);
		reMap.put("xAxis", xaxis_value);
		
		StringBuffer fruit2_sql = new StringBuffer("select s.code,s.shortname from sys_yw_dicenumitem s where upper(s.elementcode) = 'PROOPERATE' and s.status = 0 order by s.code");
		List<Map<String,String>> fruit2 = (List<Map<String,String>>)mapDataDao.queryListBySQL(fruit2_sql.toString());
		String[] datal_str = new String[fruit2.size()];
		String[] datal_value = new String[fruit2.size()];
		for(int i=0;i<datal_value.length;i++){
			datal_str[i] = fruit2.get(i).get("code");
			datal_value[i] = fruit2.get(i).get("shortname");
		}
		Map legend_value = new HashMap<String, Object>();
		legend_value.put("data", datal_value);
		reMap.put("legend", legend_value);
		
		
		List<Map> series_value_list = new ArrayList<Map>();
		for(int i=0;i<datal_str.length;i++){
			StringBuffer fruit3_sql = new StringBuffer();
			fruit3_sql.append("select a.*, nvl(b.amount, 0) amount")
			.append(" from (select s.code, s.name xmdqhj")
			.append(" from sys_yw_dicenumitem s")
			.append(" where upper(s.elementcode) = 'PROXMDQHJ'")
			.append(" and s.status = 0) a,")
			.append(" (select xmdqhj code,")
			.append(" sum(p.amount) amount  from pro_project p where substr(pro_sendtime, 0, 4) = '"+param.get("year")+"' and p.pro_perate='"+datal_str[i]+"' group by xmdqhj) b")
			.append(" where a.code = b.code(+) order by a.code");
			List<Map<String,Object>> fruit3 = (List<Map<String,Object>>)mapDataDao.queryListBySQL(fruit3_sql.toString());
			String[] data_value = new String[fruit3.size()];
			for(int j=0;j<data_value.length;j++){
				data_value[j] = fruit3.get(j).get("amount").toString();
			}
			Map series_value_map = new HashMap<String, Object>();
			series_value_map.put("name", datal_value[i]);//此处需与前端名称一致
			series_value_map.put("data", data_value);
			
			series_value_list.add(series_value_map);
		}
		reMap.put("series", series_value_list);
		
		return reMap;
	}
	
	/**
	 * 图表3查询
	 * @param param
	 * @return
	 */
	public Map<String,Object> myChart3Query(Map<String,Object> param){
		Map reMap = new HashMap<String,Object>();//返回页面的map
		int year = Integer.valueOf((String)param.get("year"));
		int timeSpan = Integer.valueOf((String)param.get("timeSpan"));
		
		Map baseoption_value = new HashMap<String, Object>();
		
		StringBuffer fruit1_sql = new StringBuffer(" select s.code code,s.shortname name from sys_yw_diccodeitem s where  upper(s.elementcode) = 'PROTRADE' and s.status = 0 and s.levelno = 1");
		List<Map<String,String>> fruit1 = (List<Map<String,String>>)mapDataDao.queryListBySQL(fruit1_sql.toString());
		String[] datax_str = new String[fruit1.size()];
		String[] datax_value = new String[fruit1.size()];
		for(int i=0;i<datax_value.length;i++){
			datax_str[i] = fruit1.get(i).get("code");
			if (i%2 == 1){
				datax_value[i] = "\n\t" + fruit1.get(i).get("name");
			} else {
				datax_value[i] = fruit1.get(i).get("name");
			}
		}
		Map xaxis_value = new HashMap<String, Object>();
		xaxis_value.put("data", datax_value);
		baseoption_value.put("xAxis", xaxis_value);
		
		
		String[] datat_value = new String[timeSpan];
		for(int i=0;i<timeSpan;i++){
			datat_value[i] = year + i + "-01-01"; 
		}
		Map timeline_value = new HashMap<String, Object>();
		timeline_value.put("data", datat_value);
		baseoption_value.put("timeline", timeline_value);
		
		List options_value_list = new ArrayList<Map>();
		StringBuffer fruit2_sql = new StringBuffer();
		fruit2_sql.append("select t1.budget_year,sum(t1.je)");
		for(int i=0;i<datax_str.length;i++){
			fruit2_sql.append(",sum(case when substr(t.pro_trade,0,2) ='"+datax_str[i]+"' then t1.je else 0 end ) \""+datax_str[i]+"\"");
		}
		fruit2_sql.append("")
		.append(" from pro_project t,")
		.append(" (select a.projectid,a.budget_year, case when aa = 0 then  bb  else aa end je")
		.append(" from (select x.projectid,x.budget_year,  sum(case when x.xmhj = 2 then  x.total else  0 end) aa,")
		.append(" sum(case  when x.xmhj = 1 then  x.total  else 0 end) bb from pro_budget x")
		.append(" where x.budget_year >= '"+year+"' and x.budget_year <'"+(year + timeSpan)+"' group by x.projectid,x.budget_year) a) t1")
		.append(" where t.projectid = t1.projectid")
		.append(" group by t1.budget_year order by t1.budget_year");
		List<Map<String,Object>> fruit2 = (List<Map<String,Object>>)mapDataDao.queryListBySQL(fruit2_sql.toString());
		for(int i=year,j=0;i<year + timeSpan;i++){
			Map options_value_map = new HashMap<String, Object>();
			Map title_value = new HashMap<String, Object>();
			title_value.put("text", i + "年各行业财政预算支出");
			
			List series_value_list = new ArrayList<Map>();
			Map series_value_map = new HashMap<String, Object>();
			String[] data_value = new String[datax_str.length];//行业数量长度
			if (j < fruit2.size() && i == Integer.parseInt(fruit2.get(j).get("budget_year").toString())){
				for(int p=0;p<datax_str.length;p++){
					data_value[p] = fruit2.get(j).get(datax_str[p]).toString();
				}
				j++ ;
			} else {
				for(int p=0;p<datax_str.length;p++){
					data_value[p] = "0";
				}
			}
			series_value_map.put("data", data_value);
			series_value_list.add(series_value_map);
			
			options_value_map.put("title", title_value);
			options_value_map.put("series", series_value_list);
			
			options_value_list.add(options_value_map);
			
		}
		
		
		reMap.put("baseOption", baseoption_value);
		reMap.put("options", options_value_list);
		return reMap;
	}
}
