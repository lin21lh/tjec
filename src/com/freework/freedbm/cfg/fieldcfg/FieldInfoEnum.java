package com.freework.freedbm.cfg.fieldcfg;

import com.freework.freedbm.dao.jdbcm.JdbcForDTO;

/**
 * @author �̷���
 * @category
 */
public interface FieldInfoEnum {
	public JdbcForDTO getFieldInfo();
	public String name();
	public int ordinal(); 
	
}
