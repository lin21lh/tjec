/************************************************************
 * 类名：SetColumnController.java
 *
 * 类别：Controller
 * 功能：提供保存列设置、还原列设置功能
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.setcolumn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbf.base.setcolumn.service.SetColumnService;
import com.jbf.common.exception.AppException;
import com.jbf.common.web.ResultMsg;

@Scope("prototype")
@Controller
@RequestMapping({"/base/setcolumn/setColumnController"})
public class SetColumnController {
	
	@Autowired
	SetColumnService setColumnService;
	
	/**
	 * 保存列设置
	 * @param colopts datagrid列字符串
	 * @param filename 文件名称
	 * @param menuid 菜单ID
	 * @return ResultMsg
	 */
    @ResponseBody
    @RequestMapping("/saveColumnSet.do")
    public ResultMsg saveColumnSet(String colopts, String filename, String menuid) {
        ResultMsg msg = null;
        try {
        	setColumnService.saveColumnSet(colopts, filename, menuid);
            msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
        }
        catch (Exception e) {
            e.printStackTrace();
            msg = new ResultMsg(true, AppException.getMessage("crud.saveerr"));
        }
        return msg;
    }
    
    /**
     * 获取设置列
     * @param datagrid datagrid ID
     * @param filename 文件名称
     * @param menuid 功能菜单ID
     * @return datagrid 列
     */
    @ResponseBody
    @RequestMapping("/getColumnSet.do")
    public String getColumnSet(String datagrid, String filename, String menuid) {
    	
    	return setColumnService.getColModelJsFunction(datagrid, filename, menuid);
    }
    
    /**
     * 还原列设置
     * @param filename 文件名称
     * @param menuid 功能菜单ID
     * @return ResultMsg 还原列设置结果
     */
    @ResponseBody
    @RequestMapping("/resetColumnSet.do")
    public ResultMsg resetColumnSet(String filename, String menuid) {
    	ResultMsg resultMsg = null;
    	boolean isSuccess = setColumnService.deleteColSetJsFile(filename, menuid);
    	if (isSuccess)
    		resultMsg =  new ResultMsg(isSuccess, AppException.getMessage("setColumn.resetok"));
    	else
    		resultMsg =  new ResultMsg(false, AppException.getMessage("setColumn.reseterr"));
    	
    	return resultMsg;
    }
}
