package com.wfzcx.aros.xzfy.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wfzcx.aros.util.GCC;

/**
 * app端service方法
 * 
 * @author zhaoxd
 *
 */
@Scope("prototype")
@Service("com.wfzcx.aros.xzfy.service.WebDealService")
public class WebDealService {
	
	@Autowired
	private CasebaseinfoService casebaseinfoService;
	/**
	 * 保存方法
	 * @return
	 */
	public Map<String,Object> saveInfo(Map<String, Object> param){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		String result = "success";
		String nodeid = (String) param.get("nodeid");
		String caseid = (String) param.get("caseid");
		// 材料接收
		if(StringUtils.equals(nodeid, String.valueOf(GCC.PRONODEBASEINFO_NODEID_REQRECEIVE))){
			try {
				caseid = casebaseinfoService.updateXzfyReceiveByCaseid(param);
					
			} catch (Exception e) {
				e.printStackTrace();
				result = "fail";
			}
		// 案件登记
		}else if(StringUtils.equals(nodeid, String.valueOf(GCC.PRONODEBASEINFO_NODEID_REQ))) {
			try {
				casebaseinfoService.updateXzfyReqByCaseid(param);
			} catch (Exception e) {
				e.printStackTrace();
				result = "fail";
			}
		// 承办人审核
		}else if(StringUtils.equals(nodeid, String.valueOf(GCC.PRONODEBASEINFO_NODEID_ACCEPT))) {
			try {
				casebaseinfoService.updateXzfyAcceptByCaseid(param);
			} catch (Exception e) {
				e.printStackTrace();
				result = "fail";
			}
		}
		resultMap.put("caseid",caseid);
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 发送方法
	 * @return
	 */
	public Map<String,Object> sendInfo(Map<String, Object> param){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		String result = "success";
		String nodeid = (String) param.get("nodeid");
		String caseid = (String) param.get("caseid");
		// 材料接收
		if(StringUtils.equals(nodeid, String.valueOf(GCC.PRONODEBASEINFO_NODEID_REQRECEIVE))){
			try {
				casebaseinfoService.xzfyReceiveFlow(param);
			} catch (Exception e) {
				e.printStackTrace();
				result = "fail";
			}
		// 案件登记
		}else if(StringUtils.equals(nodeid, String.valueOf(GCC.PRONODEBASEINFO_NODEID_REQ))) {
			try {
				casebaseinfoService.xzfyReqFlow(param);
			} catch (Exception e) {
				e.printStackTrace();
				result = "fail";
			}
		// 承办人审核
		}else if(StringUtils.equals(nodeid, String.valueOf(GCC.PRONODEBASEINFO_NODEID_ACCEPT))) {
			try {
				casebaseinfoService.xzfyAcceptFlow(param);
			} catch (Exception e) {
				e.printStackTrace();
				result = "fail";
			}
		}
		resultMap.put("caseid", caseid);
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 手机端提交案件信息
	 * @return
	 */
   public Map<String,Object> webAddCaseInfo(Map<String, Object> param){
	   Map<String,Object> resultMap = new HashMap<String, Object>();
		String result = "success";	
		String caseid = "";
		String appname = (String) param.get("appname");
		String csaecode = (String) param.get("csaecode");
		if(StringUtils.isEmpty(csaecode)){
			csaecode = "临时案件编号-"+ appname;
			param.put("csaecode", csaecode);
		}
	
		try {
			System.out.println(param);
			caseid = casebaseinfoService.updateXzfyReceiveByCaseid(param);
		} catch (Exception e) {
			e.printStackTrace();
			result = "fail";
		}
		resultMap.put("caseid", caseid);
		resultMap.put("result", result);
		return resultMap;
   }

}
