package com.freework.base.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class FileUtil2 {
	public static boolean CreateFile(String destFileName) {
	    File file = new File(destFileName);
	    if (file.exists()) {
	     System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
	     return false;
	    }
	    if (destFileName.endsWith(File.separator)) {
	     System.out.println("创建单个文件" + destFileName + "失败，目标不能是目录！");
	     return false;
	    }
	    if (!file.getParentFile().exists()) {
	     System.out.println("目标文件所在路径不存在，准备创建。。。");
	     if (!file.getParentFile().mkdirs()) {
	      System.out.println("创建目录文件所在的目录失败！");
	      return false;
	     }
	    }
	    // 创建目标文件
	    try {
	     if (file.createNewFile()) {
	      System.out.println("创建单个文件" + destFileName + "成功！");
	      return true;
	     } else {
	      System.out.println("创建单个文件" + destFileName + "失败！");
	      return false;
	     }
	    } catch (IOException e) {
	     e.printStackTrace();
	     System.out.println("创建单个文件" + destFileName + "失败！");
	     return false;
	    }
	}


	public static boolean createDir(String destDirName) {
	    File dir = new File(destDirName);
	    if(dir.exists()) {
	     System.out.println("创建目录" + destDirName + "失败，目标目录已存在！");
	     return false;
	    }
	    if(!destDirName.endsWith(File.separator))
	     destDirName = destDirName + File.separator;
	    // 创建单个目录
	    if(dir.mkdirs()) {
	     System.out.println("创建目录" + destDirName + "成功！");
	     return true;
	    } else {
	     System.out.println("创建目录" + destDirName + "成功！");
	     return false;
	    }
	}


	public static String createTempFile(String prefix, String suffix, String dirName) {
	    File tempFile = null;
	    try{
	    if(dirName == null) {
	     // 在默认文件夹下创建临时文件
	     tempFile = File.createTempFile(prefix, suffix);
	     return tempFile.getCanonicalPath();
	    }
	    else {
	     File dir = new File(dirName);
	     // 如果临时文件所在目录不存在，首先创建
	     if(!dir.exists()) {
	      if(!FileUtil2.createDir(dirName)){
	       System.out.println("创建临时文件失败，不能创建临时文件所在目录！");
	       return null;
	      }
	     }
	     tempFile = File.createTempFile(prefix, suffix, dir);
	     return tempFile.getCanonicalPath();
	    }
	    } catch(IOException e) {
	     e.printStackTrace();
	     System.out.println("创建临时文件失败" + e.getMessage());
	     return null;
	    }
	}
	
	   /**
		 * 读取文件内容
		 * 
		 * @param filePathAndName
		 *            String 如 c:\\1.txt 绝对路径
		 * @return boolean
		 */
	public static String readFile(String filePathAndName) {
		String fileContent = "";
		try {
			File f = new File(filePathAndName);
			if (f.isFile() && f.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(f), "UTF-8");
				BufferedReader reader = new BufferedReader(read);
				String line;
				while ((line = reader.readLine()) != null) {
					fileContent += line;
				}
				read.close();
			}
		} catch (Exception e) {
			System.out.println("读取文件内容操作出错");
			e.printStackTrace();
		}
		return fileContent;
	}

	public static void writeFile(String filePathAndName, String fileContent) {
		try {
			File f = new File(filePathAndName);
			if (!f.exists()) {
				CreateFile(filePathAndName);
				//f.createNewFile();
			}
			OutputStreamWriter write = new OutputStreamWriter(
					new FileOutputStream(f), "UTF-8");
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(fileContent);
			writer.close();
		} catch (Exception e) {
			System.out.println("写文件内容操作出错");
			e.printStackTrace();
		}
	} 

	public static void main(String[] args) {
	    // 创建目录
	    String dirName = "c:/test/test0/test1";
// CreateFileUtil.createDir(dirName);
	    // 创建文件
	    String fileName = dirName + "/test2/testFile.txt";
	    FileUtil2.CreateFile(fileName);
	    // 创建临时文件
	    String prefix = "temp";
	    String suffix = ".txt";
	    for(int i = 0; i < 10; i++) {
	     System.out.println("创建了临时文件:" + FileUtil2.createTempFile(prefix, suffix, dirName));
	    }
	  
	}
}
