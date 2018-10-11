package com.freework.freedbm.util;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConvertUtils;

import com.freework.freedbm.BaseDTO;
import com.freework.freedbm.Cfg;
import com.freework.freedbm.DTO;
import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;

public class Tool {
	

	
	public static void requestToManagerDTO(DTO dto,HttpServletRequest request){
		TableQuery cfg=DTOByCfg.getTableDataManager(dto);
		JdbcForDTO[] f=cfg.getFields();
		for (int i = 0; i < f.length; i++) {
			if(f[i].isDbCol()){
				String value=request.getParameter(f[i].getName());
				if(value!=null){
					if(f[i].getType()==Cfg.String){
						f[i].setValue(dto, value);
					}else{
						f[i].setValue(dto, ConvertUtils.convert(value, f[i].getType().getReturnedClass()));
					}
				}
			}
			
		}
		
		
	}
	
	
	public static BaseDTO requestToManagerDTO(TableQuery cfg,HttpServletRequest request){
		BaseDTO dto=(BaseDTO) cfg.newInstance();
		JdbcForDTO[] f=cfg.getFields();
		for (int i = 0; i < f.length; i++) {
			String value=request.getParameter(f[i].getName());
			if(value!=null){
				Object obj=ConvertUtils.convert(value, f[i].getType().getReturnedClass());
				f[i].setValue(dto, obj);
			}
			
		}
		return dto;
		
		
	}
	
	
	
	public static Object[] convertArray(Class type,String arrayStr[]){
		Object[] objArray=new Object[arrayStr.length];
		for (int i = 0; i < arrayStr.length; i++) {
			objArray[i]=ConvertUtils.convert(arrayStr[i], type);
		}
		return objArray;
		
	}
	
}
