package com.freework.queryData.compileSQL.simpleSQL;

import java.util.List;






import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import com.freework.queryData.compileSQL.CompileSQL;
import com.freework.queryData.compileSQL.CompileSQLFactory;

public class SimpleCmpleSQLFty implements CompileSQLFactory,InitializingBean {
	private SQLPartAnalyze analyze=new SQLPartAnalyze();
	
	public SimpleCmpleSQLFty get(){
		return this;
	}
	private ApplicationContext context;
	@Resource()
	public void setContext(ApplicationContext context) {
		this.context = context;
		
	}
	@Override
	public CompileSQL getCompileSQL(String sourceSql) {
		return new SimpleCmpleSQL(sourceSql,analyze);
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		try{
		List<RegisterSQLPart> registerSQLList=	(List<RegisterSQLPart>) context.getBean("queryData.registerSQLList");
		System.out.println(registerSQLList);
		analyze.setRegisterSQLList(registerSQLList);
		}catch(Exception e){
		}
	}

}
