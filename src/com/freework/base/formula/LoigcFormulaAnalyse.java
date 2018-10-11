package com.freework.base.formula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.freework.base.formula.customMethod.CustomMethod;
import com.freework.base.formula.customMethod.CustomMethods;
import com.freework.base.util.NumberUtil;

public class LoigcFormulaAnalyse {

	private String formulaStr;
	private String oldFormulaStr;

	private int leftBracket;
	private int rightBracket;
	private List<String> variableList = new LinkedList<String>();
	private LogicFormula formula = null;

	public List<String> getVariableList() {
		return variableList;
	}

	public LogicFormula getFormula() {
		return formula;
	}

	public String getFormulaStr() {
		return oldFormulaStr;
	}

	public LoigcFormulaAnalyse() {
	}

	public LoigcFormulaAnalyse(String formula) {
		init(formula);
	}

	public void init(String formula) {
		this.formulaStr = "(" + formula + ")";
		List<CustomMethod> customMethods=new ArrayList<CustomMethod>();

		this.formulaStr =CustomMethods.analyseCustomMethod(this.formulaStr, customMethods);
		LogicFormulaCheck formulaCheck = new LogicFormulaCheck();
		formulaCheck.setFormula(this.formulaStr);
		formulaCheck.checkValid();
		leftBracket = formulaCheck.getLeftBracket();
		rightBracket = formulaCheck.getRightBracket();
		this.formula = analyse(customMethods);
	}
	private LogicFormula analyse(List<CustomMethod> customMethods) {
		List<LogicFormula> analyseList = new ArrayList<LogicFormula>();
		String formulaStr = "", calRule = "";
		calRule = this.formulaStr;
		for (int i = 0; i < leftBracket; i++) {
			int iStart = calRule.lastIndexOf("(") + 1;
			formulaStr = calRule.substring(iStart,
					iStart + calRule.substring(iStart).indexOf(")")).trim();
			
			List<String> formulalist = splitAndOr(formulaStr);
			
			analyseList.add(getLogicFormula(formulalist, 0, analyseList,customMethods));
			
			iStart = iStart - 1;
			int iEnd = calRule.substring(iStart).indexOf(")") + 1;
			calRule = calRule.substring(0, iStart).trim() + "@#analyseList_"
					+ i
					+ calRule.substring(iStart + iEnd, calRule.length()).trim();
		}
		return analyseList.get(analyseList.size() - 1);
	}
	public static LogicOperaction symbolIndexOf(String str) {

		for (int i = 0; i < LogicOperaction.logicOperactions.length; i++) {
			int index = str.indexOf(LogicOperaction.logicOperactions[i].operactionStr);
			if (index != -1) {
				LogicOperaction logicOperaction = LogicOperaction.logicOperactions[i].clone();
				logicOperaction.index = index;
				return logicOperaction;
			}

		}
		return null;

	}

	/**
	 * 根据 &或|字符分割字符
	 * 
	 * @param str
	 * @return
	 */
	public List<String> splitAndOr(String str) {
		int beginIndex = -1;

		List<String> list = new ArrayList<String>();
		for (int endIndex = 1; endIndex < str.length(); endIndex++) {
			char c = str.charAt(endIndex);
			if (c == '|' || c == '&') {
				list.add(str.substring(beginIndex + 1, endIndex));
				list.add(String.valueOf(c));
				beginIndex = endIndex;
			}
		}
		if (beginIndex != str.length())
			list.add(str.substring(beginIndex + 1));
		return list;
	}

	/**
	 * 根据字符列表生成公式
	 * 
	 * @param list
	 *            &或|字符分割字符列表
	 * @param index
	 * @param analyseList
	 * @return
	 */
	public LogicFormula getLogicFormula(List<String> list, int index,
			List<LogicFormula> analyseList,List<CustomMethod> customMethods) {
		LogicFormula lf = null;

		if (list.size() > index + 1) {
			String lval = list.get(index);
			String operactionStr = list.get(index + 1);
			int operaction = operactionStr.charAt(0) == '&' ? LogicFormula.AND: LogicFormula.OR;
			lf = new LogicFormula(
					getLogicFormula(lval, analyseList,customMethods),FormulaAbstract.FORMULA,
					operaction,
					getLogicFormula(list, index + 2, analyseList,customMethods),FormulaAbstract.FORMULA
					);
		} else {

			lf = getLogicFormula(list.get(index), analyseList,customMethods);
		}
		return lf;
	}

