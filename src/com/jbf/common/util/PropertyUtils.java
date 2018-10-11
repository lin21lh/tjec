package com.jbf.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;



public class PropertyUtils {
	private static PropertyUtils  instance = null; // 创建对象
	private static Hashtable<String,Properties> table = new Hashtable<String,Properties>(); //静态对象初始化
	
	private PropertyUtils(){
			super();
	}
	
	/**
	 * 
	 * <b>功能描述: 初始化对象实例</b>
	 * <p>
	 * <pre></pre>
	 * @version 创建时间： 2012-2-6下午04:07:40
	 */
	public static PropertyUtils getInstance(){
			if (instance == null){
					return new PropertyUtils(); 
			}
			return instance;
	}
	
	/**
	 * 
	 * <b>功能描述: 通过文件名称获取文件实例</b>
	 * <p>
	 * <pre></pre>
	 * @version 创建时间： 2012-2-6下午04:22:14
	 */
	public Properties getProperty(String fileName){
			if (fileName==null || "".equals(fileName)){
					System.err.println("文件名称不能为空");
					return null;
			}
			Properties resultProp = table.get(fileName);
			if (resultProp == null){
					InputStream input = null;
					
					try {
						input = PropertyUtils.class.getResourceAsStream(fileName);
						resultProp = new Properties();
						resultProp.load(input);
						table.put(fileName, resultProp);
					} catch (IOException e) {
						e.printStackTrace();
					}finally{
						if (input != null){
								try {
									input.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
								input = null;
						}
					}
					
			}
			return resultProp;
	}
	
	 /**
	  * 
	  * <b>功能描述:获取文件属性</b>
	  * <p>
	  * <pre></pre>
	  * @version 创建时间： 2012-2-6下午04:22:55
	  */
	public String getPropertyValue(String fileName,String strKey){
		Properties prop = getProperty(fileName);
		try {
				return (String)prop.get(strKey);
		} catch (RuntimeException e) {
				e.printStackTrace();
		}
		return "";
	}
	
	public static void main(String[] args) {
		System.out.println(PropertyUtils.getInstance().getPropertyValue("/sys.properties", "kq.fo.batch.settlement.accountBalance.url"));
	}
}
