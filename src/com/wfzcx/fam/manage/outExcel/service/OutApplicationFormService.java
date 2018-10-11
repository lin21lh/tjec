package com.wfzcx.fam.manage.outExcel.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface OutApplicationFormService {
	
	/**
	 * 导出开立申请表
	 * @Title: outRegisterAppicationForm 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param work
	 * @param @param in
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return HSSFWorkbook 返回类型 
	 * @throws
	 */
	public HSSFWorkbook outRegisterAppicationForm(Map<String,Object> map) throws FileNotFoundException,IOException;
	
	/**
	 * 
	 * @Title: outRecordAppicationForm 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param map
	 * @param @return
	 * @param @throws FileNotFoundException
	 * @param @throws IOException 设定文件 
	 * @return HSSFWorkbook 返回类型 
	 * @throws
	 */
	public HSSFWorkbook outRecordAppicationForm(Map<String,Object> map) throws FileNotFoundException,IOException;
}
