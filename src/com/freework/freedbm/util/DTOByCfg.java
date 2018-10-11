package com.freework.freedbm.util;

import java.util.HashMap;
import java.util.Map;

import com.freework.freedbm.BaseDTO;
import com.freework.freedbm.DTO;
import com.freework.freedbm.cfg.tablecfg.TableDataManager;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;

public class DTOByCfg {

	
	
	static private Map<Class,TableDataManager> map=new HashMap<Class,TableDataManager>();
//	static TableServiceI tableService=(TableServiceI) SpringBeanUtil.bean().getBean("dic.table.service.TableService");
//	
//	public static TableDataManager getPredefineTableDataManager(DTO dto){
//		if(dto!=null){
//			try {
//				TableCfg cfg=ReadDTOAnnotatin.getTableCfg(dto.getClass());
//				if(cfg==null)
//					new InstantiationException(dto.getClass()+"AnnotatinÎ´¼Ó×¢");
//				SysDictableVO table=tableService.findTableByName(cfg.getTableName());
//				if(table!=null){
//					TableDataManager dicManager=table.dicManager();
//					if(dicManager!=null)
//						return dicManager;
//					else
//						return new PredefineTableCfg(cfg,cfg.getFields().length);
//				}else{
//					return new PredefineTableCfg(cfg,cfg.getFields().length);
//				}
//			} catch (InstantiationException e) {
//				e.printStackTrace();
//				return null;
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//				return null;
//
//			}
//		}else
//			return null;
//			
//	}
	
	
	public static void put(Class clazz,TableDataManager m){
		map.put(clazz, m);
	}
	public static TableDataManager get(Class clazz){
		return map.get(clazz);
	}
	
	public static TableDataManager getTableDataManager(DTO dto){
		if(dto instanceof BaseDTO)
			return ((BaseDTO) dto).managerCfg();
		else if(dto==null)
			return null;
		else
			return map.get(dto.getClass());
		
	}
	
	
	
	private static JdbcForDTO[] getUpdateField(DTO dto){
		TableDataManager cfg=getTableDataManager(dto);
		JdbcForDTO[] fields=cfg.getFields();
		JdbcForDTO[] rfields=new JdbcForDTO[fields.length];
		int i=0;
		for (JdbcForDTO jdbcForDTO : fields) {
			Object value=jdbcForDTO.getValue(dto);
			if (value != null && !"".equals(value)){
				rfields[i]=jdbcForDTO;
				i++;
			}
		}
		JdbcForDTO[] tmp=new JdbcForDTO[i];
		System.arraycopy(rfields, 0, tmp, 0, i);
		return tmp;
	}
	
	public static JdbcForDTO[] getUpdateField(DTO dto,boolean isQuery){
		if(dto instanceof BaseDTO)
			return ((BaseDTO) dto).updateField();
		else if(dto==null)
			return null;
		else
			return isQuery?getUpdateField(dto):getTableDataManager(dto).getFields();
	}
	
	
}
