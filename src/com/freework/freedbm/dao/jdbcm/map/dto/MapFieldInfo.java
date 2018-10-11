package com.freework.freedbm.dao.jdbcm.map.dto;

import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnum;
import com.freework.freedbm.cfg.fieldcfg.Like;
import com.freework.freedbm.cfg.fieldcfg.WhereOperators;
import com.freework.freedbm.cfg.fieldcfg.type.Type;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.base.util.unmodifiableMap.UnmodifiableKeyMap;

public class MapFieldInfo implements JdbcForDTO,FieldInfoEnum {

	private static final long serialVersionUID = -109829329369074814L;
	private int order=0;
	private String name;
	private String colName;
	@Override
	public Object clone() {
		return new MapFieldInfo(order, colName, colName, type, isDbCol, isDbCol, like, colName);
	}


	private String comments;
	private Type type;
	private boolean isDbCol;
	private boolean iskey;
	private Object defVal=null;
	private WhereOperators like;
	public MapFieldInfo(int order, String name, String colName,
			Type type, boolean isDbCol, boolean iskey,
			WhereOperators like,String comments) {
		super();

		this.order = order;
		this.name = name;
		this.colName = colName;
		this.comments = comments;
		this.type = type;
		this.isDbCol = isDbCol;
		this.iskey = iskey;
		this.like = like;
	}


	public WhereOperators getLike() {
		return like;
	}


	public void setLike(WhereOperators like) {
		this.like = like;
	}


	public boolean isKey() {
		return iskey;
	}


	public void setIskey(boolean iskey) {
		this.iskey = iskey;
	}



	

	public String getName() {
		return name;
	}


	public Type getType() {
		return type;
	}


	public String getColName() {
		return colName;
	}


	public void setColName(String colName) {
		this.colName = colName;
	}


	public String getComments() {
		return comments;
	}


	public void setComments(String comments) {
		this.comments = comments;
	}


	public boolean isDbCol() {
		return isDbCol;
	}


	public void setDbCol(boolean isDbCol) {
		this.isDbCol = isDbCol;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setType(Type type) {
		this.type = type;
	}


	public Object getValue(Object obj) {
		return ((UnmodifiableKeyMap)obj).getIndex(order);
		
	}


	public void setValue(Object obj, Object Value) {
		((UnmodifiableKeyMap)obj).putIndex(order, Value);
	}


	public int getOrder() {
		return order;
	}
	

	public void setOrder(int order) {
		this.order = order;
	}


	public JdbcForDTO getFieldInfo() {
		return this;
	}


	public String name() {
		return name;
	}


	public int ordinal() {
		return order;
	}


	public Object getDefVal() {
		return defVal;
	}


	public void setDefVal(Object defVal) {
		this.defVal = defVal;
	}





	

}
