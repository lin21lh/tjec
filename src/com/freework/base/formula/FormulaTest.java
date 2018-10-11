package com.freework.base.formula;

import java.util.HashMap;
import java.util.Map;
public class FormulaTest {

	public static void main(String a[]){
		
		
		
		FormulaAnalyse t=new FormulaAnalyse("a+1+b+2*c+$if{e>0,1,0}");//公式解析     $if{e>0,1,0}判断函数

		Formula formula=t.getFormula();//获得公式对象，最好将解析后的公式对象缓存下来。
		Map<String,Number> map=new HashMap<String,Number>();
		map.put("a", 1);
		map.put("b", 2);
		map.put("c", 3);
		map.put("e", 1);
		System.out.println(	formula.calculateNumber(map));
		map.put("e", 0);
		System.out.println(	formula.calculateNumber(map));
		int size=100000;
		long starttime=System.currentTimeMillis();
		for (int i = 0; i <size; i++) {
			formula.calculateNumber(map);
		}
		System.out.println(size+"次执行时间:"+(System.currentTimeMillis()-starttime));

	}
}
