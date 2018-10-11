package com.jbf.base.filemanage.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.util.StringUtil;

public class UpLoadFile {
	static UpLoadFile up=new UpLoadFile();
	public static UpLoadFile getUpLoadFile() {
		return up;
	}
	
	public List readRequestInputStreams(HttpServletRequest request, Long sizeMax) {//上传多个文件时
		DiskFileItemFactory factory = new DiskFileItemFactory(); 
		File file = new File(UpLoadFile.class.getClassLoader().getResource("").getPath().replaceAll("%20", " ") + "/fileupload/tmp");
        if (!file.exists())
        	file.mkdirs();
        
		factory.setRepository(file);
		ServletFileUpload fu = new ServletFileUpload(factory);
		 
		fu.setSizeMax(sizeMax);
		FileItem fi;
		
		HashMap paramMap = (HashMap) ControllerUtil.getRequestParameterMap(request);
		
		List list=new ArrayList();
		try {
			List list2=fu.parseRequest(request);
			UpLoadFileDTO udata = new UpLoadFileDTO();
			
			for(int i=0;i<list2.size();i++){
				fi = (FileItem)list2.get(i) ;
				
				String name=fi.getName();
				if(name!=null&&fi.getContentType()!=null) {
					int index=name.lastIndexOf('\\');
					index=index==-1?name.lastIndexOf('/'):index;
					if(index!=-1)
						name=name.substring(index+1);
					udata.setInput(fi.getInputStream());
					udata.setFilesize(fi.getSize());
					udata.setFiletype(fi.getContentType());
					udata.setFileName(name);
					udata.setFieldName(fi.getFieldName());
					udata.putAll(paramMap);
					list.add(udata);
					udata=new UpLoadFileDTO();
				}else {
					String str=fi.getString();
					udata.put(fi.getFieldName(),new String(str.getBytes("ISO-8859-1"),"UTF-8"));
				}
			}
		} catch (FileUploadException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * @Title: readRequestInputStreamsForPhotos
	 * @Description: 拍照上传处理
	 * @author ybb
	 * @date 2017年4月5日 下午5:22:37
	 * @param request
	 * @param sizeMax
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("resource")
	public List readRequestInputStreamsForPhotos(HttpServletRequest request, Long sizeMax) throws AppException, IOException{//上传多个文件时
	     
		//获取页面参数
		HashMap paramMap = (HashMap) ControllerUtil.getRequestParameterMap(request);
		//文件名称
		String filename = StringUtil.stringConvert(paramMap.get("filename"));
		//文件路径
		String filepath = StringUtil.stringConvert(paramMap.get("filepath"));
		
		//判断文件路径是否为空
		if (StringUtils.isBlank(filepath)) {
			throw new AppException("上传失败：请重新拍照后再上传");
		}
		
		//获取上传文件对象，并判断文件是否存在
		File file = new File(filepath);
		if (!file.exists()) {
			throw new AppException("上传失败：请重新拍照后再上传");
		}
		
		//获取文件输入流
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new AppException("上传失败：请重新拍照后再上传");
		}  
		
		//封装上传文件对象
		List<UpLoadFileDTO> list = new ArrayList<UpLoadFileDTO>();
		
		UpLoadFileDTO udata = new UpLoadFileDTO();
		udata.setInput(in);
		udata.setFilesize(in.getChannel().size());
		//udata.setFiletype(fi.getContentType());
		
		//获取文件名称
		String name = filepath;
		int index = name.lastIndexOf('\\');
		index = index == -1 ? name.lastIndexOf('/') : index;
		if(index != -1) {
			name = name.substring(index + 1);
		}
		
		udata.setFileName(name);
		udata.setFieldName(filename);
		udata.putAll(paramMap);
		
		list.add(udata);
		
		return list;
	}
	
	public class UpLoadFileDTO {
		HashMap mapvalue = new HashMap();
		InputStream input = null;
		String fileName = null;
		String fieldName = null;
		long filesize=0L;
		String filetype="";
		public UpLoadFileDTO() {}
		
    	public	 UpLoadFileDTO(InputStream input,String fileName,long filesize,String filetype){
			this.input = input;
			this.fileName = fileName;
			this.filesize = filesize;
			this.filetype = filetype;
		}
    	
		public HashMap getmap() {
			return mapvalue;
		}
		public void putAll(HashMap map) {
			mapvalue.putAll(map);
		}
		public void put(String key,String value) {
			mapvalue.put(key, value);
		}
		public String getmap(String key) {
			return (String) mapvalue.get(key);
		}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public InputStream getInput() {
			return input;
		}
		public void setInput(InputStream input) {
			this.input = input;
		}
		public long getFilesize() {
			return filesize;
		}
		public void setFilesize(long filesize) {
			this.filesize = filesize;
		}
		public String getFiletype() {
			return filetype;
		}
		public void setFiletype(String filetype) {
			this.filetype = filetype;
		}
		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
	}
}
