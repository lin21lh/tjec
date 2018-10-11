package com.wfzcx.aros.homepage.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.WebContextFactoryUtil;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.util.GCC;

@Scope("prototype")
@Service("com.wfzcx.aros.homepage.service.HomepageService")
public class HomepageService {

	@Autowired
	private MapDataDaoI mapDataDao;
	
	/*
	 * option1 = {
	    xAxis: {
            data:['案件总数','已完成','已受理','待受理'],
        },
	    series : [
	        {   
	            data:[50,10,30,10]
	        }
	    ]
	};*/
	public Map<String,Object> myChart1Query(List<Map<String, Object>> list){
		List data1_list = new ArrayList<Object>();
		for(Map map:list){
			data1_list.add(map.get("name"));
		}
		Map xAxis_map = new HashMap<String, Object>();
		xAxis_map.put("data", data1_list);
		
		List data2_list = new ArrayList<Object>();
		for(Map map:list){
			data2_list.add(map.get("value"));
		}
		Map series_map = new HashMap<String, Object>();
		series_map.put("data", data2_list);
		List series_list = new ArrayList<Object>();
		series_list.add(series_map);
		
		Map<String,Object> optionMap = new HashMap<String, Object>();
		optionMap.put("xAxis", xAxis_map);
		optionMap.put("series", series_list);
		return optionMap;
	}
	
	/*
	 * option2 = {
	    series : [
	        {
	            data:[
	                {value:335, name:'审批'},
	                {value:310, name:'中止'},
	                {value:0, name:'恢复'},
	                {value:234, name:'终止'},
	                {value:234, name:'和解'},
	                {value:2, name:'延期'}
	            ]
	        }
	    ]
	};*/
	public Map<String,Object> myChart2Query(List<Map<String, Object>> list){
		
		Map series_map = new HashMap<String, Object>();
		series_map.put("data", list);
		List series_list = new ArrayList<Object>();
		series_list.add(series_map);
		
		Map<String,Object> optionMap = new HashMap<String, Object>();
		optionMap.put("series", series_list);
		return optionMap;
	}
	
	/*
	 * option3 = {
	    legend: {
	        data:['待受理','已受理','已完成']
	    },
	    xAxis: {
            data:['2016年8月','2016年7月','2016年6月','2016年5月'],
        },
	    series : [
	        {
	            name:'待受理',
	            data:[50,10,30,10]
	        },{
	            name:'已受理',
	            data:[70,100,2,90]
	        },{
	            name:'已完成',
	            data:[30,50,50,90]
	        }
	    ]
	};*/
	public Map<String,Object> myChart3Query(Map<String,Object> param){
		//此处填充数据
		String[] data_legend = {"待受理","已受理","已完成"};
		Map legend_map = new HashMap<String, Object>();
		legend_map.put("data", data_legend);
		//此处填充数据
		String[] data_xAxis = {"2016年8月","2016年7月","2016年6月","2016年5月"};
		Map xAxis_map = new HashMap<String, Object>();
		xAxis_map.put("data", data_xAxis);
		//此处填充数据
		List series_list = new ArrayList<Object>();
		Map series1_map = new HashMap<String, Object>();
		series1_map.put("name", "待受理");
		int[] data1_series = {40,10,30,10};
		series1_map.put("data", data1_series);
		series_list.add(series1_map);
		Map series2_map = new HashMap<String, Object>();
		series2_map.put("name", "已受理");
		int[] data2_series = {70,100,2,90};
		series2_map.put("data", data2_series);
		series_list.add(series2_map);
		Map series3_map = new HashMap<String, Object>();
		series3_map.put("name", "已完成");
		int[] data3_series = {30,50,50,90};
		series3_map.put("data", data3_series);
		series_list.add(series3_map);
		
		Map<String,Object> optionMap = new HashMap<String, Object>();
		optionMap.put("legend", legend_map);
		optionMap.put("xAxis", xAxis_map);
		optionMap.put("series", series_list);//系列名称需与前端一致
		return optionMap;
	}
	
	public Map<String,Object> myChart4Query(Map<String,Object> param){
		//此处填充数据
		String[] data_legend = {"待办任务","已受理","已完成"};
		Map legend_map = new HashMap<String, Object>();
		legend_map.put("data", data_legend);
		//此处填充数据
		String[] data_xAxis = {"行政处罚","行政强制措施","行政征收","行政许可","行政确权","信息公开","举报投诉处理","行政不作为"};
		Map xAxis_map = new HashMap<String, Object>();
		xAxis_map.put("data", data_xAxis);
		//此处填充数据
		List series_list = new ArrayList<Object>();
		Map series1_map = new HashMap<String, Object>();
		series1_map.put("name", "待办任务");
		int[] data1_series = {112,15,56,12,25,11,34,200};
		series1_map.put("data", data1_series);
		series_list.add(series1_map);
		Map series2_map = new HashMap<String, Object>();
		series2_map.put("name", "已受理");
		int[] data2_series = {30,32,46,21,24,8,30,125};
		series2_map.put("data", data2_series);
		series_list.add(series2_map);
		Map series3_map = new HashMap<String, Object>();
		series3_map.put("name", "已完成"); 
		int[] data3_series = {50,22,12,12,33,19,67,49};
		series3_map.put("data", data3_series);
		series_list.add(series3_map);
		
		Map<String,Object> optionMap = new HashMap<String, Object>();
		optionMap.put("legend", legend_map);
		optionMap.put("xAxis", xAxis_map);
		optionMap.put("series", series_list);//系列名称需与前端一致
		return optionMap;
	}
	
