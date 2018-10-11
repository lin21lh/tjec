package com.freework.queryData.compileSQL.simpleSQL;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class MyList<T> extends AbstractList<T> {

	ArrayList<List> list=new ArrayList<List>();
	ArrayList<int[]> scopes=new ArrayList<int[]>();
	
	int size=0;
	@Override
	public T get(int index) {
		int i=0;
		for (int[] scope : scopes) {
			if(scope[0]<=index&&scope[1]>index){
				if(i!=0){
					index=index-scope[0];
				}
				break;
			}
			i++;
		}
		return 	(T)list.get(i).get(index);
	}
	public boolean addAllList(List<T> c) {
		list.add(c);
		int[] scope=new int[2];
		scope[0]=size;
		scopes.add(scope);
		size+=c.size();
		scope[1]=size;
		return true;
    }
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return size;
	}

	public static void main(String args[]){
		MyList list=new MyList();
		list.addAllList(Arrays.asList("1","2","3"));
		list.addAllList(Arrays.asList("1","2","3"));

		HashSet set=new HashSet();
		set.addAll(list);
		System.out.println(set);
	}
	
}
