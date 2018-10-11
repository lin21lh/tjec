package com.freework.base.util.unmodifiableMap;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;





public class KeysIndex  implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 757397453580535021L;
	private Map<String,Integer> map=new HashMap<String,Integer>();
	private List<String> names=null;
	int size=0;
	public KeysIndex(){this.names=new ArrayList<String>();}
	public KeysIndex(String... names){
		int length=names.length;
		this.names=new ArrayList<String>(length);
		this.setNames(names);
		}

	
	public void setName(String name){
		map.put(name, size);
		names.add(name);
		size++;
	}
	public void setNames(String... names){
		for (String name : names) {
			map.put(name, size);
			this.names.add(name);
			size++;
		}
	}
	 String	getName(int index){
		 return names.get(index);
	 }
	public Object[] getObjectArray(){
		return new Object[size];
	}

	public Set<String> keySet() {
		return new ListSet<String>(names);
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}
	
	public Integer getIndex(String name){
		return map.get(name);
	}

	
	 class ListSet<E> extends AbstractSet<E> {
			List<E> list=null; 
			public ListSet(List<E> list){
				this.list=list;
			}
			@Override
			public Iterator<E> iterator() {
				return list.iterator();
			}

			@Override
			public int size() {
				return list.size();
			}
	 }
}
