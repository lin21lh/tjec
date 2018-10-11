package com.wfzcx.aros.tjfx.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.util.ControllerUtil;
import com.wfzcx.aros.tjfx.service.CaseAnalysisService;

@Scope("prototype")
@Controller
@RequestMapping("/aros/tjfx/ChartController")
public class ChartController {

	@Autowired
	CaseAnalysisService caseAnalysisService;
	
	/**
	 * 被申请人类型趋势占比统计
	 * @param request
	 * @return
	 */
	@RequestMapping("/bsqrlxtjInit.do")
	public ModelAndView bsqrlxtjInit(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("aros/chart/bsqrlxtj_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		// 获取系统当前日期
//		String currentYear = FormatUtil.stringDate2().substring(0, 4);
//		mv.addObject("startyear", currentYear);
//		
//		String currentDate = FormatUtil.stringDate2().substring(0, 4);
//		mv.addObject("startdate", currentDate);		
//		mv.addObject("enddate", currentDate);
		return mv;
	}
	
	/**
	 * 申请事项类型趋势占比统计
	 * @param request
	 * @return
	 */
	@RequestMapping("/sqsxlxtjInit.do")
	public ModelAndView sqsxlxtjInit(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("aros/chart/sqsxlxtj_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
	}
	
	/**
	 * 案件数目统计
	 * @param request
	 * @return
	 */
	@RequestMapping("/ajsmtjInit.do")
	public ModelAndView ajsmtjInit(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("aros/chart/ajsmtj_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/bsqrlxtjQuery.do")
	@ResponseBody
	public List<Map> bsqrlxtjQuery(HttpServletRequest request) throws Exception {
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		List<Map<String, Object>> ana = caseAnalysisService.queryCaseDefQSAnalysis(param);
		List data1 = new ArrayList<Object>();
		List data2 = new ArrayList<Object>();
		
		for(Map map:ana){
			data1.add(map.get("name"));
			data2.add(map.get("value"));
		}
		List<Map> rel = new ArrayList<Map>();
		Map op1 = new HashMap<String, Object>();
		Map xAxis1 = new HashMap<String, Object>();
		List series1 = new ArrayList<Object>();
		op1.put("xAxis", xAxis1);
		op1.put("series", series1);
		if(ana.size()>=10){
			List data3 = new ArrayList<Object>();
			for(int i=0;i<ana.size();i++){
				if(i%2 == 1){
					data3.add("\n\t" + ana.get(i).get("name"));
				} else {
					data3.add(ana.get(i).get("name"));
				}
			}
			xAxis1.put("data", data3);
		} else {
			xAxis1.put("data", data1);
		}
		Map series_map1 = new HashMap<String, Object>();
		series1.add(series_map1);
		series_map1.put("data", data2);
		rel.add(op1);
		Map op2 = new HashMap<String, Object>();
		Map legend2 = new HashMap<String, Object>();
		List series2 = new ArrayList<Object>();
		op2.put("legend", legend2);
		op2.put("series", series2);
		legend2.put("data", data1);
		Map series_map2 = new HashMap<String, Object>();
		series2.add(series_map2);
		series_map2.put("data", ana);
		rel.add(op2);
		
		return rel;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/sqsxlxtjQuery.do")
	@ResponseBody
	public List<Map> sqsxlxtjQuery(HttpServletRequest request) throws Exception {
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		List<Map<String, Object>> ana = caseAnalysisService.queryCaseTypeQSAnalysis(param);
		List data1 = new ArrayList<Object>();
		List data2 = new ArrayList<Object>();
		for(Map map:ana){
			data1.add(map.get("name"));
			data2.add(map.get("value"));
		}
		List<Map> rel = new ArrayList<Map>();
		Map op1 = new HashMap<String, Object>();
		Map xAxis1 = new HashMap<String, Object>();
		List series1 = new ArrayList<Object>();
		op1.put("xAxis", xAxis1);
		op1.put("series", series1);
		if(ana.size()>=10){
			List data3 = new ArrayList<Object>();//对x坐标展示做上下间隔处理
			for(int i=0;i<ana.size();i++){
				if(i%2 == 1){
					data3.add("\n\t" + ana.get(i).get("name"));
				} else {
					data3.add(ana.get(i).get("name"));
				}
			}
			xAxis1.put("data", data3);
		} else {
			xAxis1.put("data", data1);
		}
		Map series_map1 = new HashMap<String, Object>();
		series1.add(series_map1);
		series_map1.put("data", data2);
		rel.add(op1);
		Map op2 = new HashMap<String, Object>();
		Map legend2 = new HashMap<String, Object>();
		List series2 = new ArrayList<Object>();
		op2.put("legend", legend2);
		op2.put("series", series2);
		legend2.put("data", data1);
		Map series_map2 = new HashMap<String, Object>();
		series2.add(series_map2);
		series_map2.put("data", ana);
		rel.add(op2);
		
		return rel;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/ajsmtjQuery.do")
	@ResponseBody
	public List<Map> ajsmtjQuery(HttpServletRequest request) throws Exception {
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		List ana = caseAnalysisService.queryCaseSumQSAnalysis(param);
		
		List data_name = new ArrayList<Object>();
		List data_ajzs = new ArrayList<Object>();
		List data_ysj = new ArrayList<Object>();
		List data_ysl = new ArrayList<Object>();
		
		for(Map map:(List<Map>)ana.get(0)){
			data_name.add(map.get("name"));
			data_ajzs.add(map.get("value"));
		}
		for(Map map:(List<Map>)ana.get(1)){
			data_ysj.add(map.get("value"));
		}
		for(Map map:(List<Map>)ana.get(2)){
			data_ysl.add(map.get("value"));
		}
		List<Map> rel = new ArrayList<Map>();
		Map op1 = new HashMap<String, Object>();
		Map xAxis1 = new HashMap<String, Object>();
		List series1 = new ArrayList<Object>();
		op1.put("xAxis", xAxis1);
		op1.put("series", series1);
		if(((List)ana.get(0)).size()>=10){
			List data_name_space = new ArrayList<Object>();
			for(int i=0;i<((List)ana.get(0)).size();i++){
				if(i%2 == 1){
					data_name_space.add("\n\t" + ((List<Map>)ana.get(0)).get(i).get("name"));
				} else {
					data_name_space.add(((List<Map>)ana.get(0)).get(i).get("name"));
				}
			}
			xAxis1.put("data", data_name_space);
		} else {
			xAxis1.put("data", data_name);
		}
		Map series1_map_ajzs = new HashMap<String, Object>();
		Map series1_map_ysj = new HashMap<String, Object>();
		Map series1_map_ysl = new HashMap<String, Object>();
		series1.add(series1_map_ajzs);
		series1.add(series1_map_ysj);
		series1.add(series1_map_ysl);
		series1_map_ajzs.put("data", data_ajzs);
		series1_map_ysj.put("data", data_ysj);
		series1_map_ysl.put("data", data_ysl);
		rel.add(op1);
		
		Map op2 = new HashMap<String, Object>();
		Map legend2 = new HashMap<String, Object>();
		List series2 = new ArrayList<Object>();
		op2.put("legend", legend2);
		op2.put("series", series2);
		legend2.put("data", data_name);
		Map series2_map_ajzs = new HashMap<String, Object>();
		Map series2_map_ysj = new HashMap<String, Object>();
		Map series2_map_ysl = new HashMap<String, Object>();
		series2.add(series2_map_ajzs);
		series2.add(series2_map_ysj);
		series2.add(series2_map_ysl);
		series2_map_ajzs.put("data", ana.get(0));
		series2_map_ysj.put("data", ana.get(1));
		series2_map_ysl.put("data", ana.get(2));
		rel.add(op2);
		
		return rel;
	}
	
	/**
	 * @Title: caseStateInit
	 * @Description: 全市案件办理结果统计：返回统计页面
	 * @author ybb
	 * @date 2016年11月18日 下午2:19:18
	 * @param request
	 * @return
	 */
	@RequestMapping("/caseStateInit.do")
	public ModelAndView caseStateInit(HttpServletRequest request) {
		
		ModelAndView mv = new ModelAndView("aros/chart/casestate_init");
		
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		
		return mv;
	}
	
	/**
	 * @Title: caseStateQuery
	 * @Description: 全市案件办理结果统计：执行查询
	 * @author ybb
	 * @date 2016年11月18日 下午2:53:57
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/caseStateQuery.do")
	@ResponseBody
	public Map<String, Object> caseStateQuery(HttpServletRequest request) throws Exception {
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = caseAnalysisService.queryCaseStateCount(param);
		
		return map;
	}
}
