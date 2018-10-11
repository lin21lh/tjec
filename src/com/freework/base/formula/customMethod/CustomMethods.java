package com.freework.base.formula.customMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freework.base.util.LoadDirClass;

public class CustomMethods {
	static	Map<String,CustomMethod> methodMap=new HashMap<String,CustomMethod>();
	static{
			
			LoadDirClass ldc=new LoadDirClass( CustomMethod.class);
			Collection<CustomMethod> customMethods= ldc.getObjects();
			for (CustomMethod customMethod : customMethods) {
				methodMap.put(customMethod.getMethodName(), customMethod);
			}
		
	}
	public static boolean isCustomMethod (String methodName){
		return methodMap.containsKey(methodName);
	}
	
	
	public static CustomMethod getCustomMethod(String name,String[] info){
		return methodMap.get(name).newCustomMethod(info);
		
	}
	
	
	
	
	public static CustomMethod getCustomMethod(String formula){
		int startIndex=formula.indexOf('$');
		int beginIndex=formula.indexOf('{');
		int endIndex=formula.lastIndexOf('}');
		if(beginIndex==-1)
	    	  throw  new IllegalArgumentException(formula+"无法解析");
		if(endIndex==-1)
	    	  throw  new IllegalArgumentException(formula+"无法解析");
		String methodName=formula.substring(startIndex+1, beginIndex);
		String info=formula.substring(beginIndex+1, endIndex);
		return getCustomMethod(methodName,methodArgs(info));
		
	}
	/**
	 * 解析方法中的参数
	 * @param info参数
	 * @return
	 */
	public static String[] methodArgs(String info){
	
		if(info.indexOf("$")==-1)
			return info.split(",");
		else{
			List <String>list=new ArrayList<String>();
			String str=replaceMethod(info,list);
			 String[] args=str.split(",");
			 for (int i = 0; i < args.length; i++) {
				 int index=args[i].indexOf("@#customMethod_");
				if(index!=-1){
						String string=args[i].substring(index+15);
						args[i]=
							args[i].substring(0,index)
							+list.get(Integer.parseInt(string.substring(0, string.indexOf("#_"))))
							+string.substring(string.indexOf("#_")+2);
				}
			}
			return args;
		}
	}
	
	
	/**
	 * 替换参数字符串中的方法 替换为@#customMethod_*#_ *为list的索引号
	 * @param argsStr 参数字符串
	 * @param list 公式字符串
	 * @return
	 */
	public static String replaceMethod(String argsStr,List<String> list){
		List<String> customMethodStrList=getCustomMethodStrList(argsStr);
		System.out.println(customMethodStrList+"="+argsStr);
		for (int i=0;i<customMethodStrList.size();i++) {
			
			String  customMethodStr=customMethodStrList.get(i);
			list.add("$"+customMethodStr+"}");
			int indexfCustomMethod=argsStr.indexOf(customMethodStr);

			argsStr=argsStr.substring(0,indexfCustomMethod-1)
			+"@#customMethod_"+i+"#_"+argsStr.substring(indexfCustomMethod+customMethodStr.length()+1);

		}
		System.out.println(list+"====="+argsStr);
		return argsStr;
		
	}
	public static void main(String args[]){
		//String str="$if{b+1=c,$if{b+$if{1=1,1,10}=c,c+1,$if{1=1,1,10}},10}+$if{1-1=0,2,3}";
		String str="$regex{/^\\d{1,4}$|^\\d{1,4}[.]\\d{1,2}$/,sds}";
		List list=new ArrayList();
		System.out.println(analyseCustomMethod(str,list));
		System.out.println(list);
	}
	public static List<String> getCustomMethodStrList(String str) {
		List<String> list = new ArrayList<String>();
		int j=0;
		int index=-1;
		for (int i = 0; i < str.length(); i++) {
			if ( str.charAt(i) == '$') {
				if(j==0)
					index=i;
				//j++;
			} else if ( str.charAt(i) == '{') {
					j++;
			} else if (str.charAt(i) == '}') {
				j--;
				if(j==0&&index!=-1)
					list.add(str.substring(index+1,i));
			}

		}
		return list;

	}
	public static String analyseCustomMethod(String formula,List<CustomMethod> list){
		List<String> customMethodStrList=getCustomMethodStrList(formula);
		for (int i=0;i<customMethodStrList.size();i++) {
			String  customMethodStr=customMethodStrList.get(i);
			
			int index=customMethodStr.indexOf("{");
			if(index==-1)
		    	  throw  new IllegalArgumentException(formula+"无法解析");
			String methodName=customMethodStr.substring(0, index);
			
			String info=customMethodStr.substring(index+1);
			
			String[] args=methodArgs(info);
			
			list.add(getCustomMethod(methodName,args));
			
			int indexfCustomMethod=formula.indexOf(customMethodStr);
			
			formula=formula.substring(0,indexfCustomMethod-1)
			+"@#customMethod_"+i+formula.substring(formula.indexOf(customMethodStr)+customMethodStr.length()+1);
		}
		return formula;
		
	}
}
