package com.freework.freedbm.cfg.fieldcfg.type;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public interface FormatType<T> {

	public T stringToObject(String str);
	public String toString(T value);
	
}
