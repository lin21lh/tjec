package com.freework.freedbm.dao.jdbcm.map.dto;


import com.alibaba.fastjson.serializer.SerializeConfig;
import com.freework.freedbm.BaseDTO;
import com.freework.freedbm.cfg.tablecfg.TableDataManager;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.base.util.unmodifiableMap.UnmodifiableKeyMap;
public class MapDTO extends UnmodifiableKeyMap<Object> implements BaseDTO{

	
	protected MapDTO(){};
	private transient boolean[]  updateFieldIndexs;
	private transient int updateFieldIndexsSize=0;

	public  MapDTO(MapDTOCfg cfg){
		super(cfg);
		updateFieldIndexs=new boolean[cfg.fields.length];
	}
	public  MapDTO(MapQuery cfg){
		super(cfg);
		
	}
	public void putIndex(int index, Object value) {
		if(values.length>index){
			values[index]=value;
			if(updateFieldIndexs!=null&&!updateFieldIndexs[index]){
			    updateFieldIndexs[index]=true;
			    updateFieldIndexsSize++;
			}
		}
		//return value;
	}

	public  JdbcForDTO[] updateField(){
		JdbcForDTO fieldInfos[]=new JdbcForDTO[updateFieldIndexsSize];
		int i=0;
		 JdbcForDTO[] fields= managerCfg().getFields();
		for (int j=0;j< updateFieldIndexs.length;j++) {
			if(updateFieldIndexs[j]){
				fieldInfos[i]=fields[j];i++;
				if(i==updateFieldIndexsSize)break;
			}
		}
			return fieldInfos;
	}
	
	public TableDataManager managerCfg() {
		if(keysIndex instanceof TableDataManager){
			return ((TableDataManager)keysIndex);
		}
		return null;

	}

	
	
}
