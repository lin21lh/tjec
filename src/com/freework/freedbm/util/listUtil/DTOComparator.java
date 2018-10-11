package com.freework.freedbm.util.listUtil;


import com.freework.freedbm.DTO;
import com.freework.freedbm.cfg.tablecfg.TableDataManager;
import com.freework.freedbm.util.DTOByCfg;


public  class DTOComparator extends MyComparator<DTO> {

	

	public DTOComparator(boolean isDESC, String... fieldNames) {
		super(isDESC, fieldNames);
	}

	public DTOComparator(String... fieldNames) {
		super(fieldNames);
	}

	public Object getValue(String fieldName,DTO dto){
		TableDataManager  jft=DTOByCfg.getTableDataManager(dto);
		return jft.getField(fieldName).getValue(dto);
	}
	
}