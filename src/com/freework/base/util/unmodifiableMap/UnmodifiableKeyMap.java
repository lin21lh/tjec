package com.freework.base.util.unmodifiableMap;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class UnmodifiableKeyMap<V> extends AbstractMap<String, V> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5402773421600646120L;
	protected KeysIndex keysIndex;
	protected Object[] values=null;
	protected EntrySet entrys=null;
	protected int index=0;

	public UnmodifiableKeyMap(KeysIndex keysIndex){
		this.keysIndex=keysIndex;
		values=keysIndex.getObjectArray();
	}
	protected UnmodifiableKeyMap(){
		
	}
	public void clear() {
		values=keysIndex.getObjectArray();
	}
	public void putAll(Map<? extends String, ? extends V> t) {
		
	}
	public V remove(Object key) {
		Integer index=keysIndex.getIndex((String)key);
		if(index!=null){
			Object r=values[index];
			values[index]=null;
			return (V)r;
		}
		return null;
	}
	public boolean isEmpty() {
		return index==0;
	}
	public Set<java.util.Map.Entry<String, V>> entrySet() {
		if(entrys==null)
			entrys=new EntrySet();

		return entrys;
	
		//return list.;
	}
	public boolean containsKey(Object key) {
		return keysIndex.containsKey(key);
	}
	public boolean containsValue(Object value) {
		
		for (int i = 0; i < values.length; i++) {
			if((value==null&&values[i]==null)||(value!=null&&value.equals(values[i])))
				return true;
		}
		return false;
	}
	
	
	
	
	

	public V getIndex(int index) {
		return (V)values[index];
	}


	public Set<String> keySet() {
		return keysIndex.keySet();
	}
	
	public V put(String key, V value) {
		Integer index=	keysIndex.getIndex(key);
		if(index!=null){
			V oldobj=(V) values[index];
			values[index]=value;
			this.index++;
			return oldobj;
		}else{
			return null;
		}
	}

	public void putIndex(int index, V value) {
		if(values.length>index){
			values[index]=value;
		}
		//return value;
	}

	public int size() {
		return values.length;
	}
	

	public Collection<V> values() {
		return (Collection<V>) Arrays.asList(values);
	}
	public V get(Object key) {
		Integer index=	keysIndex.getIndex((String)key);
		return index!=null?(V)values[index.intValue()]:null;
		
	}
	public Integer getIndexByKey(String key){
		return keysIndex.getIndex(key);
	}
	private class EntrySet extends AbstractSet/*<Map.Entry<K,V>>*/ {
		SimpleMapIterator iterator=new SimpleMapIterator ();
        public Iterator iterator() {
        	iterator.i=0;
            return iterator;
        }
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry e = (Map.Entry) o;
            return get((String)e.getKey())!=null;
        }
        public boolean remove(Object o) {
        	return false;
        }
        public int size() {
            return values.length;
        }
        public void clear() {
        }
    }
	
	 class SimpleMapIterator implements Iterator<java.util.Map.Entry> {
		 int i=0;
		 Entry e=new Entry(0);
		 SimpleMapIterator(){
		 
		 }
		public boolean hasNext() {
			return i<values.length;
		}

		public java.util.Map.Entry next() {
			e.index=i;
			i++;
			return e;
		}

		public void remove() {
			
		}
	}
	 
	class Entry implements java.util.Map.Entry<String, V>{

		int index=0;
		public Entry(int index){
			this.index=index;
		}
		public String getKey() {
			return keysIndex.getName(index);
		}

		public V getValue() {
			return (V)values[index];
		}

		public V setValue(V value) {
			return (V) (values[index]=value);
		}
		
		
	}
	public String toString(){
		StringBuilder json=new StringBuilder(values.length*16);
		return toString(json);
	}
	
	public String toString(StringBuilder json){
		boolean pand=false;
		json.append("{");
		for (int i = 0; i < values.length; i++) {
		
			if(values[i]!=null){ 
				if(pand)
					json.append(",");
				else
					pand=true;
				
				json.append("\"").append(keysIndex.getName(i)).append("\":");
				if(values[i].getClass() == String.class)
				    	json.append("\"").append(values[i]).append("\"");
				    else
				    	json.append(values[i]);
				
			}
			 
		}
		json.append("}");
		return json.toString();
	}
	
}
