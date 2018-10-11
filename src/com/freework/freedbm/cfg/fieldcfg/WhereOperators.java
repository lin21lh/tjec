package com.freework.freedbm.cfg.fieldcfg;
public interface WhereOperators {
	String getOperator();
	public final static WhereOperators LESS =  new SimpleOperators("<"); // <小于
	public final static WhereOperators LESS_EQUALS = new SimpleOperators("<="); // <=小于等于
	public final static WhereOperators GREATER_EQUALS =  new SimpleOperators(">="); // >=大于等于
	public final static WhereOperators GREATER =  new SimpleOperators(">"); // >大于
	public final static WhereOperators NOTEQUALS = new SimpleOperators("<>"); // !=不等于
	
}
