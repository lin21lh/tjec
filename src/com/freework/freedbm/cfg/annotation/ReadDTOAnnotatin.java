package com.freework.freedbm.cfg.annotation;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.freework.freedbm.Cfg;
import com.freework.freedbm.cfg.fieldcfg.FieldInfo;
import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnumUtil;
import com.freework.freedbm.cfg.fieldcfg.type.SQLTypeMap;
import com.freework.freedbm.cfg.fieldcfg.type.Type;
import com.freework.freedbm.cfg.tablecfg.TableDataManager;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.freedbm.util.DTOByCfg;
import com.freework.base.util.FieldUtil;

public class ReadDTOAnnotatin {
	static TableCfgDump table=new TableCfgDump(); 
	
	public static  TableCfg getTableCfg(Class clazz) throws InstantiationException, IllegalAccessException {
		TableDataManager tableManager=DTOByCfg.get(clazz);
		if(tableManager instanceof TableCfg)
			return (TableCfg) tableManager;
		if(clazz.isAnnotationPresent(DTOClass.class)){
			DTOClass dto=(DTOClass) clazz.getAnnotation(DTOClass.class);
			TableCfg cfg=(TableCfg)(dto.defaultLoad()?new DefaultTableCfg(): table.findClassTableCfg(clazz.getName()).newInstance());

			cfg.tableName=dto.tablename();
	        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
	        List<FieldInfo> list=new ArrayList<FieldInfo>();
			for (java.lang.reflect.Field field : fields) {
	            if (field.isAnnotationPresent(Field.class)) { 
	            	Field fd=field.getAnnotation(Field.class);
	            	String[] linktype=fd.linkType();
	            	
	            	Type type=linktype.length!=0?CreateLinkType.createLinkType(linktype):SQLTypeMap.getSQLType(field.getType());
	            	FieldInfo inf=new FieldInfo(field.getName(),fd.colName(),type);
	            	inf.setComments(fd.comments());
	            	inf.setLike(fd.like().like);
	            	inf.setKey(fd.isKey());
	            	String defVal=fd.defVal();
	            	inf.setDefVal("".equals(defVal)?null:defVal);
	            	 try {
	            		 PropertyDescriptor p= FieldUtil.getPropertyDescriptor(clazz,field.getName());
	            		 inf.setReadMethod(p.getReadMethod());
	            		 inf.setWriteMethod(p.getWriteMethod());
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					list.add(inf);
	            	
	            }

			}
	        cfg.dtoClass=clazz;
	        FieldInfo [] fields2= list.toArray(new FieldInfo[list.size()]);
			cfg.pKey=FieldInfoEnumUtil.getPkey(fields2);
			setFields(cfg,fields2);
			DTOByCfg.put(clazz, cfg);
			return cfg;
		}else
			return null;
		


	}

	
	
	public static void setFields(TableCfg cfg,JdbcForDTO[] fields){
		cfg.setFields(fields);
		 Map<String,JdbcForDTO> map=new HashMap<String, JdbcForDTO>();
		 for (int i = 0; i < fields.length; i++) {
				map.put(fields[i].getName(), fields[i]);

		}
		
		cfg.map=map;
		builderSQL(cfg);
	}
	
	public static void builderSQL(TableCfg cfg){
		cfg.querysql=Cfg.DB_TYPE.getQuerySql(cfg.tableName,cfg.fields,null);
		cfg.insertSQLKey=Cfg.DB_TYPE.getInsertSqlKey(cfg.tableName, cfg.fields);
		cfg.insertsql=Cfg.DB_TYPE.getInsertSql(cfg.tableName, cfg.fields);
		cfg.updatesql=Cfg.DB_TYPE.getUpdateSql(cfg.tableName, cfg.fields);
	}
	
	
	
	
}
