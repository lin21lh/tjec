package com.freework.freedbm.bean;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.freework.freedbm.dao.jdbcm.map.dto.MapDTO;
import com.freework.freedbm.dao.jdbcm.map.dto.MapDTOCfg;

public interface BuildMapDTO {

	public 	MapDTO getMapDTO(String tableName,HttpServletRequest request );
	public MapDTOCfg getMapDTOCfg (String tableName);
	public MapDTO mapToDTO(String tableName,Map<String,?> map);
	public MapDTO mapToUpdate(String tableName,Map<String,?> map);

}
