package com.freework.freedbm.cfg.tablecfg;


import java.lang.reflect.Method;

import com.freework.freedbm.cfg.fieldcfg.FieldInfo;
import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnum;
import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnumUtil;
import com.freework.freedbm.cfg.id.IdentifierGenerator;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.base.util.FieldUtil;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public abstract class AbstractTableCfg<T extends Enum<T>> implements TableDataManager,java.io.Serializable{
	private  JdbcForDTO pKey[] = FieldInfoEnumUtil.getPkey(getEnumClass());
	private  JdbcForDTO fields[] = FieldInfoEnumUtil.getFieldInfos(getEnumClass());
	private   String insertSQL=DB_TYPE.getInsertSql(getTableName(), fields);
	private   String insertSQLKey=DB_TYPE.getInsertSqlKey(getTableName(), fields);
	private   String updateSQL=DB_TYPE.getUpdateSql(getTableName(),fields);
	private   String querySQL=DB_TYPE.getQuerySql(getTableName(),fields,null);
	private   Class dtoClass=null;
	public abstract Class<T> getEnumClass();
	public IdentifierGenerator getIdentifierGenerator(){
		return null;
	}
	public JdbcForDTO getField(String name){
		try{
			FieldInfoEnum fieldInfoEnum= ((FieldInfoEnum) Enum.valueOf(getEnumClass(), name));
			return fieldInfoEnum==null?null:fieldInfoEnum.getFieldInfo();
		}catch( java.lang.IllegalArgumentException e){
			return null;
		}
	} 
	
	public Class getDTOClass() {
		return dtoClass;

	}

	public AbstractTableCfg(Class dtoClass){
		this.dtoClass=dtoClass;
		for (int i = 0; i < fields.length; i++) {
			FieldInfo f=(FieldInfo)fields[i];
			Method ms[]=FieldUtil.getPropertyMethods(dtoClass,f.getName());
			
			f.setWriteMethod(ms[0]);
			f.setReadMethod(ms[1]);
		}

	}
	public Object newInstance(){
		try {
		return	dtoClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public JdbcForDTO[]getPKey(){
		return pKey;
	}
	
	

	public  String getInsertsql() {
		return insertSQL;
	}
	public  String getInsertSQLKey() {
		return insertSQLKey;
	}

	public  String getQuerysql() {
		return querySQL;
	}

	public  String getUpdatesql() {
		return updateSQL;
	}

	public  JdbcForDTO[] getFields() {
		return fields;
	}
}
