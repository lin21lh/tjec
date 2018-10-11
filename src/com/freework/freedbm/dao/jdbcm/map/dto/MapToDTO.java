package com.freework.freedbm.dao.jdbcm.map.dto;

import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.freework.freedbm.BaseDTO;
import com.freework.freedbm.cfg.tablecfg.TableDataManager;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;


class MapToDTO extends MapDTO implements BaseDTO{
	Map map=null;
	
	public  MapToDTO(MapDTOCfg cfg,Map map){
		super();
		this.keysIndex=cfg;
		this.map=map;
	}
	
	public  JdbcForDTO[] getUpdateField(){
		
		return getManagerCfg().getFields();

	}
	public TableDataManager getManagerCfg() {
		return ((MapDTOCfg)keysIndex);
	}
	public void putIndex(int index, Object value) {
		String name=((MapDTOCfg)keysIndex).getFields()[index].getName();
		Object oldValue=map.get(name);
		map.put(name, value);
	}
	public Object getIndex(int index) {
		JdbcForDTO fields=((MapDTOCfg)keysIndex).getFields()[index];
		Class<?> returnedClass=fields.getType().getReturnedClass();

		Object value=map.get(fields.getName());
		if(value!=null&&returnedClass!=value.getClass()){
			Converter converter=ConvertUtils.lookup(returnedClass);
			if(converter==null){
				throw new RuntimeException(value.getClass()+"不能转换为"+returnedClass);
			}
			value=converter.convert(returnedClass, value);
		}
		return value;

	}
}
