package com.freework.base.formula;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

public class Property {

	private final static Map<Class,Map<String,Method[]>> classCache=new HashMap<Class,Map<String,Method[]>>();
	
	public static void setPropertyValue(Object obj,String name,Object value){
		
		try {
			 getMethods(obj.getClass(),name)[0].invoke(obj,value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	
	
	
	public static Object getPropertyValue(Object obj,String name){
		try {
			return getMethods(obj.getClass(),name)[1].invoke(obj);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	private static Method[] getMethods(Class clazz,String name){
		Map<String,Method[]> map=	classCache.get(clazz);
		if(map==null){
			map=getMapReadMethod(clazz);
			classCache.put(clazz, map);
		}
		return map.get(name);
		
	}
	private static Map<String,Method[]> getMapReadMethod(Class clazz){
		
		Map<String,Method[]> map=new HashMap<String,Method[]>();
		
		PropertyDescriptor	ps[]=PropertyUtils.getPropertyDescriptors(clazz);
		for (PropertyDescriptor propertyDescriptor : ps) {
			
			map.put(propertyDescriptor.getName(), new Method[]{propertyDescriptor.getWriteMethod(),propertyDescriptor.getReadMethod()});
		}
		return map;
	}
}
