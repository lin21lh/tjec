package com.freework.queryData.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;

public class ResultSetMap extends AbstractMap<String,Object> {

	private JdbcForDTO fields[];
	private ResultSet rs;
	private EntrySet es=new EntrySet();

	public ResultSetMap(JdbcForDTO[] fields, ResultSet rs) {
		super();
		this.fields = fields;
		this.rs = rs;
	}
	@Override
	public Set<java.util.Map.Entry<String,Object>> entrySet() {
		return es;
	}
	public boolean isEmpty() {
		return fields.length==0;
	}
	
	 void  reset(){
		 es.reset();
     }
	
	class Entry implements java.util.Map.Entry<String, Object>{

		int index=0;
		public Entry(int index){
			this.index=index;
		}
		public String getKey() {
			return fields[index].getName();
		}

		public Object getValue() {
			try {
				return ((SQLType)fields[index].getType()).get(rs, index+1);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public Object setValue(Object value) {
			return null;
		}
		
		
	}
	 class SimpleMapIterator implements Iterator<java.util.Map.Entry> {
		 int i=0;
		 Entry e=new Entry(0);
		 SimpleMapIterator(){
		 
		 }
		public boolean hasNext() {
			return i<fields.length;
		}

		public java.util.Map.Entry next() {
			e.index=i;
			i++;
			return e;
		}

		public void remove() {
			
		}
	}
	
	private class EntrySet extends AbstractSet/*<Map.Entry<K,V>>*/ {
		SimpleMapIterator iterator=new SimpleMapIterator ();
        public Iterator<?> iterator() {
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
            return fields.length;
        }
       void  reset(){
    	   iterator.i=0;
        }
        public void clear() {
        }
    }
}
