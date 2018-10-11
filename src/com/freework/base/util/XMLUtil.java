package com.freework.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

public class XMLUtil {
	private static final String DEFAULT_ENCODING = "UTF-8";   

    /**
     *  将传入的xml文件，转化成Object 
     * @param filepath
     * @param objectFactoryClass
     * @return
     * @throws JAXBException
     */
	   public static <T> T xmlToObject (File file,Class<T> objectFactoryClass) throws JAXBException {  
	            // 1. 生成JAXBContex对象   
	            JAXBContext context = JAXBContext.newInstance(objectFactoryClass);  
	            // 2. 生成Unmarsaller对象   
	            Unmarshaller unmarshaller = context.createUnmarshaller();  
	            // 3. Unmarsaller   
	         
	            T obj = (T) unmarshaller.unmarshal(file);  
	            return obj;
	        
	    }  
	   
	   public static void objectToXml(Class objectFactoryClass, final String xmlPath, String encoding,Object obj) throws JAXBException, IOException{  
		   objectToXml(objectFactoryClass,   new  FileOutputStream(new File(xmlPath)), encoding, obj);
	    }  
	   
	   public static void objectToXml(Class objectFactoryClass,  Writer out, String encoding,Object obj) throws JAXBException, IOException{  
           JAXBContext context = JAXBContext.newInstance(objectFactoryClass);  

           Marshaller m = context.createMarshaller();  
           //格式化输出  
           m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);  
           //编码  
           encoding = encoding == null ? DEFAULT_ENCODING : encoding;  
           //设置输出的编码格式  
           m.setProperty(Marshaller.JAXB_ENCODING, encoding);  
           m.marshal(obj, out);  
         
   }  
	   public static void objectToXml(Class objectFactoryClass,  OutputStream out, String encoding,Object obj) throws JAXBException, IOException{  
           JAXBContext context = JAXBContext.newInstance(objectFactoryClass);  

           Marshaller m = context.createMarshaller();  
           //格式化输出  
           m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);  
           //编码  
           encoding = encoding == null ? DEFAULT_ENCODING : encoding;  
      
           //设置输出的编码格式  
           m.setProperty("jaxb.encoding", encoding);  
          try{
           m.marshal(obj, out);  
       		out.flush();  
          }finally{
	            out.close();
          }
      
   }  
	   public static void MapToXml( final String xmlPath,String rootName, String encoding,Map map){
	        Document document = DocumentHelper.createDocument();  
	        Element  root=document.addElement(rootName);
	        MapToXml(root,map);
	        XMLWriter xmlWriter =null;
			try {
				FileOutputStream fileWriter = new FileOutputStream(xmlPath);
				OutputFormat  xmlFormat = new OutputFormat();  
	            xmlFormat.setEncoding(encoding);  
	            //创建写文件方法  
	             xmlWriter = new XMLWriter(fileWriter,xmlFormat);  
	            //写入文件  
	            xmlWriter.write(document);  
	            //关闭  
	            xmlWriter.close();  
			} catch (IOException e) {
				e.printStackTrace();
			}  finally{
				if(xmlWriter!=null)
					try {
						xmlWriter.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				
			}
            //设置文件编码  
        
		   
	   }
	   public static  Map<String,Object> attributeToMap(Element element){
		  List<Attribute> list=element.attributes();
		  Map<String,Object> map=new HashMap<String,Object>();
		  for (Attribute attribute : list) {
			  map.put(attribute.getName(), attribute.getData());
		  }
		  return map;
	   }
	   
	   
	   private static void addchildren(Map<String,Object> map,List<Element> list){
		   if(list.size()>1){
			   for (Element element2 : list) {
				
				 Object value=map.get(element2.getName());
				   	if(value==null){
				   		map.put(element2.getName(), elementToMap(element2));
				   	}else{
					   	List<Map> list2=null;
				   		if(value instanceof Map){
				   			list2=new LinkedList<Map>();
				   			list2.add((Map) value);
				   			map.put(element2.getName(),list2);
				   		}else{
				   			list2=(List<Map>) value;
				   		}
					   	list2.add(elementToMap(element2));
				   		
				   	}
			   }
			   
		   }else if(list.size()==1){
			   
			   	Element element=list.get(0);
			   	String name=element.getName();
			   	if(name.substring(name.length()-1).equals("s"))
			   		map.put(element.getName(),elementToMap(element));
			   	else
			   		map.put(element.getName(),Arrays.asList(elementToMap(element)));

			   
		   }
		   
	   }
	   public static  Map<String,Object> xmlToMap(File file,String encoding) throws IOException{
		   Document document=XMLUtil.readDocumentFromFile(file, encoding);
			Element root=document.getRootElement();
		
		   return elementToMap(root);
	   }
	   
	   
	   public static  Map<String,Object> elementToMap(Element element){
		   Map<String,Object> map=null;
		   if(element.attributeCount()>0)
			   map=attributeToMap(element);
		   else
			   map=new HashMap<String,Object>();
		   
		   List<Element> list=element.elements();
		   if(list.size()>0){
			   addchildren(map,list);
		   }
		return map;
			 
	   }
	    
	   
	   
	   
	   public static void ListToXml(Element element,List<Map> list,String name){
		   for (Map map : list) {
			   int index=name.indexOf('_');
			   if(index>0){
				   String upname=name.substring(0,index);
				   name=name.substring(index+1);
				   element= element.addElement(upname);
			   }
			   Element element2=element.addElement(name);
			   MapToXml(element2,map);
		   	}
		   
	   }
	   
	  
	 
	   
	   public static void MapToXml(Element element,Map map){
		   Set<Map.Entry> set= map.entrySet();
		   for (Map.Entry entry : set) {
			   
			   String name=entry.getKey().toString();
			   Object value=entry.getValue();
			   if(value instanceof List){
					   ListToXml(element,(List<Map>) value,name);
			   }else   if(value instanceof Map){
				   MapToXml(element.addElement(name),(Map) value);
			   }else  if(value!=null){
					element.addAttribute(name, value.toString());
			   }
		   }
		   
	   }
	   
	   
	   
	   
	   public static void objectToXml( final String xmlPath, String encoding,Object obj,Class... objectFactoryClass) throws JAXBException, IOException{  
           JAXBContext context = JAXBContext.newInstance(objectFactoryClass);  

           Marshaller m = context.createMarshaller();  
           //格式化输出  
           m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);  
           //编码  
           encoding = encoding == null ? DEFAULT_ENCODING : encoding;  
      
           //设置输出的编码格式  
           m.setProperty("jaxb.encoding", encoding);  
           OutputStream out = new  FileOutputStream(new File(xmlPath));  
          try{
           m.marshal(obj, out);  
       	out.flush();  
          }finally{
	            out.close();
          }
      
   }  

	public static Document readDocumentFromFile(String filePath, String encoding)
			throws IOException {
		return readDocumentFromFile(new File(filePath), encoding);
	}

	public static Document readDocumentFromFileStr(String text)
			throws IOException {
		try {
			return DocumentHelper.parseText(text);
		} catch (DocumentException e) {

			e.printStackTrace();
			return null;
		}
	}

	public static Document readDocumentFromFile(File file,String encoding) throws IOException {
		
			return readDocumentFromFile(new FileInputStream(file),encoding);
		
	}
	public static Document readDocumentFromFile(InputStream fileInput,String encoding) throws IOException {
		try {
			// if (fileExist(filePath))
			// return null;
			SAXReader builder = new SAXReader(false);
			builder.setEncoding(encoding);
			Document FileDocument = builder.read(fileInput);
			
			return FileDocument;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
	}
	public static void addContent(Element e, String elementName, String value) {
		
		e.addElement(elementName).attributeValue("VAL", value);

	}

	public static void setAttribute(Element e, String elementName, String value) {
		
		e.element(elementName).attributeValue("VAL", value);
	}

	public static String getAttributeValue(Element e, String elementName,
			String nameValue) {
		if (elementName == null)
			return "";
		return getAttributeValue(e.element(elementName), nameValue);

	}

	public static String getAttributeValue(Element e, String nameValue) {
		if (e == null)
			return "";
		return e.attributeValue(nameValue);
	}

	public static void outputDocumentToFile(Document document,
			String encodingMode) throws IOException {
		Writer out = new OutputStreamWriter(new FileOutputStream(document
				.getPath()), encodingMode);
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(out, format);
		writer.write(document);

		out.close();
	}

	public static org.w3c.dom.Document parse(Document doc) throws ParserConfigurationException, SAXException, IOException {
		if (doc == null) {
			return (null);
		}
		java.io.StringReader reader = new java.io.StringReader(doc.asXML());
		org.xml.sax.InputSource source = new org.xml.sax.InputSource(reader);
		javax.xml.parsers.DocumentBuilderFactory documentBuilderFactory = javax.xml.parsers.DocumentBuilderFactory
				.newInstance();
		javax.xml.parsers.DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();
		return (documentBuilder.parse(source));
	}

}
