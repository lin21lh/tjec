package com.freework.freedbm.cfg.annotation;

import com.freework.freedbm.cfg.id.IdentifierGenerator;

public class DefaultTableCfg extends TableCfg {
	public IdentifierGenerator getIdentifierGenerator(){
		return null;
	}
	@Override
	public Object newInstance() {
		try {
			return this.dtoClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

}
