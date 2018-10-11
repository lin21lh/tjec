package com.jbf.common.ronline;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.jbf.common.util.WebContextFactoryUtil;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.paramCfg.component.impl.ParamCfgComponentImpl;

 
public class DocConverter {
    private static int environment;// 环境1：windows,2:linux(涉及pdf2swf路径问题)
    private String fileString;
    private static String outputPath;;// 输入路径，如果不设置就输出在默认位置
    private String fileName;
    private File pdfFile;
    private File swfFile;
    private File docFile;
    private static String SWFToolsPath="";
 
    public DocConverter(String docPath,String pdfAndSwfPath) {
        ini(docPath,pdfAndSwfPath);
    }
    
   static{
	   ParamCfgComponent c = (ParamCfgComponentImpl)WebContextFactoryUtil.getBean("sys.paramCfg.component.ParamCfgComponentImpl");
	   SWFToolsPath = c.findGeneralParamValue("SYSTEM","SWFTOOLPATH");
	   String systemStr = System.getProperties().getProperty("os.name").toLowerCase();
	   if(systemStr.contains("Windows".toLowerCase())){
		   environment = 1;
	   }else if(systemStr.contains("Linux".toLowerCase())){
		   environment = 2;
	   }else{
		   environment=1;
	   }
	   System.out.println("【运行环境】"+systemStr);
   }
    /*
     * 重新设置 file @param fileString
     */
    public void setFile(String docPath,String pdfAndSwfPath) {
        ini(docPath,pdfAndSwfPath);
    }
 
    /*
     * 初始化 @param fileString
     */
    private void ini(String docPath,String pdfAndSwfPath) {
    	
    	//String swfpath = PropertyUtils.getInstance().getPropertyValue(fileString, strKey);
        this.fileString = docPath;
        fileName = pdfAndSwfPath.substring(0, pdfAndSwfPath.lastIndexOf("."));
        docFile = new File(fileString);
        pdfFile = new File(fileName+".pdf");
        swfFile = new File(fileName+".swf");
    }
 
    /*
     * 转为PDF @param file
     */
    private void doc2pdf() throws Exception {
        if (docFile.exists()) {
            if (!pdfFile.exists()) {
                OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
                try {
                    connection.connect();
                    DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
                    converter.convert(docFile, pdfFile);
                    // close the connection
                    connection.disconnect();
                    System.out.println("【在线预览】pdf转换成功，PDF输出：" + pdfFile.getPath());
                } catch (java.net.ConnectException e) {
                    // ToDo Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("【在线预览】swf转换异常，openoffice服务未启动！");
                    throw e;
                } catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {
                    e.printStackTrace();
                    System.out.println("【在线预览】swf转换器异常，读取转换文件失败");
                    throw e;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            } else {
                System.out.println("【在线预览】已经转换为pdf，不需要再进行转化");
            }
        } else {
            System.out.println("【在线预览】swf转换器异常，需要转换的文档不存在，无法转换");
        }
    }
 
    /*
     * 转换成swf
     */
    private void pdf2swf() throws Exception {
        Runtime r = Runtime.getRuntime();
        Process  p= null;
        if (!swfFile.exists()) {
            if (pdfFile.exists()) {
                if (environment == 1)// windows环境处理
                {
                    try {
                        // 这里根据SWFTools安装路径需要进行相应更改
                    	if("".equals(SWFToolsPath) || null==SWFToolsPath){
                    		throw new Exception("请检查swftool的安装路径是否配置正确！");
                    	}
                    	p = r.exec(SWFToolsPath+"pdf2swf.exe " + pdfFile.getPath() + " -o " + swfFile.getPath() + " -T 9");
                        List command=new ArrayList();
       				 	command.add(SWFToolsPath+"pdf2swf.exe ");
       				 	command.add("\""+ pdfFile.getPath()+"\"");
       				 	command.add("\""+swfFile.getPath()+"\"");
       				 	command.add("-T");
       				 	command.add("9");
       				 	p=new ProcessBuilder(command).start();
       				 	
                        System.out.print(loadStream(p.getInputStream()));
                        System.err.print(loadStream(p.getErrorStream()));
                        System.out.print(loadStream(p.getInputStream()));
                        System.err.println("【在线预览】swf转换成功，文件输出：" + swfFile.getPath());
                        if (pdfFile.exists()) {
                            pdfFile.delete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception("请检查swftool的安装路径是否配置正确！");
                    }
                } else if (environment == 2)// linux环境处理
                {
                    try {
                    	p = r.exec("pdf2swf " + pdfFile.getPath() + " -o " + swfFile.getPath() + " -T 9");
                    	
                    	List command=new ArrayList();
       				 	command.add("pdf2swf");
       				 	command.add("\""+ pdfFile.getPath()+"\"");
       				 	command.add("\""+swfFile.getPath()+"\"");
       				 	command.add("-T");
       				 	command.add("9");
       				 	p=new ProcessBuilder(command).start();
       				 	
                        System.out.print(loadStream(p.getInputStream()));
                        System.err.print(loadStream(p.getErrorStream()));
                        System.err.println("【在线预览】swf转换成功，文件输出：" + swfFile.getPath());
                        if (pdfFile.exists()) {
                            pdfFile.delete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception("请检查swftool的安装路径是否配置正确！");
                    }
                }
            } else {
                System.out.println("【在线预览】pdf不存在，无法转换");
            }
        } else {
            System.out.println("【在线预览】swf已存在不需要转换");
        }
    }
 
    static String loadStream(InputStream in) throws IOException {
        int ptr = 0;
        //把InputStream字节流 替换为BufferedReader字符流 2013-07-17修改
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder buffer = new StringBuilder();
        while ((ptr = reader.read()) != -1) {
            buffer.append((char) ptr);
        }
        return buffer.toString();
    }
 
    /*
     * 转换主方法
     */
    public String conver() {
        if (swfFile.exists()) {
            System.out.println("【在线预览】swf转换器开始工作，该文件已经转换为swf");
            return swfFile.getPath();
        }
 
        if (environment == 1) {
            System.out.println("【在线预览】swf转换器开始工作，当前设置运行环境windows");
        } else {
            System.out.println("【在线预览】swf转换器开始工作，当前设置运行环境linux");
        }
 
        try {
            doc2pdf();
            pdf2swf();
            return swfFile.getPath();
        } catch (Exception e) {
            // TODO: Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }
 
    /*
     * 返回文件路径 @param s
     */
    public String getswfPath() {
        if (swfFile.exists()) {
            String tempString = swfFile.getPath();
            tempString = tempString.replaceAll("\\\\", "/");
            return tempString;
        } else {
            return "";
        }
    }
 
    /*
     * 设置输出路径
     */
    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
        if (!outputPath.equals("")) {
            String realName = fileName.substring(fileName.lastIndexOf("/"), fileName.lastIndexOf("."));
            if (outputPath.charAt(outputPath.length()) == '/') {
                swfFile = new File(outputPath + realName + ".swf");
            } else {
                swfFile = new File(outputPath + realName + ".swf");
            }
        }
    }
    public static void main(String s[]) {
        DocConverter d = new DocConverter("C:\\Users\\liujb\\Desktop\\8-核算内容 (1).txt","C:\\Users\\liujb\\Desktop\\8-核算内容 (1).swf");
        d.conver();
    }
}