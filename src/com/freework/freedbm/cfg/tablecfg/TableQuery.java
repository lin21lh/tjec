package com.freework.freedbm.cfg.tablecfg;

public interface TableQuery<T extends Enum<T>>  extends CreateObject,TableCfg<T> {
	public  String getQuerysql();
	public Object newInstance();


}
