package com.freework.queryData.util;

import java.beans.PropertyDescriptor;
import java.util.List;

import com.freework.base.util.FieldUtil;
import com.freework.freedbm.cfg.fieldcfg.FieldInfo;
import com.freework.freedbm.cfg.fieldcfg.type.SQLTypeMap;
import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;

public class Query<T extends Enum<T>> implements TableQuery<T> {

	private Class dtoClass=null;
	private String sql=null;
	private List<String> fields=null;
	private  JdbcForDTO fieldArray[] =null;
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public Query(){}
	
	public Query(Class dtoClass,String ... fields){
		this.dtoClass=dtoClass;
		this.setFields(fields);
	}

	public Query(String sql,Class dtoClass,String ... fields){
		this.sql=sql;
		this.dtoClass=dtoClass;
		this.setFields(fields);
	}
	public Query(Class dtoClass,List<String> fields){
		this.dtoClass=dtoClass;
		this.setFields(fields);
	}
	public void setDtoClass(Class dtoClass) {
		this.dtoClass = dtoClass;
	}
	public void setFields(List<String> fields) {
		fieldArray=new JdbcForDTO[fields.size()];
		int i=0;
		for (String field : fields) {
			
			PropertyDescriptor p=FieldUtil.getPropertyDescriptor(dtoClass,field);
			FieldInfo f=null;
			if(p==null){
				f=new FieldInfo(field,field, SQLTypeMap.getSQLType(String.class)){
					private static final long serialVersionUID = 1L;
					public void setValue(Object obj, Object Value) {
					}
				};
			}else{
				 f=new FieldInfo(field,field, SQLTypeMap.getSQLType(p.getPropertyType()));
				f.setWriteMethod(p.getWriteMethod());
				f.setReadMethod(p.getReadMethod());
			}
			fieldArray[i]=f;
			i++;
		}
		
	}
	public void setFields(String ... fields) {
		this.setFields(java.util.Arrays.asList(fields));
	}
	public void setFields(String fieldsStr) {
		String[] fields=fieldsStr.split(",");
		this.setFields(java.util.Arrays.asList(fields));
	}


	@Override
	public Class getDTOClass() {
		return dtoClass;
	}

	

	@Override
	public JdbcForDTO[] getFields() {
		
		if(fieldArray==null){
			PropertyDescriptor ps[]=FieldUtil.getPropertyDescriptors(dtoClass);
			int i=0;
			fieldArray=new JdbcForDTO[ps.length];
			for (PropertyDescriptor p : ps) {
				FieldInfo f=new FieldInfo(p.getName(),p.getName(), SQLTypeMap.getSQLType(p.getPropertyType()));
				f.setWriteMethod(p.getWriteMethod());
				f.setReadMethod(p.getReadMethod());
				fieldArray[i]=f;
				i++;
			}
			
		}
		
		return fieldArray;
	}

	@Override
	public JdbcForDTO getField(java.lang.String name) {
		for (int i = 0; i < fieldArray.length; i++) {
			if(fieldArray[i].getName().equals(name)){
				return fieldArray[i];
			}
		} 
		return null;
	}

	@Override
	public java.lang.String getQuerysql() {
		return sql;
	}

	@Override
	public Object newInstance() {
		try {
			return dtoClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public java.lang.String getTableName() {
		return null;
	}
	@Override
	public JdbcForDTO[] getPKey() {
		return null;
	}
}
