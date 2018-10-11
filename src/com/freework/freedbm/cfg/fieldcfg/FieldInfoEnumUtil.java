package com.freework.freedbm.cfg.fieldcfg;

import java.util.ArrayList;
import java.util.List;

import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public class FieldInfoEnumUtil {
	public static JdbcForDTO[] getFieldInfos(Class Enumclass){
		FieldInfoEnum[] values=(FieldInfoEnum[]) Enumclass.getEnumConstants();
		return getFieldInfos(values);
		
	}
	
	public static JdbcForDTO[] getFieldInfos(FieldInfoEnum[] values){
		JdbcForDTO[] fields=new JdbcForDTO[values.length];
		for (int i = 0; i < fields.length; i++) {
			fields[i]=values[i].getFieldInfo();
		}
		return fields;
	}
	
	public static JdbcForDTO[] getPkey(Class Enumclass){
		FieldInfoEnum[] values=(FieldInfoEnum[]) Enumclass.getEnumConstants();
		List<JdbcForDTO> list=new ArrayList<JdbcForDTO>(2);
		for (int i = 0; i < values.length; i++) {
			JdbcForDTO field=values[i].getFieldInfo();
			if(field.isKey())
				list.add(field);
		}
		return list.toArray(new JdbcForDTO[list.size()]);
		
	}
	
	public static JdbcForDTO[] getPkey(JdbcForDTO[] fs){
		List<JdbcForDTO> list=new ArrayList<JdbcForDTO>(2);
		for (int i = 0; i < fs.length; i++) {
			JdbcForDTO field=fs[i];
			if(field.isKey())
				list.add(field);
		}
		return list.toArray(new JdbcForDTO[list.size()]);
		
	}
}