	void addVariableList(String variable){
	if(!variableList.contains(variable))
		variableList.add(variable);
	}
	
	/**
	 * 
	 * @param string
	 * @param analyseList
	 * @return
	 */
	LogicFormula getLogicFormula(String string, List<LogicFormula> analyseList,List<CustomMethod> customMethods) {
		if (string.indexOf("@#analyseList_") != -1) {

			return analyseList.get(Integer.parseInt(string.substring(14)));
		}

		LogicOperaction operaction = symbolIndexOf(string);
		if (operaction == null)
			throw new IllegalArgumentException("公式无法解析:" + string);
		
		String lval = string.substring(0, operaction.index);
		String rval = string.substring(operaction.index + operaction.length);
		
			if (symbolIndexOf(lval) != null)
				throw new IllegalArgumentException("公式无法解析:" + lval);
			if (symbolIndexOf(rval) != null)
				throw new IllegalArgumentException("公式无法解析:" + rval);
			
			Object[] objs= getValValue(lval ,customMethods);
			int lvalType=(Integer) objs[0];
			Object lvalobj=objs[1];
			objs=null;
			objs= getValValue(rval,customMethods);
			int rvalType=(Integer) objs[0];
			Object rvalobj=objs[1];
			LogicFormula formula=new LogicFormula(lvalobj,lvalType, operaction.operaction, rvalobj, rvalType);
			
		return formula;

	}
	public static final  String RESERVED_WORDS[]={"null","true","false"};
	public static final  Object RESERVED_WORDS_VALUE[]={null,true,false};

	
	public static int reservedWordsIndex(String word){
		if(word==null)
			return -1;
		for (int i = 0; i < RESERVED_WORDS.length; i++) {
			if(word.equals(RESERVED_WORDS[i])){
				return i;
			}
		}
		return -1;
	}
	
	private  Object[]  getValValue(Object val,List<CustomMethod> customMethods){
		Object[] objs=new Object[2];
		if(val==null){
			objs[0]=FormulaAbstract.CONVENTION;
			objs[1]=null;
		}else if (val instanceof FormulaAbstract){
			objs[0]=FormulaAbstract.FORMULA;
			objs[1]=val;
			
		}else {
			String sval=(String)val;
			
			if(sval.charAt(0)=='\''&&sval.charAt(sval.length()-1)=='\''){
				objs[0]=FormulaAbstract.CONVENTION;
				objs[1]=sval.substring(1, sval.length()-1);
			}else  if(NumberUtil.isNumber(sval)){
				
				objs[0]=FormulaAbstract.CONVENTION;
				objs[1]=NumberUtil.bigDecimal(sval);
			}else{
				 if(FormulaAnalyse.isFormula(sval)){
			    		FormulaAnalyse f=new FormulaAnalyse(sval,customMethods);
			    		objs[0]=FormulaAbstract.FORMULA;
			    		objs[1]=f.getFormula();
			    		List<String> list=f.getVariableList();
			    		for (String string : list) {
							this.addVariableList(string);
						}
			    	}else
				 if(customMethods!=null&&sval.indexOf("@#customMethod_")!=-1){
	    			objs[0]=FormulaAbstract.CUSTOM_METHOD;
		    		objs[1]=customMethods.get(Integer.parseInt(sval.substring(15)));
		    	}else {
		    		int index=reservedWordsIndex(sval);
		    		
		    		if(index==-1){
				    	objs[0]=FormulaAbstract.VARIABLE;
				    	objs[1]=sval;
				    	addVariableList(sval);
		    		}else{
		    			objs[0]=FormulaAbstract.CONVENTION;
		    			objs[1]=RESERVED_WORDS_VALUE[index];
		    			
		    			
		    		}
		    	}
		    	
		}
			
			
		} 
		return objs;
	} 
	
	public static void main(String a[]){
		LoigcFormulaAnalyse lfa=new LoigcFormulaAnalyse("1.1>a&a>10");
		Map map=new HashMap();
		map.put("a", 1);
		System.out.println(lfa.getFormula().compareTo(map));
	}
	
}
