package com.freework.queryData.compileSQL.simpleSQL;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.freework.base.util.SqlUtil;
public class SQLPart implements ISQLPart {
	private List<String> paramNames = null;
	private String targetSql = "";
	public SQLPart(String sourceSql) {
		paramNames = SqlUtil.getSqlParameter(sourceSql, PARAM_START_CHAR,PARAM_END_CHAR);
		targetSql=sourceSql;
		if(paramNames.size()==0){
			paramNames=null;
		}else{
			for (String paramName : paramNames) {
				targetSql = targetSql.replace(PARAM_START_CHAR + paramName+ PARAM_END_CHAR, "?");
			}
		}
	}

	@Override
	public void addSqlAndParam(Object uvalue,List<Object> params,StringBuilder sql){
		if (paramNames!=null&&paramNames.size() != 0) {
				for (String name : paramNames) {
					Object value=null;
					value=getParamValue(name,uvalue);
					params.add(value);
			}
		}
			sql.append(targetSql);


	}

	public static Object getParamValue(String name,Object obj){
		if(obj instanceof Map){
			return ((Map)obj).get(name);
		}else{
			try {
				return PropertyUtils.getProperty(obj, name);
			} catch (IllegalAccessException e) {
				//e.printStackTrace();
			} catch (InvocationTargetException e) {
				//e.printStackTrace();
			} catch (NoSuchMethodException e) {
				//e.printStackTrace();
			}

			
		}
		return null;
		
		
	}
	
	public String  toString(){
		return targetSql;
	}
	@Override
	public List<String> getParamNames() {
		return paramNames;
	}

	

}
