package com.freework.freedbm.util.listUtil.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class TagertParameterName {
	private List<String> names=null;
	private List<Integer> types=null;
	private String tagertName=null;
	private final static int VARIABLE=0;
	private final static int CHARACTER=1;
	private int length=0;
	private int variableSize=0;
	private String oldTagertName=null;
	
	private int 每个变量值的长度=8;//每个变量值的长度
	


	public String getTagertName() {
		return oldTagertName;
	}
	private void nameParse(String str) {
		
		
		int begin = str.indexOf('{');
		int end =0;
		if(begin==-1){
			tagertName=str;
			return;
		}
		names = new ArrayList<String>();
		types=new ArrayList<Integer>();
		while (begin != -1) {
			if(begin!=0){
				end=begin;
				String name=str.substring(0, end);
				length+=name.length();
				names.add(name);
				types.add(CHARACTER);
				str = str.substring(begin, str.length()).trim();
			}else{

				end = str.indexOf('}');
				names.add(str.substring(1, end));
				types.add(VARIABLE);
				str = str.substring(end+1, str.length()).trim();
				variableSize++;
			}
			begin = str.indexOf('{');

			
		}
		if(str.length()!=0){
			names.add(str);
			types.add(CHARACTER);
		}
			
		
		
	}
	public String  getTagertName(Map map){
		if(tagertName!=null)
			return tagertName;
		
		int size=length+variableSize*每个变量值的长度;
		char[] allChar=new char[size];
		int begin=0;
		int count=0;

		for (int i = 0; i < types.size(); i++) {
			int type=types.get(i);
			String name=names.get(i);
			if(type==VARIABLE){
				Object value=map.get(name);
				if(value==null)
					name="";
				else
					name=value.toString();
			}
			count+=name.length();
			if(size<=count){
				每个变量值的长度+=count-size+1;
				 size=count+variableSize*每个变量值的长度;
				 char[] tmp=new char[size];
				 System.arraycopy(allChar, 0, tmp, 0, begin);
				 allChar= tmp;
			}
			name.getChars(0, name.length(), allChar, begin);
			begin=count;

		}
		return new String(allChar,0,count);
	}
	
	public TagertParameterName(String tagertName){
		oldTagertName=tagertName;
		nameParse(tagertName);
	}
	
	
}
