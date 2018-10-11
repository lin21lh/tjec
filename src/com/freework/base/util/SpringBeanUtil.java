
package com.freework.base.util;

import java.lang.reflect.Method;

public class SpringBeanUtil
   
{
	private SpringBeanUtil(){}

	private static  SpringBeanFind findBean=null;
	
	public static SpringBeanFind bean(){
		return  findBean;
	}
	

    public  void setFindBean(SpringBeanFind findBean) {
		SpringBeanUtil.findBean = findBean;
	}

	public static Object getBean(String name)
    {
        return (findBean==null?null:findBean.getBeanByName(name));
    }
   



   
}