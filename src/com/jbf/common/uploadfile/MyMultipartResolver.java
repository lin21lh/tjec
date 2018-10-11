package com.jbf.common.uploadfile;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class MyMultipartResolver extends CommonsMultipartResolver{

	private String includeUrls;     
    private String[] includeUrlArray;  
      
    public String getIncludeUrls() {  
        return includeUrls;  
    }  
  
    public void setIncludeUrls(String includeUrls) {  
        this.includeUrls = includeUrls;
        if("".equals(this.includeUrls)){
        	this.includeUrlArray = new String[0];
        } else {
        	this.includeUrlArray = includeUrls.split(",");  
        }
    }  
  
    /**  
     * 这里是处理Multipart http的方法。如果这个返回值为true,那么Multipart http body就会MyMultipartResolver 消耗掉.如果这里返回false  
     */  
    @Override  
    public boolean isMultipart(HttpServletRequest request) {  
        for (String url: includeUrlArray) {  
            // 这里可以自己换判断  
            if (request.getRequestURI().contains(url)) { 
            	return super.isMultipart(request);
            }  
        }  
        return false; 
    }
}
