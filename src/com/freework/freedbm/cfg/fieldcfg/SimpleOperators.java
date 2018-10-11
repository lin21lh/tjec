package com.freework.freedbm.cfg.fieldcfg;

public class SimpleOperators  implements WhereOperators{

	private String operator;
	public SimpleOperators(String operator){
		this.operator=operator;
		
	}
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
}
