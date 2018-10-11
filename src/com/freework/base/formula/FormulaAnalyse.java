package com.freework.base.formula;

import java.util.ArrayList;
import java.util.List;

import com.freework.base.formula.customMethod.CustomMethod;
import com.freework.base.formula.customMethod.CustomMethods;
import com.freework.base.formula.customMethod.MethodAbstract;



public class FormulaAnalyse implements java.io.Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -993341078419935731L;


	
	
	private String formulaStr;
	private String oldFormulaStr;

	private int leftBracket;
	private int rightBracket;

	private List<String> variableList=new ArrayList<String>();
	private Formula formula = null;

	
	public List<String> getVariableList() {
		return variableList;
	}


	
	public Formula getFormula() {
		return formula;
	}

	public String getFormulaStr() {
		return oldFormulaStr;
	}
	public FormulaAnalyse(String formula) {
		List<CustomMethod> customMethods=new ArrayList<CustomMethod>();
		init(formula,customMethods);
	}
	 FormulaAnalyse(String formula,List<CustomMethod> customMethods) {
		init(formula,customMethods);
	}
	public void init(String formula,List<CustomMethod> customMethods){
		this.oldFormulaStr=formula;
		this.formulaStr = "(" + replaceSubtration(formula) + ")";
		this.formulaStr =CustomMethods.analyseCustomMethod(this.formulaStr, customMethods);
		FormulaCheck formulaCheck = new FormulaCheck();
		formulaCheck.setFormula(this.formulaStr);
		
		formulaCheck.checkValid();
		leftBracket = formulaCheck.getLeftBracket();
		rightBracket = formulaCheck.getRightBracket();
		
		List<List<String>> analyseList = analyse();
		this.formula = getFormula(analyseList,customMethods.size()==0?null:customMethods);
	}
	
	private List<List<String>> analyse() {
		List<List<String>> analyseList = new ArrayList<List<String>>();
		String formulaStr = "", calRule = "";
		calRule = this.formulaStr;
		for (int i = 0; i < leftBracket; i++) {
			int iStart = calRule.lastIndexOf("(") + 1;
			formulaStr = calRule.substring(iStart,iStart + calRule.substring(iStart).indexOf(")")).trim();
			analyseList.add(symbolParse(formulaStr));
			iStart = iStart - 1;
			int iEnd = calRule.substring(iStart).indexOf(")") + 1;
			calRule = calRule.substring(0, iStart).trim() + "@#analyseList_" + i
					+ calRule.substring(iStart + iEnd, calRule.length()).trim();
		}
		return analyseList;
	}

	public static int symbolIndexOf(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if(c=='+'||c=='`'||c=='*'||c=='/')
				return i;

		}
		return -1;
	}

	List<String> symbolParse(String str) {
		List<String> list = new ArrayList<String>();
		int sym = symbolIndexOf(str);
		while (sym != -1) {
			String insStr = str.substring(0, sym).trim();
			list.add(insStr);
			insStr = str.substring(sym, sym + 1).trim();
			list.add(insStr);
			str = str.substring(sym + 1, str.length()).trim();
			sym = symbolIndexOf(str);
		}
		if (sym == -1) {
			list.add(str);
		}
		return list;
	}

	/**
	 * 替换Formula公式对象
	 * 
	 * @param list
	 *            公式
	 * @param operaction1
	 *            运算符1
	 * @param operaction2
	 *            运算符2
	 */
	public void setFormula(List list, char operaction1, char operaction2,
			List<Formula> Formulas,List<CustomMethod> customMethods) {
		String strOperaction1 = String.valueOf(operaction1);
		String strOperaction2 = String.valueOf(operaction2);
		for (int i = 1; i < list.size(); i++) {
			Object value = list.get(i);
			if (strOperaction1.equals(value)) {
				Formula formula = new Formula(list.get(i - 1), operaction1, list.get(i + 1), Formulas,customMethods);
				addVariableList(formula);
				list.remove(i - 1);
				list.remove(i - 1);
				list.set(i - 1, formula);
				i--;
			} else if (strOperaction2.equals(value)) {
				Formula formula = new Formula(list.get(i - 1), operaction2, list.get(i + 1), Formulas,customMethods);
				addVariableList(formula);

				list.remove(i - 1);
				list.remove(i - 1);
				list.set(i - 1, formula);
				i--;

			}

		}
	}
	/**
	 * 增加变量
	 * @param formula 公式
	 */
	private void addVariableList(Formula formula){
		if(formula.getLvalType()==Formula.VARIABLE){
			String val=(String) formula.getLval();
			if(!variableList.contains(val))
				variableList.add(val);
		}
		if(formula.getRvalType()==Formula.VARIABLE){
			String val=(String) formula.getRval();
			if(!variableList.contains(val))
				variableList.add(val);
		}
		if(formula.getLvalType()==Formula.CUSTOM_METHOD){
			Object val=(Object) formula.getLval();
	
			if(val instanceof MethodAbstract){
			//	System.out.println(((MethodAbstract) val).getVariableList());
    			variableList.addAll(((MethodAbstract) val).getVariableList());
    		}
    		
		}
	
		if(formula.getRvalType()==Formula.CUSTOM_METHOD){
			Object val=(Object) formula.getRval();
			if(val instanceof MethodAbstract){			
    			variableList.addAll(((MethodAbstract) val).getVariableList());
    		}
    		
		}
		
	}
	
	private Formula getFormula(List<List<String>> analyseList,List<CustomMethod> customMethods) {
		List<Formula> Formulas = new ArrayList<Formula>(analyseList.size());
		for (List<String> list : analyseList) {
			Formulas.add(getFormula(list, Formulas,customMethods));
		}
		return Formulas.get(Formulas.size() - 1);
	}

	private Formula getFormula(List objs, List<Formula> Formulas,List<CustomMethod> customMethods) {
		setFormula(objs, '*', '/', Formulas,customMethods);
		setFormula(objs, '+', '`', Formulas,customMethods);
		
		Formula resultFormula = null;
		if (objs.size() != 0) {
			Object obj=objs.get(0);
			
			if (obj instanceof Formula){
				resultFormula = (Formula) objs.get(0);
			}else{
				
				resultFormula =new Formula(obj, '+',"0", Formulas,customMethods);
				
				addVariableList(resultFormula);

			}
		}

		return resultFormula;

	}

	/*
	 * /*为了使公式中支持负数，使用“`”表示减号，使用“-”表示负号
	 */
	public static String replaceSubtration(String vstr) {
		String tmp = "";
		String result = "";
		int startS = vstr.indexOf("-");
		if (startS != -1) {
			if (startS > 0) {
				tmp = vstr.substring(startS - 1, startS);
				if (!"+".equals(tmp) && !"-".equals(tmp) && !"*".equals(tmp)
						&& !"/".equals(tmp) && !"(".equals(tmp)) {
					result = result + vstr.substring(0, startS) + "`";
				} else
					result = result + vstr.substring(0, startS + 1);
			} else
				result = result + vstr.substring(0, startS + 1);
			vstr = vstr.substring(startS + 1);
		}
		while (startS != -1) {
			startS = vstr.indexOf("-");
			if (startS > 0) {
				tmp = vstr.substring(startS - 1, startS);
				if (!"+".equals(tmp) && !"-".equals(tmp) && !"*".equals(tmp)
						&& !"/".equals(tmp) && !"(".equals(tmp))
					result = result + vstr.substring(0, startS) + "`";
				else
					result = result + vstr.substring(0, startS + 1);
			} else
				result = result + vstr.substring(0, startS + 1);
			vstr = vstr.substring(startS + 1);
		}
		result += vstr;
		return result;
	}
	
	
	
	public static  boolean isFormula(String formula ){
		String str=replaceSubtration(formula);
		return symbolIndexOf(str)!=-1;
	}
}
