package com.freework.freedbm.cfg.fieldcfg.type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.Map;

import com.freework.freedbm.Cfg;
import com.freework.freedbm.dao.jdbcm.Param;
import com.freework.freedbm.dao.jdbcm.Param.p;
public class SQLTypeMap {
 static Map<Class,SQLType> sqlTypeMap=new Hashtable<Class,SQLType>();
 static{
	 Field[] fields=Cfg.class.getDeclaredFields();
	 for (int i = 0; i < fields.length; i++) {
		 Field field=fields[i];
		 int modifier=field.getModifiers();

		   if(Modifier.isFinal(modifier)
			&&Modifier.isStatic(modifier)
			&&Modifier.isPublic(modifier)){
			try {

			Object obj=field.get(null);
				if(obj instanceof SQLType){
					SQLType sqlType=(SQLType) obj;
					 sqlTypeMap.put(sqlType.getReturnedClass(), sqlType);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}
	}

 }
 	public static Param getParam(Object obj){
 		return obj==null?p.param(getSQLType(NullType.class), obj):p.param(getSQLType(obj.getClass()), obj);
 	}
	public static SQLType getSQLType(Class type){
		SQLType SQLType= sqlTypeMap.get(type);
		if(SQLType==null)
			return Cfg.String;
		return SQLType;
	}
	public static SQLType getSQLType(Object type){
		if(type==null)
			return Cfg.String;
		return getSQLType(type.getClass());
	}
	
}
