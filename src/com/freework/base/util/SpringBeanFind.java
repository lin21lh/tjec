package com.freework.base.util;

import java.lang.reflect.Method;

public interface SpringBeanFind {
	 public String[] getBeanNamesForType(Class clazz);
	 public  String getBeanName(Class clazz);
	 public  Object getBeanByName(String name);
	 public  Method getMethod(Class clazz, String methodName);
}
