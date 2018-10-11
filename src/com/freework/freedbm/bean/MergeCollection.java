package com.freework.freedbm.bean;

import java.util.AbstractCollection;
import java.util.Iterator;

public class MergeCollection<E> extends AbstractCollection<E> {

	private Iterator<? extends E> iterator1;
	private Iterator<? extends E> iterator2;
	private int size;
	public MergeCollection(Iterator<? extends E> iterator1,Iterator<? extends E> iterator2,int size){
		this.iterator1=iterator1;
		this.iterator2=iterator2;
		this.size=size;
		
	}
	@Override
	public Iterator<E> iterator() {
		return new MergeIterator();
	}

	@Override
	public int size() {
		return size;
	}
	
	class MergeIterator implements Iterator<E>{

		@Override
		public boolean hasNext() {
			return iterator1.hasNext()||iterator2.hasNext();
		}

		@Override
		public E next() {
			return (E)(iterator1.hasNext()?iterator1.next():iterator2.next());
		}

		@Override
		public void remove() {
			
		}
		
		
		
	}

}
