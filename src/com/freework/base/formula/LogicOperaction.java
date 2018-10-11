package com.freework.base.formula;



public class LogicOperaction {

	final static LogicOperaction[] logicOperactions={
		new LogicOperaction("<=",LogicFormula.LESS_EQUALS,2),
		new LogicOperaction(">=",LogicFormula.GREATER_EQUALS,2),
		new LogicOperaction("!=",LogicFormula.NOTEQUALS,2),
		new LogicOperaction("<",LogicFormula.LESS,1),
		new LogicOperaction("=",LogicFormula.EQUALS,1),
		new LogicOperaction(">",LogicFormula.GREATER,1)
		//new LogicOperaction("|",LogicFormula.OR,1),
		//new LogicOperaction("&",LogicFormula.AND,1)
		};
	public String  operactionStr="";
	public int length=0;
	public int operaction=0;
	public int index=0;
	public LogicOperaction clone(){
		
		return new LogicOperaction(operactionStr,operaction,length);
	}
	
	public LogicOperaction(String operactionStr, int operaction, int length) {
		super();
		this.operactionStr = operactionStr;
		this.length = length;
		this.operaction = operaction;
	}
	

	
	
	
	
	
	
	
	
	
	
	
}
