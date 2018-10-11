package com.freework.freedbm.dao.jdbcm.map.dto;

import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.freework.freedbm.BaseDTO;
import com.freework.freedbm.cfg.tablecfg.TableDataManager;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
class UpdateMapToDTO extends MapDTO implements BaseDTO{
	private JdbcForDTO []updateField=null;
	public  UpdateMapToDTO(MapDTOCfg cfg,Map map){
		super();
		this.keysIndex=cfg;
		int allSize=cfg.getFields().length;
		int size=map.size()<allSize?map.size():allSize;
		this.values=new Object[allSize];
		this.updateField=new JdbcForDTO[size];
		Set<Map.Entry<String,Object>> entrys=map.entrySet();
		short i=0;
		for (Map.Entry<String,Object> entry : entrys) {
			String key=entry.getKey();
			if(key!=null){
				MapFieldInfo jft=	(MapFieldInfo) cfg.getField(key);
				if(jft!=null){
					updateField[i]=jft;
				    values[jft.getOrder()]=convert(entry.getValue(),jft.getType().getReturnedClass());
					i++;
				}
			}
		}
		if(i==0){
			updateField=new JdbcForDTO[0];
		}else if(i<updateField.length){
			JdbcForDTO  tmp[]=new JdbcForDTO[i];
			System.arraycopy(updateField, 0, tmp, 0,i);
			updateField=tmp;
		}
	}
	
	public  JdbcForDTO[] updateField(){
			return updateField;

	}
	
	
	public Object convert(Object value,Class<?> returnedClass){
		
		if(value!=null&&returnedClass!=value.getClass()){
			Converter converter=ConvertUtils.lookup(returnedClass);
			if(converter==null){
				throw new RuntimeException(value.getClass()+"不能转换为"+returnedClass);
			}
			value=converter.convert(returnedClass,value);
		}
	
	return value;
	}
	public TableDataManager getManagerCfg() {
		return ((MapDTOCfg)keysIndex);
	}

	
}
