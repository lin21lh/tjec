package com.freework.freedbm.cfg.tablecfg;


import java.lang.reflect.Method;

import com.freework.freedbm.cfg.fieldcfg.FieldInfo;
import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnum;
import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnumUtil;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.base.util.FieldUtil;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public abstract class AbstractQueryTableCfg<T extends Enum<T>> implements TableQuery<T>{
	private  JdbcForDTO pKey[] = FieldInfoEnumUtil.getPkey(getEnumClass());
	private  JdbcForDTO fields[] = FieldInfoEnumUtil.getFieldInfos(getEnumClass());
	protected  String querySQL=DB_TYPE.getQuerySql(getTableName(),fields,null);
	private   Class dtoClass=null;
	public abstract Class<T> getEnumClass();

	public Class getDTOClass() {
		return dtoClass;

	}
	public JdbcForDTO getField(String name){
		return ((FieldInfoEnum) Enum.valueOf(getEnumClass(), name)).getFieldInfo();
	} 
	
	
	public AbstractQueryTableCfg(Class dtoClass){
		this(dtoClass,null);
	}
	public AbstractQueryTableCfg(Class dtoClass,String querySQL){
		if(querySQL==null)
			 this.querySQL=DB_TYPE.getQuerySql(getTableName(),fields,null);
		else
			 this.querySQL=querySQL;

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
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
	public JdbcForDTO[]getPKey(){
		return pKey;
	}

	public  String getQuerysql() {
		return querySQL;
	}


	public  JdbcForDTO[] getFields() {
		return fields;
	}
}
