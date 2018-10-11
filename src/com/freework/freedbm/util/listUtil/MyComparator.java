package com.freework.freedbm.util.listUtil;

import java.util.Comparator;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;



public abstract  class MyComparator<T> implements Comparator<T> {

	String[] fieldNames=null;
	boolean isDESC=false; 
	public MyComparator(String... fieldNames){
		this.fieldNames=fieldNames;
	}
	public MyComparator(boolean isDESC,String... fieldNames){
		this.fieldNames=fieldNames;
		this.isDESC=isDESC;
	}

	public abstract Object getValue(String fieldName,T dto);
	public int myCompare(T o1, T o2){
		for (int i = 0; i < fieldNames.length; i++) {
			Object value1=getValue(fieldNames[i],o1);
			Object value2=getValue(fieldNames[i],o2);
			Comparable comparable1=null;

			if(value1 instanceof Comparable){
				comparable1=(Comparable)value1;
			}else if(value1==null){
				if(value2!=null)
						return -1;
			}else
				comparable1=value1.toString();


			 if(value2==null){
				if(value1!=null)
					return 1;
			 }
			if(comparable1!=null&&value2!=null){
				if(comparable1.getClass()!=value2.getClass()){
					Converter converter=ConvertUtils.lookup(comparable1.getClass());
					if(converter!=null)
						value2=converter.convert(value2.getClass(), value2);
					else
						value2=null;
					
				}			
				if(value2==null)
					return -1;
				
				int e=comparable1.compareTo(value2);
				if(e!=0)
					return e;
			}
				
		}
		return 0;
	}
	
	
	public int compare(T o1, T o2) {
		if(isDESC){
			return myCompare(o2,o1);
		}else{
			return myCompare(o1,o2);
		}
	}


}
