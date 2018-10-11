package com.freework.base.exception;


import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;



public class AppErrMessage {

	
	private static MessageSourceAccessor messageSource;
	@Resource
	public void setMessageSource(MessageSource messageSource) {
	        this.messageSource = new MessageSourceAccessor(messageSource);
	 }
	 public static String getMessage(String code,String[] args){
			
			return getMessage(code,code,args);
		}
	public static String getMessage(String code,String defaultMessage,String[] args){
			return messageSource.getMessage(code, args, defaultMessage);
	}
}
