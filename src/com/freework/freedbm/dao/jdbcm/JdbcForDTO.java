package com.freework.freedbm.dao.jdbcm;
import java.beans.PropertyDescriptor;

import com.freework.freedbm.cfg.fieldcfg.Like;
import com.freework.freedbm.cfg.fieldcfg.WhereOperators;
import com.freework.freedbm.cfg.fieldcfg.type.Type;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public interface JdbcForDTO {

	public String getColName();
	public String getName();

	public Type getType();
	public Object getDefVal();
	public Object getValue(Object obj);
	public void setValue(Object obj,Object Value);
	public WhereOperators getLike();
	public boolean isDbCol();
	public boolean isKey();
}
