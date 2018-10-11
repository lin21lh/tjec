package com.freework.queryData.util;


public class ConditionValue {

	private Entry last = null;
	private Entry first = null;
	private int valueSize = 0;
	private final static Object noneValue= new Object();

	private int size;
	public int getSize() {
		return size;
	}

	
	
	public Result getResult() {
		if(size==0)
			return null;
		ConditionValue.Entry obj = first;
		StringBuilder sql = new StringBuilder();
		Object[] values = new Object[valueSize];
		int i = 0;
		while (obj != null) {
			sql.append(obj.sql);
			if(obj.value!=noneValue){
				values[i] = obj.value;
				i++;
			}
			obj = obj.next;
			
		}
		return new ConditionValue.Result(values, sql.toString());
	}

	public  void addNotEmpty(String sql,String value){
		if(value!=null&&!value.equals("")){
			this.add(sql, value);
		}
	}
	public void add(String sql ) {
		Entry newObj = new ConditionValue.Entry(noneValue, sql);
		if (size == 0) {
			first = newObj;
		} else {
			last.next = newObj;
		}
		last = newObj;
		size++;
	}
	public void add(String sql,Object value ) {
		Entry newObj = new ConditionValue.Entry(value, sql);
		if (size == 0) {
			first = newObj;
		} else {
			last.next = newObj;
		}
		last = newObj;
		size++;
		valueSize++;
	}

	public class Result {
		private Object[] values = null;
		private String sql = null;

		public Result(Object[] values, String sql) {
			this.values = values;
			this.sql = sql;

		}
		public Object[] getValues() {

			return values;
		}

		public String getSql() {
			return sql;
		}

	}


	 class Entry {

		public Entry(Object value, String sql) {
			this.value = value;
			this.sql = sql;

		}
		private Object value = null;
		private String sql = null;
		private Entry next = null;

	
	}

}