	/**
	 * @Title: queryCaseCount
	 * @Description: 统计当前用户的待办任务数
	 * @author ybb
	 * @date 2016年9月23日 上午10:29:44
	 * @param param
	 * @return
	 * @throws AppException
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<Map<String, Object>>  queryCaseCount(Map<String, Object> param) throws AppException {
		
		//判断当前登录用户是否有流程任务
		
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		//拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("");
		
		
		return null;
	}
	
	/**
	 * 待处理任务案件列表
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> todo(Map<String, Object> param) {
		SysUser user = SecureUtil.getCurrentUser();
		Long userid  = user.getUserid();
		ParamCfgComponent pcfg = (ParamCfgComponent)WebContextFactoryUtil.getBean("sys.paramCfg.component.ParamCfgComponentImpl");
		String dayNum = pcfg.findGeneralParamValue("SYSTEM", "CASEDEALNUMDAY");
		String calculate = "ROUND(" + dayNum +  "- TO_NUMBER(SYSDATE-to_date(appdate,'yyyy-MM-dd')))";
		StringBuffer sql = new StringBuffer();
		sql.append("select t.caseid, t.csaecode, t.appname, t.apptype, t.idtype, t.idcode, t.mobile, t.phone, ");
		sql.append("t.address, t.email, t.postcode, t.deftype, t.defname, t.depttype, t.defidtype, t.defidcode, t.defmobile, ");
		sql.append("t.defphone, t.defmailaddress, t.defaddress, t.defemail, t.defpostcode, t.noticeddate, t.actnoticeddate, ");
		sql.append("t.admtype, t.casetype, t.ifcompensation, t.amount, t.appcase, t.factreason, t.annex, t.appdate, t.operator, ");
		sql.append("t.optdate, t.protype, t.opttype, t.nodeid, t.lasttime, t.userid, t.oldprotype, t.receivedate, t.receiveway, ");
		sql.append("t.expresscom, t.couriernum, t.datasource, t.mailaddress, (case when ");
		sql.append(calculate).append(" > 0 then ").append(calculate).append(" else 0 end) as day," );
		sql.append(" n.protype||'_'||n.nodeid as key, n.proname, n.nodename");
		sql.append(" FROM b_casebaseinfo t, pub_pronodebaseinfo n where t.protype = n.protype and t.nodeid=n.nodeid ");
		sql.append("and n.roleid in (SELECT ROLEID FROM SYS_USER_ROLE WHERE USERID='");
		sql.append(userid);
		sql.append("') and (t.state = '").append(GCC.RCASEBASEINFO_STATE_REQ)
				.append("' or t.state='").append(GCC.RCASEBASEINFO_STATE_NOCLOSURE)
				.append("' or t.state is null) ");
		sql.append(" order by appdate");
		return mapDataDao.queryListBySQL(sql.toString());
	}
	
	/**
	 * @Title: myChart5Query
	 * @Description: 所属区域统计:查询案件情况数量以及占比
	 * @author ybb
	 * @date 2016年11月17日 下午4:33:11
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> myChart5Query(Map<String,Object> param){
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		//此处填充数据
			//查询行政区代码和名称
		sql.append("select t1.name, nvl(t2.percent,0) percent");
		sql.append(" from （select code, name from SYS_DICENUMITEM where elementcode = 'SYS_AREA') t1 ");
		sql.append(" left join (select a.region, a.sucessnum, round((sucessnum/allnum)*100,2) percent ");
		sql.append(" from (select region, count(caseid) sucessnum from B_CASEBASEINFO group by region) a, ");
		sql.append(" (select count(caseid) allnum from B_CASEBASEINFO) b) t2 on t1.code = t2.region ");
		
		List<JSONObject> dicenumitemList = mapDataDao.queryListBySQL(sql.toString());
		if (null == dicenumitemList || dicenumitemList.isEmpty()) {
			return null;
		}
		
		//填充X轴数据
		List<Object> data1_list = new ArrayList<Object>();
		for(JSONObject obj : dicenumitemList){
			data1_list.add(obj.getString("name"));
		}
		Map<String, Object> xAxis_map = new HashMap<String, Object>();
		xAxis_map.put("data", data1_list);
		
		//填充Y轴数据
		List<Object> data2_list = new ArrayList<Object>();
		for(JSONObject obj : dicenumitemList){
			
			data2_list.add(obj.getString("percent"));
		}
		
		Map<String, Object> series_map = new HashMap<String, Object>();
		series_map.put("data", data2_list);
		
		List<Object> series_list = new ArrayList<Object>();
		series_list.add(series_map);
		
		Map<String,Object> optionMap = new HashMap<String, Object>();
		optionMap.put("xAxis", xAxis_map);
		optionMap.put("series", series_list);
		
		return optionMap;
	}
	
	/**
	 * @Title: queryCasebaseinfoCount
	 * @Description: 所属区域统计:统计案件个数
	 * @author ybb
	 * @date 2016年11月18日 上午11:39:04
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JSONObject> queryCasebaseinfoCount(Map<String, Object> param) {
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		//此处填充数据
			//查询行政区代码和名称
		sql.append("select t1.name, nvl(t2.total,0) total");
		sql.append(" from (select code, name from SYS_DICENUMITEM where elementcode = 'SYS_AREA') t1 ");
		sql.append(" left join (select region, count(caseid) total from B_CASEBASEINFO t group by t.region) t2 on t1.code = t2.region");
		
		List<JSONObject> dicenumitemList = mapDataDao.queryListBySQL(sql.toString());
		if (null == dicenumitemList || dicenumitemList.isEmpty()) {
			return null;
		}
		
		return dicenumitemList;
	}
}
