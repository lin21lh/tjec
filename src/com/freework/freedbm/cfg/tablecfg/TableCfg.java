package com.freework.freedbm.cfg.tablecfg;

import com.freework.freedbm.Cfg;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;


/**
 * @author ≥Ã∑Á¡Î
 * @category
 */


public interface TableCfg<T extends Enum<T>>  extends Cfg {
	public String getTableName();
	public  Class getDTOClass() ;
	public JdbcForDTO[] getPKey();
	public  JdbcForDTO[] getFields();
	public JdbcForDTO getField(String name);




}
