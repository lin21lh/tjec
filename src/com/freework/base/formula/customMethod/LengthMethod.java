package com.freework.base.formula.customMethod;

public class LengthMethod extends MethodAbstract {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public CustomMethod newCustomMethod(String[] args) {
		LengthMethod method=new LengthMethod();
		Object[] objs= getValValue(args[0]);
		method.lvalType=(Integer) objs[0];
		method.lval =method.lvalType==CONVENTION ?objs[1].toString() :objs[1] ;
		return method;
	}

	@Override
	public String getMethodName() {
		return "length";
	}

	@Override
	public Object getValue(Object obj) {
		Object value=this.getLvalValue(obj);
		if(value==null)
			return 0;
		return value.toString().length();
	}



}
