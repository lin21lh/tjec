package com.wfzcx.aros.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.util.Random;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @ClassName: UtilTool
 * @Description: 工具类
 * @author ybb
 * @date 2016年8月23日 下午1:54:34
 * @version V1.0
 */
public class UtilTool {

	/**
	 * @Title: objToXml
	 * @Description: 将对象转化为xml字符串
	 * @author ybb
	 * @date 2016年8月23日 下午1:55:36
	 * @param obj
	 * @return
	 */
	public static String objToXml(Object obj) {
		XStream xstream = new XStream();
		return xstream.toXML(obj);
	}
	
	/**
	 * @Title: xmlToObj
	 * @Description: 将xml字符串转换成bean
	 * @author ybb
	 * @date 2016年8月23日 下午1:56:35
	 * @param xml
	 * @return
	 */
	public static Object xmlToObj(String xml){
		XStream xstream = new XStream(new DomDriver());
		return xstream.fromXML(xml);
	}
	
	/**
	 * 将字CLOB转成STRING类型 
	 * @param clob
	 * @return String
	 * @author 张田田
	 */
	public static String clobToString(Clob clob)
	{ 
        String reString = ""; 
        Reader is = null;
        BufferedReader br = null;
        
        StringBuffer sb = new StringBuffer(); 
		try
		{
			is = clob.getCharacterStream();
			br = new BufferedReader(is); 
			String getVal = br.readLine(); 
			// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING 
			while (getVal != null)
			{
				sb.append(getVal); 
				getVal = br.readLine(); 
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (null != br)
				{
					br.close();
				}
				if (null != is)
				{
					is.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		// 得到流 
        reString = sb.toString(); 
        return reString; 
    }
	
	/**
	 * @Title: getRandomString
	 * @Description: 生成随机数
	 * @author ybb
	 * @date 2016年9月9日 下午4:44:22
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) { //length表示生成字符串的长度
		
	    String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";   
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < length; i++) {   
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }   
	    
	    return sb.toString();   
	 }   

}
