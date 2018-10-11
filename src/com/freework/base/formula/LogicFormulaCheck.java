package com.freework.base.formula;

public class LogicFormulaCheck extends FormulaCheck {
	public void checkValid(){
	     if ((formula==null) || (formula.trim().length()<=0) ) {
	      throw new IllegalArgumentException("����������calRule!");
	     }
	      compareToLR();
	      checkFormula();
	   }
	
	 /*
	   /*��鹫ʽ���Ƿ���ڷǷ��ַ���(<>��<!)��
	   */
	protected void checkFormula(){
	      String[] bracket =new String[2];
	      String[] sign=new String[4];
	      bracket[0]="(";
	      bracket[1]=")";
	      sign[0]="<";
	      sign[1]=">";
	      sign[2]="|";
	      sign[3]="!";
	      String vstr="";
	      for(int i=0;i<bracket.length;i++){
	        for(int j=0;j<sign.length;j++){
	          if (i==0)
	            vstr=bracket[i]+sign[j];
	          else
	            vstr=sign[j]+bracket[i];
	          if (formula.indexOf(vstr)>0){
	        	  throw  new IllegalArgumentException("��ʽ�д��ڷǷ��ַ�"+vstr);

	          }
	        }
	      }
	      for(int i=0;i<sign.length;i++){
	        for(int j=0;j<sign.length;j++){
	          vstr=sign[i]+sign[j];
	          if (formula.indexOf(vstr)>0){
	        	  throw   new IllegalArgumentException("��ʽ�д��ڷǷ��ַ�"+vstr);

	          }
	        }
	      }
	      if (formula.indexOf("()")>0){
	    	  throw  new IllegalArgumentException("��ʽ�д��ڷǷ��ַ�()");

	      }
	      if (formula.indexOf(")(")>0){
	    	  throw  new IllegalArgumentException("��ʽ�д��ڷǷ��ַ�)(");

	      }
	    }
}
