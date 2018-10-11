package com.freework.freedbm.dao.jdbcm;

import com.freework.freedbm.Cfg;
import com.freework.freedbm.cfg.fieldcfg.Like;
import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.cfg.fieldcfg.type.SQLTypeMap;

public class Param{
	SQLType type=null;
	Object value=null;
	Like like=null;
	
	public static class p{
		public static Param param(SQLType type, Object value,Like like){
			return  new Param( type,  value,like);
		}
		public static Param param(SQLType type, Object value){
			return  new Param( type,  value);
		}
		public static Param[] params(Object... params){
			Param params2[]=new Param[params.length];
			for (int i = 0; i < params.length; i++) {
				params2[i]=param(params[i]);
			}
			return params2;
		}
		
		public static Param param( Object value){
			if(value==null)
				return new Param( Cfg.String,  value);
			return  new Param( SQLTypeMap.getSQLType(value.getClass()),  value);
		}
	}
	public Param(SQLType type, Object value,Like like) {
		super();
		this.type = type;
		this.value = value;
		this.like=like;
	}
	
	

	public Param(SQLType type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}
	public SQLType getType() {
		return type;
	}
	public void setType(SQLType type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Like getLike() {
		return like;
	}

	public void setLike(Like like) {
		this.like = like;
	}
	
}