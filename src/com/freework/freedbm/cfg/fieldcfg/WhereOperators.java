package com.freework.freedbm.cfg.fieldcfg;
public interface WhereOperators {
	String getOperator();
	public final static WhereOperators LESS =  new SimpleOperators("<"); // <С��
	public final static WhereOperators LESS_EQUALS = new SimpleOperators("<="); // <=С�ڵ���
	public final static WhereOperators GREATER_EQUALS =  new SimpleOperators(">="); // >=���ڵ���
	public final static WhereOperators GREATER =  new SimpleOperators(">"); // >����
	public final static WhereOperators NOTEQUALS = new SimpleOperators("<>"); // !=������
	
}
