package com.freework.queryData.util;

import java.beans.PropertyDescriptor;
import java.util.List;

import com.freework.base.util.FieldUtil;
import com.freework.freedbm.DTO;
import com.freework.freedbm.cfg.fieldcfg.FieldInfo;
import com.freework.freedbm.cfg.fieldcfg.type.SQLTypeMap;
import com.freework.freedbm.cfg.tablecfg.TableDataManager;
import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.freedbm.util.DTOByCfg;

public class QueryDTO <T extends Enum<T>> implements TableQuery<T>{
	private Class<? extends DTO> dtoClass=null;
	private String sql=null;
	private  JdbcForDTO fieldArray[] =null;
	private TableDataManager tdm;
	public void setSql(String sql) {
		this.sql = sql;
	}
	public QueryDTO(Class<? extends DTO> dtoClass,List<String> fields)throws RuntimeException{
		DTO dto=null;
		try {
			dto=dtoClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		this.dtoClass=dtoClass;
		tdm=DTOByCfg.getTableDataManager(dto);
		this.setFields(fields);
	}
	public void setFields(List<String> fields) {
		fieldArray=new JdbcForDTO[fields.size()];
		int i=0;
		for (String field : fields) {
			fieldArray[i]=tdm.getField(field);
			if(fieldArray[i]==null){
				fieldArray[i]=createField(field);
			}
			i++;
		}
	}
	private JdbcForDTO createField(String field){
		
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
		return f;
		
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
		return tdm.newInstance();
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
