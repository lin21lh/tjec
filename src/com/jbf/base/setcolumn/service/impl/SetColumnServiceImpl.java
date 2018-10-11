/************************************************************
 * 类名：SetColumnServiceImpl.java
 *
 * 类别：Service实现类
 * 功能：列设置服务实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.setcolumn.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.jbf.base.setcolumn.service.SetColumnService;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;


@Service
public class SetColumnServiceImpl implements SetColumnService {

	String path = "com/jbf/base/setcolumn/service";
	
	public SetColumnServiceImpl() {
		setPath("com/jbf/base/setcolumn/service");
	}
	public boolean saveColumnSet(String colopts, String filename, String menuid) {
		
		String[] coltype = colopts.split("￥");
		String columns = null;
		String frozenColumns = coltype[0];
		if (coltype.length == 2)
			columns = coltype[1];
		
		FileOutputStream output=null;
		try {
			File tmp=new File(getUserSetFileNameMkidr(filename,menuid));
			if(!tmp.exists())
				tmp.createNewFile();
			System.out.println(tmp);
			 output=new FileOutputStream(tmp);
			 output.write(getColumnJS(frozenColumns, columns).getBytes("UTF-8"));
			 return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			 return false;
		} catch (IOException e) {
			e.printStackTrace();
			 return false;
		}finally{
			if(output!=null)
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public String getColModelJsFunction(String datagrid, String filename,String menuid) {
		File file=new File(getUserSetFileName(filename,menuid));
		if(!file.isFile()||!file.exists()){
			return "";
		}
		try {
			StringBuilder jsFn=new StringBuilder(datagrid).append(".datagrid({");
			jsFn.append(getFileContent(file));
			jsFn.append("})");
			
			return jsFn.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public boolean deleteColSetJsFile(String filename,String menuid) {
		File file=new File(getUserSetFileName(filename,menuid));
		if(file.isFile() && file.exists()){
			return file.delete();
		}
		return false;
	}
	
	public String getFileContent(File file) throws IOException{
		FileInputStream fis=null;
		try{
		 fis=new FileInputStream(file);
		 int size=fis.available();
 
		 byte[] cb= new byte[size];
		 if(fis.read(cb)!=-1){
			 return  new String(cb,"UTF-8");
		 }else
			 return "";
		}finally{
			if(fis!=null)
				fis.close();
		}
	}
	
	public String getUserSetFileName(String filename,String menuid){
		String usercode=SecureUtil.getCurrentUser().getUsercode();
		StringBuilder filetmpname=new StringBuilder(path).append("/tmp/").append(filename).append("_").append(usercode).append("_").append(menuid).append("_SET.js");
		return filetmpname.toString();
	}
	
	
	public String getColumnJS(String frozenColumns, String columns) {
		StringBuilder js = new StringBuilder();
		
		if (StringUtil.isNotBlank(frozenColumns)) {
			js.append("frozenColumns:[[");
			columnsOpt(js, frozenColumns);
			js.append("]]");
		}
		
		if (StringUtil.isNotBlank(columns)) {
			if (js.length() > 0)
				js.append(",");
			
			js.append(" columns:[[");
			columnsOpt(js, columns);
			js.append("]]");
		}
		
		return js.toString();
	}
	
	public void appendColOpt(StringBuilder js,String name,String[] value, int index, boolean isStr){
		if(value.length > index && StringUtil.isNotBlank(value[index])){
			js.append(name);
			if(isStr)js.append("\"");
				js.append(value[index]);
			if(isStr)js.append("\"");
			js.append(",");
		}
	}
	
	public void columnsOpt(StringBuilder js, String columns) {
		String[] column = columns.split("`");
		boolean pand = false;
		for (String col : column) {
			String[] colOpts = col.split("~");
			if (pand)
				js.append(", ");
			js.append("{");
			appendColOpt(js, "title:", colOpts, 0, true);
			appendColOpt(js, "field:", colOpts, 1, true);
			appendColOpt(js, "width:", colOpts, 2, false);
			appendColOpt(js, "align:", colOpts, 3,  true);
			appendColOpt(js, "resizable:", colOpts, 4, false);
			appendColOpt(js, "sortable:", colOpts, 5, false);
			appendColOpt(js, "hidden:", colOpts, 6, false);
			appendColOpt(js, "order:", colOpts, 7, true);
			appendColOpt(js, "fixed:", colOpts, 8, false);
			appendColOpt(js, "checkbox:", colOpts, 9, false);
			appendColOpt(js, "formatter:", colOpts, 10, false);
			js.deleteCharAt(js.length() - 1);
			js.append("}");
			pand = true;
		}
	}
	
	public String getUserSetFileNameMkidr(String filename,String menuid){
		String usercode=SecureUtil.getCurrentUser().getUsercode();
		StringBuilder filetmpname=new StringBuilder(path).append("/tmp/");
		File tmp=new File(filetmpname.toString());
		if(!tmp.exists())
				tmp.mkdir();
			
		filetmpname.append(filename).append("_").append(usercode).append("_").append(menuid).append("_SET.js");
		return filetmpname.toString();
	}
	
	public void setPath(String path){
		 try {
			 URL url=Thread.currentThread().getContextClassLoader().getResource(path);
			 this.path =java.net.URLDecoder.decode(url.getFile(),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	}
}


