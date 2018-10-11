package com.freework.base.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;



public class FieldUtil {
	public static void setFieldValue(PropertyDescriptor property,Object obj,Object value){
		try {
			property.getWriteMethod().invoke(obj, value);
		} catch (IllegalArgumentException e) {
			

			e.printStackTrace();
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static Object getFieldValue(PropertyDescriptor property,Object obj){
		Object value=null;
		try {
			value=property.getReadMethod().invoke(obj);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	 public static PropertyDescriptor[] getPropertyDescriptors(Class class1)
	    {
	        if(class1 == null)
	            throw new IllegalArgumentException("No bean class specified");
	        PropertyDescriptor apropertydescriptor[] = null;
	        BeanInfo beaninfo = null;
	        try
	        {
	            beaninfo = Introspector.getBeanInfo(class1);
	        }
	        catch(IntrospectionException introspectionexception)
	        {
	            return new PropertyDescriptor[0];
	        }
	        apropertydescriptor = beaninfo.getPropertyDescriptors();
	        if(apropertydescriptor == null)
	            apropertydescriptor = new PropertyDescriptor[0];
	        return apropertydescriptor;
	    }
	public static Method[] getPropertyMethods(Class cls, String s){
		Method ms[]=new Method[2];
		PropertyDescriptor p=getPropertyDescriptor(cls,s);
		ms[0]=p.getWriteMethod();
		ms[1]=p.getReadMethod();
		return ms;
		
	}
	
	
	
	public static PropertyDescriptor getPropertyDescriptor(Class cls, String s){
		 PropertyDescriptor apropertydescriptor[] = getPropertyDescriptors(cls);
	        if(apropertydescriptor != null)
	        {
	            for(int l = 0; l < apropertydescriptor.length; l++){
	                if(s.equals(apropertydescriptor[l].getName()))
	                    return apropertydescriptor[l];
	            }

	        }
	        return  null;
	}
	 
	
}
