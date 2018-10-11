package com.freework.freedbm.util.listUtil.group;

import java.util.Map;

import com.freework.base.util.unmodifiableMap.UnmodifiableKeyMap;


public abstract class Value {
	Object name="";
	static int TYPE_MapValue=0;
	static int TYPE_SimpleMapValue=1;
	public Value(Object name){
		this.name=name;
		
		
	}
	static Value value=new Value(""){
		@Override
		public Object getValue(Object obj) {
			return obj;
		}};
	
	public static Value value(Object name,int type){
		
		
		if(TYPE_MapValue==type)
			return value.new MapValue((String)name);
		else if(TYPE_SimpleMapValue==type)
			return value.new SimpleMapValue((Integer)name);
		return value;
	}
	
	
	abstract public Object getValue(Object obj);
	class MapValue extends Value{

		public MapValue(String name) {
			super(name);
		}

		public Object getValue(Object obj) {
			return ((Map)obj).get(name);
		}
	}
	class SimpleMapValue extends Value{
		public SimpleMapValue(Integer index) {
			super(index);
		}
		public Object getValue(Object obj) {
			return ((UnmodifiableKeyMap)obj).getIndex((Integer)name);
		}
	}
	
}
