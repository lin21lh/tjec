package com.freework.freedbm.util.listUtil;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;

public class ListSet<E> extends AbstractSet<E> {
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
