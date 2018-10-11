package com.freework.base.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class FileUtil {
	public static  final String SYS_DIR_NAME="freeworkDir"; 
	public static String getFileSysPath(String dirName){
		 String path="";
		 if(System.getProperty( "os.name").indexOf("Windows")!=-1){
			 path="C:\\"+SYS_DIR_NAME+"\\"+dirName+"\\";
		 }else
			 path="/"+SYS_DIR_NAME+"/"+dirName+"/";
		 File file=new File(path);
		 if(!file.isDirectory()){
			 file.mkdir();
		 }
		
		return path;
	}
	
	public static int getFileSize(File file){
		if(file.exists()&&file.isFile()){
			FileInputStream fis=null;
			try {
				fis=new FileInputStream(file);
				return fis.available();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return 0;
			} catch (IOException e) {
				e.printStackTrace();
				return 0;
			}finally{
				if(fis!=null)
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}else{
			return 0;
		}
		
	}
	public static byte[] ObjectOutput(Object Object) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(10240);
		ObjectOutputStream oos;

		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(Object);

			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static Object ObjectInput(byte[] b) {

		try {
			ByteArrayInputStream input = new ByteArrayInputStream(b);
			ObjectInputStream in = new ObjectInputStream(input);
			return in.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

	}

 
	public static void copyFile2(File fileFromPath, File fileToPath) 
	 { 
	InputStream in = null; 
	OutputStream out = null; 
	try { 
	in = new FileInputStream(fileFromPath); 
	out = new FileOutputStream(fileToPath); 
	int length = in.available(); 
	int len = (length % 10240 == 0) ? (length / 10240) 
	: (length / 10240 + 1); 
	byte[] temp = new byte[10240]; 
	for (int i = 0; i < len; i++) { 
	in.read(temp); 
	out.write(temp); 
	} 
	}catch(Exception e) {
		
	} finally { 
	if (in != null)
		try {
			in.close();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} 
	if (out != null)
		try {
			out.close();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} 
	} 
	} 


	public static void copyDir(File from, File to) {
	   if (!to.exists()) {
	    to.mkdirs();
	   }
	   File[] files = from.listFiles();
	   for (int i = 0; i < files.length; i++) {
	    File file1 = files[i];
	    File file2 = new File(to.getPath() + File.separator
	      + files[i].getName());
	    if (!file1.isDirectory()) {
	     copyFile(file1, file2);
	    } else {
	     copyDir(file1, file2);
	    }
	   }

	}








	public void zipFiles(Object[] sources,OutputStream fout){ //压缩文件
		try{
		
			ZipOutputStream zout=new ZipOutputStream(fout);	//得到压缩输出流
			byte[] buf=new byte[1024];//设定读入缓冲区尺寸
			int num;
			FileInputStream fin=null;
			ZipEntry entry=null;
			for (int i=0;i<sources.length;i++){
				String filename=sources[i].toString(); //得到待压缩文件路径名
				String entryname=filename.substring(filename.lastIndexOf("\\")+1); //得到文件名
				entry=new ZipEntry(entryname); //实例化条目列表
				zout.putNextEntry(entry); //将ZIP条目列表写入输出流
				fin=new FileInputStream(filename); //从源文件得到文件输入流
				while ((num=fin.read(buf))!=-1){  //如果文件未读完
					zout.write(buf,0,num);	//写入缓冲数据
				}
				fin.close();	//关闭文件输入流
			}
			zout.close();	//关闭压缩输出流
		
			
		

		}
		catch (Exception ex){
			ex.printStackTrace();	//打印出错信息

		}
	}

	


	
	public static void zipDir(OutputStream fout,String dir,String path){
//		String file[]=getFileNames(new File(dir));
//		for (int i = 0; i < file.length; i++) {
//			System.out.println(file[i]);
//		}
		zipFiles(fout,getFileNames(new File(dir)),path);
		
	}
	
	public static final String GZipDecompress(byte[] compressed) {
		if (compressed == null)
			return null;

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		GZIPInputStream zin = null;
		String decompressed;
		try {
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new GZIPInputStream(in);
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			decompressed = null;
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

		return decompressed;
	}
	public static final byte[] decompress(byte[] compressed) {
		if(compressed == null)
		return null;
		 
		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		try {
		out = new ByteArrayOutputStream();
		in = new ByteArrayInputStream(compressed);
		zin = new ZipInputStream(in);
		ZipEntry entry = zin.getNextEntry();
		byte[] buffer = new byte[1024];
		int offset = -1;
		while((offset = zin.read(buffer)) != -1) {
		out.write(buffer, 0, offset);
		}
		return out.toByteArray();
		} catch (IOException e) {
		} finally {
		if(zin != null) {
		try {zin.close();} catch(IOException e) {}
		}
		if(in != null) {
		try {in.close();} catch(IOException e) {}
		}
		if(out != null) {
		try {out.close();} catch(IOException e) {}
		}
		}
		 
		return null;
		}
	

	public static void zipFiles(OutputStream fout,List<String> texts,List<String> filenames){ //压缩文件
		try{
		
			ZipOutputStream zout=new ZipOutputStream(fout);	//得到压缩输出流
			int num;
			ZipEntry entry=null;
			Iterator<String> filenameI=filenames.iterator();
			for (String text : texts) {
				
				String filename=filenameI.next(); //得到待压缩文件路径名
				entry=new ZipEntry(filename); //实例化条目列表
				zout.putNextEntry(entry); //将ZIP条目列表写入输出流
				zout.write(text.getBytes());	//写入缓冲数据
			}
			zout.close();	//关闭压缩输出流
		
			
		

		}
		catch (Exception ex){
			ex.printStackTrace();	//打印出错信息

		}
	}
	
	public static void main(String a[]){
		try {
			OutputStream fout=new FileOutputStream("D:/ee.zip");
			String[] sources={"D:/ttt/t1.txt","D:/ttt/t2.txt","D:/ttt/t3.txt",};
			
			zipFiles(fout,sources,"D:/ttt/");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void zipFiles(OutputStream fout,String[] sources,String path){ //压缩文件
		try{
		
			ZipOutputStream zout=new ZipOutputStream(fout);	//得到压缩输出流
			byte[] buf=new byte[1024];//设定读入缓冲区尺寸
			int num;
			FileInputStream fin=null;
			ZipEntry entry=null;
			for (int i=0;i<sources.length;i++){
				
				String filename=sources[i].toString(); //得到待压缩文件路径名
		
				String entryname=filename.substring(filename.indexOf(path)+path.length()); //得到文件名
			
				//System.out.println(entryname);
				entry=new ZipEntry(entryname); //实例化条目列表
				
				zout.putNextEntry(entry); //将ZIP条目列表写入输出流
				
				fin=new FileInputStream(filename); //从源文件得到文件输入流
				while ((num=fin.read(buf))!=-1){  //如果文件未读完
					zout.write(buf,0,num);	//写入缓冲数据
				}
				fin.close();	//关闭文件输入流
			}
			zout.close();	//关闭压缩输出流
		
			
		

		}
		catch (Exception ex){
			ex.printStackTrace();	//打印出错信息

		}
	}
	/** 
	* 
	* 解压缩文件 
	* @param inFilePath：待解压缩的文件的路径 
	* @param inFileName:输入需要解压缩的文件的文件名 
	* @param outFilePath：输出解压缩完的文件的路径  
	* @return 
	*/ 
	public static boolean uncoilZIP(String inFilePath, String inFileName,String outFilePath) { 
		int BUFFER = 2048; 
		boolean flag = false; 
		

		ZipInputStream zip = null; 
		ZipEntry entry = null; 
	    InputStream in=null;
		FileOutputStream fos =null;
		try { 
			in=new FileInputStream(inFilePath + inFileName);
			zip = new ZipInputStream(in); 
			int i = 1; 
			int start = 0; 
			while ((entry=zip.getNextEntry())!=null) { 
				if(i==1){ 
					if(entry.isDirectory()){ 
						start = entry.getName().length(); 
					} 
				} 
				if (entry.isDirectory()) { 
					new File(outFilePath + entry.getName().substring(start)).mkdirs(); 
					i++; 
					continue; 
				}else{ 
					new File(outFilePath).mkdirs(); 
					i++; 
				} 
				int count; 
				byte data[] = new byte[BUFFER]; 
				fos = new FileOutputStream(outFilePath + entry.getName().substring(start)); 
				while ((count = zip.read(data, 0, BUFFER)) != -1) { 
					fos.write(data, 0, count); 
				} 
				fos.flush(); 
				fos.close();
			} 
			flag = true; 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} finally { 
			try { 
			  if(zip!=null)
				  zip.close();
			  if(in!=null)
				  in.close();
			 
			} catch (Exception e) { 
				e.printStackTrace(); 
			} 
		} 

		return flag; 
	} 

	public static boolean uncoilZIP(InputStream in,String outFilePath) { 
		int BUFFER = 2048; 
		boolean flag = false; 
		

		ZipInputStream zip = null; 
		ZipEntry entry = null; 
		FileOutputStream fos =null;
		try { 
			zip = new ZipInputStream(in); 
			int i = 1; 
			int start = 0; 
		
			while ((entry=zip.getNextEntry())!=null) { 
				System.out.println(entry);
				if(i==1){ 
					if(entry.isDirectory()){ 
						start = entry.getName().length(); 
					} 
				} 
				if (entry.isDirectory()) { 
					new File(outFilePath + entry.getName().substring(start)).mkdirs(); 
					i++; 
					continue; 
				}else{ 
					new File(outFilePath).mkdirs(); 
					i++; 
				} 
				int count; 
				byte data[] = new byte[BUFFER]; 
				fos = new FileOutputStream(outFilePath + entry.getName().substring(start)); 
				while ((count = zip.read(data, 0, BUFFER)) != -1) { 
					fos.write(data, 0, count); 
				} 
				fos.flush(); 
				fos.close();
			} 
			flag = true; 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} finally { 
			try { 
			  if(zip!=null)
				  zip.close();
			  if(in!=null)
				  in.close();
			 
			} catch (Exception e) { 
				e.printStackTrace(); 
			} 
		} 

		return flag; 
	} 

	public static void copyFile(File src, File dest) {

		  
		try {
			FileChannel	srcChannel = new FileInputStream(src).getChannel();
		
		   FileChannel  dstChannel  = new FileOutputStream(dest).getChannel();
		   dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
		      
	     
	       srcChannel.close();
	       dstChannel.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  

	}

	public static long getDirSize(String dir) {   
		return  getDirSize(new File(dir));
	}

	public static long getDirSize(File dir) {   
		   if (dir == null) {   
		       return 0;   
		   }   
		   if (!dir.isDirectory()) {   
		       return 0;   
		   }   
		   long dirSize = 0;   
		   File[] files = dir.listFiles();   
		    for (File file : files) {   
		        if (file.isFile()) {   
		            dirSize += file.length();   
		        } else if (file.isDirectory()) {   
		            dirSize += file.length();   
		            dirSize += getDirSize(file); // 如果遇到目录则通过递归调用继续统计   
		        }   
		    }   
		    return dirSize;   
		}  
	
	
	
	private static List<String> getFileName(File dir,FileFilter filter) {
		List<String> filenames=new LinkedList<String>();
		 if (!dir.isDirectory()) {   
			 return null;
		 }
		   File[] files = dir.listFiles(filter);  
		   if(files==null)
			   return null;
		   for (File file : files) {   
			  
		        if (file.isFile()) {   
		        	filenames.add(file.getAbsolutePath());
		        } else if (file.isDirectory()) {   
		        	List<String> list=getFileName(file,filter);
		        	if(list!=null)
		        	filenames.addAll(list);   
		        }   
		    }   
		    return filenames;   
	 
		
	} 
	private static List<String> getFileName(File dir) {
		List<String> filenames=new ArrayList<String>();
		 if (!dir.isDirectory()) {   
			 return null;
		 }
		   File[] files = dir.listFiles();   
		   for (File file : files) {   
		        if (file.isFile()) {   
		        	filenames.add(file.getAbsolutePath());
		        } else if (file.isDirectory()) {   
		          
		        	filenames.addAll(getFileName(file));   
		        }   
		    }   
		    return filenames;   
	 
		
	} 
	public static String[] getFileNames(File dir) {
		List<String> filenames= getFileName(dir);

		return filenames.toArray(new String[filenames.size()]);
		
	}
	public static String[] getFileNames(File dir,FileFilter filter) {
		List<String> filenames= getFileName(dir,filter);

		return filenames.toArray(new String[filenames.size()]);
		
	}
	
	public static boolean outTxtFile(String filename,String txt){
		FileOutputStream  out  =null;
		   try {
			   out= new FileOutputStream(filename);
			   out.write(txt.getBytes());
			   return true;
		   } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				 return false;
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
		   try {
			   out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	
		
	}
	
	public static void outFile(InputStream in,File file){
		FileOutputStream  out= null;
		 byte[] bytes=new byte[512];
		try {
			  out= new FileOutputStream(file);
			 while( in.read(bytes)!=-1){
				 out.write(bytes);
				 
			 }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			try {
				if(out!=null)
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(in!=null)
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static String readTxtFile(InputStream input,String source) {
		 byte[] bytes=null;
		 try {
			bytes= new byte[input.available()];
			input.read(bytes);
		    } catch (IOException e) {	e.printStackTrace();}
		    finally {
		    	try {
		    	 if(input!=null)
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		   try {
			return bytes!=null?new String(bytes,source):"";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String readTxtFile(String filepath,String source) {
		 byte[] bytes=null;
		 FileInputStream input=null;
		 try {
			  input=	new FileInputStream(new File(filepath));
			bytes= new byte[input.available()];
			input.read(bytes);
		    } catch (IOException e) {	e.printStackTrace();}
		    finally {
		    	try {
		    	 if(input!=null)
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		   try {
			return bytes!=null?new String(bytes,source):"";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
	public static final String decompressGZIP(byte[] compressed) {
		if (compressed == null)
			return null;

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		GZIPInputStream zin = null;
		String decompressed;
		try {
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new GZIPInputStream(in);
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			decompressed = null;
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

		return decompressed;
	}
	public static void deleteFile(File file){ 
		   if(file.exists()){ 
		    if(file.isFile()){ 
		     file.delete(); 
		    }else if(file.isDirectory()){ 
		     File files[] = file.listFiles(); 
		     for(int i=0;i<files.length;i++){ 
		      deleteFile(files[i]); 
		     } 
		    } 
		    file.delete(); 
		   }else{ 
		    System.out.println("所删除的文件不存在！"+'\n'); 
		   } 
		}
}
